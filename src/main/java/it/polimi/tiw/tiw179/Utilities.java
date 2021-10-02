package it.polimi.tiw.tiw179;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.UnavailableException;
import java.io.IOException;
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

    public static void ProcessTemplate(String templatePath, WebContext ctx, TemplateEngine templateEngine) throws IOException {
        templateEngine.process(templatePath,ctx,ctx.getResponse().getWriter());
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
}
