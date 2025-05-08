
package in.adcet.event_management.gui;

import in.adcet.event_management.entity.Events; // Assuming your entity class is named Events
import in.adcet.event_management.service.EventService; // Import your EventService
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.Optional; // To handle cases where an event might not be found

public class EventManager {
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
    private EventService eventService; // Instance of your EventService

    // Try to use Factory
    public EventManager() {
        this.eventService = new EventService(); // Initialize your EventService
        // You might need to adjust how you get the EventService instance
    }

    public void setVisible(boolean visible) {
        if (visible) {
            createAndShowGUI();
        } else if (frame != null) {
            frame.dispose();
        }
    }

    private void createAndShowGUI() {
        frame = new JFrame("College Event Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JLayeredPane mainLayeredPane = new JLayeredPane();
        mainLayeredPane.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()));

        JPanel leftBanner = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/eventlogo1.png"));
                if (icon != null && icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                    g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(20, 40, 100));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        leftBanner.setBounds(0, 0, 250, frame.getHeight());
        mainLayeredPane.add(leftBanner, JLayeredPane.DEFAULT_LAYER);

        JPanel topBanner = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/ADCET1.jpg"));
                if (icon != null && icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                    g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(PRIMARY_COLOR);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        topBanner.setBounds(250, 0, frame.getWidth() - 250, 150);
        mainLayeredPane.add(topBanner, JLayeredPane.DEFAULT_LAYER);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(SECONDARY_COLOR);
        contentPanel.setBounds(250, 150, frame.getWidth() - 250, frame.getHeight() - 150);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel headerLabel = new JLabel("Manage Events", SwingConstants.CENTER);
        headerLabel.setFont(HEADER_FONT);
        headerLabel.setForeground(PRIMARY_COLOR);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        contentPanel.add(headerLabel);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(SECONDARY_COLOR);
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel codeLabel = new JLabel("Enter Event Code:");
        codeLabel.setFont(LABEL_FONT);
        codeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(codeLabel);
        centerPanel.add(Box.createVerticalStrut(15));

        JPanel searchBarPanel = new JPanel();
        searchBarPanel.setLayout(new BoxLayout(searchBarPanel, BoxLayout.X_AXIS));
        searchBarPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

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
            @Override
            public void focusGained(FocusEvent e) {
                if (codeSearchField.getText().equals("e.g., EVENT101")) {
                    codeSearchField.setText("");
                    codeSearchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (codeSearchField.getText().isEmpty()) {
                    codeSearchField.setForeground(Color.GRAY);
                    codeSearchField.setText("e.g., EVENT101");
                }
            }
        });

        JButton searchButton = createHoverButton("Search", ACCENT_COLOR, HOVER_COLOR);
        searchButton.addActionListener(e -> searchEvent());

        searchBarPanel.add(codeSearchField);
        searchBarPanel.add(Box.createHorizontalStrut(15));
        searchBarPanel.add(searchButton);
        centerPanel.add(searchBarPanel);

        eventStatusLabel = new JLabel(" ", SwingConstants.CENTER);
        eventStatusLabel.setFont(LABEL_FONT);
        eventStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(Box.createVerticalStrut(25));
        centerPanel.add(eventStatusLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 10));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton backButton = createHoverButton("Back", new Color(100, 100, 100), new Color(120, 120, 120));
        backButton.addActionListener(e -> {
            frame.dispose();
            new AdminDashboard("Admin").setVisible(true);
        });

        JButton updateButton = createHoverButton("Update", ACCENT_COLOR, HOVER_COLOR);
        updateButton.addActionListener(e -> updateEvent());

        JButton deleteButton = createHoverButton("Delete", new Color(200, 50, 50), new Color(220, 70, 70));
        deleteButton.addActionListener(e -> deleteEvent());

        buttonPanel.add(backButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(buttonPanel);

        contentPanel.add(centerPanel);
        mainLayeredPane.add(contentPanel, JLayeredPane.DEFAULT_LAYER);
        frame.add(mainLayeredPane);
        frame.setVisible(true);
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
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
            }
        });
        return button;
    }

    // Search An Event
    private void searchEvent() {
        String code = codeSearchField.getText().trim().toUpperCase();
        if (code.isEmpty() || code.equals("E.G., EVENT101")) {
            eventStatusLabel.setText("Please enter an event code");
            eventStatusLabel.setForeground(Color.RED);
            return;
        }

        Optional<Events> eventOptional = eventService.getEventByCode(code);

        if (eventOptional.isPresent()) {
            Events event = eventOptional.get();
            eventStatusLabel.setText("Event Found: " + event.getName());
            eventStatusLabel.setForeground(new Color(0, 150, 0));
        } else {
            eventStatusLabel.setText("Event Not Found!");
            eventStatusLabel.setForeground(Color.RED);
        }
    }

    // Delete Event
    private void deleteEvent() {
        String code = codeSearchField.getText().trim().toUpperCase();
        Optional<Events> eventOptional = eventService.getEventByCode(code);

        if (!eventOptional.isPresent()) {
            JOptionPane.showMessageDialog(frame, "No event selected!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Events event = eventOptional.get();
        int confirm = JOptionPane.showConfirmDialog(frame,
                "Are you sure you want to delete '" + event.getName() + "'?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            eventService.deleteEvent(code);
            JOptionPane.showMessageDialog(frame, "Event deleted successfully!");
            codeSearchField.setText("e.g., EVENT101");
            codeSearchField.setForeground(Color.GRAY);
            eventStatusLabel.setText(" ");
        }
    }

    // Create Update Form
    private void updateEvent() {
        String code = codeSearchField.getText().trim().toUpperCase();
        Optional<Events> eventOptional = eventService.getEventByCode(code);

        if (!eventOptional.isPresent()) {
            JOptionPane.showMessageDialog(frame, "No event selected!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Events eventToUpdate = eventOptional.get();

        JPanel updatePanel = new JPanel(new GridLayout(5, 2, 10, 10));
        updatePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField nameField = new JTextField(eventToUpdate.getName());
        JTextField dateField = new JTextField(String.valueOf(eventToUpdate.getEventDate()));
        JTextField locationField = new JTextField(eventToUpdate.getVenue());
        JTextArea descArea = new JTextArea(eventToUpdate.getDescription());
        descArea.setLineWrap(true);

        updatePanel.add(new JLabel("Event Name:"));
        updatePanel.add(nameField);
        updatePanel.add(new JLabel("Date:"));
        updatePanel.add(dateField);
        updatePanel.add(new JLabel("Location:"));
        updatePanel.add(locationField);
        updatePanel.add(new JLabel("Description:"));
        updatePanel.add(new JScrollPane(descArea));

        int result = JOptionPane.showConfirmDialog(
                frame,
                updatePanel,
                "Update Event: " + code,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            eventToUpdate.setName(nameField.getText());
            eventToUpdate.setEventDate(LocalDate.parse(dateField.getText()));
            eventToUpdate.setVenue(locationField.getText());
            eventToUpdate.setDescription(descArea.getText());


            Optional<Events> event = eventService.updateEvent(eventToUpdate);
            if(!event.isPresent()){
                JOptionPane.showMessageDialog(frame, "Can't Update Event!");
            } else{
                JOptionPane.showMessageDialog(frame, "Event updated successfully!");
            }

        }
    }
}