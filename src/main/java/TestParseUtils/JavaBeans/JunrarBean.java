package TestParseUtils.JavaBeans;

/**
 * A javabean designed to store rows of Junrar test results
 **/
public class JunrarBean implements java.io.Serializable{
    private String suite;
    private String test;
    private int totalCov;
    private int io;
    private int volume;
    private int exception;
    private int baseClass;
    private int rarfile;
    private int unpack;
    private int ppm;
    private int vm;
    private int decode;
    private int crypt;
    private int crc;
    private int unsigned;
    private int time;
    private String status;

    public JunrarBean(){}

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

    public int getIo() {
        return io;
    }

    public void setIo(int io) {
        this.io = io;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getException() {
        return exception;
    }

    public void setException(int exception) {
        this.exception = exception;
    }

    public int getBaseClass() {
        return baseClass;
    }

    public void setBaseClass(int baseClass) {
        this.baseClass = baseClass;
    }

    public int getRarfile() {
        return rarfile;
    }

    public void setRarfile(int rarfile) {
        this.rarfile = rarfile;
    }

    public int getUnpack() {
        return unpack;
    }

    public void setUnpack(int unpack) {
        this.unpack = unpack;
    }

    public int getPpm() {
        return ppm;
    }

    public void setPpm(int ppm) {
        this.ppm = ppm;
    }

    public int getVm() {
        return vm;
    }

    public void setVm(int vm) {
        this.vm = vm;
    }

    public int getDecode() {
        return decode;
    }

    public void setDecode(int decode) {
        this.decode = decode;
    }

    public int getCrypt() {
        return crypt;
    }

    public void setCrypt(int crypt) {
        this.crypt = crypt;
    }

    public int getCrc() {
        return crc;
    }

    public void setCrc(int crc) {
        this.crc = crc;
    }

    public int getUnsigned() {
        return unsigned;
    }

    public void setUnsigned(int unsigned) {
        this.unsigned = unsigned;
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
}
