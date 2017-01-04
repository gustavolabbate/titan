/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.titan.api.impl;

import org.titan.api.FunctionsFacade;
import org.titan.utils.DBConnection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ggodoy
 */
public class FunctionsProcessor {

    FunctionsFacade functionsProvider = new FunctionsImpl();
    private List<String> functionMisc;
    private List<String> valueMisc;
    private Boolean misc = false;
    private String titanHome = System.getenv("TITAN_HOME");

    public synchronized ArrayList<String> callFunction(String function, int size,
            String value, String variable, DBConnection dbConn,
            int numLines, ArrayList<String> expressions) throws Exception {
        try {
            ArrayList<String> field;

            field = functionValue(function, size, value, variable, dbConn,
                    numLines, expressions);

            return field;
            // }
        } catch (Exception e) {
            throw new Exception("[Fn: " + function + "]" + "[Vl: " + value + "] [Var: " + variable + "] thrown message: " + e.getMessage(),
                    e.getCause());
        }
    }

    public synchronized ArrayList<String> functionValue(String function, int size,
            String value, String variable, DBConnection dbConn,
            int numLines, ArrayList<String> expressions) throws Exception {
        // String field=null;
        ArrayList<String> field = new ArrayList<String>();
        ArrayList<String> fldCur = new ArrayList<String>();
        String[] functions;
        String[] subFunction;
        functionMisc = new ArrayList<String>();
        valueMisc = new ArrayList<String>();

        functionsProvider.setVariation(numLines);

        if (function.startsWith("fixed")) {
            functionMisc.add("fixed");
            valueMisc.add(function.replace("fixed=", ""));
        } else if (function.startsWith("misc")) {
            // variableMisc.add(null);
            misc = true;
            functions = function.split(";");

            for (int x = 0; x < functions.length; x++) {
                functions[x] = functions[x].replace("misc={", "");
                functions[x] = functions[x].replace("}", "");
                functions[x] = functions[x].trim();
            }

            for (int x = 0; x < functions.length; x++) {
                if (functions[x].contains("sql=")) {
                    String[] sql = functions[x].split("sql=");
                    functionMisc.add("sql");
                    valueMisc.add(sql[1]);
                } else if (functions[x].contains("=")) {
                    subFunction = functions[x].split("=");
                    functionMisc.add(subFunction[0].trim());
                    valueMisc.add(subFunction[1]);
                } else {
                    functionMisc.add(functions[x].trim());
                    valueMisc.add(null);
                }
            }
        } else {
            // variableMisc.add(value);
            if (function.contains("sql=")) {
                functions = function.split("sql=");
                functionMisc.add("sql");
                valueMisc.add(functions[1]);
            } else if (function.contains("=")) {
                functions = function.split("=");
                functionMisc.add(functions[0].trim());
                valueMisc.add(functions[1]);
            } else {
                functionMisc.add(function.trim());
                valueMisc.add(null);
            }
        }

        for (int x = 0; x < functionMisc.size(); x++) {
            int random = (int) (Math.random() * (functionMisc.size()));

            // if(misc)
            // {
            // Collections.shuffle(fldCur);
            // }

            if (!function.startsWith("calc")) {
                function = functionMisc.get(random);
                value = valueMisc.get(random);
            } else {
                function = "calc";
            }

            if (function.toLowerCase().equals("zeros")) {
                field = functionsProvider.zeros(size);
            } else if (function.toLowerCase().equals("vazio")) {
                field = functionsProvider.spaces(size);
            } else if (function.toLowerCase().equals("numero")) {
                String[] vl = value.split(",");
                field = functionsProvider.newInt(size, Integer.parseInt(vl[0]),
                        Integer.parseInt(vl[1]));
            } else if (function.toLowerCase().equals("calc")) {
                for (int i = 0; i < expressions.size(); i++) {
                    field.add(functionsProvider.calc(size, expressions.get(i)));
                }
            } else if (function.toLowerCase().equals("numeroevazio")) {
                String[] vl = value.split(",");
                for (int i = 0; i < functionsProvider.getVariation(); i++) {
                    field.add(functionsProvider.intSpaces(size, Integer.parseInt(vl[0]),
                            Integer.parseInt(vl[1])));
                }
            } else if (function.toLowerCase().equals("texto")) {
                for (int i = 0; i < functionsProvider.getVariation(); i++) {
                    field.add(functionsProvider.charAa(size));
                }
            } else if (function.toLowerCase().equals("textoevazio")) {
                for (int i = 0; i < functionsProvider.getVariation(); i++) {
                    field.add(functionsProvider.charAaSpaces(size));
                }
            } else if (function.toLowerCase().equals("mascara")) {
                field = functionsProvider.maskNumber(value, size);
            } else if (function.toLowerCase().equals("dominio")) {
                String[] val = value.split(",");
                field = functionsProvider.fixed(val, size);
            } else if (function.toLowerCase().equals("fixed")) {
                field.add(functionsProvider.asis(value, size));
            } else if (function.toLowerCase().equals("horavalida")) {
                field = functionsProvider.hourValid(value);
            } else if (function.toLowerCase().equals("datavalida")) {
                field = functionsProvider.dateValid(value);
            } else if (function.toLowerCase().equals("horainvalida")) {
                if (value.toLowerCase().equals("hhmmss")) {
                    field = functionsProvider.hourInvalid_HHmmss();
                } else if (value.toLowerCase().equals("hhmm")) {
                    field = functionsProvider.hourInvalid_HHmm();
                }
            } else if (function.toLowerCase().equals("datainvalida")) {
                if (value.toLowerCase().equals("ddmmyyyy")) {
                    field = functionsProvider.dateInvalid_ddMMyyyy();
                } else if (value.toLowerCase().equals("yyyymmdd")) {
                    field = functionsProvider.dateInvalid_yyyyMMdd();
                } else if (value.toLowerCase().equals("yyyyddmm")) {
                    field = functionsProvider.dateInvalid_yyyyddMM();
                } else if (value.toLowerCase().equals("yymmdd")) {
                    field = functionsProvider.dateInvalid_yyMMdd();
                }
            } else if (function.toLowerCase().equals("periodo")) {
                for (int i = 0; i < functionsProvider.getVariation(); i++) {
                    field.add(functionsProvider.datePeriod(value, size));
                }
            } else if (function.toLowerCase().equals("datavalidafuturo")) {
                for (int i = 0; i < functionsProvider.getVariation(); i++) {
                    field.add(functionsProvider.dateValidFuture(value));
                }
            } else if (function.toLowerCase().equals("datavalidapassado")) {
                for (int i = 0; i < functionsProvider.getVariation(); i++) {
                    field.add(functionsProvider.dateValidPast(value));
                }
            } else if (function.toLowerCase().equals("horavalidapassado")) {
                String[] val = value.split(",");
                for (int i = 0; i < functionsProvider.getVariation(); i++) {
                    field.add(functionsProvider.hourValidPast(val[0],
                            Integer.parseInt(val[1].trim())));
                }
            } else if (function.toLowerCase().equals("horavalidafuturo")) {
                String[] val = value.split(",");
                for (int i = 0; i < functionsProvider.getVariation(); i++) {
                    field.add(functionsProvider.hourValidFuture(val[0],
                            Integer.parseInt(val[1].trim())));
                }
            } else if (function.toLowerCase().equals("agora")) {
                for (int i = 0; i < functionsProvider.getVariation(); i++) {
                    field.add(functionsProvider.now(value));
                }
            } else if (function.toLowerCase().equals("arquivo")) {
                // for(int i=0; i<variation; i++)
                // {
                field = functionsProvider.file(value, size);
                // }
            } else if (function.toLowerCase().equals("numerosequencial")) {
                field = functionsProvider.intSequence(size, value);
            } else if (function.toLowerCase().equals("mascarasequencial")) {
                field = functionsProvider.maskNumberSequence(value, size);
            } else if (function.toLowerCase().equals("sql")) {
                field = functionsProvider.query(size, value, dbConn);
            } else if (function.toLowerCase().equals("cpf")) {
                field = functionsProvider.generateCnpjCpf(size, function);
            } else if (function.toLowerCase().equals("cnpj")) {
                field = functionsProvider.generateCnpjCpf(size, function);
            } else if (function.toLowerCase().equals("cpfponto")) {
                field = functionsProvider.generateCnpjCpf(size, function);
            } else if (function.toLowerCase().equals("cnpjponto")) {
                field = functionsProvider.generateCnpjCpf(size, function);
            } else if (function.equalsIgnoreCase("periodoIncremental")) {
                field = functionsProvider.periodBetween(value, size);

            } else {
                throw new Exception("[Fn: " + function + "]" + "[Vl: " + value + "] [Var: " + variable + "] thrown message: "
                        + "Incorrect function or value");
            }
            fldCur.addAll(field);
        }

        // if(misc)
        // {
        // Collections.shuffle(fldCur);
        // }
        return fldCur;
    }
}
