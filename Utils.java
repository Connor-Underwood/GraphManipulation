import java.util.Arrays;
import java.util.Objects;

public class Utils {
    public static final String ROOT = "./";

    public static void checkEquals(Object expected, Object actual, String message) {
        if (!Objects.equals(expected, actual))
            throw new AssertionError(String.format("%s: expected %s, returned %s", message, expected, actual));
    }

    public static void checkAlmostEquals(Double expected, Double actual, String message) {
        if (Math.abs(actual - expected) > 1e-3)
            throw new AssertionError(String.format("%s: expected %s, returned %s", message, expected, actual));
            //throw new AssertionError(String.format("    ", message));
    }

    public static void checkArrayEquals(double[] expected, double[] actual, String message) {
        if (expected.length == actual.length) {
            for (int i = 0; i < expected.length; i++) {
                if (Math.abs(actual[i] - expected[i]) > 1e-3) {
                    throw new AssertionError(String.format("%s!\n\t - Expected: %s\n\t - Returned: %s",
                            message, Arrays.toString(expected), Arrays.toString(actual)));
                }
            }
        } else {
            throw new AssertionError("Array length mismatch: expected length " + expected.length + ", returned length " + actual.length);
        }
    }

    public static void checkArrayEquals(int[] expected, int[] actual, String message) {
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError(String.format("%s!\n\t - Expected: %s\n\t - Returned: %s",
                    message, Arrays.toString(expected), Arrays.toString(actual)));
        }
    }
}
