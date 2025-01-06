// créer un AdminData user dans la database et l'AdminData root afin de pouvoir les utiliser dans le code (peut être le faire appart de cette partie)

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class CreateDatabase {
    public static void flushUsers() {
        String url = "jdbc:mysql://localhost:3306/";
        String username = "root";
        String password = "password";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            String query = "SELECT User, Host FROM mysql.user WHERE User != 'root' "
                    + "AND User NOT IN ('mysql.infoschema', 'mysql.session', 'mysql.sys')";
            ResultSet resultSet = statement.executeQuery(query);

            List<String[]> usersToDrop = new ArrayList<>();

            while (resultSet.next()) {
                String user = resultSet.getString("User");
                String host = resultSet.getString("Host");
                usersToDrop.add(new String[]{user, host});
            }

            for (String[] userHost : usersToDrop) {
                String user = userHost[0];
                String host = userHost[1];

                String dropUserQuery = "DROP USER '" + user + "'@'" + host + "'";
                try {
                    statement.executeUpdate(dropUserQuery);
                    System.out.println("Dropped user: " + user + "@" + host);
                } catch (Exception e) {
                    System.err.println("Failed to drop user: " + user + "@" + host);
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws SQLException {
        // URL, username et password correspondant au localhost de notre SQL
        String url = "jdbc:mysql://localhost:3306/";
        String username = "root";
        String password = "password";

        String resetDatabase = "DROP DATABASE db_time_attendance;";
        String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS db_time_attendance;";
        String useDatabaseQuery = "USE db_time_attendance;";
        String createTableQuery = """
                CREATE TABLE IF NOT EXISTS t_emp (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name CHAR(20) NOT NULL,
                    code VARCHAR(36) NOT NULL
                );
                """;

        String createTable2Query = """
                CREATE TABLE IF NOT EXISTS t_lock_in_record (
                id INT,
                check_in_time DATETIME,
                FOREIGN KEY (id) REFERENCES t_emp(id))
                """;

        String createTable3Query = """
                CREATE TABLE IF NOT EXISTS t_admin (
                id INT,
                username VARCHAR(20) NOT NULL,
                password VARCHAR(20) NOT NULL,
                FOREIGN KEY (id) REFERENCES t_emp(id))

                """;

        String createTable4Query = """
                CREATE TABLE IF NOT EXISTS t_work_time (
                start TIME NOT NULL
                )
                """;

        String insertEmpRoot = "INSERT INTO t_emp (name, code) VALUES ('root', 'password')";
        String insertEmp2 = "INSERT INTO t_emp (name, code) VALUES ('Gabriel', 'pieds')";
        String insertEmp3 = "INSERT INTO t_emp (name, code) VALUES ('Mathis', 'password')";
        String insertEmp4 = "INSERT INTO t_emp (name, code) VALUES ('Aurian', 'password')";
        String insertAdminRoot = "INSERT INTO t_admin (id, username, password) VALUES (1,'root','password')";

        String newUser = "adminChecker";
        String newPassword = "password";
        String newUser1 = "user";
        String newPassword1 = "userpassword";
        String createUserQuery = "CREATE USER '" + newUser + "'@'localhost' IDENTIFIED BY '" + newPassword + "';";
        String createUserQuery1 = "CREATE USER '" + newUser1 + "'@'localhost'IDENTIFIED BY '" + newPassword1 + "';";

        String grantAccess1 = "GRANT SELECT ON db_time_attendance.t_admin TO '" + newUser + "'@'localhost';";
        String grantAccess2 = "GRANT SELECT ON db_time_attendance.t_emp TO '" + newUser + "'@'localhost';";
        String grantAccess11 = "GRANT SELECT ON db_time_attendance.t_emp TO '" + newUser1 + "'@'localhost';";
        String grantAccess12 = "GRANT INSERT, UPDATE, DELETE ON db_time_attendance.t_lock_in_record TO '"+ newUser1 + "'@'localhost';";


        flushUsers();

        try (
                // Connect to the MySQL server (no database specified yet)
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(resetDatabase);
            System.out.println("Databasse successfully deleted");

            statement.executeUpdate(createDatabaseQuery);
            System.out.println("Database created or already exists.");

            statement.executeUpdate(useDatabaseQuery);
            System.out.println("Switched to the database.");

            statement.executeUpdate(createTableQuery);
            System.out.println("Table 'users' created or already exists.");

            statement.executeUpdate(createTable2Query);
            System.out.println("Table 'records' created or already exists.");

            statement.executeUpdate(createTable3Query);
            System.out.println("Table 'admin' created or already exists.");

            statement.executeUpdate(createTable4Query);
            System.out.println("Table 'schedule' created or already exists.");

            statement.executeUpdate(insertEmpRoot);
            statement.executeUpdate(insertEmp2);
            statement.executeUpdate(insertEmp3);
            statement.executeUpdate(insertEmp4);
            System.out.println("Insertion of root and Gab as employee successfull");

            statement.executeUpdate(insertAdminRoot);
            System.out.println("Insertion of root as admin successfull");

            statement.executeUpdate(createUserQuery);
            statement.executeUpdate(createUserQuery1);
            System.out.println("Users created successfully");

            statement.executeUpdate(grantAccess1);
            statement.executeUpdate(grantAccess11);
            System.out.println("Grant accesses 1 successfully");

            statement.executeUpdate(grantAccess2);
            statement.executeUpdate(grantAccess12);
            System.out.println("Grant accesses 2 successfully");


        }

        catch (SQLException e) {
            System.out.println("Error by creating the database skeleton Error type: " + e.getMessage());
            e.printStackTrace();
        }

        //Create admiData
        AdminData root = new AdminData("root", "password");
        root.makeQuery("SELECT * FROM t_emp");
        UserChecker gab = new UserChecker("Gabriel", "pieds");

    }
}
