import db.utility.DbInfo;
import frames.MainFrame;

public class App {
    private final static String URL = "jdbc:h2:~/test";
    private final static String USER = "sa";
    private final static String PASSWORD = "";

    private static DbInfo dbInfo;

    public static void main(String[] args) throws Exception {
        dbInfo = new DbInfo(URL, USER, PASSWORD);

        // TableBuilderService tbs = new TableBuilderService(dbInfo);
        // tbs.dropAllTables();
        // tbs.ensureTablesCreated();

        
        MainFrame mf = new MainFrame("Uni Manager", dbInfo);
    
    }
}
