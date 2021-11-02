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

@WebServlet(name = "GetOptionsTopicJS", value = "/GetOptionsTopicJS")
@MultipartConfig
public class GetOptionsTopicJS extends HttpServlet {

    private Connection connection;

    @Override
    public void init() throws ServletException {
        connection=Utilities.MakeDBConnection(getServletContext());
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
        TopicJSDAO dao= new TopicJSDAO(connection);
        ArrayList<Integer> ids;
        try {
            ids= dao.getTopicIds();
            Utilities.sendJson(ids, response);
        } catch (SQLException|IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println(ErrorMessage.QueryNotGood.getMessage());
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
