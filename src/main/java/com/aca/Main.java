package com.aca;

import com.aca.components.*;
import com.aca.components.table.MySQLTable;
import com.aca.components.table.PostgreSQLTable;
import com.aca.converter.ConverterFactory;
import com.aca.ddlanalyzer.DDlAnalyzerFactory;
import com.aca.helper.JdbcUrlHelper;
import com.aca.generator.SQLGeneratorFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by home on 1/12/2019.
 */
public class Main {
    public static void main(String[] args) throws SQLException {

        String jdbcMySQLUrl = "jdbc:mysql://aca-db.duckdns.org:3306/do_not_touch";
        String jdbcPostgreSqlUrl = "jdbc:postgresql://aca-db.duckdns.org:5432/postgres";

//        String jdbcMySQLUrl = "jdbc:mysql://localhost:3306/test2";
//        String jdbcPostgreSqlUrl = "jdbc:postgresql://localhost:5432/postgres";
        Schema postgreSQLSchema = DDlAnalyzerFactory.getAnalyzer(JdbcUrlHelper.getDbType(jdbcPostgreSqlUrl)).getSchema(jdbcPostgreSqlUrl);
        Schema mySQLSchema = DDlAnalyzerFactory.getAnalyzer(JdbcUrlHelper.getDbType(jdbcMySQLUrl)).getSchema(jdbcMySQLUrl);


        int index = -1;
        Scanner sc = new Scanner(System.in);
        System.out.println("Select table index for migrating: ");
        for (Object o : mySQLSchema.getTables()) {
            System.out.println("\t" + (++index) + " " + ((MySQLTable) o).getName());
        }

        String indexes = sc.nextLine().trim();
        indexes = indexes.replaceAll("\\s+", "");
        Schema mySQLSchemaAfterSelected = userSelectedTables(indexes, mySQLSchema, JdbcUrlHelper.getDbType(jdbcMySQLUrl));
        //Schema postgreSQLSchemaAfterSelected = userSelectedTables(indexes, postgreSQLSchema, JdbcUrlHelper.getDbType(jdbcPostgreSqlUrl));

        Schema<PostgreSQLTable> convertedMyToPostgres = ConverterFactory.getConverter(JdbcUrlHelper.getDbType(jdbcMySQLUrl), JdbcUrlHelper.getDbType(jdbcPostgreSqlUrl)).convert(mySQLSchemaAfterSelected);
        //     Schema<MySQLTable> convertedPostgresToMy = ConverterFactory.getConverter(JdbcUrlHelper.getDbType(jdbcPostgreSqlUrl), JdbcUrlHelper.getDbType(jdbcMySQLUrl)).convert(postgreSQLSchemaAfterSelected);

//        convertedMyToPostgres.getTables().forEach(
//                t -> {
//                    System.out.println("Table : " + t.getName());
//                    System.out.println(t.isEnable());
//                    System.out.println("Columns :");
//                    System.out.println("\t" + t.getColumns());
//                    {
//                        if (t.getConstraints().size() != 0) {
//                            System.out.println("Constraint :");
//                            System.out.println("\t" + t.getConstraints());
//                        }
//                    }
//                    System.out.println("------------------------------------------------");
//                });
        SQLGeneratorFactory.getSQLGenerator(JdbcUrlHelper.getDbType(jdbcPostgreSqlUrl)).migrate(convertedMyToPostgres);
    }

    static Schema userSelectedTables(String indexes, Schema schema, String dbType) {
        indexes = indexes.replaceAll("\\s+", "");
        switch (dbType) {
            case "mysql":
                for (int i = 0; i < indexes.length(); i++) {
                    int index = Character.getNumericValue(indexes.charAt(i));
                    Object mySQLTable = schema.getTables().get(index);
                    ((MySQLTable) mySQLTable).setEnable(true);
                }
                return schema;
            case "postgresql":
                for (int i = 0; i < indexes.length(); i++) {
                    int index = Character.getNumericValue(indexes.charAt(i));
                    Object postgreSQLTable = schema.getTables().get(index);
                    ((PostgreSQLTable) postgreSQLTable).setEnable(true);
                }
                return schema;
            default:
                return null;
        }
    }
}



