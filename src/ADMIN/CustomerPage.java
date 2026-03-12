package ADMIN;

import config.DataBaseCon;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import net.proteanit.sql.DbUtils;

public class CustomerPage extends JFrame {
    
    // Custom colors for hotel theme
    private static final Color PRIMARY_GOLD = new Color(255, 215, 0);
    private static final Color SECONDARY_GOLD = new Color(218, 165, 32);
    private static final Color DARK_PURPLE = new Color(75, 0, 130);
    private static final Color MEDIUM_PURPLE = new Color(128, 0, 128);
    private static final Color LIGHT_PURPLE = new Color(186, 85, 211);
    private static final Color PANEL_DARK = new Color(30, 30, 30);
    private static final Color PANEL_LIGHT = new Color(45, 45, 45);
    private JPanel contentPanel; // Add this line
    
    // Custom panel button with glow and hover effects
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
            setHorizontalAlignment(JTextField.CENTER);
            
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

    public CustomerPage() {
    super.setUndecorated(true);
    setBackground(new Color(0,0,0,0)); // Add this line
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
        
        
        // Create main panel with transparent background
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBounds(0, 0, 1100, 650);
        mainPanel.setOpaque(false);
        
        
        // Create content panel
        contentPanel = new JPanel() {
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
        contentPanel.setBounds(20, 20, 1060, 610);
        contentPanel.setOpaque(false);
        
        // Title
        JLabel titleLabel = new JLabel("CUSTOMER MANAGEMENT") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                Font font = new Font("Constantia", Font.BOLD, 32);
                g2.setFont(font);
                FontMetrics fm = g2.getFontMetrics();
                
                String text = "CUSTOMER MANAGEMENT";
                int x = 30;
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
        titleLabel.setBounds(20, 15, 400, 50);
        contentPanel.add(titleLabel);
        
        // Close button - ADD THIS HERE
    CloseButton closeBtn = new CloseButton();
    closeBtn.setBounds(1010, 18, 40, 30);
    contentPanel.add(closeBtn);

        // Control panel for buttons and search
        JPanel controlPanel = new JPanel(null);
        controlPanel.setBounds(30, 70, 1000, 45);
        controlPanel.setOpaque(false);
        
        // ADD button
        ModernPanelButton addBtn = new ModernPanelButton("ADD", MEDIUM_PURPLE, () -> {
            CustomerForm ctf = new CustomerForm();
            ctf.setVisible(true);
            ctf.action = "Add";
            ctf.st_label.setText("SAVE");
            ctf.browse.setVisible(false);
        });
        addBtn.setBounds(0, 5, 80, 35);
        controlPanel.add(addBtn);
        
        // UPDATE button
        ModernPanelButton updateBtn = new ModernPanelButton("UPDATE", DARK_PURPLE, () -> {
            int viewRow = customer_table.getSelectedRow();
            if (viewRow < 0) {
                showCustomDialog("Please select a customer to update!", "SELECTION REQUIRED");
                return;
            }

            int modelRow = customer_table.convertRowIndexToModel(viewRow);
            TableModel model = customer_table.getModel();
            String id = model.getValueAt(modelRow, 0).toString();

            CustomerForm stf = new CustomerForm();
            stf.setVisible(true);

            try {
                DataBaseCon dbc = new DataBaseCon();
                ResultSet rs = dbc.getData("SELECT * FROM tbl_users WHERE id = " + id);

                if (rs.next()) {
                    stf.st_id.setText(rs.getString("id"));
                    stf.st_fname.setText(rs.getString("first_name"));
                    stf.st_lname.setText(rs.getString("last_name"));
                    stf.st_email.setText(rs.getString("email"));
                    stf.st_status1.setSelectedItem(rs.getString("type"));
                    stf.st_status.setSelectedItem(rs.getString("status"));

                    try {
                        byte[] imgBytes = rs.getBytes("idpicture");
                        if (imgBytes != null && imgBytes.length > 0) {
                            stf.image.setIcon(stf.ResizeImage(null, imgBytes, stf.image));
                        }
                    } catch (Exception e) {
                        stf.image.setIcon(null);
                    }

                    stf.action = "Update";
                    stf.st_label.setText("UPDATE");
                }
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        updateBtn.setBounds(90, 5, 90, 35);
        controlPanel.add(updateBtn);
        
        // DELETE button
        ModernPanelButton deleteBtn = new ModernPanelButton("DELETE", new Color(139, 0, 0), () -> {
            int viewRow = customer_table.getSelectedRow();
            if (viewRow < 0) {
                showCustomDialog("Please select a customer to delete!", "SELECTION REQUIRED");
                return;
            }

            int modelRow = customer_table.convertRowIndexToModel(viewRow);
            TableModel model = customer_table.getModel();
            int id = Integer.parseInt(model.getValueAt(modelRow, 0).toString());

            int confirm = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete ID: " + id + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = DataBaseCon.deleteData("tbl_users", "id", id);
                if (success) {
                    displayData();
                    showCustomDialog("Record Deleted Successfully!", "SUCCESS");
                } else {
                    showCustomDialog("Failed to delete record!", "ERROR");
                }
            }
        });
        deleteBtn.setBounds(190, 5, 90, 35);
        controlPanel.add(deleteBtn);
        
        // REFRESH button
        ModernPanelButton refreshBtn = new ModernPanelButton("REFRESH", new Color(100, 100, 100), () -> {
            displayData();
        });
        refreshBtn.setBounds(290, 5, 90, 35);
        controlPanel.add(refreshBtn);
        
        // Search label
        JLabel searchLabel = new JLabel("SEARCH:");
        searchLabel.setFont(new Font("Constantia", Font.BOLD, 14));
        searchLabel.setForeground(PRIMARY_GOLD);
        searchLabel.setBounds(600, 10, 70, 25);
        controlPanel.add(searchLabel);
        
        // Search field
        search = new ModernTextField();
        search.setBounds(670, 5, 180, 35);
        search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchTable();
            }
        });
        controlPanel.add(search);
        
        // SEARCH button
        ModernPanelButton searchBtn = new ModernPanelButton("GO", LIGHT_PURPLE, () -> {
            searchTable();
        });
        searchBtn.setBounds(860, 5, 60, 35);
        controlPanel.add(searchBtn);
        
        contentPanel.add(controlPanel);
        
        // Setup table
        setupTable();
        
        jScrollPane1.setBounds(30, 130, 1000, 430);
        jScrollPane1.setOpaque(false);
        jScrollPane1.getViewport().setBackground(new Color(50, 50, 50));
        jScrollPane1.setBorder(BorderFactory.createCompoundBorder(
            new RoundedCornerBorder(15),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));
        contentPanel.add(jScrollPane1);
        
        // Status label
        JLabel statusLabel = new JLabel("Total Customers: " + customer_table.getRowCount());
        statusLabel.setFont(new Font("Constantia", Font.ITALIC, 12));
        statusLabel.setForeground(new Color(180, 180, 180));
        statusLabel.setBounds(30, 570, 300, 20);
        contentPanel.add(statusLabel);
        
        mainPanel.add(contentPanel);
        getContentPane().add(mainPanel);
        
        // Update references
        add = addBtn;
        edit = updateBtn;
        delete = deleteBtn;
        refresh = refreshBtn;
        search_button = searchBtn;
        
        revalidate();
        repaint();
    }
    // Custom close button
// Custom close button
private class CloseButton extends JLabel {
    private boolean isHovered = false;
    
    public CloseButton() {
        super("X");
        setFont(new Font("Arial Black", Font.BOLD, 24));
        setForeground(Color.WHITE);
        setHorizontalAlignment(SwingConstants.CENTER);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                setForeground(new Color(255, 80, 80));
                setFont(getFont().deriveFont(Font.BOLD, 30f));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                setForeground(Color.WHITE);
                setFont(getFont().deriveFont(Font.BOLD, 24f));
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose(); // Just close this window, don't open AdminDashboard
                // Remove this line: new AdminDashboard().setVisible(true);
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        if (isHovered) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 0, 0, 60));
            g2.fillOval(getWidth()/2 - 15, getHeight()/2 - 15, 30, 30);
            g2.dispose();
        }
        super.paintComponent(g);
    }
}
    
    private void setupTable() {
        // Table appearance
        customer_table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        customer_table.setForeground(Color.WHITE);
        customer_table.setBackground(new Color(50, 50, 50));
        customer_table.setSelectionBackground(MEDIUM_PURPLE);
        customer_table.setSelectionForeground(PRIMARY_GOLD);
        customer_table.setRowHeight(32);
        customer_table.setGridColor(new Color(80, 80, 80));
        customer_table.setShowGrid(true);
        customer_table.setIntercellSpacing(new Dimension(0, 0));
        
        // Make table non-editable
        customer_table.setDefaultEditor(Object.class, null);
        
        // Style the table header
        JTableHeader header = customer_table.getTableHeader();
        header.setFont(new Font("Constantia", Font.BOLD, 13));
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
            "SELECT id, first_name, last_name, email, type, status FROM tbl_users ORDER BY id"
        );

        customer_table.setModel(DbUtils.resultSetToTableModel(rs));
        rs.close();

    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

    public void searchTable() {
        DefaultTableModel model = (DefaultTableModel) customer_table.getModel();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(model);
        customer_table.setRowSorter(tr);

        String text = search.getText().trim();
        if (text.length() == 0) {
            tr.setRowFilter(null);
        } else {
            tr.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text)));
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
                
                if (title.equals("SUCCESS")) {
                    g2.setColor(PRIMARY_GOLD);
                    g2.fillOval(x, y, size, size);
                    
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(4));
                    g2.drawLine(x + 12, y + 25, x + 22, y + 35);
                    g2.drawLine(x + 22, y + 35, x + 38, y + 15);
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
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        customer_table = new javax.swing.JTable();
        add = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        edit = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        delete = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        refresh = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        search = new javax.swing.JTextField();
        search_button = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(104, 0, 104));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));
        jPanel2.setLayout(null);

        jPanel3.setBackground(new java.awt.Color(104, 0, 104));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel2.add(jPanel3);
        jPanel3.setBounds(500, 0, 180, 60);

        jLabel1.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 0));
        jLabel1.setText("CUSTOMER PAGE");
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

        add.setBackground(new java.awt.Color(140, 104, 141));
        add.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        add.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addMouseExited(evt);
            }
        });
        add.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("ADD");
        add.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 60, 30));

        jPanel1.add(add, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 80, 30));

        edit.setBackground(new java.awt.Color(140, 104, 141));
        edit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        edit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                editMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                editMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                editMouseExited(evt);
            }
        });
        edit.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 0));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("UPDATE");
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });
        edit.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 60, 30));

        jPanel1.add(edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, 80, 30));

        delete.setBackground(new java.awt.Color(140, 104, 141));
        delete.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        delete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                deleteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                deleteMouseExited(evt);
            }
        });
        delete.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 0));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("DELETE");
        delete.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 60, 30));

        jPanel1.add(delete, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 80, 80, 30));

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

        jLabel7.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 0));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("REFRESH");
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });
        refresh.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 30));

        jPanel1.add(refresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 80, 80, 30));

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

    private void addMouseClicked(java.awt.event.MouseEvent evt) {                                 
        // Handled in modern design
    }                                

    private void addMouseEntered(java.awt.event.MouseEvent evt) {                                 
        // Handled in modern design
    }                                

    private void addMouseExited(java.awt.event.MouseEvent evt) {                                
        // Handled in modern design
    }                               

    private void deleteMouseEntered(java.awt.event.MouseEvent evt) {                                    
       // Handled in modern design
    }                                   

    private void deleteMouseExited(java.awt.event.MouseEvent evt) {                                   
        // Handled in modern design
    }                                  

    private void refreshMouseClicked(java.awt.event.MouseEvent evt) {                                     
       displayData();
    }                                    

    private void editMouseClicked(java.awt.event.MouseEvent evt) {                                  
        // Handled in modern design
    }                                 

    private void editMouseEntered(java.awt.event.MouseEvent evt) {                                  
        // Handled in modern design
    }                                 

    private void editMouseExited(java.awt.event.MouseEvent evt) {                                 
        // Handled in modern design
    }                                

    private void deleteMouseClicked(java.awt.event.MouseEvent evt) {                                    
        // Handled in modern design
    }                                   

    private void searchKeyPressed(java.awt.event.KeyEvent evt) {                                  
        searchTable();
    }                                 

    private void search_buttonMouseClicked(java.awt.event.MouseEvent evt) {                                           
        searchTable();
    }                                          

    private void customer_tableMouseClicked(java.awt.event.MouseEvent evt) {                                            
        
    }                                           

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {                                     

    }                                    

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {                                     
        displayData();
    }                                    


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel add;
    private javax.swing.JTable customer_table;
    private javax.swing.JPanel delete;
    private javax.swing.JPanel edit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel refresh;
    private javax.swing.JTextField search;
    private javax.swing.JPanel search_button;
    // End of variables declaration//GEN-END:variables
}
