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



    public String makeQuery(String query) throws SQLException {
        Statement statement = connection.createStatement();
        String[] lines = query.split("\n");
        StringBuilder res = new StringBuilder();

        for (String line : lines) {
            if (line.trim().length() >= 6 && line.trim().substring(0, 6).equalsIgnoreCase("SELECT")) {
                ResultSet resultSet = statement.executeQuery(line);
                res.append(ResultSetToString(resultSet));
            }
            else {
                statement.executeUpdate(line);
            }
        }

        return res.toString();
    }


    public String ResultSetToString(ResultSet rs) throws SQLException {
        System.out.println("ResultSet: ");

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        StringBuilder result = new StringBuilder();

        for (int i = 1; i <= columnCount; i++) {
            result.append(metaData.getColumnName(i)).append("\t");
        }
        result.append("\n");

        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                result.append(rs.getString(i)).append("\t");
            }
            result.append("\n");
        }

        System.out.println(result.toString());

        return result.toString();
    }



    public Connection getConnection() {
        return connection;
    }
}
