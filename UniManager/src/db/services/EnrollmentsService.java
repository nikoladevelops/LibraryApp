package db.services;

import db.models.CourseModel;
import db.utility.DbInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentsService extends DbService {

    public EnrollmentsService(DbInfo dbInfo) {
        super(dbInfo);
    }

    public List<CourseModel> getAllEnrollmentsOfStudent(int student_id) throws SQLException {
        String query = 
        "SELECT * "
        + "FROM enrollments e " 
        + "JOIN courses c ON e.course_id = c.course_id " 
        + "WHERE e.student_id = ?";
        List<CourseModel> result = new ArrayList<>();

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query);) {

            statement.setInt(1, student_id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int courseId = resultSet.getInt("course_id");
                String courseName = resultSet.getString("course_name");
                String description = resultSet.getString("description");
                int departmentId = resultSet.getInt("department_id");

                result.add(new CourseModel(courseId, courseName, description, departmentId));
            }

            return result;
        }
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

    public void deleteEnrollmentOfStudent(int studentId) throws SQLException {
        String query = "DELETE FROM enrollments WHERE student_id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, studentId);
            statement.executeUpdate();
        }
    }

}
