import os
import re

# Update Modelo.java
mod_path = 'cliente/MVC/Modelo.java'
with open(mod_path, 'r', encoding='utf-8') as f:
    mod_content = f.read()

old_mod = '''    public boolean consultarCancionActiva(String id) {
        try {
            Query q = new Query("api_consultar(" + id + ")");
            return q.hasSolution();
        } catch (Exception ex) {
            System.err.println("Error en consultarCancionActiva: " + ex.getMessage());
            return false;
        }
    }'''

new_mod = '''    public String[] consultarCancionActiva(String id) {
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
    }'''

mod_content = mod_content.replace(old_mod, new_mod)

with open(mod_path, 'w', encoding='utf-8') as f:
    f.write(mod_content)

# Update Controlador.java
ctrl_path = 'cliente/MVC/Controlador.java'
with open(ctrl_path, 'r', encoding='utf-8') as f:
    ctrl_content = f.read()

old_ctrl = '''    public void registrarListenerConsultar() {
        vista.getConsultarBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = vista.getValorConsulta();
                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                        "Ingresa un ID para consultar.\\nEjemplo: c01",
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
    }'''

new_ctrl = '''    public void registrarListenerConsultar() {
        vista.getConsultarBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = vista.getValorConsulta();
                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                        "Ingresa un ID para consultar.\\nEjemplo: c01",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String[] datos = modelo.consultarCancionActiva(id);
                if (datos != null) {
                    vista.mostrarResultadoConsulta(
                        "<html>✓ La canción <b>" + id + "</b> está activa.<br><br>" +
                        "<b>Título:</b> " + datos[0] + "<br>" +
                        "<b>Artista:</b> " + datos[1] + "<br>" +
                        "<b>Géneros:</b> " + datos[2] + "<br>" +
                        "<b>Idioma:</b> " + datos[3] + "</html>"
                    );
                } else {
                    vista.mostrarResultadoConsulta(
                        "<html>✗ La canción <b>" + id + "</b><br>" +
                        "no existe o fue eliminada lógicamente.</html>"
                    );
                }
            }
        });
    }'''

ctrl_content = ctrl_content.replace(old_ctrl, new_ctrl)

with open(ctrl_path, 'w', encoding='utf-8') as f:
    f.write(ctrl_content)

# Update Vista.java
vista_path = 'cliente/MVC/Vista.java'
with open(vista_path, 'r', encoding='utf-8') as f:
    vista_content = f.read()

old_vista = 'JDialog dialog = new JDialog(this, "Consulta Simple", true);\n        dialog.setSize(460, 280);'
new_vista = 'JDialog dialog = new JDialog(this, "Consulta Simple", true);\n        dialog.setSize(460, 360);'

vista_content = vista_content.replace(old_vista, new_vista)

with open(vista_path, 'w', encoding='utf-8') as f:
    f.write(vista_content)

print("Modificados Modelo.java, Controlador.java y Vista.java")
