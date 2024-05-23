import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String JDBC_URL = "jdbc:h2:~/test"; // Database URL
    private static final String USER = "sa"; // Default user
    private static final String PASSWORD = ""; // Default password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            // Example: Create a table
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT, name VARCHAR(255), PRIMARY KEY(id))";
            statement.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertSampleData() {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            // Insert sample data
            String insertSQL1 = "INSERT INTO users (name) VALUES ('John Doe')";
            String insertSQL2 = "INSERT INTO users (name) VALUES ('Jane Smith')";
            statement.execute(insertSQL1);
            statement.execute(insertSQL2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static String getUserName(){
        String name = "";
        // Example: Retrieve data from the database
        try (Connection connection = DatabaseManager.getConnection(); Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM users";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                name = resultSet.getString("name");
                System.out.println("User: " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            name="error";
        }

        return name;
    }
}