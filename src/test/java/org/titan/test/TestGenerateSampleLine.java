/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.titan.test;

import org.titan.api.impl.FunctionsImpl;
import org.titan.processor.FileTemplate;
import org.titan.processor.Mass;
import org.titan.processor.TemplateProcessor;
import java.text.ParseException;
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
    static String fileName = "src/test/resources/hardware_info.xml";

    public static void main(String[] args) {
        method2();
    }

    private static void method1() {
        TemplateProcessor template = null;
        try {

            template = new TemplateProcessor(fileName);
            fileRead = template.readTemplate(false);

            ConcurrentHashMap<String, String> msg = null;
//            for (int i = 0; i < 1; i++) {

            Mass mass = new Mass(fileRead.get(0), false, 10);
            msg = mass.createSampleLine(fileRead.get(0).getTrailFields(), 1, null, "serial_number");

//            }
            System.out.println(msg.get("line"));
            System.out.println(msg.get("line").length());
            System.out.println(msg.get("identifier"));
        } catch (Exception ex) {
            Logger.getLogger(TestGenerateSampleLine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void method2() {
        FunctionsImpl fc = new FunctionsImpl();
        System.out.println("*****>>>> " + fc.asis("[0-9A-Z]", 7));
        try {
            System.out.println("*****>>>> " + fc.maskNumber("[0-9A-Z]", 7));
        } catch (ParseException ex) {
            Logger.getLogger(TestGenerateSampleLine.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
