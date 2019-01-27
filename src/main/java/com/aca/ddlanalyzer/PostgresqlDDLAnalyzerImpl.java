package com.aca.ddlanalyzer;

import com.aca.components.*;
import com.aca.components.util.Nullable;


import java.sql.*;

/**
 * Created by home on 1/12/2019.
 */
public class PostgresqlDDLAnalyzerImpl implements DDLAnalyzer {
    @Override
    public Schema<PostgreSQLTable> getSchema(String jdbcUrl) throws SQLException {
        String user = "root";
        String password = "root";
        Connection connection = DriverManager.getConnection(
                jdbcUrl,
              user,
                password
        );

        String showTablesSql =
                "SELECT TABLE_NAME, TABLE_TYPE " +
                        "FROM INFORMATION_SCHEMA.TABLES " +
                        "WHERE TABLE_SCHEMA = 'public'";
        Statement showTablesStatement = connection.createStatement();
        ResultSet resultSet = showTablesStatement.executeQuery(showTablesSql);

        Schema<PostgreSQLTable> schema = new Schema<PostgreSQLTable>();
        while (resultSet.next()) {
            String tableName = resultSet.getString(1);
            PostgreSQLTable table = new PostgreSQLTable(resultSet.getString(1), resultSet.getString(2));
            PreparedStatement showColumnsStatement = connection.prepareStatement(
                    "SELECT COLUMN_NAME, ORDINAL_POSITION, COLUMN_DEFAULT, " +
                            "IS_NULLABLE, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, " +
                            "CHARACTER_OCTET_LENGTH, NUMERIC_PRECISION, NUMERIC_SCALE  " +
                            " FROM INFORMATION_SCHEMA.COLUMNS " +
                            " WHERE TABLE_SCHEMA = 'public' " +
                            " AND TABLE_NAME   = ?;");
            showColumnsStatement.setString(1, tableName);
            ResultSet resultSet1 = showColumnsStatement.executeQuery();
            while (resultSet1.next()) {

                table.addColumn(new PostgreSQLColumn(
                        resultSet1.getString(1),
                        resultSet1.getInt(2),
                        resultSet1.getString(3),
                        Nullable.valueOf(resultSet1.getString(4)),
                        resultSet1.getString(5),
                        resultSet1.getInt(6),
                        resultSet1.getInt(7),
                        resultSet1.getInt(8),
                        resultSet1.getInt(9)));

                PreparedStatement showFkeysStatement = connection.prepareStatement(
                        "SELECT  TC.CONSTRAINT_NAME, TC.CONSTRAINT_TYPE, TC.TABLE_NAME, KCU.COLUMN_NAME,  CCU.TABLE_NAME, CCU.COLUMN_NAME " +
                                " FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS TC " +
                                " JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS KCU" +
                                " ON TC.CONSTRAINT_NAME = KCU.CONSTRAINT_NAME" +
                                " AND TC.TABLE_SCHEMA = KCU.TABLE_SCHEMA" +
                                " JOIN INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE AS CCU" +
                                " ON CCU.CONSTRAINT_NAME = TC.CONSTRAINT_NAME" +
                                " AND CCU.TABLE_SCHEMA = TC.TABLE_SCHEMA" +
                                " WHERE TC.TABLE_NAME = ? ;");

                showFkeysStatement.setString(1, tableName);
                ResultSet resultSet2 = showFkeysStatement.executeQuery();
                while (resultSet2.next()) {
                    table.addConstraint(
                            new PostgreSQLConstraint(
                                    resultSet2.getString(1),
                                    resultSet2.getString(2),
                                    resultSet2.getString(3),
                                    resultSet2.getString(4),
                                    resultSet2.getString(5),
                                    resultSet2.getString(6)));

                }
                schema.addTables(table);
            }
        }
        return schema;
    }
}


