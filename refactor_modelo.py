import os

# --- MODELO.JAVA ---
modelo_path = 'cliente/MVC/Modelo.java'
with open(modelo_path, 'r', encoding='utf-8') as f:
    modelo_content = f.read()

# Replace PROLOG_FILE path
modelo_content = modelo_content.replace(
    'src/cliente/ArchivosExtra/BasedeConocimiento.pl',
    'motor_prolog/api/interfaz_java.pl'
)

# Add HECHOS_FILE and REGLAS_FILE
modelo_content = modelo_content.replace(
    '    private static final String PROLOG_FILE =',
    '    private static final String HECHOS_FILE = System.getProperty("user.dir").replace("\\\\", "/") + "/motor_prolog/conocimiento/hechos.pl";\n' +
    '    private static final String REGLAS_FILE = System.getProperty("user.dir").replace("\\\\", "/") + "/motor_prolog/conocimiento/reglas_actividad.pl";\n' +
    '    private static final String PROLOG_FILE ='
)

# Remove "cualquiera" logic from obtenerPlaylist and support tabs
old_obtener_playlist = '''    public List<String[]> obtenerPlaylist(String emocion, String actividad,
                                          String energia, String idioma) {
        List<String[]> playlist = new ArrayList<String[]>();
        try {
            String consulta = "buscar_avanzado(" +
                emocion    + ", " +
                actividad  + ", " +
                energia    + ", " +
                idioma     + ", Titulo, Artista, GenerosStr)";

            Query q = new Query(consulta);
            if (q.hasSolution()) {
                for (Map<String, Term> sol : q.allSolutions()) {
                    String titulo  = sol.get("Titulo")
                        .toString().replace("'", "");
                    String artista = sol.get("Artista")
                        .toString().replace("'", "");
                    String genStr  = sol.get("GenerosStr")
                        .toString().replace("'", "");
                    playlist.add(new String[]{titulo, artista, genStr});
                }
            }
        } catch (Exception ex) {
            System.err.println("Error en obtenerPlaylist: " + ex.getMessage());
        }
        return playlist;
    }'''

new_obtener_playlist = '''    public List<String[]> obtenerPlaylist(String tipo, String arg1, String arg2) {
        List<String[]> playlist = new ArrayList<String[]>();
        try {
            String consulta = "";
            if (tipo.equals("actividad")) {
                consulta = "buscar_por_actividad('" + arg1 + "', Titulo, Artista, GenerosStr)";
            } else if (tipo.equals("emocion")) {
                consulta = "buscar_por_emocion('" + arg1 + "', Titulo, Artista, GenerosStr)";
            } else if (tipo.equals("perfil")) {
                consulta = "buscar_por_idioma_energia('" + arg1 + "', '" + arg2 + "', Titulo, Artista, GenerosStr)";
            }

            Query q = new Query(consulta);
            if (q.hasSolution()) {
                for (Map<String, Term> sol : q.allSolutions()) {
                    String titulo  = sol.get("Titulo").toString().replace("'", "");
                    String artista = sol.get("Artista").toString().replace("'", "");
                    String genStr  = sol.get("GenerosStr").toString().replace("'", "");
                    playlist.add(new String[]{titulo, artista, genStr});
                }
            }
        } catch (Exception ex) {
            System.err.println("Error en obtenerPlaylist: " + ex.getMessage());
        }
        return playlist;
    }'''

modelo_content = modelo_content.replace(old_obtener_playlist, new_obtener_playlist)

# Modify consultarCancionActiva to use api_consultar
# It already uses api_consultar, so we leave it.

# Update agregarCancion (remove file I/O)
old_agregar_cancion = '''    public boolean agregarCancion(String id, String titulo,
                                   String artista, String generos,
                                   String idioma) {
        try {
            // Agregar en memoria con JPL
            String consulta = "api_agregar_cancion(" +
                id + ", '" + titulo + "', '" + artista + "', " +
                generos + ", " + idioma + ")";
            Query q = new Query(consulta);
            boolean ok = q.hasSolution();

            // Escribir físicamente en el archivo
            if (ok) {
                String linea = "cancion(" + id + ", '" + titulo +
                    "', '" + artista + "', " + generos +
                    ", " + idioma + ").";
                escribirHechoEnArchivo(linea);
            }
            return ok;
        } catch (Exception ex) {
            System.err.println("Error en agregarCancion: " + ex.getMessage());
            return false;
        }
    }'''

new_agregar_cancion = '''    public boolean agregarCancion(String id, String titulo,
                                   String artista, String generos,
                                   String idioma) {
        try {
            String consulta = "api_agregar_cancion(" +
                id + ", '" + titulo + "', '" + artista + "', " +
                generos + ", " + idioma + ")";
            Query q = new Query(consulta);
            return q.hasSolution();
        } catch (Exception ex) {
            System.err.println("Error en agregarCancion: " + ex.getMessage());
            return false;
        }
    }'''

modelo_content = modelo_content.replace(old_agregar_cancion, new_agregar_cancion)

# Update eliminarFisico
old_eliminar_fisico = '''    public boolean eliminarFisico(String id) {
        try {
            // Eliminar de memoria con JPL
            Query q = new Query(
                "api_eliminar_cancion(" + id + ", fisico)");
            boolean ok = q.hasSolution();

            // Eliminar físicamente del archivo
            if (ok) {
                eliminarHechoDelArchivo(id);
            }
            return ok;
        } catch (Exception ex) {
            System.err.println("Error en eliminarFisico: " + ex.getMessage());
            return false;
        }
    }'''

new_eliminar_fisico = '''    public boolean eliminarFisico(String id) {
        try {
            Query q = new Query("api_eliminar_cancion(" + id + ", archivo)");
            return q.hasSolution();
        } catch (Exception ex) {
            System.err.println("Error en eliminarFisico: " + ex.getMessage());
            return false;
        }
    }'''

modelo_content = modelo_content.replace(old_eliminar_fisico, new_eliminar_fisico)

# Update obtenerTodasLasCanciones
old_obtener_todas = '''    public List<String[]> obtenerTodasLasCanciones() {
        List<String[]> canciones = new ArrayList<String[]>();
        try {
            Query q = new Query(
                "cancion_activa(ID), " +
                "cancion(ID, T, A, G, I), " +
                "atomic_list_concat(G, ', ', GStr)");
            if (q.hasSolution()) {
                for (Map<String, Term> sol : q.allSolutions()) {
                    String id      = sol.get("ID").toString();
                    String titulo  = sol.get("T")
                        .toString().replace("'", "");
                    String artista = sol.get("A")
                        .toString().replace("'", "");
                    String generos = sol.get("GStr").toString();
                    String idioma  = sol.get("I").toString();
                    canciones.add(new String[]{
                        id, titulo, artista, generos, idioma});
                }
            }
        } catch (Exception ex) {
            System.err.println("Error en obtenerTodasLasCanciones: "
                + ex.getMessage());
        }
        return canciones;
    }'''

new_obtener_todas = '''    public List<String[]> obtenerTodasLasCanciones() {
        List<String[]> canciones = new ArrayList<String[]>();
        try {
            Query q = new Query("api_obtener_todas_canciones(ID, T, A, GStr, I)");
            if (q.hasSolution()) {
                for (Map<String, Term> sol : q.allSolutions()) {
                    String id      = sol.get("ID").toString();
                    String titulo  = sol.get("T").toString().replace("'", "");
                    String artista = sol.get("A").toString().replace("'", "");
                    String generos = sol.get("GStr").toString().replace("'", "");
                    String idioma  = sol.get("I").toString().replace("'", "");
                    canciones.add(new String[]{id, titulo, artista, generos, idioma});
                }
            }
        } catch (Exception ex) {
            System.err.println("Error en obtenerTodasLasCanciones: " + ex.getMessage());
        }
        return canciones;
    }'''

modelo_content = modelo_content.replace(old_obtener_todas, new_obtener_todas)

# Replace leerSeccionHechos and leerSeccionReglas
old_leer = '''    public String leerSeccionHechos() {
        return leerSeccion("HECHOS");
    }

    /**
     * Lee la sección REGLAS del archivo .pl y la retorna como String.
     * La Vista la usa para mostrar el contenido en el dialog Ver Reglas.
     * Lee desde el marcador REGLAS hasta el final del archivo.
     *
     * @return contenido de la sección REGLAS como String
     */
    public String leerSeccionReglas() {
        return leerSeccion("REGLAS");
    }

    /**
     * Lee una sección del archivo .pl identificada por su marcador.
     * Los marcadores tienen el formato:
     * % ==================== NOMBRE ====================
     *
     * @param seccion nombre de la sección: "HECHOS" o "REGLAS"
     * @return contenido de la sección como String
     */
    private String leerSeccion(String seccion) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(PROLOG_FILE))) {
            String linea;
            boolean enSeccion = false;

            while ((linea = br.readLine()) != null) {
                String lineaTrim = linea.trim();

                // Detectar inicio de las secciones comentadas
                if (lineaTrim.startsWith("%")) {
                    if (seccion.equals("HECHOS") && lineaTrim.contains("HECHOS")) {
                        enSeccion = true;
                        continue; // No incluir el encabezado principal
                    }
                    if (seccion.equals("REGLAS") && lineaTrim.contains("REGLAS")) {
                        enSeccion = true;
                        continue; // No incluir el encabezado principal
                    }

                    // Si estamos leyendo HECHOS y topamos con el bloque de REGLAS, detenemos la lectura
                    if (seccion.equals("HECHOS") && lineaTrim.contains("REGLAS")) {
                        break;
                    }
                }

                // Si la bandera está activa, acumular la línea completa (con su indentación)
                if (enSeccion) {
                    sb.append(linea).append("\\n");
                }
            }
        } catch (IOException ex) {
            sb.append("No se pudo leer el archivo: ").append(ex.getMessage());
        }
        return sb.toString();
    }'''

new_leer = '''    public String leerSeccionHechos() {
        return leerArchivoCompleto(HECHOS_FILE);
    }

    public String leerSeccionReglas() {
        return leerArchivoCompleto(REGLAS_FILE);
    }

    private String leerArchivoCompleto(String ruta) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea).append("\\n");
            }
        } catch (IOException ex) {
            sb.append("No se pudo leer el archivo: ").append(ex.getMessage());
        }
        return sb.toString();
    }'''

modelo_content = modelo_content.replace(old_leer, new_leer)

# Remove escribirHechoEnArchivo and eliminarHechoDelArchivo
import re
modelo_content = re.sub(r'/\*\*[\s\S]*?private void escribirHechoEnArchivo[\s\S]*?\}', '', modelo_content)
modelo_content = re.sub(r'/\*\*[\s\S]*?private void eliminarHechoDelArchivo[\s\S]*?\}', '', modelo_content)

with open(modelo_path, 'w', encoding='utf-8') as f:
    f.write(modelo_content)

print("Modificado Modelo.java")
