package ADMIN;

import config.DataBaseCon;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.text.SimpleDateFormat;

/**
 *
 * @author User
 */
public class CustomerBooking extends javax.swing.JFrame {

    private String loggedInUser;
    
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
            setFont(new Font("Constantia", Font.BOLD, 14));
            setForeground(PRIMARY_GOLD);
        }
    }
    
    // Custom text field
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

    public CustomerBooking(String email) {
        initComponents();
        this.loggedInUser = email;
        applyModernDesign();
        loadRoomNumbers();

        // Only attach listener after combo box is filled
        jComboBox5.addActionListener(e -> {
            if (jComboBox5.getItemCount() > 0) loadRoomDetails();
        });

        // Auto-fill today's date in check-in field
        jTextField7.setText(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
    }
    
private void applyModernDesign() {
    // Clear content pane
    getContentPane().removeAll();
    getContentPane().setLayout(null);
    
    // Set frame size
    setSize(960, 600);
    setLocationRelativeTo(null);
    
    // Make background completely transparent
    setBackground(new Color(0, 0, 0, 0));
    
    // Create main panel with transparent background
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(null);
    mainPanel.setBounds(0, 0, 960, 600);
    mainPanel.setOpaque(false);
    
    // Create main form panel with softer corners
    JPanel formPanel = new JPanel() {
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
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // Softer corners (30px radius)
            
            g2.setColor(new Color(255, 215, 0, 100));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(2, 2, getWidth()-5, getHeight()-5, 26, 26); // Matching inner border
            
            g2.dispose();
        }
    };
    formPanel.setLayout(null);
    formPanel.setBounds(30, 30, 900, 540); // Adjusted to fit new size
    formPanel.setOpaque(false);
    
    // Main Title inside panel
    JLabel mainTitleLabel = new JLabel("CUSTOMER CHECK-IN") {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            Font font = new Font("Constantia", Font.BOLD, 32);
            g2.setFont(font);
            FontMetrics fm = g2.getFontMetrics();
            
            String text = "CUSTOMER CHECK-IN";
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
    mainTitleLabel.setBounds(0, 15, 900, 50);
    mainTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    formPanel.add(mainTitleLabel);
    
    // Form title
    JLabel formTitle = new JLabel("BOOKING DETAILS");
    formTitle.setFont(new Font("Constantia", Font.BOLD, 22));
    formTitle.setForeground(PRIMARY_GOLD);
    formTitle.setBounds(30, 70, 300, 30);
    formPanel.add(formTitle);
    
    
    // Left column (x = 30)
// Left column (x = 30)
// Left column (x = 30)
int leftX = 30;
int rightX = 350;
int roomX = 670;
int labelY = 115;
int fieldY = 140;
int spacing = 60; // Increased spacing between rows
int labelHeight = 20; // Smaller height for labels
int fieldHeight = 35; // Standard height for fields

// Room Number (Room column)
GlowLabel roomNumberLabel = new GlowLabel("ROOM NUMBER");
roomNumberLabel.setBounds(roomX, labelY, 150, labelHeight);
formPanel.add(roomNumberLabel);

jComboBox5.setBounds(roomX, fieldY, 200, fieldHeight);
((JComponent) jComboBox5).setBorder(BorderFactory.createCompoundBorder(
    BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
    BorderFactory.createEmptyBorder(5, 10, 5, 10)
));
formPanel.add(jComboBox5);

// Name (First row)
GlowLabel fullNameLabel = new GlowLabel("FULL NAME");
fullNameLabel.setBounds(leftX, labelY, 150, labelHeight);
formPanel.add(fullNameLabel);

jTextField1.setBounds(leftX, fieldY, 250, fieldHeight);
formPanel.add(jTextField1);

// Address (First row - right column)
GlowLabel addressLabel = new GlowLabel("ADDRESS");
addressLabel.setBounds(rightX, labelY, 150, labelHeight);
formPanel.add(addressLabel);

jTextField6.setBounds(rightX, fieldY, 250, fieldHeight);
formPanel.add(jTextField6);

// Mobile (Second row)
GlowLabel mobileLabel = new GlowLabel("MOBILE NUMBER");
mobileLabel.setBounds(leftX, labelY + spacing, 150, labelHeight);
formPanel.add(mobileLabel);

jTextField2.setBounds(leftX, fieldY + spacing, 250, fieldHeight);
formPanel.add(jTextField2);

// Check-in Date (Second row - right column)
GlowLabel checkinDateLabel = new GlowLabel("CHECK-IN DATE");
checkinDateLabel.setBounds(rightX, labelY + spacing, 150, labelHeight);
formPanel.add(checkinDateLabel);

jTextField7.setBounds(rightX, fieldY + spacing, 250, fieldHeight);
jTextField7.setEditable(false);
jTextField7.setBackground(new Color(80, 80, 80));
formPanel.add(jTextField7);

// Price (Second row - room column) - THIS IS THE ONLY PRICE LABEL
GlowLabel priceLabel = new GlowLabel("PRICE");
priceLabel.setBounds(roomX, labelY + spacing, 150, labelHeight);
formPanel.add(priceLabel);

jTextField9.setBounds(roomX, fieldY + spacing, 200, fieldHeight);
jTextField9.setEditable(false);
jTextField9.setBackground(new Color(80, 80, 80));
formPanel.add(jTextField9);

// Nationality (Third row)
GlowLabel nationalityLabel = new GlowLabel("NATIONALITY");
nationalityLabel.setBounds(leftX, labelY + (spacing * 2), 150, labelHeight);
formPanel.add(nationalityLabel);

jTextField3.setBounds(leftX, fieldY + (spacing * 2), 250, fieldHeight);
formPanel.add(jTextField3);

// ID Proof (Third row - right column)
GlowLabel idProofLabel = new GlowLabel("ID PROOF");
idProofLabel.setBounds(rightX, labelY + (spacing * 2), 150, labelHeight);
formPanel.add(idProofLabel);

jTextField5.setBounds(rightX, fieldY + (spacing * 2), 250, fieldHeight);
formPanel.add(jTextField5);

// Gender (Fourth row)
GlowLabel genderLabel = new GlowLabel("GENDER");
genderLabel.setBounds(leftX, labelY + (spacing * 3), 150, labelHeight);
formPanel.add(genderLabel);

jComboBox1.setBounds(leftX, fieldY + (spacing * 3), 250, fieldHeight);
formPanel.add(jComboBox1);

// Bed Type (Fourth row - right column)
GlowLabel bedTypeLabel = new GlowLabel("BED TYPE");
bedTypeLabel.setBounds(rightX, labelY + (spacing * 3), 150, labelHeight);
formPanel.add(bedTypeLabel);

jTextField10.setBounds(rightX, fieldY + (spacing * 3), 250, fieldHeight);
jTextField10.setEditable(false);
jTextField10.setBackground(new Color(80, 80, 80));
formPanel.add(jTextField10);

// Email (Fifth row)
GlowLabel emailLabel = new GlowLabel("EMAIL");
emailLabel.setBounds(leftX, labelY + (spacing * 4), 150, labelHeight);
formPanel.add(emailLabel);

jTextField4.setBounds(leftX, fieldY + (spacing * 4), 250, fieldHeight);
formPanel.add(jTextField4);

// Room Type (Fifth row - right column)
GlowLabel roomTypeLabel = new GlowLabel("ROOM TYPE");
roomTypeLabel.setBounds(rightX, labelY + (spacing * 4), 150, labelHeight);
formPanel.add(roomTypeLabel);

jTextField8.setBounds(rightX, fieldY + (spacing * 4), 250, fieldHeight);
jTextField8.setEditable(false);
jTextField8.setBackground(new Color(80, 80, 80));
formPanel.add(jTextField8);

// Buttons
ModernButton allotBtn = new ModernButton("ALLOCATE ROOM");
allotBtn.setBounds(roomX, fieldY + (spacing * 2) + 20, 200, 45);
allotBtn.addActionListener(this::jButton2ActionPerformed);
formPanel.add(allotBtn);

ModernButton clearBtn = new ModernButton("CLEAR");
clearBtn.setBounds(roomX, fieldY + (spacing * 3) + 15, 90, 40);
clearBtn.addActionListener(this::jButton3ActionPerformed);
formPanel.add(clearBtn);

ModernButton backBtn = new ModernButton("BACK");
backBtn.setBounds(roomX + 100, fieldY + (spacing * 3) + 15, 100, 40);
backBtn.addActionListener(e -> {
    dispose();
    new UserDash1(loggedInUser).setVisible(true);
});
formPanel.add(backBtn);

// Decorative line
JLabel decorLine = new JLabel();
decorLine.setBounds(30, 500, 880, 2);
decorLine.setOpaque(true);
decorLine.setBackground(new Color(255, 215, 0, 100));
formPanel.add(decorLine);

mainPanel.add(formPanel);
    
    getContentPane().add(mainPanel);
    
    // Update references to use custom components
    jTextField1.setBackground(new Color(60, 60, 60));
    jTextField1.setForeground(Color.WHITE);
    jTextField1.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
        BorderFactory.createEmptyBorder(8, 12, 8, 12)
    ));
    
    jTextField2.setBackground(new Color(60, 60, 60));
    jTextField2.setForeground(Color.WHITE);
    jTextField2.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
        BorderFactory.createEmptyBorder(8, 12, 8, 12)
    ));
    
    jTextField3.setBackground(new Color(60, 60, 60));
    jTextField3.setForeground(Color.WHITE);
    jTextField3.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
        BorderFactory.createEmptyBorder(8, 12, 8, 12)
    ));
    
    jTextField4.setBackground(new Color(60, 60, 60));
    jTextField4.setForeground(Color.WHITE);
    jTextField4.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
        BorderFactory.createEmptyBorder(8, 12, 8, 12)
    ));
    
    jTextField5.setBackground(new Color(60, 60, 60));
    jTextField5.setForeground(Color.WHITE);
    jTextField5.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
        BorderFactory.createEmptyBorder(8, 12, 8, 12)
    ));
    
    jTextField6.setBackground(new Color(60, 60, 60));
    jTextField6.setForeground(Color.WHITE);
    jTextField6.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
        BorderFactory.createEmptyBorder(8, 12, 8, 12)
    ));
    
    jTextField7.setBackground(new Color(80, 80, 80));
    jTextField7.setForeground(PRIMARY_GOLD);
    jTextField7.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
        BorderFactory.createEmptyBorder(8, 12, 8, 12)
    ));
    
    jTextField8.setBackground(new Color(80, 80, 80));
    jTextField8.setForeground(PRIMARY_GOLD);
    jTextField8.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
        BorderFactory.createEmptyBorder(8, 12, 8, 12)
    ));
    
    jTextField9.setBackground(new Color(80, 80, 80));
    jTextField9.setForeground(PRIMARY_GOLD);
    jTextField9.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
        BorderFactory.createEmptyBorder(8, 12, 8, 12)
    ));
    
    jTextField10.setBackground(new Color(80, 80, 80));
    jTextField10.setForeground(PRIMARY_GOLD);
    jTextField10.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
        BorderFactory.createEmptyBorder(8, 12, 8, 12)
    ));
    
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
        tr1.setBounds(1000 - offset - cornerSize, 80, cornerSize, 2);
        tr1.setOpaque(true);
        tr1.setBackground(new Color(255, 215, 0, 150));
        panel.add(tr1);
        
        JLabel tr2 = new JLabel();
        tr2.setBounds(1000 - offset - 1, 80, 2, cornerSize);
        tr2.setOpaque(true);
        tr2.setBackground(new Color(255, 215, 0, 150));
        panel.add(tr2);
        
        // Bottom-left corner
        JLabel bl1 = new JLabel();
        bl1.setBounds(offset, 650 - offset - cornerSize, cornerSize, 2);
        bl1.setOpaque(true);
        bl1.setBackground(new Color(255, 215, 0, 150));
        panel.add(bl1);
        
        JLabel bl2 = new JLabel();
        bl2.setBounds(offset, 650 - offset - cornerSize, 2, cornerSize);
        bl2.setOpaque(true);
        bl2.setBackground(new Color(255, 215, 0, 150));
        panel.add(bl2);
        
        // Bottom-right corner
        JLabel br1 = new JLabel();
        br1.setBounds(1000 - offset - cornerSize, 650 - offset - cornerSize, cornerSize, 2);
        br1.setOpaque(true);
        br1.setBackground(new Color(255, 215, 0, 150));
        panel.add(br1);
        
        JLabel br2 = new JLabel();
        br2.setBounds(1000 - offset - 1, 650 - offset - cornerSize, 2, cornerSize);
        br2.setOpaque(true);
        br2.setBackground(new Color(255, 215, 0, 150));
        panel.add(br2);
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
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jComboBox5 = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Constantia", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 0));
        jLabel2.setText("NAME");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, -1, -1));
        jPanel2.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 150, 30));

        jLabel3.setFont(new java.awt.Font("Constantia", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 0));
        jLabel3.setText("MOBILE NO.");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));
        jPanel2.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 150, 30));

        jLabel4.setFont(new java.awt.Font("Constantia", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 0));
        jLabel4.setText("NATIONALITY");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));
        jPanel2.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 150, 30));

        jLabel5.setFont(new java.awt.Font("Constantia", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 0));
        jLabel5.setText("GENDER");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, -1, -1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "MALE", "FEMALE" }));
        jPanel2.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 150, 30));

        jLabel6.setFont(new java.awt.Font("Constantia", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 0));
        jLabel6.setText("EMAIL");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, -1, -1));
        jPanel2.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 150, 30));

        jLabel7.setFont(new java.awt.Font("Constantia", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 0));
        jLabel7.setText("ID PROOF");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 250, -1, -1));
        jPanel2.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 270, 170, 30));

        jLabel8.setFont(new java.awt.Font("Constantia", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 0));
        jLabel8.setText("ADDRESS");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 10, -1, -1));
        jPanel2.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 30, 160, 30));
        jPanel2.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 90, 170, 30));

        jLabel10.setFont(new java.awt.Font("Constantia", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 0));
        jLabel10.setText("BED");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 130, 40, -1));

        jLabel11.setFont(new java.awt.Font("Constantia", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 0));
        jLabel11.setText("ROOM TYPE");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 190, -1, -1));

        jLabel12.setFont(new java.awt.Font("Constantia", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 0));
        jLabel12.setText("PRICE");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 70, -1, -1));

        jButton1.setBackground(new java.awt.Color(204, 204, 204));
        jButton1.setForeground(new java.awt.Color(51, 51, 51));
        jButton1.setText("BACK");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 270, 100, 30));

        jButton2.setBackground(new java.awt.Color(204, 204, 204));
        jButton2.setForeground(new java.awt.Color(51, 51, 51));
        jButton2.setText("ALLOTE ROOM");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 160, 120, 40));

        jButton3.setBackground(new java.awt.Color(204, 204, 204));
        jButton3.setForeground(new java.awt.Color(51, 51, 51));
        jButton3.setText("CLEAR");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 220, 100, 30));

        jLabel14.setFont(new java.awt.Font("Constantia", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 0));
        jLabel14.setText("Room Number");
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 10, -1, -1));
        jPanel2.add(jTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 100, 190, 30));

        jPanel2.add(jComboBox5, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 30, 190, 30));

        jLabel15.setFont(new java.awt.Font("Constantia", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 0));
        jLabel15.setText("CHECK IN DATE (TODAY)");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 70, -1, -1));
        jPanel2.add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 210, 170, 30));
        jPanel2.add(jTextField10, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 150, 170, 30));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 680, 340));

        jPanel3.setBackground(new java.awt.Color(51, 51, 51));

        jLabel13.setBackground(new java.awt.Color(51, 51, 51));
        jLabel13.setFont(new java.awt.Font("Constantia", 1, 36)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 0));
        jLabel13.setText("CUSTOMER  CHECK IN");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 430, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        this.dispose();
    new UserDash1(loggedInUser).setVisible(true);
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       String name = jTextField1.getText().trim();
    String mobile = jTextField2.getText().trim();
    String nationality = jTextField3.getText().trim();
    String gender = jComboBox1.getSelectedItem().toString();
    String email = jTextField4.getText().trim();
    String idProof = jTextField5.getText().trim();
    String address = jTextField6.getText().trim();
    String checkinStr = new SimpleDateFormat("yyyy-MM-dd")
        .format(new java.util.Date());
    String bed = jTextField10.getText().trim();
    String roomType = jTextField8.getText().trim();
    String roomNumber = jComboBox5.getSelectedItem() != null ? jComboBox5.getSelectedItem().toString() : "";
    String price = jTextField9.getText().trim();

    // ================= VALIDATION =================
    if (name.isEmpty() || mobile.isEmpty() || nationality.isEmpty() ||
        email.isEmpty() || idProof.isEmpty() || address.isEmpty() ||
        roomNumber.isEmpty() || price.isEmpty()) {

        JOptionPane.showMessageDialog(this, "Please fill all fields!");
        return;
    }

    try{
        
    String insertSql = "INSERT INTO customer_checkin "
        + "(room_number, name, mobile_no, nationality, gender, email, id_proof, address, "
        + "checkin_date, bed_type, room_type, price, status) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    String updateSql = "UPDATE rooms SET status='Occupied' WHERE room_number=?";

    try (Connection conn = DataBaseCon.connectDB()) {

        conn.setAutoCommit(false);

        try (PreparedStatement pstInsert = conn.prepareStatement(insertSql);
             PreparedStatement pstUpdate = conn.prepareStatement(updateSql)) {

            pstInsert.setString(1, roomNumber);
            pstInsert.setString(2, name);
            pstInsert.setString(3, mobile);
            pstInsert.setString(4, nationality);
            pstInsert.setString(5, gender);
            pstInsert.setString(6, email);
            pstInsert.setString(7, idProof);
            pstInsert.setString(8, address);
            pstInsert.setString(9, checkinStr);
            pstInsert.setString(10, bed);
            pstInsert.setString(11, roomType);
            pstInsert.setDouble(12, Double.parseDouble(price));
            pstInsert.setString(13, "CHECKED IN");

            pstInsert.executeUpdate();

            pstUpdate.setString(1, roomNumber);
            pstUpdate.executeUpdate();

            conn.commit();

            JOptionPane.showMessageDialog(this, "Room Allotted Successfully!");
            clearFields();
            loadRoomNumbers();

        } catch (SQLException ex) {
            conn.rollback();
            JOptionPane.showMessageDialog(this, "Transaction Failed: " + ex.getMessage());
        }
    }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format! Use yyyy-MM-dd");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        clearFields();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
        new UserDash1(loggedInUser).setVisible(true);
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
            java.util.logging.Logger.getLogger(CustomerBooking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CustomerBooking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CustomerBooking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CustomerBooking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            new CustomerBooking("test@email.com").setVisible(true);
        }
    });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables

    private void clearFields() {
    jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jTextField6.setText("");
        jTextField7.setText(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
        jTextField9.setText("");

        if (jComboBox5.getItemCount() > 0) jComboBox5.setSelectedIndex(0);
        jComboBox1.setSelectedIndex(0);
        jTextField10.setText("");
        jTextField8.setText("");
    }

    private void loadRoomNumbers() {
         try (Connection conn = DataBaseCon.connectDB()) {
            String sql = "SELECT room_number FROM rooms WHERE status='Available'";
            try (PreparedStatement pst = conn.prepareStatement(sql);
                 ResultSet rs = pst.executeQuery()) {

                jComboBox5.removeAllItems();
                while (rs.next()) {
                    jComboBox5.addItem(rs.getString("room_number"));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading rooms: " + e.getMessage());
        }
    }

    private void loadPrice() {
        if (jComboBox5.getSelectedItem() == null) return;

        String selectedRoom = jComboBox5.getSelectedItem().toString();
        try (Connection conn = DataBaseCon.connectDB();
             PreparedStatement pst = conn.prepareStatement("SELECT price FROM rooms WHERE room_number=?")) {

            pst.setString(1, selectedRoom);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    jTextField9.setText(String.valueOf(rs.getDouble("price")));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading price: " + e.getMessage());
        }
    }
        private void loadRoomDetails() {
    if (jComboBox5.getSelectedItem() == null) return;

    String selectedRoom = jComboBox5.getSelectedItem().toString();
    String sql = "SELECT price, bed_type, room_type FROM rooms WHERE room_number=?";

    try (Connection conn = DataBaseCon.connectDB();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setString(1, selectedRoom);
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                jTextField9.setText(String.valueOf(rs.getDouble("price"))); // Price
                jTextField10.setText(rs.getString("bed_type"));             // Bed
                jTextField8.setText(rs.getString("room_type"));             // Room Type
            }
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error loading room details: " + e.getMessage());
    }
    }
}
