/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MVC;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Clase Controlador - Capa de lógica del patrón MVC.
 *
 * Responsabilidades:
 * - Recibir el Modelo y la Vista en el constructor.
 * - Registrar todos los listeners de los botones de la Vista.
 * - Leer los valores de la Vista mediante sus getters.
 * - Invocar los métodos del Modelo para realizar consultas a Prolog.
 * - Actualizar la Vista con los resultados obtenidos del Modelo.
 *
 */
public class Controlador {

    // Referencia al Modelo para realizar consultas
    private Modelo modelo;

    // Referencia a la Vista para registrar listeners y actualizar pantalla
    private Vista vista;

    /**
     * Constructor del Controlador.
     * Recibe el Modelo y la Vista ya instanciados.
     * Asigna el Controlador a la Vista y registra todos los listeners.
     *
     * @param modelo objeto Modelo ya instanciado desde el Main
     * @param vista  objeto Vista ya instanciado desde el Main
     */
    public Controlador(Modelo modelo, Vista vista) {
        this.modelo = modelo;
        this.vista  = vista;
        this.vista.setControlador(this);
        inicializarListeners();
    }

    /**
     * Registra todos los listeners de los botones de la Vista.
     * Se llama una sola vez desde el constructor.
     */
    private void inicializarListeners() {
        registrarListenerGenerar();
        registrarListenerVolver();
        registrarListenerNueva();
    }

    // ==================== LISTENERS PANEL PRINCIPAL ====================

    /**
     * Listener: Botón Generar Playlist
     *
     * Debe:
     * 1. Leer estado, actividad, energia e idioma de la Vista
     *    usando vista.getEstado(), vista.getActividad(),
     *    vista.getEnergia(), vista.getIdioma()
     * 2. Llamar a generarPlaylist() con esos valores
     */
    private void registrarListenerGenerar() {
        vista.getGenerarBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarPlaylist();
            }
        });
    }

    /**
     * Listener: Botón Volver
     *
     * Debe:
     * 1. Llamar a vista.mostrarPanelPreferencias()
     *    para regresar al panel de preferencias
     */
    private void registrarListenerVolver() {
        vista.getVolverBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vista.mostrarPanelPreferencias();
            }
        });
    }

    /**
     * Listener: Botón Nueva Búsqueda
     *
     * Debe:
     * 1. Llamar a vista.mostrarPanelPreferencias()
     *    para iniciar una nueva búsqueda desde cero
     */
    private void registrarListenerNueva() {
        vista.getNuevaBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vista.mostrarPanelPreferencias();
            }
        });
    }

    // ==================== LISTENERS MENU ====================

    /**
     * Listener: Menú Ver Hechos
     * Se registra cuando el Controlador recibe la referencia
     * al JMenuItem desde la Vista mediante getter.
     *
     * Debe:
     * 1. Llamar a vista.mostrarDialogoHechos()
     *    para abrir el dialog con los hechos del archivo .pl
     */
    public void registrarListenerVerHechos() {
        // TODO: vista.getItemHechos().addActionListener(...)
        // vista.mostrarDialogoHechos()
    }

    /**
     * Listener: Menú Ver Reglas
     * Se registra cuando el Controlador recibe la referencia
     * al JMenuItem desde la Vista mediante getter.
     *
     * Debe:
     * 1. Llamar a vista.mostrarDialogoReglas()
     *    para abrir el dialog con las reglas del archivo .pl
     */
    public void registrarListenerVerReglas() {
        // TODO: vista.getItemReglas().addActionListener(...)
        // vista.mostrarDialogoReglas()
    }

    /**
     * Listener: Menú Agregar Hecho
     *
     * Debe:
     * 1. Llamar a vista.mostrarDialogoAgregarHecho()
     *    para abrir el formulario de agregar
     * 2. Una vez abierto el dialog, registrar los listeners
     *    de agregarHechoBtn y cerrarAgregarBtn
     */
    public void registrarListenerAgregarHecho() {
        // TODO: vista.getItemAgregarHecho().addActionListener(...)
        // vista.mostrarDialogoAgregarHecho()
    }

    /**
     * Listener: Menú Eliminar Hecho
     *
     * Debe:
     * 1. Llamar a vista.mostrarDialogoEliminarHecho()
     *    para abrir la lista de hechos
     * 2. Una vez abierto el dialog, registrar los listeners
     *    de eliminarHechoBtn y cerrarEliminarBtn
     */
    public void registrarListenerEliminarHecho() {
        // TODO: vista.getItemEliminarHecho().addActionListener(...)
        // vista.mostrarDialogoEliminarHecho()
    }

    /**
     * Listener: Menú Consulta Simple
     *
     * Debe:
     * 1. Llamar a vista.mostrarDialogoConsulta()
     *    para abrir el dialog de consulta simple
     * 2. Una vez abierto el dialog, registrar los listeners
     *    de consultarBtn y cerrarConsultaBtn
     */
    public void registrarListenerConsulta() {
        // TODO: vista.getItemConsulta().addActionListener(...)
        // vista.mostrarDialogoConsulta()
    }

    // ==================== LISTENERS DIALOG AGREGAR HECHO ====================

    /**
     * Listener: Combo Tipo de Hecho en dialog de agregar
     *
     * Debe:
     * 1. Leer el tipo seleccionado con vista.getTipoHecho()
     * 2. Llamar a vista.toggleCamposCancion(esCancion)
     *    para mostrar u ocultar los campos de título y artista
     */
    public void registrarListenerTipoHecho() {
        // TODO: vista.getTipoHechoCombo().addActionListener(...)
        // boolean esCancion = vista.getTipoHecho().equals("cancion")
        // vista.toggleCamposCancion(esCancion)
    }

    /**
     * Listener: Botón Agregar Hecho en dialog de agregar
     *
     * Debe:
     * 1. Leer tipo con vista.getTipoHecho()
     * 2. Leer valor con vista.getVal1()
     * 3. Si tipo es "cancion" leer también vista.getVal2() y vista.getVal3()
     * 4. Validar que los campos no estén vacíos
     * 5. Construir el hecho en formato Prolog:
     *    - Simple:  tipo(valor).
     *    - Cancion: cancion(genero, 'titulo', 'artista').
     * 6. Llamar a vista.agregarHechoAlArchivo(hecho)
     * 7. Llamar a vista.limpiarCamposAgregar()
     * 8. Mostrar mensaje de éxito con JOptionPane
     */
    public void registrarListenerConfirmarAgregar() {
        // TODO: vista.getAgregarHechoBtn().addActionListener(...)
    }

    /**
     * Listener: Botón Cerrar en dialog de agregar
     *
     * Debe:
     * 1. Cerrar el dialog activo con dialog.dispose()
     */
    public void registrarListenerCerrarAgregar() {
        // TODO: vista.getCerrarAgregarBtn().addActionListener(...)
        // dialog.dispose()
    }

    // ==================== LISTENERS DIALOG ELIMINAR HECHO ====================

    /**
     * Listener: Botón Eliminar Seleccionado en dialog de eliminar
     *
     * Debe:
     * 1. Leer el hecho seleccionado con vista.getHechoSeleccionado()
     * 2. Validar que no sea null
     * 3. Mostrar confirmación con JOptionPane
     * 4. Si acepta: llamar a vista.eliminarHechoDelArchivo(hecho)
     * 5. Llamar a vista.eliminarHechoDeLista(hecho)
     * 6. Mostrar mensaje de éxito con JOptionPane
     */
    public void registrarListenerConfirmarEliminar() {
        // TODO: vista.getEliminarHechoBtn().addActionListener(...)
    }

    /**
     * Listener: Botón Cerrar en dialog de eliminar
     *
     * Debe:
     * 1. Cerrar el dialog activo con dialog.dispose()
     */
    public void registrarListenerCerrarEliminar() {
        // TODO: vista.getCerrarEliminarBtn().addActionListener(...)
        // dialog.dispose()
    }

    // ==================== LISTENERS DIALOG CONSULTA SIMPLE ====================

    /**
     * Listener: Botón Consultar en dialog de consulta simple
     *
     * Debe:
     * 1. Leer tipo con vista.getTipoConsulta()
     * 2. Leer valor con vista.getValorConsulta()
     * 3. Validar que el campo no esté vacío
     * 4. Llamar a modelo.consultarHecho(tipo, valor)
     * 5. Mostrar resultado con vista.mostrarResultadoConsulta(resultado)
     */
    public void registrarListenerConsultar() {
        // TODO: vista.getConsultarBtn().addActionListener(...)
        // modelo.consultarHecho(tipo, valor)
        // vista.mostrarResultadoConsulta(resultado)
    }

    /**
     * Listener: Botón Cerrar en dialog de consulta simple
     *
     * Debe:
     * 1. Cerrar el dialog activo con dialog.dispose()
     */
    public void registrarListenerCerrarConsulta() {
        // TODO: vista.getCerrarConsultaBtn().addActionListener(...)
        // dialog.dispose()
    }

    // ==================== MÉTODOS DE LÓGICA ====================

    /**
     * Genera la playlist recomendada.
     *
     * Debe:
     * 1. Leer estado, actividad y energia de la Vista
     * 2. Llamar a modelo.obtenerGenero(estado, actividad, energia)
     * 3. Llamar a modelo.obtenerPlaylist(estado, actividad, energia)
     * 4. Llamar a vista.mostrarResultados(genero, playlist)
     */
    private void generarPlaylist() {
        // TODO: implementar cuando el Modelo esté completo
        // String estado    = vista.getEstado()
        // String actividad = vista.getActividad()
        // String energia   = vista.getEnergia()
        // String genero    = modelo.obtenerGenero(estado, actividad, energia)
        // List<String[]> playlist = modelo.obtenerPlaylist(estado, actividad, energia)
        // vista.mostrarResultados(genero, playlist)
    }
}