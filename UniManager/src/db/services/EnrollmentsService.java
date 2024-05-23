package db.services;

import db.utility.DbInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnrollmentsService extends DbService {

    public EnrollmentsService(DbInfo dbInfo) {
        super(dbInfo);
    }

    public void createEnrollment(int studentId, int courseId, java.sql.Date enrollmentDate) throws SQLException {
        String query = "INSERT INTO enrollments (student_id, course_id, enrollment_date) VALUES (?, ?, ?)";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            statement.setDate(3, enrollmentDate);
            statement.executeUpdate();
        }
    }

    public ResultSet getEnrollment(int enrollmentId) throws SQLException {
        String query = "SELECT * FROM enrollments WHERE enrollment_id = ?";
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, enrollmentId);
        return statement.executeQuery();
    }

    public void updateEnrollment(int enrollmentId, int studentId, int courseId, java.sql.Date enrollmentDate) throws SQLException {
        String query = "UPDATE enrollments SET student_id = ?, course_id = ?, enrollment_date = ? WHERE enrollment_id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            statement.setDate(3, enrollmentDate);
            statement.setInt(4, enrollmentId);
            statement.executeUpdate();
        }
    }

    public void deleteEnrollment(int enrollmentId) throws SQLException {
        String query = "DELETE FROM enrollments WHERE enrollment_id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, enrollmentId);
            statement.executeUpdate();
        }
    }

}
