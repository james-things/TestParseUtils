package TestParseUtils;

import java.util.*;

public class SimplifyCSV {
    private int index = 0;
    private String[] order = new String[4];
    private Map<String, SimplifyBean> slBeanMap = new HashMap<String, SimplifyBean>();
    private ArrayList<SimplifyBean> slBeans = new ArrayList<>();

    public SimplifyCSV(ArrayList <SimplifyBean> list) {
        this.slBeans = list;
        sortByLinesCovered(slBeans);
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

    public void sortByLinesCovered(ArrayList<SimplifyBean> list) {
        list.sort(Comparator.comparing(SimplifyBean::getTotalCov).reversed());
    }
    public void sortByCore(ArrayList<SimplifyBean> list) {
        list.sort(Comparator.comparing(SimplifyBean::getCore).reversed());
    }
    public void sortBySearch(ArrayList<SimplifyBean> list) {
        list.sort(Comparator.comparing(SimplifyBean::getSearch).reversed());
    }
    public void sortByShuffle(ArrayList<SimplifyBean> list) {
        list.sort(Comparator.comparing(SimplifyBean::getShuffle).reversed());
    }
    public void sortBySort(ArrayList<SimplifyBean> list) {
        list.sort(Comparator.comparing(SimplifyBean::getSort).reversed());
    }

    public void printMostLinesFirst() {
        printResults(slBeans);
    }

    public void printOtherPrioritization() {
        newPrioritization();
    }

    public void printResults(ArrayList<SimplifyBean> beans) {
        int foundErrorAtPosition = -1;
        int count = 1;
        int timeToFindError = 0;
        boolean foundError = false;
        System.out.println("Coverage\t\tStatus\t\tTime\t\tTest");
        for (SimplifyBean bean : beans) {
            System.out.println(String.format("%s\t%s\t\t%s\t\t%s",
                    bean.getTotalCov(), bean.getStatus(), bean.getTime(),bean.getTest()));
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

    void circularUpdateIndex() {
        if (index == 3) index = 0;
        else index += 1;
    }

    void newPrioritization() {
        findOrder();

        ArrayList<SimplifyBean> tempList = new ArrayList<>();
        int iterations = slBeans.size();
        for (int i = 0; i < iterations; i++) {
            switch(this.order[index]) {
                case "core":
                    sortByCore(slBeans);
                    break;
                case "search":
                    sortBySearch(slBeans);
                    break;
                case "shuffle":
                    sortByShuffle(slBeans);
                    break;
                case "sort":
                    sortBySort(slBeans);
                    break;
                default:
                    break;
            }
            SimplifyBean tempBean = slBeans.get(0);
            tempList.add(tempBean);
            slBeans.remove(tempBean);
            circularUpdateIndex();
        }
        System.out.print("Beans processed: " + tempList.size());
        printResults(tempList);
    }

    void findOrder() {
        String[] order = new String[4];
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
            System.out.println(getKey(maxMap, i) + " " + i);
            this.order[counter] = getKey(maxMap, i);
            counter++;
        }

        for (int i = 0; i < 4; i++) {
            System.out.println(this.order[i]);
        }
    }
}
