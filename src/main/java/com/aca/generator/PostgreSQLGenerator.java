package com.aca.generator;

import com.aca.components.PostgreSQLColumn;
import com.aca.components.PostgreSQLConstraint;
import com.aca.components.PostgreSQLTable;
import com.aca.components.Schema;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by home on 1/21/2019.
 */
public class PostgreSQLGenerator implements SQLGenerator<PostgreSQLTable> {
    private static Connection connection = null;
    private static Statement statement = null;

    //   ("FOREIGN KEY".equals((postgreSQLConstraintByColumnName != null ? (postgreSQLConstraintByColumnName.getType()) : " ")) ?
    //   "REFERENCES " + postgreSQLConstraintByColumnName.getReferencedTable() + "(" + postgreSQLConstraintByColumnName.getReferencedColumn() + ")" : "")

    @Override
    public void migrate(Schema<PostgreSQLTable> schema) throws SQLException {
        this.connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres",
                "postgres",
                "root"
        );
        for (PostgreSQLTable table : schema.getTables()) {
            int size = table.getColumns().size();
            String sql = "CREATE TABLE " + table.getName() + " (";
            for (PostgreSQLColumn column : table.getColumns()) {
                PostgreSQLConstraint postgreSQLConstraintByColumnName = table.getConstraintByColumnName(column.getName());
                if (size != 1) {
                    sql += column.getName() + " " + column.getDataType() +
                            ((column.getCharacterMaximumLength() != 0) ? ("(" + column.getCharacterMaximumLength() + ")") : "") +
                            " " + column.getIsNullable() + " " +
                            ("PRIMARY KEY".equals((postgreSQLConstraintByColumnName != null ? (postgreSQLConstraintByColumnName.getType()) : " ")) ? "PRIMARY KEY" : "") + ", ";
                } else {
                    sql += column.getName() + " " + column.getDataType() +
                            ((column.getCharacterMaximumLength() != 0) ? ("(" + column.getCharacterMaximumLength() + ")") : "") +
                            " " + column.getIsNullable() + " " +
                            ("PRIMARY KEY".equals((postgreSQLConstraintByColumnName != null ? (postgreSQLConstraintByColumnName.getType()) : " ")) ? "PRIMARY KEY" : "");
                }
                size--;
            }
            sql += ")";
            System.out.println(sql);
            statement = connection.createStatement();
            statement.executeUpdate(sql);
            System.out.println(table.getName() + " Table created successfully");
        }
    }

}
