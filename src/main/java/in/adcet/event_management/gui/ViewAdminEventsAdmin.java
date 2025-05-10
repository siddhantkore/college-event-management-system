package in.adcet.event_management.gui;

import in.adcet.event_management.DTO.AdminEvent;
import in.adcet.event_management.entity.Events;
import in.adcet.event_management.service.EventService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ViewAdminEventsAdmin {
    private AdminEventDashboard frame;
    private JFrame parentFrame;
    private String username;
    public ViewAdminEventsAdmin(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }
    
    public ViewAdminEventsAdmin() {
        this(null);
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public void setVisible(boolean visible) {
        if (visible) {
            initializeUI();
        } else if (frame != null) {
            frame.dispose();
        }
    }
    
    private void initializeUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (frame == null) {
            frame = new AdminEventDashboard(parentFrame,username);
        }
        frame.setVisible(true);
    }
    
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            ViewAdminEventsAdmin adminEventManager = new ViewAdminEventsAdmin();
//            adminEventManager.setVisible(true);
//        });
//    }
}

class AdminEventDashboard extends JFrame {
    private JPanel adminEventsPanel;
    private JTextField searchField;
    private List<AdminEvent> adminEventsList;
    private Color primaryColor = new Color(30, 58, 138);      // Navy blue
    private Color secondaryColor = new Color(241, 245, 249); // Light gray-blue
    private Color accentColor = new Color(59, 130, 246);     // Bright blue
    private Color dangerColor = new Color(220, 38, 38);      // Red for delete
    private Color successColor = new Color(22, 163, 74);     // Green for update
    private JFrame parentFrame;

    private String username;
    private EventService eventService = new EventService();

    public AdminEventDashboard(JFrame parentFrame,String username) {
        this.username = username;
        this.parentFrame = parentFrame;
        setupUI();
    }

    private void setupUI() {
        setTitle("View Admin Events");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        adminEventsList = generateSampleAdminEvents();

        JPanel imagePanel = createImagePanel();
        add(imagePanel, BorderLayout.WEST);

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main Content
        adminEventsPanel = new JPanel();
        adminEventsPanel.setLayout(new GridLayout(0, 2, 25, 25));
        adminEventsPanel.setBackground(secondaryColor);
        adminEventsPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        JScrollPane scrollPane = new JScrollPane(adminEventsPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(secondaryColor);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        refreshAdminEventsList(adminEventsList);
    }

    private JPanel createImagePanel() {
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(250, getHeight()));
        imagePanel.setBackground(new Color(226, 232, 240));
        
        ImageIcon originalIcon = new ImageIcon(getClass().getClassLoader().getResource("images/eventlogo1.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(250, 600, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        
        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, primaryColor));
        
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        
        return imagePanel;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(new EmptyBorder(12, 20, 12, 20));

        // Back Button
        JButton backButton = createNavigationButton();
        backButton.setOpaque(true);
        backButton.setContentAreaFilled(true);
        backButton.setBorderPainted(false);
        
        JLabel title = new JLabel("ALL EVENTS");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        // Search Panel
        JPanel searchPanel = createSearchComponent();
        
        JPanel leftHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftHeaderPanel.setBackground(primaryColor);
        leftHeaderPanel.add(backButton);

        JPanel centerHeaderPanel = new JPanel();
        centerHeaderPanel.setBackground(primaryColor);
        centerHeaderPanel.add(title);

        JPanel rightHeaderPanel = new JPanel();
        rightHeaderPanel.setBackground(primaryColor);
        rightHeaderPanel.add(searchPanel);

        headerPanel.add(leftHeaderPanel, BorderLayout.WEST);
        headerPanel.add(centerHeaderPanel, BorderLayout.CENTER);
        headerPanel.add(rightHeaderPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JButton createNavigationButton() {
        JButton navButton = new JButton("← Back to Dashboard");
        navButton.setFocusPainted(false);
        navButton.setOpaque(true);
        navButton.setFont(new Font("Arial", Font.BOLD, 14));
        navButton.setBackground(secondaryColor);
        navButton.setForeground(primaryColor);
        navButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(secondaryColor, 1),
            BorderFactory.createEmptyBorder(6, 15, 6, 15)
        ));
        navButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        navButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                navButton.setBackground(Color.WHITE);
                navButton.setForeground(primaryColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                navButton.setBackground(secondaryColor);
                navButton.setForeground(primaryColor);
            }
        });

        navButton.addActionListener(e -> {
            dispose();
            new AdminDashboard(username).setVisible(true);
        });
        return navButton;
    }

    private JPanel createSearchComponent() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(primaryColor);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        
        searchField = new JTextField("Search events...");
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setPreferredSize(new Dimension(300, 38));
        searchField.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        searchField.setBackground(secondaryColor);
        searchField.setForeground(Color.GRAY);
        
        JLabel searchIcon = new JLabel(); //new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/search.jpg")))
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search events...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
    
            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(Color.GRAY);
                    searchField.setText("Search events...");
                }
            }
        });
        
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (!searchField.getText().equals("Search events...")) {
                    searchAdminEvents(searchField.getText().toLowerCase());
                }
            }
        });
    
        return searchPanel;
    }
    
    private void searchAdminEvents(String query) {
        List<AdminEvent> filtered = new ArrayList<>();
        for (AdminEvent event : adminEventsList) {
            if (event.getName().toLowerCase().contains(query) ||
                event.code.toLowerCase().contains(query) ||
                event.getDescription().toLowerCase().contains(query)) {
                filtered.add(event);
            }
        }
        refreshAdminEventsList(filtered);
    }

    private void refreshAdminEventsList(List<AdminEvent> events) {
        adminEventsPanel.removeAll();
        for (AdminEvent event : events) {
            adminEventsPanel.add(new AdminEventCardComponent(event, primaryColor, secondaryColor, accentColor, this));
        }
        adminEventsPanel.revalidate();
        adminEventsPanel.repaint();
    }

    // show all events
    private List<AdminEvent> generateSampleAdminEvents() {

        List<Events> eventsList = eventService.getAllEvents();
        return eventsList.stream()
                .map(event -> new AdminEvent(
                        event.getName(),
                        event.getCode(),
                        event.getDescription(),
                        event.getStatus(),
                        event.getRegistrationCount(),
                        event.getBannerPath()
                )).collect(Collectors.toList());

//        list.add(new AdminEvent("Tech Conference 2023", "TC2023", "Annual technology conference with keynote speakers and workshops", "Upcoming", 215, "banner1.jpg"));

    }


    // update Admin
    public void updateAdminEvent(AdminEvent oldEvent, AdminEvent updatedEvent) {
        int index = adminEventsList.indexOf(oldEvent);
        Optional<Events> event = eventService.getEventByCode(oldEvent.getCode());
        if(event==null)
            return;
        Events event1 = event.get();
        event1.setCode(updatedEvent.getCode());
        event1.setDescription(updatedEvent.getDescription());
        event1.setName(updatedEvent.getName());
        event1.setStatus(updatedEvent.getStatus());
        event1.setRegistrationCount(updatedEvent.getRegistered());

        Optional<Events> eventUpdated = eventService.updateEvent(event1);
        if(eventUpdated.get()==null)
            return;
        if (index != -1) {
            adminEventsList.set(index, updatedEvent);
            refreshAdminEventsList(adminEventsList);
            JOptionPane.showMessageDialog(this, "Event updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // delete Admin
    public void deleteAdminEvent(AdminEvent event) {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete '" + event.getName() + "'?",
            "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            adminEventsList.remove(event);
            eventService.deleteEvent(event.getCode());
            refreshAdminEventsList(adminEventsList);
            JOptionPane.showMessageDialog(this, "Event deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

class AdminEventCardComponent extends JPanel {
    private AdminEvent event;
    private AdminEventDashboard parent;

    public AdminEventCardComponent(AdminEvent event, Color primaryColor, Color secondaryColor, Color accentColor, AdminEventDashboard parent) {
        this.event = event;
        this.parent = parent;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(350, 240));
        setBackground(secondaryColor);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(primaryColor.brighter(), 1),
                new EmptyBorder(15, 15, 15, 15)));
        
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accentColor, 2),
                    new EmptyBorder(15, 15, 15, 15)));
            }
            public void mouseExited(MouseEvent e) {
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(primaryColor.brighter(), 1),
                    new EmptyBorder(15, 15, 15, 15)));
            }
        });

        JLabel nameLabel = new JLabel(event.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        nameLabel.setForeground(primaryColor);
        nameLabel.setBorder(new EmptyBorder(0, 0, 8, 0));

        JLabel codeLabel = new JLabel("ID: " + event.code);
        codeLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        codeLabel.setForeground(new Color(100, 100, 100));

        JLabel descLabel = new JLabel("<html><div style='width:300px;padding-top:8px;'>" +
                event.getDescription() + "</div></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descLabel.setForeground(Color.DARK_GRAY);

        JLabel statusLabel = new JLabel(event.getStatus().toUpperCase());
//        JLabel statusLabel = new JLabel("");
        statusLabel.setOpaque(true);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        if (event.getStatus().equalsIgnoreCase("Upcoming")) {
            statusLabel.setBackground(new Color(59, 130, 246)); // Blue
        } else if (event.getStatus().equalsIgnoreCase("Ongoing")) {
            statusLabel.setBackground(new Color(234, 88, 12)); // Orange
        } else {
            statusLabel.setBackground(new Color(101, 163, 13)); // Green for completed
        }
        
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setBorder(new EmptyBorder(4, 12, 4, 12));

        JLabel regLabel = new JLabel("Participants: " + event.getRegistered());
        regLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        regLabel.setForeground(new Color(100, 100, 100));

        JButton detailsBtn = new JButton("View Details");
        detailsBtn.setBorderPainted(false);
        detailsBtn.setOpaque(true);
        styleActionButton(detailsBtn, accentColor, Color.WHITE);
        detailsBtn.addActionListener(e -> new AdminEventDetailsWindow(event, parent));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(secondaryColor);
        topPanel.add(nameLabel, BorderLayout.NORTH);
        topPanel.add(codeLabel, BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(secondaryColor);
        
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(secondaryColor);
        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(regLabel, BorderLayout.EAST);
        
        bottomPanel.add(statusPanel, BorderLayout.NORTH);
        bottomPanel.add(detailsBtn, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(descLabel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void styleActionButton(JButton button, Color bgColor, Color textColor) {
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(6, 20, 6, 20)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }
}

class AdminEventDetailsWindow extends JDialog {
    private AdminEvent event;
    private AdminEventDashboard parent;

    public AdminEventDetailsWindow(AdminEvent event, AdminEventDashboard parent) {
        super(parent, event.getName() + " - Details", true);
        this.event = event;
        this.parent = parent;
        initializeWindow();
    }
    
    private void initializeWindow() {
        setSize(700, 600);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(241, 245, 249));

        // Banner image (placeholder)
        JPanel bannerPanel = new JPanel();
        bannerPanel.setPreferredSize(new Dimension(650, 200));
        bannerPanel.setBackground(new Color(203, 213, 225));
        bannerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(30, 58, 138), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        JLabel bannerLabel = new JLabel("Event Banner", SwingConstants.CENTER);
        bannerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        bannerLabel.setForeground(new Color(75, 85, 99));
        bannerPanel.add(bannerLabel);
        
        mainPanel.add(bannerPanel, BorderLayout.NORTH);

        // Details panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(new Color(241, 245, 249));
        detailsPanel.setBorder(new EmptyBorder(20, 10, 20, 10));

        addDetailRow(detailsPanel, "Event Name:", event.getName());
        addDetailRow(detailsPanel, "Event ID:", event.code);
        addDetailRow(detailsPanel, "Status:", event.getStatus());
        addDetailRow(detailsPanel, "Participants:", String.valueOf(event.getRegistered()));
        
        // Description with scroll
        JTextArea descArea = new JTextArea(event.getDescription());
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descArea.setBackground(new Color(241, 245, 249));
        descArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Description"),
            new EmptyBorder(5, 5, 5, 5)
        ));
        
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setPreferredSize(new Dimension(600, 120));
        detailsPanel.add(Box.createVerticalStrut(15));
        detailsPanel.add(descScroll);

        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(241, 245, 249));
        
        JButton updateBtn = new JButton("Update Event");
        updateBtn.setOpaque(true);
        updateBtn.setBorderPainted(false);
        styleButton(updateBtn, new Color(59, 130, 246), Color.WHITE);
        updateBtn.addActionListener(e -> showUpdateDialog());
        
        JButton deleteBtn = new JButton("Delete Event");
        deleteBtn.setOpaque(true);
        deleteBtn.setBorderPainted(false);
        styleButton(deleteBtn, new Color(220, 38, 38), Color.WHITE);
        deleteBtn.addActionListener(e -> parent.deleteAdminEvent(event));
        
        JButton closeBtn = new JButton("Close");
        closeBtn.setBorderPainted(false);
        closeBtn.setOpaque(true);
        styleButton(closeBtn, new Color(100, 116, 139), Color.WHITE);
        closeBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(closeBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
        setVisible(true);
    }
    
    private void addDetailRow(JPanel panel, String label, String value) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBackground(new Color(241, 245, 249));
        rowPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
        
        JLabel labelLbl = new JLabel(label);
        labelLbl.setFont(new Font("Arial", Font.BOLD, 14));
        labelLbl.setForeground(new Color(30, 41, 59));
        
        JLabel valueLbl = new JLabel(value);
        valueLbl.setFont(new Font("Arial", Font.PLAIN, 14));
        valueLbl.setForeground(new Color(71, 85, 105));
        
        rowPanel.add(labelLbl, BorderLayout.WEST);
        rowPanel.add(valueLbl, BorderLayout.CENTER);
        panel.add(rowPanel);
    }
    
    private void styleButton(JButton button, Color bgColor, Color textColor) {
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(8, 25, 8, 25)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }
    
    private void showUpdateDialog() {
        JDialog updateDialog = new JDialog(this, "Update Event", true);
        updateDialog.setSize(500, 400);
        updateDialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(241, 245, 249));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2, 10, 15));
        formPanel.setBackground(new Color(241, 245, 249));
        
        // Form fields
        JLabel nameLabel = new JLabel("Event Name:");
        JTextField nameField = new JTextField(event.getName());
        
        JLabel codeLabel = new JLabel("Event Code:");
        JTextField codeField = new JTextField(event.code);
        
        JLabel statusLabel = new JLabel("Status:");
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Upcoming", "Ongoing", "Completed"});
        statusCombo.setSelectedItem(event.getStatus());
        
        JLabel regLabel = new JLabel("Participants:");
        JSpinner regSpinner = new JSpinner(new SpinnerNumberModel(event.getRegistered(), 0, 1000, 1));
        
        JLabel descLabel = new JLabel("Description:");
        JTextArea descArea = new JTextArea(event.getDescription(), 3, 20);
        descArea.setLineWrap(true);
        JScrollPane descScroll = new JScrollPane(descArea);
        
        // Add components to form
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(codeLabel);
        formPanel.add(codeField);
        formPanel.add(statusLabel);
        formPanel.add(statusCombo);
        formPanel.add(regLabel);
        formPanel.add(regSpinner);
        formPanel.add(descLabel);
        formPanel.add(descScroll);

        panel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(241, 245, 249));
        
        JButton saveBtn = new JButton("Save Changes");
        saveBtn.setBorderPainted(false);
        saveBtn.setOpaque(true);
        styleButton(saveBtn, new Color(59, 130, 246), Color.WHITE);
        saveBtn.addActionListener(e -> {
            AdminEvent updatedEvent = new AdminEvent(
                nameField.getText(),
                codeField.getText(),
                descArea.getText(),
                (String)statusCombo.getSelectedItem(),
                (Integer)regSpinner.getValue(),
                    event.getBannerPath()
            );
            parent.updateAdminEvent(event, updatedEvent);
            updateDialog.dispose();
            dispose(); // Close details window after update
        });
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setOpaque(true);
        cancelBtn.setBorderPainted(false);
        styleButton(cancelBtn, new Color(100, 116, 139), Color.WHITE);
        cancelBtn.addActionListener(e -> updateDialog.dispose());
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        updateDialog.add(panel);
        updateDialog.setVisible(true);
    }
}
//
//public class AdminEvent {
//    String name;
//    String code;
//    String description;
//    String status;
//    int registered;
//    String bannerPath;
//
//    public AdminEvent(String name, String code, String description, String status, int registered, String bannerPath) {
//        this.name = name;
//        this.code = code;
//        this.description = description;
//        this.status = status;
//        this.registered = registered;
//        this.bannerPath = bannerPath;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) return true;
//        if (obj == null || getClass() != obj.getClass()) return false;
//        AdminEvent other = (AdminEvent) obj;
//        return code.equals(other.code);
//    }
//}