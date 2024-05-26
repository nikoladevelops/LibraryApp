import db.utility.DbInfo;
import frames.courses.CoursesViewFrame;
import javax.swing.JFrame;

public class App {
    private final static String URL = "jdbc:h2:~/test";
    private final static String USER = "sa";
    private final static String PASSWORD = "";

    private static DbInfo dbInfo;

    public static void main(String[] args) throws Exception {
        dbInfo = new DbInfo(URL, USER, PASSWORD);
        
        //MainFrame mf = new MainFrame("Uni Manager", dbInfo);
        CoursesViewFrame df = new CoursesViewFrame("Courses", dbInfo);
        df.setVisible(true);
        df.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
}
