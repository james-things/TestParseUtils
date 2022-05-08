package TestParseUtils.CSV;

import TestParseUtils.JavaBeans.JsonBean;
import TestParseUtils.JavaBeans.JunrarBean;
import TestParseUtils.JavaBeans.SimplifyBean;
import TestParseUtils.Processors;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.FileWriter;
import java.util.*;

/**
 * A class for prioritizing JSON-java tests and printing the results
 **/
public class JsonCSV {
    public static Processors jjProcessors = new Processors("jj");
    private int index = 0;
    private String[] order = new String[18];
    private Map<String, JsonBean> jjBeanMap = new HashMap<>();
    private ArrayList<JsonBean> jjBeans;

    public JsonCSV(ArrayList<JsonBean> list) {
        this.jjBeans = list;
        mapBeans();
    }

    public void mapBeans() {
        for (JsonBean bean : jjBeans) {
            jjBeanMap.put(bean.getTest(), bean);
        }
    }

    public <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void sortBy(String sortKey, ArrayList<JsonBean> list) {
        switch (sortKey) {
            case "lineCov":
                list.sort(Comparator.comparing(JsonBean::getTotalCov).reversed());
                break;
            case "cdl":
                list.sort(Comparator.comparing(JsonBean::getCdl).reversed());
                break;
            case "cookie":
                list.sort(Comparator.comparing(JsonBean::getCookie).reversed());
                break;
            case "cookieList":
                list.sort(Comparator.comparing(JsonBean::getCookieList).reversed());
                break;
            case "http":
                list.sort(Comparator.comparing(JsonBean::getHttp).reversed());
                break;
            case "httpTokener":
                list.sort(Comparator.comparing(JsonBean::getHttpTokener).reversed());
                break;
            case "jsonArray":
                list.sort(Comparator.comparing(JsonBean::getJsonArray).reversed());
                break;
            case "jsonException":
                list.sort(Comparator.comparing(JsonBean::getJsonException).reversed());
                break;
            case "jsonMl":
                list.sort(Comparator.comparing(JsonBean::getJsonMl).reversed());
                break;
            case "jsonObject":
                list.sort(Comparator.comparing(JsonBean::getJsonObject).reversed());
                break;
            case "jsonPointer":
                list.sort(Comparator.comparing(JsonBean::getJsonPointer).reversed());
                break;
            case "jsonPointerException":
                list.sort(Comparator.comparing(JsonBean::getJsonPointerException).reversed());
                break;
            case "jsonStringer":
                list.sort(Comparator.comparing(JsonBean::getJsonStringer).reversed());
                break;
            case "jsonTokener":
                list.sort(Comparator.comparing(JsonBean::getJsonTokener).reversed());
                break;
            case "jsonWriter":
                list.sort(Comparator.comparing(JsonBean::getJsonWriter).reversed());
                break;
            case "property":
                list.sort(Comparator.comparing(JsonBean::getProperty).reversed());
                break;
            case "xml":
                list.sort(Comparator.comparing(JsonBean::getXml).reversed());
                break;
            case "xmlParserConfiguration":
                list.sort(Comparator.comparing(JsonBean::getXmlParserConfiguration).reversed());
                break;
            case "xmlTokener":
                list.sort(Comparator.comparing(JsonBean::getXmlTokener).reversed());
                break;
            default:
                break;
        }
    }

    public void printMostLinesFirst() throws Exception {
        sortBy("lineCov", jjBeans);
        System.out.println("\nTests prioritized by most overall lines covered first:");
        printResults();
        writeResultsCSV("JSON-java-MostLinesFirst.csv");
    }

    public void printOtherPrioritization() throws Exception {
        newPrioritization();
        System.out.println("\nTests prioritized by most lines covered in most untested project units first:");
        printResults();
        writeResultsCSV("JSON-java-UncovProgUnitsFirst.csv");
    }

    private double calculateAFPD(int TFs, int N, int M) {
        return 1 - (((double)TFs / ((double)M * (double)N)) + 1/((double)N*2));
    }

    public void printResults() {
        int afpdTFs = 0;
        int afpdN = 0;
        int afpdM = jjBeans.size();
        ArrayList<Integer> foundFailuresAtPositions = new ArrayList<>();
        int count = 1; // tracks test position
        int timeToFindFailure = 0;
        boolean foundFirstFailure = false;

        System.out.format("\n%-10s%-8s%-6s%-30s", "Coverage", "Status", "Time", "Test");
        for (JsonBean bean : jjBeans) {
            System.out.format("\n%-10s%-8s%-6s%-30s", bean.getTotalCov(), bean.getStatus(), bean.getTime(), bean.getTest());
            // Collect some stats
            if (!foundFirstFailure) timeToFindFailure += bean.getTime(); // first failure
            if (!foundFirstFailure && (Objects.equals(bean.getStatus(), "error") || Objects.equals(bean.getStatus(),"failed"))) {
                foundFirstFailure = true;}
            // track afpd-relevant and other details
            if (Objects.equals(bean.getStatus(), "error") || Objects.equals(bean.getStatus(),"failed")) {
                afpdTFs += count;
                afpdN += 1;
                foundFailuresAtPositions.add(count);
            }
            count++; //increment count
        }
        double AFPD = calculateAFPD(afpdTFs, afpdN, afpdM);
        System.out.print("\n\nFound test failures at positions: ");
        for (int testPos : foundFailuresAtPositions) System.out.print(testPos + ", ");
        System.out.println("\n\nCalculated AFPD: " + AFPD);
        System.out.println("AFPD = ((TF1, TF2, ... TFn) / nm) + (1 / 2n)");
    }


    void newPrioritization() {
        ArrayList<JsonBean> tempList = new ArrayList<>();

        findOrder();

        int iterations = jjBeans.size();
        for (int i = 0; i < iterations; i++) {
            sortBy(this.order[index], jjBeans);
            JsonBean tempBean = jjBeans.get(0);
            tempList.add(tempBean);
            jjBeans.remove(tempBean);
            circularUpdateIndex();
        }
        jjBeans = tempList;
    }

    void circularUpdateIndex() {
        if (index == 17) index = 0;
        else index += 1;
    }

    void findOrder() {
        int counter = 0;
        Map<String, Integer> maxMap = new HashMap<>();
        maxMap.put("cdl", 0);
        maxMap.put("cookie", 0);
        maxMap.put("cookieList", 0);
        maxMap.put("http", 0);
        maxMap.put("httpTokener", 0);
        maxMap.put("jsonArray", 0);
        maxMap.put("jsonException", 0);
        maxMap.put("jsonMl", 0);
        maxMap.put("jsonObject", 0);
        maxMap.put("jsonPointer", 0);
        maxMap.put("jsonPointerException", 0);
        maxMap.put("jsonStringer", 0);
        maxMap.put("jsonTokener", 0);
        maxMap.put("jsonWriter", 0);
        maxMap.put("property", 0);
        maxMap.put("xml", 0);
        maxMap.put("xmlParserConfiguration", 0);
        maxMap.put("xmlTokener", 0);
        ArrayList<Integer> maximums = new ArrayList<>();
        for (JsonBean bean : jjBeans) {
            if (bean.getCdl() > maxMap.get("cdl")) maxMap.replace("cdl", bean.getCdl());
            if (bean.getCookie() > maxMap.get("cookie")) maxMap.replace("cookie", bean.getCookie());
            if (bean.getCookieList() > maxMap.get("cookieList")) maxMap.replace("cookieList", bean.getCookieList());
            if (bean.getHttp() > maxMap.get("http")) maxMap.replace("http", bean.getHttp());
            if (bean.getHttpTokener() > maxMap.get("httpTokener")) maxMap.replace("httpTokener", bean.getHttpTokener());
            if (bean.getJsonArray() > maxMap.get("jsonArray")) maxMap.replace("jsonArray", bean.getJsonArray());
            if (bean.getJsonException() > maxMap.get("jsonException")) maxMap.replace("jsonException", bean.getJsonException());
            if (bean.getJsonMl() > maxMap.get("jsonMl")) maxMap.replace("jsonMl", bean.getJsonMl());
            if (bean.getJsonObject() > maxMap.get("jsonObject")) maxMap.replace("jsonObject", bean.getJsonObject());
            if (bean.getJsonPointer() > maxMap.get("jsonPointer")) maxMap.replace("jsonPointer", bean.getJsonPointer());
            if (bean.getJsonPointerException() > maxMap.get("jsonPointerException")) maxMap.replace("jsonPointerException", bean.getJsonPointerException());
            if (bean.getJsonStringer() > maxMap.get("jsonStringer")) maxMap.replace("jsonStringer", bean.getJsonStringer());
            if (bean.getJsonTokener() > maxMap.get("jsonTokener")) maxMap.replace("jsonTokener", bean.getJsonTokener());
            if (bean.getJsonWriter() > maxMap.get("jsonWriter")) maxMap.replace("jsonWriter", bean.getJsonWriter());
            if (bean.getProperty() > maxMap.get("property")) maxMap.replace("property", bean.getProperty());
            if (bean.getXml() > maxMap.get("xml")) maxMap.replace("xml", bean.getXml());
            if (bean.getXmlParserConfiguration() > maxMap.get("xmlParserConfiguration")) maxMap.replace("xmlParserConfiguration", bean.getXmlParserConfiguration());
            if (bean.getXmlTokener() > maxMap.get("xmlTokener")) maxMap.replace("xmlTokener", bean.getXmlTokener());
        }

        for (Map.Entry<String, Integer> entry : maxMap.entrySet()) maximums.add(entry.getValue());

        Collections.sort(maximums);
        Collections.reverse(maximums);

        for (int i : maximums) {
            this.order[counter] = getKey(maxMap, i);
            counter++;
        }
    }

    /**
     * Write test results to CSV.
     */
    private void writeResultsCSV(String fileName) throws Exception {
        ICsvBeanWriter beanWriter = null;
        try {
            beanWriter = new CsvBeanWriter(new FileWriter("src/main/resources/TestParseUtils-Output/" + fileName),
                    CsvPreference.STANDARD_PREFERENCE);

            // the header elements are used to map the bean values to each column (names must match)
            final String[] header = new String[] { "suite", "test", "totalCov", "cdl", "cookie", "cookieList", "http",
                    "httpTokener", "jsonArray", "jsonException", "jsonMl", "jsonObject", "jsonPointer", "jsonPointerException",
                    "jsonStringer", "jsonTokener", "jsonWriter", "property", "xml", "xmlParserConfiguration", "xmlTokener",
                    "time", "status" };
            final CellProcessor[] processors = jjProcessors.getProcessor();

            // write the header
            beanWriter.writeHeader(header);

            // write the beans
            for( final JsonBean bean : jjBeans ) {
                beanWriter.write(bean, header, processors);
            }

            System.out.println("New CSV written to project src/main/resources/TestParseUtils-Output/ directory.");
        }
        finally {
            if( beanWriter != null ) {
                beanWriter.close();
            }
        }
    }
}
