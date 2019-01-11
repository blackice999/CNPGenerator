package generator;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class CNPBuilder {
    private static final String ZERO_FILL_TWO_DECIMALS = "%02d";
    private static final String ZERO_FILL_THREE_DECIMALS = "%03d";

    static final int NUMBER_OF_REGISTRIES = 999;
    static final int NUMBER_OF_MONTHS = 12;
    private static final int NUMBER_OF_COUNTIES = 52;
    private static final int NUMBER_OF_GENDERS = 2;

    private static final int DAYS_IN_FEBRUARY = 28;
    private static final int THIRTY_DAYS_MONTH = 30;
    static final int THIRTY_ONE_DAYS_MONTH = 31;

    private static final int ID_MASCULINE = 0;

    private static final int BETWEEN_TWENTIETH_CENTURY_CODE = 1;
    private static final int BETWEEN_NINETIETH_CENTURY_CODE = 3;
    private static final int BETWEEN_TWENTY_FIRST_CENTURY_CODE = 5;

    static final int MIN_YEAR = 1800;
    static final int MAX_YEAR = 2099;

    private static final int TWENTIETH_CENTURY_YEAR_START = 1900;
    private static final int NINETIETH_CENTURY_YEAR_END = 1899;
    private static final int TWENTY_FIRST_CENTURY_YEAR_START = 2000;
    private static final int TWENTIETH_CENTURY_YEAR_END = 1999;

    private static final int CONTROL_SUM_DIVIDER = 11;
    private static int[] CONTROL_VALUES = new int[]{2, 7, 9, 1, 4, 6, 3, 5, 8, 2, 7, 9};

    private int year;
    private int month;
    private int day;
    private int sex;
    private int county;
    private int registryNumber;
    private int controlSum;

    private List<Integer> thirtyDaysMonth;
    private List<Integer> thirtyOneDaysMonth;
    private Random random;

    public CNPBuilder() {
        random = new Random();
        thirtyDaysMonth = Arrays.asList(Calendar.JANUARY, Calendar.MARCH, Calendar.MAY, Calendar.JULY, Calendar.AUGUST, Calendar.OCTOBER);
        thirtyOneDaysMonth = Arrays.asList(Calendar.APRIL, Calendar.JUNE, Calendar.SEPTEMBER, Calendar.NOVEMBER);

        year = createYear();
        month = createMonth();
        day = createDay(month);
        sex = createSex(year);
        county = createCounty();
        registryNumber = createRegistryNumber();

        String yearIdentifier = String.format(ZERO_FILL_TWO_DECIMALS, year % 100);

        final String cnpWithoutControlSum = sex +
                yearIdentifier +
                String.format(ZERO_FILL_TWO_DECIMALS, month) +
                String.format(ZERO_FILL_TWO_DECIMALS, day) +
                county +
                String.format(ZERO_FILL_THREE_DECIMALS, registryNumber);

        controlSum = getControlSum(cnpWithoutControlSum);
    }

    public CNPBuilder setSex(int sex) {
        this.sex = sex;
        return this;
    }

    public CNPBuilder setYear(int year) {
        this.year = year;
        return this;
    }

    public CNPBuilder setMonth(int month) {
        this.month = month;
        return this;
    }

    public CNPBuilder setDay(int day) {
        this.day = day;
        return this;
    }

    public CNPBuilder setCounty(int county) {
        this.county = county;
        return this;
    }

    public CNPBuilder setRegistryNumber(int registryNumber) {
        this.registryNumber = registryNumber;
        return this;
    }

    public CNP createCNP() {
        String yearIdentifier = String.format(ZERO_FILL_TWO_DECIMALS, year % 100);
        final String cnpWithoutControlSum = sex +
                yearIdentifier +
                String.format(ZERO_FILL_TWO_DECIMALS, month) +
                String.format(ZERO_FILL_TWO_DECIMALS, day) +
                county +
                String.format(ZERO_FILL_THREE_DECIMALS, registryNumber);

        controlSum = getControlSum(cnpWithoutControlSum);

        return new CNP(String.valueOf(sex),
                String.valueOf(yearIdentifier),
                String.format(ZERO_FILL_TWO_DECIMALS, month),
                String.format(ZERO_FILL_TWO_DECIMALS, day),
                String.format(ZERO_FILL_TWO_DECIMALS, county),
                String.format(ZERO_FILL_THREE_DECIMALS, registryNumber),
                String.valueOf(controlSum));
    }

    private int createRegistryNumber() {
        return random.nextInt(NUMBER_OF_REGISTRIES) + 1;
    }

    private int createCounty() {
        return random.nextInt(NUMBER_OF_COUNTIES) + 1;
    }

    private int createDay(int month) {
        if (thirtyDaysMonth.contains(month)) {
            return random.nextInt(THIRTY_DAYS_MONTH) + 1;
        } else if (thirtyOneDaysMonth.contains(month)) {
            return random.nextInt(THIRTY_ONE_DAYS_MONTH) + 1;
        } else {
            return random.nextInt(DAYS_IN_FEBRUARY) + 1;
        }
    }

    private int createMonth() {
        return random.nextInt(NUMBER_OF_MONTHS) + 1;
    }

    private int createYear() {
        return random.nextInt((MAX_YEAR - MIN_YEAR) + 1) + MIN_YEAR;
    }

    private int createSex(int birthYear) {
        int randomSex = random.nextInt(NUMBER_OF_GENDERS);
        int dateCode = getCenturyCodeByYear(birthYear);
        return randomSex == ID_MASCULINE ? dateCode : dateCode + 1;
    }

    private int getCenturyCodeByYear(int birthYear) {
        if (isDateBetweenTwentiethCentury(birthYear)) {
            return BETWEEN_TWENTIETH_CENTURY_CODE;
        }

        if (isDateBetweenNinetiethCentury(birthYear)) {
            return BETWEEN_NINETIETH_CENTURY_CODE;
        }

        if (isDateBetweenTwentyFirstCentury(birthYear)) {
            return BETWEEN_TWENTY_FIRST_CENTURY_CODE;
        }

        return BETWEEN_TWENTIETH_CENTURY_CODE;
    }

    private boolean isDateBetweenTwentyFirstCentury(int birthYear) {
        return birthYear >= TWENTY_FIRST_CENTURY_YEAR_START && birthYear <= MAX_YEAR;
    }

    private boolean isDateBetweenNinetiethCentury(int birthYear) {
        return birthYear >= MIN_YEAR && birthYear <= NINETIETH_CENTURY_YEAR_END;
    }

    private boolean isDateBetweenTwentiethCentury(int birthYear) {
        return birthYear >= TWENTIETH_CENTURY_YEAR_START && birthYear <= TWENTIETH_CENTURY_YEAR_END;
    }

    private int getControlSum(String cnp) {
        int controlSum = IntStream.range(0, cnp.length()).map(i -> CONTROL_VALUES[i] * Integer.parseInt(String.valueOf(cnp.charAt(i)))).sum();
        controlSum %= CONTROL_SUM_DIVIDER;
        return isDoubleDigit(controlSum) ? 1 : controlSum;
    }

    private boolean isDoubleDigit(int number) {
        return number == 10;
    }
}
