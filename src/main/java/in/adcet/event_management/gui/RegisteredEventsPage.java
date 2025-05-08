package in.adcet.event_management.gui;

import in.adcet.event_management.DTO.RegistrationDTO;
import in.adcet.event_management.service.RegisterService;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class RegisteredEventsPage extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JButton backButton, refreshButton;
    private JTextField searchField;
    private JLabel titleLabel, bannerLabel, searchLabel;
    private JComboBox<String> filterComboBox;
    private TableRowSorter<TableModel> sorter;
    private String username;
    private List<RegistrationDTO> events;

    private RegisterService registerService = new RegisterService();

    public RegisteredEventsPage(String username) {
        this.username = username;
        setTitle("Registered Events");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Left Banner
        ImageIcon bannerIcon = new ImageIcon(getClass().getClassLoader().getResource("images/eventlogo1.png"));
        if (bannerIcon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            bannerIcon = new ImageIcon(createPlaceholderImage(200, 600, new Color(240, 240, 240)));
        }
        bannerLabel = new JLabel(new ImageIcon(bannerIcon.getImage().getScaledInstance(200, 600, Image.SCALE_SMOOTH)));
        bannerLabel.setBounds(0, 0, 200, 600);
        add(bannerLabel);

        // Title
        titleLabel = new JLabel("REGISTERED EVENTS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 70, 70));
        titleLabel.setBounds(200, 20, 700, 40);
        add(titleLabel);

        // Search Panel
        JPanel searchPanel = new JPanel(null);
        searchPanel.setBounds(220, 70, 650, 40);
        searchPanel.setBackground(Color.WHITE);
        add(searchPanel);

        searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchLabel.setBounds(0, 10, 60, 20);
        searchPanel.add(searchLabel);

        searchField = new JTextField();
        searchField.setBounds(70, 5, 250, 30);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Add placeholder text
        searchField.setText("Search events...");
        searchField.setForeground(Color.GRAY);
        searchField.addFocusListener(new FocusAdapter() {
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
        searchPanel.add(searchField);

        // Filter ComboBox
        filterComboBox = new JComboBox<>(new String[]{"All Events", "Completed"});
        filterComboBox.setBounds(340, 5, 150, 30);
        filterComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchPanel.add(filterComboBox);

        // Refresh Button
        refreshButton = new JButton("Refresh");
        refreshButton.setBounds(510, 5, 120, 30);
        styleButton(refreshButton, new Color(65, 105, 225));
        searchPanel.add(refreshButton);

        // Back Button
        backButton = new JButton("Back");
        backButton.setBounds(750, 20, 100, 30);
        styleButton(backButton, new Color(70, 70, 70));
        add(backButton);

        backButton.addActionListener(e -> {
            dispose();
            new StudentDashboard(username).setVisible(true);
        });
        refreshButton.addActionListener(e -> refreshData());

        // Table with reduced columns
        String[] columns = {"Event Name", "Date", "Status", "Actions"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 3 ? JButton.class : Object.class;
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(70, 70, 70));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(new Color(230, 240, 255));
        table.setGridColor(new Color(220, 220, 220));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(0, 1));

        // Enable sorting
        sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(220, 130, 650, 420);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane);

        // Add Dummy Data
        addDummyData(); // get All events

        // Button Column Action Listener
        table.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        table.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));

        // Search functionality
        setupSearchFunctionality();

        // Filter functionality
        filterComboBox.addActionListener(e -> applyFilter());

        setVisible(true);
    }

    private BufferedImage createPlaceholderImage(int width, int height, Color color) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(color);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
        return image;
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }

    private void setupSearchFunctionality() {
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!searchField.getText().equals("Search events...")) {
                    search();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!searchField.getText().equals("Search events...")) {
                    search();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (!searchField.getText().equals("Search events...")) {
                    search();
                }
            }
        });
    }

    private void search() {
        String text = searchField.getText();
        if (text.trim().length() == 0 || text.equals("Search events...")) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    private void applyFilter() {
        String filter = (String) filterComboBox.getSelectedItem();
        if (filter.equals("All Events")) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter(filter, 2));
        }
    }

    private void refreshData() {
        model.setRowCount(0);
        addDummyData();
        JOptionPane.showMessageDialog(this, "Data refreshed successfully!", "Refresh", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addDummyData() {

        events = registerService.getEventsOfAStudent(username);
        for(RegistrationDTO e:events){
            model.addRow(new Object[]{e.getName(),e.getEventDate(),e.getStatus(),"Details"});
        }
//        model.addRow(new Object[]{"Coding Contest", "2025-05-20", "Pending", "View Details"});
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());

            if (isSelected) {
                setBackground(new Color(65, 105, 225));
                setForeground(Color.WHITE);
            } else {
                setBackground(new Color(65, 105, 225));
                setForeground(Color.WHITE);
            }

            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isPressed()) {
                g2.setColor(getBackground().darker());
            } else if (getModel().isRollover()) {
                g2.setColor(new Color(100, 149, 237));
            } else {
                g2.setColor(getBackground());
            }

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            g2.setColor(new Color(0, 0, 0, 20));
            g2.fillRoundRect(1, 2, getWidth()-2, getHeight()-1, 10, 10);
            g2.dispose();

            super.paintComponent(g);
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
            
            button.setBackground(new Color(65, 105, 225));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Segoe UI", Font.BOLD, 12));
            button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            button.setText(value == null ? "" : value.toString());
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                int row = table.convertRowIndexToModel(table.getEditingRow());
                String eventName = (String) model.getValueAt(row, 0);
                String date = (String) model.getValueAt(row, 1);
                String status = (String) model.getValueAt(row, 2);
                String time="";
                String venue="";
                showEventDetails(eventName, date, status,time,venue);
            }
            isPushed = false;
            return button.getText();
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }

    private void showEventDetails(String eventName, String date, String status,String time, String venue) {
        JDialog detailsDialog = new JDialog(this, "Event Details", true);
        detailsDialog.setSize(500, 500);
        detailsDialog.setLocationRelativeTo(this);
        detailsDialog.setResizable(false);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        // Header with icon
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBackground(Color.WHITE);
        
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/event_icon.png"));
        if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            icon = new ImageIcon(createPlaceholderImage(40, 40, new Color(240, 240, 240)));
        }
        JLabel iconLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        
        JLabel titleLabel = new JLabel(eventName);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(70, 70, 70));
        
        headerPanel.add(iconLabel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Main content panel with scroll
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Details panel with card-like appearance
        JPanel detailsPanel = new JPanel(new GridLayout(0, 1, 5, 10));
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        detailsPanel.setBackground(new Color(250, 250, 250));
        
        // Event Details Section
        addDetailRow(detailsPanel, "Event Name:", eventName);
        addDetailRow(detailsPanel, "Date:", date);
        addDetailRow(detailsPanel, "Time:", time);
        addDetailRow(detailsPanel, "Venue:", venue);
//        addDetailRow(detailsPanel, "Organizer:", "Computer Science Department");
        
        // Status row with improved styling
        JPanel statusRow = new JPanel(new BorderLayout(5, 0));
        statusRow.setBackground(new Color(250, 250, 250));
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusRow.add(statusLabel, BorderLayout.WEST);
        
        JLabel statusValue = createStatusLabel(status);
        statusValue.setHorizontalAlignment(SwingConstants.LEFT);
        statusRow.add(statusValue, BorderLayout.CENTER);
        detailsPanel.add(statusRow);
        
        contentPanel.add(detailsPanel);
        contentPanel.add(Box.createVerticalStrut(15));

        // Participants Information
        JPanel participantsPanel = new JPanel(new BorderLayout());
        participantsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)), 
                "Participants Information"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        participantsPanel.setBackground(new Color(250, 250, 250));
        
        JTextArea participantsArea = new JTextArea(
//            "• Total Registered: 45 students\n" +
//            "• Team Size: 1-3 members\n" +
//            "• Requirements: Bring your own laptop\n" +
//            "• Contact: prof.smith@university.edu"
        );
        participantsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        participantsArea.setEditable(false);
        participantsArea.setBackground(new Color(250, 250, 250));
        participantsPanel.add(participantsArea, BorderLayout.CENTER);
        
        contentPanel.add(participantsPanel);
        contentPanel.add(Box.createVerticalStrut(15));

        // Prize Information (only for competitions)
        if (status.equals("Winner")) {
            JPanel prizePanel = new JPanel(new BorderLayout());
            prizePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220)), 
                    "Prize Information"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            prizePanel.setBackground(new Color(250, 250, 250));
            
            JTextArea prizeArea = new JTextArea(
//                "• 1st Prize: $1000 + Trophy\n" +
//                "• 2nd Prize: $500\n" +
//                "• 3rd Prize: $250\n" +
//                "• Special awards for innovation"
            );
            prizeArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            prizeArea.setEditable(false);
            prizeArea.setBackground(new Color(250, 250, 250));
            prizePanel.add(prizeArea, BorderLayout.CENTER);
            
            contentPanel.add(prizePanel);
            contentPanel.add(Box.createVerticalStrut(15));
        }

        // Description with improved styling
        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)), 
                "Event Description"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        descPanel.setBackground(new Color(250, 250, 250));
        
        JTextArea descArea = new JTextArea(
//            "This event is a " + eventName + " that will test participants' skills in various areas. " +
//            "The competition will consist of multiple rounds with increasing difficulty. " +
//            "Participants will be judged by a panel of experts from the industry.\n\n" +
//            "Rules:\n" +
//            "1. All participants must register beforehand\n" +
//            "2. No external help is allowed during the competition\n" +
//            "3. Decisions by judges are final\n\n" +
//            "What to bring:\n" +
//            "- Student ID\n" +
//            "- Necessary equipment (laptop, etc.)\n" +
//            "- Your creativity and enthusiasm!"
        );
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setBackground(new Color(250, 250, 250));
        descPanel.add(new JScrollPane(descArea), BorderLayout.CENTER);
        
        contentPanel.add(descPanel);

        // Add close button at bottom
        JButton closeButton = new JButton("Close");
        styleButton(closeButton, new Color(70, 70, 70));
        closeButton.addActionListener(e -> detailsDialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(closeButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        detailsDialog.add(panel);
        detailsDialog.setVisible(true);
    }

    private void addDetailRow(JPanel panel, String label, String value) {
        JPanel rowPanel = new JPanel(new BorderLayout(5, 0));
        rowPanel.setBackground(new Color(250, 250, 250));
        
        JLabel labelLbl = new JLabel(label);
        labelLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        rowPanel.add(labelLbl, BorderLayout.WEST);
        
        JLabel valueLbl = new JLabel(value);
        valueLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rowPanel.add(valueLbl, BorderLayout.CENTER);
        
        panel.add(rowPanel);
    }

    private JLabel createStatusLabel(String status) {
        JLabel label = new JLabel(status);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setOpaque(true);
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        
        switch(status) {
            case "Pending":
                label.setForeground(new Color(150, 100, 0));
                label.setBackground(new Color(255, 245, 200));
                label.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 150, 0), 1),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
                break;
            case "Completed":
                label.setForeground(new Color(0, 120, 0));
                label.setBackground(new Color(220, 255, 220));
                label.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 150, 0), 1),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
                break;
            case "Winner":
                label.setForeground(new Color(0, 0, 150));
                label.setBackground(new Color(220, 230, 255));
                label.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 0, 180), 1),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
                break;
            default:
                label.setForeground(Color.BLACK);
                label.setBackground(Color.WHITE);
        }
        
        return label;
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            try {
//                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            new RegisteredEventsPage();
//        });
//    }
}