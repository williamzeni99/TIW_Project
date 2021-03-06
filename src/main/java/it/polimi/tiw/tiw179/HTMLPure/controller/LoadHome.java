package it.polimi.tiw.tiw179.HTMLPure.controller;

import it.polimi.tiw.tiw179.HTMLPure.DAO.TopicDAO;
import it.polimi.tiw.tiw179.ErrorMessage;
import it.polimi.tiw.tiw179.Utilities;
import it.polimi.tiw.tiw179.HTMLPure.beans.Topic;
import it.polimi.tiw.tiw179.HTMLPure.beansForm.AddTopicForm;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "LoadHome", value = "/LoadHome")
public class LoadHome extends HttpServlet {
    Connection connection;
    TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        connection = Utilities.MakeDBConnection(getServletContext());
        templateEngine = Utilities.createTemplateEngine(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TopicDAO topicDAO= new TopicDAO(connection);
        Topic topic= null;
        AddTopicForm form= (AddTopicForm) request.getAttribute("form");
        ArrayList<Integer> redtopics= (ArrayList<Integer>) request.getAttribute("redTopics");
        String idToMove= (String) request.getAttribute("idToMove");
        WebContext ctx= new WebContext(request,response,getServletContext(),response.getLocale());
        ArrayList<Integer> listId = null;
        try {
            topic = topicDAO.getTopics();
            listId=topicDAO.getTopicsList(0);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ErrorMessage.QueryNotGood.getMessage());
        }
        ctx.setVariable("topics",topic.getSubtopics());
        ctx.setVariable("redTopics", new ArrayList<>());
        ctx.setVariable("listId", listId);

        if(form!=null){
            ctx.setVariable("id", form.getIdFather());
            ctx.setVariable("body", form.getBody());
            ctx.setVariable("errorMsg", form.getErrorMessage().getMessage());
        }
        if(redtopics!=null){
            ctx.setVariable("redTopics", redtopics);
        }
        if(idToMove!=null){
            ctx.setVariable("idToMove", idToMove);
        }


        String path="/WEB-INF/HTMLPure/HomePage.html";
        templateEngine.process(path,ctx,response.getWriter());
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
