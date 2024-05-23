package db.utility;

public class DbInfo {
    public String jdbcUrl;
    public String user;
    public String password;

    public DbInfo(String jdbcUrl, String user, String password) {
        this.jdbcUrl = jdbcUrl;
        this.user = user;
        this.password = password;
    }
}
