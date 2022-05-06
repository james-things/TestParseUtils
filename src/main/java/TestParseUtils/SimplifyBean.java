package TestParseUtils;

public class SimplifyBean implements java.io.Serializable{
    //	core	search	shuffle	sort
    private String suite;
    private String test;
    private int totalCov;
    private int core;
    private int search;
    private int shuffle;
    private int sort;
    private int time;
    private String status;

    public SimplifyBean(){}

    public String getSuite() {
        return suite;
    }

    public void setSuite(String suite) {
        this.suite = suite;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public int getTotalCov() {
        return totalCov;
    }

    public void setTotalCov(int totalCov) {
        this.totalCov = totalCov;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCore() {
        return core;
    }

    public void setCore(int core) {
        this.core = core;
    }

    public int getSearch() {
        return search;
    }

    public void setSearch(int search) {
        this.search = search;
    }

    public int getShuffle() {
        return shuffle;
    }

    public void setShuffle(int shuffle) {
        this.shuffle = shuffle;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
