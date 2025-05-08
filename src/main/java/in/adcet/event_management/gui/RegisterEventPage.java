package in.adcet.event_management.gui;

import in.adcet.event_management.entity.Events;
import in.adcet.event_management.entity.User;
import in.adcet.event_management.service.RegisterService;
import in.adcet.event_management.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class RegisterEventPage extends JFrame {
    private Color primaryColor = new Color(12, 53, 106);      // Dark blue
    private Color secondaryColor = new Color(240, 240, 245);  // Light gray
    private Color accentColor = new Color(65, 105, 225);      // Royal blue
    private Color textColor = new Color(60, 60, 60);          // Dark gray for text
    private UserService userService = new UserService();
    private RegisterService registerService = new RegisterService();

    public RegisterEventPage(Events event) {
        setTitle("Register for " + event.getName());
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Main container with left banner and right form
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(secondaryColor);

        // Left Banner Panel (250px width)
        JPanel leftBannerPanel = new JPanel(new BorderLayout());
        leftBannerPanel.setPreferredSize(new Dimension(250, getHeight()));
        leftBannerPanel.setBackground(new Color(230, 230, 240));
        leftBannerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, primaryColor));

        // Banner image with proper scaling
        ImageIcon originalIcon = new ImageIcon("./images/eventlogo1.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(250, 600, Image.SCALE_SMOOTH);
        JLabel bannerImage = new JLabel(new ImageIcon(scaledImage));
        bannerImage.setHorizontalAlignment(SwingConstants.CENTER);
        bannerImage.setVerticalAlignment(SwingConstants.CENTER);
        bannerImage.setBorder(new EmptyBorder(30, 20, 30, 20));
        leftBannerPanel.add(bannerImage, BorderLayout.CENTER);
        mainPanel.add(leftBannerPanel, BorderLayout.WEST);

        // Right Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(secondaryColor);
        formPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Title with icon
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        titlePanel.setBackground(secondaryColor);
        JLabel formTitle = new JLabel("Registration Form");
        formTitle.setFont(new Font("Arial", Font.BOLD, 24));
        formTitle.setForeground(primaryColor);
        titlePanel.add(new JLabel(new ImageIcon("./images/register_icon.png")));
        titlePanel.add(formTitle);
        formPanel.add(titlePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Event Information (autofilled from event object)
        JPanel eventPanel = createLabeledFieldPanel("Event Information");
        addLabeledField(eventPanel, "Event Name:", createReadOnlyField(event.getName()));
        addLabeledField(eventPanel, "Event Code:", createReadOnlyField(event.getCode()));
        formPanel.add(eventPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Personal Information
        JPanel personalPanel = createLabeledFieldPanel("Personal Information");
        addLabeledField(personalPanel, "Full Name:", createTextField(""));
        addLabeledField(personalPanel, "Gender:", createComboBox(new String[]{"Male", "Female", "Other", "Prefer not to say"}));
        addLabeledField(personalPanel, "Email:", createTextField(""));
        addLabeledField(personalPanel, "Phone:", createTextField(""));
        formPanel.add(personalPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Terms and Conditions
        JPanel termsPanel = new JPanel();
        termsPanel.setLayout(new BoxLayout(termsPanel, BoxLayout.Y_AXIS));
        termsPanel.setBackground(secondaryColor);
        termsPanel.setBorder(BorderFactory.createTitledBorder("Agreement"));
        
        JCheckBox agreeCheck = new JCheckBox("I agree to the terms and conditions");
        agreeCheck.setFont(new Font("Arial", Font.PLAIN, 12));
        agreeCheck.setBackground(secondaryColor);
        
        JButton viewTermsBtn = new JButton("View Terms and Conditions");
        styleButton(viewTermsBtn, new Color(100, 100, 100), Color.WHITE, 12);
        viewTermsBtn.addActionListener(e -> showTermsAndConditions());
        
        JPanel termsButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        termsButtonPanel.setBackground(secondaryColor);
        termsButtonPanel.add(agreeCheck);
        termsButtonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        termsButtonPanel.add(viewTermsBtn);
        
        termsPanel.add(termsButtonPanel);
        formPanel.add(termsPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 10));
        buttonPanel.setBackground(secondaryColor);
        
        JButton submitBtn = new JButton("Submit Registration");
        styleButton(submitBtn, accentColor, Color.WHITE, 14);
        submitBtn.addActionListener(e -> {
            if (validateForm(personalPanel)) {
                submitRegistration(event, getFieldValue(personalPanel, "Full Name:"), 
                                 getComboBoxValue(personalPanel, "Gender:"),
                                 getFieldValue(personalPanel, "Email:"), 
                                 getFieldValue(personalPanel, "Phone:"));
            }
        });
        
        JButton cancelBtn = new JButton("Cancel");
        styleButton(cancelBtn, new Color(150, 150, 150), Color.WHITE, 14);
        cancelBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(submitBtn);
        buttonPanel.add(cancelBtn);
        
        formPanel.add(buttonPanel);

        // Add form panel to scroll pane
        JScrollPane formScrollPane = new JScrollPane(formPanel);
        formScrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(formScrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createLabeledFieldPanel(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(secondaryColor);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1), title));
        return panel;
    }

    private void addLabeledField(JPanel panel, String labelText, JComponent field) {
        JPanel fieldPanel = new JPanel(new BorderLayout(10, 0));
        fieldPanel.setBackground(secondaryColor);
        fieldPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setPreferredSize(new Dimension(100, 20));
        
        fieldPanel.add(label, BorderLayout.WEST);
        fieldPanel.add(field, BorderLayout.CENTER);
        
        panel.add(fieldPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
    }

    private JTextField createReadOnlyField(String value) {
        JTextField field = new JTextField(value);
        field.setEditable(false);
        field.setBackground(new Color(245, 245, 245));
        field.setFont(new Font("Arial", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }

    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setFont(new Font("Arial", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }

    private JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(new Font("Arial", Font.PLAIN, 12));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(3, 5, 3, 5)
        ));
        return combo;
    }

    private void styleButton(JButton button, Color bgColor, Color textColor, int fontSize) {
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, fontSize));
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

    private boolean validateForm(JPanel personalPanel) {
        String fullName = getFieldValue(personalPanel, "Username");
        String email = getFieldValue(personalPanel, "Email:");
        String phone = getFieldValue(personalPanel, "Phone:");
        
        if (fullName.trim().isEmpty()) {
            showError("Username is required");
            return false;
        }
        
        if (email.trim().isEmpty() || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showError("Please enter a valid email address");
            return false;
        }
        
        if (phone.trim().isEmpty() || !phone.matches("^[0-9]{10,15}$")) {
            showError("Please enter a valid phone number (10-15 digits)");
            return false;
        }
        
        return true;
    }

    private String getFieldValue(JPanel panel, String label) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel fieldPanel = (JPanel) comp;
                JLabel fieldLabel = (JLabel) fieldPanel.getComponent(0);
                if (fieldLabel.getText().equals(label)) {
                    JTextField field = (JTextField) fieldPanel.getComponent(1);
                    return field.getText();
                }
            }
        }
        return "";
    }

    private String getComboBoxValue(JPanel panel, String label) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel fieldPanel = (JPanel) comp;
                JLabel fieldLabel = (JLabel) fieldPanel.getComponent(0);
                if (fieldLabel.getText().equals(label)) {
                    JComboBox<?> combo = (JComboBox<?>) fieldPanel.getComponent(1);
                    return combo.getSelectedItem().toString();
                }
            }
        }
        return "";
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    private void submitRegistration(Events event, String username, String gender,
                                    String email, String phone) {

        User user = userService.getUserByUsername(username);
        if(user==null){
            showError("User not found");
            return;
        }
        registerService.registerAEvent(event,user);
        // save to the database for email user
        String message = "<html><div style='text-align:center;'><h2>Registration Successful!</h2>" +
                        "<p><b>Event:</b> " + event.getName() + " (" + event.getCode() + ")</p>" +
                        "<p><b>Name:</b> " + username + "</p>" +
                        "<p><b>Gender:</b> " + gender + "</p>" +
                        "<p><b>Email:</b> " + email + "</p>" +
                        "<p><b>Phone:</b> " + phone + "</p><br>" +
                        "<p>Thank you for registering!</p></div></html>";
        
        JOptionPane.showMessageDialog(this, message, "Registration Complete", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private void showTermsAndConditions() {
        JDialog termsDialog = new JDialog(this, "Terms and Conditions", true);
        termsDialog.setSize(550, 450);
        termsDialog.setLocationRelativeTo(this);
        termsDialog.setLayout(new BorderLayout());
        
        JTextArea termsText = new JTextArea();
        termsText.setEditable(false);
        termsText.setLineWrap(true);
        termsText.setWrapStyleWord(true);
        termsText.setText(getTermsAndConditionsText());
        termsText.setFont(new Font("Arial", Font.PLAIN, 12));
        termsText.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JScrollPane scrollPane = new JScrollPane(termsText);
        termsDialog.add(scrollPane, BorderLayout.CENTER);
        
        JButton closeBtn = new JButton("Close");
        styleButton(closeBtn, accentColor, Color.WHITE, 12);
        closeBtn.addActionListener(e -> termsDialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(closeBtn);
        termsDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        termsDialog.setVisible(true);
    }

    private String getTermsAndConditionsText() {
        return "TERMS AND CONDITIONS FOR EVENT REGISTRATION\n\n" +
               "1. Registration Commitment\n" +
               "   By registering for this event, you commit to attending unless you cancel at least 48 hours before the event.\n\n" +
               "2. Code of Conduct\n" +
               "   All participants must adhere to the university's code of conduct during the event.\n\n" +
               "3. Data Privacy\n" +
               "   Your personal information will only be used for event-related communications and will not be shared with third parties.\n\n" +
               "4. Photography\n" +
               "   By attending, you consent to being photographed for promotional materials.\n\n" +
               "5. Liability\n" +
               "   The organizers are not responsible for any personal injury or loss of property during the event.\n\n" +
               "6. Cancellation Policy\n" +
               "   Event may be cancelled or rescheduled due to unforeseen circumstances.\n\n" +
               "7. Refund Policy\n" +
               "   Any paid registrations are non-refundable unless the event is cancelled by organizers.\n\n" +
               "By registering, you acknowledge that you have read and agree to these terms and conditions.";
    }
}