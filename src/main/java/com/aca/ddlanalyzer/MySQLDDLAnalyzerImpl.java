package com.aca.ddlanalyzer;

import com.aca.components.*;
import com.aca.components.column.MySQLColumn;
import com.aca.components.constraint.MySQLConstraint;
import com.aca.components.table.MySQLTable;
import com.aca.components.util.Nullable;
import com.aca.helper.JdbcUrlHelper;

import java.sql.*;

/**
 * Created by home on 1/12/2019.
 */
public class MySQLDDLAnalyzerImpl implements DDLAnalyzer {

    private Connection connection;
    private String url;
    private String username;
    private String password;

    public MySQLDDLAnalyzerImpl(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Schema<MySQLTable> getSchema() throws SQLException {
        String user = "root";
        String password = "root";
        this.connection = DriverManager.getConnection(
                url,
                username,
                password
        );
        String dbName = JdbcUrlHelper.getDbName(url);

        PreparedStatement showTablesStatement = connection.prepareStatement(
                " SELECT TABLE_NAME, TABLE_TYPE " +
                        " FROM INFORMATION_SCHEMA.TABLES" +
                        " WHERE TABLE_SCHEMA = ?");
        showTablesStatement.setString(1, dbName);
        ResultSet resultSet = showTablesStatement.executeQuery();
        Schema<MySQLTable> schema = new Schema<MySQLTable>();
        while (resultSet.next()) {
            String tableName = resultSet.getString(1);
            MySQLTable table = new MySQLTable(resultSet.getString(1), resultSet.getString(2));
            PreparedStatement showColumnsStatement = connection.prepareStatement(
                    "SELECT COLUMN_NAME, ORDINAL_POSITION, COLUMN_DEFAULT, " +
                            "IS_NULLABLE, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, CHARACTER_OCTET_LENGTH, " +
                            "NUMERIC_PRECISION, NUMERIC_SCALE, COLUMN_TYPE, COLUMN_KEY " +
                            "FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ? ;");
            showColumnsStatement.setString(1, table.getName());
            ResultSet resultSet1 = showColumnsStatement.executeQuery();
            while (resultSet1.next()) {

                table.addColumn(new MySQLColumn(
                        resultSet1.getString(1),
                        resultSet1.getInt(2),
                        resultSet1.getString(3),
                        Nullable.valueOf(resultSet1.getString(4)),
                        resultSet1.getString(5),
                        resultSet1.getInt(6),
                        resultSet1.getInt(7),
                        resultSet1.getInt(8),
                        resultSet1.getInt(9)));
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
                                resultSet2.getString(1),
                                resultSet2.getString(2),
                                resultSet2.getString(3),
                                resultSet2.getString(4),
                                resultSet2.getString(5),
                                resultSet2.getString(6)));
            }
            schema.addTables(table);
        }

        return schema;
    }
}
