package ua.com.juja.sqlcmd.controller.web.actions;

import ua.com.juja.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MenuAction extends AbstractAction{
    public MenuAction(Service service) {
        super(service);
    }

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("items", service.menuList());
        goTo("menu.jsp", req, resp);
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/menu");
    }
}
