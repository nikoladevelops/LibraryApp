package db.services;

import db.utility.DbInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeachesService extends DbService {

    public TeachesService(DbInfo dbInfo) {
        super(dbInfo);
    }

    public void createTeaches(int professorId, int courseId) throws SQLException {
        String query = "INSERT INTO teaches (professor_id, course_id) VALUES (?, ?)";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, professorId);
            statement.setInt(2, courseId);
            statement.executeUpdate();
        }
    }

    public ResultSet getTeaches(int professorId, int courseId) throws SQLException {
        String query = "SELECT * FROM teaches WHERE professor_id = ? AND course_id = ?";
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, professorId);
        statement.setInt(2, courseId);
        return statement.executeQuery();
    }

    public void updateTeaches(int professorId, int courseId) throws SQLException {
        // As there is a composite primary key, we can't update both keys
        // Typically you would delete the old record and insert a new one
        deleteTeaches(professorId, courseId);
        createTeaches(professorId, courseId);
    }

    public void deleteTeaches(int professorId, int courseId) throws SQLException {
        String query = "DELETE FROM teaches WHERE professor_id = ? AND course_id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, professorId);
            statement.setInt(2, courseId);
            statement.executeUpdate();
        }
    }
}
