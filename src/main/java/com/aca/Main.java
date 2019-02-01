package com.aca;

import com.aca.components.*;
import com.aca.components.table.MySQLTable;
import com.aca.components.table.PostgreSQLTable;
import com.aca.components.util.DoDbWork;
import com.aca.components.util.UnsupportedFeatures;
import com.aca.converter.ConverterFactory;
import com.aca.dataMigrator.DataMigratorFactory;
import com.aca.ddlanalyzer.DDlAnalyzerFactory;
import com.aca.generator.SQLGenerator;
import com.aca.helper.JdbcUrlHelper;
import com.aca.generator.SQLGeneratorFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Created by home on 1/12/2019.
 */
public class Main {
    public static void main(String[] args) throws SQLException {
        String usernameFrom = "root";
        String usernameTo = "postgres";
        String passwordFrom = "root";
        String passwordTo = "root";

        // String jdbcMySQLUrl = "jdbc:mysql://aca-db.duckdns.org:3306/do_not_touch";
        //  String jdbcPostgreSqlUrl = "jdbc:postgresql://aca-db.duckdns.org:5432/postgres";

        String jdbcMySQLUrl = "jdbc:mysql://localhost:3306/test2";
        String jdbcPostgreSqlUrl = "jdbc:postgresql://localhost:5432/postgres";
        Schema postgreSQLSchema = DDlAnalyzerFactory.getAnalyzer(JdbcUrlHelper.getDbType(jdbcPostgreSqlUrl)).getSchema(jdbcPostgreSqlUrl);
        Schema mySQLSchema = DDlAnalyzerFactory.getAnalyzer(JdbcUrlHelper.getDbType(jdbcMySQLUrl)).getSchema(jdbcMySQLUrl);

        Schema mySQLSchemaAfterSelected = userSelectedTables(mySQLSchema, jdbcMySQLUrl);
        Schema postgreSQLSchemaAfterSelected = userSelectedTables(postgreSQLSchema, jdbcPostgreSqlUrl);


        Schema<PostgreSQLTable> convertedMyToPostgres = ConverterFactory.getConverter(JdbcUrlHelper.getDbType(jdbcMySQLUrl), JdbcUrlHelper.getDbType(jdbcPostgreSqlUrl)).convert(mySQLSchemaAfterSelected);
        // Schema<MySQLTable> convertedPostgresToMy = ConverterFactory.getConverter(JdbcUrlHelper.getDbType(jdbcPostgreSqlUrl), JdbcUrlHelper.getDbType(jdbcMySQLUrl)).convert(postgreSQLSchemaAfterSelected);


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


        SQLGeneratorFactory
                .getSQLGenerator(JdbcUrlHelper.getDbType(jdbcPostgreSqlUrl))
                .generateSQLOf(convertedMyToPostgres);
//        SQLGeneratorFactory
//                .getSQLGenerator(JdbcUrlHelper.getDbType(jdbcMySQLUrl))
//                .generateSQLOf(convertedPostgresToMy);

        DataMigratorFactory.getDataMigrator(jdbcMySQLUrl, jdbcPostgreSqlUrl, usernameFrom, passwordFrom).generateMigrationSQL(mySQLSchemaAfterSelected);
      //  DataMigratorFactory.getDataMigrator(jdbcPostgreSqlUrl,jdbcMySQLUrl,usernameFrom,passwordFrom).generateMigrationSQL(postgreSQLSchemaAfterSelected);

        System.out.println(UnsupportedFeatures.getUnsupportedFeatures());

        //popupa galis ete yndunuma migrate enq anum

        DoDbWork.migrate(jdbcPostgreSqlUrl, usernameTo, passwordTo);
//        DoDbWork.migrate(jdbcMySQLUrl, usernameTo, passwordTo);
    }

    private static <T> Schema<T> userSelectedTables(Schema<T> schema, String url) {

        String dbType = JdbcUrlHelper.getDbType(url);

        Scanner sc = new Scanner(System.in);
        System.out.println("Select table index for migrating: ");
        String indexes;
        int index = 0;

        switch (dbType) {
            case "mysql":
                for (T table : schema.getTables()) {
                    System.out.println("\t" + index++ + " " + ((MySQLTable) table).getName());
                }
                indexes = sc.nextLine();
                List<Integer> indexM = Arrays
                        .stream(indexes.split(" "))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                indexM
                        .forEach(i -> ((MySQLTable) schema.getTables().get(i)).setEnabled(true));

                return schema;
            case "postgresql":
                for (T table : schema.getTables()) {
                    System.out.println("\t" + index++ + " " + ((PostgreSQLTable) table).getName());
                }
                indexes = sc.nextLine();
                List<Integer> indexP = Arrays
                        .stream(indexes.split(" "))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                indexP
                        .forEach(i -> ((PostgreSQLTable) schema.getTables().get(i)).setEnabled(true));
                return schema;
            default:
                return null;
        }
    }
}



