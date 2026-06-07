/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MVC;

import java.util.Arrays;
import java.util.List;

/**
 * Clase Modelo - Capa de datos del patrón MVC.
 *
 * Responsabilidades:
 * - Almacenar las listas de datos que la Vista necesita para construir los combos.
 * - Guardar la ruta del archivo Prolog.
 * - Exponer getters para que la Vista acceda a las listas.
 * - Contener los métodos de consulta a Prolog que el Controlador invocará.
 *
 */
public class Modelo {

    // Ruta del archivo de la base de conocimiento Prolog
    private static final String PROLOG_FILE = "src/ArchivosExtra/BasedeConocimiento.pl";

    // Lista de estados de ánimo disponibles
    private List<String> estadosAnimo;

    // Lista de actividades disponibles
    private List<String> actividades;

    // Lista de géneros musicales disponibles
    private List<String> generos;

    // Lista de idiomas disponibles
    private List<String> idiomas;

    /**
     * Constructor vacío del Modelo.
     * Inicializa todas las listas de datos
     * que la Vista usará para construir los JComboBox.
     */
    public Modelo() {
        estadosAnimo = Arrays.asList(
            "feliz", "triste", "energetico",
            "relajado", "romantico", "nostalgico"
        );

        actividades = Arrays.asList(
            "ejercicio", "estudiar", "fiesta",
            "dormir", "conducir", "trabajar"
        );

        generos = Arrays.asList(
            "reggaeton", "rock", "lofi",
            "jazz", "balada", "pop", "clasica", "salsa"
        );

        idiomas = Arrays.asList(
            "espanol", "ingles", "instrumental"
        );
    }

    // ==================== CONSULTAS PROLOG ====================

    /**
     * Consulta el género musical recomendado según las preferencias del usuario.
     *
     * Debe:
     * 1. Limpiar hechos dinámicos anteriores con retractall
     *    - retractall(estado(_))
     *    - retractall(actividad_sel(_))
     *    - retractall(energia_sel(_))
     * 2. Inyectar los nuevos hechos con assertz
     *    - assertz(estado(feliz))
     *    - assertz(actividad_sel(estudiar))
     *    - assertz(energia_sel(baja))
     * 3. Consultar recomienda(Genero)
     * 4. Retornar el género como String
     *
     * @param estado    estado de ánimo seleccionado por el usuario
     * @param actividad actividad seleccionada por el usuario
     * @param energia   nivel de energía seleccionado por el usuario
     * @return          género musical recomendado como String
     */
    public String obtenerGenero(String estado, String actividad, String energia) {
        // TODO: implementar consulta JPL a Prolog
        return null;
    }

    /**
     * Obtiene la playlist de canciones del género recomendado.
     *
     * Debe:
     * 1. Consultar obtener_playlist(Genero, Titulo, Artista)
     * 2. Iterar todas las soluciones con allSolutions()
     * 3. Por cada solución extraer Genero, Titulo y Artista
     * 4. Agregar cada canción como String[] a la lista
     * 5. Retornar la lista completa
     *
     * @param estado    estado de ánimo seleccionado por el usuario
     * @param actividad actividad seleccionada por el usuario
     * @param energia   nivel de energía seleccionado por el usuario
     * @return          lista de canciones donde cada elemento es
     *                  String[]{ genero, titulo, artista }
     */
    public List<String[]> obtenerPlaylist(String estado, String actividad, String energia) {
        // TODO: implementar consulta JPL a Prolog
        return null;
    }
    
    // ==================== GETTERS ====================

    /**
     * Retorna la lista de estados de ánimo.
     */
    public List<String> getEstadosAnimo() {
        return estadosAnimo;
    }

    /**
     * Retorna la lista de actividades.
     */
    public List<String> getActividades() {
        return actividades;
    }

    /**
     * Retorna la lista de géneros musicales.
     */
    public List<String> getGeneros() {
        return generos;
    }

    /**
     * Retorna la lista de idiomas disponibles.
     */
    public List<String> getIdiomas() {
        return idiomas;
    }

    /**
     * Retorna la ruta del archivo Prolog.
     */
    public String getPrologFile() {
        return PROLOG_FILE;
    }
}
