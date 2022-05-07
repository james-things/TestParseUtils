package TestParseUtils.CSV;

import TestParseUtils.JavaBeans.TelekBean;

import java.util.*;

/**
 * A class for prioritizing TelekMath tests and printing the results
 **/
public class TelekCSV {
    private int index = 0;
    private String[] order = new String[8];
    private Map<String, TelekBean> tmBeanMap = new HashMap<>();
    private ArrayList<TelekBean> tmBeans;

    public TelekCSV(ArrayList<TelekBean> list) {
        this.tmBeans = list;
        mapBeans();
    }

    public void mapBeans() {
        for (TelekBean bean : tmBeans) {
            tmBeanMap.put(bean.getTest(), bean);
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

    public void sortBy(String sortKey, ArrayList<TelekBean> list) {
        switch (sortKey) {
            case "lineCov":
                list.sort(Comparator.comparing(TelekBean::getTotalCov).reversed());
                break;
            case "plain":
                list.sort(Comparator.comparing(TelekBean::getPlain).reversed());
                break;
            case "containers":
                list.sort(Comparator.comparing(TelekBean::getContainers).reversed());
                break;
            case "colors":
                list.sort(Comparator.comparing(TelekBean::getColors).reversed());
                break;
            case "arrayref":
                list.sort(Comparator.comparing(TelekBean::getArrayref).reversed());
                break;
            case "utils":
                list.sort(Comparator.comparing(TelekBean::getUtils).reversed());
                break;
            case "special":
                list.sort(Comparator.comparing(TelekBean::getSpecial).reversed());
                break;
            case "core":
                list.sort(Comparator.comparing(TelekBean::getCore).reversed());
                break;
            case "advanced":
                list.sort(Comparator.comparing(TelekBean::getAdvanced).reversed());
                break;
            default:
                break;
        }
    }

    public void printMostLinesFirst() {
        sortBy("lineCov", tmBeans);
        System.out.println("\nTests prioritized by most overall lines covered first:");
        printResults();
    }

    public void printOtherPrioritization() {
        newPrioritization();
        System.out.println("\nTests prioritized by most lines covered in most untested project units first:");
        printResults();
    }

    public void printResults() {
        int foundErrorAtPosition = -1;
        int count = 1;
        int timeToFindError = 0;
        boolean foundError = false;

        System.out.format("\n%-10s%-8s%-6s%-30s", "Coverage", "Status", "Time", "Test");
        for (TelekBean bean : tmBeans) {
            System.out.format("\n%-10s%-8s%-6s%-30s", bean.getTotalCov(), bean.getStatus(), bean.getTime(), bean.getTest());
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
        ArrayList<TelekBean> tempList = new ArrayList<>();

        findOrder();

        int iterations = tmBeans.size();
        for (int i = 0; i < iterations; i++) {
            sortBy(this.order[index], tmBeans);
            TelekBean tempBean = tmBeans.get(0);
            tempList.add(tempBean);
            tmBeans.remove(tempBean);
            circularUpdateIndex();
        }
        tmBeans = tempList;
    }

    void circularUpdateIndex() {
        if (index == 7) index = 0;
        else index += 1;
    }

    void findOrder() {
        int counter = 0;
        Map<String, Integer> maxMap = new HashMap<>();
        maxMap.put("plain", 0);
        maxMap.put("containers", 0);
        maxMap.put("colors", 0);
        maxMap.put("arrayref", 0);
        maxMap.put("utils", 0);
        maxMap.put("special", 0);
        maxMap.put("core", 0);
        maxMap.put("advanced", 0);
        ArrayList<Integer> maximums = new ArrayList<>();
        for (TelekBean bean : tmBeans) {
            if (bean.getPlain() > maxMap.get("plain")) maxMap.replace("plain", bean.getPlain());
            if (bean.getContainers() > maxMap.get("containers")) maxMap.replace("containers", bean.getContainers());
            if (bean.getColors() > maxMap.get("colors")) maxMap.replace("colors", bean.getColors());
            if (bean.getArrayref() > maxMap.get("arrayref")) maxMap.replace("arrayref", bean.getArrayref());
            if (bean.getUtils() > maxMap.get("utils")) maxMap.replace("utils", bean.getUtils());
            if (bean.getSpecial() > maxMap.get("special")) maxMap.replace("special", bean.getSpecial());
            if (bean.getCore() > maxMap.get("core")) maxMap.replace("core", bean.getCore());
            if (bean.getAdvanced() > maxMap.get("advanced")) maxMap.replace("advanced", bean.getAdvanced());
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
