package in.adcet.event_management.gui;

import in.adcet.event_management.entity.User;
import in.adcet.event_management.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CreateAccountFrame extends JFrame {

    JTextField nameField, emailField, usernameField;
    JPasswordField passwordField, confirmPasswordField;
    JComboBox<String> roleCombo;
    JCheckBox agreeCheck;

    public CreateAccountFrame() {
        setTitle("Create Account - ADCET EMS");
        setSize(900, 700);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Banner
        ImageIcon collegeBanner = new ImageIcon(getClass().getClassLoader().getResource("images/ADCET1.jpg"));
        Image img = collegeBanner.getImage().getScaledInstance(900, 200, Image.SCALE_SMOOTH);
        JLabel bannerLabel = new JLabel(new ImageIcon(img));
        add(bannerLabel, BorderLayout.NORTH);

        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        add(mainPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel title = new JLabel("Create Account", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 34));
        title.setForeground(new Color(26, 81, 199));
        mainPanel.add(title, gbc);

        Font labelFont = new Font("Arial", Font.PLAIN, 16);

        // Name
        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0;
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(labelFont);
        mainPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        nameField = createTextField();
        mainPanel.add(nameField, gbc);

        // Email
        gbc.gridy = 2; gbc.gridx = 0;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(labelFont);
        mainPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        emailField = createTextField();
        mainPanel.add(emailField, gbc);

        // Username
        gbc.gridy = 3; gbc.gridx = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(labelFont);
        mainPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        usernameField = createTextField();
        mainPanel.add(usernameField, gbc);

        // Password
        gbc.gridy = 4; gbc.gridx = 0;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(labelFont);
        mainPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(18);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBackground(Color.WHITE);
        passwordField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        mainPanel.add(passwordField, gbc);

        // Confirm Password
        gbc.gridy = 5; gbc.gridx = 0;
        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setFont(labelFont);
        mainPanel.add(confirmLabel, gbc);

        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(18);
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
        confirmPasswordField.setBackground(Color.WHITE);
        confirmPasswordField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        mainPanel.add(confirmPasswordField, gbc);

        // Role
        gbc.gridy = 6; gbc.gridx = 0;
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

        // Checkbox
        gbc.gridy = 7; gbc.gridx = 1;
        agreeCheck = new JCheckBox("I agree to the terms and conditions.");
        agreeCheck.setBackground(new Color(240, 248, 255));
        agreeCheck.setFont(new Font("Arial", Font.PLAIN, 13));
        mainPanel.add(agreeCheck, gbc);

        // Buttons
        gbc.gridy = 8; gbc.gridx = 1;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton registerBtn = createButton("Register");
        registerBtn.setFocusPainted(false);
        registerBtn.setOpaque(true);
        registerBtn.setContentAreaFilled(true);
        registerBtn.setBorderPainted(false);

        JButton clearBtn = createButton("Clear");
        clearBtn.setFocusPainted(false);
        clearBtn.setOpaque(true);
        clearBtn.setContentAreaFilled(true);
        clearBtn.setBorderPainted(false);

        JButton backBtn = createButton("Back");
        clearBtn.setFocusPainted(false);
        clearBtn.setOpaque(true);
        clearBtn.setContentAreaFilled(true);
        clearBtn.setBorderPainted(false);

        buttonPanel.add(registerBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(backBtn);
        mainPanel.add(buttonPanel, gbc);

        // Actions
        registerBtn.addActionListener(e -> {

            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String username = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword());
            String confirm = new String(confirmPasswordField.getPassword());
            String role = roleCombo.getSelectedItem().toString();

            if (!agreeCheck.isSelected()) {
                JOptionPane.showMessageDialog(this, "Please agree to the terms.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!pass.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {

                User newUser = new User();
                newUser.setName(name);
                newUser.setRole(role);
                newUser.setEmail(email);
                newUser.setPassword(pass);
                newUser.setUsername(username);

                // Save the user

                UserService userService = new UserService();
                userService.signupUser(newUser);

                JOptionPane.showMessageDialog(this, "Account created successfully!");
                new LoginFrame();
                dispose();
            }
        });

        clearBtn.addActionListener(e -> {
            nameField.setText("");
            emailField.setText("");
            usernameField.setText("");
            passwordField.setText("");
            confirmPasswordField.setText("");
            roleCombo.setSelectedIndex(0);
            agreeCheck.setSelected(false);
        });

        backBtn.addActionListener(e -> {
            new WelcomeFrame();
            dispose();
        });

        setVisible(true);
    }

    private JTextField createTextField() {
        JTextField tf = new JTextField(18);
        tf.setFont(new Font("Arial", Font.PLAIN, 14));
        tf.setBackground(Color.WHITE);
        tf.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return tf;
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(100, 35));
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(26, 81, 199));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
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

//    public static void main(String[] args) {
//        new CreateAccountFrame();
//    }
}
