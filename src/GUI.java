import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GUI {
    private static JFrame window = new JFrame();

    public void mainWindow() {
        window.getContentPane().removeAll();
        window.setBounds(200, 200, 400, 200);
        window.setLayout(null);


        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("employee");
        JMenuItem it1 = new JMenuItem("employee");
        JMenuItem it2 = new JMenuItem("admin");
        JMenuItem it3 = new JMenuItem("disconnect");

        fileMenu.add(it1);
        fileMenu.add(it2);
        fileMenu.add(it3);
        menuBar.add(fileMenu);
        window.setJMenuBar(menuBar);

        JLabel label = new JLabel("name:");
        label.setBounds(50, 20, 150, 25);
        window.add(label);

        JTextField username = new JTextField(20);
        username.setBounds(200, 20, 130, 25);
        window.add(username);

        JLabel label2 = new JLabel("Password:");
        label2.setBounds(50, 50, 150, 20);
        window.add(label2);

        JPasswordField password = new JPasswordField(20);
        password.setBounds(200, 50, 130, 25);
        window.add(password);

        JButton login = new JButton("Login");
        login.setBounds(130, 80, 130, 25);
        window.add(login);

        actionListenLogin(password, fileMenu, username, login);
        actionListenItem1(fileMenu, label, it1);
        actionListenItem2(fileMenu, label, it2);
        actionListenItem3(it3);

        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
    }

    public void adminWindow(AdminData admin) {
        window.getContentPane().removeAll();
        window.setBounds(200, 200, 800, 600);
        JTextArea textArea = new JTextArea(20, 30);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBounds(10, 10, 770, 270);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(10, 10, 770, 270);

        window.add(scrollPane);

        JTextArea displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);
        JScrollPane displayScrollPane = new JScrollPane(displayArea);
        displayScrollPane.setBounds(10, 305, 770, 250);
        window.add(displayScrollPane);

        JButton login = new JButton("Execute");
        login.setBounds(10, 280, 130, 25);
        window.add(login);

        ActionListenTerminal(textArea, admin, displayArea, login);

        window.revalidate();
        window.repaint();
    }

    public void ActionListenTerminal(JTextArea textArea, AdminData admin, JTextArea displayArea, JButton login) {
        ActionListener execute = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text = textArea.getText();
                System.out.println("Text Area Content: \n" + text);
                try {
                    String res = admin.makeQuery(text);
                    displayArea.setText(res);
                } catch (SQLException ex) {
                    displayArea.setText(ex.getMessage());
                    throw new RuntimeException(ex);
                }

                window.revalidate();
                window.repaint();
            }
        };

        login.addActionListener(execute);
    }

    public void actionListenLogin(JPasswordField password, JMenu fileMenu, JTextField username, JButton login) {
        ActionListener loging = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                char[] pass = password.getPassword();
                String passwordString = new String(pass);

                if (fileMenu.getText().equals("employee")) {
                    try {
                        UserChecker user = new UserChecker(username.getText().trim(), passwordString);
                        if (user.getId() > -1) {
                            userWindow(user);
                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JLabel label2 = new JLabel("Wrong username or password");
                        label2.setForeground(Color.RED);
                        label2.setBounds(100, 110, 200, 25);
                        window.add(label2);
                        window.revalidate();
                        window.repaint();
                    }
                } else {
                    AdminData admin = new AdminData(username.getText().trim(), passwordString);
                    if (admin.getConnection() != null) {
                        adminWindow(admin);
                    } else {
                        JLabel label2 = new JLabel("Wrong username or password");
                        label2.setForeground(Color.RED);
                        label2.setBounds(100, 110, 200, 25);
                        window.add(label2);
                        window.revalidate();
                        window.repaint();
                    }
                }

            }
        };
        login.addActionListener(loging);
    }

    public void userWindow(UserChecker user) {
        window.getContentPane().removeAll();
        JButton present = new JButton("attend as present");
        present.setBounds(90, 30, 200, 25);

        actionListenAttend(present, user);

        window.add(present);
        window.revalidate();
        window.repaint();
        System.out.println("déclanché");
    }

    public void actionListenAttend(JButton button, UserChecker user) {
        ActionListener attend = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LocalDateTime now = LocalDateTime.now();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = now.format(formatter);

                JLabel label2 = new JLabel("Login: " + formattedDateTime);
                label2.setBounds(110, 80, 200, 25);

                window.add(label2);
                window.revalidate();
                window.repaint();
                try {
                    user.Attendance(formattedDateTime);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        button.addActionListener(attend);
    }

    public void actionListenItem1(JMenu fileMenu, JLabel label,  JMenuItem it1){
        ActionListener item1 = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileMenu.setText("employee");
                label.setText("name:");
            }
        };
        it1.addActionListener(item1);
    }

    public void actionListenItem2(JMenu fileMenu, JLabel label, JMenuItem it2){
        ActionListener item2 = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileMenu.setText("admin");
                label.setText("username:");
            }
        };
        it2.addActionListener(item2);
    }
    private void actionListenItem3(JMenuItem it3) {
        ActionListener item3 = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainWindow();
            }
        };
        it3.addActionListener(item3);
    }

    public static void main(String[] args) {
        GUI gui = new GUI();
        gui.mainWindow();
    }
}

