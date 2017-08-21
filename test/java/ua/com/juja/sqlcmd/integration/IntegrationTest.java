package ua.com.juja.sqlcmd.integration;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.Main;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class IntegrationTest {

    private ConfigurableInputStream in;
    private LogOutputStream out;

    @Before
    public  void setup(){
        in = new ConfigurableInputStream();
        out = new LogOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @Test
    public void testHelp(){

		in.add("help");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User\r\n" +
                "Please enter database name, username and password, in the format: connect|database|username|password\r\n" +
                "List of all commands:\r\n" +
                "\t connect|database|username|password \r\n" +
                "\t\t Connect to database\r\n" +
                "\t help \r\n" +
                "\t\t View all commands and their description\r\n" +
                "\t list \r\n" +
                "\t\t Show all tables from database\r\n" +
                "\t find|tableName \r\n" +
                "\t\t Show all data from tableName\r\n" +
                "\t exit \r\n" +
                "\t\t Close connection to database and exit program!\r\n" +
                "Enter a new command or use help command.\r\n" +
                "Connection was close!\r\n" +
                "Goodbye!!!\r\n", out.getData());
    }


    @Test
    public void testExit(){

		in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User\r\n" +
                "Please enter database name, username and password, in the format: connect|database|username|password\r\n" +
                "Connection was close!\r\n" +
                "Goodbye!!!\r\n", out.getData());
    }

	@Test
	public void testConnect(){

		in.add("connect|sqlcmd|yura|yura1990");
		in.add("exit");

		Main.main(new String[0]);

		assertEquals("Hello User\r\n" +
				"Please enter database name, username and password, in the format: connect|database|username|password\r\n" +
				"Connection was successful!\r\n" +
				"Enter a new command or use help command.\r\n" +
				"Connection was close!\r\n" +
				"Goodbye!!!\r\n", out.getData());
	}

	@Test
    public void testUnknownCommand(){
        in.add("connect|sqlcmd|yura|yura1990");
	    in.add("errorCommand");
	    in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User\r\n" +
                "Please enter database name, username and password, in the format: connect|database|username|password\r\n" +
                "Connection was successful!\r\n" +
                "Enter a new command or use help command.\r\n" +
                "Unknown command!\r\n" +
                "Enter a new command or use help command.\r\n" +
                "Connection was close!\r\n" +
                "Goodbye!!!\r\n", out.getData());
    }

    @Test
    public void testList(){
        in.add("connect|sqlcmd|yura|yura1990");
        in.add("list");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello User\r\n" +
                "Please enter database name, username and password, in the format: connect|database|username|password\r\n" +
                "Connection was successful!\r\n" +
                "Enter a new command or use help command.\r\n" +
                "[test, test2, users]\r\n" +
                "Enter a new command or use help command.\r\n" +
                "Connection was close!\r\n" +
                "Goodbye!!!\r\n", out.getData());
    }
}
