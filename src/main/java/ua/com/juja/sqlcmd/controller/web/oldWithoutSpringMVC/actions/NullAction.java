package ua.com.juja.sqlcmd.controller.web.oldWithoutSpringMVC.actions;

import ua.com.juja.sqlcmd.controller.web.oldWithoutSpringMVC.AbstractAction;
import ua.com.juja.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NullAction extends AbstractAction {

    public NullAction() {
        super();
    }

    public NullAction(Service service) {
        super(service);
    }

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        goTo("connect.jsp", req, resp);
    }

    @Override
    public boolean canProcess(String url) {
        return false;
    }
}
