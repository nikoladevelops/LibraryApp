package db.services;

import db.utility.DbInfo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbService {

    private final DbInfo dbInfo;

    public DbService(DbInfo dbInfo) {
        this.dbInfo = dbInfo;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbInfo.jdbcUrl, dbInfo.user, dbInfo.password);
    }
}
