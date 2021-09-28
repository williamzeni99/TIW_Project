package it.polimi.tiw.tiw179.controller;

import it.polimi.tiw.tiw179.DAO.UserDAO;
import it.polimi.tiw.tiw179.ErrorMessage;
import it.polimi.tiw.tiw179.beans.User;
import it.polimi.tiw.tiw179.beansForm.LoginFom;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet(name = "LoginHandler", value = "/LoginHandler")
public class LoginHandler extends HttpServlet {
    private Connection connection;
    private static final String parameterName="username";
    private static final String parameterPass="password";

    @Override
    public void init() throws ServletException {
        super.init();
        ServletContext servletContext= getServletContext();
        String driver = servletContext.getInitParameter("dbDriver");
        String url = servletContext.getInitParameter("dbUrl");
        String passwd= servletContext.getInitParameter("dbPasswd");
        String userdb=servletContext.getInitParameter("dbUser");
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new UnavailableException(ErrorMessage.Driver.getMessage());
        }
        try {
            connection= DriverManager.getConnection(url,userdb,passwd);
        } catch (SQLException e) {
            throw new UnavailableException(ErrorMessage.ConnectionDB.getMessage());
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       /* UserDAO userDAO= new UserDAO(connection);
        LoginFom loginFom= new LoginFom(request.getParameter(parameterName),request.getParameter(parameterPass));

        if(loginFom.isFormValid()){
            try {
                if(userDAO.findUser(request.getParameter(parameterName))){
                    if(userDAO.match(request.getParameter(parameterName), request.getParameter(parameterPass))){
                        RequestDispatcher dispatcher= request.getRequestDispatcher("/WEB-INF/templates/HomePage.html");
                        request.setAttribute("LoginForm", loginFom);
                        dispatcher.forward(request,response);
                    }
                    else{
                        loginFom.setErrorMessagePasswd(ErrorMessage.UserNotMatch);
                    }
                }
                else{
                    loginFom.setErrorMessageUser(ErrorMessage.UserNotFound);
                }
            } catch (SQLException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ErrorMessage.QueryNotGood.getMessage());
            }


        }

        try {
            if(!userDAO.findUser(request.getParameter("username"))){

            }
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ErrorMessage.QueryNotGood.getMessage());
            return;
        }

        */
    }
}
