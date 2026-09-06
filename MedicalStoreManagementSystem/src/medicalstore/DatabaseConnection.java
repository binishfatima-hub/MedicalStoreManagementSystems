package medicalstore;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/medical_store";
            String username = "root";
            String password = "fatima";

            return DriverManager.getConnection(url, username, password);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {

        Connection con = getConnection();

        if (con != null) {
            System.out.println("Database Connected Successfully!");
        } else {
            System.out.println("Database Connection Failed!");
        }
    }
}

