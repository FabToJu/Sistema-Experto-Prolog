/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MVC;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase Vista - Capa de presentación del patrón MVC.
 *
 * Responsabilidades:
 * - Recibir el Modelo en el constructor y usarlo para
 *   llenar los JComboBox con sus listas de datos.
 * - Construir y posicionar todos los componentes gráficos.
 * - Declarar todos los botones como atributos de la clase.
 * - Exponer getters de botones y campos para que el Controlador
 *   registre los listeners y lea los valores.
 * - Exponer métodos públicos para que el Controlador
 *   actualice la pantalla con los resultados.
 *
 */
public class Vista extends JFrame {

    // Referencia al Controlador, se asigna con setControlador()
    private Controlador controlador;

    // Referencia al Modelo, recibida en el constructor
    private Modelo modelo;

    // CardLayout para alternar entre paneles
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // ===== Componentes Panel de Preferencias =====
    private JPanel preferencesPanel;
    private JComboBox<String> moodCombo;       // Llenado con modelo.getEstadosAnimo()
    private JComboBox<String> actividadCombo;  // Llenado con modelo.getActividades()
    private JComboBox<String> energiaCombo;    // Llenado con ["alta","media","baja"]
    private JComboBox<String> idiomaCombo;     // Llenado con modelo.getIdiomas()
    private JButton generarBtn;                // Genera la playlist

    // ===== Componentes Panel de Resultados =====
    private JPanel resultPanel;
    private JLabel generoLabel;                // Muestra el género recomendado
    private JTable playlistTable;              // Tabla con canciones recomendadas
    private DefaultTableModel tableModel;      // Modelo de datos de la tabla
    private JButton volverBtn;                 // Regresa al panel de preferencias
    private JButton nuevaBtn;                  // Inicia una nueva búsqueda

    // ===== Botones Dialog Ver Hechos =====
    private JButton cerrarHechosBtn;           // Cierra el dialog de hechos

    // ===== Botones Dialog Ver Reglas =====
    private JButton cerrarReglasBtn;           // Cierra el dialog de reglas

    // ===== Componentes Dialog Agregar Hecho =====
    private JComboBox<String> tipoHechoCombo;  // Tipo de hecho a agregar
    private JLabel labelTipoValor;             // Etiqueta dinámica valor/género
    private JTextField val1Field;              // Valor principal del hecho
    private JLabel labelTitulo;                // Etiqueta título canción
    private JTextField val2Field;              // Título de la canción
    private JLabel labelArtista;               // Etiqueta artista canción
    private JTextField val3Field;              // Artista de la canción
    private JButton agregarHechoBtn;           // Confirma agregar el hecho
    private JButton cerrarAgregarBtn;          // Cierra el dialog de agregar

    // ===== Componentes Dialog Eliminar Hecho =====
    private JList<String> listaHechos;         // Lista de hechos para seleccionar
    private DefaultListModel<String> listModel;// Modelo de datos de la lista
    private JButton eliminarHechoBtn;          // Confirma eliminar el hecho
    private JButton cerrarEliminarBtn;         // Cierra el dialog de eliminar

    // ===== Componentes Dialog Consulta Simple =====
    private JComboBox<String> comboTipoConsulta;   // Tipo de dato a consultar
    private JTextField campoConsulta;              // Valor a consultar
    private JLabel resultadoConsultaLabel;         // Muestra el resultado
    private JButton consultarBtn;                  // Ejecuta la consulta
    private JButton cerrarConsultaBtn;             // Cierra el dialog de consulta

    // Ruta del archivo Prolog obtenida del Modelo
    private String prologFile;

    /**
     * Constructor de la Vista.
     * Recibe el Modelo para obtener las listas que llenarán los JComboBox.
     *
     * @param modelo objeto Modelo ya instanciado desde el Main
     */
    public Vista(Modelo modelo) {
        this.modelo     = modelo;
        this.prologFile = modelo.getPrologFile();
        initUI();
    }

    /**
     * Inicializa la ventana principal.
     * Configura tamaño, título, CardLayout, paneles y menú superior.
     */
    private void initUI() {
        setTitle("Sistema Experto - Recomendador Musical");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);
        mainPanel.add(crearPanelPreferencias(), "preferencias");
        mainPanel.add(crearPanelResultados(),   "resultados");

        // ==================== MENU BAR ====================
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(20, 20, 35));

        JMenu menuBC = new JMenu("Base de Conocimiento");
        menuBC.setForeground(Color.WHITE);
        menuBC.setFont(new Font("Arial", Font.BOLD, 13));

        JMenuItem itemHechos        = new JMenuItem("Ver Hechos");
        JMenuItem itemReglas        = new JMenuItem("Ver Reglas");
        JMenuItem itemAgregarHecho  = new JMenuItem("Agregar Hecho");
        JMenuItem itemEliminarHecho = new JMenuItem("Eliminar Hecho");
        JMenuItem itemConsulta      = new JMenuItem("Consulta Simple");

        estilizarMenuItem(itemHechos);
        estilizarMenuItem(itemReglas);
        estilizarMenuItem(itemAgregarHecho);
        estilizarMenuItem(itemEliminarHecho);
        estilizarMenuItem(itemConsulta);

        // Sin listeners, el Controlador los registra con getItemXxx()
        menuBC.add(itemHechos);
        menuBC.add(itemReglas);
        menuBC.addSeparator();
        menuBC.add(itemAgregarHecho);
        menuBC.add(itemEliminarHecho);
        menuBC.addSeparator();
        menuBC.add(itemConsulta);
        menuBar.add(menuBC);
        setJMenuBar(menuBar);

        add(mainPanel);
        cardLayout.show(mainPanel, "preferencias");
    }

    // ==================== DIALOGS ====================

    /**
     * Construye y muestra el dialog de Ver Hechos.
     * Lee la sección HECHOS del archivo .pl y la muestra
     * en un JTextArea con fuente monoespaciada.
     * Llamado por el Controlador al hacer clic en "Ver Hechos".
     */
    public void mostrarDialogoHechos() {
        JDialog dialog = new JDialog(this, "Hechos - Base de Conocimiento", true);
        dialog.setSize(520, 520);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(30, 30, 46));
        dialog.setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Hechos - Base de Conocimiento", SwingConstants.CENTER);
        titulo.setForeground(new Color(137, 80, 252));
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        String contenido = leerSeccionProlog("HECHOS");
        JTextArea textArea = crearTextAreaProlog(contenido);
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));
        scroll.getViewport().setBackground(new Color(40, 42, 58));

        cerrarHechosBtn = new JButton("Cerrar");
        estilizarBoton(cerrarHechosBtn, new Color(80, 80, 100));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(30, 30, 46));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        bottomPanel.add(cerrarHechosBtn);

        dialog.add(titulo,       BorderLayout.NORTH);
        dialog.add(scroll,       BorderLayout.CENTER);
        dialog.add(bottomPanel,  BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * Construye y muestra el dialog de Ver Reglas.
     * Lee la sección REGLAS del archivo .pl y la muestra
     * en un JTextArea con fuente monoespaciada.
     * Llamado por el Controlador al hacer clic en "Ver Reglas".
     */
    public void mostrarDialogoReglas() {
        JDialog dialog = new JDialog(this, "Reglas - Base de Conocimiento", true);
        dialog.setSize(520, 520);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(30, 30, 46));
        dialog.setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Reglas - Base de Conocimiento", SwingConstants.CENTER);
        titulo.setForeground(new Color(137, 80, 252));
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        String contenido = leerSeccionProlog("REGLAS");
        JTextArea textArea = crearTextAreaProlog(contenido);
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));
        scroll.getViewport().setBackground(new Color(40, 42, 58));

        cerrarReglasBtn = new JButton("Cerrar");
        estilizarBoton(cerrarReglasBtn, new Color(80, 80, 100));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(30, 30, 46));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        bottomPanel.add(cerrarReglasBtn);

        dialog.add(titulo,      BorderLayout.NORTH);
        dialog.add(scroll,      BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * Construye y muestra el dialog de Agregar Hecho.
     * Contiene un combo de tipo, campos de valor/título/artista
     * y los botones agregarHechoBtn y cerrarAgregarBtn.
     * Los campos de título y artista inician ocultos.
     * Llamado por el Controlador al hacer clic en "Agregar Hecho".
     */
    public void mostrarDialogoAgregarHecho() {
        JDialog dialog = new JDialog(this, "Agregar Hecho", true);
        dialog.setSize(460, 340);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(30, 30, 46));
        dialog.setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Agregar nuevo hecho", SwingConstants.CENTER);
        titulo.setForeground(new Color(137, 80, 252));
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(30, 30, 46));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Combo tipo de hecho
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(crearLabel("Tipo de hecho:"), gbc);
        gbc.gridx = 1;
        tipoHechoCombo = new JComboBox<String>(new String[]{
            "estado_animo", "actividad", "energia", "cancion"
        });
        estilizarCombo(tipoHechoCombo);
        formPanel.add(tipoHechoCombo, gbc);

        // Campo valor principal
        gbc.gridx = 0; gbc.gridy = 1;
        labelTipoValor = crearLabel("Valor:");
        formPanel.add(labelTipoValor, gbc);
        gbc.gridx = 1;
        val1Field = crearTextField();
        formPanel.add(val1Field, gbc);

        // Campo título (solo para cancion)
        gbc.gridx = 0; gbc.gridy = 2;
        labelTitulo = crearLabel("Título (canción):");
        labelTitulo.setVisible(false);
        formPanel.add(labelTitulo, gbc);
        gbc.gridx = 1;
        val2Field = crearTextField();
        val2Field.setVisible(false);
        formPanel.add(val2Field, gbc);

        // Campo artista (solo para cancion)
        gbc.gridx = 0; gbc.gridy = 3;
        labelArtista = crearLabel("Artista (canción):");
        labelArtista.setVisible(false);
        formPanel.add(labelArtista, gbc);
        gbc.gridx = 1;
        val3Field = crearTextField();
        val3Field.setVisible(false);
        formPanel.add(val3Field, gbc);

        // Botones
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 20, 10, 20);
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        botonesPanel.setBackground(new Color(30, 30, 46));

        agregarHechoBtn = new JButton("Agregar Hecho");
        estilizarBoton(agregarHechoBtn, new Color(137, 80, 252));

        cerrarAgregarBtn = new JButton("Cerrar");
        estilizarBoton(cerrarAgregarBtn, new Color(80, 80, 100));

        botonesPanel.add(agregarHechoBtn);
        botonesPanel.add(cerrarAgregarBtn);
        formPanel.add(botonesPanel, gbc);

        dialog.add(titulo,    BorderLayout.NORTH);
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    /**
     * Construye y muestra el dialog de Eliminar Hecho.
     * Carga en una JList todos los hechos actuales del archivo .pl.
     * Contiene los botones eliminarHechoBtn y cerrarEliminarBtn.
     * Llamado por el Controlador al hacer clic en "Eliminar Hecho".
     */
    public void mostrarDialogoEliminarHecho() {
        JDialog dialog = new JDialog(this, "Eliminar Hecho", true);
        dialog.setSize(520, 480);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(30, 30, 46));
        dialog.setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Selecciona el hecho a eliminar", SwingConstants.CENTER);
        titulo.setForeground(new Color(137, 80, 252));
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        List<String> hechos = leerHechosDelArchivo();
        listModel = new DefaultListModel<String>();
        for (String h : hechos) {
            listModel.addElement(h);
        }

        listaHechos = new JList<String>(listModel);
        listaHechos.setBackground(new Color(40, 42, 58));
        listaHechos.setForeground(Color.WHITE);
        listaHechos.setFont(new Font("Monospaced", Font.PLAIN, 13));
        listaHechos.setSelectionBackground(new Color(137, 80, 252));
        listaHechos.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JScrollPane scroll = new JScrollPane(listaHechos);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        scroll.getViewport().setBackground(new Color(40, 42, 58));

        eliminarHechoBtn = new JButton("Eliminar Seleccionado");
        estilizarBoton(eliminarHechoBtn, new Color(200, 60, 60));

        cerrarEliminarBtn = new JButton("Cerrar");
        estilizarBoton(cerrarEliminarBtn, new Color(80, 80, 100));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setBackground(new Color(30, 30, 46));
        bottomPanel.add(eliminarHechoBtn);
        bottomPanel.add(cerrarEliminarBtn);

        dialog.add(titulo,       BorderLayout.NORTH);
        dialog.add(scroll,       BorderLayout.CENTER);
        dialog.add(bottomPanel,  BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * Construye y muestra el dialog de Consulta Simple.
     * Permite consultar si un valor existe en la base de conocimiento
     * usando un solo atributo como parámetro.
     * Contiene comboTipoConsulta, campoConsulta, resultadoConsultaLabel,
     * consultarBtn y cerrarConsultaBtn.
     * Llamado por el Controlador al hacer clic en "Consulta Simple".
     */
    public void mostrarDialogoConsulta() {
        JDialog dialog = new JDialog(this, "Consulta Simple", true);
        dialog.setSize(460, 300);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(30, 30, 46));
        dialog.setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Consulta Simple", SwingConstants.CENTER);
        titulo.setForeground(new Color(137, 80, 252));
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(30, 30, 46));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Combo para seleccionar el tipo a consultar
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(crearLabel("Consultar:"), gbc);
        gbc.gridx = 1;
        comboTipoConsulta = new JComboBox<String>(new String[]{
            "estado_animo", "actividad", "energia", "idioma", "cancion"
        });
        estilizarCombo(comboTipoConsulta);
        formPanel.add(comboTipoConsulta, gbc);

        // Campo para ingresar el valor a consultar
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(crearLabel("Valor:"), gbc);
        gbc.gridx = 1;
        campoConsulta = crearTextField();
        formPanel.add(campoConsulta, gbc);

        // Label para mostrar el resultado de la consulta
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        resultadoConsultaLabel = new JLabel("", SwingConstants.CENTER);
        resultadoConsultaLabel.setForeground(new Color(137, 80, 252));
        resultadoConsultaLabel.setFont(new Font("Arial", Font.BOLD, 13));
        formPanel.add(resultadoConsultaLabel, gbc);

        // Botones
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 20, 10, 20);
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        botonesPanel.setBackground(new Color(30, 30, 46));

        consultarBtn = new JButton("Consultar");
        estilizarBoton(consultarBtn, new Color(137, 80, 252));

        cerrarConsultaBtn = new JButton("Cerrar");
        estilizarBoton(cerrarConsultaBtn, new Color(80, 80, 100));

        botonesPanel.add(consultarBtn);
        botonesPanel.add(cerrarConsultaBtn);
        formPanel.add(botonesPanel, gbc);

        dialog.add(titulo,    BorderLayout.NORTH);
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    // ==================== LÓGICA ARCHIVO PROLOG ====================

    /**
     * Lee una sección completa del archivo .pl según el nombre indicado.
     * Busca la línea con el marcador de sección y lee hasta encontrar
     * el siguiente marcador de sección diferente.
     *
     * @param seccion nombre de la sección: "HECHOS" o "REGLAS"
     * @return contenido de la sección como String
     */
    private String leerSeccionProlog(String seccion) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(prologFile));
            String linea;
            boolean enSeccion = false;
            while ((linea = br.readLine()) != null) {
                if (linea.contains("% ==================== " + seccion)) {
                    enSeccion = true;
                }
                if (enSeccion) {
                    if (!linea.contains(seccion) &&
                        linea.contains("% ====================") &&
                        sb.length() > 0) {
                        break;
                    }
                    sb.append(linea).append("\n");
                }
            }
            br.close();
        } catch (IOException ex) {
            sb.append("No se pudo leer el archivo: ").append(ex.getMessage());
        }
        return sb.toString();
    }

    /**
     * Lee únicamente las líneas de hechos del archivo .pl.
     * Ignora comentarios y se detiene al llegar a la sección REGLAS.
     *
     * @return lista de Strings con cada hecho en formato Prolog
     */
    private List<String> leerHechosDelArchivo() {
        List<String> hechos = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(prologFile));
            String linea;
            boolean enHechos = false;
            while ((linea = br.readLine()) != null) {
                if (linea.contains("% ==================== HECHOS")) enHechos = true;
                if (linea.contains("% ==================== REGLAS")) break;
                if (enHechos) {
                    String trimmed = linea.trim();
                    if (!trimmed.isEmpty() && !trimmed.startsWith("%")) {
                        hechos.add(trimmed);
                    }
                }
            }
            br.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Error al leer el archivo Prolog.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        return hechos;
    }

    /**
     * Agrega un nuevo hecho al archivo .pl justo antes de la sección REGLAS.
     *
     * @param hecho línea en formato Prolog, ej: estado_animo(contento).
     */
    public void agregarHechoAlArchivo(String hecho) {
        try {
            List<String> lineas = new ArrayList<String>();
            BufferedReader br = new BufferedReader(new FileReader(prologFile));
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
            br.close();

            int insertIndex = -1;
            for (int i = 0; i < lineas.size(); i++) {
                if (lineas.get(i).contains("% ==================== REGLAS")) {
                    insertIndex = i;
                    break;
                }
            }

            if (insertIndex != -1) {
                lineas.add(insertIndex, hecho);
            } else {
                lineas.add(hecho);
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(prologFile));
            for (String l : lineas) {
                bw.write(l);
                bw.newLine();
            }
            bw.close();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Error al escribir en el archivo Prolog.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Elimina un hecho específico del archivo .pl buscando la línea exacta.
     *
     * @param hecho línea en formato Prolog a eliminar, ej: estado_animo(contento).
     */
    public void eliminarHechoDelArchivo(String hecho) {
        try {
            List<String> lineas = new ArrayList<String>();
            BufferedReader br = new BufferedReader(new FileReader(prologFile));
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
            br.close();

            List<String> nuevasLineas = new ArrayList<String>();
            for (String l : lineas) {
                if (!l.trim().equals(hecho)) {
                    nuevasLineas.add(l);
                }
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(prologFile));
            for (String l : nuevasLineas) {
                bw.write(l);
                bw.newLine();
            }
            bw.close();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Error al eliminar del archivo Prolog.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==================== PANEL PREFERENCIAS ====================

    /**
     * Construye el panel de preferencias.
     * Los JComboBox se llenan con los getters del Modelo.
     * generarBtn no tiene listener, el Controlador lo registra.
     */
    private JPanel crearPanelPreferencias() {
        preferencesPanel = new JPanel(new BorderLayout());
        preferencesPanel.setBackground(new Color(30, 30, 46));

        JLabel titulo = new JLabel("¿Qué quieres escuchar hoy?", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(30, 30, 46));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Llenado con modelo.getEstadosAnimo()
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(crearLabel("Estado de ánimo:"), gbc);
        gbc.gridx = 1;
        moodCombo = new JComboBox<String>();
        for (String s : modelo.getEstadosAnimo()) {
            moodCombo.addItem(s);
        }
        estilizarCombo(moodCombo);
        formPanel.add(moodCombo, gbc);

        // Llenado con modelo.getActividades()
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(crearLabel("Actividad:"), gbc);
        gbc.gridx = 1;
        actividadCombo = new JComboBox<String>();
        for (String s : modelo.getActividades()) {
            actividadCombo.addItem(s);
        }
        estilizarCombo(actividadCombo);
        formPanel.add(actividadCombo, gbc);

        // Valores fijos de energía
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(crearLabel("Nivel de energía:"), gbc);
        gbc.gridx = 1;
        energiaCombo = new JComboBox<String>(new String[]{"alta", "media", "baja"});
        estilizarCombo(energiaCombo);
        formPanel.add(energiaCombo, gbc);

        // Llenado con modelo.getIdiomas()
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(crearLabel("Idioma preferido:"), gbc);
        gbc.gridx = 1;
        idiomaCombo = new JComboBox<String>();
        for (String s : modelo.getIdiomas()) {
            idiomaCombo.addItem(s);
        }
        estilizarCombo(idiomaCombo);
        formPanel.add(idiomaCombo, gbc);

        // Sin listener, el Controlador lo registra con getGenerarBtn()
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 60, 10, 60);
        generarBtn = new JButton("Generar Playlist");
        estilizarBoton(generarBtn, new Color(137, 80, 252));
        formPanel.add(generarBtn, gbc);

        preferencesPanel.add(titulo,    BorderLayout.NORTH);
        preferencesPanel.add(formPanel, BorderLayout.CENTER);
        return preferencesPanel;
    }

    // ==================== PANEL RESULTADOS ====================

    /**
     * Construye el panel de resultados.
     * generoLabel y playlistTable vacíos, se llenan con mostrarResultados().
     * volverBtn y nuevaBtn sin listener, el Controlador los registra.
     */
    private JPanel crearPanelResultados() {
        resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(new Color(30, 30, 46));

        generoLabel = new JLabel("", SwingConstants.CENTER);
        generoLabel.setFont(new Font("Arial", Font.BOLD, 22));
        generoLabel.setForeground(new Color(137, 80, 252));
        generoLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        tableModel = new DefaultTableModel(new String[]{"#", "Canción", "Artista"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        playlistTable = new JTable(tableModel);
        playlistTable.setBackground(new Color(49, 50, 68));
        playlistTable.setForeground(Color.WHITE);
        playlistTable.setFont(new Font("Arial", Font.PLAIN, 13));
        playlistTable.setRowHeight(28);
        playlistTable.getTableHeader().setBackground(new Color(137, 80, 252));
        playlistTable.getTableHeader().setForeground(Color.WHITE);
        playlistTable.setGridColor(new Color(60, 60, 80));

        JScrollPane scrollPane = new JScrollPane(playlistTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        scrollPane.getViewport().setBackground(new Color(49, 50, 68));

        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        botonesPanel.setBackground(new Color(30, 30, 46));

        // Sin listener, el Controlador lo registra con getVolverBtn()
        volverBtn = new JButton("Volver");
        estilizarBoton(volverBtn, new Color(80, 80, 100));

        // Sin listener, el Controlador lo registra con getNuevaBtn()
        nuevaBtn = new JButton("Nueva Búsqueda");
        estilizarBoton(nuevaBtn, new Color(137, 80, 252));

        botonesPanel.add(volverBtn);
        botonesPanel.add(nuevaBtn);

        resultPanel.add(generoLabel,  BorderLayout.NORTH);
        resultPanel.add(scrollPane,   BorderLayout.CENTER);
        resultPanel.add(botonesPanel, BorderLayout.SOUTH);
        return resultPanel;
    }

    // ==================== MÉTODOS PÚBLICOS PARA EL CONTROLADOR ====================

    /**
     * Actualiza el panel de resultados con el género y playlist recomendados.
     * Llamado por el Controlador después de consultar el Modelo.
     *
     * @param genero   género recomendado por Prolog
     * @param playlist lista donde cada elemento es String[]{ genero, titulo, artista }
     */
    public void mostrarResultados(String genero, List<String[]> playlist) {
        generoLabel.setText("Género recomendado: " + genero.toUpperCase());
        tableModel.setRowCount(0);
        int i = 1;
        for (String[] cancion : playlist) {
            tableModel.addRow(new Object[]{i++, cancion[1], cancion[2]});
        }
        cardLayout.show(mainPanel, "resultados");
    }

    /**
     * Muestra el panel de preferencias.
     * Llamado por el Controlador cuando el usuario presiona
     * Volver o Nueva Búsqueda.
     */
    public void mostrarPanelPreferencias() {
        cardLayout.show(mainPanel, "preferencias");
    }

    /**
     * Actualiza el label de resultado de la consulta simple.
     * Llamado por el Controlador después de ejecutar la consulta en Prolog.
     *
     * @param resultado mensaje a mostrar, ej: "Existe en la base de conocimiento"
     */
    public void mostrarResultadoConsulta(String resultado) {
        resultadoConsultaLabel.setText(resultado);
    }

    /**
     * Muestra u oculta los campos de título y artista en el dialog de agregar.
     * Llamado por el Controlador cuando cambia el combo de tipo de hecho.
     *
     * @param esCancion true muestra los campos, false los oculta
     */
    public void toggleCamposCancion(boolean esCancion) {
        labelTipoValor.setText(esCancion ? "Género:" : "Valor:");
        labelTitulo.setVisible(esCancion);
        val2Field.setVisible(esCancion);
        labelArtista.setVisible(esCancion);
        val3Field.setVisible(esCancion);
    }

    /**
     * Elimina un elemento de la lista de hechos en pantalla.
     * Llamado por el Controlador después de eliminar el hecho del archivo.
     *
     * @param hecho hecho a eliminar de la lista visual
     */
    public void eliminarHechoDeLista(String hecho) {
        listModel.removeElement(hecho);
    }

    /**
     * Limpia los campos del formulario de agregar hecho.
     * Llamado por el Controlador después de agregar exitosamente.
     */
    public void limpiarCamposAgregar() {
        val1Field.setText("");
        val2Field.setText("");
        val3Field.setText("");
    }

    // ==================== HELPERS ====================

    /**
     * Crea un JTextArea de solo lectura con estilo de código Prolog.
     */
    private JTextArea crearTextAreaProlog(String contenido) {
        JTextArea textArea = new JTextArea(contenido);
        textArea.setEditable(false);
        textArea.setBackground(new Color(40, 42, 58));
        textArea.setForeground(new Color(200, 200, 220));
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        textArea.setCaretColor(Color.WHITE);
        return textArea;
    }

    /**
     * Crea un JTextField con el estilo oscuro de la interfaz.
     */
    private JTextField crearTextField() {
        JTextField field = new JTextField();
        field.setBackground(new Color(49, 50, 68));
        field.setForeground(Color.WHITE);
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setCaretColor(Color.WHITE);
        field.setPreferredSize(new Dimension(200, 30));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 110)),
            BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));
        return field;
    }

    /**
     * Crea un JLabel con texto blanco estándar de la interfaz.
     */
    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }

    /**
     * Aplica el estilo oscuro estándar a un JComboBox.
     */
    private void estilizarCombo(JComboBox<String> combo) {
        combo.setBackground(new Color(49, 50, 68));
        combo.setForeground(Color.WHITE);
        combo.setFont(new Font("Arial", Font.PLAIN, 13));
        combo.setPreferredSize(new Dimension(200, 30));
    }

    /**
     * Aplica estilo de color, fuente y cursor a un JButton.
     */
    private void estilizarBoton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(200, 40));
    }

    /**
     * Aplica estilo oscuro estándar a un JMenuItem.
     */
    private void estilizarMenuItem(JMenuItem item) {
        item.setBackground(new Color(49, 50, 68));
        item.setForeground(Color.WHITE);
        item.setFont(new Font("Arial", Font.PLAIN, 13));
    }

    // ==================== SETTER ====================

    /**
     * Asigna el Controlador a la Vista.
     * Llamado desde el constructor del Controlador.
     */
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    // ==================== GETTERS BOTONES PRINCIPALES ====================

    /** Retorna generarBtn para que el Controlador registre su listener */
    public JButton getGenerarBtn() { return generarBtn; }

    /** Retorna volverBtn para que el Controlador registre su listener */
    public JButton getVolverBtn()  { return volverBtn;  }

    /** Retorna nuevaBtn para que el Controlador registre su listener */
    public JButton getNuevaBtn()   { return nuevaBtn;   }

    // ==================== GETTERS BOTONES DIALOGS ====================

    /** Retorna cerrarHechosBtn para que el Controlador registre su listener */
    public JButton getCerrarHechosBtn()    { return cerrarHechosBtn;   }

    /** Retorna cerrarReglasBtn para que el Controlador registre su listener */
    public JButton getCerrarReglasBtn()    { return cerrarReglasBtn;   }

    /** Retorna agregarHechoBtn para que el Controlador registre su listener */
    public JButton getAgregarHechoBtn()    { return agregarHechoBtn;   }

    /** Retorna cerrarAgregarBtn para que el Controlador registre su listener */
    public JButton getCerrarAgregarBtn()   { return cerrarAgregarBtn;  }

    /** Retorna eliminarHechoBtn para que el Controlador registre su listener */
    public JButton getEliminarHechoBtn()   { return eliminarHechoBtn;  }

    /** Retorna cerrarEliminarBtn para que el Controlador registre su listener */
    public JButton getCerrarEliminarBtn()  { return cerrarEliminarBtn; }

    /** Retorna consultarBtn para que el Controlador registre su listener */
    public JButton getConsultarBtn()       { return consultarBtn;      }

    /** Retorna cerrarConsultaBtn para que el Controlador registre su listener */
    public JButton getCerrarConsultaBtn()  { return cerrarConsultaBtn; }

    // ==================== GETTERS VALORES ====================

    /** Retorna el estado de ánimo seleccionado */
    public String getEstado()    { return (String) moodCombo.getSelectedItem();      }

    /** Retorna la actividad seleccionada */
    public String getActividad() { return (String) actividadCombo.getSelectedItem(); }

    /** Retorna el nivel de energía seleccionado */
    public String getEnergia()   { return (String) energiaCombo.getSelectedItem();   }

    /** Retorna el idioma seleccionado */
    public String getIdioma()    { return (String) idiomaCombo.getSelectedItem();    }

    /** Retorna el tipo de hecho seleccionado en el dialog de agregar */
    public String getTipoHecho() { return (String) tipoHechoCombo.getSelectedItem(); }

    /** Retorna el valor principal del campo en el dialog de agregar */
    public String getVal1()      { return val1Field.getText().trim().toLowerCase();  }

    /** Retorna el título de la canción en el dialog de agregar */
    public String getVal2()      { return val2Field.getText().trim();                }

    /** Retorna el artista de la canción en el dialog de agregar */
    public String getVal3()      { return val3Field.getText().trim();                }

    /** Retorna el hecho seleccionado en la lista del dialog de eliminar */
    public String getHechoSeleccionado() { return listaHechos.getSelectedValue();   }

    /** Retorna el tipo seleccionado en el combo de consulta simple */
    public String getTipoConsulta() { return (String) comboTipoConsulta.getSelectedItem(); }

    /** Retorna el valor ingresado en el campo de consulta simple */
    public String getValorConsulta() { return campoConsulta.getText().trim().toLowerCase(); }
}