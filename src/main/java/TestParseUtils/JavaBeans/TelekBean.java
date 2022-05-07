package TestParseUtils.JavaBeans;

/**
 * A bean designed to store rows of TelekMath test results
 **/
public class TelekBean implements java.io.Serializable{
    private String suite;
    private String test;
    private int totalCov;
    private int plain;
    private int containers;
    private int colors;
    private int arrayref;
    private int utils;
    private int special;
    private int core;
    private int advanced;
    private int time;
    private String status;

    public TelekBean(){}

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

    public int getPlain() {
        return plain;
    }

    public void setPlain(int plain) {
        this.plain = plain;
    }

    public int getContainers() {
        return containers;
    }

    public void setContainers(int containers) {
        this.containers = containers;
    }

    public int getColors() {
        return colors;
    }

    public void setColors(int colors) {
        this.colors = colors;
    }

    public int getArrayref() {
        return arrayref;
    }

    public void setArrayref(int arrayref) {
        this.arrayref = arrayref;
    }

    public int getUtils() {
        return utils;
    }

    public void setUtils(int utils) {
        this.utils = utils;
    }

    public int getSpecial() {
        return special;
    }

    public void setSpecial(int special) {
        this.special = special;
    }

    public int getAdvanced() {
        return advanced;
    }

    public void setAdvanced(int advanced) {
        this.advanced = advanced;
    }
}
