package frames.students;

import db.models.CourseModel;
import db.services.CoursesService;
import db.services.EnrollmentsService;
import db.services.StudentsService;
import frames.CreateFrame;
import helper.ControlHelper;
import helper.InputTextPaneInfo;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Calendar;
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

public class StudentsCreateFrame extends CreateFrame {

    private CoursesService cs;
    private StudentsService ss;
    private EnrollmentsService es;
    
    // Needed in order to know which thing is selected
    private JList<CourseModel> coursesList;
    // Needed in order to clear and reset list items
    private DefaultListModel<CourseModel> coursesListModel = new DefaultListModel<>();

    public StudentsCreateFrame(StudentsService ss, CoursesService cs, EnrollmentsService es, Runnable refreshFunc) {
        super("Create New Student");
        this.setLayout(new FlowLayout());


        // TODO when you load, the courses panel is completely emppty
        // TODO when you load, only the students panel is populated with students
        // TODO when you select a student, refresh the courses panel, only with the courses that the student posses
        // TODO when you click on Create New, ensure that the refresh function correctly refreshes all students and clears the courses one
        // TODO when you edit, pass the data of the student and all the courses he attends, ensure that they are all selected in the courses panel
        
        this.ss = ss;
        this.cs = cs;
        this.es = es;
        
        JTextPane firstNamePane = new JTextPane();
        JTextPane middleNamePane = new JTextPane();
        JTextPane lastNamePane = new JTextPane();
        JTextPane emailJTextPane = new JTextPane();
        JTextPane facultyNumberPane = new JTextPane();

        InputTextPaneInfo[] infos = {
            new InputTextPaneInfo(firstNamePane, "First Name"),
            new InputTextPaneInfo(middleNamePane, "Middle Name"),
            new InputTextPaneInfo(lastNamePane, "Last Name"),
            new InputTextPaneInfo(emailJTextPane, "Email"),
            new InputTextPaneInfo(facultyNumberPane, "Faculty Number")
        };

        
        JPanel panel = ControlHelper.generateInputPanel(infos, 200);
        
        JPanel coursesPanel = createCoursesResultsPanel();
        
        JButton createBtn = ControlHelper.generateDefaultButton("Create", 120, 40);


        createBtn.addActionListener(e -> {
            String newFirstName = firstNamePane.getText();
            String newMiddleName = middleNamePane.getText();
            String newLastName = lastNamePane.getText();
            String newEmail = emailJTextPane.getText();
            String newFacultyNumber = facultyNumberPane.getText();

            if(
                newFirstName.isEmpty() ||
                newMiddleName.isEmpty() ||
                newLastName.isEmpty() ||
                newEmail.isEmpty() ||
                newFacultyNumber.isEmpty() ||
                coursesList.getSelectedIndex() == -1
            ){
                System.err.println("Couldn't create student, empty data detected");
                return;
            }

            List<CourseModel> allSelectedCourses = coursesList.getSelectedValuesList();

            try {
                // Create a student
                int newStudentId = ss.createStudent(newFirstName, newMiddleName, newLastName, newEmail, newFacultyNumber);
                
                Calendar today = Calendar.getInstance();
                java.util.Date utilDate = today.getTime();
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                
                // Create enrollments for each selected course
                for (CourseModel selectedCourse : allSelectedCourses) {
                    es.createEnrollment(newStudentId, selectedCourse.courseId, sqlDate);
                }

                
                refreshFunc.run();
            } catch (Exception ex) {
                System.err.println("Error: Couldn't create student and enroll him in courses");
            }
        });

        this.add(panel);
        this.add(coursesPanel);
        this.add(createBtn);
    }

    private JPanel createCoursesResultsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER,10,100));

        panel.setSize(new Dimension(100, 200));
        panel.setMaximumSize(new Dimension(100,500));
        
        // Just a label
        JLabel lbl = ControlHelper.generateLabel("Student Courses", 15, Color.black);
        lbl.setPreferredSize(new Dimension(100,30));
        lbl.setMaximumSize(new Dimension(100,30));
        lbl.setMinimumSize(new Dimension(100,30));
        panel.add(lbl);
        //
        
        // Populate the List model with all departments
        refreshCoursesListModel();

        // Create a new list from the list model
        coursesList = createCoursesJList(coursesListModel);

        // Add the list to a scroll pane
        JScrollPane scrollPane = new JScrollPane(coursesList);
        scrollPane.setPreferredSize(new Dimension(350, 200));
        
        // Add the scroll pane to the main panel
        panel.add(scrollPane);

        return panel;
    }



    public void refreshCoursesListModel(){
        coursesListModel.clear();
        List<CourseModel> allCourses = null;
        try {
            allCourses = cs.getAllCourses();
        } catch (Exception e) {
            System.err.println("Error: Courses couldn't be loaded");
        }

        for (CourseModel course : allCourses) {
            coursesListModel.addElement(course);
        }

        this.repaint();
        this.revalidate();
    }

    public JList<CourseModel> createCoursesJList(DefaultListModel<CourseModel> model){
        JList<CourseModel> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        // Create and set the custom renderer
        list.setCellRenderer(new ListCellRenderer<CourseModel>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends CourseModel> list, CourseModel value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = new JLabel(value.courseName);
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


}
