package com.aca.ddlanalyzer;

import com.aca.components.Column;
import com.aca.components.Constraint;
import com.aca.components.Schema;
import com.aca.components.Table;

import java.sql.*;

/**
 * Created by home on 1/12/2019.
 */
public class MySQLDDLAnalyzerImpl implements DDLAnalyzer {
    @Override
    public Schema getSchema() throws SQLException {
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/test2",
                "root",
                "root"
        );

        String showTablesSql = "SHOW TABLES;";
        Statement showTablesStatement = connection.createStatement();
        ResultSet resultSet = showTablesStatement.executeQuery(showTablesSql);

        Schema schema = new Schema();
        while (resultSet.next()) {

            String tableName = resultSet.getString(1);

           Table table = new Table(tableName);
           String showColumnsSql = "SHOW COLUMNS FROM " + tableName + ";";
            Statement showColumnsStatement = connection.createStatement();
            ResultSet resultSet1 = showColumnsStatement.executeQuery(showColumnsSql);
            while (resultSet1.next()) {

                table.addColumns(new Column(resultSet1.getString(1), resultSet1.getString(2), resultSet1.getString(3),
                        resultSet1.getString(4), resultSet1.getString(5), resultSet1.getString(6)));
            }
            PreparedStatement showFkeysStatement = connection.prepareStatement(
                    "SELECT  COLUMN_NAME,  CONSTRAINT_NAME,  REFERENCED_TABLE_NAME,  REFERENCED_COLUMN_NAME " +
                            "  FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
                            "  WHERE TABLE_NAME = ? " +
                            "AND REFERENCED_TABLE_NAME IS NOT NULL AND REFERENCED_COLUMN_NAME IS NOT NULL;"
            );
            showFkeysStatement.setString(1, tableName);
            ResultSet resultSet2 = showFkeysStatement.executeQuery();
           while (resultSet2.next()) {
               table.addConstraint(new Constraint(resultSet2.getString(1), resultSet2.getString(2),
                       resultSet2.getString(3), resultSet2.getString(4)));
           }
           schema.addTables(table);
        }

        return schema;
    }
}
