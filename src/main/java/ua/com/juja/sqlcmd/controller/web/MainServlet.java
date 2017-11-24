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

        DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("db_manager");

        if (action.startsWith("/connect")) {
            if (manager == null) {
                req.getRequestDispatcher("connect.jsp").forward(req, resp);
            } else {
                req.setAttribute("message", "You are already connected");
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }
            return;
        }

        if (manager == null) {
            req.getRequestDispatcher("connect.jsp").forward(req, resp);
            return;
        }

        if (action.startsWith("/menu")) {
            req.setAttribute("items", service.commandsList());
            req.getRequestDispatcher("menu.jsp").forward(req, resp);

        } else if (action.startsWith("/help")) {
            req.getRequestDispatcher("help.jsp").forward(req, resp);

        } else if (action.startsWith("/list")) {
            req.getRequestDispatcher("list.jsp").forward(req, resp);

        } else if (action.startsWith("/find")) {
            req.getRequestDispatcher("find.jsp").forward(req, resp);

        }else {
            req.setAttribute("message", "Something wrong!!!");
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }

    private String getAction(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        return requestURI.substring(req.getContextPath().length(), requestURI.length());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);

        if (action.startsWith("/connect")){
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
        }
    }
}
