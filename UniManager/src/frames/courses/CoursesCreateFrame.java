package frames.courses;

import db.models.DepartmentModel;
import db.services.CoursesService;
import db.services.DepartmentsService;
import frames.CreateFrame;
import helper.ControlHelper;
import helper.InputTextPaneInfo;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

public class CoursesCreateFrame extends CreateFrame {

    private CoursesService cs;
    private DepartmentsService ds;
    
    // Needed in order to know which thing is selected
    private JList<DepartmentModel> list;
    // Needed in order to clear and reset list items
    private DefaultListModel<DepartmentModel> listModel = new DefaultListModel<>();

    public CoursesCreateFrame(CoursesService cs, DepartmentsService ds, Runnable refreshFunc) {
        super("Create New Course");
        this.setLayout(new FlowLayout());

        this.ds = ds;
        this.cs = cs;
        
        JTextPane courseNamePane = new JTextPane();
        JTextPane descriptionPane = new JTextPane();

        InputTextPaneInfo[] infos = {
            new InputTextPaneInfo(courseNamePane, "Course Name"),
            new InputTextPaneInfo(descriptionPane, "Description"),};

        
        JPanel panel = ControlHelper.generateInputPanel(infos, 200);
        
        JPanel departmentsPanel = createResultsPanel();
        

        
        JButton createBtn = ControlHelper.generateDefaultButton("Create", 120, 40);


        createBtn.addActionListener(e -> {
            String newCourseName = courseNamePane.getText();
            String newCourseDesc = descriptionPane.getText();

            if(newCourseName.isEmpty() || newCourseDesc.isEmpty() || list.getSelectedIndex() == -1){
                System.err.println("Couldn't create course, empty data detected");
                return;
            }

            int newDepartmentId = list.getSelectedValue().departmentId;

            try {
                cs.createCourse(newCourseName, newCourseDesc, newDepartmentId);
                refreshFunc.run();
            } catch (Exception ex) {
                System.err.println("Error: Couldn't create course");
            }
        });

        this.add(panel);
        this.add(departmentsPanel);
        this.add(createBtn);
    }

    public JList<DepartmentModel> createJList(DefaultListModel<DepartmentModel> model) {
        JList<DepartmentModel> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Create and set the custom renderer
        list.setCellRenderer(new ListCellRenderer<DepartmentModel>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends DepartmentModel> list, DepartmentModel value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = new JLabel(value.departmentName);
                if (isSelected) {
                    label.setBackground(list.getSelectionBackground());
                    label.setForeground(list.getSelectionForeground());
                } else {
                    label.setBackground(list.getBackground());
                    label.setForeground(list.getForeground());
                }
                label.setOpaque(true);
                return label;
            }
        });

        return list;
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 100));

        panel.setSize(new Dimension(220, 500));
        panel.setMaximumSize(new Dimension(100, 500));

        // Just a label
        JLabel lbl = ControlHelper.generateLabel("All Departments", 15, Color.black);
        lbl.setPreferredSize(new Dimension(100, 30));
        lbl.setMaximumSize(new Dimension(100, 30));
        lbl.setMinimumSize(new Dimension(100, 30));
        panel.add(lbl);
        //

        //Populate the List model with all departments
        refreshListModel();

        //Create a new list from the list model
        list = createJList(listModel);

        // Add the list to a scroll pane
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(350, 200));

        // Add the scroll pane to the main panel
        panel.add(scrollPane);

        return panel;
    }


    public void refreshListModel(){
        listModel.clear();
        List<DepartmentModel> allDepartments = new ArrayList<>();
        try {
            allDepartments = ds.getAllDepartments();
        } catch (Exception e) {
            System.err.println("Error: Departments couldn't be loaded");
        }

        for (DepartmentModel department : allDepartments) {
            listModel.addElement(department);
        }

        this.repaint();
        this.revalidate();
    }


}
