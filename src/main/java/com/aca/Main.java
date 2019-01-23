package com.aca;

import com.aca.components.*;
import com.aca.converter.SchemaConverter;
import com.aca.ddlanalyzer.DDlAnalyzerFactory;
import com.aca.helper.JdbcUrlHelper;
import com.aca.sqlgenerator.MySQLGenerator;
import com.aca.sqlgenerator.PostgreSQLGenerator;

import java.sql.SQLException;

/**
 * Created by home on 1/12/2019.
 */
public class Main {
    public static void main(String[] args) throws SQLException {

        String jdbcUrl = "jdbc:mysql://localhost:3306/test2";
       /// String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
        Schema<MySQLTable> schema = DDlAnalyzerFactory.getAnalyzer(JdbcUrlHelper.getDbType(jdbcUrl)).getSchema(jdbcUrl, "root", "root");

        schema.getTables().forEach(
                t -> {
                    System.out.println("Table : " + t.getTableName());
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

        Schema<PostgreSQLTable> converted = SchemaConverter.convertFromMySQLtoPostgrSQL(schema);
        converted.getTables().forEach(
                t -> {
                    System.out.println("Table : " + t.getTableName());
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
       // PostgreSQLGenerator.migrate(converted);

    }
}
