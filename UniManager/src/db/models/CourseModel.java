package db.models;

public class CourseModel {
    public int courseId;
    public String courseName;
    public String description;
    public int departmentId;

    public CourseModel(int courseId, String courseName, String description, int departmentId){
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
        this.departmentId=departmentId;
    }
}
