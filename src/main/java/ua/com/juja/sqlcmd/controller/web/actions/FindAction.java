package ua.com.juja.sqlcmd.controller.web.actions;

import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FindAction extends AbstractAction{
    public FindAction(Service service) {
        super(service);
    }

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tableName = req.getParameter("table");
        try {
            req.setAttribute("tableNames", service.find(getDB_manager(req, resp), tableName));
            goTo("find.jsp", req, resp);
        } catch (PgSQLDatabaseManagerException e) {
            exceptionPage(e, req, resp);
        }
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/find");
    }
}
