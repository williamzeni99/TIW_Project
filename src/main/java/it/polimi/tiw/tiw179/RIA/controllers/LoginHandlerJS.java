package it.polimi.tiw.tiw179.RIA.controllers;

import it.polimi.tiw.tiw179.RIA.DAO.UserJSDAO;
import it.polimi.tiw.tiw179.RIA.beans.UserJS;
import it.polimi.tiw.tiw179.Utilities;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "LoginHandlerJS", value = "/LoginHandlerJS")
@MultipartConfig
public class LoginHandlerJS extends HttpServlet {
    private Connection connection;

    @Override
    public void init() throws ServletException {
        connection= Utilities.MakeDBConnection(getServletContext());
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
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username= request.getParameter("username");
        String passwd= request.getParameter("password");

        if(username==null || username.isEmpty() || passwd==null || passwd.isEmpty()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Credential must be not null");
            return;
        }

        UserJSDAO userJSDAO= new UserJSDAO(connection);
        UserJS user= null;
        try {
            if(userJSDAO.findUser(username)){
                user=userJSDAO.match(username, passwd);
                if(user==null){
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().println("Invalid password");
                    return;
                }
                request.getSession().setAttribute("user", user);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().println(username);
            }
            else{
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().println("User not registered");
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Internal server error, retry later");
        }

    }
}
