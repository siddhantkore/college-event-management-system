package in.adcet.event_management.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StudentDashboard extends JFrame {

    private String username;

    public StudentDashboard(String usernamee) {
        this.username = usernamee;
        setTitle("Student Dashboard");
        setSize(1000, 600);
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ---------- Sidebar Panel ----------
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(30, 30, 60));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setLayout(new BorderLayout());

        // --- Top: College Logo ---
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(30, 30, 60));
        logoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        ImageIcon logoIcon = new ImageIcon(getClass().getClassLoader().getResource("images/logo4.jpg"));
        Image logoImage = logoIcon.getImage().getScaledInstance(140, 100, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
        logoPanel.add(logoLabel);
        sidebar.add(logoPanel, BorderLayout.NORTH);

        // --- Center: Menu Buttons ---
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(new Color(30, 30, 60));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        String[] btnNames = {
            "Register event",
            "View Events",
            "Registered Events",
            "Logout"
        };

        for (String name : btnNames) {
            JButton btn = new JButton(name);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(160, 40));
            btn.setFocusPainted(false);
            btn.setBackground(new Color(60, 60, 100));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Arial", Font.PLAIN, 14));

            // Hover Effect
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent evt) {
                    btn.setBackground(new Color(80, 80, 130));
                }

                public void mouseExited(MouseEvent evt) {
                    btn.setBackground(new Color(60, 60, 100));
                }
            });

            // Click handling / Invoking windows
            btn.addActionListener(e -> {
                switch (name) {
                    case "Register event":
                        new RegisterEvent(username).setVisible(true);
                        dispose();
                        break;
                    case "View Events":
                        StudentViewEvent sve = new StudentViewEvent();
                        sve.setUsername(username);
                        sve.setVisible(true);

                        dispose();
                        break;
                    case "Registered Events":
                        new RegisteredEventsPage(username).setVisible(true);
                        dispose();
                        break;
                    case "Logout":
                        new LoginFrame().setVisible(true);
                        dispose();
                        break;
                }
            });

            menuPanel.add(Box.createVerticalStrut(10));
            menuPanel.add(btn);
        }

        sidebar.add(menuPanel, BorderLayout.CENTER);

        // --- Bottom: Profile Section ---
        JPanel profilePanel = new JPanel();
        profilePanel.setBackground(new Color(30, 30, 60));
        profilePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));
        ImageIcon profileImageIcon = new ImageIcon(getClass().getClassLoader().getResource("images/profile1.jpg"));
        Image profileImg = profileImageIcon.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
        JLabel profileLabel = new JLabel("Profile", new ImageIcon(profileImg), JLabel.LEFT);
        profileLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        profileLabel.setForeground(Color.WHITE);
        profilePanel.add(profileLabel);

        sidebar.add(profilePanel, BorderLayout.SOUTH);

        // ---------- Main Panel ----------
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Banner image at top
        ImageIcon bannerIcon = new ImageIcon(getClass().getClassLoader().getResource("images/ADCET1.jpg"));
        Image bannerImage = bannerIcon.getImage().getScaledInstance(800, 180, Image.SCALE_SMOOTH);
        JLabel bannerLabel = new JLabel(new ImageIcon(bannerImage));
        bannerLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(bannerLabel, BorderLayout.NORTH);

        // Welcome Text at center
        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(40, 40, 80));
        mainPanel.add(welcomeLabel, BorderLayout.CENTER);

        // ---------- Add to Frame ----------
        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new StudentDashboard("Student"));
//    }
}
