package it.polimi.tiw.tiw179.HTMLPure.controller;

import it.polimi.tiw.tiw179.HTMLPure.DAO.TopicDAO;
import it.polimi.tiw.tiw179.ErrorMessage;
import it.polimi.tiw.tiw179.Utilities;
import it.polimi.tiw.tiw179.HTMLPure.beansForm.AddTopicForm;
import org.thymeleaf.TemplateEngine;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

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
                ArrayList<Integer> ids= new ArrayList<>();
                ids.add(Integer.parseInt(request.getParameter(idFatherParameter)));
                if(topicDAO.idExist(ids)){
                    topicDAO.addTopicDB(request.getParameter(idFatherParameter), request.getParameter(bodyParameter));
                    response.sendRedirect(getServletContext().getContextPath()+"/LoadHome");
                    return;
                }
                else{
                    form.setErrorMessage(ErrorMessage.TopicNotFound);
                }
            } catch (SQLException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ErrorMessage.QueryNotGood.getMessage());
                return;
            } catch (IllegalArgumentException e){
                form.setErrorMessage(ErrorMessage.NoMoreTopic);
            }
        }
        else{
            if(form.checkErrorId()){
                form.setErrorMessage(ErrorMessage.TopicNotInt);
            }
            else{
                form.setErrorMessage(ErrorMessage.TopicNameMissing);
            }
        }

        request.setAttribute("form", form);
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
