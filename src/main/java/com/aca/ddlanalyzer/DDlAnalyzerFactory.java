package com.aca.ddlanalyzer;

import com.aca.helper.JdbcUrlHelper;

/**
 * Created by home on 1/12/2019.
 */
public class DDlAnalyzerFactory {
    public static DDLAnalyzer getAnalyzer(String url, String username, String password) {

        String type = JdbcUrlHelper.getDbType(url);

        switch (type) {
            case "mysql":
                return new MySQLDDLAnalyzerImpl(url,username,password);
            case "postgresql":
                return new PostgresqlDDLAnalyzerImpl(url,username,password);

            /*case "oracle":
                return new OracleDDLAnalyzer();
            case "sqlserver":
                return new SQLServerDDLAnalyzer();*/

            default:
                throw new UnsupportedOperationException("Unsupported RDBMS");
        }
    }
}
