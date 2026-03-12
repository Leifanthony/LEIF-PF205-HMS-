package ADMIN;

import config.DataBaseCon;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import net.proteanit.sql.DbUtils;

public class CustomerDetailBills extends JFrame {
    
    // Custom colors for hotel theme
    private static final Color PRIMARY_GOLD = new Color(255, 215, 0);
    private static final Color SECONDARY_GOLD = new Color(218, 165, 32);
    private static final Color DARK_PURPLE = new Color(75, 0, 130);
    private static final Color MEDIUM_PURPLE = new Color(128, 0, 128);
    private static final Color LIGHT_PURPLE = new Color(186, 85, 211);
    private static final Color PANEL_DARK = new Color(30, 30, 30);
    private static final Color PANEL_LIGHT = new Color(45, 45, 45);
    
    // Custom button with glow and hover effects
    private class ModernButton extends JButton {
        private float glowIntensity = 0f;
        private float pressIntensity = 0f;
        private Timer glowTimer;
        private Timer pressTimer;
        
        public ModernButton(String text) {
            super(text);
            setFont(new Font("Constantia", Font.BOLD, 14));
            setForeground(Color.WHITE);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    startGlowAnimation(true);
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    startGlowAnimation(false);
                }
                
                @Override
                public void mousePressed(MouseEvent e) {
                    startPressAnimation(true);
                }
                
                @Override
                public void mouseReleased(MouseEvent e) {
                    startPressAnimation(false);
                }
            });
        }
        
        private void startGlowAnimation(boolean increase) {
            if (glowTimer != null && glowTimer.isRunning()) {
                glowTimer.stop();
            }
            
            glowTimer = new Timer(20, e -> {
                if (increase) {
                    glowIntensity = Math.min(1f, glowIntensity + 0.15f);
                    setForeground(PRIMARY_GOLD);
                } else {
                    glowIntensity = Math.max(0f, glowIntensity - 0.15f);
                    setForeground(Color.WHITE);
                }
                repaint();
                
                if ((increase && glowIntensity >= 1f) || (!increase && glowIntensity <= 0f)) {
                    glowTimer.stop();
                }
            });
            glowTimer.start();
        }
        
        private void startPressAnimation(boolean press) {
            if (pressTimer != null && pressTimer.isRunning()) {
                pressTimer.stop();
            }
            
            pressTimer = new Timer(15, e -> {
                if (press) {
                    pressIntensity = Math.min(1f, pressIntensity + 0.2f);
                } else {
                    pressIntensity = Math.max(0f, pressIntensity - 0.2f);
                }
                repaint();
                
                if ((press && pressIntensity >= 1f) || (!press && pressIntensity <= 0f)) {
                    pressTimer.stop();
                }
            });
            pressTimer.start();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int w = getWidth();
            int h = getHeight();
            
            // 3D Press effect
            int pressOffset = (int)(pressIntensity * 3);
            
            // Draw gradient background
            GradientPaint gradient = new GradientPaint(
                0, pressOffset, new Color(60, 60, 60),
                w, h - pressOffset, new Color(40, 40, 40)
            );
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, w, h, 20, 20);
            
            // Draw glow effect
            if (glowIntensity > 0) {
                g2.setColor(new Color(255, 215, 0, (int)(150 * glowIntensity)));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(2, 2, w-5, h-5, 18, 18);
                
                for (int i = 1; i <= 2; i++) {
                    int alpha = (int)(50 * glowIntensity * (1 - i/3f));
                    g2.setColor(new Color(255, 215, 0, alpha));
                    g2.setStroke(new BasicStroke(i));
                    g2.drawRoundRect(i, i, w-2*i-1, h-2*i-1, 20-i, 20-i);
                }
            }
            
            // Draw border
            g2.setColor(new Color(255, 255, 255, 30));
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(1, 1, w-3, h-3, 18, 18);
            
            g2.dispose();
            super.paintComponent(g);
        }
    }
    
    // Custom panel button
    private class ModernPanelButton extends JPanel {
        private boolean isHovered = false;
        private float glowIntensity = 0f;
        private Timer glowTimer;
        private String text;
        private Runnable onClick;
        private Color bgColor;
        
        public ModernPanelButton(String text, Color bgColor, Runnable onClick) {
            this.text = text;
            this.bgColor = bgColor;
            this.onClick = onClick;
            setPreferredSize(new Dimension(100, 35));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setOpaque(false);
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    startGlowAnimation(true);
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    startGlowAnimation(false);
                }
                
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (onClick != null) {
                        onClick.run();
                    }
                }
            });
        }
        
        private void startGlowAnimation(boolean increase) {
            if (glowTimer != null && glowTimer.isRunning()) {
                glowTimer.stop();
            }
            
            glowTimer = new Timer(20, e -> {
                if (increase) {
                    glowIntensity = Math.min(1f, glowIntensity + 0.15f);
                } else {
                    glowIntensity = Math.max(0f, glowIntensity - 0.15f);
                }
                repaint();
                
                if ((increase && glowIntensity >= 1f) || (!increase && glowIntensity <= 0f)) {
                    glowTimer.stop();
                }
            });
            glowTimer.start();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            int w = getWidth();
            int h = getHeight();
            
            // Draw gradient background
            GradientPaint gradient = new GradientPaint(
                0, 0, bgColor,
                w, 0, new Color(bgColor.getRed() + 20, bgColor.getGreen() + 20, bgColor.getBlue() + 20)
            );
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, w, h, 15, 15);
            
            // Draw glow effect
            if (glowIntensity > 0) {
                g2.setColor(new Color(255, 215, 0, (int)(150 * glowIntensity)));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, w-3, h-3, 13, 13);
            }
            
            // Draw text
            g2.setFont(new Font("Constantia", Font.BOLD, 13));
            FontMetrics fm = g2.getFontMetrics();
            int textX = (w - fm.stringWidth(text)) / 2;
            int textY = (h + fm.getAscent() - fm.getDescent()) / 2;
            
            // Text shadow
            g2.setColor(new Color(0, 0, 0, 100));
            g2.drawString(text, textX + 1, textY + 1);
            
            // Text
            g2.setColor(Color.WHITE);
            g2.drawString(text, textX, textY);
            
            g2.dispose();
        }
    }
    
    // Custom text field for search
    private class ModernTextField extends JTextField {
        private boolean isFocused = false;
        private float glowIntensity = 0f;
        private Timer glowTimer;
        
        public ModernTextField() {
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setForeground(Color.WHITE);
            setCaretColor(PRIMARY_GOLD);
            setBackground(new Color(60, 60, 60));
            setBorder(BorderFactory.createCompoundBorder(
                new RoundedCornerBorder(10),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
            
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    isFocused = true;
                    startGlowAnimation(true);
                    setBorder(BorderFactory.createCompoundBorder(
                        new RoundedCornerBorder(10, PRIMARY_GOLD, 2),
                        BorderFactory.createEmptyBorder(7, 11, 7, 11)
                    ));
                }
                
                @Override
                public void focusLost(FocusEvent e) {
                    isFocused = false;
                    startGlowAnimation(false);
                    setBorder(BorderFactory.createCompoundBorder(
                        new RoundedCornerBorder(10),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                    ));
                }
            });
        }
        
        private void startGlowAnimation(boolean increase) {
            if (glowTimer != null && glowTimer.isRunning()) {
                glowTimer.stop();
            }
            
            glowTimer = new Timer(20, e -> {
                if (increase) {
                    glowIntensity = Math.min(1f, glowIntensity + 0.15f);
                } else {
                    glowIntensity = Math.max(0f, glowIntensity - 0.15f);
                }
                repaint();
                
                if ((increase && glowIntensity >= 1f) || (!increase && glowIntensity <= 0f)) {
                    glowTimer.stop();
                }
            });
            glowTimer.start();
        }
    }
    
    // Rounded corner border class
    private class RoundedCornerBorder extends AbstractBorder {
        private int radius;
        private Color color;
        private int thickness;
        
        public RoundedCornerBorder(int radius) {
            this.radius = radius;
            this.color = new Color(100, 100, 100);
            this.thickness = 1;
        }
        
        public RoundedCornerBorder(int radius, Color color, int thickness) {
            this.radius = radius;
            this.color = color;
            this.thickness = thickness;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x, y, width - thickness, height - thickness, radius, radius);
            g2.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius/2, radius/2, radius/2, radius/2);
        }
    }

    public CustomerDetailBills() {
        initComponents();
        applyModernDesign();
        displayData();
        System.out.println("CustomerPage constructor called");
    }
    
    private void applyModernDesign() {
        // Clear content pane
        getContentPane().removeAll();
        getContentPane().setLayout(null);
        
        // Set frame size
        setSize(1100, 650);
        setLocationRelativeTo(null);
        
        // Make background completely transparent
        setBackground(new Color(0, 0, 0, 0));
        
        // Create main panel with transparent background
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBounds(0, 0, 1100, 650);
        mainPanel.setOpaque(false);
        
        // Create main panel for content
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(40, 40, 40),
                    getWidth(), getHeight(), new Color(60, 60, 60)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                
                g2.setColor(new Color(255, 215, 0, 100));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(2, 2, getWidth()-5, getHeight()-5, 26, 26);
                
                g2.dispose();
            }
        };
        contentPanel.setLayout(null);
        contentPanel.setBounds(30, 30, 1040, 590);
        contentPanel.setOpaque(false);
        
        // Main Title inside panel
        JLabel mainTitleLabel = new JLabel("CUSTOMER BILLING") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                Font font = new Font("Constantia", Font.BOLD, 32);
                g2.setFont(font);
                FontMetrics fm = g2.getFontMetrics();
                
                String text = "CUSTOMER BILLING";
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                
                // Draw shadow
                g2.setColor(new Color(0, 0, 0, 100));
                g2.drawString(text, x + 2, y + 2);
                
                // Draw gradient text
                GradientPaint gradient = new GradientPaint(
                    0, 0, PRIMARY_GOLD,
                    getWidth(), 0, SECONDARY_GOLD
                );
                g2.setPaint(gradient);
                g2.drawString(text, x, y);
                
                g2.dispose();
            }
        };
        mainTitleLabel.setBounds(0, 20, 1040, 50);
        mainTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(mainTitleLabel);
        
        // Control panel for buttons and search
        JPanel controlPanel = new JPanel(null);
        controlPanel.setBounds(30, 80, 980, 45);
        controlPanel.setOpaque(false);
        
        // Back button
        ModernPanelButton backBtn = new ModernPanelButton("BACK", MEDIUM_PURPLE, () -> {
            dispose();
            new AdminDashboard().setVisible(true);
        });
        backBtn.setBounds(0, 5, 80, 35);
        controlPanel.add(backBtn);
        
        // Refresh button
        ModernPanelButton refreshBtn = new ModernPanelButton("REFRESH", DARK_PURPLE, () -> {
            displayData();
        });
        refreshBtn.setBounds(90, 5, 90, 35);
        controlPanel.add(refreshBtn);
        
        // Search label
        JLabel searchLabel = new JLabel("SEARCH BY CHECKOUT DATE:");
        searchLabel.setFont(new Font("Constantia", Font.BOLD, 14));
        searchLabel.setForeground(PRIMARY_GOLD);
        searchLabel.setBounds(400, 10, 220, 25);
        controlPanel.add(searchLabel);
        
        // Search field
        search = new ModernTextField();
        search.setBounds(620, 5, 180, 35);
        search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    searchTable();
                }
            }
        });
        controlPanel.add(search);
        
        // Search button
        ModernPanelButton searchBtn = new ModernPanelButton("SEARCH", LIGHT_PURPLE, () -> {
            searchTable();
        });
        searchBtn.setBounds(810, 5, 90, 35);
        controlPanel.add(searchBtn);
        
        contentPanel.add(controlPanel);
        
        // Setup table
        setupTable();
        
        jScrollPane1.setBounds(30, 140, 980, 400);
        jScrollPane1.setOpaque(false);
        jScrollPane1.getViewport().setBackground(new Color(50, 50, 50));
        jScrollPane1.setBorder(BorderFactory.createCompoundBorder(
            new RoundedCornerBorder(15),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));
        contentPanel.add(jScrollPane1);
        
        // Status label
        JLabel statusLabel = new JLabel("Double-click any row to print receipt");
        statusLabel.setFont(new Font("Constantia", Font.ITALIC, 12));
        statusLabel.setForeground(new Color(180, 180, 180));
        statusLabel.setBounds(30, 550, 300, 20);
        contentPanel.add(statusLabel);
        
        mainPanel.add(contentPanel);
        getContentPane().add(mainPanel);
        
        // Update references
        refresh = refreshBtn;
        search_button = searchBtn;
        
        revalidate();
        repaint();
    }
 private void setupTable() {
    // Table appearance
    customer_table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    customer_table.setForeground(Color.WHITE);
    customer_table.setBackground(new Color(50, 50, 50));
    customer_table.setSelectionBackground(MEDIUM_PURPLE);
    customer_table.setSelectionForeground(PRIMARY_GOLD);
    customer_table.setRowHeight(30);
    customer_table.setGridColor(new Color(80, 80, 80));
    customer_table.setShowGrid(true);
    customer_table.setIntercellSpacing(new Dimension(0, 0));
    
    // IMPORTANT: Make table non-editable
    customer_table.setDefaultEditor(Object.class, null);
    
    // Enable row selection
    customer_table.setRowSelectionAllowed(true);
    
    // Style the table header
    JTableHeader header = customer_table.getTableHeader();
    header.setFont(new Font("Constantia", Font.BOLD, 13));
    header.setForeground(PRIMARY_GOLD);
    header.setBackground(new Color(40, 40, 40));
    header.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
    
    // Center align all cells
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(JLabel.CENTER);
            if (!isSelected) {
                setBackground(new Color(50, 50, 50));
                setForeground(Color.WHITE);
            }
            return this;
        }
    };
    
    for (int i = 0; i < customer_table.getColumnCount(); i++) {
        customer_table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
}
    public void displayData() {
        try {
            DataBaseCon dbc = new DataBaseCon();
            ResultSet rs = dbc.getData(
                "SELECT id, room_number, name, mobile_no, nationality, gender, email, " +
                "checkin_date, checkout_date, bed_type, room_type, price, status " +
                "FROM customer_checkin WHERE status='CHECKED OUT' ORDER BY checkout_date DESC"
            );

            customer_table.setModel(DbUtils.resultSetToTableModel(rs));
            rs.close();

        } catch (SQLException ex) {
            System.out.println("Errors: " + ex.getMessage());
            showCustomDialog("Error loading data: " + ex.getMessage(), "ERROR");
        }
    }

    public void searchTable() {
        try {
            String date = search.getText().trim();
            
            if (date.isEmpty()) {
                displayData();
                return;
            }

            DataBaseCon dbc = new DataBaseCon();
            String query = "SELECT id, room_number, name, mobile_no, nationality, gender, email, " +
                           "checkin_date, checkout_date, bed_type, room_type, price, status " +
                           "FROM customer_checkin WHERE checkout_date = '" + date + "' AND status='CHECKED OUT' " +
                           "ORDER BY checkout_date DESC";

            ResultSet rs = dbc.getData(query);
            
            DefaultTableModel model = (DefaultTableModel) DbUtils.resultSetToTableModel(rs);
            customer_table.setModel(model);
            
            if (model.getRowCount() == 0) {
                showCustomDialog("No records found for date: " + date, "INFO");
            }

        } catch (Exception e) {
            showCustomDialog(e.getMessage(), "ERROR");
        }
    }
    
    private void showCustomDialog(String message, String title) {
        JDialog dialog = new JDialog(this, "", true);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0));
        
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(25, 25, 25, 240));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                g2.setColor(PRIMARY_GOLD);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 23, 23);
                
                g2.dispose();
            }
        };
        panel.setLayout(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        panel.setOpaque(false);
        
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int size = 50;
                int x = (getWidth() - size) / 2;
                int y = 5;
                
                if (title.equals("SUCCESS") || title.equals("INFO")) {
                    g2.setColor(PRIMARY_GOLD);
                    g2.fillOval(x, y, size, size);
                    
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Arial", Font.BOLD, 36));
                    g2.drawString(title.equals("SUCCESS") ? "✓" : "i", x + 18, y + 40);
                } else {
                    g2.setColor(PRIMARY_GOLD);
                    g2.fillOval(x, y, size, size);
                    
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Arial", Font.BOLD, 36));
                    g2.drawString("!", x + 20, y + 40);
                }
                
                g2.dispose();
            }
        };
        iconPanel.setPreferredSize(new Dimension(80, 60));
        iconPanel.setOpaque(false);
        panel.add(iconPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Constantia", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY_GOLD);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        String formattedMessage = "<html><div style='text-align: center; width: 250px;'>"
                + "<span style='color: #FFFFFF; font-family: Constantia; font-size: 13px;'>" + message + "</span>"
                + "</div></html>";
        
        JLabel messageLabel = new JLabel(formattedMessage);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(messageLabel);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Constantia", Font.BOLD, 14));
        okButton.setForeground(Color.WHITE);
        okButton.setBackground(MEDIUM_PURPLE);
        okButton.setFocusPainted(false);
        okButton.setBorderPainted(false);
        okButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        okButton.setPreferredSize(new Dimension(100, 35));
        
        okButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                okButton.setBackground(LIGHT_PURPLE);
                okButton.setForeground(PRIMARY_GOLD);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                okButton.setBackground(MEDIUM_PURPLE);
                okButton.setForeground(Color.WHITE);
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(okButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        
        okButton.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        customer_table = new javax.swing.JTable();
        refresh = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        search = new javax.swing.JTextField();
        search_button = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();

        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(104, 0, 104));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));
        jPanel2.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 0));
        jLabel1.setText("CUSTOMER DETAIL BILLS");
        jPanel2.add(jLabel1);
        jLabel1.setBounds(20, 10, 300, 40);

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 760, 60));

        customer_table.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        customer_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        customer_table.setFocusable(false);
        customer_table.setIntercellSpacing(new java.awt.Dimension(0, 0));
        customer_table.setOpaque(false);
        customer_table.setRowHeight(20);
        customer_table.setSelectionBackground(new java.awt.Color(153, 153, 255));
        customer_table.getTableHeader().setReorderingAllowed(false);
        customer_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                customer_tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(customer_table);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 740, 300));

        refresh.setBackground(new java.awt.Color(140, 104, 141));
        refresh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        refresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                refreshMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                refreshMouseExited(evt);
            }
        });
        refresh.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 0));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("REFRESH");
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
        });
        refresh.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 30));

        jPanel1.add(refresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 80, 30));

        search.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        search.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        search.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        search.setOpaque(false);
        search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchKeyPressed(evt);
            }
        });
        jPanel1.add(search, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 80, 190, 30));

        search_button.setBackground(new java.awt.Color(140, 104, 141));
        search_button.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        search_button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                search_buttonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                search_buttonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                search_buttonMouseExited(evt);
            }
        });
        search_button.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("SEARCH");
        search_button.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 30));

        jPanel1.add(search_button, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 80, 80, 30));

        jLabel7.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 0));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("SEARCH BY CHECK OUT DATE");
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 80, 220, 30));

        jPanel3.setBackground(new java.awt.Color(140, 104, 141));

        jLabel11.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 0));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("BACK");
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 80, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

        private void refreshMouseEntered(java.awt.event.MouseEvent evt) {                                     
        // Handled in modern design
    }                                    

    private void refreshMouseExited(java.awt.event.MouseEvent evt) {                                    
        // Handled in modern design
    }                                   

    private void search_buttonMouseEntered(java.awt.event.MouseEvent evt) {                                           
        // Handled in modern design
    }                                          

    private void search_buttonMouseExited(java.awt.event.MouseEvent evt) {                                          
        // Handled in modern design
    }                                         

    private void refreshMouseClicked(java.awt.event.MouseEvent evt) {                                     
        displayData();
    }                                    

    private void searchKeyPressed(java.awt.event.KeyEvent evt) {                                  
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            searchTable();
        }
    }                                 

    private void search_buttonMouseClicked(java.awt.event.MouseEvent evt) {                                           
        searchTable();
    }                                          

  private void customer_tableMouseClicked(java.awt.event.MouseEvent evt) {                                            
    // Only respond to double clicks
    if (evt.getClickCount() == 2) {
        int row = customer_table.getSelectedRow();
        if (row < 0) return;
        
        // Convert view row to model row (important if table is sorted)
        row = customer_table.convertRowIndexToModel(row);
        
        try {
            // Get all data from the selected row
            String id = customer_table.getValueAt(row, 0).toString();
            String room = customer_table.getValueAt(row, 1).toString();
            String name = customer_table.getValueAt(row, 2).toString();
            String mobile = customer_table.getValueAt(row, 3).toString();
            String nationality = customer_table.getValueAt(row, 4).toString();
            String gender = customer_table.getValueAt(row, 5).toString();
            String email = customer_table.getValueAt(row, 6).toString();
            String checkin = customer_table.getValueAt(row, 7).toString();
            String checkout = customer_table.getValueAt(row, 8).toString();
            String bed = customer_table.getValueAt(row, 9).toString();
            String roomType = customer_table.getValueAt(row, 10).toString();
            String price = customer_table.getValueAt(row, 11).toString();

            // Create detailed receipt
            String receipt = 
                "╔════════════════════════════════════════╗\n" +
                "║           HOTEL RECEIPT               ║\n" +
                "╠════════════════════════════════════════╣\n" +
                "║ Customer ID: " + padRight(id, 24) + " ║\n" +
                "║ Name: " + padRight(name, 28) + " ║\n" +
                "║ Mobile: " + padRight(mobile, 26) + " ║\n" +
                "║ Email: " + padRight(email, 27) + " ║\n" +
                "║ Nationality: " + padRight(nationality, 21) + " ║\n" +
                "║ Gender: " + padRight(gender, 26) + " ║\n" +
                "╠════════════════════════════════════════╣\n" +
                "║ Room Number: " + padRight(room, 22) + " ║\n" +
                "║ Room Type: " + padRight(roomType, 23) + " ║\n" +
                "║ Bed Type: " + padRight(bed, 24) + " ║\n" +
                "╠════════════════════════════════════════╣\n" +
                "║ Check-in: " + padRight(checkin, 24) + " ║\n" +
                "║ Check-out: " + padRight(checkout, 23) + " ║\n" +
                "╠════════════════════════════════════════╣\n" +
                "║ Total Price: ₱" + padRight(price, 21) + " ║\n" +
                "╚════════════════════════════════════════╝\n" +
                "     Thank you for staying with us!";

            JTextArea textArea = new JTextArea(receipt);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            textArea.setEditable(false);
            textArea.setBackground(new Color(245, 245, 245));
            textArea.setForeground(Color.BLACK);
            textArea.setMargin(new Insets(10, 10, 10, 10));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 500));
            scrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY_GOLD, 2));

            // Create custom panel for dialog
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(new Color(45, 45, 45));
            panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            JLabel titleLabel = new JLabel("Customer Receipt");
            titleLabel.setFont(new Font("Constantia", Font.BOLD, 18));
            titleLabel.setForeground(PRIMARY_GOLD);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(titleLabel, BorderLayout.NORTH);
            
            panel.add(scrollPane, BorderLayout.CENTER);
            
            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.setBackground(new Color(45, 45, 45));
            
            JButton printBtn = new JButton("PRINT");
            printBtn.setFont(new Font("Constantia", Font.BOLD, 14));
            printBtn.setForeground(Color.WHITE);
            printBtn.setBackground(MEDIUM_PURPLE);
            printBtn.setFocusPainted(false);
            printBtn.setBorderPainted(false);
            printBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            printBtn.setPreferredSize(new Dimension(100, 35));
            
            printBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    printBtn.setBackground(LIGHT_PURPLE);
                    printBtn.setForeground(PRIMARY_GOLD);
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    printBtn.setBackground(MEDIUM_PURPLE);
                    printBtn.setForeground(Color.WHITE);
                }
            });
            
            JButton closeBtn = new JButton("CLOSE");
            closeBtn.setFont(new Font("Constantia", Font.BOLD, 14));
            closeBtn.setForeground(Color.WHITE);
            closeBtn.setBackground(new Color(100, 100, 100));
            closeBtn.setFocusPainted(false);
            closeBtn.setBorderPainted(false);
            closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            closeBtn.setPreferredSize(new Dimension(100, 35));
            
            closeBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    closeBtn.setBackground(new Color(120, 120, 120));
                    closeBtn.setForeground(PRIMARY_GOLD);
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    closeBtn.setBackground(new Color(100, 100, 100));
                    closeBtn.setForeground(Color.WHITE);
                }
            });
            
            buttonPanel.add(printBtn);
            buttonPanel.add(closeBtn);
            panel.add(buttonPanel, BorderLayout.SOUTH);

            // Create dialog
            JDialog dialog = new JDialog(this, "Receipt Details", true);
            dialog.setUndecorated(true);
            dialog.getContentPane().add(panel);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            
            // Add border to dialog
            dialog.getRootPane().setBorder(BorderFactory.createLineBorder(PRIMARY_GOLD, 2));

            printBtn.addActionListener(e -> {
                try {
                    textArea.print();
                } catch (Exception ex) {
                    showCustomDialog("Error printing: " + ex.getMessage(), "ERROR");
                }
            });
            
            closeBtn.addActionListener(e -> dialog.dispose());

            dialog.setVisible(true);

        } catch (Exception e) {
            showCustomDialog("Error: " + e.getMessage(), "ERROR");
        }
    }
}

// Helper method to pad strings for receipt formatting
private String padRight(String s, int n) {
    if (s.length() > n) {
        return s.substring(0, n - 3) + "...";
    }
    return String.format("%-" + n + "s", s);
}                            

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {                                     
        displayData();
    }                                    

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {                                      
        displayData(); 
    }                                     

    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {                                      
        dispose();
        new AdminDashboard().setVisible(true);
    }                  


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable customer_table;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel refresh;
    private javax.swing.JTextField search;
    private javax.swing.JPanel search_button;
    // End of variables declaration//GEN-END:variables
}
