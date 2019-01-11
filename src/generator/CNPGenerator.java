package generator;

import generator.counties.Counties;
import util.MathUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CNPGenerator implements Generator {
    private static final String COUNTY_CODE_NOT_VALID_ERROR_MESSAGE = "County code not valid";
    private static final String NUMBER_MISMATCH_ERROR_MESSAGE = "Number of entries must be positive";

    private List<CNP> cnps;

    public CNPGenerator() {
        cnps = new ArrayList<>();
    }

    @Override
    public List<CNP> generate(int numberOfElements) {
        verifyIfNumberIsBetweenBounds(numberOfElements);
        for (int i = 0; i < numberOfElements; i++) {
            cnps.add(new CNPBuilder().createCNP());
        }

        return cnps;
    }


    public List<CNP> generateBySex(int numberOfElements, int sex) {
        verifyIfNumberIsBetweenBounds(numberOfElements);
        verifyIfSexInvalid(sex);
        for (int i = 0; i < numberOfElements; i++) {
            cnps.add(new CNPBuilder().setSex(sex).createCNP());
        }

        return cnps;
    }

    public List<CNP> generateByYear(int numberOfElements, int year) {
        verifyIfNumberIsBetweenBounds(numberOfElements);
        verifyIfYearInvalid(year);
        for (int i = 0; i < numberOfElements; i++) {
            cnps.add(new CNPBuilder().setYear(year).createCNP());
        }

        return cnps;
    }

    public List<CNP> generateByMonth(int numberOfElements, int month) {
        verifyIfNumberIsBetweenBounds(numberOfElements);
        verifyIMonthInvalid(month);
        for (int i = 0; i < numberOfElements; i++) {
            cnps.add(new CNPBuilder().setMonth(month).createCNP());
        }

        return cnps;
    }

    public List<CNP> generateByDay(int numberOfElements, int day) {
        verifyIfNumberIsBetweenBounds(numberOfElements);
        verifyIfDayInvalid(day);
        for (int i = 0; i < numberOfElements; i++) {
            cnps.add(new CNPBuilder().setDay(day).createCNP());
        }

        return cnps;
    }

    public List<CNP> generateByCounty(int numberOfElements, int countyCode) {
        verifyIfNumberIsBetweenBounds(numberOfElements);
        verifyIfCountyCodeInvalid(countyCode);
        for (int i = 0; i < numberOfElements; i++) {
            cnps.add(new CNPBuilder().setCounty(countyCode).createCNP());
        }

        return cnps;
    }

    public List<CNP> generateByRegistryNumber(int numberOfElements, int registryNumber) {
        verifyIfNumberIsBetweenBounds(numberOfElements);
        verifyIfRegistryNumberInvalid(registryNumber);
        for (int i = 0; i < numberOfElements; i++) {
            cnps.add(new CNPBuilder().setRegistryNumber(registryNumber).createCNP());
        }

        return cnps;
    }

    private void verifyIfNumberIsBetweenBounds(int numberOfElements) {
        if (MathUtil.lessThan(numberOfElements, 0)) {
            throw new IllegalArgumentException(NUMBER_MISMATCH_ERROR_MESSAGE);
        }
    }

    private void verifyIfNumberIsBetweenBounds(int numberOfElements, int lowerBound, int upperBound) {
        if (MathUtil.outsideExclusive(numberOfElements, lowerBound, upperBound)) {
            throw new IllegalArgumentException("Value should be between " + lowerBound + " and " + upperBound);
        }
    }

    private boolean countyCodeInvalid(int countyCode) {
        return Arrays.stream(Counties.COUNTIES).anyMatch(i -> i != countyCode);
    }

    private void verifyIfSexInvalid(int sex) {
        verifyIfNumberIsBetweenBounds(sex, 1, 6);
    }

    private void verifyIfYearInvalid(int year) {
        verifyIfNumberIsBetweenBounds(year, CNPBuilder.MIN_YEAR, CNPBuilder.MAX_YEAR);
    }

    private void verifyIMonthInvalid(int month) {
        verifyIfNumberIsBetweenBounds(month, 1, CNPBuilder.NUMBER_OF_MONTHS);
    }

    private void verifyIfDayInvalid(int day) {
        verifyIfNumberIsBetweenBounds(day, 1, CNPBuilder.THIRTY_ONE_DAYS_MONTH);
    }

    private void verifyIfCountyCodeInvalid(int countyCode) {
        if (countyCodeInvalid(countyCode)) {
            throw new IllegalArgumentException(COUNTY_CODE_NOT_VALID_ERROR_MESSAGE);
        }
    }

    private void verifyIfRegistryNumberInvalid(int registryNumber) {
        verifyIfNumberIsBetweenBounds(registryNumber, 1, CNPBuilder.NUMBER_OF_REGISTRIES);
    }
}
