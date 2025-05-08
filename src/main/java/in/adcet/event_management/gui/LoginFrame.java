package in.adcet.event_management.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import in.adcet.event_management.service.UserService;
import in.adcet.event_management.entity.User;

public class LoginFrame extends JFrame {

    JTextField usernameField;
    JPasswordField passwordField;
    JComboBox<String> roleCombo;
    JCheckBox showPassword, rememberMe;
    private UserService userService = new UserService();

    public LoginFrame() {
        setTitle("Login - ADCET EMS");
        setSize(900, 700);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // College Banner
        ImageIcon collegeBanner = new ImageIcon(getClass().getClassLoader().getResource("images/ADCET1.jpg"));
        Image img = collegeBanner.getImage().getScaledInstance(900, 200, Image.SCALE_SMOOTH);
        JLabel bannerLabel = new JLabel(new ImageIcon(img));
        add(bannerLabel, BorderLayout.NORTH);

        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        add(mainPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Centered Login Heading
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel loginTitle = new JLabel("Login", JLabel.CENTER);
        loginTitle.setFont(new Font("Arial", Font.BOLD, 36));
        loginTitle.setForeground(new Color(26, 81, 199));
        loginTitle.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(loginTitle, gbc);

        Font labelFont = new Font("Arial", Font.PLAIN, 16);

        // Username
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(labelFont);
        mainPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(18);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBackground(Color.WHITE);
        usernameField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        mainPanel.add(usernameField, gbc);

        // Password
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(labelFont);
        mainPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(18);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBackground(Color.WHITE);
        passwordField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        mainPanel.add(passwordField, gbc);

        // Show Password
        gbc.gridy = 3;
        gbc.gridx = 1;
        showPassword = new JCheckBox("Show Password");
        showPassword.setFont(new Font("Arial", Font.PLAIN, 13));
        showPassword.setBackground(new Color(240, 248, 255));
        showPassword.addActionListener(e -> {
            passwordField.setEchoChar(showPassword.isSelected() ? (char) 0 : '•');
        });
        mainPanel.add(showPassword, gbc);

        // Role
        gbc.gridy = 4;
        gbc.gridx = 0;
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(labelFont);
        mainPanel.add(roleLabel, gbc);

        gbc.gridx = 1;
        String[] roles = {"Select Role", "Student", "Admin"};
        roleCombo = new JComboBox<>(roles);
        roleCombo.setFont(new Font("Arial", Font.BOLD, 14));
        roleCombo.setBackground(Color.WHITE);
        roleCombo.setForeground(new Color(0, 0, 120));
        mainPanel.add(roleCombo, gbc);

        // Remember Me
        gbc.gridy = 5;
        gbc.gridx = 1;
        rememberMe = new JCheckBox("Remember Me");
        rememberMe.setFont(new Font("Arial", Font.PLAIN, 13));
        rememberMe.setBackground(new Color(240, 248, 255));
        mainPanel.add(rememberMe, gbc);

        // Buttons
        gbc.gridy = 6;
        gbc.gridx = 1;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton loginBtn = createButton("Login");
        JButton clearBtn = createButton("Clear");
        JButton backBtn = createButton("Back");

        buttonPanel.add(loginBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(backBtn);
        mainPanel.add(buttonPanel, gbc);

        // Actions
        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String role = roleCombo.getSelectedItem().toString();

            if (username.isEmpty() || password.isEmpty() || role.equals("Select Role")) {
                JOptionPane.showMessageDialog(this, "Please fill all fields properly", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                User loggedInUser = userService.loginUser(username, password);
                if (loggedInUser == null) {
                    JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                } else if (!loggedInUser.getRole().equalsIgnoreCase(role)) {
                    JOptionPane.showMessageDialog(this, "Selected role does not match user role", "Login Failed", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, role + " login successful for: " + username);
                    if (role.equals("Admin")) {
                        new AdminDashboard(username);
                    } else if (role.equals("Student")) {
                        new StudentDashboard(username);
                    }
                    dispose();
                }
            }
        });

        clearBtn.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
            roleCombo.setSelectedIndex(0);
            rememberMe.setSelected(false);
            showPassword.setSelected(false);
            passwordField.setEchoChar('•');
        });

        backBtn.addActionListener(e -> {
            new WelcomeFrame();
            dispose();
        });

        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(100, 35));
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(26, 81, 199));
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(0, 64, 160));
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(26, 81, 199));
            }
        });
        return btn;
    }
}
