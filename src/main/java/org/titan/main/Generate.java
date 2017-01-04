package org.titan.main;

import org.titan.processor.FileGenerator;
import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

import org.titan.processor.FileTemplate;
import org.titan.processor.Mass;
import org.titan.processor.TemplateProcessor;
import org.titan.utils.Messages;

/**
 * Main Class
 */
final class Generate {

    private static final Logger LOGGER = Logger.getLogger(Generate.class.getName());
    private static int simulationLines;
    private static boolean preview;
    private static String fileName;
    private static boolean verify = false;

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) {

        for (int i = 0; i < args.length; i++) {
            if (args[i].endsWith(".xml")) {
                fileName = args[i];

            } else if (args[i].endsWith(".jar")) {

                printJarInfo(args[i]);

            } else if (args[i].contains("-p")) {

                preview(args[i]);

            } else if (args[i].contains("-v")) {
                LOGGER.info(Messages.getMessage("verify"));
                verify = true;
            }

        }
        generateFiles();

    }

    /**
     * When the jarName is passed as a parameter, print the jar info, from
     * manifest.
     *
     * @param jarFile
     */
    public static void printJarInfo(String jarFile) {
        try {
            JarFile jar = new JarFile(jarFile);
            LOGGER.info("Build-Version: \"" + jar.getManifest().getMainAttributes().getValue("final-version")
                    + "\" - code-name: \"" + jar.getManifest().getMainAttributes().getValue("code-name")
                    + "\" - Built-Date: \"" + jar.getManifest().getMainAttributes().getValue("Built-Date") + "\"");

            LOGGER.debug(" Class-Path: " + jar.getManifest().getMainAttributes().getValue("Class-Path"));
        } catch (IOException ioe) {
            LOGGER.error(ioe.getMessage(), ioe);
        }
    }

    /**
     * Generate a console preview with few lines.
     * @param lines 
     */
    public static void preview(String lines) {
        try {
            simulationLines = Integer.valueOf(lines.substring(2, lines.length()));
            preview = true;
            LOGGER.info(Messages.getMessage("preview.on"));
        } catch (NumberFormatException nfe) {
            LOGGER.error(Messages.getMessage("preview.size.error"));
            LOGGER.info(Messages.getMessage("preview.off"));
        }

    }

    /**
     * Generate files.
     */
    public static void generateFiles() {
        TemplateProcessor template;
        Mass mass;
        FileGenerator fileGenerator;

        try {
            if (!new File(fileName).exists()) {
                LOGGER.error(Messages.getMessage("file.not.exist", new String[]{fileName}));
                System.exit(1);
            }

            LOGGER.info(Messages.getMessage("file.working", new String[]{fileName}));

        } catch (ArrayIndexOutOfBoundsException e) {
            LOGGER.error(Messages.getMessage("xml.must.inform"));
            System.exit(1);
        }

        try {
            template = new TemplateProcessor(fileName, verify);
            
            fileGenerator = new FileGenerator();
            if (!verify) {

                for (FileTemplate fileTemplate : template.getFileTemplateList()) {
                    mass = new Mass(fileTemplate, preview, simulationLines);

                    for (int f = 0; f < fileTemplate.getFilesQtd(); f++) {

                        if (fileTemplate.getFilesQtd() == 1) {
                            fileName = fileTemplate.getFileName();
                        } else {
                            fileName = fileTemplate.getFileName() + (f + 1);
                        }

                        fileGenerator.generateFile(mass, fileTemplate, fileName, preview, simulationLines);

                    }

                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}