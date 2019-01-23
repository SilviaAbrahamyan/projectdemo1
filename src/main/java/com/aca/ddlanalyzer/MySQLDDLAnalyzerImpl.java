package com.aca.ddlanalyzer;

import com.aca.components.*;

import java.sql.*;

/**
 * Created by home on 1/12/2019.
 */
public class MySQLDDLAnalyzerImpl implements DDLAnalyzer {
    @Override
    public  Schema<MySQLTable> getSchema(String jdbcUrl, String username, String password) throws SQLException {
        Connection connection = DriverManager.getConnection(
               jdbcUrl,
               username,
                password
        );

        String showTablesSql = "SELECT * from Information_Schema.Tables WHERE table_schema = 'test2'";
        Statement showTablesStatement = connection.createStatement();
        ResultSet resultSet = showTablesStatement.executeQuery(showTablesSql);
        Schema<MySQLTable> schema = new Schema<MySQLTable>();
        while (resultSet.next()) {
            String tableName = resultSet.getString(3);
            MySQLTable table = new MySQLTable(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4),
                    resultSet.getString(5), resultSet.getString(6), resultSet.getString(7), resultSet.getString(8), resultSet.getString(9), resultSet.getString(10),
                    resultSet.getString(11), resultSet.getString(12), resultSet.getString(13), resultSet.getString(14), resultSet.getString(15),
                    resultSet.getString(16), resultSet.getString(17), resultSet.getString(18), resultSet.getString(19), resultSet.getString(20), resultSet.getString(21));
            MySQLTable table1 = new MySQLTable(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
            String showColumnsSql = "SELECT *" +
                    "  FROM INFORMATION_SCHEMA.COLUMNS" +
                    "  WHERE table_name =\'" + tableName + "\' ;";
            Statement showColumnsStatement = connection.createStatement();
            ResultSet resultSet1 = showColumnsStatement.executeQuery(showColumnsSql);
            while (resultSet1.next()) {
                table1.addColumns(new MySQLColumn(resultSet1.getString(1), resultSet1.getString(2), resultSet1.getString(3),
                        resultSet1.getString(4), resultSet1.getString(5), resultSet1.getString(6), resultSet1.getString(7),
                        resultSet1.getString(8), resultSet1.getString(9), resultSet1.getString(10), resultSet1.getString(11)));

                table.addColumns(new MySQLColumn(resultSet1.getString(1), resultSet1.getString(2), resultSet1.getString(3),
                        resultSet1.getString(4), resultSet1.getString(5), resultSet1.getString(6), resultSet1.getString(7),
                        resultSet1.getString(8), resultSet1.getString(9), resultSet1.getString(10), resultSet1.getString(11),
                        resultSet1.getString(12), resultSet1.getString(13), resultSet1.getString(14), resultSet1.getString(15),
                        resultSet1.getString(16), resultSet1.getString(17), resultSet1.getString(18), resultSet1.getString(19)));
            }
            PreparedStatement showFkeysStatement = connection.prepareStatement(
                    "SELECT  K.CONSTRAINT_NAME, C.CONSTRAINT_TYPE, K.TABLE_NAME, K.COLUMN_NAME,   K.REFERENCED_TABLE_NAME,  K.REFERENCED_COLUMN_NAME " +
                            " FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS  K , INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS C" +
                            " WHERE  K.TABLE_NAME = C.TABLE_NAME AND K.CONSTRAINT_NAME = C.CONSTRAINT_NAME AND K.TABLE_NAME = ?;"

            );
            showFkeysStatement.setString(1, tableName);
            ResultSet resultSet2 = showFkeysStatement.executeQuery();
            while (resultSet2.next()) {
                table.addConstraint(
                        new MySQLConstraint(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getString(4),
                                resultSet.getString(5),
                                resultSet.getString(6)));
            }
           schema.addTables(table);
        }

        return schema;
    }
}
