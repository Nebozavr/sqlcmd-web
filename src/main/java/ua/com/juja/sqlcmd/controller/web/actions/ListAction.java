package ua.com.juja.sqlcmd.controller.web.actions;

import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ListAction extends AbstractAction{

    public ListAction(Service service) {
        super(service);
    }

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("tables", service.listTables(getDB_manager(req, resp)));
            goTo("list.jsp", req, resp);
        } catch (PgSQLDatabaseManagerException e) {
            exceptionPage(e, req, resp);
        }
    }

    @Override
    public void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //do nothing
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/list");
    }
}
