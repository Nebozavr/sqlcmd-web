package ua.com.juja.sqlcmd.controller.web.actions;

import ua.com.juja.sqlcmd.controller.web.AbstractAction;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ConnectAction extends AbstractAction {
    public ConnectAction(Service service) {
        super(service);
    }

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (getDB_manager(req, resp) == null) {
            goTo("connect.jsp", req, resp);
        } else {
            req.setAttribute("message", "You are already connected");
            goTo("menu.jsp", req, resp);
        }
    }

    @Override
    public void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String databaseName = req.getParameter("dbName");
        String userName = req.getParameter("username");
        String password = req.getParameter("password");
        try {
            DatabaseManager manager = service.connect(databaseName, userName, password);
            req.getSession().setAttribute("db_manager", manager);
            redirectTo("menu?success=1", resp);
        } catch (PgSQLDatabaseManagerException e) {
            exceptionPage(e, req, resp);
        }
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/connect");
    }
}
