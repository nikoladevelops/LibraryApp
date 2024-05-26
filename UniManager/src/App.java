import db.utility.DbInfo;
import frames.students.StudentsViewFrame;
import javax.swing.JFrame;

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

        
        //MainFrame mf = new MainFrame("Uni Manager", dbInfo);\
        
        StudentsViewFrame sf = new StudentsViewFrame("Students", dbInfo);
        sf.setVisible(true);
        sf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // CoursesViewFrame cf = new CoursesViewFrame("Courses", dbInfo);
        // cf.setVisible(true);
        // cf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        // DepartmentsViewFrame df = new DepartmentsViewFrame("Departments", dbInfo);
        // df.setVisible(true);
        // df.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
