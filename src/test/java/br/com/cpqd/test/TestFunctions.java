package br.com.cpqd.test;

import br.com.cpqd.titan.api.FunctionsFacade;
import br.com.cpqd.titan.api.impl.FunctionsImpl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author ggodoy
 * 
 */
public class TestFunctions {
	private FunctionsFacade functions;

	@Before
	public void setUp() {
		functions = new FunctionsImpl();
		functions.setVariation(1);
	}

        @After
	public void tearDown() {
	}

	/**
	 * Test method for
	 * {@link br.com.cpqd.generator.batch.functions.functions#fill(String strSource, int size )
	 * )}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFill() {

		assertEquals(functions.fill("solimoes", 12, null, null), "    solimoes");

		assertEquals(functions.fill("amazonas", 10, null, "0123456789"),
				"89amazonas");

		assertEquals(functions.fill("paranagua", 15, ">", "0123456789"),
				"paranagua012345");

		assertEquals(functions.fill("araguaia", 3, "<", null), "aia");

	}

	/**
	 * Test method for
	 * {@link br.com.cpqd.generator.batch.functions.functions#generateCnpjCpf(int, java.lang.String)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGenerateCnpjCpf() throws Exception {

		assertNotNull(functions.generateCnpjCpf(11, "cpf"));
		assertNotNull(functions.generateCnpjCpf(14, "cnpj"));
		assertNotNull(functions.generateCnpjCpf(14, "cpfponto"));
		assertNotNull(functions.generateCnpjCpf(14, "cnpjponto"));

		Pattern getPattern = Pattern.compile("[0-9]{11}");
		Matcher m = getPattern.matcher(functions.generateCnpjCpf(11, "cpf")
				.toString());
		assertTrue(m.find());

		getPattern = Pattern.compile("[A-Za-z]{11}");
		m = getPattern.matcher(functions.generateCnpjCpf(11, "cpf").toString());
		assertFalse(m.find());

	}

	/**
	 * Test method for
	 * {@link br.com.cpqd.generator.batch.functions.functions#Zeros(int)}.
	 */
	@Test
	public void testZeros() {
		assertNotNull(functions.zeros(0));
		assertNotNull(functions.zeros(5));
		assertNotNull(functions.zeros(100));

		assertEquals(functions.zeros(5).get(0), "00000");
		assertEquals(functions.zeros(10).get(0), "0000000000");
	}

	/**
	 * Test method for
	 * {@link br.com.cpqd.generator.batch.functions.functions#Spaces(int)}.
	 */
	@Test
	public void testSpaces() {
		assertNotNull(functions.spaces(0));
		assertNotNull(functions.spaces(5));
		assertNotNull(functions.spaces(100));

		assertEquals(functions.spaces(5).get(0), "     ");
		assertEquals(functions.spaces(10).get(0), "          ");
	}

	/**
	 * Test method for
	 * {@link br.com.cpqd.generator.batch.functions.functions#Calc(int, java.lang.String)}
	 * .
	 */
	@Test
	public void testCalc() {

		assertNotNull(functions.calc(10, "22+15"));
		assertNotNull(functions.calc(10, "105-112"));
		assertNotNull(functions.calc(10, "100-200"));
		assertNotNull(functions.calc(10, "10.5-10.4"));

		assertEquals(functions.calc(5, "(2+2)"), "4.0  ");
		assertEquals(functions.calc(5, "(10.5-10.4)"), "0.099");
	}

	/**
	 * Test method for
	 * {@link br.com.cpqd.generator.batch.functions.functions#Int(int, int, int)}
	 * .
	 */
	@Test
	public void testInt() {
		Pattern getPattern = Pattern.compile("[3-7]{5}");
		Matcher m = getPattern.matcher(functions.newInt(5, 3, 7).toString());
		assertTrue(m.find());

		getPattern = Pattern.compile("[0-9]{8}");
		m = getPattern.matcher(functions.newInt(8, 0, 9).toString());
		assertTrue(m.find());

		getPattern = Pattern.compile("[5-5]{8}");
		m = getPattern.matcher(functions.newInt(8, 5, 5).toString());
		assertTrue(m.find());

		getPattern = Pattern.compile("[4-6]{3}");
		m = getPattern.matcher(functions.newInt(3, 4, 6).toString());
		assertTrue(m.find());

		getPattern = Pattern.compile("[6-9]{9}");
		m = getPattern.matcher(functions.newInt(9, 1, 3).toString());
		assertFalse(m.find());

	}

	/**
	 * Test method for
	 * {@link br.com.cpqd.generator.batch.functions.functions#IntSequence(int, java.lang.String, int)}
	 * .
	 */
	@Test
	public void testIntSequence() {
		functions.setVariation(3);
		assertNotNull(functions.intSequence(5, "5"));
		
		functions.setVariation(8);
		assertNotNull(functions.intSequence(5, "6"));
		
		functions.setVariation(18);
		assertNotNull(functions.intSequence(1, "2"));

		functions.setVariation(3);
		assertEquals(functions.intSequence(5, "5").get(0), "    5");
		assertEquals(functions.intSequence(5, "5").get(1), "    6");
		assertEquals(functions.intSequence(5, "5").get(2), "    7");

		assertEquals(functions.intSequence(5, "12,$,>").get(0), "12$$$");
		assertEquals(functions.intSequence(8, "6,%").get(0), "%%%%%%%6");

	}

	/**
	 * Test method for
	 * {@link br.com.cpqd.generator.batch.functions.functions#MaskNumberSequence(java.lang.String, int, int)}
	 * .
	 */
	@Test
	public void testMaskNumberSequence() {
		try {
			int size;
                        int variation;
                        ArrayList<String> genMask = new ArrayList<String>();
			String ret;
                        Matcher matcher; 
                        Pattern pattern;
                        
                        //Test generating various masks..
                        size = 9;
                        variation = 350;
                        functions.setVariation(variation);
			genMask = functions.maskNumberSequence("######", size);
                        assertNotNull(genMask);
                        assertEquals("test: generating data fails...", variation, genMask.size());
                        
//			functions.setVariation(1);
//			pattern = Pattern.compile("[0-9]{9}");
//			matcher = pattern.matcher(functions.maskNumberSequence("######", 12).get(0));
//			assertTrue(matcher.find());
                        
                        //Test when mask is greater than size
                        size = 12;
//                        pattern = Pattern.compile("(?<!\d)\d{10}(?!\d)");
                        ret = functions.maskNumberSequence("1AX#############", size).get(0);
//                        System.out.println("<" + ret + ">");
//                        Matcher m = pattern.matcher(ret);
//			assertTrue(m.find());
                        assertEquals("test: mask greater than size fails....", size, ret.length());
                        
                        //Test when size is greater than mask.
                        size = 20;
                        ret = functions.maskNumberSequence("1AX#############", size).get(0);
//                        m = getPattern.matcher(ret);
//			assertTrue(m.find());
                        assertEquals("test: size greater than mask fails....", size, ret.length());
                        
                        
                        
                        
		} catch (ParseException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Test method for
	 * {@link br.com.cpqd.generator.batch.functions.functions#MaskNumber(java.lang.String, int)}
	 * .
	 */
	@Test
	public void testMaskNumber() {
		try {
			Pattern getPattern = Pattern
					.compile("\\([0-9]{2}\\)[0-9]{4}-[0-9]{4}");
			Matcher m = getPattern.matcher(functions.maskNumber(
					"(##)####-####,$", 13).get(0));
			assertTrue(m.find());

			getPattern = Pattern
					.compile("\\$\\$\\([0-9]{2}\\)[0-9]{4}-[0-9]{4}");
			m = getPattern.matcher(functions.maskNumber("(##)####-####,$", 15)
					.get(0));
			assertTrue(m.find());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
        
        @Test
	public void testFixed() {
		String ret;
                
                //
                ret = functions.asis("myString", 8);
                assertEquals("fail on generating equals string", "myString", ret);
                
                ret = functions.asis("myString", 4);
                assertEquals("fail on trimming string", "ring", ret);
                
                ret = functions.asis("myString", 10);
                assertEquals("fail on filling string", "  myString", ret);
                
	}

//        @Test
	public void testPeriodBetween() throws Exception {
            ArrayList<String> ret = functions.periodBetween("hoje, agora, ddMMyyyy HH:mm:ss", 17);
             
            for (String string : ret) {
                System.out.println(string);
            }
            
	}
        
        

//	@Test
	public void testFunctionValue() {
		fail("Not yet implemented");
	}

//	@Test
	public void testCallFunction() {
		fail("Not yet implemented");
	}

//	@Test
	public void testQuery() {
		fail("Not yet implemented");
	}

//	@Test
	public void testNewInt() {
		fail("Not yet implemented");
	}

//	@Test
	public void testIntSpaces() {
		fail("Not yet implemented");
	}

//	@Test
	public void testCharAa() {
		fail("Not yet implemented");
	}

//	@Test
	public void testCharAaSpaces() {
		fail("Not yet implemented");
	}

//	@Test
	public void testAsis() {
		fail("Not yet implemented");
	}

//	@Test
	public void testHourValid() {
		fail("Not yet implemented");
	}

//	@Test
	public void testDateValid() {
		fail("Not yet implemented");
	}

//	@Test
	public void testHourInvalid_HHmmss() {
		fail("Not yet implemented");
	}

//	@Test
	public void testHourInvalid_HHmm() {
		fail("Not yet implemented");
	}

//	@Test
	public void testDateInvalid_ddMMyyyy() {
		fail("Not yet implemented");
	}

//	@Test
	public void testDateInvalid_yyyyMMdd() {
		fail("Not yet implemented");
	}

//	@Test
	public void testDateInvalid_yyyyddMM() {
		fail("Not yet implemented");
	}

//	@Test
	public void testDateInvalid_yyMMdd() {
		fail("Not yet implemented");
	}

//	@Test
	public void testDateValidFuture() {
		fail("Not yet implemented");
	}

//	@Test
	public void testDatePeriod() {
		fail("Not yet implemented");
	}

//	@Test
	public void testHourValidPast() {
		fail("Not yet implemented");
	}

//	@Test
	public void testHourValidFuture() {
		fail("Not yet implemented");
	}

//	@Test
	public void testDateValidPast() {
		fail("Not yet implemented");
	}

//	@Test
	public void testNow() {
		fail("Not yet implemented");
	}

//	@Test
	public void testFile() {
		fail("Not yet implemented");
	}

//	@Test
	public void testOpenSeedFile() {
		fail("Not yet implemented");
	}

//	@Test
	public void testGetVariation() {
		fail("Not yet implemented");
	}

//	@Test
	public void testSetVariation() {
		fail("Not yet implemented");
	}

}
