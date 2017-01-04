package org.titan.api.impl;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.swing.text.MaskFormatter;

import org.nfunk.jep.JEP;

import org.titan.api.FunctionsFacade;
import org.titan.utils.DBConnection;
import org.titan.utils.DataBase;
import org.titan.utils.Utils;

/**
 *
 * @author ggodoy
 *
 */
public class FunctionsImpl implements FunctionsFacade {

	/**
	 * @param args
	 * @throws Exception
	 */

//	private static final Logger LOGGER = Logger.getLogger(FunctionsImpl.class.getName());
	private List<String> functionMisc;
    private  List<String> valueMisc;
    private int variation;
    private Boolean misc = false;
    private String titanHome = System.getenv("TITAN_HOME");

    public int getVariation() {
        return variation;
    }

    public void setVariation(int variation) {
        this.variation = variation;
    }
    

    /**
     * 
     *
     * @param strSource
     * @param size
     * @param direction
     * @param charFill
     * @return
     * @see
     */
    public synchronized String fill(String strSource, int size, String direction,
            String charFill) {
        StringBuffer fill = new StringBuffer();
        String strReturn;
        int dif = size - strSource.length();

        for (int x = 0; x < dif; x++) {
            if (charFill != null) {
                fill.append(charFill);

            } else {
                fill.append(" ");
            }
        }

        if (direction != null && !direction.equals("<")) {
            strReturn = (strSource + fill).substring(0, size);

        } else {
            strReturn = (fill + strSource).substring(
                    (fill + strSource).length() - size,
                    (fill + strSource).length());

        }
        return strReturn;
    }

    /**
     *
     * @param size
     * @param type
     * @return
     * @throws Exception
     */
    public  synchronized  ArrayList<String> generateCnpjCpf(int size, String type)
            throws Exception {
        String field;
        ArrayList<String> z = new ArrayList<String>();

        for (int r = 0; r < variation; r++) {

            if (type.toLowerCase().equalsIgnoreCase("cpf")) {
                field = Utils.generateCpf();
            } else if (type.toLowerCase().equalsIgnoreCase("cnpj")) {
                field = Utils.generateCnpj();
            } else if (type.toLowerCase().equalsIgnoreCase("cpfponto")) {
                field = Utils.maskCpf();
            } else if (type.toLowerCase().equalsIgnoreCase("cnpjponto")) {
                field = Utils.maskCnpj();
            } else {
                field = " ";
            }

            z.add(fill(field, size, null, null));
        }
        return z;
    }

    /**
     *
     * @param size
     * @param value
     * @param dbConn
     * @return
     * @throws Exception
     */
    public synchronized ArrayList<String> query(int size, String value,
            DBConnection dbConn) throws Exception {

        String field;
        ArrayList<String> z = new ArrayList<String>();
        ArrayList<String> dbresults;

        DataBase db = new DataBase(dbConn);

        dbresults = db.runSelect(value);

        // for(int r=0; r<variation; r++)
        for (int r = 0; r < dbresults.size(); r++) {

            // field = dbresults.get((int)(Math.random()*(dbresults.size())));
            field = dbresults.get(r);

            if (field.length() < size) {
                int dif = size - field.length();
                StringBuffer spaces = new StringBuffer();

                for (int x = 0; x < dif; x++) {
                    spaces.append(" ");
                }
                field = spaces + field;
            }

            field = field.substring(0, size);
            z.add(field);
        }
        // Collections.shuffle(z);
        return z;
    }

    /**
     *
     * @param size
     * @return
     */
    public synchronized ArrayList<String> zeros(int size) {
        ArrayList<String> z = new ArrayList<String>();
        StringBuffer field = new StringBuffer();

        for (int x = 0; x < size; x++) {
            field.append("0");
        }

        for (int i = 0; i < variation; i++) {
            z.add(fill(field.toString(), size, null, null));
        }

        return z;

    }

    /**
     *
     * @param size
     * @return
     */
    public synchronized ArrayList<String> spaces(int size) {
        ArrayList<String> z = new ArrayList<String>();
        StringBuffer field = new StringBuffer();

        for (int x = 0; x < size; x++) {
            field.append(" ");

        }

        for (int i = 0; i < variation; i++) {
            z.add(fill(field.toString(), size, null, null));
        }
        return z;

    }

    /**
     *
     * @param size
     * @param expression
     * @return
     */
    public synchronized String calc(int size, String expression) {

        String field = "";
        JEP myParser = new JEP();

        myParser.addStandardFunctions();

        myParser.parseExpression(expression.replace("calc=", ""));
        Double result = myParser.getValue();

        field = String.valueOf(result);

        return fill(field, size, ">", null);
    }

    /**
     *
     * @param size
     * @param init
     * @param finish
     * @return
     */
    public synchronized ArrayList<String> newInt(int size, int init, int finish) {
        ArrayList<String> z = new ArrayList<String>();

        StringBuffer field;

        for (int i = 0; i < variation; i++) {
            int rand = (int) (Math.random() * (finish - init) + init);
            field = new StringBuffer();
            field.append(String.valueOf(rand));
            for (int x = 0; x < size - 1; x++) {

                rand = (int) (Math.random() * (finish - init) + init);
                field.append(String.valueOf(rand));
            }

            z.add(field.substring(0, size));

        }
        return z;
    }

    public synchronized ArrayList<String> intSequence(int size, String value) {
        ArrayList<String> z = new ArrayList<String>();
        String[] val = value.split(",");

        Long init = Long.parseLong(val[0]);
        String cha;
        String direct;

        if (val.length > 1) {
            cha = val[1];
        } else {
            cha = " ";
        }

        if (val.length > 2) {
            direct = val[2];
        } else {
            direct = "<";
        }

        for (int i = 0; i < variation; i++) {

            z.add(fill(String.valueOf(init), size, direct, cha));
            init++;
        }

        return z;
    }

    /**
     *
     * @param pattern
     * @param size
     * @return
     * @throws ParseException
     */
    public synchronized ArrayList<String> maskNumberSequence(String pattern,
            int size) throws ParseException {
        ArrayList<String> z = new ArrayList<String>();
        StringBuffer aux = new StringBuffer();
        String field = "";
        int rand = 0;
        MaskFormatter mf = new MaskFormatter(pattern);
        mf.setValueContainsLiteralCharacters(false);
        for (int x = 0; x < variation; x++) {
            aux.append("");
            for (int e = 0; e < size; e++) {
                rand = 0 + (int) (Math.random() * (9 + 1));
                aux.append(String.valueOf(rand));
            }
            field = mf.valueToString(aux.toString());
            // int fieldSize = size - (field.length()+1);
            // for (int i=0; i < fieldSize; i++)
            int fieldSize = field.length()
                    + Integer.valueOf(String.valueOf(x).length());
            aux = new StringBuffer();
            for (int i = 0; i < fieldSize; i++) {
                aux.append("0");
            }

            aux.append(x);

            z.add((field + aux.toString()).substring(0, size));
        }
        return z;
    }

    /**
     *
     * @param value
     * @param size
     * @return
     * @throws ParseException
     */
    public synchronized ArrayList<String> maskNumber(String value, int size)
            throws ParseException {
        ArrayList<String> z = new ArrayList<String>();
        String fill = " ";
        String[] val = value.split(",");
        String pattern = val[0];
        if (val.length != 1) {
            fill = val[1];
        }

        String field;
        int rand = 0;
        int dif = 0;
        MaskFormatter mf = new MaskFormatter(pattern);
        mf.setValueContainsLiteralCharacters(false);
        StringBuffer aux = new StringBuffer();

        for (int x = 0; x < variation; x++) {
            // aux.append(" ");
            while (aux.length() < size) {
                rand = (int) (Math.random() * (999999999));
                aux.append(String.valueOf(rand));
            }

            field = mf.valueToString(aux.toString());

            if (field.length() < size) {
                dif = size - field.length();
                StringBuffer fillChar = new StringBuffer();

                for (int y = 0; y < dif; y++) {
                    fillChar.append(fill.substring(0, 1));
                }
                field = fillChar + field;
                fillChar = null;
            }

            // field = aux.toString().substring(0, size);

            z.add(field);

            field = null;
            aux = new StringBuffer();
        }

        return z;
    }

    /**
     *
     * @param size
     * @param init
     * @param finish
     * @return
     */
    public synchronized String intSpaces(int size, int init, int finish) {
        int spacesqtd = (int) (Math.random() * size);
        String[] nb = new String[size + spacesqtd];
        StringBuffer field = new StringBuffer();
        int rand;

        for (int i = 0; i < size; i++) {
            rand = init + (int) (Math.random() * (finish + 1));
            nb[i] = String.valueOf(rand);
        }

        for (int i = 0; i < spacesqtd; i++) {
            nb[size + i] = " ";
        }

        Collections.shuffle(Arrays.asList(nb));

        for (int i = 0; i < size; i++) {
            field.append(nb[i]);
        }

        return field.substring(0, size);
    }

    /**
     *
     * @param size
     * @return
     */
    public synchronized String charAa(int size) {
        String[] a = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
            "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w",
            "x", "y", "z"};
        String[] aCap = a.clone();
        StringBuffer field = new StringBuffer();

        for (int i = 0; i < aCap.length; i++) {
            aCap[i] = aCap[i].toUpperCase();
        }
        Collections.shuffle(Arrays.asList(a));
        Collections.shuffle(Arrays.asList(aCap));

        for (int x = 0; x < size; x++) {
            field.append(a[(int) (Math.random() * (a.length))]);
            field.append(aCap[(int) (Math.random() * (a.length))]);
        }
        return field.toString().substring(0, size);

    }

    /**
     *
     * @param size
     * @return
     */
    public synchronized String charAaSpaces(int size) {
        String[] a = {" ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ",
            " ", " ", " ", " ", " ", " ", " ", " ", " ", "a", "b", "c",
            "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
            "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        String[] aCap = a.clone();
        StringBuffer field = new StringBuffer();

        for (int i = 0; i < aCap.length; i++) {
            aCap[i] = aCap[i].toUpperCase();
        }
        Collections.shuffle(Arrays.asList(a));
        Collections.shuffle(Arrays.asList(aCap));
        for (int x = 0; x < size; x++) {
            field.append(a[(int) (Math.random() * (a.length))]);
            field.append(aCap[(int) (Math.random() * (a.length))]);
        }
        return field.toString().substring(0, size);

    }

    public synchronized ArrayList<String> fixed(String[] values, int size) {
        ArrayList<String> z = new ArrayList<String>();

        Collections.shuffle(Arrays.asList(values));

        String fields;

        if (values.length == 1) {
            fields = values[0];

            z.add(fill(fields, size, null, null));
        } else {
            for (int r = 0; r < variation; r++) {
                fields = values[(int) (Math.random() * (values.length))];

                z.add(fill(fields, size, null, null));
            }
        }

        return z;
    }

    /**
     * Bla Bla Bla
     * @param values
     * @param size
     * @return 
     */
    public synchronized String asis(String values, int size) {
        return fill(values, size, null, null);

    }

    public synchronized ArrayList<String> hourValid(String format) throws Exception {
        String fields;
        ArrayList<String> z = new ArrayList<String>();
        File seedFile = new File(titanHome + "/seedFiles/horasValidas.seed");
        ArrayList<String> validDate = OpenSeedFile(seedFile);

        SimpleDateFormat origin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatDate = new SimpleDateFormat(format);

        for (int r = 0; r < variation; r++) {
            fields = formatDate.format(origin.parse(validDate.get((int) (Math
                    .random() * (validDate.size())))));
            z.add(fields);
        }
        return z;
    }

    public synchronized ArrayList<String> dateValid(String format) throws Exception {

        String fields;
        ArrayList<String> z = new ArrayList<String>();
        File seedFile = new File(titanHome + "/seedFiles/datasValidas.seed");
        ArrayList<String> validDate = OpenSeedFile(seedFile);

        SimpleDateFormat origin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatDate = new SimpleDateFormat(format);

        for (int r = 0; r < variation; r++) {
            fields = formatDate.format(origin.parse(validDate.get((int) (Math
                    .random() * (validDate.size())))));
            z.add(fields);
        }
        return z;
    }

    public synchronized ArrayList<String> hourInvalid_HHmmss() throws Exception {
        String fields;
        ArrayList<String> z = new ArrayList<String>();
        File seedFile = new File(titanHome + "/seedFiles/horasInvalidas.seed");
        ArrayList<String> validDate = OpenSeedFile(seedFile);

        for (int r = 0; r < variation; r++) {
            String[] HourMinuteSeconds = validDate.get(
                    (int) (Math.random() * (validDate.size()))).split(":");
            fields = HourMinuteSeconds[0] + HourMinuteSeconds[1]
                    + HourMinuteSeconds[2];
            z.add(fields);
        }
        return z;
    }

    public synchronized ArrayList<String> hourInvalid_HHmm() throws Exception {
        String fields;
        ArrayList<String> z = new ArrayList<String>();
        File seedFile = new File(titanHome + "/seedFiles/horasInvalidas.seed");
        ArrayList<String> invalidDate = OpenSeedFile(seedFile);

        for (int r = 0; r < variation; r++) {
            String[] HourMinuteSeconds = invalidDate.get(
                    (int) (Math.random() * (invalidDate.size()))).split(":");
            fields = HourMinuteSeconds[0] + HourMinuteSeconds[1];
            z.add(fields);
        }
        return z;
    }

    public synchronized ArrayList<String> dateInvalid_ddMMyyyy() throws Exception {
        String fields;
        ArrayList<String> z = new ArrayList<String>();
        File seedFile = new File(titanHome + "/seedFiles/datasInvalidas.seed");
        ArrayList<String> invalidDate = OpenSeedFile(seedFile);

        for (int r = 0; r < variation; r++) {
            String[] YearMonthDay = invalidDate.get(
                    (int) (Math.random() * (invalidDate.size()))).split("-");
            fields = YearMonthDay[2] + YearMonthDay[1] + YearMonthDay[0];
            z.add(fields);
        }
        return z;
    }

    public synchronized ArrayList<String> dateInvalid_yyyyMMdd() throws Exception {
        String fields;
        ArrayList<String> z = new ArrayList<String>();
        File seedFile = new File(titanHome + "/seedFiles/datasInvalidas.seed");
        ArrayList<String> invalidDate = OpenSeedFile(seedFile);

        for (int r = 0; r < variation; r++) {
            String[] YearMonthDay = invalidDate.get(
                    (int) (Math.random() * (invalidDate.size()))).split("-");
            fields = YearMonthDay[0] + YearMonthDay[1] + YearMonthDay[2];
            z.add(fields);
        }
        return z;
    }

    public synchronized ArrayList<String> dateInvalid_yyyyddMM() throws Exception {
        String fields;
        ArrayList<String> z = new ArrayList<String>();
        File seedFile = new File(titanHome + "/seedFiles/datasInvalidas.seed");
        ArrayList<String> invalidDate = OpenSeedFile(seedFile);

        for (int r = 0; r < variation; r++) {
            String[] YearMonthDay = invalidDate.get(
                    (int) (Math.random() * (invalidDate.size()))).split("-");
            fields = YearMonthDay[0] + YearMonthDay[2] + YearMonthDay[1];
            z.add(fields);
        }
        return z;
    }

    public synchronized ArrayList<String> dateInvalid_yyMMdd() throws Exception {
        String fields;
        ArrayList<String> z = new ArrayList<String>();
        File seedFile = new File(titanHome + "/seedFiles/datasInvalidas.seed");
        ArrayList<String> invalidDate = OpenSeedFile(seedFile);

        for (int r = 0; r < variation; r++) {
            String[] YearMonthDay = invalidDate.get(
                    (int) (Math.random() * (invalidDate.size()))).split("-");
            fields = YearMonthDay[0].substring(2, 4) + YearMonthDay[2]
                    + YearMonthDay[1];
            z.add(fields);
        }
        return z;
    }

    public synchronized String dateValidFuture(String format) throws Exception {
        String fields;

        Calendar cal = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        SimpleDateFormat formatDate = new SimpleDateFormat(format);

        int day = (int) (Math.random() * 9);
        int month = (int) (1 + Math.random() * 12);
        int year = (int) (Math.random() * 5);

        cal.add(Calendar.DAY_OF_MONTH, day);
        cal.add(Calendar.MONTH, month);
        cal.add(Calendar.YEAR, year);

        while (cal.before(today)) {
            cal.add(Calendar.DAY_OF_MONTH, day);
            cal.add(Calendar.MONTH, month);
            cal.add(Calendar.YEAR, year);
        }

        fields = formatDate.format(cal.getTime());

        return fields;
    }

    /**
     * Providing a initial and a end date, generate all internal dates, based on
     * format.
     *
     * @param size
     * @param type
     * @return
     * @throws Exception
     */
    public synchronized ArrayList<String> periodBetween(String value, int size) throws Exception {
        String fields;
        ArrayList<String> z = new ArrayList<String>();
        String[] ar = value.split(",");

        String dateInit = ar[0].trim();
        String dateFinish = ar[1].trim();
        String format = ar[2].trim();

        SimpleDateFormat formatDate = new SimpleDateFormat(format);

        Calendar init = Calendar.getInstance();
        Calendar finish = Calendar.getInstance();
        Calendar newDate;


        
        
        // Set initial date
        // verify if it is 'hoje'
        if (dateInit.toLowerCase().contains("hoje")) {
            init = Utils.setCalendarAsFirstHour();
        }

        if (dateInit.toLowerCase().contains("-")) {
                // if has '-' in expression, subtract the days after signal
                String[] b = dateInit.split("-");
                // set getted day on initialDate
                init.add(Calendar.DAY_OF_MONTH, -Integer.parseInt(b[1]));
            } else if (dateInit.toLowerCase().contains("+")) {
                // if has '+' in expression, add the days after signal	
                String[] b = dateInit.split("\\+");
                // set getted day on initialDate
                init.add(Calendar.DAY_OF_MONTH, +Integer.parseInt(b[1]));
            } else {
                //if dont have a minus or plus, get the current date
            }
        

        // Set finalDate
        // verify if it is 'hoje'
        if (dateFinish.toLowerCase().contains("hoje")) {
            finish = Utils.setCalendarAsLastHour();
        }

        if (dateFinish.contains("-")) {
                // if has '-' in expression, subtract the days after signal
                String[] b = dateFinish.split("-");
                // set getted day on finalDate
                finish.add(Calendar.DAY_OF_MONTH, -Integer.parseInt(b[1]));
            } else if (dateFinish.contains("+")) {
                // if has '+' in expression, add the days after signal	
                String[] b = dateFinish.split("\\+");
                // set getted day on finalDate
                finish.add(Calendar.DAY_OF_MONTH, +Integer.parseInt(b[1]));
            }
        

        // date = finish.getTime();
        Calendar generatedDate;
        generatedDate = init;
        z.add(formatDate.format(init.getTime()).substring(0, size));
        if (finish.after(init)) {
            
            while (!(formatDate.format(generatedDate.getTime()).equals(formatDate.format(finish.getTime())))) {
                if (format.contains("SSS")) {
                    generatedDate.add(Calendar.MILLISECOND, 1);
                } else if (format.contains("ss")) {
                    generatedDate.add(Calendar.SECOND, 1);
                } else if (format.contains("mm")) {
                    generatedDate.add(Calendar.MINUTE, 1);
                } else if (format.contains("HH")) {
                    generatedDate.add(Calendar.HOUR, 1);
                } else if (format.contains("dd")) {
                    generatedDate.add(Calendar.DATE, 1);
                } else if (format.contains("MM")) {
                    generatedDate.add(Calendar.MONTH, 1);
                } else {
                    generatedDate.add(Calendar.YEAR, 1);
                }

                z.add(formatDate.format(generatedDate.getTime()).substring(0, size));
            }
        }

        return z;
    }

    public synchronized String datePeriod(String value, int size) throws Exception {
        String fields;
        String[] ar = value.split(",");

        String format = ar[0].trim();
        String dateInit = ar[1].trim();
        String dateFinish = ar[2].trim();

        SimpleDateFormat formatDate = new SimpleDateFormat(format);

        Calendar init = Calendar.getInstance();
        Calendar finish = Calendar.getInstance();
        Calendar newDate;
        Date date;

        // Obtendo a data inicial
        // verifica se � 'hoje'
        if (dateInit.toLowerCase().contains("hoje")) {
            if (dateInit.toLowerCase().contains("-")) {
                // Se tiver '-' na expressao, tira os dias com o valor ap�s o
                // sinal
                String[] b = dateInit.split("-");
                // atribui o dia obtido no valor da data inicial
                init.add(Calendar.DAY_OF_MONTH, -Integer.parseInt(b[1]));
                date = init.getTime();
            } else if (dateInit.toLowerCase().contains("+")) {
                // Se tiver '+' na expressao, acrescenta os dias com o valor
                // ap�s o sinal
                String[] b = dateInit.split("\\+");
                // atribui o dia obtido no valor da data inicial
                init.add(Calendar.DAY_OF_MONTH, +Integer.parseInt(b[1]));
                date = init.getTime();
            } else {
                // se nao tiver sinal '-' ou '+' atribui a data corrente para o
                // campo (hoje)
                date = init.getTime();
            }
        } else {
            date = formatDate.parse(dateInit);
            init.setTime(date);
        }

        // Obtendo a data final
        // verifica se � 'hoje'
        if (dateFinish.toLowerCase().contains("hoje")) {
            if (dateFinish.contains("-")) {
                // Se tiver '-' na expressao, tira os dias com o valor ap�s o
                // sinal
                String[] b = dateFinish.split("-");
                // atribui o dia obtido no valor da data inicial
                finish.add(Calendar.DAY_OF_MONTH, -Integer.parseInt(b[1]));
            } else if (dateFinish.contains("+")) {
                // Se tiver '+' na expressao, acrescenta os dias com o valor
                // ap�s o sinal
                String[] b = dateFinish.split("\\+");
                // atribui o dia obtido no valor da data inicial
                finish.add(Calendar.DAY_OF_MONTH, +Integer.parseInt(b[1]));
            }
        } else {
            date = formatDate.parse(dateFinish);
            finish.setTime(date);
        }

        // date = finish.getTime();
        if (finish.after(init)) {
            long difDays = (finish.getTimeInMillis() - init.getTimeInMillis())
                    / (24 * 60 * 60 * 1000);
            int days = (int) (Math.random() * (difDays));

            newDate = init;
            newDate.add(Calendar.DAY_OF_MONTH, days);

            fields = formatDate.format(newDate.getTime());

            return fill(fields, size, null, null);
        } else {
            return "D2<=D1";
        }
    }

    public synchronized String hourValidPast(String format, int hourBack)
            throws Exception {
        String fields;
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, -hourBack);

        SimpleDateFormat formatDate = new SimpleDateFormat(format);
        fields = formatDate.format(cal.getTime());

        return fields;
    }

    public synchronized String hourValidFuture(String format, int hourForward)
            throws Exception {
        String fields;
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, hourForward);

        SimpleDateFormat formatDate = new SimpleDateFormat(format);
        fields = formatDate.format(cal.getTime());

        return fields;
    }

    public synchronized String dateValidPast(String format) throws Exception {
        String fields;

        Calendar cal = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        SimpleDateFormat formatDate = new SimpleDateFormat(format);

        int day = (int) (Math.random() * 9);
        int month = (int) (1 + Math.random() * 12);
        int year = (int) (Math.random() * 5);

        cal.add(Calendar.DAY_OF_MONTH, -day);
        cal.add(Calendar.MONTH, -month);
        cal.add(Calendar.YEAR, -year);

        while (cal.after(today)) {
            cal.add(Calendar.DAY_OF_MONTH, day);
            cal.add(Calendar.MONTH, month);
            cal.add(Calendar.YEAR, year);
        }

        fields = formatDate.format(cal.getTime());

        return fields;
    }

    public synchronized String now(String format) throws Exception {
        String fields;
        Calendar cal = Calendar.getInstance();

        // SimpleDateFormat origin = new
        // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatDate = new SimpleDateFormat(format);

        fields = formatDate.format(cal.getTime());
        return fields;
    }

    /**
     *
     * @param seedFile
     * @param size
     * @return
     * @throws Exception
     */
    public synchronized ArrayList<String> file(String seedFile, int size)
            throws Exception {
        // locating seedFiles:
        // 1 - try on currentDir, on seedFiles dir
        // 2 - try local, on root dir
        // 3 - try local, on mySeedFiles
        File seedLocateDefault = new File(titanHome + "/seedFiles/"
                + seedFile.trim() + ".seed");
        File seedLocateOnRoot = new File(seedFile.trim() + ".seed");
        File seedLocateMySeed = new File("./mySeedFiles/" + seedFile.trim()
                + ".seed");
        ArrayList<String> fldRet = new ArrayList<String>();
        ArrayList<String> values;

        if (seedLocateDefault.exists()) {
            values = OpenSeedFile(seedLocateDefault);
        } else if (seedLocateOnRoot.exists()) {
            values = OpenSeedFile(seedLocateOnRoot);
        } else if (seedLocateMySeed.exists()) {
            values = OpenSeedFile(seedLocateMySeed);
        } else {
            throw new Exception("File '" + seedFile + "' doesn't exists.");
        }

        for (int y = 0; y < values.size(); y++) {
            fldRet.add(fill(values.get(y).trim(), size, null, null));

        }
        return fldRet;
    }

    /**
     * see my jdoc?
     *
     * @author ggodoy
     * @see
     */
    public synchronized ArrayList<String> OpenSeedFile(File seedFile)
            throws Exception {
        ArrayList<String> seedLines = new ArrayList<String>();

        Scanner scanner = new Scanner(seedFile);

        try {
            int i = 0;
            while (scanner.hasNextLine()) {
                i++;
                seedLines.add(scanner.nextLine());
            }
            // Collections.shuffle(lines);
            return seedLines;
        } finally {
            scanner.close();

        }
    }
}
