package com.aca.generator;

import com.aca.components.Schema;

import java.sql.SQLException;

/**
 * Created by home on 1/25/2019.
 */
public interface SQLGenerator<T> {
    void migrate(Schema<T> schema) throws SQLException;
}