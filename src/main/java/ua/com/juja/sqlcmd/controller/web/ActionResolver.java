package ua.com.juja.sqlcmd.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import ua.com.juja.sqlcmd.controller.web.actions.*;
import ua.com.juja.sqlcmd.service.Service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ActionResolver {

    @Autowired
    private Service service;

    private List<Action> actions;

    public ActionResolver() {
        actions = new LinkedList<>();
        actions.addAll(Arrays.asList(
                new ConnectAction(service),
                new MenuAction(service),
                new HelpAction(service),
                new CreateAction(service),
                new ListAction(service),
                new FindAction(service),
                new ControlAction(service),
                new DisconnectAction(service)));
    }

    public Action getAction(String url) {
        for (Action action : actions) {
            if (action.canProcess(url)) {
                return action;
            }
        }
        return new NullAction();
    }
}
