package com.aca.dataMigrator;

import com.aca.components.table.MySQLTable;
import com.aca.components.table.PostgreSQLTable;
import com.aca.helper.JdbcUrlHelper;

public class DataMigratorFactory {

    public static DataMigrator getDataMigrator(String urlFrom, String urlTo , String usernameFrom, String passwordFrom){

        String dbFrom = JdbcUrlHelper.getDbType(urlFrom);
        String dbTo = JdbcUrlHelper.getDbType(urlTo);
        switch (dbFrom) {
            case "postgresql": {
                switch (dbTo) {
                    case "mysql":
                        return new DataMigrator<MySQLTable>(urlFrom, usernameFrom, passwordFrom );
                    default:
                        throw new UnsupportedOperationException("Unsupported RDBMS");
                }
            }

            case "mysql": {
                switch (dbTo) {
                    case "postgresql":
                        return new DataMigrator<PostgreSQLTable>(urlFrom, usernameFrom, passwordFrom);
                    default:
                        throw new UnsupportedOperationException("Unsupported RDBMS");
                }
            }

            /*case "oracle": {}
            case "sqlserver":{}
            */
            default:
                throw new UnsupportedOperationException("Unsupported RDBMS");
        }
    }

}
