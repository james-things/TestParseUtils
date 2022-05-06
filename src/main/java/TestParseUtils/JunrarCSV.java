package TestParseUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class JunrarCSV {
    private List<JunrarBean> jBeans = new ArrayList<>();

    public JunrarCSV(List <JunrarBean> list) {
        this.jBeans = list;
    }

    public void sortByLinesCovered() {
        jBeans.sort(Comparator.comparing(JunrarBean::getTotalCov).reversed());
    }

    public void printMostLinesFirst() {
        int foundErrorAtPosition = -1;
        int count = 1;
        int timeToFindError = 0;
        boolean foundError = false;
        System.out.println("\nTests ordered by most lines covered first\n");
        sortByLinesCovered();
        System.out.println("Coverage\t\tStatus\t\tTime\t\tTest");
        for (JunrarBean jBean : jBeans) {
            System.out.println(String.format("%s\t%s\t\t%s\t\t%s",
                    jBean.getTotalCov(), jBean.getStatus(), jBean.getTime(),jBean.getTest()));
            // add to time to find error as long as an error hasn't been found
            if (!foundError) timeToFindError += jBean.getTime();
            // if we've found an error, store count
            if (!foundError && Objects.equals(jBean.getStatus(), "error")) {
                foundError = true;
                foundErrorAtPosition = count;
            }
            count++; //increment count
        }
        System.out.println("\nFound error at position: " + foundErrorAtPosition);
        System.out.println("Time to find error: " + timeToFindError + "ms");
    }
}
