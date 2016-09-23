/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cpqd.test;

import br.com.cpqd.titan.processor.FileTemplate;
import br.com.cpqd.titan.processor.Mass;
import br.com.cpqd.titan.processor.TemplateProcessor;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ggodoy
 */
public class TestGenerateSampleLine {

    static List<FileTemplate> fileRead;
//    static String fileName = "/home/ggodoy/projetos/JMeterJavaSampler/jmeter-java-sampler-mq/jmeterJMX/Gife23Performance.xml";
    static String fileName = "src/test/resources/teste.xml";
    
    public static void main(String[] args) {
        TemplateProcessor template = null;
        try {
            

            template = new TemplateProcessor(fileName);
            fileRead = template.readTemplate(false);

            ConcurrentHashMap<String, String> msg = null;
//            for (int i = 0; i < 1; i++) {

                Mass mass = new Mass(fileRead.get(0), false, 10);
                msg = mass.createSampleLine(fileRead.get(0).getTrailFields(), 1,  null, "nsu");
                
//            }
            
                System.out.println(msg.get("line"));
                System.out.println(msg.get("line").length());
                System.out.println(msg.get("identifier"));

        } catch (Exception ex) {
            Logger.getLogger(TestGenerateSampleLine.class.getName()).log(Level.SEVERE, null, ex);
        }



    }
}
