/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente.MVC;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Clase Vista - Capa de presentación del patrón MVC.
 *
 * Responsabilidades:
 * - Recibir el Modelo en el constructor y usarlo para
 *   llenar los JComboBox con sus listas de datos.
 * - Construir y posicionar todos los componentes gráficos.
 * - Declarar todos los botones como atributos de la clase.
 * - Deshabilitar combos cuando hay combinaciones mutuamente excluyentes
 *   según las reglas de Prolog.
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
    private JTabbedPane tabbedPane;
    private JComboBox<String> moodCombo;      // Llenado con modelo.getEstadosAnimo()
    private JComboBox<String> actividadCombo; // Llenado con modelo.getActividades()
    private JComboBox<String> energiaCombo;   // Valores: alta, media, baja
    private JComboBox<String> idiomaCombo;    // Llenado con modelo.getIdiomas()
    private JButton generarBtn;               // Sin listener, lo registra el Controlador
    private JLabel advertenciaActividad;      // Aviso de restricción en actividad
    private JLabel advertenciaEnergia;        // Aviso de restricción en energía

    // ===== Componentes Panel de Resultados =====
    private JPanel resultPanel;
    private JLabel generoLabel;               // Muestra los filtros usados
    private JTable playlistTable;             // Tabla con canciones recomendadas
    private DefaultTableModel tableModel;     // Modelo de datos de la tabla
    private JButton volverBtn;                // Sin listener, lo registra el Controlador
    private JButton nuevaBtn;                 // Sin listener, lo registra el Controlador

    // ===== Componentes Dialog Ver Hechos =====
    private JButton cerrarHechosBtn;

    // ===== Componentes Dialog Ver Reglas =====
    private JButton cerrarReglasBtn;

    // ===== Componentes Dialog Agregar Canción =====
    private JTextField idField;               // ID de la canción, ej: c31
    private JTextField tituloField;           // Título de la canción
    private JTextField artistaField;          // Artista de la canción
    private JTextField generosField;          // Géneros en formato [g1, g2]
    private JComboBox<String> idiomaAgregarCombo; // Idioma de la canción
    private JButton agregarHechoBtn;          // Confirma agregar
    private JButton cerrarAgregarBtn;         // Cierra el dialog

    // ===== Componentes Dialog Eliminar Canción =====
    private JList<String> listaHechos;        // Lista de canciones activas
    private DefaultListModel<String> listModel;
    private JButton eliminarHechoBtn;         // Confirma eliminar
    private JButton cerrarEliminarBtn;        // Cierra el dialog

    // ===== Componentes Dialog Consulta Simple =====
    private JTextField campoConsulta;         // ID a consultar
    private JLabel resultadoConsultaLabel;    // Muestra el resultado
    private JButton consultarBtn;             // Ejecuta la consulta
    private JButton cerrarConsultaBtn;        // Cierra el dialog

    // Variable para pausar listeners mientras se actualizan los combos por código
    private boolean actualizandoCombos = false;
    
    // ===== Componentes Menu Bar =====
    private JMenuItem itemHechos;
    private JMenuItem itemReglas;
    private JMenuItem itemAgregarHecho;
    private JMenuItem itemEliminarHecho;
    private JMenuItem itemConsulta;
    
    /**
     * Constructor de la Vista.
     * Recibe el Modelo para obtener las listas que llenarán los JComboBox.
     *
     * @param modelo objeto Modelo ya instanciado desde el Main
     */
    public Vista(Modelo modelo) {
        this.modelo = modelo;
        initUI();
    }

    /**
     * Inicializa la ventana principal.
     * Configura tamaño, título, CardLayout, paneles y menú superior.
     */
    private void initUI() {
        setTitle("Sistema Experto - Recomendador Musical");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(620, 550);
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

        // Quita la declaración de tipo, déjalos así:
        itemHechos        = new JMenuItem("Ver Hechos");
        itemReglas        = new JMenuItem("Ver Reglas");
        itemAgregarHecho  = new JMenuItem("Agregar Canción");
        itemEliminarHecho = new JMenuItem("Eliminar Canción");
        itemConsulta      = new JMenuItem("Consulta Simple");

        estilizarMenuItem(itemHechos);
        estilizarMenuItem(itemReglas);
        estilizarMenuItem(itemAgregarHecho);
        estilizarMenuItem(itemEliminarHecho);
        estilizarMenuItem(itemConsulta);

        // Sin listeners, el Controlador los registra mediante getItemXxx()
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
     * Recibe el contenido ya leído por el Modelo.
     * Llamado por el Controlador al hacer clic en "Ver Hechos".
     *
     * @param contenido texto de la sección HECHOS del archivo .pl
     */
    public JDialog mostrarDialogoHechos(String contenido) {
        JDialog dialog = new JDialog(this,
            "Hechos - Base de Conocimiento", true);
        dialog.setSize(540, 540);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(30, 30, 46));
        dialog.setLayout(new BorderLayout());

        JLabel titulo = new JLabel(
            "Hechos - Base de Conocimiento", SwingConstants.CENTER);
        titulo.setForeground(new Color(137, 80, 252));
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

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

        dialog.add(titulo,      BorderLayout.NORTH);
        dialog.add(scroll,      BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);
        
        //dialog.setVisible(true);
        
        return dialog;
    }

    /**
     * Construye y muestra el dialog de Ver Reglas.
     * Recibe el contenido ya leído por el Modelo.
     * Llamado por el Controlador al hacer clic en "Ver Reglas".
     *
     */
    public JDialog mostrarDialogoReglas(String contenido) {
        JDialog dialog = new JDialog(this,
            "Reglas - Base de Conocimiento", true);
        dialog.setSize(540, 540);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(30, 30, 46));
        dialog.setLayout(new BorderLayout());

        JLabel titulo = new JLabel(
            "Reglas - Base de Conocimiento", SwingConstants.CENTER);
        titulo.setForeground(new Color(137, 80, 252));
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

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
//        dialog.setVisible(true);
        return dialog;
    }

    /**
     * Construye y muestra el dialog de Agregar Canción.
     * Contiene campos para ID, título, artista, géneros e idioma.
     * El formato de géneros debe ingresarse como [genero1, genero2].
     * Llamado por el Controlador al hacer clic en "Agregar Canción".
     */
    public JDialog mostrarDialogoAgregarHecho() {
        JDialog dialog = new JDialog(this, "Agregar Canción", true);
        dialog.setSize(480, 400);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(30, 30, 46));
        dialog.setLayout(new BorderLayout());

        JLabel titulo = new JLabel(
            "Agregar nueva canción", SwingConstants.CENTER);
        titulo.setForeground(new Color(137, 80, 252));
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(30, 30, 46));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 20, 8, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ID de la canción
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(crearLabel("ID (ej: c31):"), gbc);
        gbc.gridx = 1;
        idField = crearTextField();
        formPanel.add(idField, gbc);

        // Título
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(crearLabel("Título:"), gbc);
        gbc.gridx = 1;
        tituloField = crearTextField();
        formPanel.add(tituloField, gbc);

        // Artista
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(crearLabel("Artista:"), gbc);
        gbc.gridx = 1;
        artistaField = crearTextField();
        formPanel.add(artistaField, gbc);

        // Géneros en formato Prolog [g1, g2]
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(crearLabel("Géneros [g1, g2]:"), gbc);
        gbc.gridx = 1;
        generosField = crearTextField();
        generosField.setToolTipText("Ej: [pop, dance] o [rock, balada]");
        formPanel.add(generosField, gbc);

        // Idioma
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(crearLabel("Idioma:"), gbc);
        gbc.gridx = 1;
        idiomaAgregarCombo = new JComboBox<String>(
            new String[]{"espanol", "ingles", "instrumental"});
        estilizarCombo(idiomaAgregarCombo);
        formPanel.add(idiomaAgregarCombo, gbc);

        // Botones
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 20, 10, 20);
        JPanel botonesPanel = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 15, 0));
        botonesPanel.setBackground(new Color(30, 30, 46));

        agregarHechoBtn = new JButton("Agregar");
        estilizarBoton(agregarHechoBtn, new Color(137, 80, 252));

        cerrarAgregarBtn = new JButton("Cerrar");
        estilizarBoton(cerrarAgregarBtn, new Color(80, 80, 100));

        botonesPanel.add(agregarHechoBtn);
        botonesPanel.add(cerrarAgregarBtn);
        formPanel.add(botonesPanel, gbc);

        dialog.add(titulo,    BorderLayout.NORTH);
        dialog.add(formPanel, BorderLayout.CENTER);
//        dialog.setVisible(true);
        return dialog;
    }

    /**
     * Construye y muestra el dialog de Eliminar Canción.
     * Recibe la lista de canciones activas ya obtenida por el Modelo.
     * Muestra formato: "ID - Título - Artista".
     * Llamado por el Controlador al hacer clic en "Eliminar Canción".
     *
     * @param canciones lista de canciones activas donde cada elemento
     *                  es String[]{ id, titulo, artista, generos, idioma }
     */
    public JDialog mostrarDialogoEliminarHecho(List<String[]> canciones) {
        JDialog dialog = new JDialog(this, "Eliminar Canción", true);
        dialog.setSize(560, 500);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(30, 30, 46));
        dialog.setLayout(new BorderLayout());

        JLabel titulo = new JLabel(
            "Selecciona la canción a eliminar", SwingConstants.CENTER);
        titulo.setForeground(new Color(137, 80, 252));
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        // Poblar lista con las canciones recibidas del Controlador
        listModel = new DefaultListModel<String>();
        for (String[] c : canciones) {
            listModel.addElement(c[0] + " - " + c[1] + " - " + c[2]);
        }

        listaHechos = new JList<String>(listModel);
        listaHechos.setBackground(new Color(40, 42, 58));
        listaHechos.setForeground(Color.WHITE);
        listaHechos.setFont(new Font("Monospaced", Font.PLAIN, 13));
        listaHechos.setSelectionBackground(new Color(137, 80, 252));
        listaHechos.setBorder(
            BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JScrollPane scroll = new JScrollPane(listaHechos);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        scroll.getViewport().setBackground(new Color(40, 42, 58));

        // Leyenda explicativa
        JLabel leyenda = new JLabel(
            "  Lógico: oculta en sesión  |  Físico: elimina permanentemente",
            SwingConstants.LEFT);
        leyenda.setForeground(new Color(180, 160, 220));
        leyenda.setFont(new Font("Arial", Font.ITALIC, 11));
        leyenda.setBorder(BorderFactory.createEmptyBorder(5, 15, 0, 0));

        eliminarHechoBtn = new JButton("Eliminar Seleccionado");
        estilizarBoton(eliminarHechoBtn, new Color(200, 60, 60));

        cerrarEliminarBtn = new JButton("Cerrar");
        estilizarBoton(cerrarEliminarBtn, new Color(80, 80, 100));

        JPanel botonesPanel = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 15, 10));
        botonesPanel.setBackground(new Color(30, 30, 46));
        botonesPanel.add(eliminarHechoBtn);
        botonesPanel.add(cerrarEliminarBtn);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(30, 30, 46));
        bottomPanel.add(leyenda,      BorderLayout.NORTH);
        bottomPanel.add(botonesPanel, BorderLayout.SOUTH);

        dialog.add(titulo,      BorderLayout.NORTH);
        dialog.add(scroll,      BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);
//        dialog.setVisible(true);
        
        return dialog;
        
    }

    /**
     * Construye y muestra el dialog de Consulta Simple.
     * El usuario ingresa un ID y el Controlador verifica
     * si existe en la base de conocimiento mediante el Modelo.
     * Llamado por el Controlador al hacer clic en "Consulta Simple".
     */
    public JDialog mostrarDialogoConsulta() {
        JDialog dialog = new JDialog(this, "Consulta Simple", true);
        dialog.setSize(460, 360);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(30, 30, 46));
        dialog.setLayout(new BorderLayout());

        JLabel titulo = new JLabel(
            "Consultar canción por ID", SwingConstants.CENTER);
        titulo.setForeground(new Color(137, 80, 252));
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(30, 30, 46));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo ID a consultar
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(crearLabel("ID de canción:"), gbc);
        gbc.gridx = 1;
        campoConsulta = crearTextField();
        campoConsulta.setToolTipText("Ej: c01, c15, c30");
        formPanel.add(campoConsulta, gbc);

        // Label para mostrar el resultado
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        resultadoConsultaLabel = new JLabel("", SwingConstants.CENTER);
        resultadoConsultaLabel.setForeground(new Color(137, 80, 252));
        resultadoConsultaLabel.setFont(new Font("Arial", Font.BOLD, 13));
        formPanel.add(resultadoConsultaLabel, gbc);

        // Botones
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 20, 10, 20);
        JPanel botonesPanel = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 15, 0));
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
//        dialog.setVisible(true);
        
        return dialog;
    }

    // ==================== PANEL PREFERENCIAS ====================

    /**
     * Construye el panel de preferencias.
     * Los JComboBox se llenan con los getters del Modelo.
     * Incluye labels de advertencia para restricciones entre combos.
     * El moodCombo tiene listener interno para exclusiones.
     * El energiaCombo tiene listener interno para exclusiones.
     * generarBtn no tiene listener, el Controlador lo registra.
     */
    private JPanel crearPanelPreferencias() {
        preferencesPanel = new JPanel(new BorderLayout());
        preferencesPanel.setBackground(new Color(30, 30, 46));

        JLabel titulo = new JLabel(
            "¿Qué quieres escuchar hoy?", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(49, 50, 68));
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));

        // TAB 1: Actividad
        JPanel tabActividad = new JPanel(new GridBagLayout());
        tabActividad.setBackground(new Color(30, 30, 46));
        GridBagConstraints gbcAct = new GridBagConstraints();
        gbcAct.insets = new Insets(10, 20, 10, 20);
        gbcAct.gridx = 0; gbcAct.gridy = 0;
        tabActividad.add(crearLabel("Actividad:"), gbcAct);
        gbcAct.gridx = 1;
        actividadCombo = new JComboBox<String>();
        for (String s : modelo.getActividades()) {
            actividadCombo.addItem(s);
        }
        estilizarCombo(actividadCombo);
        tabActividad.add(actividadCombo, gbcAct);
        tabbedPane.addTab("Actividad", tabActividad);

        // TAB 2: Emoción
        JPanel tabEmocion = new JPanel(new GridBagLayout());
        tabEmocion.setBackground(new Color(30, 30, 46));
        GridBagConstraints gbcEmo = new GridBagConstraints();
        gbcEmo.insets = new Insets(10, 20, 10, 20);
        gbcEmo.gridx = 0; gbcEmo.gridy = 0;
        tabEmocion.add(crearLabel("Estado de ánimo:"), gbcEmo);
        gbcEmo.gridx = 1;
        moodCombo = new JComboBox<String>();
        for (String s : modelo.getEstadosAnimo()) {
            moodCombo.addItem(s);
        }
        estilizarCombo(moodCombo);
        tabEmocion.add(moodCombo, gbcEmo);
        tabbedPane.addTab("Emoción", tabEmocion);

        // TAB 3: Perfil (Energía e Idioma)
        JPanel tabPerfil = new JPanel(new GridBagLayout());
        tabPerfil.setBackground(new Color(30, 30, 46));
        GridBagConstraints gbcPer = new GridBagConstraints();
        gbcPer.insets = new Insets(10, 20, 10, 20);
        gbcPer.gridx = 0; gbcPer.gridy = 0;
        tabPerfil.add(crearLabel("Energía:"), gbcPer);
        gbcPer.gridx = 1;
        energiaCombo = new JComboBox<String>(new String[]{"Energia", "alta", "media", "baja", "cualquiera"});
        estilizarCombo(energiaCombo);
        tabPerfil.add(energiaCombo, gbcPer);

        gbcPer.gridx = 0; gbcPer.gridy = 1;
        tabPerfil.add(crearLabel("Idioma preferido:"), gbcPer);
        gbcPer.gridx = 1;
        idiomaCombo = new JComboBox<String>();
        for (String s : modelo.getIdiomas()) {
            idiomaCombo.addItem(s);
        }
        estilizarCombo(idiomaCombo);
        tabPerfil.add(idiomaCombo, gbcPer);
        tabbedPane.addTab("Perfil", tabPerfil);

        // Advertencias (Ocultas pero instanciadas para que no sea null)
        advertenciaActividad = new JLabel("", SwingConstants.CENTER);
        advertenciaEnergia = new JLabel("", SwingConstants.CENTER);

        // ===== Botón Generar =====
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(30, 30, 46));
        generarBtn = new JButton("Generar Playlist");
        estilizarBoton(generarBtn, new Color(137, 80, 252));
        bottomPanel.add(generarBtn);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        preferencesPanel.add(titulo, BorderLayout.NORTH);
        preferencesPanel.add(tabbedPane, BorderLayout.CENTER);
        preferencesPanel.add(bottomPanel, BorderLayout.SOUTH);

        return preferencesPanel;
    }

    // ==================== LÓGICA DE EXCLUSIONES ====================

    /**
     * Registra los listeners internos de moodCombo y energiaCombo
     * para aplicar las restricciones mutuamente excluyentes
     * definidas en las reglas de Prolog.
     */
    private void registrarListenerExclusiones() { }


    /**
     * Aplica restricciones de energía y actividad según la emoción.
     * Basado en las reglas de inferencia de emoción de Prolog.
     *
     * @param emocion estado de ánimo seleccionado
     */
    private void aplicarExclusionesPorEmocion(String emocion) {
        resetearCombos();
        if (emocion.equals("feliz")) {
            fijarComboEnergia("alta",
                "!! Feliz requiere energía alta");
            fijarComboActividad("fiesta",
                "!! Feliz es compatible con fiesta");

        } else if (emocion.equals("energetico")) {
            fijarComboEnergia("alta",
                "!! Energético requiere energía alta");
            filtrarComboActividad(
                new String[]{"ejercicio", "fiesta", "conducir"},
                "!! Energético: ejercicio, fiesta o conducir");

        } else if (emocion.equals("relajado")) {
            filtrarComboEnergia(
                new String[]{"baja", "media"},
                "!! Relajado: energía baja o media");
            filtrarComboActividad(
                new String[]{"estudiar", "dormir", "trabajar"},
                "!! Relajado: estudiar, dormir o trabajar");

        } else if (emocion.equals("triste")    ||
                   emocion.equals("romantico") ||
                   emocion.equals("nostalgico")) {
            filtrarComboEnergia(
                new String[]{"baja", "media"},
                "!! " + emocion + ": energía baja o media");
            fijarComboActividad("trabajar",
                "!! " + emocion + " es compatible con trabajar");
        }
    }

    /**
     * Aplica restricciones en actividadCombo según el nivel de energía.
     * Solo se ejecuta si el moodCombo no restringió ya la actividad.
     * Basado en las reglas de adecuación de actividad de Prolog.
     *
     * @param energia nivel de energía seleccionado
     */
    private void aplicarExclusionesPorEnergia(String energia) {
        if (energia.equals("alta")) {
            filtrarComboActividad(
                new String[]{"ejercicio", "fiesta", "conducir"},
                "!! Energía alta: ejercicio, fiesta o conducir");
        } else if (energia.equals("media")) {
            filtrarComboActividad(
                new String[]{"conducir", "trabajar", "estudiar"},
                "!! Energía media: conducir, trabajar o estudiar");
        } else if (energia.equals("baja")) {
            filtrarComboActividad(
                new String[]{"dormir", "trabajar", "estudiar"},
                "!! Energía baja: dormir, trabajar o estudiar");
        }
    }

    /**
     * Fija el energiaCombo a un único valor y lo deshabilita.
     *
     * @param valor   valor único a mostrar
     * @param mensaje advertencia a mostrar
     */
    private void fijarComboEnergia(String valor, String mensaje) {
        energiaCombo.removeAllItems();
        energiaCombo.addItem(valor);
        energiaCombo.setEnabled(false);
        energiaCombo.setBackground(new Color(60, 60, 80));
        advertenciaEnergia.setText(mensaje);
        advertenciaEnergia.setVisible(true);
    }

    /**
     * Filtra el energiaCombo para mostrar solo los valores permitidos.
     *
     * @param valores  valores permitidos
     * @param mensaje  advertencia a mostrar
     */
    private void filtrarComboEnergia(String[] valores, String mensaje) {
        actualizandoCombos = true;
        energiaCombo.removeAllItems();
        for (String v : valores) {
            energiaCombo.addItem(v);
        }

        if (valores.length == 1) {
            energiaCombo.setEnabled(false);
            energiaCombo.setBackground(new Color(60, 60, 80));
        } else {
            // EXPLICITAMENTE volver a habilitar si hay más de 1 opción
            energiaCombo.setEnabled(true);
            energiaCombo.setBackground(new Color(49, 50, 68));
        }
        advertenciaEnergia.setText(mensaje);
        advertenciaEnergia.setVisible(true);
        actualizandoCombos = false;
    }

    /**
     * Fija el actividadCombo a un único valor y lo deshabilita.
     *
     * @param valor   valor único a mostrar
     * @param mensaje advertencia a mostrar
     */
    private void fijarComboActividad(String valor, String mensaje) {
        actividadCombo.removeAllItems();
        actividadCombo.addItem(valor);
        actividadCombo.setEnabled(false);
        actividadCombo.setBackground(new Color(60, 60, 80));
        advertenciaActividad.setText(mensaje);
        advertenciaActividad.setVisible(true);
    }

    /**
     * Filtra el actividadCombo para mostrar solo los valores permitidos.
     *
     * @param valores  valores permitidos
     * @param mensaje  advertencia a mostrar
     */
    private void filtrarComboActividad(String[] valores, String mensaje) {
    actualizandoCombos = true;
    actividadCombo.removeAllItems();
    for (String v : valores) {
        actividadCombo.addItem(v);
    }
    
    if (valores.length == 1) {
        actividadCombo.setEnabled(false);
        actividadCombo.setBackground(new Color(60, 60, 80));
    } else {
        // EXPLICITAMENTE volver a habilitar si hay más de 1 opción
        actividadCombo.setEnabled(true);
        actividadCombo.setBackground(new Color(49, 50, 68));
    }
    advertenciaActividad.setText(mensaje);
    advertenciaActividad.setVisible(true);
    actualizandoCombos = false;
}

    /**
     * Resetea los combos de actividad y energía a su estado completo.
     * Oculta las advertencias y rehabilita todos los valores.
     * Se llama antes de aplicar nuevas restricciones.
     */
    private void resetearCombos() {
        actualizandoCombos = true; // Pausamos listeners

        energiaCombo.removeAllItems();
        energiaCombo.addItem("alta");
        energiaCombo.addItem("media");
        energiaCombo.addItem("baja");
        energiaCombo.setEnabled(true);
        energiaCombo.setBackground(new Color(49, 50, 68));
        advertenciaEnergia.setVisible(false);

        actividadCombo.removeAllItems();
        for (String s : modelo.getActividades()) {
            actividadCombo.addItem(s);
        }
        actividadCombo.setEnabled(true);
        actividadCombo.setBackground(new Color(49, 50, 68));
        advertenciaActividad.setVisible(false);

        actualizandoCombos = false; // Reactivamos listeners
    }

    // ==================== PANEL RESULTADOS ====================

    /**
     * Construye el panel de resultados.
     * Tabla con columnas: #, Canción, Artista, Géneros.
     * volverBtn y nuevaBtn sin listener, el Controlador los registra.
     */
    private JPanel crearPanelResultados() {
        resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(new Color(30, 30, 46));

        generoLabel = new JLabel("", SwingConstants.CENTER);
        generoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        generoLabel.setForeground(new Color(137, 80, 252));
        generoLabel.setBorder(
            BorderFactory.createEmptyBorder(20, 0, 10, 0));

        tableModel = new DefaultTableModel(
            new String[]{"#", "Canción", "Artista", "Géneros"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        playlistTable = new JTable(tableModel);
        playlistTable.setBackground(new Color(49, 50, 68));
        playlistTable.setForeground(Color.WHITE);
        playlistTable.setFont(new Font("Arial", Font.PLAIN, 13));
        playlistTable.setRowHeight(28);
        playlistTable.getTableHeader().setBackground(
            new Color(137, 80, 252));
        playlistTable.getTableHeader().setForeground(Color.WHITE);
        playlistTable.setGridColor(new Color(60, 60, 80));

        // Anchos de columnas
        playlistTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        playlistTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        playlistTable.getColumnModel().getColumn(2).setPreferredWidth(130);
        playlistTable.getColumnModel().getColumn(3).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(playlistTable);
        scrollPane.setBorder(
            BorderFactory.createEmptyBorder(0, 20, 0, 20));
        scrollPane.getViewport().setBackground(new Color(49, 50, 68));

        JPanel botonesPanel = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 20, 15));
        botonesPanel.setBackground(new Color(30, 30, 46));

        volverBtn = new JButton("Volver");
        estilizarBoton(volverBtn, new Color(80, 80, 100));

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
     * Actualiza el panel de resultados con la playlist obtenida de Prolog.
     * Cada canción es String[]{ titulo, artista, generos }.
     * Llamado por el Controlador después de consultar el Modelo.
     *
     * @param filtros  descripción de filtros usados para el label
     * @param playlist lista de canciones
     */
    public void mostrarResultados(String filtros, List<String[]> playlist) {
        generoLabel.setText("Resultados: " + filtros);
        tableModel.setRowCount(0);
        int i = 1;
        for (String[] cancion : playlist) {
            tableModel.addRow(new Object[]{
                i++, cancion[0], cancion[1], cancion[2]});
        }
        cardLayout.show(mainPanel, "resultados");
    }

    /**
     * Muestra el panel de preferencias y resetea los combos.
     * Llamado por el Controlador cuando el usuario presiona
     * Volver o Nueva Búsqueda.
     */
    public void mostrarPanelPreferencias() {
        moodCombo.setSelectedIndex(0); 
        idiomaCombo.setSelectedIndex(0);
        actividadCombo.setSelectedIndex(0);
        energiaCombo.setSelectedIndex(0);
        cardLayout.show(mainPanel, "preferencias");
    }

    /**
     * Actualiza el label de resultado en el dialog de consulta.
     * Llamado por el Controlador después de ejecutar la consulta.
     *
     * @param resultado mensaje a mostrar
     */
    public void mostrarResultadoConsulta(String resultado) {
        resultadoConsultaLabel.setText(resultado);
    }

    /**
     * Elimina un elemento de la lista de canciones en pantalla.
     * Llamado por el Controlador después de eliminar del Modelo.
     *
     * @param item elemento a eliminar de la lista visual
     */
    public void eliminarHechoDeLista(String item) {
        listModel.removeElement(item);
    }

    /**
     * Limpia todos los campos del formulario de agregar canción.
     * Llamado por el Controlador después de agregar exitosamente.
     */
    public void limpiarCamposAgregar() {
        idField.setText("");
        tituloField.setText("");
        artistaField.setText("");
        generosField.setText("");
        idiomaAgregarCombo.setSelectedIndex(0);
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
        textArea.setBorder(
            BorderFactory.createEmptyBorder(10, 15, 10, 15));
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
    public JButton getCerrarHechosBtn()   { return cerrarHechosBtn;   }

    /** Retorna cerrarReglasBtn para que el Controlador registre su listener */
    public JButton getCerrarReglasBtn()   { return cerrarReglasBtn;   }

    /** Retorna agregarHechoBtn para que el Controlador registre su listener */
    public JButton getAgregarHechoBtn()   { return agregarHechoBtn;   }

    /** Retorna cerrarAgregarBtn para que el Controlador registre su listener */
    public JButton getCerrarAgregarBtn()  { return cerrarAgregarBtn;  }

    /** Retorna eliminarHechoBtn para que el Controlador registre su listener */
    public JButton getEliminarHechoBtn()  { return eliminarHechoBtn;  }

    /** Retorna cerrarEliminarBtn para que el Controlador registre su listener */
    public JButton getCerrarEliminarBtn() { return cerrarEliminarBtn; }

    /** Retorna consultarBtn para que el Controlador registre su listener */
    public JButton getConsultarBtn()      { return consultarBtn;      }

    /** Retorna cerrarConsultaBtn para que el Controlador registre su listener */
    public JButton getCerrarConsultaBtn() { return cerrarConsultaBtn; }

    // ==================== GETTERS VALORES ====================
    public String getTipoBusqueda() {
        int idx = tabbedPane.getSelectedIndex();
        if (idx == 0) return "actividad";
        if (idx == 1) return "emocion";
        return "perfil";
    }


    /** Retorna el estado de ánimo seleccionado */
    public String getEstado()    { return (String) moodCombo.getSelectedItem();      }

    /** Retorna la actividad seleccionada */
    public String getActividad() { return (String) actividadCombo.getSelectedItem(); }

    /** Retorna el nivel de energía seleccionado */
    public String getEnergia()   { return (String) energiaCombo.getSelectedItem();   }

    /** Retorna el idioma seleccionado */
    public String getIdioma()    { return (String) idiomaCombo.getSelectedItem();    }

    /** Retorna el ID de la nueva canción */
    public String getId()        { return idField.getText().trim().toLowerCase();    }

    /** Retorna el título de la nueva canción */
    public String getTitulo()    { return tituloField.getText().trim();              }

    /** Retorna el artista de la nueva canción */
    public String getArtista()   { return artistaField.getText().trim();             }

    /** Retorna los géneros en formato [g1, g2] */
    public String getGeneros()   { return generosField.getText().trim();             }

    /** Retorna el idioma seleccionado para la nueva canción */
    public String getIdiomaAgregar() {
        return (String) idiomaAgregarCombo.getSelectedItem();
    }

    /** Retorna el elemento seleccionado en la lista de eliminar */
    public String getHechoSeleccionado() {
        return listaHechos.getSelectedValue();
    }

    /** Retorna el ID ingresado en el campo de consulta simple */
    public String getValorConsulta() {
        return campoConsulta.getText().trim().toLowerCase();
    }
    
    // ==================== GETTERS MENU ====================
    public JMenuItem getItemHechos()        { return itemHechos; }
    public JMenuItem getItemReglas()        { return itemReglas; }
    public JMenuItem getItemAgregarHecho()  { return itemAgregarHecho; }
    public JMenuItem getItemEliminarHecho() { return itemEliminarHecho; }
    public JMenuItem getItemConsulta()      { return itemConsulta; }

}