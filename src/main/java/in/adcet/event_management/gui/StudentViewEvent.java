package in.adcet.event_management.gui;

import in.adcet.event_management.DTO.RegistrationDTO;
import in.adcet.event_management.entity.Events;
import in.adcet.event_management.service.EventService;
import in.adcet.event_management.service.RegisterService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StudentViewEvent {
    private EventFrame frame;
    private JFrame parentFrame;
    private String username;

    public StudentViewEvent(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    public StudentViewEvent() {
        this(null);
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setVisible(boolean visible) {
        if (visible) {
            createAndShowGUI();
        } else if (frame != null) {
            frame.dispose();
        }
    }
    
    private void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (frame == null) {
            frame = new EventFrame(parentFrame,username);
        }
        frame.setVisible(true);
    }

}

class EventFrame extends JFrame {
    private JPanel cardsContainer;
    private JTextField searchField;
    private List<Events> allEvents;
    private Color primaryColor = new Color(12, 53, 106);      // Dark blue
    private Color secondaryColor = new Color(240, 240, 245);  // Light gray
    private Color accentColor = new Color(65, 105, 225);      // Royal blue
    private Color textColor = new Color(60, 60, 60);          // Dark gray for text
    private JFrame parentFrame;

    private EventService eventService = new EventService();
    private RegisterService registerService = new RegisterService();
    private  String username;


    public EventFrame(JFrame parentFrame, String username) {
        this.username = username;
        this.parentFrame = parentFrame;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("All Events");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 700); 
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        allEvents = eventService.getAllEvents(); // getAllEvents

        // Left Banner
        JPanel leftBannerPanel = new JPanel(new BorderLayout());
        leftBannerPanel.setPreferredSize(new Dimension(220, getHeight()));
        leftBannerPanel.setBackground(new Color(230, 230, 240));
        leftBannerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, primaryColor));
        
        // Banner with logo and quick links
        JPanel bannerContent = new JPanel();
        bannerContent.setLayout(new BoxLayout(bannerContent, BoxLayout.Y_AXIS));
        bannerContent.setBackground(new Color(230, 230, 240));
        bannerContent.setBorder(new EmptyBorder(20, 10, 20, 10));
        
        JLabel logo = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("images/eventlogo4.jpg")));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        bannerContent.add(logo);
        
        bannerContent.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Quick links panel
        JPanel quickLinksPanel = new JPanel();
        quickLinksPanel.setLayout(new BoxLayout(quickLinksPanel, BoxLayout.Y_AXIS));
        quickLinksPanel.setBackground(new Color(230, 230, 240));
        quickLinksPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel quickLinksTitle = new JLabel("Quick Links");
        quickLinksTitle.setFont(new Font("Arial", Font.BOLD, 16));
        quickLinksTitle.setForeground(primaryColor);
        quickLinksTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        quickLinksPanel.add(quickLinksTitle);
        
        quickLinksPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // set and get status of events from
        String[] links = {"Upcoming Events", "Ongoing Events", "Past Events", "My Registrations"};
        for (String link : links) {
            JLabel linkLabel = new JLabel(link);
            linkLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            linkLabel.setForeground(accentColor);
            linkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            linkLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            linkLabel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    filterByCategory(link);
                }
                public void mouseEntered(MouseEvent e) {
                    linkLabel.setForeground(accentColor.darker());
                }
                public void mouseExited(MouseEvent e) {
                    linkLabel.setForeground(accentColor);
                }
            });
            quickLinksPanel.add(linkLabel);
            quickLinksPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        
        bannerContent.add(quickLinksPanel);
        leftBannerPanel.add(bannerContent, BorderLayout.NORTH);
        add(leftBannerPanel, BorderLayout.WEST);

        // Top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(primaryColor);
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Back Button
        JButton backButton = createBackButton();
        
        JLabel title = new JLabel("All Events");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        // Search Panel
        JPanel searchPanel = createSearchPanel();
        
        JPanel topLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topLeftPanel.setBackground(primaryColor);
        topLeftPanel.add(backButton);

        JPanel topCenterPanel = new JPanel();
        topCenterPanel.setBackground(primaryColor);
        topCenterPanel.add(title);

        JPanel topRightPanel = new JPanel();
        topRightPanel.setBackground(primaryColor);
        topRightPanel.add(searchPanel);

        topPanel.add(topLeftPanel, BorderLayout.WEST);
        topPanel.add(topCenterPanel, BorderLayout.CENTER);
        topPanel.add(topRightPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Main Content
        cardsContainer = new JPanel();
        cardsContainer.setLayout(new GridLayout(0, 2, 20, 20));
        cardsContainer.setBackground(secondaryColor);
        cardsContainer.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JScrollPane scrollPane = new JScrollPane(cardsContainer);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(secondaryColor);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        loadEvents(allEvents); // showing events got from database
    }

    private void filterByCategory(String category) {
        List<Events> filtered = new ArrayList<>();
        for (Events event : allEvents) {
            if (category.equals("Upcoming Events") && event.getStatus().equalsIgnoreCase("Upcoming")) {
                filtered.add(event); // status upcoming
            } else if (category.equals("Ongoing Events") && event.getStatus().equalsIgnoreCase("Ongoing")) {
                filtered.add(event); // status ongoing
            } else if (category.equals("Past Events") && event.getStatus().equalsIgnoreCase("Completed")) {
                filtered.add(event); // closed events
            } else if (category.equals("My Registrations")) {
                List<RegistrationDTO> events = registerService.getEventsOfAStudent(username);
                for (RegistrationDTO e:events){
                    if(event.getName().equalsIgnoreCase(e.getName())){
                        filtered.add(event);
                    }
                }
                filtered.add(event); // get all events of a student
            }
        }
        loadEvents(filtered); // showing filtered events
    }

    private JButton createBackButton() {
        JButton backButton = new JButton("\u2190 Back");
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setOpaque(true);
        backButton.setBackground(secondaryColor);
        backButton.setForeground(primaryColor);
        backButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(secondaryColor, 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        backButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                backButton.setBackground(Color.WHITE);
                backButton.setForeground(primaryColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                backButton.setBackground(secondaryColor);
                backButton.setForeground(primaryColor);
            }
        });

        backButton.addActionListener(e -> {
            new StudentDashboard(username).setVisible(true);
            dispose();
            if (parentFrame != null) {
                parentFrame.setVisible(true);
            }
        });
        return backButton;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(primaryColor);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        
        searchField = new JTextField("Search for events");
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setPreferredSize(new Dimension(300, 35));
        searchField.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        searchField.setBackground(secondaryColor);
        searchField.setForeground(Color.GRAY);
        
        JLabel searchIcon = new JLabel();//new ImageIcon(getClass().getClassLoader().getResource("images/search.png"))
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        
        // Add placeholder behavior
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search for events")) {
                    searchField.setText("");
                    searchField.setForeground(textColor);
                }
            }
    
            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(Color.GRAY);
                    searchField.setText("Search for events");
                }
            }
        });
        
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (!searchField.getText().equals("Search for events")) {
                    filterEvents(searchField.getText().toLowerCase()); // search an events
                }
            }
        });
    
        return searchPanel;
    }

    private void filterEvents(String query) {
        List<Events> filtered = new ArrayList<>();
        for (Events event : allEvents) {
            if (event.getName().toLowerCase().contains(query) ||
                event.getCode().toLowerCase().contains(query) ||
                event.getDescription().toLowerCase().contains(query) ||
                event.getVenue().toLowerCase().contains(query) ||
                event.getOrganizer().toLowerCase().contains(query)) {
                filtered.add(event);
            }
        }
        loadEvents(filtered); // show filtered events
    }

    // get all the events from database
    private void loadEvents(List<Events> events) {
        cardsContainer.removeAll();
        if (events.isEmpty()) {
            JLabel noResults = new JLabel("No events found matching your criteria");
            noResults.setFont(new Font("Arial", Font.ITALIC, 18));
            noResults.setForeground(textColor);
            noResults.setHorizontalAlignment(SwingConstants.CENTER);
            cardsContainer.add(noResults);
        } else {
            for (Events event : events) {
                cardsContainer.add(new EventCard(event, primaryColor, secondaryColor, accentColor, textColor));
            }
        }
        cardsContainer.revalidate();
        cardsContainer.repaint();
    }

}

class EventCard extends JPanel {
    public EventCard(Events event, Color primaryColor, Color secondaryColor, Color accentColor, Color textColor) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(350, 250));  // Slightly larger cards
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

        // Top panel with name and code
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(secondaryColor);
        
        JLabel nameLabel = new JLabel(event.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        nameLabel.setForeground(primaryColor);
        
        JLabel codeLabel = new JLabel("Code: " + event.getCode());
        codeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        codeLabel.setForeground(Color.GRAY);
        
        topPanel.add(nameLabel, BorderLayout.NORTH);
        topPanel.add(codeLabel, BorderLayout.SOUTH);

        // Middle panel with description and date
        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.setBackground(secondaryColor);
        
        JLabel descLabel = new JLabel("<html><div style='width:300px;padding-top:5px;'>" + 
                                    event.getDescription() + "</div></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descLabel.setForeground(textColor);
        
        JLabel dateLabel = new JLabel(formatDate(String.valueOf(event.getEventDate())) + " - " + formatDate(String.valueOf(event.getEndDate())));
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(100, 100, 100));
        dateLabel.setBorder(new EmptyBorder(5, 0, 0, 0));
        
        middlePanel.add(descLabel, BorderLayout.CENTER);
        middlePanel.add(dateLabel, BorderLayout.SOUTH);

        // Bottom panel with status, registered count, and button
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(secondaryColor);
        
        // Status and registration panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(secondaryColor);

        if(event.getEventDate().isBefore(LocalDate.now())){
            event.setStatus("COMPLETED");
        }

        JLabel statusLabel = new JLabel(event.getStatus());
        statusLabel.setOpaque(true);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        if (event.getStatus().equalsIgnoreCase("Upcoming")) {
            statusLabel.setBackground(new Color(46, 125, 50)); // Green
        } else if (event.getStatus().equalsIgnoreCase("Ongoing")) {
            statusLabel.setBackground(new Color(211, 47, 47)); // Red
        } else {
            statusLabel.setBackground(new Color(158, 158, 158)); // Gray
        }
        
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setBorder(new EmptyBorder(3, 10, 3, 10));

        JLabel regLabel = new JLabel("Registered: " + event.getRegistrationCount());
        regLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        regLabel.setForeground(Color.GRAY);
        
        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(regLabel, BorderLayout.EAST);
        
        // Button panel
        JButton moreBtn = new JButton("View Details");
        moreBtn.setBackground(accentColor);
        moreBtn.setForeground(Color.WHITE);
        moreBtn.setFocusPainted(false);
        moreBtn.setFont(new Font("Arial", Font.BOLD, 14));
        moreBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor.darker(), 1),
            BorderFactory.createEmptyBorder(8, 25, 8, 25)
        ));
        moreBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        moreBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                moreBtn.setBackground(accentColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                moreBtn.setBackground(accentColor);
            }
        });
        moreBtn.addActionListener(e -> new EventDetailsDialog(event, primaryColor, secondaryColor, accentColor, textColor));

        bottomPanel.add(statusPanel, BorderLayout.NORTH);
        bottomPanel.add(moreBtn, BorderLayout.SOUTH);

        // Add all panels to card
        add(topPanel, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private String formatDate(String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            return date.format(DateTimeFormatter.ofPattern("MMM d, yyyy"));
        } catch (Exception e) {
            return dateStr;
        }
    }
}

class EventDetailsDialog extends JDialog {
    public EventDetailsDialog(Events event, Color primaryColor, Color secondaryColor, Color accentColor, Color textColor) {
        setTitle(event.getName() + " - Event Details");
        setSize(700, 700);  // Larger dialog
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(secondaryColor);

        // Banner image
        JLabel banner = new JLabel(new ImageIcon(event.getBannerPath()));
//        JLabel banner = new JLabel(new ImageIcon());
        banner.setPreferredSize(new Dimension(650, 200));
        banner.setHorizontalAlignment(SwingConstants.CENTER);
        banner.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(primaryColor, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        mainPanel.add(banner, BorderLayout.NORTH);

        // Details panel with tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(secondaryColor);
        tabbedPane.setForeground(primaryColor);
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));

        // Overview tab
        JPanel overviewPanel = createOverviewPanel(event, secondaryColor, textColor);
        tabbedPane.addTab("Overview", overviewPanel);

        // Schedule tab
        JPanel schedulePanel = createSchedulePanel(event, secondaryColor, textColor);
        tabbedPane.addTab("Schedule", schedulePanel);

        // Organizer tab
        JPanel organizerPanel = createOrganizerPanel(event, secondaryColor, textColor);
        tabbedPane.addTab("Organizer", organizerPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(secondaryColor);
        
        JButton registerBtn = new JButton(event.getStatus().equals("Completed") ? "Event Ended" : "Register Now");
        styleButton(registerBtn, accentColor, Color.WHITE);
        registerBtn.setEnabled(!event.getStatus().equals("Completed"));
        registerBtn.addActionListener(e -> {
            dispose();
            new RegisterEventPage(event).setVisible(true);
        });
        
        JButton closeBtn = new JButton("Close");
        styleButton(closeBtn, new Color(100, 100, 100), Color.WHITE);
        closeBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(registerBtn);
        buttonPanel.add(closeBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
        setVisible(true);
    }
    
    private JPanel createOverviewPanel(Events event, Color bgColor, Color textColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JTextArea details = new JTextArea();
        details.setEditable(false);
        details.setFont(new Font("Arial", Font.PLAIN, 14));
        details.setBackground(bgColor);
        details.setForeground(textColor);
        details.setLineWrap(true);
        details.setWrapStyleWord(true);
        
        String detailsText = "Event Code: " + event.getCode() + "\n\n" +
                           "Status: " + event.getStatus() + "\n\n" +
                           "Description:\n" + event.getDescription() + "\n\n" +
                           "Location: " + event.getVenue() + "\n\n" +
                           "Registered Participants: " + event.getRegistrationCount();
        
        details.setText(detailsText);
        
        JScrollPane scrollPane = new JScrollPane(details);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSchedulePanel(Events event, Color bgColor, Color textColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JTextArea schedule = new JTextArea();
        schedule.setEditable(false);
        schedule.setFont(new Font("Arial", Font.PLAIN, 14));
        schedule.setBackground(bgColor);
        schedule.setForeground(textColor);
        schedule.setLineWrap(true);
        schedule.setWrapStyleWord(true);
        
        String scheduleText = "Start Date: " + formatDate(String.valueOf(event.getEventDate())) + "\n\n" +
                            "End Date: " + formatDate(String.valueOf(event.getEndDate())) + "\n\n" +
                            "Time: " + event.getTime() + "\n\n" +
                            "Duration: " + calculateDuration(String.valueOf(event.getEventDate()), String.valueOf(event.getEndDate())) + "\n\n" +
                            "Location: " + event.getVenue();
        
        schedule.setText(scheduleText);
        
        JScrollPane scrollPane = new JScrollPane(schedule);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createOrganizerPanel(Events event, Color bgColor, Color textColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JTextArea organizer = new JTextArea();
        organizer.setEditable(false);
        organizer.setFont(new Font("Arial", Font.PLAIN, 14));
        organizer.setBackground(bgColor);
        organizer.setForeground(textColor);
        organizer.setLineWrap(true);
        organizer.setWrapStyleWord(true);

        
        JScrollPane scrollPane = new JScrollPane(organizer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private String formatDate(String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            return date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));
        } catch (Exception e) {
            return dateStr;
        }
    }
    
    private String calculateDuration(String startDate, String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            long days = java.time.temporal.ChronoUnit.DAYS.between(start, end) + 1;
            return days + " day" + (days > 1 ? "s" : "");
        } catch (Exception e) {
            return "N/A";
        }
    }
    
    private void styleButton(JButton button, Color bgColor, Color textColor) {
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
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

class Event {
    String name;
    String code;
    String shortDesc;
    String status;
    int registered;
    String bannerPath;
    String startDate;
    String endDate;
    String location;
    String department;
    String organizer;
    String contactPerson;
    String contactEmail;
    String time;

    public Event(String name, String code, String shortDesc, String status, int registered, 
                String bannerPath, String startDate, String endDate, String location, 
                String department, String contactPerson, String contactEmail, String time) {
        this.name = name;
        this.code = code;
        this.shortDesc = shortDesc;
        this.status = status;
        this.registered = registered;
        this.bannerPath = bannerPath;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.department = department;
        this.organizer = organizer;
        this.contactPerson = contactPerson;
        this.contactEmail = contactEmail;
        this.time = time;
    }
}

class StudentDashboards extends JFrame {
    public StudentDashboards(String title) {
        super(title + " Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}