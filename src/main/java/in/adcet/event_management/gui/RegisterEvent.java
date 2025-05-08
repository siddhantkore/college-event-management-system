package in.adcet.event_management.gui;

import in.adcet.event_management.entity.Events;
import in.adcet.event_management.entity.User;
import in.adcet.event_management.service.EventService;
import in.adcet.event_management.service.RegisterService;
import in.adcet.event_management.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class RegisterEvent {

    // Same colors and fonts as Manage Events
    private static final Color PRIMARY_COLOR = new Color(30, 58, 138);
    private static final Color SECONDARY_COLOR = new Color(241, 245, 249);
    private static final Color ACCENT_COLOR = new Color(59, 130, 246);
    private static final Color HOVER_COLOR = new Color(37, 99, 235);
    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 32);
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 16);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

    private JFrame frame;
    private JTextField codeSearchField;
    private JLabel eventStatusLabel;
    private List<Events> availableEvents;
    private EventService eventService = new EventService();
    private RegisterService registerService = new RegisterService();
    private UserService userService = new UserService();
    private String username;


    // May be it is Entity OR DTO
//    class Event {
//        String name, date, location, description;
//        public Event(String name, String date, String location, String description) {
//            this.name = name;
//            this.date = date;
//            this.location = location;
//            this.description = description;
//        }
//    }

    public RegisterEvent(String username){
        this.username = username;
    }

    public void setVisible(boolean visible) {
        if (visible) {
            createAndShowGUI();
        } else if (frame != null) {
            frame.dispose();
        }
    }

//    public static void main(String[] args) {
//        RegisterEvent app = new RegisterEvent();
//        app.createAndShowGUI();
//    }

    private void createAndShowGUI() {
        frame = new JFrame("Student Dashboard - Event Registration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);


        initializeEvents();

        // Main panel with layered structure (same as Manage Events)
        JLayeredPane mainLayeredPane = new JLayeredPane();
        mainLayeredPane.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()));

        // 1. Left Banner (Same Image)
        JPanel leftBanner = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon(getClass().getResource("/images/eventlogo1.png"));
                if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                    g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(20, 40, 100));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        leftBanner.setBounds(0, 0, 250, frame.getHeight());
        mainLayeredPane.add(leftBanner, JLayeredPane.DEFAULT_LAYER);

        // 2. Top Banner (Same Image)
        JPanel topBanner = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon(getClass().getResource("/images/ADCET1.jpg"));
                if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                    g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(PRIMARY_COLOR);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        topBanner.setBounds(250, 0, frame.getWidth()-250, 150);
        mainLayeredPane.add(topBanner, JLayeredPane.DEFAULT_LAYER);

        // 3. Main Content Panel (Same structure)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(SECONDARY_COLOR);
        contentPanel.setBounds(250, 150, frame.getWidth()-250, frame.getHeight()-150);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Changed Header
        JLabel headerLabel = new JLabel("Register for Events", SwingConstants.CENTER);
        headerLabel.setFont(HEADER_FONT);
        headerLabel.setForeground(PRIMARY_COLOR);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        contentPanel.add(headerLabel);

        // Search Section (Same as Manage Events)
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setBackground(SECONDARY_COLOR);
        searchPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel codeLabel = new JLabel("Enter Event Code:");
        codeLabel.setFont(LABEL_FONT);
        codeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchPanel.add(codeLabel);
        searchPanel.add(Box.createVerticalStrut(10));

        // Search Field (Same)
        codeSearchField = new JTextField(20);
        codeSearchField.setFont(LABEL_FONT);
        codeSearchField.setMaximumSize(new Dimension(350, 40));
        codeSearchField.setText("e.g., EVENT101");
        codeSearchField.setForeground(Color.GRAY);
        codeSearchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        codeSearchField.addFocusListener(new FocusListener() {
            @Override public void focusGained(FocusEvent e) {
                if (codeSearchField.getText().equals("e.g., EVENT101")) {
                    codeSearchField.setText("");
                    codeSearchField.setForeground(Color.BLACK);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (codeSearchField.getText().isEmpty()) {
                    codeSearchField.setForeground(Color.GRAY);
                    codeSearchField.setText("e.g., EVENT101");
                }
            }
        });
        searchPanel.add(codeSearchField);

        // Status Label
        eventStatusLabel = new JLabel(" ", SwingConstants.CENTER);
        eventStatusLabel.setFont(LABEL_FONT);
        eventStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchPanel.add(Box.createVerticalStrut(20));
        searchPanel.add(eventStatusLabel);

        // Changed Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 10));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton viewDetailsButton = createHoverButton("View Details", ACCENT_COLOR, HOVER_COLOR);
        viewDetailsButton.addActionListener(e -> showEventDetails());

        JButton registerButton = createHoverButton("Register", new Color(50, 150, 50), new Color(60, 180, 60));
        registerButton.addActionListener(e -> registerForEvent());

        JButton backButton = createHoverButton("Back", new Color(100, 100, 100), new Color(120, 120, 120));
        backButton.addActionListener(e -> {
            frame.dispose();
            new StudentDashboard(username).setVisible(true);
        });

        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        searchPanel.add(Box.createVerticalStrut(30));
        searchPanel.add(buttonPanel);

        contentPanel.add(searchPanel);
        mainLayeredPane.add(contentPanel, JLayeredPane.DEFAULT_LAYER);
        frame.add(mainLayeredPane);
        frame.setVisible(true);
    }

    // No need of this
    private void initializeEvents() {
        availableEvents = eventService.getAllEvents();
//        availableEvents.put("EVENT101", new Event("Tech Conference", "2023-12-15", "Auditorium", "Annual tech event with industry leaders"));
//        availableEvents.put("EVENT102", new Event("Cultural Fest", "2023-11-20", "Main Ground", "Cultural performances and food festival"));
//        availableEvents.put("EVENT103", new Event("Coding Workshop", "2023-10-10", "Computer Lab", "Hands-on programming session"));
    }

    // Show details of event for given code
    private void showEventDetails() {
        String code = codeSearchField.getText().trim().toUpperCase();
        Optional<Events> event = eventService.getEventByCode(code);

        if (event.isPresent()) {
            Events events = event.get();
            String details = String.format(
                "<html><b>%s</b><br>Date: %s<br>Location: %s<br><br>%s</html>",
                    events.getName(), events.getEventDate(), events.getVenue(), events.getDescription()
            );
            JOptionPane.showMessageDialog(frame, details, "Event Details", JOptionPane.INFORMATION_MESSAGE);
        } else {
            eventStatusLabel.setText("Event not found!");
            eventStatusLabel.setForeground(Color.RED);
        }
    }

    // add a event for a student as per username
    private void registerForEvent() {
        String code = codeSearchField.getText().trim().toUpperCase();
        Optional<Events> event = eventService.getEventByCode(code);

        if (event!=null) {
            Events events = event.get();
            int confirm = JOptionPane.showConfirmDialog(
                frame,
                "Confirm registration for:\n\n" + events.getName(),
                "Confirm Registration",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {

                User user = userService.getUserByUsername(username);
                registerService.registerAEvent(events,user);
                eventStatusLabel.setText("Registered successfully!");
                eventStatusLabel.setForeground(new Color(0, 150, 0));
            }
        } else {
            eventStatusLabel.setText("Invalid event code!");
            eventStatusLabel.setForeground(Color.RED);
        }
    }

    private JButton createHoverButton(String text, Color normalColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(normalColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { button.setBackground(hoverColor); }
            @Override public void mouseExited(MouseEvent e) { button.setBackground(normalColor); }
        });
        return button;
    }
}