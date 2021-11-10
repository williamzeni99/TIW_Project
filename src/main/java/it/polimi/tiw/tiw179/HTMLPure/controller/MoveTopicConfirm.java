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

@WebServlet(name = "MoveTopicConfirm", value = "/MoveTopicConfirm")
public class MoveTopicConfirm extends HttpServlet {
    Connection connection;

    @Override
    public void init() throws ServletException {
        connection= Utilities.MakeDBConnection(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TopicDAO topicDAO= new TopicDAO(connection);
        String id = request.getParameter("idToMove");
        String idW = request.getParameter("idWhereToMove");
        if(!Utilities.isGood(id) && !Utilities.isGood(idW)){
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ErrorMessage.TopicNotInt.getMessage());
            return;
        }

        try {
            ArrayList<Integer> ids= new ArrayList<>();
            ids.add(Integer.parseInt(id));
            ids.add(Integer.parseInt(idW));
            if(!topicDAO.idExist(ids)){
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ErrorMessage.TopicNotFound.getMessage());
                return;
            }

            if(topicDAO.isMySon(ids.get(0), ids.get(1))){
                response.sendError(HttpServletResponse.SC_FORBIDDEN, ErrorMessage.CantMoveinSon.getMessage());
                return;
            }

            topicDAO.moveTopic(Integer.parseInt(id),Integer.parseInt(idW));
            String path = getServletContext().getContextPath() + "/LoadHome";
            response.sendRedirect(path);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ErrorMessage.QueryNotGood.getMessage());
        } catch (IllegalArgumentException e){
            response.sendError(HttpServletResponse.SC_FORBIDDEN,ErrorMessage.NoMoreTopic.getMessage());
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
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
