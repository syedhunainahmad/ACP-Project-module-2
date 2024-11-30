import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:XE"; // Update with your DB URL
    private static final String USER = "system"; // Update with your DB username
    private static final String PASSWORD = "hr"; // Update with your DB password

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }
}
