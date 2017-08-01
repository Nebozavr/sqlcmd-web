public class Main {

    public static void main(String[] argv) {

        try {
            ConnectionDB.connectionToDataBase();
           // Clear.query("delete from users");
          //  Clear.query("ALTER SEQUENCE id_seq RESTART WITH 1");
          // Clear.query("delete from users");
          // Clear.query("select * from users order by username");
         //  Update.query("users", "username", "testName", "password", "lol");
            //Find.query("users");
           // Insert.query("users", "username", "testName", "password", "testPass");
            //Delete.query("users", "username", "testName");
            //Create.query("test", "id int",
            // "column1 text", "column2 text");
            Drop.query("orders");
           // ListTables.query();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
