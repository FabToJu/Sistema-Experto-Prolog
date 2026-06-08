/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;
import org.jpl7.Query;
/**
 *
 * @author diva_
 */
public class Prueba {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       Query q = new Query("true");
       System.out.println(q.hasSolution());
    }
    
}
