package com.aca.generator;

/**
 * Created by home on 1/25/2019.
 */
public class SQLGeneratorFactory {
    public static SQLGenerator getSQLGenerator(String type) {
        switch (type) {
            case "mysql":
                return new MySQLGenerator();
            case "postgresql":
                return new PostgreSQLGenerator();
            default:
                return null;
        }
    }
}

