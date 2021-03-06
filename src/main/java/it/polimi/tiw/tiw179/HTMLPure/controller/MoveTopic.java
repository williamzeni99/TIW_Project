package it.polimi.tiw.tiw179.HTMLPure.controller;

import it.polimi.tiw.tiw179.HTMLPure.DAO.TopicDAO;
import it.polimi.tiw.tiw179.ErrorMessage;
import it.polimi.tiw.tiw179.Utilities;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

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
        if(!Utilities.isGood(id)){
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invalid id request");
            return;
        }
        TopicDAO dao= new TopicDAO(connection);
        ArrayList<Integer> topiclist;
        ArrayList<Integer> ids= new ArrayList<>();
        ids.add(Integer.parseInt(id));
        try {
            if(!dao.idExist(ids)){
                response.sendError(HttpServletResponse.SC_NOT_FOUND, ErrorMessage.TopicNotFound.getMessage());
                return;
            }
            topiclist= dao.getTopicsList(Integer.parseInt(id));
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ErrorMessage.QueryNotGood.getMessage());
            return;
        }

        request.setAttribute("redTopics", topiclist);
        request.setAttribute("idToMove", id);
        String path = "/LoadHome";
        RequestDispatcher dispatcher= request.getRequestDispatcher(path);
        dispatcher.forward(request,response);
    }

    @Override
    public void destroy() {
        try {
            Utilities.closeDBConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
