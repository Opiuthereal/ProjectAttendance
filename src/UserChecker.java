import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserChecker {
    private int id;
    private Connection connection;


    public UserChecker(String name, String password) throws SQLException {
        String userPass = "userpassword";
        this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_time_attendance", "user", userPass);

        if (userExists(name, password)){
            this.id = this.getFirstId(name, password);
            System.out.println(this.id);
        }

        else{
            System.out.println("User does not exists");
            this.id = -1;
            this.connection = null;
            throw new SQLException("Wrong username or password");
        }
    }

    public boolean userExists(String name, String password) throws SQLException {
        Statement statement = connection.createStatement();
        System.out.println("Checking if user exists");
        String query = "SELECT * FROM t_emp WHERE name = '" + name + "' AND code = '" + password + "'";
        ResultSet rs = statement.executeQuery(query);
        if (rs.next()) {
            System.out.println("User exists");
            return true;
        }
        return false;
    }

    public int getFirstId(String name, String password) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "SELECT id FROM t_emp WHERE name = '" + name + "' AND code = '" + password + "'";
        ResultSet rs = statement.executeQuery(query);
        rs.next();
        int id = rs.getInt(1);
        System.out.println("First id is " + id);
        return id;
    }

    public int getId(){
        return this.id;
    }

    public void Attendance (String formattedDateTime) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "INSERT INTO t_lock_in_record (id, check_in_time) VALUES (" + id + ",'" + formattedDateTime + "') ";
        System.out.println("Test insertion date");
        statement.executeUpdate(query);
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
}
