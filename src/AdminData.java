import java.sql.*;

public class AdminData {
    private Connection connection;

    public AdminData(String username, String password) {
        try {
            if (AdminExists(username, password)) {
                System.out.println("Connection to Database Successful");
                this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_time_attendance", username, password);
            }

            else{
                System.out.println("Admin does not exist");
                this.connection = null;
            }
        } catch (SQLException e) {
            System.out.println("Invalid username or password");
        }
    }

    public boolean AdminExists(String username, String password) throws SQLException {
        String adminDef = "adminChecker";
        String passwordDef = "password";

        try (Connection adminCheck = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_time_attendance", adminDef, passwordDef)) {
            Statement statement = adminCheck.createStatement();
            System.out.println("Checking if user exists");
            System.out.println("Username: " + username + " Password: " + password);

            ResultSet rs = statement.executeQuery("SELECT * FROM t_admin WHERE username = '" + username.trim() + "' AND password = '" + password.trim() + "';");

            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error while connecting to the database: " + e.getMessage());
        }

        return false;
    }



    public void makeQuery(String query) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        ResultSetToString(rs);


    }

    public void ResultSetToString(ResultSet rs) throws SQLException {
        System.out.println("ResultSet: ");

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            System.out.print(metaData.getColumnName(i) + "\t");
        }
        System.out.println();

        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(rs.getString(i) + "\t");
            }
            System.out.println();
        }
    }


    public Connection getConnection() {
        return connection;
    }
}
