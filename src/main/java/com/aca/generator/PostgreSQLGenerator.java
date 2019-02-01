package com.aca.generator;

import com.aca.components.column.PostgreSQLColumn;
import com.aca.components.constraint.PostgreSQLConstraint;
import com.aca.components.generatedSQLs.GeneratedCreateSQLs;
import com.aca.components.generatedSQLs.GeneratedForeignSQls;
import com.aca.components.generatedSQLs.GeneratedPrimarySQLs;
import com.aca.components.table.MySQLTable;
import com.aca.components.table.PostgreSQLTable;
import com.aca.components.Schema;
import com.aca.components.util.UnsupportedFeatures;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 1/21/2019.
 */
public class PostgreSQLGenerator implements SQLGenerator<PostgreSQLTable> {

    @Override
    public void generateSQLOf(Schema<PostgreSQLTable> schema) throws SQLException {
        schema
                .getTables()
                .stream()
                .filter(PostgreSQLTable::isEnabled)
                .forEach(table -> {
                    StringBuilder sql = new StringBuilder();
                    sql
                            .append("CREATE TABLE IF NOT EXISTS ")
                            .append(table.getName())
                            .append(" (");

                    table
                            .getColumns()
                            .stream()
                            .forEach(column -> sql
                                    .append(column.getName())
                                    .append(" ")
                                    .append(column.getType())
                                    .append(" ")
                                    .append(column.getDefaultValue() != null ? "DEFAULT " + column.getDefaultValue() : "")
                                    .append(" ")
                                    .append(column.getIsNullable())
                                    .append(","));

                    sql.setLength(sql.length() - 1);
                    sql.append(");\n");

                    GeneratedCreateSQLs.add(sql.toString());
                    sql.setLength(0);
                });

        schema
                .getTables()
                .stream()
                .filter(table -> table.getConstraintByPrimaryKey().size() != 0)
                .filter(PostgreSQLTable::isEnabled)
                .forEach(table -> {
                    StringBuilder sql = new StringBuilder();
                    sql
                            .append(" ALTER TABLE ")
                            .append(table.getName())
                            .append(" ADD PRIMARY KEY(");
                    for (PostgreSQLConstraint constraint : table.getConstraintByPrimaryKey()) {
                        sql.append(constraint.getColumn()).append(",");
                    }
                    sql.setLength(sql.length() - 1);
                    sql.append(");\n");

                    GeneratedPrimarySQLs.add(sql.toString());
                    sql.setLength(0);
                });
        schema
                .getTables()
                .stream()
                .filter(table -> table.getConstraintByPrimaryKey().size() != 0)
                .filter(PostgreSQLTable::isEnabled)
                .forEach(table -> {

                    table
                            .getConstraintByForeignKey()
                            .forEach(constraint -> {
                                boolean enabledReferencedTable = false;
                                for (PostgreSQLTable sqlTable : schema.getTables()) {
                                    if (sqlTable.getName().equalsIgnoreCase(constraint.getReferencedTable())
                                            && sqlTable.isEnabled()) {
                                        enabledReferencedTable = true;
                                        break;
                                    }
                                }
                                if (enabledReferencedTable) {
                                    StringBuilder sql = new StringBuilder();
                                    sql.append(" ALTER TABLE " + constraint.getTable() + " ADD CONSTRAINT ")
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

                                    GeneratedForeignSQls.add(sql.toString());
                                    sql.setLength(0);

                                } else {
                                    UnsupportedFeatures.add("Can't create " + constraint.getTable() + " FK CONSTRAINT from " + constraint.getColumn() + " to " +
                                            constraint.getReferencedColumn() + " column because " + constraint.getReferencedTable() + " doesn't exists");
                                }
                            });
                });

    }
}
