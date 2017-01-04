/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.titan.processor;

import org.titan.functions.Columns;
import org.titan.gui.ConsoleOutput;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author ggodoy
 */
public class FileGenerator {

    private static final Logger log = Logger.getLogger(Mass.class.getName());
    private PrintStream fileStreamWriter;
    private Calendar init, finish;

    /**
     *
     * @param cols
     * @param lines
     * @param file
     * @param between
     * @param delimiter
     * @throws Exception
     */
    public void writeFile(Columns cols, int lines, List<Fields> file, String between, String delimiter) throws Exception {
        int rowSize = 0;
        int restart = 0;
        StringBuffer lineAppend;

        for (int l = 0; l < lines; l++) {
            try {

//				Calendar lineinit = Calendar.getInstance();
                lineAppend = new StringBuffer();
                int size = cols.getColumns().size();
                for (int x = 0; x < cols.getColumns().size(); x++) {
                    rowSize = cols.getColumns().get(x).getRows().size();
                    if (rowSize > 1) {
                        try {
                            if (!delimiter.equals("")) {
                                lineAppend.append(cols.getColumns().get(x).getRows().get(l).trim());
                            } else {
                                lineAppend.append(cols.getColumns().get(x).getRows().get(l));
                            }
                        } catch (Exception e) {
                            restart = l - rowSize;
//							System.out.println("\n"+l +" - "+ rowSize + " = " + restart);
                            while (restart >= rowSize) {
                                restart = restart - rowSize;
                            }
//							Collections.shuffle(cols.getColumns().get(x).getRows());
//							lineAppend.append(cols.getColumns().get(x).getRows().get(0));
                            lineAppend.append(cols.getColumns().get(x).getRows().get(restart));
                        }

                    } else {
                        try {
                             lineAppend.append(cols.getColumns().get(x).getRows().get(0));
                        } catch (Exception e) {
//							System.err.println("Linha Gerada com _ERRO_");
                            log.error(e.getMessage(), e);
                            log.error("Exiting.");
                            System.exit(1);
                        }
                    }

                    if (!delimiter.equals("")) {
                        if (x != size - 1) {
                            lineAppend.append(delimiter);
                        }
                    }
                }




                try {
                    fileStreamWriter.println(lineAppend);

                    if (!between.equals("")) {
                        fileStreamWriter.println(between);
                    }
                    //AltVersion

                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }

//				System.out.print("\rline."+(l+1)+"/"+lines);

            } catch (Exception e) {
                log.error(file.get(l).getName(), e);
            }
        }

//		System.out.print("\n");

    }

    public void generateFile(Mass mass, FileTemplate file, String fileName, boolean isSimulate, int simulationLines) throws FileNotFoundException {

//        for (int f = 0; f < file.getFilesQtd(); f++) {
//            String fileName = "";
//
//            if (file.getFilesQtd() == 1) {
//                fileName = file.getFileName();
//            } else {
//                fileName = file.getFileName() + (f + 1);
//            }


            log.info("Generating file: " + fileName);

            File fileWrited = new File(fileName);


            if (!isSimulate) {
                    if (file.getFileAppend().equalsIgnoreCase("true")) {
                    if (fileWrited.exists()) {
                        fileStreamWriter = new PrintStream(new FileOutputStream(fileWrited, true));
                    } else {
                        fileStreamWriter = new PrintStream(fileWrited);
                    }
                } else {
                    fileStreamWriter = new PrintStream(fileWrited);
                }
            }

            //gravar no arquivo o header
            try {
                if (file.getHeaderLines() > 0) {
                    init = Calendar.getInstance();
                    log.info("Header [writeFile.init]");
                    if (isSimulate) {
                        simulateWriteFile(mass.headerCols, simulationLines, file.getHeaderFields(), file.getBetweenLine(), file.getDelimiter());
                    } else {
                        writeFile(mass.headerCols, file.getHeaderLines(), file.getHeaderFields(), file.getBetweenLine(), file.getDelimiter());
                    }

                    finish = Calendar.getInstance();
                    log.debug("Header dtwrt:" + (finish.getTimeInMillis() - init.getTimeInMillis()) / 1000 + " secs");
                    log.info("Header [writeFile.finish]");
                }
            } catch (Exception e) {
                log.error("Error generating file", e);
            }

            //gravar no arquivo o trailer
            if (file.getTrailLines() > 0) {
                try {

                    if (file.getTrailLines() > 0) {
                        init = Calendar.getInstance();
                        log.info("Trailer [writeFile.init]");

                        if (isSimulate) {
                            simulateWriteFile(mass.trailerCols, simulationLines, file.getTrailFields(), file.getBetweenLine(), file.getDelimiter());
                        } else {
                            writeFile(mass.trailerCols, file.getTrailLines(), file.getTrailFields(), file.getBetweenLine(), file.getDelimiter());
                        }

                        finish = Calendar.getInstance();
                        log.debug("Trailer dtwrt:" + (finish.getTimeInMillis() - init.getTimeInMillis()) / 1000 + " secs");
                        log.info("Trailer [writeFile.finish]");
                    }
                } catch (Exception e) {
                    log.info("Error generating file", e);
                }
            }

            //gravar no arquivo o footer
            try {
                if (file.getFooterLines() > 0) {
                    init = Calendar.getInstance();
                    log.info("Footer [writeFile.init]");
                    if (isSimulate) {
                        simulateWriteFile(mass.footerCols, simulationLines, file.getFooterFields(), file.getBetweenLine(), file.getDelimiter());
                    } else {
                        writeFile(mass.footerCols, file.getFooterLines(), file.getFooterFields(), file.getBetweenLine(), file.getDelimiter());
                    }
                    finish = Calendar.getInstance();
                    log.debug("Footer dtwrt:" + (finish.getTimeInMillis() - init.getTimeInMillis()) / 1000 + " secs");
                    log.info("Footer [writeFile.finish]");
                }
            } catch (Exception e) {
                log.error("Error writing footer section", e);
            }


            log.info("File: '" + fileName + "' sucessfuly generated.");

//        }
    }

    /**
     *
     * @param cols
     * @param lines
     * @param file
     * @param between
     * @param delimiter
     * @throws Exception
     */
    public void simulateWriteFile(Columns cols, int lines, List<Fields> file, String between, String delimiter) throws Exception {
        int rowSize = 0;
        int restart = 0;
        StringBuffer lineAppend;
        ConsoleOutput console = new ConsoleOutput();
        console.getFrame().setVisible(true);
        int posicao = 0;
        String text = "";

        for (int l = 0; l < lines; l++) {
            try {

//				Calendar lineinit = Calendar.getInstance();
                lineAppend = new StringBuffer();
                int size = cols.getColumns().size();
                for (int x = 0; x < cols.getColumns().size(); x++) {
                    rowSize = cols.getColumns().get(x).getRows().size();
                    if (rowSize > 1) {
                        try {
                            if (!delimiter.equals("")) {
                                lineAppend.append(cols.getColumns().get(x).getRows().get(l).trim());
                            } else {
                                lineAppend.append(cols.getColumns().get(x).getRows().get(l));
                            }
                        } catch (Exception e) {
                            restart = l - rowSize;
//							System.out.println("\n"+l +" - "+ rowSize + " = " + restart);
                            while (restart >= rowSize) {
                                restart = restart - rowSize;
                            }
//							Collections.shuffle(cols.getColumns().get(x).getRows());
//							lineAppend.append(cols.getColumns().get(x).getRows().get(0));
                            lineAppend.append(cols.getColumns().get(x).getRows().get(restart));
                        }

                    } else {
                        try {
                            lineAppend.append(cols.getColumns().get(x).getRows().get(0));
                        } catch (Exception e) {
//							System.err.println("Linha Gerada com _ERRO_");
                            lineAppend.append("_ERRO_");
                        }
                    }

                    if (!delimiter.equals("")) {
                        if (x != size - 1) {
                            lineAppend.append(delimiter);
                        }
                    }
                }




                try {
//                    fileStreamWriter.println(lineAppend);
//
//                    if (!between.equals("")) {
//                        fileStreamWriter.println(between);
//                    }
                    //AltVersion

                    text = text + lineAppend.toString() + "\n";
                    console.getcPanel().setText(text);
                    posicao = console.getcPanel().getDocument().getLength();
                    console.getcPanel().setCaretPosition(posicao);
//						Thread.sleep(500);

                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }

//				System.out.print("\rline."+(l+1)+"/"+lines);

            } catch (Exception e) {
                log.error(file.get(l).getName(), e);
            }
        }

//		System.out.print("\n");

    }
}
