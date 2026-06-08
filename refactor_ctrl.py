import os
import re

ctrl_path = 'cliente/MVC/Controlador.java'
with open(ctrl_path, 'r', encoding='utf-8') as f:
    ctrl_content = f.read()

# Replace generarPlaylist()
old_generar = '''    private void generarPlaylist() {
        String emocion   = vista.getEstado();
        String actividad = vista.getActividad();
        String energia   = vista.getEnergia();
        String idioma    = vista.getIdioma();

        List<String[]> playlist = modelo.obtenerPlaylist(
            emocion, actividad, energia, idioma);

        if (playlist == null || playlist.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "No se encontraron canciones con esos filtros.\\n" +
                "Intenta con una combinación diferente.",
                "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String filtros = emocion + " | " + actividad +
                         " | " + energia + " | " + idioma;
        vista.mostrarResultados(filtros, playlist);
    }'''

new_generar = '''    private void generarPlaylist() {
        String tipo = vista.getTipoBusqueda();
        List<String[]> playlist = null;
        String filtros = "";

        if (tipo.equals("actividad")) {
            String actividad = vista.getActividad();
            if (actividad.equals("Actividad")) {
                JOptionPane.showMessageDialog(null, "Seleccione una actividad válida.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            playlist = modelo.obtenerPlaylist(tipo, actividad, "");
            filtros = "Actividad: " + actividad;
        } else if (tipo.equals("emocion")) {
            String emocion = vista.getEstado();
            if (emocion.equals("Animo")) {
                JOptionPane.showMessageDialog(null, "Seleccione un estado de ánimo válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            playlist = modelo.obtenerPlaylist(tipo, emocion, "");
            filtros = "Emoción: " + emocion;
        } else if (tipo.equals("perfil")) {
            String energia = vista.getEnergia();
            String idioma = vista.getIdioma();
            if (energia.equals("Energia") || idioma.equals("Idioma")) {
                JOptionPane.showMessageDialog(null, "Seleccione energía e idioma válidos.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            playlist = modelo.obtenerPlaylist(tipo, energia, idioma);
            filtros = "Energía: " + energia + " | Idioma: " + idioma;
        }

        if (playlist == null || playlist.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "No se encontraron canciones con esos filtros.\\n" +
                "Intenta con una combinación diferente.",
                "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        vista.mostrarResultados(filtros, playlist);
    }'''

ctrl_content = ctrl_content.replace(old_generar, new_generar)

with open(ctrl_path, 'w', encoding='utf-8') as f:
    f.write(ctrl_content)

vista_path = 'cliente/MVC/Vista.java'
with open(vista_path, 'r', encoding='utf-8') as f:
    vista_content = f.read()

# Fix mostrarPanelPreferencias
old_mostrar = '''    public void mostrarPanelPreferencias() {
    actualizandoCombos = true;
    moodCombo.setSelectedIndex(0); 
    idiomaCombo.setSelectedIndex(0);
    actualizandoCombos = false;
    
    // Re-aplica las reglas para la selección por defecto
    aplicarExclusionesPorEmocion((String) moodCombo.getSelectedItem()); 
    cardLayout.show(mainPanel, "preferencias");
}'''

new_mostrar = '''    public void mostrarPanelPreferencias() {
        moodCombo.setSelectedIndex(0); 
        idiomaCombo.setSelectedIndex(0);
        actividadCombo.setSelectedIndex(0);
        energiaCombo.setSelectedIndex(0);
        cardLayout.show(mainPanel, "preferencias");
    }'''

vista_content = vista_content.replace(old_mostrar, new_mostrar)

with open(vista_path, 'w', encoding='utf-8') as f:
    f.write(vista_content)

print("Modificado Controlador.java y Vista.java")
