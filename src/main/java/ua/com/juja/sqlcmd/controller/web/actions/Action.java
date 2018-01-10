package ua.com.juja.sqlcmd.controller.web.actions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Action {

    void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

    void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

    boolean canProcess(String url);
}
