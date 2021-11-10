package it.polimi.tiw.tiw179.RIA.controllers;

import it.polimi.tiw.tiw179.ErrorMessage;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LogoutJS", value = "/LogoutJS")
public class LogoutJS extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session= request.getSession();
        if(session!=null){
            session.invalidate();
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
