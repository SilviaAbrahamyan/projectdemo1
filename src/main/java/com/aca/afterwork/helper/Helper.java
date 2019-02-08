package com.aca.afterwork.helper;

import com.aca.ddlanalyzer.DDlAnalyzerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by home on 2/3/2019.
 */
public class Helper {
    public static List getTables(String url, String username, String password) throws SQLException {

      return  DDlAnalyzerFactory.
                getAnalyzer(url, username, password)
                .getSchema().getTables();
    }


}
