package com.aca.components;

public class Constraint {


    private String column;
    private String constraint;
    private String referencedTable;
    private String referencedColumn;

    public Constraint (String column, String constraint, String referencedTable, String referencedColumn) {

        this.column = column;
        this.constraint = constraint;
        this.referencedTable = referencedTable;
        this.referencedColumn = referencedColumn;
    }

    public String getColumn() {
        return column;
    }

    public String getConstraint() {
        return constraint;
    }

    public String getReferencedTable() {
        return referencedTable;
    }

    public String getReferencedColumn() {
        return referencedColumn;
    }
}
