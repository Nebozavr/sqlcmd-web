package ua.com.juja.sqlcmd.view;

import java.util.Scanner;

public class Console implements View {

    @Override
    public void write(String message) {
        System.out.println(message);
    }

    @Override
    public void writeError(Exception e) {
        String message = e.getMessage();
        if (e.getCause() != null && e.getCause().equals(e.getMessage())) {
            message += " " + e.getCause().getMessage();
        }
        System.out.println("An error occurred because: " + message);
        System.out.println("Please try again");
    }

    @Override
    public String read() {
        Scanner scanner = new Scanner(System.in);
        return  scanner.nextLine();
    }
}
