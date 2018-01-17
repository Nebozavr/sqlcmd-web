package ua.com.juja.sqlcmd.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.service.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private Service service;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String startPageGet() {
        return "redirect:/connect";
    }

    @RequestMapping(value = "/connect", method = RequestMethod.GET)
    public String connectGet(HttpServletRequest request, HttpSession session) {
        if (getDB_manager(session) == null) {
            return "connect";
        } else {
            request.setAttribute("message", "You are already connected");
            return "menu";
        }
    }

    @RequestMapping(value = "/connect", method = RequestMethod.POST)
    public String connectPost(HttpServletRequest request, HttpSession session) {
        String databaseName = request.getParameter("dbName");
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            DatabaseManager manager = service.connect(databaseName, userName, password);
            session.setAttribute("db_manager", manager);
            return "redirect:menu?success=1";
        } catch (PgSQLDatabaseManagerException e) {
            request.setAttribute("message", e.getMessage());
            return "error";
        }
    }


    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    public String menuGet(HttpServletRequest request) {
        request.setAttribute("items", service.menuList());
        return "menu";
    }

    @RequestMapping(value = "/disconnect", method = RequestMethod.GET)
    public String disconnectGet(HttpSession session) {
        if (getDB_manager(session) == null) {
            return "redirect:/connect";
        }
        return "disconnect";
    }

    @RequestMapping(value = "/disconnect", method = RequestMethod.POST)
    public String disconnectPost(HttpServletRequest request, HttpSession session) {
        try {
            session.setAttribute("db_manager", service.disconnect(getDB_manager(session)));
            return "redirect:/connect";
        } catch (PgSQLDatabaseManagerException e) {
            request.setAttribute("message", e.getMessage());
            return "error";
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listGet(HttpServletRequest request, HttpSession session) {
        if (getDB_manager(session) == null) {
            return "redirect:/connect";
        }
        try {
            request.setAttribute("tables", service.listTables(getDB_manager(session)));
            return "list";
        } catch (PgSQLDatabaseManagerException e) {
            request.setAttribute("message", e.getMessage());
            return "error";
        }
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public String findGet(HttpServletRequest request, HttpSession session) {
        if (getDB_manager(session) == null) {
            return "redirect:/connect";
        }
        try {
            request.setAttribute("tableNames", service.find(getDB_manager(session), request.getParameter("table")));
            return "find";
        } catch (PgSQLDatabaseManagerException e) {
            request.setAttribute("message", e.getMessage());
            return "error";
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String createGet(HttpSession session) {
        if (getDB_manager(session) == null) {
            return "redirect:/connect";
        }
        return "create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createPost(HttpServletRequest request, HttpSession session) {
        int count = Integer.parseInt(request.getParameter("countRows"));
        List<String> columns = new LinkedList<>();
        for (int i = 1; i <= count; i++) {
            String result = request.getParameter("columnName" + i) + " " + request.getParameter("typeColumn" + i);
            columns.add(result);
        }

        try {
            DatabaseManager manager = getDB_manager(session);
            request.getSession().setAttribute("db_manager", manager);
            service.createTable(manager, request.getParameter("tableName"), columns);
            return "redirect:list";
        } catch (PgSQLDatabaseManagerException e) {
            request.setAttribute("message", e.getMessage());
            return "error";
        }
    }

    @RequestMapping(value = "/control", method = RequestMethod.GET)
    public String controlGet(HttpServletRequest request, HttpSession session) {
        if (getDB_manager(session) == null) {
            return "redirect:/connect";
        }
        try {
            request.setAttribute("tableNames", service.find(getDB_manager(session), request.getParameter("table")));
            return "control";
        } catch (PgSQLDatabaseManagerException e) {
            request.setAttribute("message", e.getMessage());
            return "error";
        }
    }

    @RequestMapping(value = "/control", method = RequestMethod.POST)
    public String controlPost(HttpServletRequest request, HttpSession session) {
        try {

            DatabaseManager manager = getDB_manager(session);
            String action = request.getParameter("action");
            String tableName = request.getParameter("table");

            if (action.equals("Clear")) {
                service.clear(manager, tableName);
                return "redirect:control?table=" + tableName;
            } else if (action.equals("Drop")) {
                service.drop(manager, tableName);
                return "redirect:list";
            } else if (action.equals("Insert")) {

                List<String> result = new LinkedList<>();
                List<String> columnsName = service.find(manager, tableName).get(0);
                for (String value : columnsName) {
                    result.add(value);
                    result.add(request.getParameter(value));
                }
                service.insert(manager, tableName, result);
                return "redirect:control?table=" + tableName;
            } else if (action.equals("Update")) {

                List<String> result = new LinkedList<>();
                result.add(request.getParameter("columnWhere"));
                result.add(request.getParameter("valueWhere"));
                result.add(request.getParameter("columnSet"));
                result.add(request.getParameter("valueSet"));

                service.update(manager, tableName, result);
                return "redirect:control?table=" + tableName;
            } else if (action.equals("Delete")) {
                String columnName = request.getParameter("columnDelete");
                String value = request.getParameter("valueDelete");
                service.delete(manager, tableName, columnName, value);
                return "redirect:control?table=" + tableName;
            } else {
                request.setAttribute("message", "Something WRONG!!! Run...... :)");
                return "error";
            }
        } catch (PgSQLDatabaseManagerException e) {
            request.setAttribute("message", e.getMessage());
            return "error";
        }
    }

    private DatabaseManager getDB_manager(HttpSession session) {
        return (DatabaseManager) session.getAttribute("db_manager");
    }

}
