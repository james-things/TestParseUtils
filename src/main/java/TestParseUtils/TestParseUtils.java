package TestParseUtils;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import javax.xml.parsers.ParserConfigurationException;

import TestParseUtils.JavaBeans.JunrarBean;
import TestParseUtils.JavaBeans.SimplifyBean;
import TestParseUtils.JavaBeans.TelekBean;
import TestParseUtils.CSV.JunrarCSV;
import TestParseUtils.CSV.SimplifyCSV;
import TestParseUtils.CSV.TelekCSV;
import TestParseUtils.XML.XMLTestPrinter;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

/**
 * A collection of customized utilities for processing and calculating prioritization
 * in a collection of sample java projects.
 */
public class TestParseUtils {
    //public static String pathToReports = "C:/repos/testing-project/New-Test-Reports/"; // change as per local repo location
    public static String pathToReports = new File("src/main/resources/DataFiles").getAbsolutePath();
    public static String reportFolder = "/XML/";
    public static String csvFolder = "/CSV/";
    public static Processors jProcessors = new Processors("j");
    public static Processors slProcessors = new Processors("sl");
    public static Processors tmProcessors = new Processors("tm");
    static final ArrayList<JunrarBean> jBeans = new ArrayList<>();
    static final ArrayList<SimplifyBean> slBeans = new ArrayList<>();
    static final ArrayList<TelekBean> tmBeans = new ArrayList<>();
    // static final List<JsonJavaBean> jjBeans = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        String loopString = "Y";
        Scanner in = new Scanner(System.in);

        System.out.println("Test Parse Util");
        System.out.println(pathToReports);

         // Main loop which allows the selection of different program functions.
        while (loopString.equalsIgnoreCase("y")) {
            System.out.println("\n1. Print JSON-java tests");
            System.out.println("2. Print junrar tests");
            System.out.println("3. Print SimplifyLearning tests");
            System.out.println("4. Print telek-math tests");
            System.out.println("\n5. Print spreadsheet sum functions");
            System.out.println("\n6. Calculate junrar prioritization");
            System.out.println("7. Calculate SimplifyLearning prioritization");
            System.out.println("8. Calculate telek-math prioritization");
            System.out.println("\n9. Exit application");
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
                    prioritizeJunrarCSV();
                    break;
                case 7:
                    prioritizeSimplifyCSV();
                    break;
                case 8:
                    prioritizeTelekCSV();
                    break;
                case 9:
                    loopString = "N";
                    System.out.println("Application will now terminate.");
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
        for (int i = 1; i < 160; i++) { // edit rows as needed
            System.out.println("=SUM(D"+ (i + 1) + ":G"+ (i + 1) + ")"); // edit columns as needed
        }
    }

    /**
     * Method that prints test details as categorized columns for import into Excel.
     */
    public static void printTestInfoAsCopyLists(String project) {
        XMLTestPrinter xmlPrinter = new XMLTestPrinter(pathToReports, reportFolder, project);
        xmlPrinter.printTestInfoAsCopyLists();
    }

    /**
     * Prioritize tests two ways and print results for both approaches.
     * Adapted from CsvBeanReader example in SuperCSV documentation.
     */
    private static void prioritizeJunrarCSV() throws Exception {
        String pathToTargetFile = pathToReports + csvFolder + "j.csv";

        ICsvBeanReader beanReader = null;
        try {
            beanReader = new CsvBeanReader(new FileReader(pathToTargetFile), CsvPreference.STANDARD_PREFERENCE);

            // the header elements are used to map the values to the bean (names must match)
            final String[] header = beanReader.getHeader(true);
            final CellProcessor[] processors = jProcessors.processor;

            JunrarBean jBean;
            while((jBean = beanReader.read(JunrarBean.class, header, processors)) != null ) {
                jBeans.add(jBean);
            }

            JunrarCSV jCSV = new JunrarCSV(jBeans);
            jCSV.printMostLinesFirst();
            jCSV.printOtherPrioritization();
        }
        finally {
            if( beanReader != null ) {
                beanReader.close();
            }
        }
    }

    /**
     * Prioritize tests two ways and print results for both approaches.
     * Adapted from CsvBeanReader example in SuperCSV documentation.
     */
    private static void prioritizeSimplifyCSV() throws Exception {
        String pathToTargetFile = pathToReports + csvFolder + "sl.csv";

        ICsvBeanReader beanReader = null;
        try {
            beanReader = new CsvBeanReader(new FileReader(pathToTargetFile), CsvPreference.STANDARD_PREFERENCE);

            // the header elements are used to map the values to the bean (names must match)
            final String[] header = beanReader.getHeader(true);
            final CellProcessor[] processors = slProcessors.processor;

            SimplifyBean slBean;
            while((slBean = beanReader.read(SimplifyBean.class, header, processors)) != null ) {
                slBeans.add(slBean);
            }

            SimplifyCSV slCSV = new SimplifyCSV(slBeans);
            slCSV.printMostLinesFirst();
            slCSV.printOtherPrioritization();
        }
        finally {
            if( beanReader != null ) {
                beanReader.close();
            }
        }
    }

    /**
     * Prioritize tests two ways and print results for both approaches.
     * Adapted from CsvBeanReader example in SuperCSV documentation.
     */
    private static void prioritizeTelekCSV() throws Exception {
        String pathToTargetFile = pathToReports + csvFolder + "tm.csv";

        ICsvBeanReader beanReader = null;
        try {
            beanReader = new CsvBeanReader(new FileReader(pathToTargetFile), CsvPreference.STANDARD_PREFERENCE);

            // the header elements are used to map the values to the bean (names must match)
            final String[] header = beanReader.getHeader(true);
            final CellProcessor[] processors = tmProcessors.processor;

            TelekBean tmBean;
            while((tmBean = beanReader.read(TelekBean.class, header, processors)) != null ) {
                tmBeans.add(tmBean);
            }

            TelekCSV tmCSV = new TelekCSV(tmBeans);
            tmCSV.printMostLinesFirst();
            tmCSV.printOtherPrioritization();
        }
        finally {
            if( beanReader != null ) {
                beanReader.close();
            }
        }
    }
}

