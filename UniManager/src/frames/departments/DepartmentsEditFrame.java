package frames.departments;

import db.models.DepartmentModel;
import db.services.DepartmentsService;
import frames.EditFrame;
import helper.ControlHelper;
import helper.InputTextPaneInfo;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;

public class DepartmentsEditFrame extends EditFrame {

    public DepartmentsEditFrame(DepartmentsService ds, Runnable refreshFunc, DepartmentModel department) {
        super("Edit Department");
        this.setLayout(new FlowLayout());
        
        JTextPane departmentNamePane = new JTextPane();
        InputTextPaneInfo[] infos = {
            new InputTextPaneInfo(departmentNamePane, "Department Name")
        };

        departmentNamePane.setText(department.departmentName);

        JPanel panel = ControlHelper.generateInputPanel(infos, 200);
        JButton editBtn = ControlHelper.generateDefaultButton("Edit", 120, 40);

        editBtn.addActionListener(e -> {
            try {
                ds.updateDepartment(department.departmentId, departmentNamePane.getText());
                refreshFunc.run();
            } catch (Exception ex) {
                System.err.println("Error: Couldn't edit department");
            }
        });

        this.add(panel);
        this.add(editBtn);
    }

}
