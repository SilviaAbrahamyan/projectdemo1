package com.aca.components;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 1/12/2019.
 */
public class Schema<T> {
    private List<T> tables;

    public Schema() {
        this.tables = new ArrayList<>();
    }

    public List<T> getTables() {
        return tables;
    }

    public void addTables(T table) {
        tables.add(table);
    }
}
