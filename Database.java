import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

    private static final String url = "jdbc:mysql://localhost:3306/stock";
    private static final String user = "root";
    private static final String password = "123456";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // ✅ REQUIRED
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null; // ✅ return null so app won’t crash
        }
    }
}
