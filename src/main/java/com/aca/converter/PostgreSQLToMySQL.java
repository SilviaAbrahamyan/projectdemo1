package com.aca.converter;


import com.aca.components.*;
import com.aca.components.column.MySQLColumn;
import com.aca.components.column.PostgreSQLColumn;
import com.aca.components.constraint.MySQLConstraint;
import com.aca.components.constraint.PostgreSQLConstraint;
import com.aca.components.table.MySQLTable;
import com.aca.components.table.PostgreSQLTable;
import com.aca.components.util.Type;

public class PostgreSQLToMySQL implements Converter<PostgreSQLTable, MySQLTable> {
    @Override
    public Schema<MySQLTable> convert(Schema<PostgreSQLTable> schemaFrom) {
        Schema<MySQLTable> schemaTo = new Schema<>();

        for (PostgreSQLTable tablefrom : schemaFrom.getTables()) {
            MySQLTable tableTo = new MySQLTable(tablefrom.getName(), tablefrom.getType());
            for (PostgreSQLColumn columnFrom : tablefrom.getColumns()) {
                tableTo.setEnabled(tablefrom.isEnabled());
                MySQLColumn columnTo = new MySQLColumn(
                        columnFrom.getName(), columnFrom.getOrdinalPosition(), columnFrom.getDefaultValue(),
                        columnFrom.getIsNullable(), Type.getDataType("postgresql","mysql",columnFrom.getDataType().toUpperCase()), columnFrom.getCharacterMaximumLength(),
                        columnFrom.getCharacterOctetLength(), columnFrom.getNumericPrecision(), columnFrom.getNumericScale()
                );
                tableTo.addColumn(columnTo);
            }
            for (PostgreSQLConstraint constraintFrom : tablefrom.getConstraints()) {
                MySQLConstraint constraintTo = new MySQLConstraint(
                        constraintFrom.getName(), constraintFrom.getType(),
                        constraintFrom.getTable(), constraintFrom.getColumn(),
                        constraintFrom.getReferencedTable(), constraintFrom.getReferencedColumn()
                );
                tableTo.addConstraint(constraintTo);
            }
            schemaTo.addTables(tableTo);
        }
        return schemaTo;
    }
}
