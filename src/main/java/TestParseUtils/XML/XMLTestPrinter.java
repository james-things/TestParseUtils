package TestParseUtils.XML;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A class which handles printing jUnit XML results in a way that can be easily copied into an Excel document.
 */
public class XMLTestPrinter {
    private final String pathToReports;
    private final String reportFolder;
    private final String project;

    // Constructor
    public XMLTestPrinter(String pathToReports, String reportFolder, String project) {
        this.pathToReports = pathToReports;
        this.reportFolder = reportFolder;
        this.project = project;
    }

    /**
     * Print details of project the class was instantiated with.
     */
    public void printTestInfoAsCopyLists() {
        List<String> xmlFiles = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<String> locationUrls = new ArrayList<>();
        List<String> metainfos = new ArrayList<>();
        List<String> durations = new ArrayList<>();
        List<String> statuses = new ArrayList<>();
        String suiteFileName = "";
        String path = this.pathToReports + this.project + this.reportFolder;
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < (listOfFiles != null ? listOfFiles.length : 0); i++) {
            if (listOfFiles[i].isFile()) {
                suiteFileName = listOfFiles[i].getName();
                xmlFiles.add(path + suiteFileName);
            }
        }

        // For xml files in the target directory, process target elements and nodes
        for (String xmlFile : xmlFiles) {
            try {
                File inputFile = new File(xmlFile);
                System.out.println(xmlFile + "\n");
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(inputFile);
                doc.getDocumentElement().normalize();

                NodeList nList = doc.getElementsByTagName("test");

                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        durations.add(eElement.getAttributeNode("duration") != null ?
                                eElement.getAttributeNode("duration").getValue() : "null");
                        locationUrls.add(eElement.getAttributeNode("locationUrl") != null ?
                                eElement.getAttributeNode("locationUrl").getValue() : "null");
                        names.add(eElement.getAttributeNode("name") != null ?
                                eElement.getAttributeNode("name").getValue() : "null");
                        metainfos.add(eElement.getAttributeNode("metainfo") != null ?
                                eElement.getAttributeNode("metainfo").getValue() : "null entry");
                        statuses.add(eElement.getAttributeNode("status") != null ?
                                eElement.getAttributeNode("status").getValue() : "null entry");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Print results
        System.out.println(this.project + " test data:");

        System.out.println("\nClasses to Export\n");
        for (String loc : locationUrls) {
            String newLoc = loc.substring(10); // may need to adjust
            System.out.println(newLoc);
        }
        System.out.println("\nTest Names to Export\n");
        for (String name : names) {
            System.out.println(name);
        }
        System.out.println("\nExecution Times to Export\n");
        for (String duration : durations) {
            System.out.println(duration);
        }
        System.out.println("\nPass/Fail to Export\n");
        for (String status : statuses) {
            System.out.println(status);
        }
    }
}
