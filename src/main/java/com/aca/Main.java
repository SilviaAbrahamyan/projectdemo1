package com.aca;

import com.aca.components.Column;
import com.aca.components.Constraint;
import com.aca.components.Schema;
import com.aca.components.Table;
import com.aca.ddlanalyzer.DDlAnalyzerFactory;
import com.aca.helper.JdbcUrlHelper;

import java.sql.SQLException;

/**
 * Created by home on 1/12/2019.
 */
public class Main {
    public static void main(String[] args) throws SQLException {

        String jdbcUrl = "jdbc:mysql://localhost:3306/test2";
        //String jdbcUrl = "jdbc:postgresql://localhost:5432/test2";
        Schema schema = DDlAnalyzerFactory.getAnalyzer(JdbcUrlHelper.getDbType(jdbcUrl)).getSchema();

        for (Table table : schema.getTables()) {
            System.out.println(table.getName());
            for (Column column : table.getColumns()) {
                System.out.print("\t\t" + column.getField());
                System.out.print("\t\t" + column.getType());
                System.out.print("\t\t" + column.getNullable());
                System.out.print("\t\t" + column.getKey());
                System.out.print("\t\t" + column.getDefaultValue());
                System.out.println("\t\t" + column.getExtra());
            }
            if (table.getConstraints().size() != 0) {
                System.out.println("FOREIGN KEYs for " + table.getName());
                for (Constraint constraint : table.getConstraints()) {
                    System.out.print("\t\t" + constraint.getColumn());
                    System.out.print("\t\t" + constraint.getConstraint());
                    System.out.print("\t\t" + constraint.getReferencedTable());
                    System.out.println("\t\t" + constraint.getReferencedColumn());
                }
                System.out.println();
            }
        }

    }
}
