package generator;

import java.util.*;
import java.util.stream.IntStream;

public class CNPGenerator implements Generator {
    private static final int NUMBER_OF_REGISTRIES = 999;
    private static final int NUMBER_OF_MONTHS = 12;
    private static final int NUMBER_OF_COUNTIES = 52;
    private static final int NUMBER_OF_GENDERS = 2;

    private static final int DAYS_IN_FEBRUARY = 28;
    private static final int THIRTY_DAYS_MONTH = 30;
    private static final int THIRTY_ONE_DAYS_MONTH = 31;

    private static final int ID_MASCULINE = 0;

    private static final int BETWEEN_TWENTIETH_CENTURY_CODE = 1;
    private static final int BETWEEN_NINETIETH_CENTURY_CODE = 3;
    private static final int BETWEEN_TWENTY_FIRST_CENTURY_CODE = 5;

    private static final int MIN_YEAR = 1800;
    private static final int MAX_YEAR = 2099;

    private static final int TWENTIETH_CENTURY_YEAR_START = 1900;
    private static final int NINETIETH_CENTURY_YEAR_END = 1899;
    private static final int TWENTY_FIRST_CENTURY_YEAR_START = 2000;
    private static final int TWENTIETH_CENTURY_YEAR_END = 1999;

    private static final String ZERO_FILL_TWO_DECIMALS = "%02d";
    private static final String ZERO_FILL_THREE_DECIMALS = "%03d";

    private static final int CONTROL_SUM_DIVIDER = 11;
    private static int[] CONTROL_VALUES = new int[]{2, 7, 9, 1, 4, 6, 3, 5, 8, 2, 7, 9};
    private static final String NUMBER_MISMATCH_ERROR_MESSAGE = "Number of entries must be positive";

    private int numberOfEntries;
    private List<CNP> cnps;
    private List<Integer> thirtyDaysMonth;
    private List<Integer> thirtyOneDaysMonth;
    private Random random;

    public CNPGenerator(int numberOfEntries) {
        this.numberOfEntries = numberOfEntries;
        random = new Random();
        cnps = new ArrayList<>();
        thirtyDaysMonth = Arrays.asList(Calendar.JANUARY, Calendar.MARCH, Calendar.MAY, Calendar.JULY, Calendar.AUGUST, Calendar.OCTOBER);
        thirtyOneDaysMonth = Arrays.asList(Calendar.APRIL, Calendar.JUNE, Calendar.SEPTEMBER, Calendar.NOVEMBER);
    }

    @Override
    public List<CNP> generate() {
        if (numberOfEntries < 0) {
            throw new IllegalArgumentException(NUMBER_MISMATCH_ERROR_MESSAGE);
        }

        for (int i = 0; i < numberOfEntries; i++) {
            cnps.add(createCNP());
        }

        return cnps;
    }

    private CNP createCNP() {
        final int year = createYear();
        final int month = createMonth();
        final int day = createDay(month);
        final int sex = createSex(year);
        final int county = createCounty();
        final int registryNumber = createRegistryNumber();
        final int yearIdentifier = year % 100;
        final String cnpWithoutControlSum = sex +
                String.format(ZERO_FILL_TWO_DECIMALS, yearIdentifier) +
                String.format(ZERO_FILL_TWO_DECIMALS, month) +
                String.format(ZERO_FILL_TWO_DECIMALS, day) +
                String.format(ZERO_FILL_TWO_DECIMALS, county) +
                String.format(ZERO_FILL_THREE_DECIMALS, registryNumber);

        return new CNP(String.valueOf(sex),
                String.format(ZERO_FILL_TWO_DECIMALS, yearIdentifier),
                String.format(ZERO_FILL_TWO_DECIMALS, month),
                String.format(ZERO_FILL_TWO_DECIMALS, day),
                String.format(ZERO_FILL_TWO_DECIMALS, county),
                String.format(ZERO_FILL_THREE_DECIMALS, registryNumber),
                String.valueOf(getControlSum(cnpWithoutControlSum)));
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

    private int createSex(int dataNasterii) {
        int randomSex = random.nextInt(NUMBER_OF_GENDERS);
        int dateCode = getCenturyCodeByYear(dataNasterii);
        return randomSex == ID_MASCULINE ? dateCode : dateCode + 1;
    }

    private int getCenturyCodeByYear(int anulNasterii) {
        if (isDateBetweenTwentiethCentury(anulNasterii)) {
            return BETWEEN_TWENTIETH_CENTURY_CODE;
        }

        if (isDateBetweenNinetiethCentury(anulNasterii)) {
            return BETWEEN_NINETIETH_CENTURY_CODE;
        }

        if (isDateBetweenTwentyFirstCentury(anulNasterii)) {
            return BETWEEN_TWENTY_FIRST_CENTURY_CODE;
        }

        return BETWEEN_TWENTIETH_CENTURY_CODE;
    }

    private boolean isDateBetweenTwentyFirstCentury(int anulNasterii) {
        return anulNasterii >= TWENTY_FIRST_CENTURY_YEAR_START && anulNasterii <= MAX_YEAR;
    }

    private boolean isDateBetweenNinetiethCentury(int anulNasterii) {
        return anulNasterii >= MIN_YEAR && anulNasterii <= NINETIETH_CENTURY_YEAR_END;
    }

    private boolean isDateBetweenTwentiethCentury(int anulNasterii) {
        return anulNasterii >= TWENTIETH_CENTURY_YEAR_START && anulNasterii <= TWENTIETH_CENTURY_YEAR_END;
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
