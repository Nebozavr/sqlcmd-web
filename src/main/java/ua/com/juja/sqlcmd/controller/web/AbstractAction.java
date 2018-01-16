package ua.com.juja.sqlcmd.controller.web;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractAction implements Action {
    public AbstractAction() {
    }

    protected Service service;

    public AbstractAction(Service service) {
        this.service = service;
    }

    protected void goTo(String url, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(url).forward(req, resp);
    }

    @Override
    public void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // do nothing
    }

    protected DatabaseManager getDB_manager(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("db_manager");

        if (manager == null) {
            goTo("connect.jsp", req, resp);
        }

        return manager;
    }

    protected void exceptionPage(PgSQLDatabaseManagerException e, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("message", e.getMessage());
        goTo("error.jsp", req, resp);
    }

    protected void redirectTo(String url, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(resp.encodeRedirectURL(url));
    }
}
