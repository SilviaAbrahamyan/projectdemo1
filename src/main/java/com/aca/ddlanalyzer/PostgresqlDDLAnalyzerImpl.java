package com.aca.ddlanalyzer;

import com.aca.components.Column;
import com.aca.components.Constraint;
import com.aca.components.Schema;
import com.aca.components.Table;

import java.sql.*;

/**
 * Created by home on 1/12/2019.
 */
public class PostgresqlDDLAnalyzerImpl implements DDLAnalyzer {
    @Override
    public Schema getSchema() throws SQLException {
        Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres",
                "postgres",
                "root"
        );

        String showTablesSql = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'";
        Statement showTablesStatement = connection.createStatement();
        ResultSet resultSet = showTablesStatement.executeQuery(showTablesSql);

        Schema schema = new Schema();
        while (resultSet.next()) {

            String tableName = resultSet.getString(1);

            Table table = new Table(tableName);
            PreparedStatement showColumnsStatement = connection.prepareStatement(
                    "SELECT column_name, data_type, is_nullable, is_identity, column_default, is_generated " +
                            " FROM information_schema.columns " +
                            " WHERE table_schema = 'public' " +
                            " AND table_name   = ?;");
            showColumnsStatement.setString(1, tableName);
            ResultSet resultSet1 = showColumnsStatement.executeQuery();
            while (resultSet1.next()) {

                table.addColumns(new Column(resultSet1.getString(1), resultSet1.getString(2), resultSet1.getString(3),
                        resultSet1.getString(4), resultSet1.getString(5), resultSet1.getString(6)));
            }

            PreparedStatement showFkeysStatement = connection.prepareStatement(
                    "SELECT  tc.table_name, kcu.column_name, tc.constraint_name, ccu.table_name, ccu.column_name " +
                            " FROM information_schema.table_constraints AS tc " +
                            " JOIN information_schema.key_column_usage AS kcu" +
                            " ON tc.constraint_name = kcu.constraint_name" +
                            " AND tc.table_schema = kcu.table_schema" +
                            " JOIN information_schema.constraint_column_usage AS ccu" +
                            " ON ccu.constraint_name = tc.constraint_name" +
                            " AND ccu.table_schema = tc.table_schema" +
                            " WHERE tc.constraint_type = 'FOREIGN KEY' AND tc.table_name= ? ;");

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
