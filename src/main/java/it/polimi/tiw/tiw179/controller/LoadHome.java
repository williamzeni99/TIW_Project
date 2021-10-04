package it.polimi.tiw.tiw179.controller;

import it.polimi.tiw.tiw179.Utilities;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

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
        WebContext ctx= new WebContext(request,response,getServletContext(),response.getLocale());
        String path="/WEB-INF/templates/HomePage.html";
        templateEngine.process(path,ctx,response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WebContext ctx= new WebContext(request,response,getServletContext(),response.getLocale());
        String path="/WEB-INF/templates/HomePage.html";
        templateEngine.process(path,ctx,response.getWriter());
    }
}
