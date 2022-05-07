package TestParseUtils;

import java.util.*;

/**
 * A class for prioritizing Junrar tests and printing the results
 **/
public class JunrarCSV {
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

    public void printMostLinesFirst() {
        sortBy("lineCov", jBeans);
        printResults();
    }

    public void printOtherPrioritization() {
        newPrioritization();
        printResults();
    }

    public void printResults() {
        int foundErrorAtPosition = -1;
        int count = 1;
        int timeToFindError = 0;
        boolean foundError = false;

        System.out.printf("%n%s %s %s %s", "Coverage", "Status", "Time", "Test");
        for (JunrarBean bean : jBeans) {
            System.out.printf("%n%s %s %s %s", bean.getTotalCov(), bean.getStatus(), bean.getTime(), bean.getTest());
            if (!foundError) timeToFindError += bean.getTime();
            // if we've found an error, store count
            if (!foundError && Objects.equals(bean.getStatus(), "error")) {
                foundError = true;
                foundErrorAtPosition = count;
            }
            count++; //increment count
        }
        System.out.println("\nFound error at position: " + foundErrorAtPosition);
        System.out.println("Time to find error: " + timeToFindError + "ms");
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
}
