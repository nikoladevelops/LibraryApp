package db.services;

import db.utility.DbInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentsService extends DbService {

    public StudentsService(DbInfo dbInfo) {
        super(dbInfo);
    }

    public void createStudent(String name, String email) throws SQLException {
        String query = "INSERT INTO students (name, email) VALUES (?, ?)";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.executeUpdate();
        }
    }

    public ResultSet getStudent(int studentId) throws SQLException {
        String query = "SELECT * FROM students WHERE student_id = ?";
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, studentId);
        return statement.executeQuery();
    }

    public void updateStudent(int studentId, String name, String email) throws SQLException {
        String query = "UPDATE students SET name = ?, email = ? WHERE student_id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setInt(3, studentId);
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
