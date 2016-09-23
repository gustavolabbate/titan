package br.com.cpqd.titan.processor;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import br.com.cpqd.titan.utils.DBConnection;
import br.com.cpqd.titan.utils.Messages;
import java.util.logging.Level;

/**
 *
 * @author ggodoy
 *
 */
public class TemplateProcessor {

    private static final Logger LOGGER = Logger.getLogger(TemplateProcessor.class.getName());
    private String generatedLine;

    public synchronized String getGeneratedLine() {
        return generatedLine;
    }

    public synchronized void setGeneratedLine(String generatedLine) {
        this.generatedLine = generatedLine;
    }
    String templateFile;
    private PrintStream writeFile;
    private StringBuffer verification;
    private List<FileTemplate> fileTemplateList;

    public List<FileTemplate> getFileTemplateList() {
        return fileTemplateList;
    }

    public TemplateProcessor(String template) {
        this.templateFile = template;
    }

    public TemplateProcessor(String template, boolean verify) {
        this.templateFile = template;
        this.fileTemplateList = readTemplate(verify);
    }

    /**
     *
     * @param template
     * @return
     * @throws Exception
     */
    public final List<FileTemplate> readTemplate(boolean verify) {
        try {
            ArrayList<FileTemplate> files = new ArrayList<FileTemplate>();
            FileTemplate file = new FileTemplate();
            DBConnection dbConn = new DBConnection();
            Fields fld;
            ArrayList<Fields> headerFields;
            ArrayList<Fields> trailFields;
            ArrayList<Fields> footerFields;


            Document doc = openXml(templateFile);

            Element configuration = doc.getDocumentElement();

            NodeList db = configuration.getElementsByTagName("database");

            for (int i = 0; i < db.getLength(); i++) {
                Element tag = (Element) db.item(i);

                try {
                    dbConn.setUrl(getChildTagValue(tag, "url").trim().replaceAll("[\r\n\t]", ""));
                    dbConn.setUser(getChildTagValue(tag, "user").trim().replaceAll("[\r\n\t]", ""));
                    dbConn.setPass(getChildTagValue(tag, "password").trim().replaceAll("[\r\n\t]", ""));
                    dbConn.setSgbd(getChildTagValue(tag, "sgbd").trim().replaceAll("[\r\n\t]", ""));

                } catch (Exception e) {
                    LOGGER.error(Messages.getMessage("db.error"));
                }
            }

            file.setDbConn(dbConn);
            NodeList nl = configuration.getElementsByTagName("file");


            for (int i = 0; i < nl.getLength(); i++) {
                file = new FileTemplate();

                headerFields = new ArrayList<Fields>();
                trailFields = new ArrayList<Fields>();
                footerFields = new ArrayList<Fields>();

                Element tag = (Element) nl.item(i);

                file.setFileName(getChildTagValue(tag, "name").trim());
                file.setFileAppend(tag.getAttribute("append"));
                file.setBetweenLine(tag.getAttribute("between"));
                file.setDelimiter(tag.getAttribute("delimiter"));

                try {
                    file.setFilesQtd(Integer.parseInt(getChildTagValue(tag, "files")));
                } catch (Exception e) {
                    file.setFilesQtd(1);
                }

                //get HEADER
                NodeList hd = getChilds(tag, "header");

                for (int e = 0; e < hd.getLength(); e++) {
                    Element el = (Element) hd.item(e);

                    file.setHeaderLines(Integer.parseInt(el.getAttribute("lines")));


                    if (el.getAttribute("shuffle").equalsIgnoreCase("true")) {
                        file.setHeaderShuffle(true);
                    } else {
                        file.setHeaderShuffle(false);
                    }


                    NodeList nfields = getChilds(el, "field");

                    for (int f = 0; f < nfields.getLength(); f++) {
                        fld = new Fields();

                        Element ff = (Element) nfields.item(f);

                        fld.setSize(Integer.parseInt(ff.getAttribute("size")));
                        fld.setName(ff.getAttribute("name"));
                        fld.setVariable(ff.getAttribute("var"));
                        fld.setFunction(ff.getTextContent());


                        headerFields.add(fld);
                    }
                }

                //get TRAIL
                NodeList nd = getChilds(tag, "trail");

                for (int e = 0; e < nd.getLength(); e++) {
                    Element el = (Element) nd.item(e);

                    file.setTrailLines(Integer.parseInt(el.getAttribute("lines")));


                    if (el.getAttribute("shuffle").equalsIgnoreCase("true")) {
                        file.setTrailShuffle(true);
                    } else if (el.getAttribute("shuffle").equalsIgnoreCase("false")) {
                        file.setTrailShuffle(false);
                    } else {
                        file.setTrailShuffle(true);
                    }

                    NodeList nfields = getChilds(el, "field");

                    for (int f = 0; f < nfields.getLength(); f++) {
                        fld = new Fields();

                        Element ff = (Element) nfields.item(f);

                        fld.setSize(Integer.parseInt(ff.getAttribute("size")));
                        fld.setName(ff.getAttribute("name"));
                        fld.setVariable(ff.getAttribute("var"));
                        fld.setFunction(ff.getTextContent());

                        /*
                         func = ff.getTextContent();

                         String[] functions;
                         if(func.contains("="))
                         {
                         functions = func.split("=");
                         fld.setFunction(functions[0]);
                         fld.setValue(functions[1]);
                         }
                         else
                         {
                         fld.setFunction(ff.getTextContent());
                         fld.setValue(null);
                         }
                         */
                        trailFields.add(fld);
                    }
                }


                //get FOOTER
                NodeList fd = getChilds(tag, "footer");

                for (int e = 0; e < fd.getLength(); e++) {
                    Element el = (Element) fd.item(e);

                    file.setFooterLines(Integer.parseInt(el.getAttribute("lines")));

                    if (el.getAttribute("shuffle").equalsIgnoreCase("true")) {
                        file.setFooterShuffle(true);
                    } else if (el.getAttribute("shuffle").equalsIgnoreCase("false")) {
                        file.setFooterShuffle(false);
                    } else {
                        file.setFooterShuffle(true);
                    }

                    NodeList nfields = getChilds(el, "field");

                    for (int f = 0; f < nfields.getLength(); f++) {
                        fld = new Fields();

                        Element ff = (Element) nfields.item(f);

                        fld.setSize(Integer.parseInt(ff.getAttribute("size")));
                        fld.setName(ff.getAttribute("name"));
                        fld.setVariable(ff.getAttribute("var"));
                        fld.setFunction(ff.getTextContent());

                        /*
                         func = ff.getTextContent();

                         String[] functions;
                         if(func.contains("="))
                         {
                         functions = func.split("=");
                         fld.setFunction(functions[0]);
                         fld.setValue(functions[1]);
                         }
                         else
                         {
                         fld.setFunction(ff.getTextContent());
                         fld.setValue(null);
                         }
                         */
                        footerFields.add(fld);
                    }
                }

                file.setHeaderFields(headerFields);
                file.setTrailFields(trailFields);
                file.setFooterFields(footerFields);
                files.add(file);

                if (verify) {
                    verification = new StringBuffer();
                    verification
                            .append("Verification of file: " + file.getFileName() + "\n");
                    verification
                            .append("----------------------------------------")
                            .append("----------------------------------------\n");

                    int totalSize = 0;

                    if (file.getHeaderFields().size() > 0) {
                        verification.append("HEADER:\n");
                        verification.append("    " + "field name - size " + "\n----------------------\n");
                        for (int t = 0; t < file.getHeaderFields().size(); t++) {
                            verification.append("    "
                                    + file.getHeaderFields().get(t).getName() + " - "
                                    + file.getHeaderFields().get(t).getSize() + "\n");

                            totalSize = totalSize + file.getHeaderFields().get(t).getSize();
                        }
                        verification.append("HEADER total size: " + totalSize + "\n\n");
                        totalSize = 0;
                    }


                    if (file.getTrailFields().size() > 0) {
                        verification.append("TRAIL:\n");
                        verification.append("    " + "field name - size " + "\n----------------------\n");
                        for (int t = 0; t < file.getTrailFields().size(); t++) {
                            verification.append("    "
                                    + file.getTrailFields().get(t).getName() + " - "
                                    + file.getTrailFields().get(t).getSize() + "\n");

                            totalSize = totalSize + file.getTrailFields().get(t).getSize();
                        }
                        verification.append("TRAIL total size: " + totalSize + "\n\n");
                        totalSize = 0;
                    }

                    if (file.getFooterFields().size() > 0) {
                        verification.append("FOOTER:\n");
                        verification.append("    " + "field name - size " + "\n----------------------\n");
                        for (int t = 0; t < file.getFooterFields().size(); t++) {
                            verification.append("    "
                                    + file.getFooterFields().get(t).getName() + " - "
                                    + file.getFooterFields().get(t).getSize() + "\n");

                            totalSize = totalSize + file.getFooterFields().get(t).getSize();
                        }
                        verification.append("FOOTER total size: " + totalSize + "\n\n");
                    }

                    LOGGER.info(verification);
                }
            }



            return files;
        } catch (ParserConfigurationException ex) {
            LOGGER.error(ex.getMessage() + " " + ex.getCause());
        } catch (SAXException ex) {
            LOGGER.error(ex.getMessage() + " " + ex.getCause());
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage() + " " + ex.getCause());
        }
        return null;
    }

    /**
     *
     * @param xmlFile
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws Exception
     */
    public final Document openXml(String xmlFile) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(new File(xmlFile));
    }

    /**
     *
     * @param elem
     * @param tagName
     * @return
     * @throws Exception
     */
    private String getChildTagValue(Element elem, String tagName) {
        NodeList children = elem.getElementsByTagName(tagName);
        if (children == null) {
            return null;

        }
        Element child = (Element) children.item(0);
        if (child == null) {
            return null;
        }
        return child.getFirstChild().getNodeValue();
    }

    /**
     *
     * @param elem
     * @param tagName
     * @return
     */
    private NodeList getChilds(Element elem, String tagName) {
        NodeList children = elem.getElementsByTagName(tagName);
        if (children == null) {
            return null;

        }
        return children;

    }
}