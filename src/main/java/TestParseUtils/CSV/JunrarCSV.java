package TestParseUtils.CSV;

import TestParseUtils.JavaBeans.JunrarBean;
import TestParseUtils.Processors;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.FileWriter;
import java.util.*;

/**
 * A class for prioritizing Junrar tests and printing the results
 **/
public class JunrarCSV {
    public static Processors jProcessors = new Processors("j");
    private int index = 0;
    private String[] order = new String[12];
    private Map<String, JunrarBean> jBeanMap = new HashMap<>();
    private ArrayList<JunrarBean> jBeans;

    public JunrarCSV(ArrayList<JunrarBean> list) {
        this.jBeans = list;
        mapBeans();
    }

    public void mapBeans() {
        for (JunrarBean bean : jBeans) {
            jBeanMap.put(bean.getTest(), bean);
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

    public void sortBy(String sortKey, ArrayList<JunrarBean> list) {
        switch (sortKey) {
            case "lineCov":
                list.sort(Comparator.comparing(JunrarBean::getTotalCov).reversed());
                break;
            case "io":
                list.sort(Comparator.comparing(JunrarBean::getIo).reversed());
                break;
            case "volume":
                list.sort(Comparator.comparing(JunrarBean::getVolume).reversed());
                break;
            case "exception":
                list.sort(Comparator.comparing(JunrarBean::getException).reversed());
                break;
            case "baseClass":
                list.sort(Comparator.comparing(JunrarBean::getBaseClass).reversed());
                break;
            case "rarfile":
                list.sort(Comparator.comparing(JunrarBean::getRarfile).reversed());
                break;
            case "unpack":
                list.sort(Comparator.comparing(JunrarBean::getUnpack).reversed());
                break;
            case "ppm":
                list.sort(Comparator.comparing(JunrarBean::getPpm).reversed());
                break;
            case "vm":
                list.sort(Comparator.comparing(JunrarBean::getVm).reversed());
                break;
            case "decode":
                list.sort(Comparator.comparing(JunrarBean::getDecode).reversed());
                break;
            case "crypt":
                list.sort(Comparator.comparing(JunrarBean::getCrypt).reversed());
                break;
            case "crc":
                list.sort(Comparator.comparing(JunrarBean::getCrc).reversed());
                break;
            case "unsigned":
                list.sort(Comparator.comparing(JunrarBean::getUnsigned).reversed());
                break;
            default:
                break;
        }
    }

    public void printMostLinesFirst() throws Exception {
        sortBy("lineCov", jBeans);
        System.out.println("\nTests prioritized by most overall lines covered first:");
        printResults();
        writeResultsCSV("Junrar-MostLinesFirst.csv");
    }

    public void printOtherPrioritization() throws Exception {
        newPrioritization();
        System.out.println("\nTests prioritized by most lines covered in most untested project units first:");
        printResults();
        writeResultsCSV("Junrar-UncovProgUnitsFirst.csv");
    }

    private double calculateAFPD(int TFs, int N, int M) {
        return 1 - (((double)TFs / ((double)M * (double)N)) + 1/((double)N*2));
    }

    public void printResults() {
        int afpdTFs = 0;
        int afpdN = 0;
        int afpdM = jBeans.size();
        ArrayList<Integer> foundFailuresAtPositions = new ArrayList<>();
        int count = 1; // tracks test position
        int timeToFindFailure = 0;
        int timetoFindFailures = 0;
        boolean foundFirstFailure = false;

        System.out.format("\n%-10s%-8s%-6s%-30s", "Coverage", "Status", "Time", "Test");
        for (JunrarBean bean : jBeans) {
            // System.out.format("\n%-10s%-8s%-6s%-30s", bean.getTotalCov(), bean.getStatus(), bean.getTime(), bean.getTest());
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
        ArrayList<JunrarBean> tempList = new ArrayList<>();

        findOrder();

        int iterations = jBeans.size();
        for (int i = 0; i < iterations; i++) {
            sortBy(this.order[index], jBeans);
            JunrarBean tempBean = jBeans.get(0);
            tempList.add(tempBean);
            jBeans.remove(tempBean);
            circularUpdateIndex();
        }
        jBeans = tempList;
    }

    void circularUpdateIndex() {
        if (index == 11) index = 0;
        else index += 1;
    }

    void findOrder() {
        int counter = 0;
        Map<String, Integer> maxMap = new HashMap<>();
        maxMap.put("io", 0);// io volume exception baseClass rarfile unpack ppm vm decode crypt crc unsigned
        maxMap.put("volume", 0);
        maxMap.put("exception", 0);
        maxMap.put("baseClass", 0);
        maxMap.put("rarfile", 0);
        maxMap.put("unpack", 0);
        maxMap.put("ppm", 0);
        maxMap.put("vm", 0);
        maxMap.put("decode", 0);
        maxMap.put("crypt", 0);
        maxMap.put("crc", 0);
        maxMap.put("unsigned", 0);
        ArrayList<Integer> maximums = new ArrayList<>();
        for (JunrarBean bean : jBeans) {
            if (bean.getIo() > maxMap.get("io")) maxMap.replace("io", bean.getIo());
            if (bean.getVolume() > maxMap.get("volume")) maxMap.replace("volume", bean.getVolume());
            if (bean.getException() > maxMap.get("exception")) maxMap.replace("exception", bean.getException());
            if (bean.getBaseClass() > maxMap.get("baseClass")) maxMap.replace("baseClass", bean.getBaseClass());
            if (bean.getRarfile() > maxMap.get("rarfile")) maxMap.replace("rarfile", bean.getRarfile());
            if (bean.getUnpack() > maxMap.get("unpack")) maxMap.replace("unpack", bean.getUnpack());
            if (bean.getPpm() > maxMap.get("ppm")) maxMap.replace("ppm", bean.getPpm());
            if (bean.getVm() > maxMap.get("vm")) maxMap.replace("vm", bean.getVm());
            if (bean.getDecode() > maxMap.get("decode")) maxMap.replace("decode", bean.getDecode());
            if (bean.getCrypt() > maxMap.get("crypt")) maxMap.replace("crypt", bean.getCrypt());
            if (bean.getCrc() > maxMap.get("crc")) maxMap.replace("crc", bean.getCrc());
            if (bean.getUnsigned() > maxMap.get("unsigned")) maxMap.replace("unsigned", bean.getUnsigned());
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
            final String[] header = new String[] { "suite", "test", "totalCov", "io", "volume", "exception",
                    "baseClass", "rarfile", "unpack", "ppm", "vm", "decode", "crypt", "crc", "unsigned", "time", "status" };
            final CellProcessor[] processors = jProcessors.getProcessor();

            // write the header
            beanWriter.writeHeader(header);

            // write the beans
            for( final JunrarBean bean : jBeans ) {
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
