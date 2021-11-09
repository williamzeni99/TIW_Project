package it.polimi.tiw.tiw179.RIA.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.tiw.tiw179.ErrorMessage;
import it.polimi.tiw.tiw179.RIA.DAO.TopicJSDAO;
import it.polimi.tiw.tiw179.RIA.beans.Move;
import it.polimi.tiw.tiw179.Utilities;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

@WebServlet(name = "StoreData", value = "/StoreData")
@MultipartConfig
public class StoreData extends HttpServlet {
    private Connection connection;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        connection= Utilities.MakeDBConnection(getServletContext());
        gson= new GsonBuilder().create();
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
        BufferedReader reader=request.getReader();
        String line=reader.readLine();
        StringBuilder sb = new StringBuilder();
        while (line != null){
            sb.append(line);
            line=reader.readLine();
        }
        Type movesArrayList = new TypeToken<ArrayList<Move>>(){}.getType();
        ArrayList<Move> moves = gson.fromJson(sb.toString(), movesArrayList);

        if(moves==null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(ErrorMessage.NoChangesDetected.getMessage());
            return;
        }
        TopicJSDAO dao= new TopicJSDAO(connection);

        for(Move x: moves){
            try {
                dao.moveTopic(x.getStartId(), x.getDestId());
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println(ErrorMessage.QueryNotGood.getMessage());
                return;
            }
        }
        response.setStatus(HttpServletResponse.SC_OK);

    }
}
