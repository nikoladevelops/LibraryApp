package db.services;

import db.models.CourseModel;
import db.utility.DbInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CoursesService extends DbService {

    public CoursesService(DbInfo dbInfo) {
        super(dbInfo);
    }

    public List<CourseModel> getAllCourses() throws SQLException{
        String query = "SELECT * FROM courses";
        List<CourseModel> result = new ArrayList<>();

        try (Connection connection = getConnection(); 
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()
            ) {

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

    public void createCourse(String courseName, String description, int departmentId) throws SQLException {
        String query = "INSERT INTO courses (course_name, description, department_id) VALUES (?, ?, ?)";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, courseName);
            statement.setString(2, description);
            statement.setInt(3, departmentId);
            statement.executeUpdate();
        }
    }

    public ResultSet getCourse(int courseId) throws SQLException {
        String query = "SELECT * FROM courses WHERE course_id = ?";
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, courseId);
        return statement.executeQuery();
    }

    public void updateCourse(int courseId, String courseName, String description, int departmentId) throws SQLException {
        String query = "UPDATE courses SET course_name = ?, description = ?, department_id = ? WHERE course_id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, courseName);
            statement.setString(2, description);
            statement.setInt(3, departmentId);
            statement.setInt(4, courseId);
            statement.executeUpdate();
        }
    }

    public void deleteCourse(int courseId, EnrollmentsService enrollmentService, TeachesService teachesService) throws SQLException {
        // First delete any dependent records in other tables
        deleteDependentEnrollmentsForCourse(courseId, enrollmentService);
        deleteDependentTeachesForCourse(courseId, teachesService);

        String query = "DELETE FROM courses WHERE course_id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, courseId);
            statement.executeUpdate();
        }
    }

    private void deleteDependentEnrollmentsForCourse(int courseId, EnrollmentsService enrollmentService) throws SQLException {
        String query = "SELECT enrollment_id FROM enrollments WHERE course_id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, courseId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int enrollmentId = resultSet.getInt("enrollment_id");
                enrollmentService.deleteEnrollment(enrollmentId);
            }
        }
    }

    private void deleteDependentTeachesForCourse(int courseId, TeachesService teachesService) throws SQLException {
        String query = "SELECT professor_id FROM teaches WHERE course_id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, courseId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int professorId = resultSet.getInt("professor_id");
                teachesService.deleteTeaches(professorId, courseId);
            }
        }
    }

}
