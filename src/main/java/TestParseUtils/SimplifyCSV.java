package TestParseUtils;

import java.util.*;

public class SimplifyCSV {
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

    public void printMostLinesFirst() {
        sortBy("lineCov", slBeans);
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
        for (SimplifyBean bean : slBeans) {
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
}
