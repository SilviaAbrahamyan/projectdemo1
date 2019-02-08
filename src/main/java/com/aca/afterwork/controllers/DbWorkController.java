package com.aca.afterwork.controllers;


import com.aca.afterwork.helper.Helper;
import com.aca.components.Schema;
import com.aca.ddlanalyzer.DDlAnalyzerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController

public class DbWorkController {


    private String urlFrom;
    private String usernameFrom;
    private String passwordFrom;

    @GetMapping("/")
    public ModelAndView homePage() {
        return new ModelAndView("index.html");
    }

    @PostMapping("/")
    public ModelAndView urlInput(HttpServletRequest httpServletRequest){
        this.urlFrom = httpServletRequest.getParameter("urlFrom");
        this.usernameFrom = httpServletRequest.getParameter("usernameFrom");
        this.passwordFrom = httpServletRequest.getParameter("passwordFrom");
        return new ModelAndView("redirect:/schema");
    }

    @GetMapping("/schema")
    public ModelAndView showTables(HttpServletRequest httpServletRequest) throws SQLException {
        ModelAndView modelAndView = new ModelAndView("view.html");
        List tables = Helper.getTables(urlFrom, usernameFrom, passwordFrom);
        modelAndView.addObject("tables", tables);
        return modelAndView;
    }
}
