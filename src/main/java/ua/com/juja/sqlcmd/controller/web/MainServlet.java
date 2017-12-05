package ua.com.juja.sqlcmd.controller.web;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.service.Service;
import ua.com.juja.sqlcmd.service.ServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {

    private Service service;

    @Override
    public void init() throws ServletException {
        super.init();

        service = new ServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = getAction(req);

        DatabaseManager manager = getDB_manager(req);

        if (action.startsWith("/connect")) {
            if (manager == null) {
                req.getRequestDispatcher("connect.jsp").forward(req, resp);
            } else {
                req.setAttribute("message", "You are already connected");
                req.getRequestDispatcher("menu.jsp").forward(req, resp);
            }
            return;
        }

        if (manager == null) {
            req.getRequestDispatcher("connect.jsp").forward(req, resp);
            return;
        }

        if (action.startsWith("/menu")) {
            req.setAttribute("items", service.menuList());
            req.getRequestDispatcher("menu.jsp").forward(req, resp);

        } else if (action.startsWith("/help")) {
            req.getRequestDispatcher("help.jsp").forward(req, resp);

        } else if (action.startsWith("/list")) {
            try {
                req.setAttribute("tables", service.listTables(getDB_manager(req)));
                req.getRequestDispatcher("list.jsp").forward(req, resp);
            } catch (PgSQLDatabaseManagerException e) {
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }

        } else if (action.startsWith("/find")) {
            String tableName = req.getParameter("table");
            try {
              //  req.setAttribute("table", tableName);
                req.setAttribute("tableNames", service.find(manager, tableName));
                req.getRequestDispatcher("find.jsp").forward(req, resp);
            } catch (PgSQLDatabaseManagerException e) {
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }

        }else if (action.startsWith("/control")) {
            String tableName = req.getParameter("table");
            try {
                req.setAttribute("tableNames", service.find(manager, tableName));
                req.getRequestDispatcher("control.jsp").forward(req, resp);
            } catch (PgSQLDatabaseManagerException e) {
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }

        } else if (action.startsWith("/disconnect")) {
            req.getRequestDispatcher("disconnect.jsp").forward(req, resp);

        } else {
            req.setAttribute("message", "Something wrong!!!");
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }

    private DatabaseManager getDB_manager(HttpServletRequest req) {
        return (DatabaseManager) req.getSession().getAttribute("db_manager");
    }

    private String getAction(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        return requestURI.substring(req.getContextPath().length(), requestURI.length());
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
                resp.sendRedirect(resp.encodeRedirectURL("menu?success=1"));
            } catch (PgSQLDatabaseManagerException e) {
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }
        } else if (urlAction.startsWith("/disconnect")) {
            try {

                DatabaseManager manager = service.disconnect(getDB_manager(req));
                req.getSession().setAttribute("db_manager", manager);
                resp.sendRedirect(resp.encodeRedirectURL("menu?success=2"));
            } catch (PgSQLDatabaseManagerException e) {
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }
        } else if (urlAction.startsWith("/control")) {
            try {

                DatabaseManager manager = getDB_manager(req);
                String action = req.getParameter("action");
                String tableName = req.getParameter("table");
               if (action.equals("Clear")){
                   service.clear(manager, tableName);
                   resp.sendRedirect(resp.encodeRedirectURL("control?table=" + tableName));
               } else if (action.equals("Drop")){
                   service.drop(manager, tableName);
                   resp.sendRedirect(resp.encodeRedirectURL("list"));
               }
                //resp.sendRedirect(resp.encodeRedirectURL("menu?success=2"));
            } catch (PgSQLDatabaseManagerException e) {
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }
        }
    }
}
