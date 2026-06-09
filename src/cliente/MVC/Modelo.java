/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente.MVC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.jpl7.Query;
import org.jpl7.Term;

/**
 * Clase Modelo - Capa de datos del patrón MVC.
 *
 * Responsabilidades:
 * - Almacenar las listas de datos que la Vista necesita para los JComboBox.
 * - Cargar la base de conocimiento Prolog al iniciar.
 * - Ejecutar todas las consultas a Prolog mediante JPL.
 * - Gestionar el CRUD de canciones (agregar, eliminar lógico, eliminar físico).
 * - Leer secciones del archivo .pl para mostrarse en la Vista.
 *
 * Lo que NO hace esta clase:
 * - No construye componentes gráficos.
 * - No registra listeners.
 * - No se comunica con la Vista directamente.
 */
public class Modelo {

    // Ruta del archivo único de la base de conocimiento
    // Ruta dinámica del archivo Prolog
    private static final String PROLOG_FILE =
        System.getProperty("user.dir")
            .replace("\\", "/")
        + "/src/cliente/ArchivosExtra/BasedeConocimiento.pl";

    // Lista de estados de ánimo disponibles
    private List<String> estadosAnimo;

    // Lista de actividades disponibles
    private List<String> actividades;

    // Lista de géneros musicales disponibles
    private List<String> generos;

    // Lista de idiomas disponibles, incluye "cualquiera" para filtro opcional
    private List<String> idiomas;

    /**
     * Constructor del Modelo.
     * Inicializa las listas de datos y carga el archivo Prolog.
     */
    public Modelo() {
        estadosAnimo = Arrays.asList(
            "Animo","feliz", "triste", "energetico",
            "relajado", "romantico", "nostalgico"
        );
        actividades = Arrays.asList(
            "Actividad","ejercicio", "estudiar", "fiesta",
            "dormir", "conducir", "trabajar"
        );
        generos = Arrays.asList(
            "Genero","reggaeton", "rock", "lofi", "jazz",
            "balada", "pop", "clasica", "salsa"
        );
        idiomas = Arrays.asList(
            "Idioma","espanol", "ingles", "instrumental", "cualquiera"
        );
        cargarProlog();
    }

    /**
     * Carga el archivo BasedeConocimiento.pl usando JPL.
     * Usa ruta absoluta construida dinámicamente para evitar
     * problemas con rutas relativas al ejecutar desde NetBeans.
     */
    private void cargarProlog() {
        try {
            Query q = new Query("consult('" + PROLOG_FILE + "')");
            if (q.hasSolution()) {
                System.out.println("Base de conocimiento cargada: " + PROLOG_FILE);
            } else {
                System.err.println("No se pudo cargar la base de conocimiento.");
            }
        } catch (Exception ex) {
            System.err.println("Error al cargar Prolog: " + ex.getMessage());
        }
    }

    // ==================== CONSULTAS PROLOG ====================

    /**
     * Busca canciones usando el endpoint buscar_avanzado/7.
     * Envía los 4 filtros seleccionados por el usuario.
     * Si el usuario no seleccionó idioma se envía "cualquiera".
     *
     * @param emocion   estado de ánimo seleccionado, ej: "feliz"
     * @param actividad actividad seleccionada, ej: "ejercicio"
     * @param energia   nivel de energía seleccionado, ej: "alta"
     * @param idioma    idioma preferido o "cualquiera"
     * @return lista donde cada elemento es String[]{ titulo, artista, generos }
     */
    public List<String[]> obtenerPlaylist(String emocion, String actividad,
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
    }

    /**
     * Consulta simple: verifica si una canción existe y está activa.
     * Usa el endpoint api_consultar/1 de la base de conocimiento.
     *
     * @param id ID de la canción a consultar, ej: "c01"
     * @return true si existe y no fue eliminada lógicamente
     */
    public boolean consultarCancionActiva(String id) {
        try {
            Query q = new Query("api_consultar(" + id + ")");
            return q.hasSolution();
        } catch (Exception ex) {
            System.err.println("Error en consultarCancionActiva: "
                + ex.getMessage());
            return false;
        }
    }

    // ==================== CRUD ====================

    /**
     * Agrega una nueva canción a la base de conocimiento en memoria
     * y la escribe al final de la sección HECHOS del archivo .pl.
     * Usa el endpoint api_agregar_cancion/5.
     *
     * Formato Prolog generado:
     * cancion(ID, 'Titulo', 'Artista', [genero1, genero2], idioma).
     *
     * @param id      identificador único, ej: "c31"
     * @param titulo  título de la canción
     * @param artista nombre del artista
     * @param generos géneros en formato Prolog, ej: "[pop, dance]"
     * @param idioma  idioma: espanol, ingles o instrumental
     * @return true si se agregó correctamente en memoria y en disco
     */
    public boolean agregarCancion(String id, String titulo,
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
    }

    /**
     * Elimina una canción de forma lógica (solo en memoria de sesión).
     * La canción sigue en el archivo pero no aparece en las búsquedas.
     * Usa el endpoint api_eliminar_cancion/2 con tipo logico.
     *
     * @param id ID de la canción a eliminar lógicamente
     * @return true si se marcó correctamente
     */
    public boolean eliminarLogico(String id) {
        try {
            Query q = new Query(
                "api_eliminar_cancion(" + id + ", logico)");
            return q.hasSolution();
        } catch (Exception ex) {
            System.err.println("Error en eliminarLogico: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Elimina una canción de forma física del archivo .pl y de memoria.
     * Usa el endpoint api_eliminar_cancion/2 con tipo fisico
     * y luego reescribe el archivo sin esa línea.
     *
     * @param id ID de la canción a eliminar físicamente
     * @return true si se eliminó correctamente
     */
    public boolean eliminarFisico(String id) {
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
    }

    /**
     * Restaura una canción eliminada lógicamente en esta sesión.
     * Usa el endpoint api_restaurar_cancion/1.
     *
     * @param id ID de la canción a restaurar
     * @return true si se restauró correctamente
     */
    public boolean restaurarCancion(String id) {
        try {
            Query q = new Query("api_restaurar_cancion(" + id + ")");
            return q.hasSolution();
        } catch (Exception ex) {
            System.err.println("Error en restaurarCancion: "
                + ex.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todas las canciones activas de la base de conocimiento.
     * Usada por la Vista para poblar la lista del dialog de eliminar.
     *
     * @return lista donde cada elemento es
     *         String[]{ id, titulo, artista, generos, idioma }
     */
    public List<String[]> obtenerTodasLasCanciones() {
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
    }

    // ==================== LECTURA DE ARCHIVO ====================

    /**
     * Lee la sección HECHOS del archivo .pl y la retorna como String.
     * La Vista la usa para mostrar el contenido en el dialog Ver Hechos.
     * Lee desde el marcador HECHOS hasta el marcador REGLAS.
     *
     * @return contenido de la sección HECHOS como String
     */
    public String leerSeccionHechos() {
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
                    sb.append(linea).append("\n");
                }
            }
        } catch (IOException ex) {
            sb.append("No se pudo leer el archivo: ").append(ex.getMessage());
        }
        return sb.toString();
    }

    // ==================== ESCRITURA DE ARCHIVO ====================

    /**
     * Escribe una nueva línea de hecho en el archivo .pl
     * justo antes del marcador de la sección REGLAS.
     * Llamado internamente por agregarCancion().
     *
     * @param hecho línea en formato Prolog a insertar
     */
    private void escribirHechoEnArchivo(String hecho) {
        try {
            List<String> lineas = new ArrayList<String>();
            BufferedReader br = new BufferedReader(
                new FileReader(PROLOG_FILE));
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
            br.close();

            int insertIndex = -1;
            for (int i = 0; i < lineas.size(); i++) {
                if (lineas.get(i).contains(
                        "% ==================== REGLAS")) {
                    insertIndex = i;
                    break;
                }
            }

            if (insertIndex != -1) {
                lineas.add(insertIndex, hecho);
            } else {
                lineas.add(hecho);
            }

            BufferedWriter bw = new BufferedWriter(
                new FileWriter(PROLOG_FILE));
            for (String l : lineas) {
                bw.write(l);
                bw.newLine();
            }
            bw.close();
        } catch (IOException ex) {
            System.err.println("Error al escribir en archivo: "
                + ex.getMessage());
        }
    }

    /**
     * Elimina físicamente la línea de una canción del archivo .pl
     * buscando el ID en la línea cancion(ID, ...).
     * Llamado internamente por eliminarFisico().
     *
     * @param id ID de la canción a eliminar del archivo
     */
    private void eliminarHechoDelArchivo(String id) {
        try {
            List<String> lineas = new ArrayList<String>();
            BufferedReader br = new BufferedReader(
                new FileReader(PROLOG_FILE));
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
            br.close();

            List<String> nuevasLineas = new ArrayList<String>();
            for (String l : lineas) {
                // Excluir la línea que contiene el ID de la canción
                if (!l.trim().startsWith("cancion(" + id + ",")) {
                    nuevasLineas.add(l);
                }
            }

            BufferedWriter bw = new BufferedWriter(
                new FileWriter(PROLOG_FILE));
            for (String l : nuevasLineas) {
                bw.write(l);
                bw.newLine();
            }
            bw.close();
        } catch (IOException ex) {
            System.err.println("Error al eliminar del archivo: "
                + ex.getMessage());
        }
    }

    // ==================== GETTERS ====================

    /**
     * Retorna la lista de estados de ánimo.
     * La Vista la usa para llenar el moodCombo.
     */
    public List<String> getEstadosAnimo() { return estadosAnimo; }

    /**
     * Retorna la lista de actividades.
     * La Vista la usa para llenar el actividadCombo.
     */
    public List<String> getActividades()  { return actividades;  }

    /**
     * Retorna la lista de géneros musicales.
     */
    public List<String> getGeneros()      { return generos;      }

    /**
     * Retorna la lista de idiomas.
     * La Vista la usa para llenar el idiomaCombo.
     */
    public List<String> getIdiomas()      { return idiomas;      }

    /**
     * Retorna la ruta del archivo Prolog.
     */
    public String getPrologFile()         { return PROLOG_FILE;  }
}