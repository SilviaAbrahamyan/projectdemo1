package com.aca.ddlanalyzer;

import com.aca.components.*;


import java.sql.*;

/**
 * Created by home on 1/12/2019.
 */
public class PostgresqlDDLAnalyzerImpl implements DDLAnalyzer {
    @Override
    public Schema<PostgreSQLTable> getSchema(String jdbcUrl, String username, String password) throws SQLException {
        Connection connection = DriverManager.getConnection(
                jdbcUrl,
                "postgres",
                "root"
        );

        String showTablesSql = "SELECT * FROM information_schema.tables WHERE table_schema = 'public'";
        Statement showTablesStatement = connection.createStatement();
        ResultSet resultSet = showTablesStatement.executeQuery(showTablesSql);

        Schema<PostgreSQLTable> schema = new Schema<PostgreSQLTable>();
        while (resultSet.next()) {
            String tableName = resultSet.getString(3);
            PostgreSQLTable table = new PostgreSQLTable(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4),
                    resultSet.getString(5), resultSet.getString(6), resultSet.getString(7), resultSet.getString(8), resultSet.getString(9), resultSet.getString(10),
                    resultSet.getString(11), resultSet.getString(12));
            PreparedStatement showColumnsStatement = connection.prepareStatement(
                    "SELECT  * FROM information_schema.columns " +
                            " WHERE table_schema = 'public' " +
                            " AND table_name   = ?;");
            showColumnsStatement.setString(1, tableName);
            ResultSet resultSet1 = showColumnsStatement.executeQuery();
            while (resultSet1.next()) {

                table.addColumns(new PostgreSQLColumn(resultSet1.getString(1), resultSet1.getString(2), resultSet1.getString(3),
                        resultSet1.getString(4), resultSet1.getString(5), resultSet1.getString(6), resultSet1.getString(7),
                        resultSet1.getString(8), resultSet1.getString(9), resultSet1.getString(10), resultSet1.getString(11),
                        resultSet1.getString(12), resultSet1.getString(13), resultSet1.getString(14), resultSet1.getString(15),
                        resultSet1.getString(16), resultSet1.getString(17), resultSet1.getString(18), resultSet1.getString(19),
                        resultSet1.getString(20), resultSet1.getString(21), resultSet1.getString(22), resultSet1.getString(23),
                        resultSet1.getString(24), resultSet1.getString(25), resultSet1.getString(26), resultSet1.getString(27),
                        resultSet1.getString(28), resultSet1.getString(29), resultSet1.getString(30), resultSet1.getString(31),
                        resultSet1.getString(32), resultSet1.getString(32), resultSet1.getString(33), resultSet1.getString(34),
                        resultSet1.getString(35), resultSet1.getString(36), resultSet1.getString(37), resultSet1.getString(38),
                        resultSet1.getString(39), resultSet1.getString(40), resultSet1.getString(41), resultSet1.getString(42)));

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


