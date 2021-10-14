package it.polimi.tiw.tiw179;

import org.eclipse.persistence.internal.jpa.config.converters.EnumeratedImpl;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.UnavailableException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Utilities {

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

    public static void idExist(Connection connection, int id){

    }

    // TODO: 10/6/21 completare il tutto graficamente con CSS
    // TODO: 10/7/21 creare un box per le scelte possibili del padre durante l'aggiunta di un topic 
    // TODO: 10/6/21 vedere se è possibile imporre un trigger che aggiunge automaticamente la subtopic quando ha un padre
    // TODO: 10/6/21 aggiungere un check sul fatto che l'id del topic non possa essere divisibile per 10 sul database (fatto sul server)
    // TODO: 10/6/21 implementare la funzione di spostamento
    // TODO: 10/6/21 a database ultimato verificare come fare upload del database su github
    // TODO: 10/13/21 gestire le modifiche/aggiunte contemporanee con refresh della pagina
}
