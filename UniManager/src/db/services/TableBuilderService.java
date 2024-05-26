package db.services;

import db.utility.DbInfo;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TableBuilderService extends DbService {

    public TableBuilderService(DbInfo dbInfo) {
        super(dbInfo);
    }

    /**
     * Stores department information with a unique department_id
     *
     * @return Query String needed to create the Departments table
     */
    private final String createDepartmentsTableQuery = "CREATE TABLE IF NOT EXISTS departments ("
            + "department_id INT AUTO_INCREMENT, "
            + "department_name VARCHAR(255), "
            + "PRIMARY KEY(department_id)"
            + ")";

    /**
     * Stores student information
     *
     * @return Query String needed to create the Students table
     */
    private final String createStudentsTableQuery = "CREATE TABLE IF NOT EXISTS students ("
            + "student_id INT AUTO_INCREMENT, "
            + "name VARCHAR(255), "
            + "email VARCHAR(255), "
            + "faculty_number VARCHAR(255)"
            + "PRIMARY KEY(student_id)"
            + ")";

    /**
     * Stores the available courses, also links which course belongs to which
     * department
     *
     * @return Query String needed to create the Courses table
     */
    private final String createCoursesTableQuery = "CREATE TABLE IF NOT EXISTS courses ("
            + "course_id INT AUTO_INCREMENT, "
            + "course_name VARCHAR(255), "
            + "description TEXT, "
            + "department_id INT, "
            + "PRIMARY KEY(course_id), "
            + "FOREIGN KEY(department_id) REFERENCES departments(department_id)"
            + ")";

    /**
     * Stores information about student enrollment in defferent courses
     *
     * @return Query String needed to create the Entrollments table
     */
    private final String createEnrollmentsTableQuery = "CREATE TABLE IF NOT EXISTS enrollments ("
            + "enrollment_id INT AUTO_INCREMENT, "
            + "student_id INT, "
            + "course_id INT, "
            + "enrollment_date DATE, "
            + "PRIMARY KEY(enrollment_id), "
            + "FOREIGN KEY(student_id) REFERENCES students(student_id), "
            + "FOREIGN KEY(course_id) REFERENCES courses(course_id)"
            + ")";

    /**
     * Stores information about professors and the department that they
     * specialize in
     *
     * @return Query String needed to create the Professors table
     */
    private final String createProfessorsTableQuery = "CREATE TABLE IF NOT EXISTS professors ("
            + "professor_id INT AUTO_INCREMENT, "
            + "name VARCHAR(255), "
            + "email VARCHAR(255), "
            + "department_id INT, "
            + "PRIMARY KEY(professor_id), "
            + "FOREIGN KEY(department_id) REFERENCES departments(department_id)"
            + ")";

    /**
     * Stores information about the professors and the courses that they teach
     *
     * @return Query String needed to create the Teaches table
     */
    private final String createTeachesTableQuery = "CREATE TABLE IF NOT EXISTS teaches ("
            + "professor_id INT, "
            + "course_id INT, "
            + "PRIMARY KEY(professor_id, course_id), "
            + "FOREIGN KEY(professor_id) REFERENCES professors(professor_id), "
            + "FOREIGN KEY(course_id) REFERENCES courses(course_id)"
            + ")";

    /**
     * Creates all necessary tables in the correct order for the UniManager if
     * they don't already exist
     */
    public void ensureTablesCreated() {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            statement.execute(createStudentsTableQuery);
            statement.execute(createDepartmentsTableQuery);
            statement.execute(createProfessorsTableQuery);
            statement.execute(createCoursesTableQuery);
            statement.execute(createEnrollmentsTableQuery);
            statement.execute(createTeachesTableQuery);
            
            System.err.println("SUCCESS: All tables are created");
        } catch (SQLException e) {
            System.err.println("ERROR: Couldn't create database tables");
        }

    }
    /**
     * Drops every single table
     *  */
    public void dropAllTables() {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            // Drop tables in reverse order of creation
            statement.executeUpdate("DROP TABLE IF EXISTS teaches");
            statement.executeUpdate("DROP TABLE IF EXISTS enrollments");
            statement.executeUpdate("DROP TABLE IF EXISTS courses");
            statement.executeUpdate("DROP TABLE IF EXISTS professors");
            statement.executeUpdate("DROP TABLE IF EXISTS departments");
            statement.executeUpdate("DROP TABLE IF EXISTS students");

            System.err.println("SUCCESS: All tables are dropped");
        } catch (SQLException e) {
            System.err.println("ERROR: Couldn't drop database tables - " + e.getMessage());
        }
    }

}
