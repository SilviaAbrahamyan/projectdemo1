package com.aca.helper;

/**
 * Created by home on 1/12/2019.
 */
public class JdbcUrlHelper {
    public static String getDbType(String jdbcUrl) {
        int firstIndex = jdbcUrl.indexOf(":") + 1;
        return jdbcUrl.substring(firstIndex , jdbcUrl.indexOf(":", firstIndex));
    }
}
