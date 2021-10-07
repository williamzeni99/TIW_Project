package it.polimi.tiw.tiw179.controller;

import it.polimi.tiw.tiw179.Utilities;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;

@WebServlet(name = "MoveTopic", value = "/MoveTopic")
public class MoveTopic extends HttpServlet {
    Connection connection;

    @Override
    public void init() throws ServletException {
        connection= Utilities.MakeDBConnection(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("idToMove");
        if(Utilities.isGood(id)){
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Tutto OK --> id: "+id);
        }
    }

}
