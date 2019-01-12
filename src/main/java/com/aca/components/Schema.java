package com.aca.components;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 1/12/2019.
 */
public class Schema {
    private List<Table> tables;

    public Schema() {
        this.tables = new ArrayList<>();
    }
    public List<Table> getTables() {
        return tables;
    }

    public void addTables(Table table) {
        tables.add(table);
    }
}
