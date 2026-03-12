package ADMIN;

import config.DataBaseCon;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import net.proteanit.sql.DbUtils;

public class ManageBooking extends JFrame {
    
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
    
    // Custom panel button (for BACK, REFRESH, SEARCH)
    private class ModernPanelButton extends JPanel {
        private boolean isHovered = false;
        private float glowIntensity = 0f;
        private Timer glowTimer;
        private String text;
        private Runnable onClick;
        
        public ModernPanelButton(String text, Runnable onClick) {
            this.text = text;
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
                0, 0, MEDIUM_PURPLE,
                w, 0, LIGHT_PURPLE
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
                BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
            
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    isFocused = true;
                    startGlowAnimation(true);
                    setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_GOLD, 2),
                        BorderFactory.createEmptyBorder(7, 11, 7, 11)
                    ));
                }
                
                @Override
                public void focusLost(FocusEvent e) {
                    isFocused = false;
                    startGlowAnimation(false);
                    setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
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

    public ManageBooking() {
        initComponents();
        applyModernDesign();
        displayData();
        System.out.println("CustomerPage constructor called");
    }

    public void displayData() {
        try {
            DataBaseCon dbc = new DataBaseCon();

            ResultSet rs = dbc.getData(
                "SELECT id, room_number, name, mobile_no, nationality, gender, email, " +
                "id_proof, address, checkin_date, checkout_date, bed_type, room_type, price, status " +
                "FROM customer_checkin"
            );

            customer_table.setModel(DbUtils.resultSetToTableModel(rs));
            
            // Style the table after loading data
            styleTable();
            
            rs.close();

        } catch (SQLException ex) {
            System.out.println("Errors: " + ex.getMessage());
        }
    }
    
    private void styleTable() {
        // Table appearance
        customer_table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        customer_table.setForeground(Color.WHITE);
        customer_table.setBackground(new Color(50, 50, 50));
        customer_table.setSelectionBackground(MEDIUM_PURPLE);
        customer_table.setSelectionForeground(PRIMARY_GOLD);
        customer_table.setRowHeight(28);
        customer_table.setGridColor(new Color(80, 80, 80));
        customer_table.setShowGrid(true);
        customer_table.setIntercellSpacing(new Dimension(0, 0));
        
        // Style the table header
        JTableHeader header = customer_table.getTableHeader();
        header.setFont(new Font("Constantia", Font.BOLD, 13));
        header.setBackground(new Color(40, 40, 40));
        header.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        
        // Center align all cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBackground(new Color(50, 50, 50));
        centerRenderer.setForeground(Color.WHITE);
        
        for (int i = 0; i < customer_table.getColumnCount(); i++) {
            customer_table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void searchTable() {
        DefaultTableModel model = (DefaultTableModel) customer_table.getModel();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(model);
        customer_table.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(search.getText().trim()));
    }
    
private void applyModernDesign() {
    // Clear content pane
    getContentPane().removeAll();
    getContentPane().setLayout(null);
    
    // Set frame size
    setSize(1200, 700);
    setLocationRelativeTo(null);
    
    // Make background completely transparent
    setBackground(new Color(0, 0, 0, 0));
    
    // Create main panel with transparent background
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(null);
    mainPanel.setBounds(0, 0, 1200, 700);
    mainPanel.setOpaque(false);
    
    // Create table panel (now contains everything)
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
    tablePanel.setBounds(50, 50, 1100, 600);
    tablePanel.setOpaque(false);
    
    // Title inside table panel
    JLabel titleLabel = new JLabel("MANAGE BOOKINGS") {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            Font font = new Font("Constantia", Font.BOLD, 28);
            g2.setFont(font);
            FontMetrics fm = g2.getFontMetrics();
            
            String text = "MANAGE BOOKINGS";
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
    titleLabel.setBounds(20, 15, 400, 40);
    tablePanel.add(titleLabel);
    
    // Control panel inside table panel
    JPanel controlPanel = new JPanel(null);
    controlPanel.setBounds(20, 60, 1060, 45);
    controlPanel.setOpaque(false);
    
    // Back button
    ModernPanelButton backBtn = new ModernPanelButton("BACK", () -> {
        dispose();
        new UserDash1().setVisible(true);
    });
    backBtn.setBounds(0, 5, 100, 35);
    controlPanel.add(backBtn);
    
    // Refresh button
    ModernPanelButton refreshBtn = new ModernPanelButton("REFRESH", () -> {
        displayData();
    });
    refreshBtn.setBounds(110, 5, 100, 35);
    controlPanel.add(refreshBtn);
    
    // Search label
    JLabel searchLabel = new JLabel("SEARCH:");
    searchLabel.setFont(new Font("Constantia", Font.BOLD, 14));
    searchLabel.setForeground(PRIMARY_GOLD);
    searchLabel.setBounds(720, 10, 70, 25);
    controlPanel.add(searchLabel);
    
    // Search field
    search = new ModernTextField();
    search.setBounds(790, 5, 180, 35);
    search.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            searchTable();
        }
    });
    controlPanel.add(search);
    
    // Search button
    ModernPanelButton searchBtn = new ModernPanelButton("GO", () -> {
        searchTable();
    });
    searchBtn.setBounds(980, 5, 70, 35);
    controlPanel.add(searchBtn);
    
    tablePanel.add(controlPanel);
    
    // Setup scroll pane and table
    jScrollPane1.setBounds(20, 115, 1060, 465);
    jScrollPane1.setOpaque(false);
    jScrollPane1.getViewport().setBackground(new Color(50, 50, 50));
    jScrollPane1.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
    
    // Style the table
    styleTable();
    
    tablePanel.add(jScrollPane1);
    mainPanel.add(tablePanel);
    
    getContentPane().add(mainPanel);
    
    // Update references
    edit = backBtn;
    refresh = refreshBtn;
    search_button = searchBtn;
    
    revalidate();
    repaint();
}
    
    private void addCornerDecorations(JPanel panel) {
        int cornerSize = 30;
        int offset = 15;
        
        // Top-left corner
        JLabel tl1 = new JLabel();
        tl1.setBounds(offset, 80, cornerSize, 2);
        tl1.setOpaque(true);
        tl1.setBackground(new Color(255, 215, 0, 150));
        panel.add(tl1);
        
        JLabel tl2 = new JLabel();
        tl2.setBounds(offset, 80, 2, cornerSize);
        tl2.setOpaque(true);
        tl2.setBackground(new Color(255, 215, 0, 150));
        panel.add(tl2);
        
        // Top-right corner
        JLabel tr1 = new JLabel();
        tr1.setBounds(1200 - offset - cornerSize, 80, cornerSize, 2);
        tr1.setOpaque(true);
        tr1.setBackground(new Color(255, 215, 0, 150));
        panel.add(tr1);
        
        JLabel tr2 = new JLabel();
        tr2.setBounds(1200 - offset - 1, 80, 2, cornerSize);
        tr2.setOpaque(true);
        tr2.setBackground(new Color(255, 215, 0, 150));
        panel.add(tr2);
        
        // Bottom-left corner
        JLabel bl1 = new JLabel();
        bl1.setBounds(offset, 700 - offset - cornerSize, cornerSize, 2);
        bl1.setOpaque(true);
        bl1.setBackground(new Color(255, 215, 0, 150));
        panel.add(bl1);
        
        JLabel bl2 = new JLabel();
        bl2.setBounds(offset, 700 - offset - cornerSize, 2, cornerSize);
        bl2.setOpaque(true);
        bl2.setBackground(new Color(255, 215, 0, 150));
        panel.add(bl2);
        
        // Bottom-right corner
        JLabel br1 = new JLabel();
        br1.setBounds(1200 - offset - cornerSize, 700 - offset - cornerSize, cornerSize, 2);
        br1.setOpaque(true);
        br1.setBackground(new Color(255, 215, 0, 150));
        panel.add(br1);
        
        JLabel br2 = new JLabel();
        br2.setBounds(1200 - offset - 1, 700 - offset - cornerSize, 2, cornerSize);
        br2.setOpaque(true);
        br2.setBackground(new Color(255, 215, 0, 150));
        panel.add(br2);
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
        edit = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        refresh = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        search = new javax.swing.JTextField();
        search_button = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();

        setUndecorated(true);

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
        jLabel1.setText("MANAGE BOOKING");
        jPanel2.add(jLabel1);
        jLabel1.setBounds(20, 10, 300, 40);

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 820, 60));

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

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 800, 300));

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
        jLabel8.setText("BACK");
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });
        edit.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 150, 30));

        jPanel1.add(edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, 150, 30));

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
        refresh.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 150, 30));

        jPanel1.add(refresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, 150, 30));

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
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void editMouseClicked(java.awt.event.MouseEvent evt) {                                  
        int rowIndex = customer_table.getSelectedRow();

        if (rowIndex < 0) {
            showCustomDialog("Please select a booking first!", "SELECTION REQUIRED");
            return;
        }

        // Convert view row to model row (important if table is sorted)
        rowIndex = customer_table.convertRowIndexToModel(rowIndex);

        String id = customer_table.getValueAt(rowIndex, 0).toString();

        Object checkoutObj = customer_table.getValueAt(rowIndex, 10);
        String currentCheckout = (checkoutObj == null) ? "Not Set" : checkoutObj.toString();

        String newCheckout = JOptionPane.showInputDialog(
                this,
                "Current Checkout: " + currentCheckout +
                "\nEnter New Checkout Date (YYYY-MM-DD):"
        );

        if (newCheckout == null || newCheckout.trim().isEmpty()) {
            return;
        }

        String sql = "UPDATE customer_checkin SET checkout_date = ? WHERE id = ?";
        boolean updated = DataBaseCon.updateRecord(sql, newCheckout, id);

        if (updated) {
            showCustomDialog("Checkout extended successfully!", "SUCCESS");
            displayData();
        } else {
            showCustomDialog("Failed to update booking!", "ERROR");
        }
    }                                 

    private void editMouseEntered(java.awt.event.MouseEvent evt) {                                  
        // Handled in modern design
    }                                 

    private void editMouseExited(java.awt.event.MouseEvent evt) {                                 
        // Handled in modern design
    }                                

    private void searchKeyPressed(java.awt.event.KeyEvent evt) {                                  
        searchTable();
    }                                 

    private void search_buttonMouseClicked(java.awt.event.MouseEvent evt) {                                           
        searchTable();
    }                                          

    private void customer_tableMouseClicked(java.awt.event.MouseEvent evt) {                                            
        int row = customer_table.getSelectedRow();

        if (row < 0) return;

        // Convert view row to model row
        row = customer_table.convertRowIndexToModel(row);

        String id = customer_table.getValueAt(row, 0).toString();
        String roomNumber = customer_table.getValueAt(row, 1).toString();
        Object statusObj = customer_table.getValueAt(row, 14);
        String status = (statusObj == null) ? "" : statusObj.toString();

        // Only allow checkout if currently CHECKED IN
        if (!status.equals("CHECKED IN")) {
            showCustomDialog("Customer already checked out!", "UNAVAILABLE");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Check out this customer now?",
                "Confirm Checkout",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            String today = new java.text.SimpleDateFormat("yyyy-MM-dd")
                    .format(new java.util.Date());

            try {

                String sql1 = "UPDATE customer_checkin SET checkout_date=?, status='CHECKED OUT' WHERE id=?";
                String sql2 = "UPDATE rooms SET status='Available' WHERE room_number=?";

                DataBaseCon.updateRecord(sql1, today, id);
                DataBaseCon.updateRecord(sql2, roomNumber);

                showCustomDialog("Customer checked out successfully!", "SUCCESS");

                displayData();

            } catch (Exception e) {
                showCustomDialog("Checkout failed: " + e.getMessage(), "ERROR");
            }
        }
    }                                           

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {                                     
        // Handled in modern design
    }                                    

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {                                     
        displayData();
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable customer_table;
    private javax.swing.JPanel edit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel refresh;
    private javax.swing.JTextField search;
    private javax.swing.JPanel search_button;
    // End of variables declaration//GEN-END:variables
}
