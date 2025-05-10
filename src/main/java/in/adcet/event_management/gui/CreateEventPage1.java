package in.adcet.event_management.gui;


import in.adcet.event_management.entity.Events;
import in.adcet.event_management.service.EventService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;



public class CreateEventPage1 extends JFrame {
    private JLabel selectedFile;
    private EventService eventService;
    private String username;

    private String bannerFilePath = null;

    public CreateEventPage1(String usernamee) {
        this.username = usernamee;
        this.eventService = new EventService();
        setTitle("Create Event - Admin");
        setSize(1000, 680);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Left image panel
        JPanel imagePanel = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(Math.max(200, Math.min(300, getParent().getWidth() / 4)), getParent().getHeight());
            }
        };
        imagePanel.setBackground(Color.WHITE);
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/eventlogo1.png"));
        imageLabel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (imagePanel.getWidth() > 0 && imagePanel.getHeight() > 0) {
                    Image img = icon.getImage().getScaledInstance(
                            imagePanel.getWidth(), imagePanel.getHeight(), Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(img));
                }
            }
        });
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        JPanel formWrapper = new JPanel();
        formWrapper.setBackground(Color.WHITE);
        formWrapper.setLayout(new BoxLayout(formWrapper, BoxLayout.Y_AXIS));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);

        JLabel nameLabel = new JLabel("Event Name:");
        nameLabel.setFont(labelFont);
        JTextField nameField = new JTextField(20);

        JLabel codeLabel = new JLabel("Event Code:");
        codeLabel.setFont(labelFont);
        JTextField codeField = new JTextField(20);


        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(labelFont);
        JTextArea descArea = new JTextArea(3, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descArea);

        // DATE + TIME Panel
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setFont(labelFont);
        JTextField dateField = new JTextField(10);

        JLabel timeLabel = new JLabel("Time:");
        timeLabel.setFont(labelFont);
        JTextField timeField = new JTextField(10);

        JPanel dateTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        dateTimePanel.setBackground(Color.WHITE);
        dateTimePanel.add(dateField);
        dateTimePanel.add(timeLabel);
        dateTimePanel.add(timeField);

        JLabel venueLabel = new JLabel("Venue:");
        venueLabel.setFont(labelFont);
        JTextField venueField = new JTextField(20);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(labelFont);
        String[] categories = {"Cultural", "Technical", "Sports", "Workshop"};
        JComboBox<String> categoryBox = new JComboBox<>(categories);

        JLabel maxLabel = new JLabel("Max Participants:");
        maxLabel.setFont(labelFont);
        JTextField maxField = new JTextField(20);

        JLabel regLabel = new JLabel("Register Before:");
        regLabel.setFont(labelFont);
        JTextField regField = new JTextField(20);

        JLabel bannerLabel = new JLabel("Upload Banner:");
        bannerLabel.setFont(labelFont);
        JButton uploadButton = new JButton("Upload");
        uploadButton.setFocusPainted(false);
        uploadButton.setOpaque(true);
        uploadButton.setContentAreaFilled(true);
        uploadButton.setBorderPainted(false);
        selectedFile = new JLabel("No file selected");

        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                bannerFilePath = file.getAbsolutePath();   // internally save path
                selectedFile.setText("Selected: " + file.getName());  // just show name
            } else {
                selectedFile.setText("No file selected");
                bannerFilePath = null;
            }
        });

//

        int row = 0;
        addRow(formPanel, gbc, row++, nameLabel, nameField);
        addRow(formPanel, gbc, row++, codeLabel, codeField);
        addRow(formPanel, gbc, row++, descLabel, descScroll);
        addRow(formPanel, gbc, row++, dateLabel, dateTimePanel); // Date & Time in same row
        addRow(formPanel, gbc, row++, venueLabel, venueField);
        addRow(formPanel, gbc, row++, categoryLabel, categoryBox);
        addRow(formPanel, gbc, row++, maxLabel, maxField);
        addRow(formPanel, gbc, row++, regLabel, regField);
        addRow(formPanel, gbc, row++, bannerLabel, uploadButton);

        gbc.gridx = 1;
        gbc.gridy = row++;
        formPanel.add(selectedFile, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20)) {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, super.getPreferredSize().height);
            }
        };

        buttonPanel.setBackground(Color.WHITE);

        JButton createButton = new JButton("Create Event");
        createButton.setOpaque(true);
        createButton.setBorderPainted(false);

        JButton resetButton = new JButton("Reset");
        resetButton.setOpaque(true);
        resetButton.setBorderPainted(false);

        JButton backButton = new JButton("Back");
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);

        // Updated colors with better contrast
        addHoverEffect(createButton, new Color(0, 120, 60));  // Darker green
        addHoverEffect(resetButton, new Color(220, 120, 0));  // Darker orange
        addHoverEffect(backButton, new Color(180, 0, 0));     // Darker red

        // Set consistent button appearance
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);
        createButton.setFont(buttonFont);
        resetButton.setFont(buttonFont);
        backButton.setFont(buttonFont);

        createButton.setForeground(Color.white);
        resetButton.setForeground(Color.white);
        backButton.setForeground(Color.white);

        Dimension buttonSize = new Dimension(130, 40);
        createButton.setPreferredSize(buttonSize);
        resetButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);

        buttonPanel.add(createButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(backButton);

        createButton.addActionListener(e -> {
            // Retrieve data from the form
            String eventName = nameField.getText().trim();
            String eventCode = codeField.getText().trim();
            String description = descArea.getText();
            String dateStr = dateField.getText();
            String timeStr = timeField.getText();
            String venue = venueField.getText();
            String category = (String) categoryBox.getSelectedItem();
            String maxParticipantsStr = maxField.getText().trim();
            String registerBeforeStr = regField.getText();
            String bannerPath = bannerFilePath;

            // Validate input
            if (eventName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter event name!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (eventCode.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter event code!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (dateStr.isEmpty() || timeStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both date and time valid!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE).isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "The date cannot be in the past!", "Invalid Date", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (venue.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the venue!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (maxParticipantsStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the maximum number of participants!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (registerBeforeStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the registration deadline!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                LocalDate eventDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
                LocalTime eventTime = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"));
                int maxParticipants = Integer.parseInt(maxParticipantsStr);
                LocalDate registrationDeadline = LocalDate.parse(registerBeforeStr, DateTimeFormatter.ISO_DATE);

                // Create an Event object
                Events newEvent = new Events();
                newEvent.setName(eventName);
                newEvent.setCode(eventCode);
                newEvent.setDescription(description);
                newEvent.setEventDate(eventDate);
                newEvent.setTime(eventTime);
                newEvent.setVenue(venue);
                newEvent.setCategory(category);
                newEvent.setMaxParticipant(maxParticipants);
                newEvent.setRegistrationDeadline(registrationDeadline);
                newEvent.setBannerPath(bannerPath); // You might need to handle file saving separately

                // Call the backend service to add the event
                boolean isAdded = eventService.addANewEvent(newEvent);

                if (isAdded) {
                    JOptionPane.showMessageDialog(this, "Event Created Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    resetButton.doClick(); // Clear the form after successful creation
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to create event!", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date or time format. Please use YYYY-MM-DD for date and HH:MM for time.", "Warning", JOptionPane.WARNING_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for Max Participants!", "Warning", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "An error occurred while creating the event: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace(); // For debugging purposes
            }
        });

        resetButton.addActionListener(e -> {
            nameField.setText("");
            codeField.setText("");
            descArea.setText("");
            dateField.setText("");
            timeField.setText("");
            venueField.setText("");
            maxField.setText("");
            regField.setText("");
            selectedFile.setText("No file selected");
            categoryBox.setSelectedIndex(0);
        });

        backButton.addActionListener(e -> {
            dispose(); // Close the current window
            new AdminDashboard(username); // Open the admin dashboard
        });


        JLabel heading = new JLabel("CREATE NEW EVENT", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 24));
        heading.setForeground(new Color(0, 70, 129));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        formWrapper.add(heading);
        formWrapper.add(Box.createRigidArea(new Dimension(0, 10)));
        formWrapper.add(formPanel);
        formWrapper.add(Box.createVerticalGlue());
        formWrapper.add(buttonPanel);

        add(imagePanel, BorderLayout.WEST);
        add(formWrapper, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                revalidate();
                repaint();
            }
        });
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, JComponent label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(label, gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void addHoverEffect(JButton button, Color baseColor) {
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(baseColor.darker());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
            }
        });
    }
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            try {
//                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            new CreateEventPage1().setVisible(true);
//        });
//    }
}
