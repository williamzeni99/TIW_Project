package it.polimi.tiw.tiw179.RIA.controllers;

import it.polimi.tiw.tiw179.Utilities;
import org.thymeleaf.TemplateEngine;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "LoginHandlerJS", value = "/LoginHandlerJS")
public class LoginHandlerJS extends HttpServlet {
    private Connection connection;
    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        connection= Utilities.MakeDBConnection(getServletContext());
        templateEngine= Utilities.createTemplateEngine(getServletContext());
    }

    @Override
    public void destroy() {
        try {
            Utilities.closeDBConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
