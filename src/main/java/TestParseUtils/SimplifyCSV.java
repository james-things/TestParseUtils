package TestParseUtils;

import java.util.*;

public class SimplifyCSV {
    private Map<String, SimplifyBean> slBeanMap = new HashMap<String, SimplifyBean>();
    private List<SimplifyBean> slBeans;

    public SimplifyCSV(List <SimplifyBean> list) {
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

    public void sortByLinesCovered() {
        slBeans.sort(Comparator.comparing(SimplifyBean::getTotalCov).reversed());
    }

    public void printMostLinesFirst() {
        int foundErrorAtPosition = -1;
        int count = 1;
        int timeToFindError = 0;
        boolean foundError = false;
        System.out.println("\nTests ordered by most lines covered first\n");
        sortByLinesCovered();
        System.out.println("Coverage\t\tStatus\t\tTime\t\tTest");
        for (SimplifyBean bean : slBeans) {
            System.out.println(String.format("%s\t%s\t\t%s\t\t%s",
                    bean.getTotalCov(), bean.getStatus(), bean.getTime(),bean.getTest()));
            // add to time to find error as long as an error hasn't been found
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

    public void printMostLinesPerClass() {

    }

    void findOrder() {
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
        }
    }
}
