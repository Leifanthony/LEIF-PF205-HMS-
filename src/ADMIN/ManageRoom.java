package ADMIN;

import config.DataBaseCon;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;

public class ManageRoom extends javax.swing.JFrame {
    
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
                    dispose();
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
    
    // Custom label with glow effect
    private class GlowLabel extends JLabel {
        public GlowLabel(String text) {
            super(text);
            setFont(new Font("Constantia", Font.BOLD, 16));
            setForeground(PRIMARY_GOLD);
        }
    }
    
    // Custom combo box
    private class ModernComboBox extends JComboBox<String> {
        public ModernComboBox(String[] items) {
            super(items);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setBackground(new Color(60, 60, 60));
            setForeground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    setBackground(isSelected ? MEDIUM_PURPLE : new Color(60, 60, 60));
                    setForeground(Color.WHITE);
                    setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    return this;
                }
            });
        }
    }
    
    // Custom text field
    private class ModernTextField extends JTextField {
        public ModernTextField() {
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setForeground(Color.WHITE);
            setCaretColor(PRIMARY_GOLD);
            setBackground(new Color(60, 60, 60));
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
        }
    }

    public ManageRoom() {
        initComponents();
        applyModernDesign();
        loadRooms();
        setVisible(true);
    }
private void applyModernDesign() {
    // Clear content pane
    getContentPane().removeAll();
    getContentPane().setLayout(null);
    
    // Set frame size
    setSize(1000, 600);
    setLocationRelativeTo(null);
    
    // Make background completely transparent
    setBackground(new Color(0, 0, 0, 0));
    
    // Create main panel with transparent background
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(null);
    mainPanel.setBounds(0, 0, 1000, 600);
    mainPanel.setOpaque(false);
    mainPanel.setBackground(new Color(0, 0, 0, 0));
    
    // Create left panel for table
    JPanel tablePanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(40, 40, 40),
                getWidth(), getHeight(), new Color(60, 60, 60)
            );
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            
            g2.setColor(new Color(255, 215, 0, 100));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 18, 18);
            
            g2.dispose();
        }
    };
    tablePanel.setLayout(null);
    tablePanel.setBounds(20, 30, 580, 540);
    tablePanel.setOpaque(false);
    
    // Title inside left panel above the table
    JLabel tableTitle = new JLabel("MANAGE ROOMS") {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            Font font = new Font("Constantia", Font.BOLD, 28);
            g2.setFont(font);
            FontMetrics fm = g2.getFontMetrics();
            
            String text = "MANAGE ROOMS";
            int x = 20;
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
    tableTitle.setBounds(20, 15, 400, 40);
    tablePanel.add(tableTitle);
    
    // Enhance the table
    jTable1.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    jTable1.setForeground(Color.WHITE);
    jTable1.setBackground(new Color(50, 50, 50));
    jTable1.setSelectionBackground(MEDIUM_PURPLE);
    jTable1.setSelectionForeground(PRIMARY_GOLD);
    jTable1.setRowHeight(30);
    jTable1.setGridColor(new Color(80, 80, 80));
    jTable1.setShowGrid(true);
    
    // Style the table header
    JTableHeader header = jTable1.getTableHeader();
    header.setFont(new Font("Constantia", Font.BOLD, 14));
    header.setBackground(new Color(40, 40, 40));
    header.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
    
    // Center align table cells
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    centerRenderer.setBackground(new Color(50, 50, 50));
    centerRenderer.setForeground(Color.WHITE);
    for (int i = 0; i < jTable1.getColumnCount(); i++) {
        jTable1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
    
    jScrollPane1.setBounds(20, 70, 540, 450);
    jScrollPane1.setOpaque(false);
    jScrollPane1.getViewport().setBackground(new Color(50, 50, 50));
    jScrollPane1.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
    tablePanel.add(jScrollPane1);
    
    mainPanel.add(tablePanel);
    
    // Create right panel for form
    JPanel formPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(40, 40, 40),
                getWidth(), getHeight(), new Color(60, 60, 60)
            );
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            
            g2.setColor(new Color(255, 215, 0, 100));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 18, 18);
            
            g2.dispose();
        }
    };
    formPanel.setLayout(null);
    formPanel.setBounds(620, 30, 360, 540);
    formPanel.setOpaque(false);
    
    // Form title
    JLabel formTitle = new JLabel("ADD NEW ROOM");
    formTitle.setFont(new Font("Constantia", Font.BOLD, 22));
    formTitle.setForeground(PRIMARY_GOLD);
    formTitle.setBounds(80, 30, 200, 30);
    formPanel.add(formTitle);
    
    // Room Type
    GlowLabel roomTypeLabel = new GlowLabel("ROOM TYPE");
    roomTypeLabel.setBounds(40, 90, 150, 25);
    formPanel.add(roomTypeLabel);
    
    ModernComboBox roomTypeCombo = new ModernComboBox(new String[]{"AC", "NON-AC"});
    roomTypeCombo.setBounds(40, 120, 280, 35);
    formPanel.add(roomTypeCombo);
    
    // Bed Type
    GlowLabel bedLabel = new GlowLabel("BED TYPE");
    bedLabel.setBounds(40, 170, 150, 25);
    formPanel.add(bedLabel);
    
    ModernComboBox bedCombo = new ModernComboBox(new String[]{"Single", "Double", "Suite"});
    bedCombo.setBounds(40, 200, 280, 35);
    formPanel.add(bedCombo);
    
    // Price
    GlowLabel priceLabel = new GlowLabel("PRICE");
    priceLabel.setBounds(40, 250, 150, 25);
    formPanel.add(priceLabel);
    
    ModernTextField priceField = new ModernTextField();
    priceField.setBounds(40, 280, 280, 35);
    formPanel.add(priceField);
    
    // Buttons
    ModernButton addBtn = new ModernButton("ADD ROOM");
    addBtn.setBounds(40, 360, 130, 40);
    addBtn.addActionListener(e -> {
        int roomNumber = getNextRoomNumber();
        String roomType = roomTypeCombo.getSelectedItem().toString();
        String bed = bedCombo.getSelectedItem().toString();
        String priceText = priceField.getText().trim();
        String status = "Available";

        if (priceText.isEmpty()) {
            showCustomDialog("Please fill all fields!", "VALIDATION ERROR");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            Connection con = DataBaseCon.connectDB();

            String sql = "INSERT INTO rooms (room_number, room_type, bed_type, price, status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setInt(1, roomNumber);
            pst.setString(2, roomType);
            pst.setString(3, bed);
            pst.setDouble(4, price);
            pst.setString(5, status);

            pst.executeUpdate();

            showCustomDialog("Room " + roomNumber + " Added Successfully!", "SUCCESS");

            priceField.setText("");
            roomTypeCombo.setSelectedIndex(0);
            bedCombo.setSelectedIndex(0);

            loadRooms();

            pst.close();
            con.close();

        } catch (NumberFormatException ex) {
            showCustomDialog("Price must be a number!", "ERROR");
        } catch (Exception ex) {
            showCustomDialog("Error: " + ex.getMessage(), "ERROR");
        }
    });
    formPanel.add(addBtn);
    
    ModernButton backBtn = new ModernButton("BACK");
    backBtn.setBounds(190, 360, 130, 40);
    backBtn.addActionListener(e -> {
        dispose();
        new AdminDashboard().setVisible(true);
    });
    formPanel.add(backBtn);
    
    // Decorative line
    JLabel decorLine = new JLabel();
    decorLine.setBounds(40, 440, 280, 2);
    decorLine.setOpaque(true);
    decorLine.setBackground(new Color(255, 215, 0, 100));
    formPanel.add(decorLine);
    
    mainPanel.add(formPanel);
    
    // Add corner decorations    
    getContentPane().add(mainPanel);
    
    // Update references
    jComboBox1 = roomTypeCombo;
    jComboBox2 = bedCombo;
    jTextField2 = priceField;
    
    revalidate();
    repaint();
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(102, 0, 102));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Room Number", "Room Type", "Bed Type", "Price", "Status"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 450, 410));

        jLabel2.setFont(new java.awt.Font("Constantia", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 0));
        jLabel2.setText("Room Type");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 80, -1, -1));

        jComboBox1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AC", "NON-AC" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 120, 240, 30));

        jLabel3.setFont(new java.awt.Font("Constantia", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 0));
        jLabel3.setText("Bed");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 180, 140, 30));

        jComboBox2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Single", "Double", "Suite" }));
        jPanel1.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 230, 240, 30));

        jLabel4.setFont(new java.awt.Font("Constantia", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 0));
        jLabel4.setText("Price");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 290, 150, 30));

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 340, 240, 30));

        jButton1.setText("Add Room");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 410, 110, 40));

        jLabel5.setFont(new java.awt.Font("Constantia", 1, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("MANAGE ROOM");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 740, 70));

        jButton2.setText("Back");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 410, 110, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 513, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        this.dispose ();
        AdminDashboard lp = new AdminDashboard();
        lp.setVisible(true);
    }//GEN-LAST:event_jButton2MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       int roomNumber = getNextRoomNumber();   // ✅ AUTO GENERATED
    String roomType = jComboBox1.getSelectedItem().toString();
    String bed = jComboBox2.getSelectedItem().toString();
    String priceText = jTextField2.getText().trim();
    String status = "Available";

    if (priceText.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill all fields!");
        return;
    }

    try {
        double price = Double.parseDouble(priceText);
        Connection con = DataBaseCon.connectDB();

        String sql = "INSERT INTO rooms (room_number, room_type, bed_type, price, status) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(sql);

        pst.setInt(1, roomNumber);   
        pst.setString(2, roomType);
        pst.setString(3, bed);
        pst.setDouble(4, price);
        pst.setString(5, status);

        pst.executeUpdate();

        JOptionPane.showMessageDialog(this, "Room " + roomNumber + " Added Successfully!");

        jTextField2.setText("");
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);

        loadRooms();

        pst.close();
        con.close();

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Price must be a number!");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
    
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ManageRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManageRoom().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables

    private void loadRooms() {
    try {
        Connection con = DataBaseCon.connectDB();
        String sql = "SELECT * FROM rooms";
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("room_number"),
                rs.getString("room_type"),
                rs.getString("bed_type"),
                rs.getDouble("price"),
                rs.getString("status")
            });
        }

        rs.close();
        pst.close();
        con.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
}



private int getNextRoomNumber() {
    int nextNumber = 101;

    try {
        Connection con = DataBaseCon.connectDB();
        String sql = "SELECT IFNULL(MAX(room_number), 100) + 1 FROM rooms";
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            nextNumber = rs.getInt(1);
        }

        rs.close();
        pst.close();
        con.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }

    return nextNumber;
}

}  
