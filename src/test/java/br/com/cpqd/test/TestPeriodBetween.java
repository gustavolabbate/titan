/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cpqd.test;

import br.com.cpqd.titan.api.impl.FunctionsImpl;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ggodoy
 */
public class TestPeriodBetween {
    private static FunctionsImpl functions = new FunctionsImpl();

    public static void main(String[] args)
    {
        String values;
        int size = 9;
        
//        values = "hoje, hoje+1, dd/MM/yyyy HH:mm:ss";
        values = "hoje, hoje+2, dd/MM/yyyy HH:mm";
        // yyyy-MM-dd HH:mm:ss
        try {
            ArrayList<String> periodBetween = functions.periodBetween(values, size);
            
            for (String dataGerada : periodBetween) {
                System.out.println(dataGerada);
            }
            
        } catch (Exception ex) {
            Logger.getLogger(TestPeriodBetween.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
