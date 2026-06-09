/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente.MVC;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.jpl7.Query;
import org.jpl7.Term;

public class Modelo {

    private static final String PROLOG_FILE =
        System.getProperty("user.dir").replace("\\", "/") + "/motor_prolog/api/interfaz_java.pl";

    private static final String HECHOS_FILE =
        System.getProperty("user.dir").replace("\\", "/") + "/motor_prolog/conocimiento/hechos.pl";

    private static final String REGLAS_FILE =
        System.getProperty("user.dir").replace("\\", "/") + "/motor_prolog/conocimiento/reglas_actividad.pl";

    private List<String> estadosAnimo;
    private List<String> actividades;
    private List<String> generos;
    private List<String> idiomas;

    public Modelo() {
        estadosAnimo = Arrays.asList("Animo","feliz", "triste", "energetico", "relajado", "romantico", "nostalgico");
        actividades = Arrays.asList("Actividad","ejercicio", "estudiar", "fiesta", "dormir", "conducir", "trabajar");
        generos = Arrays.asList("Genero","reggaeton", "rock", "lofi", "jazz", "balada", "pop", "clasica", "salsa");
        idiomas = Arrays.asList("Idioma","espanol", "ingles", "instrumental", "cualquiera");
        cargarProlog();
    }

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

    public List<String[]> obtenerPlaylist(String tipo, String arg1, String arg2) {
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
    }

    public String[] consultarCancionActiva(String id) {
        try {
            Query q = new Query("api_consultar(" + id + ", Titulo, Artista, GenerosStr, Idioma)");
            if (q.hasSolution()) {
                Map<String, Term> sol = q.oneSolution();
                String titulo = sol.get("Titulo").toString().replace("'", "");
                String artista = sol.get("Artista").toString().replace("'", "");
                String generos = sol.get("GenerosStr").toString().replace("'", "");
                String idioma = sol.get("Idioma").toString().replace("'", "");
                return new String[]{titulo, artista, generos, idioma};
            }
        } catch (Exception ex) {
            System.err.println("Error en consultarCancionActiva: " + ex.getMessage());
        }
        return null;
    }

    public boolean agregarCancion(String id, String titulo, String artista, String generos, String idioma) {
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
    }

    public boolean eliminarLogico(String id) {
        try {
            Query q = new Query("api_eliminar_cancion(" + id + ", logico)");
            return q.hasSolution();
        } catch (Exception ex) {
            System.err.println("Error en eliminarLogico: " + ex.getMessage());
            return false;
        }
    }

    public boolean eliminarFisico(String id) {
        try {
            Query q = new Query("api_eliminar_cancion(" + id + ", archivo)");
            return q.hasSolution();
        } catch (Exception ex) {
            System.err.println("Error en eliminarFisico: " + ex.getMessage());
            return false;
        }
    }

    public boolean restaurarCancion(String id) {
        try {
            Query q = new Query("api_restaurar_cancion(" + id + ")");
            return q.hasSolution();
        } catch (Exception ex) {
            System.err.println("Error en restaurarCancion: " + ex.getMessage());
            return false;
        }
    }

    public List<String[]> obtenerTodasLasCanciones() {
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
    }

    public String leerSeccionHechos() {
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
                sb.append(linea).append("\n");
            }
        } catch (IOException ex) {
            sb.append("No se pudo leer el archivo: ").append(ex.getMessage());
        }
        return sb.toString();
    }

    public List<String> getEstadosAnimo() { return estadosAnimo; }
    public List<String> getActividades()  { return actividades;  }
    public List<String> getGeneros()      { return generos;      }
    public List<String> getIdiomas()      { return idiomas;      }
    public String getPrologFile()         { return PROLOG_FILE;  }
}