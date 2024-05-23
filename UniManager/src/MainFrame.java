import java.awt.Point;
import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame(){
        this.setTitle("LibraryApp");
        this.setSize(500,500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // Initialize the database
        DatabaseManager.initializeDatabase();
        
        //DatabaseManager.insertSampleData();

        // Add a label to display data
        JLabel label = new JLabel("Database Initialized");
        //label.setSize(new Dimension(40,40));
        label.setBounds(10, 10, 150, 25); // Set position and size
        label.setVisible(true);
        label.setLocation(new Point(10,10));
        this.add(label);
        
        String name = DatabaseManager.getUserName();
        System.err.println(name);
        label.setText(name);


        
    }
}
