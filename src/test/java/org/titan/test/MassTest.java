/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.titan.test;

import org.titan.functions.Rows;
import org.titan.processor.FileTemplate;
import org.titan.processor.Mass;
import org.titan.processor.TemplateProcessor;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ggodoy
 */
public class MassTest {

    TemplateProcessor template;
    Mass mass;

    @Before
    public void setUp() {

        String fileName = "src/test/resources/teste.xml";

        template = new TemplateProcessor(fileName, false);

        for (FileTemplate fileTemplate : template.getFileTemplateList()) {
            mass = new Mass(fileTemplate, false, 3);
        }
    }

    @Test
    public void testCalculation() {
//        mass.getTrailerCols().getColumns().get(0).getRows().get(0);

        try {
            for (Rows rows : mass.getTrailerCols().getColumns()) {

                for (String linha : rows.getRows()) {
                    if (linha == null) 
                    {
                        fail("linha null");
                    }
                }
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }


    }
}
