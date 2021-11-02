package it.polimi.tiw.tiw179.RIA.controllers;

import it.polimi.tiw.tiw179.ErrorMessage;
import it.polimi.tiw.tiw179.RIA.DAO.TopicJSDAO;
import it.polimi.tiw.tiw179.Utilities;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "AddTopicJS", value = "/AddTopicJS")
@MultipartConfig
public class AddTopicJS extends HttpServlet {
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
        String topicName= request.getParameter("topicName"); // TODO: 10/25/21 HERE SHOULD NOT BE NULL WHY IS NULL????? 
        String idFather= request.getParameter("idFather");

        if(topicName==null || topicName.isEmpty()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(ErrorMessage.TopicNameMissing.getMessage());
            return;
        }

        if(!Utilities.isGood(idFather)){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(ErrorMessage.TopicNotInt.getMessage());
        }

        TopicJSDAO topicJSDAO= new TopicJSDAO(connection);
        ArrayList<Integer> ids= new ArrayList<>();
        ids.add(Integer.parseInt(idFather));


        try {
            if(topicJSDAO.idExist(ids)){
                topicJSDAO.addTopicDB(idFather, topicName);
                response.setStatus(HttpServletResponse.SC_OK);
            }
            else{
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().println(ErrorMessage.TopicNotFound.getMessage());
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println(ErrorMessage.QueryNotGood.getMessage());
        } catch (IllegalArgumentException e){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().println(ErrorMessage.NoMoreTopic.getMessage());
        }


    }
}
