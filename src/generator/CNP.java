package generator;

public class CNP {
    private String sex;
    private String year;
    private String month;
    private String day;
    private String county;
    private String registryNumber;
    private String controlSum;

    public CNP(String sex, String year, String month, String day, String county, String registryNumber, String controlSum) {
        this.sex = sex;
        this.year = year;
        this.month = month;
        this.day = day;
        this.county = county;
        this.registryNumber = registryNumber;
        this.controlSum = controlSum;
    }

    public String getSex() {
        return sex;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public String getCounty() {
        return county;
    }

    public String getRegistryNumber() {
        return registryNumber;
    }

    public String getControlSum() {
        return controlSum;
    }

    @Override
    public String toString() {
        return "" + sex + year + month + day + county + registryNumber + controlSum;
    }
}
