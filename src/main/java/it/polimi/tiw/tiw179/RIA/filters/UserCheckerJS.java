package it.polimi.tiw.tiw179.RIA.filters;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "UserCheckerJS")
public class UserCheckerJS implements Filter {

    public void init(FilterConfig config) throws ServletException {
        Filter.super.init(config);
    }

    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req= (HttpServletRequest) request;
        HttpServletResponse res= (HttpServletResponse) response;
        String path= req.getServletContext().getContextPath()+"/RIA/LoginJS.html";
        System.out.println("Checking user..");
        HttpSession session= req.getSession();
        if (session.isNew() || session.getAttribute("user")==null){
            res.sendRedirect(path);
            System.out.println("DONE - FAILED");
            return;
        }
        System.out.println("DONE - OK");
        chain.doFilter(request, response);
    }
    // TODO: 10/20/21 check this part
}
