package frames;

import db.services.*;
import db.utility.DbInfo;
import java.awt.Point;
import java.sql.SQLException;
import javax.swing.*;

public class MainFrame extends JFrame {
    private TableBuilderService ts;
    private DepartmentsService ds;

    public MainFrame(DbInfo dbInfo) {
        this.setTitle("LibraryApp");
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        ts = new TableBuilderService(dbInfo);
        ds = new DepartmentsService(dbInfo);

        // Ensure that all db tables have been created
        ts.ensureTablesCreated();

        // Test whether a new department can be added
        try {
            ds.createDepartment("Test");
            System.err.println("SUCCESS: Department created");
        } catch (SQLException ex) {
            System.err.println("ERROR couldnt create department!");
        }

        // Add a label to display data
        JLabel label = new JLabel("Database Initialized");
        //label.setSize(new Dimension(40,40));
        label.setBounds(10, 10, 150, 25); // Set position and size
        label.setVisible(true);
        label.setLocation(new Point(10, 10));
        this.add(label);

        //String name = DatabaseManager.getUserName();
        //System.err.println(name);

        //label.setText(name);
    }
}
