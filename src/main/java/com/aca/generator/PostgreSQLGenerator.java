package com.aca.generator;

import com.aca.components.PostgreSQLColumn;
import com.aca.components.PostgreSQLTable;
import com.aca.components.Schema;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by home on 1/21/2019.
 */
public class PostgreSQLGenerator implements SQLGenerator {
    private static Connection connection = null;
    private static Statement statement = null;


}
