package db.models;

public class StudentModel {
    public int studentId;
    public String firstName;
    public String middleName;
    public String lastName;
    public String email;
    public String facultyNumber;

    public StudentModel(int studentId, String firstName, String middleName, String lastName, String email, String facultyNumber){
        this.studentId = studentId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.facultyNumber = facultyNumber;
    }
}
