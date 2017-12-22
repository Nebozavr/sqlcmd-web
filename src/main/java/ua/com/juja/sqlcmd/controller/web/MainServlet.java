package ua.com.juja.sqlcmd.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.service.Service;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MainServlet extends HttpServlet {

    @Autowired
    private Service service;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = getAction(req);

        DatabaseManager manager = getDB_manager(req);

        if (action.startsWith("/connect")) {
            if (manager == null) {
                goTo("connect.jsp", req, resp);
            } else {
                req.setAttribute("message", "You are already connected");
                goTo("menu.jsp", req, resp);
            }
            return;
        }

        if (manager == null) {
            goTo("connect.jsp", req, resp);
            return;
        }

        if (action.startsWith("/menu")) {
            req.setAttribute("items", service.menuList());
            goTo("menu.jsp", req, resp);

        } else if (action.startsWith("/help")) {
            goTo("help.jsp", req, resp);

        } else if (action.startsWith("/create")) {
            goTo("create.jsp", req, resp);

        } else if (action.startsWith("/list")) {
            try {
                req.setAttribute("tables", service.listTables(manager));
                goTo("list.jsp", req, resp);
            } catch (PgSQLDatabaseManagerException e) {
                exceptionPage(e, req, resp);
            }

        } else if (action.startsWith("/find")) {
            String tableName = req.getParameter("table");
            try {
                req.setAttribute("tableNames", service.find(manager, tableName));
                goTo("find.jsp", req, resp);
            } catch (PgSQLDatabaseManagerException e) {
                exceptionPage(e, req, resp);
            }

        } else if (action.startsWith("/control")) {
            String tableName = req.getParameter("table");
            try {
                req.setAttribute("tableNames", service.find(manager, tableName));
                goTo("control.jsp", req, resp);
            } catch (PgSQLDatabaseManagerException e) {
                exceptionPage(e, req, resp);
            }

        } else if (action.startsWith("/disconnect")) {
            goTo("disconnect.jsp", req, resp);

        } else {
            req.setAttribute("message", "Something wrong!!!");
            goTo("error.jsp", req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String urlAction = getAction(req);

        if (urlAction.startsWith("/connect")) {
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

        } else if (urlAction.startsWith("/disconnect")) {
            try {

                DatabaseManager manager = service.disconnect(getDB_manager(req));
                req.getSession().setAttribute("db_manager", manager);
                redirectTo("menu?success=2", resp);
            } catch (PgSQLDatabaseManagerException e) {
                exceptionPage(e, req, resp);
            }

        } else if (urlAction.startsWith("/create")) {

            String tableName = req.getParameter("tableName");
            int count = Integer.parseInt(req.getParameter("countRows"));
            List<String> columns = new LinkedList<>();
            for (int i = 1; i <= count; i++) {
                String result = req.getParameter("columnName" + i) + " " + req.getParameter("typeColumn" + i);
                columns.add(result);
            }

            try {

                DatabaseManager manager = getDB_manager(req);
                req.getSession().setAttribute("db_manager", manager);
                service.createTable(manager, tableName, columns);
                redirectTo("list", resp);
            } catch (PgSQLDatabaseManagerException e) {
                exceptionPage(e, req, resp);
            }

        } else if (urlAction.startsWith("/control")) {
            try {

                DatabaseManager manager = getDB_manager(req);
                String action = req.getParameter("action");
                String tableName = req.getParameter("table");

                if (action.equals("Clear")) {
                    service.clear(manager, tableName);
                    redirectTo("control?table=" + tableName, resp);
                } else if (action.equals("Drop")) {
                    service.drop(manager, tableName);
                    redirectTo("list", resp);
                } else if (action.equals("Insert")) {

                    List<String> result = new LinkedList<>();
                    List<String> columnsName = service.find(manager, tableName).get(0);
                    for (String value : columnsName) {
                        result.add(value);
                        result.add(req.getParameter(value));
                    }
                    service.insert(manager, tableName, result);
                    redirectTo("control?table=" + tableName, resp);
                } else if (action.equals("Update")) {

                    List<String> result = new LinkedList<>();
                    result.add(req.getParameter("columnWhere"));
                    result.add(req.getParameter("valueWhere"));
                    result.add(req.getParameter("columnSet"));
                    result.add(req.getParameter("valueSet"));

                    service.update(manager, tableName, result);
                    redirectTo("control?table=" + tableName, resp);
                } else if (action.equals("Delete")) {
                    String columnName = req.getParameter("columnDelete");
                    String value = req.getParameter("valueDelete");
                    service.delete(manager, tableName, columnName, value);
                    redirectTo("control?table=" + tableName, resp);
                }
            } catch (PgSQLDatabaseManagerException e) {
                exceptionPage(e, req, resp);
            }
        }
    }

    private void redirectTo(String url, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(resp.encodeRedirectURL(url));
    }

    private void exceptionPage(PgSQLDatabaseManagerException e, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("message", e.getMessage());
        goTo("error.jsp", req, resp);
    }

    private void goTo(String url, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(url).forward(req, resp);
    }

    private DatabaseManager getDB_manager(HttpServletRequest req) {
        return (DatabaseManager) req.getSession().getAttribute("db_manager");
    }

    private String getAction(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        return requestURI.substring(req.getContextPath().length(), requestURI.length());
    }
}
