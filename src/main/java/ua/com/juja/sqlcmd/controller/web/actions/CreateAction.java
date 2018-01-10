package ua.com.juja.sqlcmd.controller.web.actions;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class CreateAction extends AbstractAction{
    public CreateAction(Service service) {
        super(service);
    }

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        goTo("create.jsp", req, resp);
    }

    @Override
    public void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tableName = req.getParameter("tableName");
        int count = Integer.parseInt(req.getParameter("countRows"));
        List<String> columns = new LinkedList<>();
        for (int i = 1; i <= count; i++) {
            String result = req.getParameter("columnName" + i) + " " + req.getParameter("typeColumn" + i);
            columns.add(result);
        }

        try {
            DatabaseManager manager = getDB_manager(req, resp);
            req.getSession().setAttribute("db_manager", manager);
            service.createTable(manager, tableName, columns);
            redirectTo("list", resp);
        } catch (PgSQLDatabaseManagerException e) {
            exceptionPage(e, req, resp);
        }
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/create");
    }
}
