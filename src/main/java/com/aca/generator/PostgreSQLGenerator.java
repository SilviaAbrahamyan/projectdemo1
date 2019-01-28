package com.aca.generator;

import com.aca.components.column.PostgreSQLColumn;
import com.aca.components.constraint.PostgreSQLConstraint;
import com.aca.components.table.MySQLTable;
import com.aca.components.table.PostgreSQLTable;
import com.aca.components.Schema;

import java.sql.*;

/**
 * Created by home on 1/21/2019.
 */
public class PostgreSQLGenerator implements SQLGenerator<PostgreSQLTable> {

    private static Connection connection = null;
    private static Statement statement = null;

    @Override
    public void migrate(Schema<PostgreSQLTable> schema) throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres",
                "postgres",
                "root"
        );

        schema
                .getTables()
                .stream()
                .filter(PostgreSQLTable::isEnable)
                .forEach(table -> {
                    StringBuilder sql = new StringBuilder();
                    sql
                            .append("CREATE TABLE IF NOT EXISTS ")
                            .append(table.getName())
                            .append(" (");
                    for (PostgreSQLColumn column : table.getColumns()) {
                        sql
                                .append(column.getName())
                                .append(" ")
                                .append(column.getType())
                                .append(" ")
                                .append(column.getDefaultValue() != null ? "DEFAULT " + column.getDefaultValue() : "")
                                .append(" ")
                                .append(column.getIsNullable())
                                .append(",");
                    }
                    sql.setLength(sql.length() - 1);
                    sql.append(");\n");
                    System.out.println(sql);
                    try {
                        statement = connection.createStatement();
                        statement.executeUpdate(sql.toString());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
        schema
                .getTables()
                .stream()
                .filter(table -> table.getConstraintByPrimaryKey().size() != 0)
                .filter(PostgreSQLTable::isEnable)
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
                    System.out.println(sql);
                    try {
                        statement = connection.createStatement();
                        statement.executeUpdate(sql.toString());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
        schema
                .getTables()
                .stream()
                .filter(table -> table.getConstraintByPrimaryKey().size() != 0)
                .filter(PostgreSQLTable::isEnable)
                .forEach(table -> {
                    for (PostgreSQLConstraint constraint : table.getConstraintByForeignKey()) {
                        DatabaseMetaData dbm = null;
                        try {
                            dbm = connection.getMetaData();
                            ResultSet tables = dbm.getTables(null, null, constraint.getReferencedTable().toLowerCase(), null);
                            if (tables.next()) {
                                StringBuilder sql = new StringBuilder();
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
                                System.out.println(sql);
                                try {
                                    statement = connection.createStatement();
                                    statement.executeUpdate(sql.toString());
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                System.out.println("Can't create " + table.getName() + " FK CONSTRAINT from " + constraint.getColumn() + " to " +
                                        constraint.getReferencedColumn() + " column because " + constraint.getReferencedTable() + " doesn't exists");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });

//        for(PostgreSQLTable table: schema.getTables()) {
//            if (table.getConstraintByPrimaryKey().size() != 0 && table.isEnable()) {
//                for (PostgreSQLConstraint constraint : table.getConstraintByForeignKey()) {
//                    DatabaseMetaData dbm = connection.getMetaData();
//                    ResultSet tables = dbm.getTables(null, null, constraint.getReferencedTable(), null);
//                    if (tables.next()) {
//                        sql.append(" ALTER TABLE " + table.getName() + " ADD CONSTRAINT ")
//                                .append(constraint.getName())
//                                .append(" FOREIGN KEY")
//                                .append("(")
//                                .append(constraint.getColumn())
//                                .append(")")
//                                .append(" REFERENCES ")
//                                .append(constraint.getReferencedTable())
//                                .append("(")
//                                .append(constraint.getReferencedColumn())
//                                .append(");\n");
//                    } else {
//                        System.out.println("Can't create FK CONSTRAINT from " + constraint.getColumn() + " to " +
//                                constraint.getReferencedColumn() + " because " + constraint.getReferencedTable() + " doesn't exists");
//                    }
//                }
//            }
//        }
//        System.out.println(sql);
//        statement = connection.createStatement();
//        statement.executeUpdate(sql.toString());
    }

}
