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


    @Override
    public void migrate(Schema<PostgreSQLTable> schema) throws SQLException {
        this.connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres",
                "postgres",
                "root"
        );

        for (PostgreSQLTable table : schema.getTables()) {
            StringBuilder sql = new StringBuilder();
            int size = table.getColumns().size();
            sql.append("CREATE TABLE IF NOT EXISTS " + table.getName() + " (");
            for (PostgreSQLColumn column : table.getColumns()) {
                if (size != 1) {
                    sql.append(column.getName() + " " + column.getDataType() + " " +
                            ((column.getCharacterMaximumLength() != 0 && !(column.getDataType().equals("BYTEA"))) ? ("(" + column.getCharacterMaximumLength() + ")") : "") + " " +
                            (column.getDefaultValue() != null ? "DEFAULT " + column.getDefaultValue() : "") + " " + column.getIsNullable() + ",");
                } else {
                    sql.append(column.getName() + " " + column.getDataType() + " " +
                            ((column.getCharacterMaximumLength() != 0 && !(column.getDataType().equals("BYTEA"))) ? ("(" + column.getCharacterMaximumLength() + ")") : "") + " " +
                            (column.getDefaultValue() != null ? "DEFAULT " + column.getDefaultValue() : "") + " " + column.getIsNullable());
                }
                size--;
            }
            if (table.getConstraintByPrimaryKey().size() != 0) {
                sql.append(", PRIMARY KEY(");
                for (PostgreSQLConstraint constraint : table.getConstraintByPrimaryKey()) {
                    sql.append(constraint.getColumn() + ",");
                }
                sql.setLength(sql.length() - 1);
                sql.append(")");
            }
            sql.append(")");
            System.out.println(sql);
            statement = connection.createStatement();
            statement.executeUpdate(sql.toString());
            System.out.println(table.getName() + " Table created successfully");
        }
    }

}
