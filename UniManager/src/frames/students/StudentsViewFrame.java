package frames.students;

import db.models.CourseModel;
import db.models.DepartmentModel;
import db.models.StudentModel;
import db.services.CoursesService;
import db.services.DepartmentsService;
import db.services.EnrollmentsService;
import db.services.StudentsService;
import db.services.TeachesService;
import db.utility.DbInfo;
import frames.ViewFrame;
import helper.ControlHelper;
import helper.InputTextPaneInfo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class StudentsViewFrame extends ViewFrame {    
    private final StudentsService ss;
    private final CoursesService cs;
    private final EnrollmentsService es;
    
    private JButton createBtn;
    private JButton editBtn;
    private JButton deleteBtn;

    private JTextPane searchPane;

    // Needed in order to clear and reset list items
    private DefaultListModel<CourseModel> coursesListModel = new DefaultListModel<>();

    // Needed in order to know which thing is selected
    private JList<CourseModel> coursesList;

    // Needed in order to clear and reset list items
    private DefaultListModel<StudentModel> studentsListModel = new DefaultListModel<>();

    // Needed in order to know which thing is selected
    private JList<StudentModel> studentsList;

    public StudentsViewFrame(String frameTitle, DbInfo dbInfo) {
        super(frameTitle, dbInfo);
        cs = new CoursesService(dbInfo);
        es = new EnrollmentsService(dbInfo);
        ss = new StudentsService(dbInfo);
        
        this.setSize(new Dimension(1200,700));


        this.getContentPane().setBackground(Color.gray);
        this.setLayout(new FlowLayout());
        
        // Create list model
        coursesListModel = new DefaultListModel<>();

        this.add(createStudentsResultsPanel());
        this.add(createCoursesResultsPanel());
        this.add(createCrudBtnsPanel());
        addBtnEvenets();

        createSearchBarPanel();

        studentsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) { // This is true when the selection is final
                    StudentModel selectedStudent = studentsList.getSelectedValue();
                    if (selectedStudent != null) {
                        refreshCoursesListModel();
                    }else{
                        coursesListModel.clear();
                    }
                }
            }
        });
    }

    private JPanel createCrudBtnsPanel(){
        JPanel crudBtnsPanel = new JPanel();
        crudBtnsPanel.setLayout(new GridLayout(3,1));

        createBtn = ControlHelper.generateButton("Create New", new Dimension(120,40), 17, Color.white, Color.black, Color.black);
        editBtn = ControlHelper.generateButton("Edit Selected", new Dimension(120,40), 17, Color.white, Color.black, Color.black);
        deleteBtn = ControlHelper.generateButton("Delete Selected", new Dimension(120,40), 17, Color.white, Color.black, Color.black);
        
        crudBtnsPanel.add(createBtn);
        crudBtnsPanel.add(editBtn);
        crudBtnsPanel.add(deleteBtn);

        return crudBtnsPanel;
    }

    private void addBtnEvenets(){
        createBtn.addActionListener(e->{
            new StudentsCreateFrame(ss, cs, es, this::refreshAll);
        });

        editBtn.addActionListener(e->{
            if(studentsList.getSelectedIndex() == -1){
                return;
            }

            StudentModel student = studentsList.getSelectedValue();

            List<CourseModel> courses = new ArrayList<>(); 
            for (int i = 0; i < coursesListModel.size(); i++) {
                CourseModel m = coursesListModel.getElementAt(i);
                courses.add(m);
            }
            
            new StudentsEditFrame(ss,cs,es, this::refreshAll, student, courses);
        });

        deleteBtn.addActionListener(e->{
            if(studentsList.getSelectedIndex() == -1){
                return;
            }

            StudentModel m = studentsList.getSelectedValue();
            
            try {
                ss.deleteStudent(m.studentId, es);
            } catch (Exception ex) {
                System.err.println("Error: Couln't delete student");
            }
            refreshAll();
        });
    }

    public void searchBtnClicked(){
        if(searchPane.getText().isEmpty()){
            refreshAll();
            return;
        }

        coursesListModel.clear();
        studentsListModel.clear();

        List<StudentModel> filteredStudents = new ArrayList<>();
        try {
            StudentModel st = ss.getStudentByFacultyNumber(searchPane.getText());
            filteredStudents.add(st);
        } catch (Exception e) {
            System.err.println("Error: Student with particular faculty number couldn't be loaded");
        }

        for (StudentModel st : filteredStudents) {
            studentsListModel.addElement(st);
        }

        this.repaint();
        this.revalidate();
    }
    
    private void createSearchBarPanel(){
        JPanel searchBarPanel = new JPanel();
        searchBarPanel.setLayout(new GridLayout(1,3));
        
        // Create search input field
        searchPane = new JTextPane();
        
        InputTextPaneInfo[] infos = {
            new InputTextPaneInfo(searchPane, "Search by faculty number")
        };
        
        // Create search input panel
        JPanel inputPanel = ControlHelper.generateInputPanel(infos, 200);
        
        // Create search button
        JButton searchBtn = ControlHelper.generateDefaultButton("Search",80,40);
        
        searchBtn.addActionListener(e->{
           searchBtnClicked();
        });
        
        searchBarPanel.add(inputPanel);
        searchBarPanel.add(searchBtn);


        this.add(searchBarPanel);
    }


    // FOR COURSES
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
       // refreshCoursesListModel();

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
            allCourses = es.getAllEnrollmentsOfStudent(studentsList.getSelectedValue().studentId);
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
        //list.setSelectionMode(ListSelectionModel);
        list.setEnabled(false);
        
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


    // FOR STUDENTS

    private JPanel createStudentsResultsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER,10,100));

        panel.setSize(new Dimension(100, 200));
        panel.setMaximumSize(new Dimension(100,500));
        
        // Just a label
        JLabel lbl = ControlHelper.generateLabel("All Students", 15, Color.black);
        lbl.setPreferredSize(new Dimension(100,30));
        lbl.setMaximumSize(new Dimension(100,30));
        lbl.setMinimumSize(new Dimension(100,30));
        panel.add(lbl);
        //
        
        // Populate the List model with all departments
        refreshStudentsListModel();

        // Create a new list from the list model
        studentsList = createStudentsJList(studentsListModel);

        // Add the list to a scroll pane
        JScrollPane scrollPane = new JScrollPane(studentsList);
        scrollPane.setPreferredSize(new Dimension(350, 200));
        
        // Add the scroll pane to the main panel
        panel.add(scrollPane);

        return panel;
    }

    public void refreshStudentsListModel(){
        studentsListModel.clear();
        List<StudentModel> allStudents = null;
        try {
            allStudents = ss.getAllStudents();
        } catch (Exception e) {
            System.err.println("Error: Students couldn't be loaded");
        }

        for (StudentModel student : allStudents) {
            studentsListModel.addElement(student);
        }

        this.repaint();
        this.revalidate();
    }

    public JList<StudentModel> createStudentsJList(DefaultListModel<StudentModel> model){
        JList<StudentModel> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Create and set the custom renderer
        list.setCellRenderer(new ListCellRenderer<StudentModel>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends StudentModel> list, StudentModel value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = new JLabel(value.firstName + " " + value.middleName + " " + value.lastName);
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

    private void refreshAll(){
        coursesListModel.clear();
        refreshStudentsListModel();
    }
    

    
}
