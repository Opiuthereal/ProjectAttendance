import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GUI {
    public static void userWindow(JFrame window, UserChecker user) {
        window.getContentPane().removeAll();
        JButton present = new JButton("attend as present");
        present.setBounds(90,30,200,25);

        eventAttend(window, present, user);

        window.add(present);
        window.revalidate();
        window.repaint();
        System.out.println("déclanché");
    }

    public static void eventAttend(JFrame window, JButton button, UserChecker user) {
        ActionListener attend = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LocalDateTime now = LocalDateTime.now();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = now.format(formatter);

                JLabel label2 = new JLabel("Login: " + formattedDateTime);
                label2.setBounds(110,80,200,25);

                window.add(label2);
                window.revalidate();
                window.repaint();
                insertLoginTime(user, formattedDateTime);
            }
        };
        button.addActionListener(attend);
    }

    public static void insertLoginTime(UserChecker user, String formattedDateTime) {
        System.out.println("à faire"); //Query pour login l'attendance de l'user ///////////////////////////////////////////
    }

    public static void main(String[] args) {
        JFrame window = new JFrame("Attendance system");
        window.setBounds(200, 200, 400, 200);
        window.setLayout(null);


        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("employee");
        JMenuItem it1 = new JMenuItem("employee");
        JMenuItem it2 = new JMenuItem("admin");

        fileMenu.add(it1);
        fileMenu.add(it2);
        menuBar.add(fileMenu);
        window.setJMenuBar(menuBar);

        JLabel label = new JLabel("Username:");
        label.setBounds(50,20,150,25);
        window.add(label);

        JTextField username = new JTextField(20);
        username.setBounds(200,20,130,25);
        window.add(username);

        JLabel label2 = new JLabel("Password:");
        label2.setBounds(50,50,150,20);
        window.add(label2);

        JPasswordField password = new JPasswordField(20);
        password.setBounds(200,50,130,25);
        window.add(password);

        JButton login = new JButton("Login");
        login.setBounds(130,80,130,25);
        window.add(login);


        ActionListener loging = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                char[] pass = password.getPassword();
                String passwordString = new String(pass);
                try {
                    UserChecker user = new UserChecker(username.getText(), passwordString);
                    if (user.getId() > -1){
                        userWindow(window, user);
                    }

                } catch (SQLException ex) {
                    JLabel label2 = new JLabel("Wrong username or password");
                    label2.setForeground(Color.RED);
                    label2.setBounds(100,110,200,25);
                    window.add(label2);
                    window.revalidate();
                    window.repaint();
                }
            }
        };

        ActionListener item1 = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileMenu.setText("employee");
                label.setText("name:");
            }
        };

        ActionListener item2 = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileMenu.setText("admin");
                label.setText("username:");
            }
        };

        login.addActionListener(loging);
        it1.addActionListener(item1);
        it2.addActionListener(item2);

        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
    }
}
