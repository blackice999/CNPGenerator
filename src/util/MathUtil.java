package util;

public class MathUtil {
    public static boolean outsideExclusive(int number, int lowerBound, int upperBound) {
        return number < lowerBound && number > upperBound;
    }

    public static boolean lessThan(int number, int upperBound) {
        return number < upperBound;
    }
}
