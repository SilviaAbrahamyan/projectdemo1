package com.aca.ddlanalyzer;

/**
 * Created by home on 1/12/2019.
 */
public class DDlAnalyzerFactory {
    public static DDLAnalyzer getAnalyzer(String type) {
        switch (type){
            case "mysql":
                return new MySQLDDLAnalyzerImpl();
            case "postgresql":
                return new PostgresqlDDLAnalyzerImpl();
            default:
                return null;
        }

    }
}
