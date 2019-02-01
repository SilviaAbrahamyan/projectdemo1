package com.aca.components.table;

import com.aca.components.constraint.MySQLConstraint;
import com.aca.components.column.MySQLColumn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by home on 1/15/2019.
 */
public class MySQLTable implements Table{
    private String name;
    private String type;
    private boolean enabled;

    private List<MySQLColumn> columns;
    private List<MySQLConstraint> constraints;


    public MySQLTable(String name, String type){
        this.name = name;
        this.type = type;
        this.columns = new ArrayList<>();
        this.constraints = new ArrayList<>();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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


    public List<MySQLConstraint> getConstraintByPrimaryKey() {
        List<MySQLConstraint> constraintsByPK = new ArrayList<>();
        for (MySQLConstraint mySQLConstraint : constraints) {
            if ("PRIMARY KEY".equals(mySQLConstraint.getType())) {
                constraintsByPK.add(mySQLConstraint);
            }
        }

        return constraintsByPK.stream().distinct().collect(Collectors.toList());
    }

    public List<MySQLConstraint> getConstraintByForeignKey() {
        List<MySQLConstraint> constraintsByFK = new ArrayList<>();
        for (MySQLConstraint mySQLConstraint : constraints) {
            if ("FOREIGN KEY".equals(mySQLConstraint.getType())) {
                constraintsByFK.add(mySQLConstraint);
            }
        }
        return constraintsByFK;
    }
}
