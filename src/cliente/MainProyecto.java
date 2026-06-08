package cliente;

import cliente.MVC.Controlador;
import cliente.MVC.Modelo;
import cliente.MVC.Vista;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import org.jpl7.Query;

/**
 *
 * @author diva_
 */
public class MainProyecto {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // Instanciar los elementos del MVC
        Modelo modelo = new Modelo();
        Vista vista = new Vista(modelo);
        Controlador controlador = new Controlador(modelo, vista);

        // Hacer visible la vista
        vista.setVisible(true);
    }
}
