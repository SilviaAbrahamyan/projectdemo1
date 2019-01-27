package com.aca.generator;

import com.aca.components.column.PostgreSQLColumn;
import com.aca.components.constraint.PostgreSQLConstraint;
import com.aca.components.table.PostgreSQLTable;
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
        StringBuilder sql = new StringBuilder();
        for (PostgreSQLTable table : schema.getTables()) {
            sql.append("CREATE TABLE IF NOT EXISTS " + table.getName() + " (");
            for (PostgreSQLColumn column : table.getColumns()) {
                sql.append(column.getName())
                        .append(" ")
                        .append(column.getType())
                        .append(" ")
                        .append(column.getDefaultValue() != null ? "DEFAULT " + column.getDefaultValue() : "")
                        .append(" ")
                        .append(column.getIsNullable())
                        .append(",");
                sql.setLength(sql.length() - 1);
            }
            sql.append(");\n");
        }
        schema.getTables().stream().filter(table -> table.getConstraintByPrimaryKey().size() != 0).forEach(table -> {
            sql.append(" ALTER TABLE " + table.getName() + " ADD PRIMARY KEY(");
            for (PostgreSQLConstraint constraint : table.getConstraintByPrimaryKey()) {
                sql.append(constraint.getColumn()).append(",");
            }
            sql.setLength(sql.length() - 1);
            sql.append(");\n");
        });
        schema.getTables().stream().filter(table -> table.getConstraintByPrimaryKey().size() != 0).forEach(table -> {
            for (PostgreSQLConstraint constraint : table.getConstraintByForeignKey()) {
                sql.append(" ALTER TABLE " + table.getName() + " ADD CONSTRAINT ")
                        .append(constraint.getName())
                        .append(" FOREIGN KEY")
                        .append("(")
                        .append(constraint.getColumn())
                        .append(")")
                        .append(" REFERENCES ")
                        .append(constraint.getReferencedTable())
                        .append("(")
                        .append(constraint.getReferencedColumn())
                        .append(");\n");
            }
        });
        System.out.println(sql);
        statement = connection.createStatement();
        statement.executeUpdate(sql.toString());
    }

}
