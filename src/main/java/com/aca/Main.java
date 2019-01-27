package com.aca;

import com.aca.components.*;
import com.aca.components.table.MySQLTable;
import com.aca.components.table.PostgreSQLTable;
import com.aca.converter.ConverterFactory;
import com.aca.ddlanalyzer.DDlAnalyzerFactory;
import com.aca.helper.JdbcUrlHelper;
import com.aca.generator.SQLGeneratorFactory;

import java.sql.SQLException;

/**
 * Created by home on 1/12/2019.
 */
public class Main {
    public static void main(String[] args) throws SQLException {

        String jdbcMySQLUrl = "jdbc:mysql://aca-db.duckdns.org:3306/do_not_touch";
        String jdbcPostgreSqlUrl = "jdbc:postgresql://aca-db.duckdns.org:5432/postgres";

//String jdbcMySQLUrl = "jdbc:mysql://localhost:3306/tutorial";
//        String jdbcPostgreSqlUrl = "jdbc:postgresql://localhost:5432/postgres";
        Schema postgreSQLSchema = DDlAnalyzerFactory.getAnalyzer(JdbcUrlHelper.getDbType(jdbcPostgreSqlUrl)).getSchema(jdbcPostgreSqlUrl);
        Schema mySQLSchema = DDlAnalyzerFactory.getAnalyzer(JdbcUrlHelper.getDbType(jdbcMySQLUrl)).getSchema(jdbcMySQLUrl);
        Schema<PostgreSQLTable> convertedMyToPostgres = ConverterFactory.getConverter(JdbcUrlHelper.getDbType(jdbcMySQLUrl), JdbcUrlHelper.getDbType(jdbcPostgreSqlUrl)).convert(mySQLSchema);
        Schema<MySQLTable> convertedPostgresToMy = ConverterFactory.getConverter(JdbcUrlHelper.getDbType(jdbcPostgreSqlUrl), JdbcUrlHelper.getDbType(jdbcMySQLUrl)).convert(postgreSQLSchema);

        convertedMyToPostgres.getTables().forEach(
                t -> {
                    System.out.println("Table : " + t.getName());
                    System.out.println("Columns :");
                    System.out.println("\t" + t.getColumns());
                    {
                        if (t.getConstraints().size() != 0) {
                            System.out.println("Constraint :");
                            System.out.println("\t" + t.getConstraints());
                        }
                    }
                    System.out.println("------------------------------------------------");
                });
        SQLGeneratorFactory.getSQLGenerator(JdbcUrlHelper.getDbType(jdbcPostgreSqlUrl)).migrate(convertedMyToPostgres);

    }
}
