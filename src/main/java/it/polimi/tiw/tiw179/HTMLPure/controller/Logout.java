package it.polimi.tiw.tiw179.HTMLPure.controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "Logout", value = "/Logout")
public class Logout extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session= request.getSession();
        if(session!=null){
            session.invalidate();
        }
        response.sendRedirect(getServletContext().getContextPath()+"/LoginHandler");

    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
