package it.polimi.tiw.tiw179.controller;

import it.polimi.tiw.tiw179.Utilities;
import org.thymeleaf.TemplateEngine;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;

@WebServlet(name = "LoadHome", value = "/LoadHome")
public class LoadHome extends HttpServlet {
    Connection connection;
    TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        connection = Utilities.MakeDBConnection(getServletContext());
        templateEngine = Utilities.createTemplateEngine(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
