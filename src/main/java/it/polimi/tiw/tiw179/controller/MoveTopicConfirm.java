package it.polimi.tiw.tiw179.controller;

import it.polimi.tiw.tiw179.DAO.TopicDAO;
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
        if(Utilities.isGood(id) && Utilities.isGood(idW)){
            try {
                ArrayList<Integer> ids= new ArrayList<>();
                ids.add(Integer.parseInt(id));
                ids.add(Integer.parseInt(idW));
                if(topicDAO.idExist(ids)){
                    topicDAO.moveTopic(Integer.parseInt(id),Integer.parseInt(idW));
                    String path = getServletContext().getContextPath() + "/LoadHome";
                    response.sendRedirect(path);
                    return;
                }
                else{
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ErrorMessage.TopicNotFound.getMessage());
                }
            } catch (SQLException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ErrorMessage.QueryNotGood.getMessage());
            }

        }else{
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ErrorMessage.TopicNotInt.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
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
