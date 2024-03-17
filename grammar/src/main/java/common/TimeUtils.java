package common;

public class TimeUtils {

    /**
     * no instances of this class
     */
    private TimeUtils() {

    }

    public static long timeToMillis(final String time) {

        if (!time.startsWith("T#")) {
            throw new RuntimeException("illegal format \"" + time + "\"");
        }

        String substring = time.substring(2);

        if (substring.endsWith("ms")) {

            substring = substring.substring(0, substring.length() - 2);

            // System.out.println(substring);

            long lValue = Long.parseLong(substring);

            return lValue;

        } else if (substring.endsWith("s")) {

            substring = substring.substring(0, substring.length() - 1);

            // System.out.println(substring);

            long lValue = Long.parseLong(substring);

            return lValue * 1000;

        }

        return 0L;
    }

}
