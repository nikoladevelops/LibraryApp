package db.services;

import db.models.StudentModel;
import db.utility.DbInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StudentsService extends DbService {

    public StudentsService(DbInfo dbInfo) {
        super(dbInfo);
    }

    public List<StudentModel> getAllStudents() throws SQLException {
        String query = "SELECT * FROM students";
        List<StudentModel> result = new ArrayList<>();

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int studentId = resultSet.getInt("student_id");
                String firstName = resultSet.getString("first_name");
                String middleName = resultSet.getString("middle_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String facultyNumber = resultSet.getString("faculty_number");

                result.add(new StudentModel(studentId, firstName, middleName, lastName, email, facultyNumber));
            }

            return result;
        }
    }

    public int createStudent(String firstName, String middleName, String lastName, String email, String facultyNumber) throws SQLException {
        String query = "INSERT INTO students (first_name, middle_name, last_name, email, faculty_number) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, firstName);
            statement.setString(2, middleName);
            statement.setString(3, lastName);
            statement.setString(4, email);
            statement.setString(5, facultyNumber);
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return the generated ID
                } else {
                    throw new SQLException("Creating student failed, no ID obtained.");
                }
            }
        }
    }

    public ResultSet getStudent(int studentId) throws SQLException {
        String query = "SELECT * FROM students WHERE student_id = ?";
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, studentId);
        return statement.executeQuery();
    }

    public void updateStudent(int studentId, String firstName, String middleName, String lastName, String email, String facultyNumber) throws SQLException {
        String query = "UPDATE students SET first_name = ?, middle_name = ?, last_name = ?, email = ?, faculty_number = ? WHERE student_id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, firstName);
            statement.setString(2, middleName);
            statement.setString(3, lastName);
            statement.setString(4, email);
            statement.setString(5, facultyNumber);
            statement.setInt(6, studentId);
            statement.executeUpdate();
        }
    }

    public void deleteStudent(int studentId, EnrollmentsService enrollmentsService) throws SQLException {
        // First delete any dependent records in other tables
        deleteDependentEnrollmentsForStudent(studentId, enrollmentsService);

        String query = "DELETE FROM students WHERE student_id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, studentId);
            statement.executeUpdate();
        }
    }

    private void deleteDependentEnrollmentsForStudent(int studentId, EnrollmentsService enrollmentsService) throws SQLException {
        String query = "SELECT enrollment_id FROM enrollments WHERE student_id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int enrollmentId = resultSet.getInt("enrollment_id");
                enrollmentsService.deleteEnrollment(enrollmentId);
            }
        }
    }

}
