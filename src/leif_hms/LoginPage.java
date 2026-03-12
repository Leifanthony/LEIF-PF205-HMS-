/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leif_hms;

import ADMIN.AdminDashboard;
import ADMIN.UserDash1;
import config.DataBaseCon;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

/**
 *
 * @author User
 */
public class LoginPage extends javax.swing.JFrame {
    
    // Custom colors for hotel theme (matching Landingpage)
    private static final Color PRIMARY_GOLD = new Color(255, 215, 0);
    private static final Color SECONDARY_GOLD = new Color(218, 165, 32);
    private static final Color DARK_PURPLE = new Color(75, 0, 130);
    private static final Color MEDIUM_PURPLE = new Color(128, 0, 128);
    private static final Color LIGHT_PURPLE = new Color(186, 85, 211);
    private static final Color HOVER_GOLD = new Color(255, 223, 0);
    private static final Color ERROR_RED = new Color(255, 69, 0, 180);
    
    // Custom button with 3D and glow effects
    private class ModernButton extends JButton {
        private float glowIntensity = 0f;
        private float pressIntensity = 0f;
        private Timer glowTimer;
        private Timer pressTimer;
        private boolean isHovered = false;
        private boolean isPressed = false;
        
        public ModernButton(String text) {
            super(text);
            setFont(new Font("Constantia", Font.BOLD, 16));
            setForeground(Color.WHITE);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
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
                public void mousePressed(MouseEvent e) {
                    isPressed = true;
                    startPressAnimation(true);
                }
                
                @Override
                public void mouseReleased(MouseEvent e) {
                    isPressed = false;
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
                    setForeground(new Color(
                        255, 
                        255 - (int)(40 * (1 - glowIntensity)), 
                        255 - (int)(40 * (1 - glowIntensity))
                    ));
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
            
            // Draw gradient background with 3D effect
            GradientPaint gradient = new GradientPaint(
                0, pressOffset, new Color(60, 60, 60),
                w, h - pressOffset, new Color(40, 40, 40)
            );
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, w, h, 25, 25);
            
            // Draw inner glow
            if (glowIntensity > 0) {
                // Inner border glow
                g2.setColor(new Color(255, 215, 0, (int)(150 * glowIntensity)));
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(2, 2, w-5, h-5, 22, 22);
                
                // Outer glow rings
                for (int i = 1; i <= 3; i++) {
                    int alpha = (int)(50 * glowIntensity * (1 - i/4f));
                    g2.setColor(new Color(255, 215, 0, alpha));
                    g2.setStroke(new BasicStroke(i));
                    g2.drawRoundRect(i, i, w-2*i-1, h-2*i-1, 25-i, 25-i);
                }
            }
            
            // Draw 3D highlight
            g2.setColor(new Color(255, 255, 255, 30));
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(1, 1, w-3, h-3, 23, 23);
            
            // Draw shadow
            g2.setColor(new Color(0, 0, 0, 50));
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(0, 0, w-1, h-1, 25, 25);
            
            g2.dispose();
            
            super.paintComponent(g);
        }
    }
    
    // Custom text field with glow - UPDATED for better visibility
    private class ModernTextField extends JTextField {
        private boolean isFocused = false;
        private float glowIntensity = 0f;
        private Timer glowTimer;
        
        public ModernTextField() {
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setForeground(Color.BLACK);
            setCaretColor(PRIMARY_GOLD);
            setBackground(new Color(240, 240, 240));
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    isFocused = true;
                    startGlowAnimation(true);
                    setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_GOLD, 2),
                        BorderFactory.createEmptyBorder(4, 9, 4, 9)
                    ));
                }
                
                @Override
                public void focusLost(FocusEvent e) {
                    isFocused = false;
                    startGlowAnimation(false);
                    setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
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
    
    // Modern password field - UPDATED for better visibility
    private class ModernPasswordField extends JPasswordField {
        private boolean isFocused = false;
        private float glowIntensity = 0f;
        private Timer glowTimer;
        
        public ModernPasswordField() {
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setForeground(Color.BLACK);
            setCaretColor(PRIMARY_GOLD);
            setBackground(new Color(240, 240, 240));
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    isFocused = true;
                    startGlowAnimation(true);
                    setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_GOLD, 2),
                        BorderFactory.createEmptyBorder(4, 9, 4, 9)
                    ));
                }
                
                @Override
                public void focusLost(FocusEvent e) {
                    isFocused = false;
                    startGlowAnimation(false);
                    setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
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
    
    // Custom close button
    private class CloseButton extends JLabel {
        private boolean isHovered = false;
        
        public CloseButton() {
            super("X");
            setFont(new Font("Arial Black", Font.BOLD, 28));
            setForeground(Color.WHITE);
            setHorizontalAlignment(SwingConstants.CENTER);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    setForeground(new Color(255, 80, 80));
                    setFont(getFont().deriveFont(Font.BOLD, 34f));
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    setForeground(Color.WHITE);
                    setFont(getFont().deriveFont(Font.BOLD, 28f));
                }
            });
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            if (isHovered) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw glow effect
                g2.setColor(new Color(255, 0, 0, 60));
                g2.fillOval(getWidth()/2 - 18, getHeight()/2 - 18, 36, 36);
                
                g2.dispose();
            }
            super.paintComponent(g);
        }
    }

    /**
     * Creates new form LoginPage
     */
    public LoginPage() {
        initComponents();
        applyModernDesign();
    }
    
    private void applyModernDesign() {
        // Remove all components from panels
        jPanel2.removeAll();
        
        // Create right panel with dark gradient
        jPanel2 = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Dark gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(30, 30, 30),
                    0, getHeight(), new Color(20, 20, 20)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Add subtle gold border
                g2.setColor(new Color(255, 215, 0, 50));
                g2.setStroke(new BasicStroke(1));
                g2.drawRect(1, 1, getWidth()-3, getHeight()-3);
                
                // Add corner decorations
                g2.setColor(new Color(255, 215, 0, 100));
                g2.setStroke(new BasicStroke(2));
                // Top left
                g2.drawLine(20, 20, 60, 20);
                g2.drawLine(20, 20, 20, 60);
                // Bottom right
                g2.drawLine(getWidth()-60, getHeight()-20, getWidth()-20, getHeight()-20);
                g2.drawLine(getWidth()-20, getHeight()-60, getWidth()-20, getHeight()-20);
                
                g2.dispose();
            }
        };
        jPanel2.setLayout(null);
        
        // Add title with gold gradient
        JLabel titleLabel = new JLabel("LOGIN FORM") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                Font font = new Font("Constantia", Font.BOLD, 28);
                g2.setFont(font);
                FontMetrics fm = g2.getFontMetrics();
                
                String text = "LOGIN FORM";
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                
                // Draw shadow
                g2.setColor(new Color(0, 0, 0, 120));
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
        titleLabel.setBounds(90, 40, 220, 50);
        jPanel2.add(titleLabel);
        
        // Add email label
        JLabel emailLabel = new JLabel("EMAIL");
        emailLabel.setFont(new Font("Constantia", Font.BOLD, 16));
        emailLabel.setForeground(PRIMARY_GOLD);
        emailLabel.setBounds(60, 140, 100, 30);
        jPanel2.add(emailLabel);
        
        // Add email field - FIXED VISIBILITY
        ModernTextField emailField = new ModernTextField();
        emailField.setBounds(60, 170, 280, 35);
        jPanel2.add(emailField);
        
        // Add password label
        JLabel passLabel = new JLabel("PASSWORD");
        passLabel.setFont(new Font("Constantia", Font.BOLD, 16));
        passLabel.setForeground(PRIMARY_GOLD);
        passLabel.setBounds(60, 230, 100, 30);
        jPanel2.add(passLabel);
        
        // Add password field - FIXED VISIBILITY
        ModernPasswordField passField = new ModernPasswordField();
        passField.setBounds(60, 260, 280, 35);
        jPanel2.add(passField);
        
        // Add register link - FIXED (removed glow, only color change on hover)
        JLabel registerLabel = new JLabel("New User? Click here to REGISTER") {
            private boolean isHovered = false;
            
            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        setForeground(PRIMARY_GOLD);
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        setForeground(new Color(180, 180, 180));
                    }
                });
            }
        };
        registerLabel.setFont(new Font("Constantia", Font.BOLD, 14));
        registerLabel.setForeground(new Color(180, 180, 180));
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLabel.setBounds(70, 380, 260, 30);
        registerLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                new Registrationform().setVisible(true);
                dispose();
            }
        });
        jPanel2.add(registerLabel);
        
ModernButton loginBtn = new ModernButton("LOGIN");
loginBtn.setBounds(210, 320, 130, 40);
loginBtn.addActionListener(e -> {
    String email = emailField.getText().trim();
    String password = String.valueOf(passField.getPassword()).trim();
    
    if (email.isEmpty() && password.isEmpty()) {
        shakeComponent(jPanel2);
        showCustomDialog("Both email and password fields are empty. Please enter your credentials to continue.", "EMPTY FIELDS");
        return;
    }
    
    if (email.isEmpty()) {
        shakeComponent(jPanel2);
        showCustomDialog("Email address is required. Please enter your registered email address.", "EMAIL REQUIRED");
        return;
    }
    
    if (password.isEmpty()) {
        shakeComponent(jPanel2);
        showCustomDialog("Password is required. Please enter your password.", "PASSWORD REQUIRED");
        return;
    }
    
    DataBaseCon conf = new DataBaseCon();
    
    // Authenticate the user (active status)
    String authSql = "SELECT * FROM tbl_users WHERE LOWER(TRIM(email))=? AND TRIM(password)=? AND LOWER(TRIM(status))=?";
    boolean loginSuccess = conf.authenticate(authSql, email.toLowerCase(), password, "active");
    
    if (!loginSuccess) {
        shakeComponent(jPanel2);
        showCustomDialog("The email or password you entered is incorrect, or your account is inactive. Please check your credentials and try again.", "INVALID CREDENTIALS");
        return;
    }
    
    // Get full user object
    String userSql = "SELECT * FROM tbl_users WHERE LOWER(TRIM(email))=? AND TRIM(password)=?";
    DataBaseCon.User user = conf.getUser(userSql, email.toLowerCase(), password);
    
    if (user != null) {
        String type = user.type.trim();
        
        if (type.equalsIgnoreCase("ADMIN")) {
            new AdminDashboard().setVisible(true);
        } else {
            new UserDash1(email).setVisible(true);
        }
        
        dispose();
    } else {
        shakeComponent(jPanel2);
        showCustomDialog("User account not found in the system. Please contact support or register a new account.", "USER NOT FOUND");
    }
});
        jPanel2.add(loginBtn);
        
        // Add back button
        ModernButton backBtn = new ModernButton("BACK");
        backBtn.setBounds(60, 320, 130, 40);
        backBtn.addActionListener(e -> {
            new Landingpage().setVisible(true);
            dispose();
        });
        jPanel2.add(backBtn);
        
        // Add close button
        CloseButton closeBtn = new CloseButton();
        closeBtn.setBounds(340, 10, 50, 40);
        closeBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                System.exit(0);
            }
        });
        jPanel2.add(closeBtn);
        
        // Add decorative elements to right panel
        JLabel decor1 = new JLabel();
        decor1.setBounds(20, 420, 100, 2);
        decor1.setOpaque(true);
        decor1.setBackground(new Color(255, 215, 0, 100));
        jPanel2.add(decor1);
        
        JLabel decor2 = new JLabel();
        decor2.setBounds(280, 420, 100, 2);
        decor2.setOpaque(true);
        decor2.setBackground(new Color(255, 215, 0, 100));
        jPanel2.add(decor2);
        
        // Rebuild the frame layout
        getContentPane().removeAll();
        setLayout(new BorderLayout());
        
        // Set preferred sizes
        jPanel2.setPreferredSize(new Dimension(400, 550));
        
        add(jPanel2, BorderLayout.CENTER);
        
        // Refresh
        revalidate();
        repaint();
        
        // Store references for validation
        jTextField1 = emailField;
        jPasswordField1 = passField;
    }
// Shake animation for validation - IMPROVED with no white edges
private void shakeComponent(Component comp) {
    final Point original = comp.getLocation();
    final Timer timer = new Timer(20, null);
    final int[] steps = {0};
    
    timer.addActionListener(e -> {
        int step = steps[0];
        if (step > 10) {
            comp.setLocation(original);
            timer.stop();
            return;
        }
        
        // Smooth shake with decreasing intensity
        int intensity = 8 - (step / 2);
        int offset = (step % 2 == 0) ? intensity : -intensity;
        comp.setLocation(original.x + offset, original.y);
        steps[0]++;
    });
    
    timer.start();
}

// Custom dialog method with transparent background and proper text display
private void showCustomDialog(String message, String title) {
    // Create custom panel with transparency
    JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Semi-transparent background (50% opacity)
            g2.setColor(new Color(25, 25, 25, 200));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            
            // Inner glow
            g2.setColor(new Color(255, 215, 0, 40));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(2, 2, getWidth()-5, getHeight()-5, 28, 28);
            
            g2.dispose();
        }
    };
    panel.setLayout(new BorderLayout(15, 15));
    panel.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));
    panel.setOpaque(false);
    
    // Icon label with better visibility
    JLabel iconLabel = new JLabel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw circle background with glow
            g2.setColor(new Color(255, 215, 0, 220));
            g2.fillOval(20, 5, 45, 45);
            
            // Draw inner glow
            g2.setColor(new Color(255, 255, 255, 150));
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(22, 7, 41, 41);
            
            // Draw exclamation mark
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 40));
            FontMetrics fm = g2.getFontMetrics();
            String text = "!";
            int x = 20 + (45 - fm.stringWidth(text)) / 2;
            int y = 5 + (45 + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(text, x, y - 3);
            
            g2.dispose();
        }
    };
    iconLabel.setPreferredSize(new Dimension(80, 55));
    iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
    panel.add(iconLabel, BorderLayout.NORTH);
    
    // Message label with proper text display - FIXED
    String formattedMessage = "<html>"
            + "<div style='text-align: center; width: 300px; padding: 20px;'>"
            + "<span style='color: #FFD700; font-family: Constantia; font-size: 18px; font-weight: bold;'>" + title + "</span>"
            + "<br><br>"
            + "<span style='color: #FFFFFF; font-family: Constantia; font-size: 14px; line-height: 1.6;'>" + message + "</span>"
            + "</div>"
            + "</html>";
    
    JLabel messageLabel = new JLabel(formattedMessage);
    messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    panel.add(messageLabel, BorderLayout.CENTER);
    
    // Create custom button with transparency
    JButton okButton = new JButton("OK") {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Semi-transparent gradient background
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(128, 0, 128, 220),
                getWidth(), getHeight(), new Color(186, 85, 211, 220)
            );
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            
            // Draw gold border
            g2.setColor(new Color(255, 215, 0, 240));
            g2.setStroke(new BasicStroke(2.5f));
            g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 18, 18);
            
            g2.dispose();
            
            super.paintComponent(g);
        }
    };
    okButton.setFont(new Font("Constantia", Font.BOLD, 16));
    okButton.setForeground(Color.WHITE);
    okButton.setFocusPainted(false);
    okButton.setBorderPainted(false);
    okButton.setContentAreaFilled(false);
    okButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    okButton.setPreferredSize(new Dimension(130, 45));
    
    // Add hover effect to OK button
    okButton.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            okButton.setForeground(PRIMARY_GOLD);
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            okButton.setForeground(Color.WHITE);
        }
    });
    
    // Button panel with transparency
    JPanel buttonPanel = new JPanel();
    buttonPanel.setOpaque(false);
    buttonPanel.add(okButton);
    panel.add(buttonPanel, BorderLayout.SOUTH);
    
    // Create and show dialog
    JDialog dialog = new JDialog(this, "", true);
    dialog.setUndecorated(true);
    dialog.setBackground(new Color(0, 0, 0, 0));
    dialog.getContentPane().setBackground(new Color(0, 0, 0, 0));
    dialog.getContentPane().add(panel);
    dialog.pack();
    dialog.setLocationRelativeTo(this);
    
    // Add decorative border with glow effect
    dialog.getRootPane().setBorder(BorderFactory.createCompoundBorder(
        new javax.swing.border.AbstractBorder() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw outer glow layers
                for (int i = 1; i <= 5; i++) {
                    int alpha = 70 - (i * 10);
                    g2.setColor(new Color(255, 215, 0, alpha));
                    g2.setStroke(new BasicStroke(i));
                    g2.drawRoundRect(x + i, y + i, width - (2*i) - 1, height - (2*i) - 1, 35 - i, 35 - i);
                }
                
                // Draw main gold border
                g2.setColor(new Color(255, 215, 0, 240));
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawRoundRect(x + 2, y + 2, width - 5, height - 5, 25, 25);
                
                // Draw corner decorations
                g2.setColor(new Color(255, 215, 0, 220));
                g2.setStroke(new BasicStroke(3));
                
                // Top-left corner
                g2.drawLine(x + 15, y + 8, x + 30, y + 8);
                g2.drawLine(x + 8, y + 15, x + 8, y + 30);
                
                // Top-right corner
                g2.drawLine(x + width - 30, y + 8, x + width - 15, y + 8);
                g2.drawLine(x + width - 8, y + 15, x + width - 8, y + 30);
                
                // Bottom-left corner
                g2.drawLine(x + 15, y + height - 8, x + 30, y + height - 8);
                g2.drawLine(x + 8, y + height - 30, x + 8, y + height - 15);
                
                // Bottom-right corner
                g2.drawLine(x + width - 30, y + height - 8, x + width - 15, y + height - 8);
                g2.drawLine(x + width - 8, y + height - 30, x + width - 8, y + height - 15);
                
                g2.dispose();
            }
            
            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(15, 15, 15, 15);
            }
        },
        BorderFactory.createEmptyBorder(8, 8, 8, 8)
    ));
    
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

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jPanel3 = new javax.swing.JPanel();
        ps = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        un = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Constantia", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(220, 220, 220));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("LOGIN FORM");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 60, 180, 50));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("X");
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 0, 60, 50));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(220, 220, 220));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("New User? click here to REGISTER");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 460, -1, -1));

        jPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField1ActionPerformed(evt);
            }
        });
        jPanel2.add(jPasswordField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 310, 300, 30));

        ps.setFont(new java.awt.Font("Constantia", 1, 14)); // NOI18N
        ps.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ps.setText("PASSWORD:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ps, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ps, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 280, 100, 30));

        un.setFont(new java.awt.Font("Constantia", 1, 14)); // NOI18N
        un.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        un.setText("EMAIL:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(un, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(un, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 170, 100, 30));
        jPanel2.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, 300, 30));

        jButton1.setText("LOGIN");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 390, 100, 30));

        jButton2.setText("BACK");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 390, 100, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordField1ActionPerformed

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        Registrationform Rm = new Registrationform();
        Rm.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
                System.exit(0);
    }//GEN-LAST:event_jButton2MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       String email = jTextField1.getText().trim(); 
    String password = String.valueOf(jPasswordField1.getPassword()).trim();

    if (email.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter email and password");
        return;
    }

    DataBaseCon conf = new DataBaseCon();

    // Authenticate the user (active status)
    String authSql = "SELECT * FROM tbl_users WHERE LOWER(TRIM(email))=? AND TRIM(password)=? AND LOWER(TRIM(status))=?";
    boolean loginSuccess = conf.authenticate(authSql, email.toLowerCase(), password, "active");

    if (!loginSuccess) {
        JOptionPane.showMessageDialog(this, "Invalid email or password or inactive account");
        return;
    }

    // Get full user object
    String userSql = "SELECT * FROM tbl_users WHERE LOWER(TRIM(email))=? AND TRIM(password)=?";
    DataBaseCon.User user = conf.getUser(userSql, email.toLowerCase(), password);

    if (user != null) {
        String type = user.type.trim();

        if (type.equalsIgnoreCase("ADMIN")) {
            new AdminDashboard().setVisible(true);
        } else {
            new UserDash1(email).setVisible(true);
        }

        this.dispose(); // Close login page

    } else {
        JOptionPane.showMessageDialog(this, "User not found.");
    }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose ();
        Landingpage lp = new Landingpage();
        lp.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(LoginPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel ps;
    private javax.swing.JLabel un;
    // End of variables declaration//GEN-END:variables
}
