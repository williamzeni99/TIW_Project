package it.polimi.tiw.tiw179.controller;

import it.polimi.tiw.tiw179.DAO.UserDAO;
import it.polimi.tiw.tiw179.ErrorMessage;
import it.polimi.tiw.tiw179.Utilities;
import it.polimi.tiw.tiw179.beans.User;
import it.polimi.tiw.tiw179.beansForm.LoginFom;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "LoginHandler", value = "/LoginHandler")
public class LoginHandler extends HttpServlet {
    private Connection connection;
    private TemplateEngine templateEngine;
    private static final String parameterName="username";
    private static final String parameterPass="password";

    @Override
    public void init() throws ServletException {
        connection= Utilities.MakeDBConnection(getServletContext());
        templateEngine=Utilities.createTemplateEngine(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getSession().getAttribute("user")!=null){
            response.sendRedirect(getServletContext().getContextPath()+"/LoadHome");
            return;
        }
        String path="/WEB-INF/templates/LoginPage.html";
        WebContext ctx= new WebContext(request, response, getServletContext(), request.getLocale());
        templateEngine.process(path,ctx, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDAO userDAO= new UserDAO(connection);
        LoginFom loginFom= new LoginFom(request.getParameter(parameterName),request.getParameter(parameterPass));

        if(loginFom.isFormValid()){
            try {
                if(userDAO.findUser(loginFom.getUsername())){
                    User user= userDAO.match(loginFom.getUsername(),request.getParameter(parameterPass));
                    if(user!=null){
                        request.getSession().setAttribute("user", user);
                        String path = getServletContext().getContextPath() + "/LoadHome";
                        response.sendRedirect(path);
                        return;
                    }
                    else{
                        loginFom.setError(ErrorMessage.UserNotMatch);
                    }
                }
                else{
                    loginFom.setError(ErrorMessage.UserNotFound);
                }
            } catch (SQLException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ErrorMessage.QueryNotGood.getMessage());
                return;
            }


        }
        else {
            if (loginFom.checkUserError()){
                loginFom.setError(ErrorMessage.UsernameRequired);
            }
            else{
                loginFom.setError(ErrorMessage.PasswdRequired);
            }
        }

        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        ctx.setVariable("errorMsg", loginFom.getErrorMessage());
        ctx.setVariable("username", loginFom.getUsername());
        String path = "/WEB-INF/templates/LoginPage.html";
        templateEngine.process(path, ctx, response.getWriter());
    }

    @Override
    public void destroy() {
        if(connection!=null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
