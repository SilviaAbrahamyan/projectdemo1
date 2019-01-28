package com.aca.components.table;

import com.aca.components.constraint.MySQLConstraint;
import com.aca.components.column.MySQLColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 1/15/2019.
 */
public class MySQLTable {
    private String name;
    private String type;
    private boolean enable;

    private List<MySQLColumn> columns;
    private List<MySQLConstraint> constraints;


    public MySQLTable(String name, String type){
        this.name = name;
        this.type = type;
        this.columns = new ArrayList<>();
        this.constraints = new ArrayList<>();
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enabled) {
        this.enable = enabled;
    }

    public void addColumn(MySQLColumn column){
        this.columns.add(column);
    }

    public void addConstraint (MySQLConstraint constraint){
        this.constraints.add(constraint);
    }

    public List<MySQLColumn> getColumns() {
        return new ArrayList<>(columns);
    }

    public void setColumns(List<MySQLColumn> columns) {
        this.columns = columns;
    }

    public List<MySQLConstraint> getConstraints() {
        return new ArrayList<>(constraints);
    }

    public void setConstraints(List<MySQLConstraint> constraints) {
        this.constraints = constraints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MySQLTable{" +
                "name='" + name + '\'' +
                '}' + "\n";
    }


}
