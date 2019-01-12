package com.aca.components;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private String Name;
    private List<Column> columns;
    private List<Constraint> constraints;

    public Table(String name) {
        Name = name;
        this.columns = new ArrayList<>();
        this.constraints = new ArrayList<>();
    }

    public String getName() {
        return Name;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }


    public void addColumns(Column column) {
        columns.add(column);
    }


    public void addConstraint(Constraint constraint) {
        constraints.add(constraint);
    }
}
