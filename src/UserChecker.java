import java.sql.*;

public class UserChecker {
    int id;
    private Connection connection;


    public UserChecker(String name, String password) throws SQLException {
        String userPass = "userpassword";
        this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_time_attendance", "user", userPass);

        if (userExists(name, password)){
            this.id = this.getFirstId(name, password);
            System.out.println("User exists" + this.id);
            System.out.println("User created succesfully");
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
            return true; // Admin exists
        }
        return false;
    }

    public int getFirstId(String name, String password) throws SQLException {
        Statement statement = connection.createStatement();
        // trouver l'id du frérot ////////////////////////////
        return 1;
    }

    public int getId(){
        return this.id;
    }

    public void Attendance () throws SQLException {
        String time = "Current time"; // entrer la date //////////////////////////////////////////////////
        Statement statement = connection.createStatement();
        String query = "FROM t_work_time INSERT this.id "; // remplir avec le query pour la connection /////////////////////////
        statement.executeUpdate(query);
    }
}
