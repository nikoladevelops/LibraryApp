package db.services;

import db.utility.DbInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfessorsService extends DbService {

    public ProfessorsService(DbInfo dbInfo) {
        super(dbInfo);
    }

    public void createProfessor(String name, String email, int departmentId) throws SQLException {
        String query = "INSERT INTO professors (name, email, department_id) VALUES (?, ?, ?)";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setInt(3, departmentId);
            statement.executeUpdate();
        }
    }

    public ResultSet getProfessor(int professorId) throws SQLException {
        String query = "SELECT * FROM professors WHERE professor_id = ?";
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, professorId);
        return statement.executeQuery();
    }

    public void updateProfessor(int professorId, String name, String email, int departmentId) throws SQLException {
        String query = "UPDATE professors SET name = ?, email = ?, department_id = ? WHERE professor_id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setInt(3, departmentId);
            statement.setInt(4, professorId);
            statement.executeUpdate();
        }
    }

    public void deleteProfessor(int professorId, TeachesService teachesService) throws SQLException {
        // First delete any dependent records in other tables
        deleteDependentTeachesForProfessor(professorId, teachesService);

        String query = "DELETE FROM professors WHERE professor_id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, professorId);
            statement.executeUpdate();
        }
    }

    private void deleteDependentTeachesForProfessor(int professorId, TeachesService teachesService) throws SQLException {
        String query = "SELECT course_id FROM teaches WHERE professor_id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, professorId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int courseId = resultSet.getInt("course_id");
                teachesService.deleteTeaches(professorId, courseId);
            }
        }
    }

}
