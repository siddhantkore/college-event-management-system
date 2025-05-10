package in.adcet.event_management.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class WelcomeFrame extends JFrame {

    public WelcomeFrame() {
        setTitle("Welcome - ADCET EMS");
        setSize(900, 600);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Top banner image
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/AppLogo.png")));
//        ImageIcon img = new ImageIcon("./images/ADCET1.jpg");
        ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("images/ADCET1.jpg"));
        Image scaledImg = img.getImage().getScaledInstance(900, 200, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImg));
        add(imageLabel, BorderLayout.NORTH);

        // Welcome Text and Buttons Panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(240, 248, 255)); // Light blue

        JLabel welcomeLabel = new JLabel("Welcome to ADCET College Event Management System");
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(26, 81, 199)); // Dark blue
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(40, 10, 30, 10));

        // Buttons
        JButton loginButton = new JButton("Login");
        loginButton.setOpaque(true);
        loginButton.setContentAreaFilled(true);
        loginButton.setBorderPainted(false);

        JButton createButton = new JButton("Create Account");
        createButton.setOpaque(true);
        createButton.setContentAreaFilled(true);
        createButton.setBorderPainted(false);

        styleButton(loginButton);
        styleButton(createButton);

        centerPanel.add(welcomeLabel);
        centerPanel.add(loginButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(createButton);

        add(centerPanel, BorderLayout.CENTER);

        // Action Listeners
        loginButton.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        createButton.addActionListener(e -> {
            new CreateAccountFrame();
            dispose();
        });

        setVisible(true);
    }

    // Style + Hover effect
    private void styleButton(JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 45));
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(26, 81, 199));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 64, 160));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(26, 81, 199));
            }
        });
    }

    public static void main(String[] args) {

        new WelcomeFrame();
    }
}