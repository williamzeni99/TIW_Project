package it.polimi.tiw.tiw179.controller;

import it.polimi.tiw.tiw179.DAO.TopicDAO;
import it.polimi.tiw.tiw179.ErrorMessage;
import it.polimi.tiw.tiw179.Utilities;
import it.polimi.tiw.tiw179.beansForm.AddTopicForm;
import org.thymeleaf.TemplateEngine;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "AddTopic", value = "/AddTopic")
public class AddTopic extends HttpServlet {
    private Connection connection;
    private TemplateEngine templateEngine;
    private static final String bodyParameter="body";
    private static final String idFatherParameter="idFather";

    @Override
    public void init() throws ServletException {
        connection=Utilities.MakeDBConnection(getServletContext());
        templateEngine=Utilities.createTemplateEngine(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TopicDAO topicDAO = new TopicDAO(connection);
        AddTopicForm form = new AddTopicForm(request.getParameter(bodyParameter), request.getParameter(idFatherParameter));

        if(form.isValid()){
            try {
                topicDAO.addTopicDB(request.getParameter(idFatherParameter), request.getParameter(bodyParameter));
                response.sendRedirect(getServletContext().getContextPath()+"/LoadHome");
                return;
            } catch (SQLException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ErrorMessage.QueryNotGood.getMessage());
                return;
            }catch (IllegalArgumentException e){
                form.setErrorMessage(ErrorMessage.NoMoreTopic);
            }
        }
        else{
            if(form.checkErrorId()){
                form.setErrorMessage(ErrorMessage.TopicNotFound);
            }
            else{
                form.setErrorMessage(ErrorMessage.TopicNameMissing);
            }
        }

        // TODO: 10/6/21 questa parte non funziona gestione errore inserimento 
        request.setAttribute("id", form.getIdFather());
        request.setAttribute("body", form.getBody());
        String path = getServletContext().getContextPath()+"/LoadHome";
        RequestDispatcher dispatcher= request.getRequestDispatcher(path);
        dispatcher.forward(request,response);

    }
}
