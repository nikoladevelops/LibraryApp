package frames.courses;

import db.models.CourseModel;
import db.models.DepartmentModel;
import db.services.CoursesService;
import db.services.DepartmentsService;
import db.services.EnrollmentsService;
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

public class CoursesViewFrame extends ViewFrame {

    private final DepartmentsService ds;
    private final CoursesService cs;
    private final EnrollmentsService es;
    private final TeachesService ts;
    
    private JButton createBtn;
    private JButton editBtn;
    private JButton deleteBtn;

    private JTextPane searchPane;

    // Needed in order to clear and reset list items
    private DefaultListModel<CourseModel> listModel = new DefaultListModel<>();

    // Needed in order to know which thing is selected
    private JList<CourseModel> list;

    public CoursesViewFrame(String frameTitle, DbInfo dbInfo) {
        super(frameTitle, dbInfo);
        ds = new DepartmentsService(dbInfo);
        cs = new CoursesService(dbInfo);
        es = new EnrollmentsService(dbInfo);
        ts = new TeachesService(dbInfo);
        


        this.getContentPane().setBackground(Color.gray);
        this.setLayout(new FlowLayout());
        
        // Create list model
        listModel = new DefaultListModel<>();

        this.add(createResultsPanel());
        this.add(createCrudBtnsPanel());
        addBtnEvenets();

        createSearchBarPanel();


    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER,10,100));

        panel.setSize(new Dimension(220, 500));
        panel.setMaximumSize(new Dimension(100,500));
        
        // Just a label
        JLabel lbl = ControlHelper.generateLabel("All Courses", 15, Color.black);
        lbl.setPreferredSize(new Dimension(100,30));
        lbl.setMaximumSize(new Dimension(100,30));
        lbl.setMinimumSize(new Dimension(100,30));
        panel.add(lbl);
        //
        
        // Populate the List model with all departments
        refreshListModel();

        // Create a new list from the list model
        list = createJList(listModel);

        // Add the list to a scroll pane
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(350, 200));
        
        // Add the scroll pane to the main panel
        panel.add(scrollPane);

        return panel;
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
            new CoursesCreateFrame(cs, ds, this::refreshListModel);
        });

        editBtn.addActionListener(e->{
            if(list.getSelectedIndex() == -1){
                return;
            }

            CourseModel m = list.getSelectedValue();
            
            new CoursesEditFrame(cs, ds, this::refreshListModel, list.getSelectedValue());
        });

        deleteBtn.addActionListener(e->{
            if(list.getSelectedIndex() == -1){
                return;
            }

            CourseModel m = list.getSelectedValue();

            try {
                cs.deleteCourse(m.courseId, es, ts);
            } catch (Exception ex) {
                System.err.println("Error: Couln't delete course");
            }
            refreshListModel();
        });
    }
    
    public void refreshListModel(){
        listModel.clear();
        List<CourseModel> allCourses = null;
        try {
            allCourses = cs.getAllCourses();
        } catch (Exception e) {
            System.err.println("Error: Courses couldn't be loaded");
        }

        for (CourseModel course : allCourses) {
            listModel.addElement(course);
        }

        this.repaint();
        this.revalidate();
    }

    public void searchBtnClicked(){
        listModel.clear();
        List<CourseModel> allCourses = null;
        try {
            allCourses = cs.getCoursesWithName(searchPane.getText());
        } catch (Exception e) {
            System.err.println("Error: Courses with particular name couldn't be loaded");
        }

        for (CourseModel course : allCourses) {
            listModel.addElement(course);
        }

        this.repaint();
        this.revalidate();
    }
    public JList<CourseModel> createJList(DefaultListModel<CourseModel> model){
        JList<CourseModel> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
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

    private void createSearchBarPanel(){
        JPanel searchBarPanel = new JPanel();
        searchBarPanel.setLayout(new GridLayout(1,3));
        
        // Create search input field
        searchPane = new JTextPane();
        
        InputTextPaneInfo[] infos = {
            new InputTextPaneInfo(searchPane, "Search by name")
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
}
