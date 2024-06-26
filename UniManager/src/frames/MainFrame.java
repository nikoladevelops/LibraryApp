package frames;

import db.services.TableBuilderService;
import db.utility.DbInfo;
import frames.courses.CoursesViewFrame;
import frames.departments.DepartmentsViewFrame;
import frames.students.StudentsViewFrame;
import helper.ControlHelper;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
    private final TableBuilderService tbs;
    private final DbInfo dbInfo;

    public MainFrame(String frameTitle, DbInfo dbInfo) {
        this.setTitle(frameTitle);
        this.setSize(700, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 150));
        this.setResizable(false);
        this.dbInfo = dbInfo;
        this.tbs = new TableBuilderService(dbInfo);
        
        
        generateTitle();
        generateBtns();
        
        generateSpecialBtns();
        
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    private void generateBtns(){
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new GridLayout(4,1,0,5));

        Map<JButton, ViewFrame> map = new HashMap<>();
        map.put(new JButton("Students"), new StudentsViewFrame("Students",dbInfo));
        map.put(new JButton("Courses"), new CoursesViewFrame("Courses",dbInfo));
        map.put(new JButton("Departments"), new DepartmentsViewFrame("Departments",dbInfo));
        
        for (Map.Entry<JButton, ViewFrame> entry : map.entrySet()) {
            JButton btn = entry.getKey();
            ViewFrame frame = entry.getValue();

            btn.addActionListener(e->{
                frame.setVisible(true);
            });

            Color normalBGColor = Color.white;
            Color hoverBGColor = Color.black;

            Color normalForeColor = Color.black;
            Color hoverForeColo = Color.white;

            ControlHelper.editButtonStyle(btn, new Dimension(250,40),17, normalBGColor, normalForeColor, Color.black);
            ControlHelper.addHoverEffect(btn, normalBGColor, hoverBGColor, normalForeColor, hoverForeColo);
            
            btnPanel.add(btn);
        }

        this.add(btnPanel, BorderLayout.CENTER);
    }

    private void generateTitle() {
        JLabel lbl = ControlHelper.generateLabel("Uni Manager", 22, Color.black);
        this.add(lbl);
    }

    private void generateSpecialBtns() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2,1, 0, 5));

        JButton dropTablesBtn = ControlHelper.generateButton(
            "Drop Tables",
            new Dimension(130,40),
            15,
            Color.white,
            Color.black,
            Color.red
        );

        JButton ensureTablesCreatedBtn = ControlHelper.generateButton(
            "Ensure Created",
            new Dimension(30,40),
            15,
            Color.white,
            Color.black,
            Color.blue
        );

        dropTablesBtn.addActionListener(e->{
            tbs.dropAllTables();
        });

        ensureTablesCreatedBtn.addActionListener(e->{
            tbs.ensureTablesCreated();
        });

        ControlHelper.addHoverEffect(dropTablesBtn, Color.white, Color.black, Color.black, Color.white);
        ControlHelper.addHoverEffect(ensureTablesCreatedBtn, Color.white, Color.black, Color.black, Color.white);

        panel.add(dropTablesBtn);
        panel.add(ensureTablesCreatedBtn);

        this.add(panel);
    }
}
