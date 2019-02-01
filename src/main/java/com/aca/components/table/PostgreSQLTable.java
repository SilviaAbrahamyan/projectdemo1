package com.aca.components.table;

import com.aca.components.constraint.PostgreSQLConstraint;
import com.aca.components.column.PostgreSQLColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 1/15/2019.
 */
public class PostgreSQLTable implements Table{

    private String name;
    private String type;
    private boolean enabled;

    private List<PostgreSQLColumn> columns;
    private List<PostgreSQLConstraint> constraints;

    public PostgreSQLTable(String name, String type){
        this.name = name;
        this.type = type;
        this.columns = new ArrayList<>();
        this.constraints = new ArrayList<>();
    }

    public void addColumn(PostgreSQLColumn column){
        this.columns.add(column);
    }

    public void addConstraint (PostgreSQLConstraint constraint){
        this.constraints.add(constraint);
    }

    public List<PostgreSQLColumn> getColumns() {
        return new ArrayList<>(columns);
    }

    public void setPostgreSQLColumns(List<PostgreSQLColumn> postgreSQLColumns) {
        this.columns = postgreSQLColumns;
    }

    public List<PostgreSQLConstraint> getConstraints() {
        return new ArrayList<>(constraints);
    }

    public void setPostgreSQLConstraints(List<PostgreSQLConstraint> postgreSQLConstraints) {
        this.constraints = postgreSQLConstraints;
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
        return "PostgreSQLTable{" +
                "name='" + name + '\'' +
                '}' + "\n";
    }

    public  List<PostgreSQLConstraint> getConstraintByPrimaryKey(){
        List<PostgreSQLConstraint> constraintsByPK = new ArrayList<>();
        for(PostgreSQLConstraint postgreSQLConstraint: constraints){
            if("PRIMARY KEY".equals(postgreSQLConstraint.getType())){
                constraintsByPK.add(postgreSQLConstraint);
            }
        }
        return constraintsByPK;
    }
    public  List<PostgreSQLConstraint> getConstraintByForeignKey(){
        List<PostgreSQLConstraint> constraintsByFK = new ArrayList<>();
        for(PostgreSQLConstraint postgreSQLConstraint: constraints){
            if("FOREIGN KEY".equals(postgreSQLConstraint.getType())){
                constraintsByFK.add(postgreSQLConstraint);
            }
        }
        return constraintsByFK;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
