package in.adcet.event_management.gui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboard extends JFrame {

    private String username;
    public AdminDashboard(String usernamee) {
        this.username = usernamee;
        setTitle("Admin Dashboard");
        setSize(1000, 600);
        setLocationRelativeTo(null); // center the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ---------- Sidebar Panel ----------
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(30, 30, 60));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // College Logo
        ImageIcon logoIcon = new ImageIcon(getClass().getClassLoader().getResource("images/logo4.jpg"));
        Image logoImage = logoIcon.getImage().getScaledInstance(150, 120, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        sidebar.add(logoLabel);


        // Sidebar Buttons
        String[] btnNames = {"Create Event", "View Events", "Manage Events", "Logout"};
        for (String name : btnNames) {
            JButton btn = new JButton(name);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(160, 40));
            btn.setFocusPainted(false);
            btn.setOpaque(true);
            btn.setContentAreaFilled(true);
            btn.setBorderPainted(false);
            btn.setBackground(new Color(60, 60, 100));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Arial", Font.PLAIN, 14));

            // Hover effect
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(80, 80, 130));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(60, 60, 100));
                }
            });

            // Button click handling
            btn.addActionListener(e -> {
                switch (name) {
                    case "Create Event":
                        new CreateEventPage1(username).setVisible(true);
                        dispose(); // Close current Admin Dashboard
                        break;
                    case "View Events":
                        ViewAdminEventsAdmin vae = new ViewAdminEventsAdmin();
                        vae.setUsername(username);
                        vae.setVisible(true);
                        dispose();
                        break;
                    case "Manage Events":
                        new EventManager(username).setVisible(true);
                        dispose();
                        break;
                    case "Logout":
                        new LoginFrame().setVisible(true);
                        dispose();
                        break;
                }
            });

            sidebar.add(Box.createVerticalStrut(10));
            sidebar.add(btn);
        }


        // Spacer
        sidebar.add(Box.createVerticalGlue());

        // Profile section with image icon
        ImageIcon profileImageIcon = new ImageIcon(getClass().getClassLoader().getResource("images/profile1.jpg")); // Path to your image
        Image profileImg = profileImageIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(profileImg);

        JLabel profileIcon = new JLabel("  Profile", scaledIcon, JLabel.LEFT);
        profileIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        profileIcon.setForeground(Color.WHITE);
        profileIcon.setFont(new Font("Arial", Font.PLAIN, 14));

        sidebar.add(Box.createVerticalGlue()); // Pushes it to the bottom
        sidebar.add(profileIcon);
        sidebar.add(Box.createVerticalStrut(20));


        // ---------- Main Panel ----------
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Banner image at top
        ImageIcon bannerIcon = new ImageIcon(getClass().getClassLoader().getResource("images/ADCET1.jpg"));

        Image bannerImage = bannerIcon.getImage().getScaledInstance(900, 200, Image.SCALE_SMOOTH);
        JLabel bannerLabel = new JLabel(new ImageIcon(bannerImage));
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

////     ---------- Main ----------
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new AdminDashboard("Admin"));
//    }
}
