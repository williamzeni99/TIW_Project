package it.polimi.tiw.tiw179.HTMLPure.filters;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "MoveFilter")
public class MoveFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
        Filter.super.init(config);
    }

    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse res= (HttpServletResponse) response;
        HttpServletRequest req= (HttpServletRequest) request;
        String id= req.getParameter("idToMove");
        if(id==null){
            String path = req.getServletContext().getContextPath() + "/LoadHome";
            res.sendRedirect(path);
            return;
        }

        chain.doFilter(req, res);
    }
}
