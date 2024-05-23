package db.services;

import db.utility.DbInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DepartmentsService extends DbService {

    public DepartmentsService(DbInfo dbInfo) {
        super(dbInfo);
    }

    public void createDepartment(String departmentName) throws SQLException {
        String query = "INSERT INTO departments (department_name) VALUES (?)";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, departmentName);
            statement.executeUpdate();
        }
    }

    public ResultSet getDepartment(int departmentId) throws SQLException {
        String query = "SELECT * FROM departments WHERE department_id = ?";
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, departmentId);
        return statement.executeQuery();
    }

    public void updateDepartment(int departmentId, String departmentName) throws SQLException {
        String query = "UPDATE departments SET department_name = ? WHERE department_id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, departmentName);
            statement.setInt(2, departmentId);
            statement.executeUpdate();
        }
    }

    public void deleteDepartment(int departmentId, CoursesService coursesService, EnrollmentsService enrollmentService, TeachesService teachesService) throws SQLException {
        // First delete any dependent records in other tables
        deleteDependentCourses(departmentId, coursesService, enrollmentService, teachesService);

        String query = "DELETE FROM departments WHERE department_id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, departmentId);
            statement.executeUpdate();
        }
    }

    private void deleteDependentCourses(int departmentId, CoursesService coursesService, EnrollmentsService enrollmentService, TeachesService teachesService) throws SQLException {
        String query = "SELECT course_id FROM courses WHERE department_id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, departmentId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int courseId = resultSet.getInt("course_id");
                coursesService.deleteCourse(courseId, enrollmentService, teachesService);
            }
        }
    }
}
