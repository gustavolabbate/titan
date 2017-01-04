package br.com.cpqd.titan.api;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;

import br.com.cpqd.titan.utils.DBConnection;

public interface FunctionsFacade {

    /**
     * Fill a string with a pattern.
     *
     * @param strSource The string to be filled.
     * @param size New size of String
     * @param direction Define direction of filling (< for left, null for
     * right). @param charFill The cha
     * r used for filling. If null, will be filled with empty spaces.
     * @return String filled with chars.
     */
    String fill(String strSource, int size, String direction, String charFill);

    /**
     * Generates a valid CPF or CNPJ number
     *
     * @param size - The size of the number generated
     * @param type - cpf, cnpj, cpfPonto, cnpjPonto (includes punctuation)
     * @return ArrayList with generated numbers (depends of variation).
     * @throws Exception
     */
    ArrayList<String> generateCnpjCpf(int size, String type) throws Exception;

    ArrayList<String> query(int size, String value, DBConnection dbConn) throws Exception;

    ArrayList<String> zeros(int size);

    ArrayList<String> spaces(int size);

    String calc(int size, String expression);

    ArrayList<String> newInt(int size, int init, int finish);

    ArrayList<String> intSequence(int size, String value);

    ArrayList<String> maskNumberSequence(String pattern, int size)
            throws ParseException;

    ArrayList<String> maskNumber(String value, int size) throws ParseException;

    String intSpaces(int size, int init, int finish);

    String charAa(int size);

    String charAaSpaces(int size);

    ArrayList<String> fixed(String[] values, int size);

    String asis(String values, int size);

    ArrayList<String> hourValid(String format) throws Exception;

    ArrayList<String> dateValid(String format) throws Exception;

    ArrayList<String> hourInvalid_HHmmss() throws Exception;

    ArrayList<String> hourInvalid_HHmm() throws Exception;

    ArrayList<String> dateInvalid_ddMMyyyy() throws Exception;

    ArrayList<String> dateInvalid_yyyyMMdd() throws Exception;

    ArrayList<String> dateInvalid_yyyyddMM() throws Exception;

    ArrayList<String> dateInvalid_yyMMdd() throws Exception;

    String dateValidFuture(String format) throws Exception;

    String datePeriod(String value, int size) throws Exception;

    String hourValidPast(String format, int hourBack) throws Exception;

    String hourValidFuture(String format, int hourForward) throws Exception;

    String dateValidPast(String format) throws Exception;

    String now(String format) throws Exception;

    ArrayList<String> file(String seedFile, int size) throws Exception;

    /**
     * Open a seed file.
     *
     * @param seedFile File
     * @return ArrayList - contents of the seed file
     * @throws Exception
     */
    ArrayList<String> OpenSeedFile(File seedFile) throws Exception;

    /**
     * Sets the variation of the generations. This is how many lines yout get in
     * returns
     *
     * @param variation
     */
    void setVariation(int variation);

    public int getVariation();

    /**
     *
     * @param value
     * @param size
     * @return
     * @throws Exception
     */
    public ArrayList<String> periodBetween(String value, int size) throws Exception;
}
