package TestParseUtils.CSV;

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
 * A class for prioritizing SimplifyLearning tests and printing the results
 **/
public class SimplifyCSV {
    public static Processors slProcessors = new Processors("sl");
    private int index = 0;
    private String[] order = new String[4];
    private Map<String, SimplifyBean> slBeanMap = new HashMap<>();
    private ArrayList<SimplifyBean> slBeans;

    public SimplifyCSV(ArrayList<SimplifyBean> list) {
        this.slBeans = list;
        mapBeans();
    }

    public void mapBeans() {
        for (SimplifyBean bean : slBeans) {
            slBeanMap.put(bean.getTest(), bean);
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

    public void sortBy(String sortKey, ArrayList<SimplifyBean> list) {
        switch (sortKey) {
            case "lineCov":
                list.sort(Comparator.comparing(SimplifyBean::getTotalCov).reversed());
                break;
            case "core":
                list.sort(Comparator.comparing(SimplifyBean::getCore).reversed());
                break;
            case "search":
                list.sort(Comparator.comparing(SimplifyBean::getSearch).reversed());
                break;
            case "shuffle":
                list.sort(Comparator.comparing(SimplifyBean::getShuffle).reversed());
                break;
            case "sort":
                list.sort(Comparator.comparing(SimplifyBean::getSort).reversed());
                break;
            default:
                break;
        }
    }

    public void printMostLinesFirst() throws Exception {
        sortBy("lineCov", slBeans);
        System.out.println("\nTests prioritized by most overall lines covered first:");
        printResults();
        writeResultsCSV("SimplifyLearning-MostLinesFirst.csv");
    }

    public void printOtherPrioritization() throws Exception {
        newPrioritization();
        System.out.println("\nTests prioritized by most lines covered in most untested project units first:");
        printResults();
        writeResultsCSV("SimplifyLearning-UncovProgUnitsFirst.csv");
    }

    private double calculateAFPD(int TFs, int N, int M) {
        return (((double)TFs / ((double)M * (double)N)) + (1 / ((double)N*2)));
    }

    public void printResults() {
        int afpdTFs = 0;
        int afpdN = 0;
        int afpdM = slBeans.size();
        ArrayList<Integer> foundFailuresAtPositions = new ArrayList<>();
        int count = 1; // tracks test position
        int timeToFindFailure = 0;
        int timetoFindFailures = 0;
        boolean foundFirstFailure = false;

        System.out.format("\n%-10s%-8s%-6s%-30s", "Coverage", "Status", "Time", "Test");
        for (SimplifyBean bean : slBeans) {
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
        ArrayList<SimplifyBean> tempList = new ArrayList<>();

        findOrder();

        int iterations = slBeans.size();
        for (int i = 0; i < iterations; i++) {
            sortBy(this.order[index], slBeans);
            SimplifyBean tempBean = slBeans.get(0);
            tempList.add(tempBean);
            slBeans.remove(tempBean);
            circularUpdateIndex();
        }
        slBeans = tempList;
    }

    void circularUpdateIndex() {
        if (index == 3) index = 0;
        else index += 1;
    }

    void findOrder() {
        int counter = 0;
        Map<String, Integer> maxMap = new HashMap<>();
        maxMap.put("core", 0);
        maxMap.put("search", 0);
        maxMap.put("shuffle", 0);
        maxMap.put("sort", 0);
        ArrayList<Integer> maximums = new ArrayList<>();
        for (SimplifyBean bean : slBeans) {
            if (bean.getCore() > maxMap.get("core")) maxMap.replace("core", bean.getCore());
            if (bean.getSearch() > maxMap.get("search")) maxMap.replace("search", bean.getSearch());
            if (bean.getShuffle() > maxMap.get("shuffle")) maxMap.replace("shuffle", bean.getShuffle());
            if (bean.getSort() > maxMap.get("sort")) maxMap.replace("sort", bean.getSort());
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
            final String[] header = new String[] { "suite", "test", "totalCov", "core", "search", "shuffle",
                    "sort", "time", "status" };
            final CellProcessor[] processors = slProcessors.getProcessor();

            // write the header
            beanWriter.writeHeader(header);

            // write the beans
            for( final SimplifyBean bean : slBeans ) {
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
