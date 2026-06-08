/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente.MVC;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

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

    // Referencia al Modelo para realizar consultas y CRUD
    private Modelo modelo;

    // Referencia a la Vista para registrar listeners y actualizar pantalla
    private Vista vista;

    /**
     * Constructor del Controlador.
     * Recibe el Modelo y la Vista ya instanciados.
     * Asigna el Controlador a la Vista y registra los listeners principales.
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
     * Registra los listeners de los botones principales y del menú.
     * Se llama una sola vez desde el constructor.
     */
    private void inicializarListeners() {
        registrarListenerGenerar();
        registrarListenerVolver();
        registrarListenerNueva();
        
        registrarListenerVerHechos();
        registrarListenerVerReglas();
        registrarListenerAgregarCancion();
        registrarListenerEliminarCancion();
        registrarListenerConsulta();
    }

    // ==================== LISTENERS PANEL PRINCIPAL ====================

    /**
     * Listener: Botón Generar Playlist.
     * Lee los 4 filtros de la Vista y llama a generarPlaylist().
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
     * Listener: Botón Volver.
     * Llama a vista.mostrarPanelPreferencias() que también resetea los combos.
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
     * Listener: Botón Nueva Búsqueda.
     * Llama a vista.mostrarPanelPreferencias() para reiniciar el flujo.
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
     * Listener: Menú Ver Hechos.
     * Obtiene el contenido de la sección HECHOS del Modelo
     * y lo pasa a la Vista para mostrarlo.
     *
     * @param item JMenuItem del menú Ver Hechos
     */
    public void registrarListenerVerHechos(javax.swing.JMenuItem item) {
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String contenido = modelo.leerSeccionHechos();
                vista.mostrarDialogoHechos(contenido);
                registrarListenerCerrarHechos();
            }
        });
    }

    /**
     * Listener: Menú Ver Reglas.
     * Obtiene el contenido de la sección REGLAS del Modelo
     * y lo pasa a la Vista para mostrarlo.
     *
     * @param item JMenuItem del menú Ver Reglas
     */
    public void registrarListenerVerReglas(javax.swing.JMenuItem item) {
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String contenido = modelo.leerSeccionReglas();
                vista.mostrarDialogoReglas(contenido);
                registrarListenerCerrarReglas();
            }
        });
    }

    /**
     * Listener: Menú Agregar Canción.
     * Abre el dialog de agregar y registra sus listeners internos.
     *
     * @param item JMenuItem del menú Agregar Canción
     */
    public void registrarListenerAgregarCancion(javax.swing.JMenuItem item) {
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vista.mostrarDialogoAgregarHecho();
                registrarListenerConfirmarAgregar();
                registrarListenerCerrarAgregar();
            }
        });
    }

    /**
     * Listener: Menú Eliminar Canción.
     * Obtiene la lista de canciones activas del Modelo,
     * la pasa a la Vista y registra los listeners internos.
     *
     * @param item JMenuItem del menú Eliminar Canción
     */
    public void registrarListenerEliminarCancion(javax.swing.JMenuItem item) {
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String[]> canciones = modelo.obtenerTodasLasCanciones();
                vista.mostrarDialogoEliminarHecho(canciones);
                registrarListenerConfirmarEliminar();
                registrarListenerCerrarEliminar();
            }
        });
    }

    /**
     * Listener: Menú Consulta Simple.
     * Abre el dialog de consulta y registra sus listeners internos.
     *
     * @param item JMenuItem del menú Consulta Simple
     */
    public void registrarListenerConsulta(javax.swing.JMenuItem item) {
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vista.mostrarDialogoConsulta();
                registrarListenerConsultar();
                registrarListenerCerrarConsulta();
            }
        });
    }

    // ==================== LISTENERS DIALOGS HECHOS Y REGLAS ====================

    /**
     * Listener: Botón Cerrar del dialog Ver Hechos.
     * Cierra el dialog actual con dispose().
     */
    public void registrarListenerCerrarHechos() {
        vista.getCerrarHechosBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                javax.swing.SwingUtilities.getWindowAncestor(
                    vista.getCerrarHechosBtn()).dispose();
            }
        });
    }

    /**
     * Listener: Botón Cerrar del dialog Ver Reglas.
     * Cierra el dialog actual con dispose().
     */
    public void registrarListenerCerrarReglas() {
        vista.getCerrarReglasBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                javax.swing.SwingUtilities.getWindowAncestor(
                    vista.getCerrarReglasBtn()).dispose();
            }
        });
    }

    // ==================== LISTENERS DIALOG AGREGAR ====================

    /**
     * Listener: Botón Agregar del dialog Agregar Canción.
     * Lee los campos de la Vista, valida que no estén vacíos,
     * llama al Modelo para agregar en memoria y en disco,
     * limpia los campos y muestra mensaje de resultado.
     */
    public void registrarListenerConfirmarAgregar() {
        vista.getAgregarHechoBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id      = vista.getId();
                String titulo  = vista.getTitulo();
                String artista = vista.getArtista();
                String generos = vista.getGeneros();
                String idioma  = vista.getIdiomaAgregar();

                // Validar campos obligatorios
                if (id.isEmpty() || titulo.isEmpty() ||
                    artista.isEmpty() || generos.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                        "Todos los campos son obligatorios.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validar formato de géneros [g1, g2]
                if (!generos.startsWith("[") || !generos.endsWith("]")) {
                    JOptionPane.showMessageDialog(null,
                        "El formato de géneros debe ser [g1, g2].\n" +
                        "Ejemplo: [pop, dance]",
                        "Error de formato", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Llamar al Modelo para agregar
                boolean ok = modelo.agregarCancion(
                    id, titulo, artista, generos, idioma);

                if (ok) {
                    JOptionPane.showMessageDialog(null,
                        "Canción agregada correctamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    vista.limpiarCamposAgregar();
                } else {
                    JOptionPane.showMessageDialog(null,
                        "Error al agregar la canción en Prolog.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Listener: Botón Cerrar del dialog Agregar Canción.
     * Cierra el dialog con dispose().
     */
    public void registrarListenerCerrarAgregar() {
        vista.getCerrarAgregarBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                javax.swing.SwingUtilities.getWindowAncestor(
                    vista.getCerrarAgregarBtn()).dispose();
            }
        });
    }

    // ==================== LISTENERS DIALOG ELIMINAR ====================

    /**
     * Listener: Botón Eliminar Seleccionado del dialog Eliminar Canción.
     * Lee la canción seleccionada, extrae el ID del formato "ID - Titulo - Artista",
     * pregunta al usuario el tipo de eliminación (lógico o físico),
     * llama al Modelo correspondiente y actualiza la lista en pantalla.
     */
    public void registrarListenerConfirmarEliminar() {
        vista.getEliminarHechoBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String seleccionado = vista.getHechoSeleccionado();
                if (seleccionado == null) {
                    JOptionPane.showMessageDialog(null,
                        "Selecciona una canción primero.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Extraer ID del formato "ID - Titulo - Artista"
                String id = seleccionado.split(" - ")[0].trim();

                // Preguntar tipo de eliminación
                Object[] opciones = {"Lógico", "Físico", "Cancelar"};
                int tipo = JOptionPane.showOptionDialog(null,
                    "¿Cómo deseas eliminar la canción " + id + "?\n\n" +
                    "Lógico: la oculta solo en esta sesión\n" +
                    "Físico: la elimina permanentemente del archivo",
                    "Tipo de eliminación",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, opciones, opciones[0]);

                if (tipo == 0) {
                    // Eliminación lógica: solo en memoria de sesión
                    boolean ok = modelo.eliminarLogico(id);
                    if (ok) {
                        vista.eliminarHechoDeLista(seleccionado);
                        JOptionPane.showMessageDialog(null,
                            "Canción ocultada lógicamente en esta sesión.",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else if (tipo == 1) {
                    // Eliminación física: de memoria y del archivo
                    boolean ok = modelo.eliminarFisico(id);
                    if (ok) {
                        vista.eliminarHechoDeLista(seleccionado);
                        JOptionPane.showMessageDialog(null,
                            "Canción eliminada permanentemente.",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null,
                            "Error al eliminar la canción.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    /**
     * Listener: Botón Cerrar del dialog Eliminar Canción.
     * Cierra el dialog con dispose().
     */
    public void registrarListenerCerrarEliminar() {
        vista.getCerrarEliminarBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                javax.swing.SwingUtilities.getWindowAncestor(
                    vista.getCerrarEliminarBtn()).dispose();
            }
        });
    }

    // ==================== LISTENERS DIALOG CONSULTA ====================

    /**
     * Listener: Botón Consultar del dialog Consulta Simple.
     * Lee el ID ingresado, valida que no esté vacío,
     * llama al Modelo para verificar si la canción está activa
     * y muestra el resultado en la Vista.
     */
    public void registrarListenerConsultar() {
        vista.getConsultarBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = vista.getValorConsulta();
                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                        "Ingresa un ID para consultar.\nEjemplo: c01",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                boolean existe = modelo.consultarCancionActiva(id);
                vista.mostrarResultadoConsulta(
                    existe
                    ? "✓ La canción " + id + " existe y está activa."
                    : "✗ La canción " + id +
                      " no existe o fue eliminada lógicamente."
                );
            }
        });
    }

    /**
     * Listener: Botón Cerrar del dialog Consulta Simple.
     * Cierra el dialog con dispose().
     */
    public void registrarListenerCerrarConsulta() {
        vista.getCerrarConsultaBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                javax.swing.SwingUtilities.getWindowAncestor(
                    vista.getCerrarConsultaBtn()).dispose();
            }
        });
    }

    // ==================== LÓGICA PRINCIPAL ====================

    /**
     * Genera la playlist con los 4 filtros seleccionados en la Vista.
     * Lee los valores, llama al Modelo y actualiza la Vista con resultados.
     * Si no hay resultados muestra un mensaje al usuario.
     */
    private void generarPlaylist() {
        String emocion   = vista.getEstado();
        String actividad = vista.getActividad();
        String energia   = vista.getEnergia();
        String idioma    = vista.getIdioma();

        List<String[]> playlist = modelo.obtenerPlaylist(
            emocion, actividad, energia, idioma);

        if (playlist == null || playlist.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "No se encontraron canciones con esos filtros.\n" +
                "Intenta con una combinación diferente.",
                "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String filtros = emocion + " | " + actividad +
                         " | " + energia + " | " + idioma;
        vista.mostrarResultados(filtros, playlist);
    }
    
    // ==================== LISTENERS MENU ====================

    private void registrarListenerVerHechos() {
        vista.getItemHechos().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String contenido = modelo.leerSeccionHechos();
                JDialog dialog = vista.mostrarDialogoHechos(contenido); // Se construye
                registrarListenerCerrarHechos();                       // Se asigna el listener
                dialog.setVisible(true);                                // Se muestra de forma segura
            }
        });
    }

    private void registrarListenerVerReglas() {
        vista.getItemReglas().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String contenido = modelo.leerSeccionReglas();
                JDialog dialog = vista.mostrarDialogoReglas(contenido);
                registrarListenerCerrarReglas();
                dialog.setVisible(true);
            }
        });
    }

    private void registrarListenerAgregarCancion() {
        vista.getItemAgregarHecho().addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = vista.mostrarDialogoAgregarHecho();
                
                registrarListenerConfirmarAgregar();
                registrarListenerCerrarAgregar();
                
                dialog.setVisible(true);
            }
        });
    }

    private void registrarListenerEliminarCancion() {
        vista.getItemEliminarHecho().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String[]> canciones = modelo.obtenerTodasLasCanciones();
                JDialog dialog = vista.mostrarDialogoEliminarHecho(canciones);
                registrarListenerConfirmarEliminar();
                registrarListenerCerrarEliminar();
                dialog.setVisible(true);
            }
        });
    }

    private void registrarListenerConsulta() {
        vista.getItemConsulta().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = vista.mostrarDialogoConsulta();
                registrarListenerConsultar();
                registrarListenerCerrarConsulta();
                dialog.setVisible(true);
            }
        });
    }
}