package demo.usul.snowflake.util;

import static demo.usul.snowflake.SnowflakeID.SEQUENTIAL_PREFIX;

public class SnowflakeUtil {

    public static long sequentialSuffix(String path) {
        String substring = path.substring(path.lastIndexOf("/") + 1);
        return Long.parseLong(substring.substring(SEQUENTIAL_PREFIX.length() + 1));
    }
}
