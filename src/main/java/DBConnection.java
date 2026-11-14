import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/lost_found_db";

    private static final String USER = "root";

    // Enter your MySQL password here
    private static final String PASSWORD = "";

    public static void getConnection() {

        try {
            Connection con = DriverManager.getConnection(
                    URL, USER, PASSWORD);

            System.out.println("Database Connected Successfully!");

        } catch (SQLException e) {
            System.out.println("Database Connection Failed!");
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        getConnection();
    }
}