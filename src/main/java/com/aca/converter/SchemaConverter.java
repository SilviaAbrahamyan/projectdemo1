package com.aca.converter;

import com.aca.components.*;
import com.aca.components.util.Nullable;
import com.aca.components.util.Type;

/**
 * Created by home on 1/18/2019.
 */
public class SchemaConverter {
    private static Schema<PostgreSQLTable> postgreSQLTableSchema;
    private static PostgreSQLTable pTable;
    private static PostgreSQLColumn pColumn;
    private static PostgreSQLConstraint pConstraint;
    private static Schema<MySQLTable> mySQLTableSchema;
    private static MySQLTable mTable;
    private static MySQLColumn mColumn;
    private static MySQLConstraint mConstraint;

    public static Schema<PostgreSQLTable> convertFromMySQLtoPostgrSQL(Schema<MySQLTable> schema) {

        postgreSQLTableSchema = new Schema<>();
        schema.getTables().forEach(t -> {
            pTable = new PostgreSQLTable(t.getName(), t.getType());
            t.getColumns().forEach(t1 -> {
               String type = t1.getDataType().toUpperCase();
               type =  Type.getDataType("mysql","postgresql",type);
                pColumn = new PostgreSQLColumn(t1.getName(),t1.getOrdinalPosition(), t1.getDefaultValue(),
                        t1.getIsNullable(),type,t1.getCharacterMaximumLength(), t1.getCharacterOctetLength(),
                        t1.getNumericPrecision(), t1.getNumericScale());
                pTable.addColumn(pColumn);
            });
            t.getConstraints().forEach(t1 -> {
                pConstraint = new PostgreSQLConstraint(t1.getName(), t1.getType(), t1.getTable(), t1.getColumn(),
                        t1.getReferencedTable(), t1.getReferencedColumn());
                pTable.addConstraint(pConstraint);
            });
            postgreSQLTableSchema.addTables(pTable);

        });
        return postgreSQLTableSchema;
    }

    public static Schema<MySQLTable> convertFromPostgrSQLtoMySQL(Schema<PostgreSQLTable> schema) {
        mySQLTableSchema = new Schema<>();
        schema.getTables().forEach(t -> {
            mTable = new MySQLTable(t.getName(), t.getType());
            t.getColumns().forEach(t1 -> {
                String type = t1.getDataType().toUpperCase();
                type =  Type.getDataType("postgresql", "mysql",type);
                        mColumn = new MySQLColumn(t1.getName(),t1.getOrdinalPosition(),t1.getDefaultValue(),
                                t1.getIsNullable(),type,t1.getCharacterMaximumLength(),t1.getCharacterOctetLength(),
                                t1.getNumericPrecision(),t1.getNumericScale());
                        mTable.addColumn(mColumn);
                    }
            );
            t.getConstraints().forEach(t1 ->{
                mConstraint = new MySQLConstraint(t1.getName(), t1.getType(), t1.getTable(), t1.getColumn(),
                        t1.getReferencedTable(), t1.getReferencedColumn());
                mTable.addConstraint(mConstraint);
            });
            mySQLTableSchema.addTables(mTable);
        });
        return mySQLTableSchema;
    }
}
