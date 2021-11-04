package it.polimi.tiw.tiw179.RIA.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.tiw.tiw179.ErrorMessage;
import it.polimi.tiw.tiw179.RIA.DAO.TopicJSDAO;
import it.polimi.tiw.tiw179.RIA.beans.TopicJS;
import it.polimi.tiw.tiw179.Utilities;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "DownloadTopicsJS", value = "/DownloadTopicsJS")
@MultipartConfig
public class DownloadTopicsJS extends HttpServlet {
    private Connection connection;

    @Override
    public void destroy() {
        try {
            Utilities.closeDBConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() throws ServletException {
        connection= Utilities.MakeDBConnection(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TopicJSDAO topicJSDAO= new TopicJSDAO(connection);
        try {
            TopicJS topic0= topicJSDAO.getTopics();
            Utilities.sendJson(topic0, response);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println(ErrorMessage.QueryNotGood.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
