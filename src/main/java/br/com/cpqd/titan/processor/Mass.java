/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cpqd.titan.processor;

import br.com.cpqd.titan.api.impl.FunctionsProcessor;
import br.com.cpqd.titan.functions.Columns;
import br.com.cpqd.titan.functions.Rows;
import br.com.cpqd.titan.utils.DBConnection;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author ggodoy
 */
public class Mass {

    private static final Logger log = Logger.getLogger(Mass.class.getName());
    private FunctionsProcessor functionsProcessor = new FunctionsProcessor();
    Columns headerCols = new Columns();
    Columns trailerCols = new Columns();
    Columns footerCols = new Columns();

    public Columns getHeaderCols() {
        return headerCols;
    }

    public Columns getTrailerCols() {
        return trailerCols;
    }

    public Columns getFooterCols() {
        return footerCols;
    }
    FileTemplate file;
    boolean isSimulate;
    int simulationLines;

    public Mass(FileTemplate file, boolean isSimulate, int simulationLines) {
        this.file = file;
        this.isSimulate = isSimulate;
        this.simulationLines = simulationLines;
        this.generateData();
    }

    public Mass(FileTemplate file) {
        this.file = file;
        this.isSimulate = false;
        this.simulationLines = 0;
        this.generateData();
    }
    
    
    
    /**
     *
     * @param file
     * @param isSimulate
     * @param simulationLines
     * @return
     * @throws FileNotFoundException
     * @throws Exception
     */
    public final void generateData() {
        int numLines = 0;

        Calendar init;
        Calendar finish;

        //Generate Header Column
        if (file.getHeaderLines() > 0) {
            init = Calendar.getInstance();

            if (isSimulate) {
                file.setHeaderLines(simulationLines);
            }

            headerCols = createColumnsData(file.getHeaderFields(), file.getHeaderLines(), file.getHeaderShuffle(), file.getDbConn());

            finish = Calendar.getInstance();
            calculateElapsedTime(init, finish, "header");
        }


        //Generate Trailer Column
        if (file.getTrailLines() > 0) {
            init = Calendar.getInstance();

            if (isSimulate) {
                file.setTrailLines(simulationLines);
            }

            trailerCols = createColumnsData(file.getTrailFields(), file.getTrailLines(), file.getTrailShuffle(), file.getDbConn());

            finish = Calendar.getInstance();
            calculateElapsedTime(init, finish, "trailer");
        }


        //Generate Footer Column
        if (file.getFooterLines() > 0) {
            init = Calendar.getInstance();
//            LOGGER.info("Footer [GenerateData.init]");

            if (isSimulate) {
                file.setFooterLines(simulationLines);
            }

            footerCols = createColumnsData(file.getFooterFields(), file.getFooterLines(), file.getFooterShuffle(), file.getDbConn());

            finish = Calendar.getInstance();
            calculateElapsedTime(init, finish, "footer");
        }
    }

    /**
     *
     * @param file
     * @param lines
     * @param shuffle
     * @return
     */
    public Columns createColumnsData(List<Fields> file, int lines, Boolean shuffle, DBConnection dbConn) {
        Calendar init = Calendar.getInstance();
        Calendar finish;
        String function, value, variable;
        int size;

        Columns cols = new Columns();
        Rows rows;
        ArrayList<Rows> r = new ArrayList<Rows>();
        ConcurrentHashMap<String, List<String>> variables = new ConcurrentHashMap<String, List<String>>();

        ArrayList<String> _rows;

        // For each field, create list of possible values and stores on column
        for (int field = 0; field < file.size(); field++) {
            log.debug((field + 1) + "/" + file.size() + " " + file.get(field).getName());
            rows = new Rows();
            _rows = new ArrayList<String>();

            function = file.get(field).getFunction();
            value = file.get(field).getValue();
            size = file.get(field).getSize();
            variable = file.get(field).getVariable();

            if (!variable.equals("")) {
                if (!variable.startsWith("$")) {
                    try {
                        _rows = functionsProcessor.callFunction(function, size, value, variable, dbConn, lines, null);
                        variables.put(variable, _rows);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                } else {
                    variable = variable.replace("$", "");

                    for (int i = 0; i < variables.get(variable).size(); i++) {
                        _rows.add(variables.get(variable).get(i).toString().replace("[", "").replace("]", "").substring(0, size));
                    }
                }
            } else {
                try {
                    // check if is a calc function
                    if (function.startsWith("calc")) {
                        _rows = new ArrayList<String>();
                        ArrayList<String> expression;

                        expression = parseExpression(function, variables, lines);
                        _rows = functionsProcessor.callFunction(function, size, value, variable, dbConn, lines, expression);

                    } else {
                        _rows = functionsProcessor.callFunction(function, size, value, variable, dbConn, lines, null);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }

            if (shuffle) {
                Collections.shuffle(_rows);
            }

            rows.setRows(_rows);
            r.add(rows);
            rows = null;
            _rows = null;
        }
        cols.setColumns(r);

        finish = Calendar.getInstance();
        log.debug("objcr:" + (finish.getTimeInMillis() - init.getTimeInMillis()) / 1000 + " secs");
        return cols;
    }
    
    
    private String identifierField;

    public synchronized String getIdentifierField() {
        return identifierField;
    }

    public synchronized void setIdentifierField(String identifierField) {
        this.identifierField = identifierField;
    }

    public synchronized ConcurrentHashMap<String, String> createSampleLine(List<Fields> file, int lines, DBConnection dbConn, String lineIdentifier) {
        String function, value, variable;
        StringBuilder lineAppend = new StringBuilder();
        ConcurrentHashMap<String, String> ret = new ConcurrentHashMap<String, String>();
        int size;
        HashMap<String, String> variables = new HashMap<String, String>();

//        FunctionsProcessor functions = new FunctionsProcessor();

        for (Iterator<Fields> it = file.iterator(); it.hasNext();) {
            Fields fields = it.next();
            function = fields.getFunction();
            value = fields.getValue();
            size = fields.getSize();
            variable = fields.getVariable();
            String name = fields.getName();

            if (!variable.equals("")) {
                if (!variable.startsWith("$")) {
                    try {
                        String variableValue = functionsProcessor.callFunction(function, size, value, variable, dbConn, lines, null).get(0);
                        lineAppend.append(variableValue);
                        if (name.equalsIgnoreCase(lineIdentifier)) {
                            ret.put("identifier", variableValue);
//                            setIdentifierField(variableValue);
                        }
                        variables.put(variable, variableValue);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                } else {
                    variable = variable.replace("$", "");
                    lineAppend.append(variables.get(variable));
                    if (name.equalsIgnoreCase(lineIdentifier)) {
//                            setIdentifierField(variables.get(variable));
                        ret.put("identifier", variables.get(variable));
                    }

                }
            } else {
                try {

                    String variableValue = functionsProcessor.callFunction(function, size, value, variable, dbConn, lines, null).get(0);

                    lineAppend.append(variableValue);
                    if (name.equalsIgnoreCase(lineIdentifier)) {
                        ret.put("identifier", variableValue);

                    }

                } catch (Exception e) {
                    log.error(e.getMessage());
                    System.out.println(Thread.currentThread().getId() + " - " + e.getMessage() + " " + function);

                }
            }
        }
        ret.put("line", lineAppend.toString());
        return ret;
    }

    private void calculateElapsedTime(Calendar init, Calendar finish, String section) {
        Long timeElapsed;
        timeElapsed = finish.getTimeInMillis() - init.getTimeInMillis();
        if (section != null) {
            log.info("[" + section.toUpperCase() + "] data generated in " + timeElapsed + "ms (" + timeElapsed / 1000 + "s.)");
        }
    }

    private ArrayList<String> parseExpression(String expression, ConcurrentHashMap<String, List<String>> variables, int lines) {
        ArrayList<String> expr = new ArrayList<String>();
        String vals[] = null;
        StringBuffer conc;
        String key;

        for (int c = 0; c < lines; c++) {
            conc = new StringBuffer();
            if (expression.contains("'$")) {
                vals = expression.split("'");

                for (int i = 0; i < vals.length; i++) {
                    if (vals[i].startsWith("$")) {
                        key = vals[i].replace("$", "");
//                        for (int x = 0; x < variables.get(vals[i]).size(); x++) {
                        conc.append(getVariable(variables, key, c));

//                        }
                    } else {
                        conc.append(vals[i]);
                    }
                }
                expr.add(conc.toString());
            } else {
                expr.add(expression.replace("calc=", ""));
            }
        }
        return expr;
    }

    private String getVariable(ConcurrentHashMap<String, List<String>> variables, String key, int position) {
        return variables.get(key).get(position);
    }
}
