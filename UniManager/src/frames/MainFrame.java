package frames;

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
    public MainFrame(String frameTitle) {
        this.setTitle(frameTitle);
        this.setSize(700, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 150));
        //this.setLayout(null);
        
        generateTitle();
        generateBtns();
        
        this.pack();
        this.setLocationRelativeTo(null);

    }

    private void generateBtns(){
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new GridLayout(4,1,0,5));

        Map<JButton, CrudFrame> map = new HashMap<JButton,CrudFrame>();
        map.put(new JButton("Students"), new StudentsFrame("Students"));
        map.put(new JButton("Courses"), new CrudFrame("Courses"));
        map.put(new JButton("Professors"), new ProfessorsFrame("Professors"));
        map.put(new JButton("Departments"), new DepartmentsFrame("Departments"));
        
        for (Map.Entry<JButton, CrudFrame> entry : map.entrySet()) {
            JButton btn = entry.getKey();
            CrudFrame frame = entry.getValue();

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
}
