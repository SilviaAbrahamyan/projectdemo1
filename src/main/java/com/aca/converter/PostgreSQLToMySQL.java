package com.aca.converter;


import com.aca.components.*;
import com.aca.components.util.Nullable;
import com.aca.components.util.Type;

public class PostgreSQLToMySQL implements Converter<PostgreSQLTable, MySQLTable> {
    @Override
    public Schema<MySQLTable> convert(Schema<PostgreSQLTable> schemaFrom) {
        Schema<MySQLTable> schemaTo = new Schema<>();

        for (PostgreSQLTable tablefrom : schemaFrom.getTables()) {
            MySQLTable tableTo = new MySQLTable(tablefrom.getName(), tablefrom.getType());
            for (PostgreSQLColumn columnFrom : tablefrom.getColumns()) {
                MySQLColumn columnTo = new MySQLColumn(
                        columnFrom.getName(), columnFrom.getOrdinalPosition(), columnFrom.getDefaultValue(),
                        Nullable.getIsNullable(columnFrom.getIsNullable()), Type.getDataType("postgresql","mysql",columnFrom.getDataType().toUpperCase()), columnFrom.getCharacterMaximumLength(),
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
