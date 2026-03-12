/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leif_hms;

import config.DataBaseCon;  
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.AbstractBorder;

/**
 *
 * @author User
 */
public class Registrationform extends javax.swing.JFrame {
    
    // Custom colors for hotel theme (matching Login and Landing)
    private static final Color PRIMARY_GOLD = new Color(255, 215, 0);
    private static final Color SECONDARY_GOLD = new Color(218, 165, 32);
    private static final Color DARK_PURPLE = new Color(75, 0, 130);
    private static final Color MEDIUM_PURPLE = new Color(128, 0, 128);
    private static final Color LIGHT_PURPLE = new Color(186, 85, 211);
    
    // Custom button with 3D and glow effects
    private class ModernButton extends JButton {
        private float glowIntensity = 0f;
        private float pressIntensity = 0f;
        private Timer glowTimer;
        private Timer pressTimer;
        
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
            
            // Draw gradient background with 3D effect
            GradientPaint gradient = new GradientPaint(
                0, pressOffset, new Color(60, 60, 60),
                w, h - pressOffset, new Color(40, 40, 40)
            );
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, w, h, 25, 25);
            
            // Draw glow effect
            if (glowIntensity > 0) {
                g2.setColor(new Color(255, 215, 0, (int)(150 * glowIntensity)));
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(2, 2, w-5, h-5, 22, 22);
                
                for (int i = 1; i <= 3; i++) {
                    int alpha = (int)(50 * glowIntensity * (1 - i/4f));
                    g2.setColor(new Color(255, 215, 0, alpha));
                    g2.setStroke(new BasicStroke(i));
                    g2.drawRoundRect(i, i, w-2*i-1, h-2*i-1, 25-i, 25-i);
                }
            }
            
            // Draw 3D highlight and shadow
            g2.setColor(new Color(255, 255, 255, 30));
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(1, 1, w-3, h-3, 23, 23);
            
            g2.setColor(new Color(0, 0, 0, 50));
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(0, 0, w-1, h-1, 25, 25);
            
            g2.dispose();
            super.paintComponent(g);
        }
    }
    
    // Custom text field with glow
    private class ModernTextField extends JTextField {
        private boolean isFocused = false;
        private float glowIntensity = 0f;
        private Timer glowTimer;
        private final String placeholder;
        
        public ModernTextField(String placeholder) {
            this.placeholder = placeholder;
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setForeground(Color.BLACK);
            setCaretColor(PRIMARY_GOLD);
            setBackground(new Color(255, 255, 255));
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
            
            // Set placeholder text
            setText(placeholder);
            setForeground(Color.GRAY);
            
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    isFocused = true;
                    startGlowAnimation(true);
                    setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_GOLD, 2),
                        BorderFactory.createEmptyBorder(7, 11, 7, 11)
                    ));
                    if (getText().equals(placeholder)) {
                        setText("");
                        setForeground(Color.BLACK);
                    }
                }
                
                @Override
                public void focusLost(FocusEvent e) {
                    isFocused = false;
                    startGlowAnimation(false);
                    setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                    ));
                    if (getText().isEmpty()) {
                        setText(placeholder);
                        setForeground(Color.GRAY);
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
        
        public String getActualText() {
            String text = getText();
            if (text.equals(placeholder)) {
                return "";
            }
            return text;
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
                g2.setColor(new Color(255, 0, 0, 60));
                g2.fillOval(getWidth()/2 - 18, getHeight()/2 - 18, 36, 36);
                g2.dispose();
            }
            super.paintComponent(g);
        }
    }
    
    // Shake animation for validation
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
            
            int intensity = 8 - (step / 2);
            int offset = (step % 2 == 0) ? intensity : -intensity;
            comp.setLocation(original.x + offset, original.y);
            steps[0]++;
        });
        
        timer.start();
    }
    
    // Custom dialog method
    private void showCustomDialog(String message, String title) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(25, 25, 25, 230));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                
                g2.setColor(new Color(255, 215, 0, 40));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(2, 2, getWidth()-5, getHeight()-5, 28, 28);
                
                g2.dispose();
            }
        };
        panel.setLayout(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));
        panel.setOpaque(false);
        
        // Icon
        JLabel iconLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(255, 215, 0, 220));
                g2.fillOval(20, 5, 45, 45);
                
                g2.setColor(new Color(255, 255, 255, 150));
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(22, 7, 41, 41);
                
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
        
        // Message
        String formattedMessage = "<html>"
                + "<div style='text-align: center; width: 320px; padding: 25px;'>"
                + "<span style='color: #FFD700; font-family: Constantia; font-size: 18px; font-weight: bold;'>" + title + "</span>"
                + "<br><br>"
                + "<span style='color: #FFFFFF; font-family: Constantia; font-size: 14px; line-height: 1.6;'>" + message + "</span>"
                + "</div>"
                + "</html>";
        
        JLabel messageLabel = new JLabel(formattedMessage);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(messageLabel, BorderLayout.CENTER);
       
        // OK Button
        JButton okButton = new JButton("OK") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(128, 0, 128, 220),
                    getWidth(), getHeight(), new Color(186, 85, 211, 220)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
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
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(okButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Dialog
        JDialog dialog = new JDialog(this, "", true);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0));
        dialog.getContentPane().setBackground(new Color(0, 0, 0, 0));
        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        
        // Border with glow
        dialog.getRootPane().setBorder(BorderFactory.createCompoundBorder(
            new AbstractBorder() {
                @Override
                public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    for (int i = 1; i <= 5; i++) {
                        int alpha = 70 - (i * 10);
                        g2.setColor(new Color(255, 215, 0, alpha));
                        g2.setStroke(new BasicStroke(i));
                        g2.drawRoundRect(x + i, y + i, width - (2*i) - 1, height - (2*i) - 1, 35 - i, 35 - i);
                    }
                    
                    g2.setColor(new Color(255, 215, 0, 240));
                    g2.setStroke(new BasicStroke(2.5f));
                    g2.drawRoundRect(x + 2, y + 2, width - 5, height - 5, 25, 25);
                    
                    // Corner decorations
                    g2.setColor(new Color(255, 215, 0, 220));
                    g2.setStroke(new BasicStroke(3));
                    
                    // Top-left
                    g2.drawLine(x + 15, y + 8, x + 30, y + 8);
                    g2.drawLine(x + 8, y + 15, x + 8, y + 30);
                    
                    // Top-right
                    g2.drawLine(x + width - 30, y + 8, x + width - 15, y + 8);
                    g2.drawLine(x + width - 8, y + 15, x + width - 8, y + 30);
                    
                    // Bottom-left
                    g2.drawLine(x + 15, y + height - 8, x + 30, y + height - 8);
                    g2.drawLine(x + 8, y + height - 30, x + 8, y + height - 15);
                    
                    // Bottom-right
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

    public Registrationform() {
        initComponents();
        applyModernDesign();
    }
    
    private void applyModernDesign() {
        // Set frame size
        setSize(480, 500);
            
        // Remove all components
        jPanel7.removeAll();
        
        // Main panel with dark gradient
        jPanel7 = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(30, 30, 30),
                    0, getHeight(), new Color(20, 20, 20)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Gold border
                g2.setColor(new Color(255, 215, 0, 50));
                g2.setStroke(new BasicStroke(1));
                g2.drawRect(1, 1, getWidth()-3, getHeight()-3);
                
                // Corner decorations
                g2.setColor(new Color(255, 215, 0, 100));
                g2.setStroke(new BasicStroke(2));
                
                // Top-left
                g2.drawLine(20, 20, 60, 20);
                g2.drawLine(20, 20, 20, 60);
                
                
                
                // Bottom-right
                g2.drawLine(getWidth()-60, getHeight()-20, getWidth()-20, getHeight()-20);
                g2.drawLine(getWidth()-20, getHeight()-60, getWidth()-20, getHeight()-20);
                
                g2.dispose();
            }
        };
        jPanel7.setLayout(null);
        jPanel7.setBounds(0, 0, 500, 600);
        
        // Title with gradient
        JLabel titleLabel = new JLabel("CREATE ACCOUNT") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                Font font = new Font("Constantia", Font.BOLD, 26);
                g2.setFont(font);
                FontMetrics fm = g2.getFontMetrics();
                
                String text = "CREATE ACCOUNT";
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                
                g2.setColor(new Color(0, 0, 0, 120));
                g2.drawString(text, x + 2, y + 2);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, PRIMARY_GOLD,
                    getWidth(), 0, SECONDARY_GOLD
                );
                g2.setPaint(gradient);
                g2.drawString(text, x, y);
                
                g2.dispose();
            }
        };
        titleLabel.setBounds(85, 30, 300, 50);
        jPanel7.add(titleLabel);
        
        // Create form fields
// Create form fields (with username field)
ModernTextField firstNameField = new ModernTextField("First Name");
firstNameField.setBounds(70, 100, 340, 45);
jPanel7.add(firstNameField);

ModernTextField lastNameField = new ModernTextField("Last Name");
lastNameField.setBounds(70, 155, 340, 45);
jPanel7.add(lastNameField);

ModernTextField emailField = new ModernTextField("Email Address");
emailField.setBounds(70, 210, 340, 45);
jPanel7.add(emailField);

ModernTextField usernameField = new ModernTextField("Username");
usernameField.setBounds(70, 265, 340, 45);
jPanel7.add(usernameField);

ModernTextField passwordField = new ModernTextField("Password");
passwordField.setBounds(70, 320, 340, 45);
jPanel7.add(passwordField);

   // Register button
    ModernButton registerBtn = new ModernButton("REGISTER");
    registerBtn.setBounds(250, 380, 180, 45);
    registerBtn.addActionListener(e -> {
        String fname = firstNameField.getActualText();
        String lname = lastNameField.getActualText();
        String email = emailField.getActualText();
        String username = usernameField.getActualText();
        String password = passwordField.getActualText();
        
        // Validation
        if (fname.isEmpty()) {
            shakeComponent(jPanel7);
            showCustomDialog("Please enter your first name", "FIELD REQUIRED");
            firstNameField.requestFocus();
            return;
        }
        
        if (lname.isEmpty()) {
            shakeComponent(jPanel7);
            showCustomDialog("Please enter your last name", "FIELD REQUIRED");
            lastNameField.requestFocus();
            return;
        }
        
        if (email.isEmpty()) {
            shakeComponent(jPanel7);
            showCustomDialog("Please enter your email address", "FIELD REQUIRED");
            emailField.requestFocus();
            return;
        }
        
        if (!email.contains("@") || !email.contains(".")) {
            shakeComponent(jPanel7);
            showCustomDialog("Please enter a valid email address (e.g., name@domain.com)", "INVALID EMAIL");
            emailField.requestFocus();
            return;
        }
        
        if (username.isEmpty()) {
            shakeComponent(jPanel7);
            showCustomDialog("Please choose a username", "FIELD REQUIRED");
            usernameField.requestFocus();
            return;
        }
        
        if (username.length() < 3) {
            shakeComponent(jPanel7);
            showCustomDialog("Username must be at least 3 characters long", "INVALID USERNAME");
            usernameField.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            shakeComponent(jPanel7);
            showCustomDialog("Please enter a password", "FIELD REQUIRED");
            passwordField.requestFocus();
            return;
        }
        
        if (password.length() < 6) {
            shakeComponent(jPanel7);
            showCustomDialog("Password must be at least 6 characters long", "WEAK PASSWORD");
            passwordField.requestFocus();
            return;
        }
        
        try (Connection con = DataBaseCon.connectDB()) {
            
            if (con == null) {
                showCustomDialog("Database connection failed! Please check your connection.", "CONNECTION ERROR");
                return;
            }
            
            // Check if email exists
            String checkEmailSql = "SELECT 1 FROM tbl_users WHERE email = ?";
            PreparedStatement checkPst = con.prepareStatement(checkEmailSql);
            checkPst.setString(1, email);
            ResultSet rs = checkPst.executeQuery();
            
            if (rs.next()) {
                shakeComponent(jPanel7);
                showCustomDialog("This email address is already registered. Please use a different email or login.", "EMAIL EXISTS");
                emailField.requestFocus();
                return;
            }
            
            // Check if username exists
            String checkUsernameSql = "SELECT 1 FROM tbl_users WHERE username = ?";
            PreparedStatement checkUserPst = con.prepareStatement(checkUsernameSql);
            checkUserPst.setString(1, username);
            ResultSet rs2 = checkUserPst.executeQuery();
            
            if (rs2.next()) {
                shakeComponent(jPanel7);
                showCustomDialog("This username is already taken. Please choose another one.", "USERNAME TAKEN");
                usernameField.requestFocus();
                return;
            }
            
            // Insert user with username field
            String sql = "INSERT INTO tbl_users (first_name, last_name, email, username, password, type, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, fname);
            pst.setString(2, lname);
            pst.setString(3, email);
            pst.setString(4, username);
            pst.setString(5, password);
            pst.setString(6, "USER");
            pst.setString(7, "ACTIVE");
            
            pst.executeUpdate();
            
            showCustomDialog("Your account has been created successfully! You can now login.", "REGISTRATION SUCCESSFUL");
            new LoginPage().setVisible(true);
            dispose();
            
        } catch (SQLException ex) {
            showCustomDialog("Database Error: " + ex.getMessage(), "ERROR");
        }
    });
    jPanel7.add(registerBtn);
    
    // Back button
    ModernButton backBtn = new ModernButton("BACK");
    backBtn.setBounds(70, 380, 160, 45);
    backBtn.addActionListener(e -> {
        new Landingpage().setVisible(true);
        dispose();
    });
    jPanel7.add(backBtn);
    
    // Login link
    JLabel loginLabel = new JLabel("Already have an account? LOGIN") {
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
                
                @Override
                public void mouseClicked(MouseEvent e) {
                    new LoginPage().setVisible(true);
                    dispose();
                }
            });
        }
    };
    loginLabel.setFont(new Font("Constantia", Font.BOLD, 13));
    loginLabel.setForeground(new Color(180, 180, 180));
    loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
    loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    loginLabel.setBounds(120, 450, 260, 30);
    jPanel7.add(loginLabel);
    
    // Close button
    CloseButton closeBtn = new CloseButton();
    closeBtn.setBounds(430, 10, 50, 40);
    closeBtn.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent evt) {
            System.exit(0);
        }
    });
    jPanel7.add(closeBtn);
    
    // Decorative line
    JLabel decorLine = new JLabel();
    decorLine.setBounds(70, 500, 360, 2);
    decorLine.setOpaque(true);
    decorLine.setBackground(new Color(255, 215, 0, 80));
    jPanel7.add(decorLine);
    
    // Store references for backward compatibility
    Fn = firstNameField;
    Ln = lastNameField;
    Em = emailField;
    Un = usernameField;
    Ps = passwordField;
    
    // Set up frame
    getContentPane().removeAll();
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(jPanel7, BorderLayout.CENTER);
    
    // Refresh
    revalidate();
    repaint();
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        Ps = new javax.swing.JTextField();
        Fn = new javax.swing.JTextField();
        Ln = new javax.swing.JTextField();
        Em = new javax.swing.JTextField();
        Un = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel7.setBackground(new java.awt.Color(153, 153, 153));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setBackground(new java.awt.Color(51, 51, 51));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(220, 220, 220));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("REGISTRATION FORM");
        jPanel9.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, 260, 60));

        Ps.setBackground(new java.awt.Color(220, 220, 220));
        Ps.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Ps.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "Password", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        jPanel9.add(Ps, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, 280, 40));

        Fn.setBackground(new java.awt.Color(220, 220, 220));
        Fn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Fn.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "First Name", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        jPanel9.add(Fn, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 280, 40));

        Ln.setBackground(new java.awt.Color(220, 220, 220));
        Ln.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Ln.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "Last Name", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        Ln.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LnActionPerformed(evt);
            }
        });
        jPanel9.add(Ln, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 280, 40));

        Em.setBackground(new java.awt.Color(220, 220, 220));
        Em.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Em.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "Email", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        jPanel9.add(Em, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, 280, 40));

        Un.setBackground(new java.awt.Color(220, 220, 220));
        Un.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Un.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "User Name", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        Un.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UnActionPerformed(evt);
            }
        });
        jPanel9.add(Un, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, 280, 40));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(220, 220, 220));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Already have an account? click here to login");
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });
        jPanel9.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 390, -1, -1));

        jButton1.setText("Register");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 330, 90, 30));

        jButton2.setText("Back");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 330, 90, 30));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("X");
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });
        jPanel9.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 0, 60, 50));

        jPanel7.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 450));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String fname = Fn.getText().trim();
    String lname = Ln.getText().trim();
    String email = Em.getText().trim();
    String password = Ps.getText().trim();

    if (fname.isEmpty() || lname.isEmpty() || email.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields");
        return;
    }

    try (Connection con = DataBaseCon.connectDB()) {

        if (con == null) {
            JOptionPane.showMessageDialog(this, "Database connection failed!");
            return;
        }

        // Check if email exists
        String checkEmailSql = "SELECT 1 FROM tbl_users WHERE email = ?";
        PreparedStatement checkPst = con.prepareStatement(checkEmailSql);
        checkPst.setString(1, email);
        ResultSet rs = checkPst.executeQuery();

        if (rs.next()) {
            JOptionPane.showMessageDialog(this, "Email already exists!");
            return;
        }

        // Insert user
        String sql = "INSERT INTO tbl_users (first_name, last_name, email, password, type, status) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, fname);
        pst.setString(2, lname);
        pst.setString(3, email);
        pst.setString(4, password); // must match NOT NULL
        pst.setString(5, "USER");
        pst.setString(6, "ACTIVE");

        pst.executeUpdate();

        JOptionPane.showMessageDialog(this, "Registered successfully!");
        new LoginPage().setVisible(true);
        this.dispose();

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
    }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        this.dispose ();
        LoginPage lp = new LoginPage();
        lp.setVisible(true);
    }//GEN-LAST:event_jLabel8MouseClicked

    private void UnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_UnActionPerformed

    private void LnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LnActionPerformed

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel4MouseClicked


    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Em;
    private javax.swing.JTextField Fn;
    private javax.swing.JTextField Ln;
    private javax.swing.JTextField Ps;
    private javax.swing.JTextField Un;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    // End of variables declaration//GEN-END:variables
}
