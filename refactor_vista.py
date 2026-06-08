import os
import re

vista_path = 'cliente/MVC/Vista.java'
with open(vista_path, 'r', encoding='utf-8') as f:
    vista_content = f.read()

# Add JTabbedPane to fields
vista_content = vista_content.replace(
    'private JPanel preferencesPanel;',
    'private JPanel preferencesPanel;\n    private JTabbedPane tabbedPane;'
)

# Replace crearPanelPreferencias completely
old_crear = re.search(r'    private JPanel crearPanelPreferencias\(\) \{[\s\S]*?return preferencesPanel;\n    \}', vista_content).group(0)

new_crear = '''    private JPanel crearPanelPreferencias() {
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
    }'''

vista_content = vista_content.replace(old_crear, new_crear)

# Replace registrarListenerExclusiones
old_exclusiones = re.search(r'    private void registrarListenerExclusiones\(\) \{[\s\S]*?\}\n\n', vista_content)
if old_exclusiones:
    vista_content = vista_content.replace(old_exclusiones.group(0), '    private void registrarListenerExclusiones() { }\n\n')

# In Controlador.java, update generarPlaylist() to check the active tab.
# Let's add getTipoBusqueda() to Vista.java
get_tipo = '''    public String getTipoBusqueda() {
        int idx = tabbedPane.getSelectedIndex();
        if (idx == 0) return "actividad";
        if (idx == 1) return "emocion";
        return "perfil";
    }
'''
vista_content = vista_content.replace('    // ==================== GETTERS VALORES ====================', '    // ==================== GETTERS VALORES ====================\n' + get_tipo)

with open(vista_path, 'w', encoding='utf-8') as f:
    f.write(vista_content)

print("Modificado Vista.java")
