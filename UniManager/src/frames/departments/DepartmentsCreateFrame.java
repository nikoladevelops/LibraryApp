package frames.departments;

import db.services.DepartmentsService;
import frames.CreateFrame;
import helper.ControlHelper;
import helper.InputTextPaneInfo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;

public class DepartmentsCreateFrame extends CreateFrame {
    
    public DepartmentsCreateFrame(DepartmentsService ds, Runnable refreshFunc) {
        super("Create New Department");
        this.setLayout(new FlowLayout());
        
        JTextPane departmentNamePane = new JTextPane(); 
        InputTextPaneInfo[] infos = {
            new InputTextPaneInfo(departmentNamePane, "Department Name")
        };

        JPanel panel = ControlHelper.generateInputPanel(infos, 200);
        JButton createBtn = ControlHelper.generateButton("Create Department", new Dimension(150,40), 17, Color.white, Color.black, Color.black);
        
        createBtn.addActionListener(e->{
            try {
                ds.createDepartment(departmentNamePane.getText());
                refreshFunc.run();
            } catch (Exception ex) {
                System.err.println("Error: Couldn't create new department");
            }
        });


        this.add(panel);
        this.add(createBtn);
    }

}
