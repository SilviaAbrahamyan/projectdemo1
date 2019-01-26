package com.aca.generator;

import com.aca.components.MySQLColumn;
import com.aca.components.MySQLTable;
import com.aca.components.Schema;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by home on 1/21/2019.
 */
public class MySQLGenerator implements SQLGenerator<MySQLTable> {
    private static Connection connection = null;
    private static Statement statement = null;


    @Override
    public void migrate(Schema<MySQLTable> schema) {

    }
}
