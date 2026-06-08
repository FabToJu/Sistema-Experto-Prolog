/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import MVC.Controlador;
import MVC.Modelo;
import MVC.Vista;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

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
