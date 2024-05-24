import db.utility.DbInfo;
import frames.MainFrame;

public class App {
    private static String URL = "jdbc:h2:~/test";
    private static String USER = "sa";
    private static String PASSWORD = "";

    private static DbInfo dbInfo;

    public static void main(String[] args) throws Exception {
        dbInfo = new DbInfo(URL, USER, PASSWORD);

        MainFrame mf = new MainFrame("Uni Manager");

    }
}
