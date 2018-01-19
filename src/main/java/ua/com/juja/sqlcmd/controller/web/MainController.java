package ua.com.juja.sqlcmd.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.service.Service;

import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private Service service;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String startPageGet() {
        return "redirect:/connect";
    }

    @RequestMapping(value = "/connect", method = RequestMethod.GET)
    public String connectGet(Model model, HttpSession session) {
        model.addAttribute("connection", new Connection());
        if (getDB_manager(session) == null) {
            return "connect";
        } else {
            model.addAttribute("message", "You are already connected");
            return "menu";
        }
    }

    @RequestMapping(value = "/connect", method = RequestMethod.POST)
    public String connectPost(@ModelAttribute("connection") Connection connection,
                              Model model, HttpSession session) {
        try {
            DatabaseManager manager = service.connect(
                    connection.getDatabaseName(),
                    connection.getUserName(),
                    connection.getPassword());

            session.setAttribute("db_manager", manager);
            return "redirect:menu?success=1";
        } catch (PgSQLDatabaseManagerException e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }
    }

    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    public String menuGet(Model model) {
        model.addAttribute("items", service.menuList());
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
    public String disconnectPost(Model model, HttpSession session) {
        try {
            session.setAttribute("db_manager", service.disconnect(getDB_manager(session)));
            return "redirect:/connect";
        } catch (PgSQLDatabaseManagerException e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listGet(Model model, HttpSession session) {
        if (getDB_manager(session) == null) {
            return "redirect:/connect";
        }
        try {
            model.addAttribute("tables", service.listTables(getDB_manager(session)));
            return "list";
        } catch (PgSQLDatabaseManagerException e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public String findGet(@RequestParam("table") String tableName,
                          Model model, HttpSession session) {
        if (getDB_manager(session) == null) {
            return "redirect:/connect";
        }
        try {
            model.addAttribute("tableNames", service.find(getDB_manager(session), tableName));
            return "find";
        } catch (PgSQLDatabaseManagerException e) {
            model.addAttribute("message", e.getMessage());
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
    public String createPost(@RequestParam Map<String, String> params,
                             Model model, HttpSession session) {


        int count = Integer.parseInt(params.get("countRows"));
        List<String> columns = new LinkedList<>();
        for (int i = 1; i <= count; i++) {
            String result = params.get("columnName" + i) + " " + params.get("typeColumn" + i);
            columns.add(result);
        }

        try {
            DatabaseManager manager = getDB_manager(session);
            session.setAttribute("db_manager", manager);
            service.createTable(manager, params.get("tableName"), columns);
            return "redirect:list";
        } catch (PgSQLDatabaseManagerException e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }
    }

    @RequestMapping(value = "/control", method = RequestMethod.GET)
    public String controlGet(@RequestParam("table") String tableName,
                             Model model, HttpSession session) {
        if (getDB_manager(session) == null) {
            return "redirect:/connect";
        }
        try {
            model.addAttribute("tableNames", service.find(getDB_manager(session), tableName));
            return "control";
        } catch (PgSQLDatabaseManagerException e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }
    }

    @RequestMapping(value = "/control", method = RequestMethod.POST)
    public String controlPost(@RequestParam Map<String, String> params, Model model, HttpSession session) {
        try {

            DatabaseManager manager = getDB_manager(session);
            String action = params.get("action");
            String tableName = params.get("table");

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
                    result.add(params.get(value));
                }
                service.insert(manager, tableName, result);
                return "redirect:control?table=" + tableName;
            } else if (action.equals("Update")) {

                List<String> result = new LinkedList<>();
                result.add(params.get("columnWhere"));
                result.add(params.get("valueWhere"));
                result.add(params.get("columnSet"));
                result.add(params.get("valueSet"));

                service.update(manager, tableName, result);
                return "redirect:control?table=" + tableName;
            } else if (action.equals("Delete")) {
                String columnName = params.get("columnDelete");
                String value = params.get("valueDelete");
                service.delete(manager, tableName, columnName, value);
                return "redirect:control?table=" + tableName;
            } else {
                model.addAttribute("message", "Something WRONG!!! Run...... :)");
                return "error";
            }
        } catch (PgSQLDatabaseManagerException e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }
    }

    private DatabaseManager getDB_manager(HttpSession session) {
        return (DatabaseManager) session.getAttribute("db_manager");
    }

}
