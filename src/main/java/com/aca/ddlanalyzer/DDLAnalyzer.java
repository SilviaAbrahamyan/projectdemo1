package com.aca.ddlanalyzer;

import com.aca.components.Schema;

import java.sql.SQLException;

/**
 * Created by home on 1/12/2019.
 */
@FunctionalInterface
public interface DDLAnalyzer {
    Schema getSchema(String jdbcUrl, String username, String password) throws SQLException;
}
