package it.polimi.tiw.tiw179;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.persistence.internal.jpa.config.converters.EnumeratedImpl;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Utilities {

    private static final Gson gson= new GsonBuilder().create();

    public static TemplateEngine createTemplateEngine(ServletContext servletContext){
        ServletContextTemplateResolver templateResolver= new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        TemplateEngine templateEngine= new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");

        return templateEngine;
    }


    public static Connection MakeDBConnection(ServletContext servletContext) throws UnavailableException {
        Connection connection=null;
        String driver = servletContext.getInitParameter("dbDriver");
        String url = servletContext.getInitParameter("dbUrl");
        String passwd= servletContext.getInitParameter("dbPasswd");
        String userdb=servletContext.getInitParameter("dbUser");
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new UnavailableException(ErrorMessage.Driver.getMessage());
        }
        try {
            connection= DriverManager.getConnection(url,userdb,passwd);
        } catch (SQLException e) {
            throw new UnavailableException(ErrorMessage.ConnectionDB.getMessage());
        }
        return connection;
    }

    public static boolean isGood(String id) {
        if(id==null || id.isEmpty()){
            return false;
        }
        try {
            int x=Integer.parseInt(id);
            if(x<0) return false;
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    public static void closeDBConnection(Connection connection) throws SQLException {
        if(connection!= null){
            connection.close();
        }
    }

    public static void sendJson(Object obj, HttpServletResponse response) throws IOException {
        String json= gson.toJson(obj);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }


    // TODO: 10/6/21 a database ultimato verificare come fare upload del database su github
    // TODO: 10/19/21 modificare controllo 9 topic a livello grafico, problema del spostare in se stessi, meglio mettere un messaggio di errore gestendo l'eccezione
}
