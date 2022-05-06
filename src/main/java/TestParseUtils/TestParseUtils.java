package TestParseUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class TestParseUtils {
    public static String pathToReports = "C:/repos/testing-project/New-Test-Reports/"; // change as per local repo location
    public static String reportFolder = "/XML/";
    public static String csvFolder = "/CSV/";
    public static Processors jProcessors = new Processors("j");
    public static Processors slProcessors = new Processors("sl");
    public static Processors tmProcessors = new Processors("tm");
    static final ArrayList<JunrarBean> jBeans = new ArrayList<>();
    // public static List<SimplifyLearningBean> slBeans;
    // public static List<TelekMathBean> tmBeans;
    // public static List<JsonJavaBean> jjBeans;

    public static void main(String[] args) throws Exception {
        String loopString = "Y";
        Scanner in = new Scanner(System.in);

        System.out.println("Test Parse Util");

        /**
         * Main loop which allows the selection of different program functions.
         */

        while (loopString.equalsIgnoreCase("y")) {
            System.out.println("\n1. Print JSON-java tests");
            System.out.println("2. Print junrar tests");
            System.out.println("3. Print SimplifyLearning tests");
            System.out.println("4. Print telek-math tests");
            System.out.println("\n5. Print spreadsheet sum functions");
            System.out.println("\n6. Print junrar csv");
            System.out.println("7. Print SimplifyLearning csv");
            System.out.println("8. Print telek-math csv");
            System.out.print("\nEnter a selection: ");
            int selection = Integer.parseInt(in.nextLine());

            switch (selection) {
                case 1:
                    printTestInfoAsCopyLists("JSON-java");
                    break;
                case 2:
                    printTestInfoAsCopyLists("junrar");
                    break;
                case 3:
                    printTestInfoAsCopyLists("SimplifyLearning");
                    break;
                case 4:
                    printTestInfoAsCopyLists("telek-math");
                    break;
                case 5:
                    printSumFunctions();
                    break;
                case 6:
                    // parseCSV("j.csv");
                    readJunrarCSV("j.csv");
                    break;
                case 7:
                    parseCSV("sl.csv");
                    break;
                case 8:
                    parseCSV("tm.csv");
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * A function to print a list of Excel functions to sum a series of table column values in a set of rows.
     */
    static void printSumFunctions() {
        for (int i = 1; i < 160; i++) {
            System.out.println("=SUM(D"+ (i + 1) + ":G"+ (i + 1) + ")");
        }
    }

    /**
     * Method that prints test details as categorized columns for import into Excel.
     * A more ideal implementation would process from XML to CSV.
     */
    public static void printTestInfoAsCopyLists(String project) throws ParserConfigurationException {
        List<String> xmlFiles = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<String> locationUrls = new ArrayList<>();
        List<String> metainfos = new ArrayList<>();
        List<String> durations = new ArrayList<>();
        List<String> statuses = new ArrayList<>();
        String suiteFileName = "";
        String path = pathToReports + project + reportFolder;
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < (listOfFiles != null ? listOfFiles.length : 0); i++) {
            if (listOfFiles[i].isFile()) {
                suiteFileName = listOfFiles[i].getName();
                // System.out.println(printStr.substring(0, (printStr.length() - 6)));
                xmlFiles.add(path + suiteFileName);
            }
        }

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

        System.out.println(project + " test data:");

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

    static void parseCSV(String targetFile) throws IOException, CsvValidationException {
        String pathToTargetFile = pathToReports + csvFolder + targetFile;
        Map<String, String> values = new CSVReaderHeaderAware(new FileReader(pathToTargetFile)).readMap();

        printMap(values);
    }

    /**
     * Pretty print a map, from StackExchange.
     */
    public static <K, V> void printMap(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey()
                    + " Value : " + entry.getValue());
        }
    }

    /**
     * Adapted from CsvBeanReader example in SuperCSV documentation.
     */
    private static void readJunrarCSV(String fileName) throws Exception {
        String pathToTargetFile = pathToReports + csvFolder + fileName;

        ICsvBeanReader beanReader = null;
        try {
            beanReader = new CsvBeanReader(new FileReader(pathToTargetFile), CsvPreference.STANDARD_PREFERENCE);

            // the header elements are used to map the values to the bean (names must match)
            final String[] header = beanReader.getHeader(true);
            final CellProcessor[] processors = jProcessors.processor;

            JunrarBean jBean;
            while((jBean = beanReader.read(JunrarBean.class, header, processors)) != null ) {
                // System.out.println(String.format("Adding bean: lineNo=%s, rowNo=%s, test=%s", beanReader.getLineNumber(),
                        // beanReader.getRowNumber(), jBean));
                jBeans.add(jBean);
            }

            prioritizejCSV(jBeans);
        }
        finally {
            if( beanReader != null ) {
                beanReader.close();
            }
        }
    }

    static void prioritizejCSV(List<JunrarBean> beansList) {
        JunrarCSV jCSV = new JunrarCSV(beansList);
        jCSV.printMostLinesFirst();
    }

    static void testjCSVFunc(List<JunrarBean> beansList) {
        int max = 0;
        for (JunrarBean bean : beansList) {
            int tempTotalCov = bean.getTotalCov();
            System.out.println(tempTotalCov);
            if (tempTotalCov > max) max = tempTotalCov;
        }
        System.out.println("Max coverage: " + max);
    }
}

