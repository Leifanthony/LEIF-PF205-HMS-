package ADMIN;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import leif_hms.LoginPage;

public class UserDash1 extends javax.swing.JFrame {

    private String loggedInUser;
    
    // Custom colors for hotel theme
    private static final Color PRIMARY_GOLD = new Color(255, 215, 0);
    private static final Color SECONDARY_GOLD = new Color(218, 165, 32);
    private static final Color DARK_PURPLE = new Color(75, 0, 130);
    private static final Color MEDIUM_PURPLE = new Color(128, 0, 128);
    private static final Color LIGHT_PURPLE = new Color(186, 85, 211);
    private static final Color HOVER_GOLD = new Color(255, 223, 0);
    private static final Color PANEL_DARK = new Color(30, 30, 30);
    private static final Color PANEL_LIGHT = new Color(45, 45, 45);
    
    // Custom menu panel with glow effects
    private class MenuPanel extends JPanel {
        private boolean isHovered = false;
        private float glowIntensity = 0f;
        private Timer glowTimer;
        private String title;
        private Color panelColor;
        private Runnable onClick;
        
        public MenuPanel(String title, Color panelColor, Runnable onClick) {
            this.title = title;
            this.panelColor = panelColor;
            this.onClick = onClick;
            
            setPreferredSize(new Dimension(280, 70));
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
                0, 0, panelColor,
                w, 0, new Color(panelColor.getRed() + 20, panelColor.getGreen() + 20, panelColor.getBlue() + 20)
            );
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, w, h, 25, 25);
            
            // Draw glow effect on hover
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
            
            // Draw subtle border
            g2.setColor(new Color(255, 255, 255, 30));
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(1, 1, w-3, h-3, 23, 23);
            
            // Draw text
            g2.setFont(new Font("Constantia", Font.BOLD, 20));
            FontMetrics fm = g2.getFontMetrics();
            int textX = 20;
            int textY = (h + fm.getAscent() - fm.getDescent()) / 2;
            
            // Draw text shadow
            g2.setColor(new Color(0, 0, 0, 100));
            g2.drawString(title, textX + 2, textY + 2);
            
            // Draw text with gradient on hover
            if (glowIntensity > 0) {
                GradientPaint textGradient = new GradientPaint(
                    0, 0, PRIMARY_GOLD,
                    w, 0, SECONDARY_GOLD
                );
                g2.setPaint(textGradient);
            } else {
                g2.setColor(Color.WHITE);
            }
            g2.drawString(title, textX, textY);
            
            // Draw arrow icon on hover
            if (glowIntensity > 0) {
                g2.setColor(PRIMARY_GOLD);
                g2.setStroke(new BasicStroke(2));
                int arrowX = w - 40;
                int arrowY = h / 2;
                g2.drawLine(arrowX - 10, arrowY - 5, arrowX, arrowY);
                g2.drawLine(arrowX - 10, arrowY + 5, arrowX, arrowY);
                g2.drawLine(arrowX - 10, arrowY, arrowX - 5, arrowY);
            }
            
            g2.dispose();
        }
    }
    
    // Custom header panel
    private class HeaderPanel extends JPanel {
        public HeaderPanel() {
            setPreferredSize(new Dimension(900, 100));
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Create gradient header
            GradientPaint gradient = new GradientPaint(
                0, 0, MEDIUM_PURPLE,
                getWidth(), 0, LIGHT_PURPLE
            );
            g2.setPaint(gradient);
            g2.fillRect(0, 0, getWidth(), getHeight());
            
            // Decorative pattern
            g2.setColor(new Color(255, 215, 0, 40));
            g2.setStroke(new BasicStroke(2));
            
            for (int i = -getHeight(); i < getWidth() + getHeight(); i += 40) {
                g2.drawLine(i, 0, i + getHeight(), getHeight());
            }
            
            // Draw title
            g2.setFont(new Font("Constantia", Font.BOLD, 36));
            FontMetrics fm = g2.getFontMetrics();
            String title = "USER DASHBOARD";
            int x = (getWidth() - fm.stringWidth(title)) / 2;
            int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            
            // Text shadow
            g2.setColor(new Color(0, 0, 0, 120));
            g2.drawString(title, x + 3, y + 3);
            
            // Gradient text
            GradientPaint textGradient = new GradientPaint(
                0, 0, PRIMARY_GOLD,
                getWidth(), 0, SECONDARY_GOLD
            );
            g2.setPaint(textGradient);
            g2.drawString(title, x, y);
            
            g2.dispose();
        }
    }
    
    // Custom right panel with image and gradient
    private class RightPanel extends JPanel {
        public RightPanel() {
            setPreferredSize(new Dimension(400, 500));
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Gradient background
            GradientPaint gradient = new GradientPaint(
                0, 0, PANEL_DARK,
                0, getHeight(), PANEL_LIGHT
            );
            g2.setPaint(gradient);
            g2.fillRect(0, 0, getWidth(), getHeight());
            
            // Draw decorative elements
            g2.setColor(new Color(255, 215, 0, 30));
            g2.setStroke(new BasicStroke(2));
            
            // Draw circles
            for (int i = 0; i < 3; i++) {
                int size = 100 + i * 50;
                g2.drawOval(getWidth()/2 - size/2, getHeight()/2 - size/2, size, size);
            }
            
            // Draw welcome text
            g2.setFont(new Font("Constantia", Font.BOLD, 24));
            g2.setColor(new Color(255, 215, 0, 180));
            String welcome = "WELCOME";
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(welcome)) / 2;
            g2.drawString(welcome, x, 150);
            
            g2.setFont(new Font("Constantia", Font.BOLD, 20));
            String user = loggedInUser != null ? loggedInUser : "Guest";
            if (user.length() > 20) {
                user = user.substring(0, 17) + "...";
            }
            fm = g2.getFontMetrics();
            x = (getWidth() - fm.stringWidth(user)) / 2;
            g2.setColor(Color.WHITE);
            g2.drawString(user, x, 200);
            
            // Draw hotel name
            g2.setFont(new Font("Constantia", Font.BOLD, 32));
            g2.setColor(new Color(255, 215, 0, 100));
            String hotel = "LEAF HOTEL";
            fm = g2.getFontMetrics();
            x = (getWidth() - fm.stringWidth(hotel)) / 2;
            g2.drawString(hotel, x, 350);
            
            g2.dispose();
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
                
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.exit(0);
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
    
public UserDash1(String email) {
    this.loggedInUser = email;
    initComponents();
    applyModernDesign();
    setVisible(true);
}

UserDash1() {
    this.loggedInUser = "Guest";
    initComponents(); // Now includes setUndecorated(true)
    applyModernDesign();
}

private void applyModernDesign() {
    // Clear everything from the content pane
    getContentPane().removeAll();
    getContentPane().setLayout(null); // Set to null layout for absolute positioning
    
    // Set frame size to 730x510
    setSize(730, 510);
    setLocationRelativeTo(null);
    
    // Create main panel with gradient background
    JPanel mainPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            GradientPaint gradient = new GradientPaint(
                0, 0, PANEL_DARK,
                getWidth(), getHeight(), PANEL_LIGHT
            );
            g2.setPaint(gradient);
            g2.fillRect(0, 0, getWidth(), getHeight());
            
            // Add subtle pattern
            g2.setColor(new Color(255, 215, 0, 20));
            g2.setStroke(new BasicStroke(1));
            
            for (int i = 0; i < getWidth(); i += 50) {
                g2.drawLine(i, 0, i, getHeight());
            }
            for (int i = 0; i < getHeight(); i += 50) {
                g2.drawLine(0, i, getWidth(), i);
            }
            
            g2.dispose();
        }
    };
    mainPanel.setLayout(null);
    mainPanel.setBounds(0, 0, 730, 510);
    
    // Add header
    HeaderPanel headerPanel = new HeaderPanel();
    headerPanel.setBounds(0, 0, 730, 70);
    mainPanel.add(headerPanel);
    
    // Add close button
    CloseButton closeBtn = new CloseButton();
    closeBtn.setBounds(680, 15, 40, 30);
    mainPanel.add(closeBtn);
    
    // Create left panel for menu (reduced size)
    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(null);
    leftPanel.setBounds(20, 80, 280, 380);
    leftPanel.setOpaque(false);
    
    // Add menu items (adjusted positions)
    MenuPanel homePanel = new MenuPanel("HOME", new Color(51, 51, 51), () -> {
        JOptionPane.showMessageDialog(this, "Welcome to Home", "Home", JOptionPane.INFORMATION_MESSAGE);
    });
    homePanel.setBounds(20, 20, 240, 60);
    leftPanel.add(homePanel);
    
    MenuPanel profilePanel = new MenuPanel("USER PROFILE", MEDIUM_PURPLE, () -> {
        if (loggedInUser == null || loggedInUser.trim().isEmpty()) {
            showCustomDialog("No logged in user detected.", "ERROR");
            return;
        }
        userProfile profile = new userProfile(loggedInUser);
        profile.setVisible(true);
    });
    profilePanel.setBounds(20, 85, 240, 60);
    leftPanel.add(profilePanel);
    
    MenuPanel bookingPanel = new MenuPanel("BOOKING", DARK_PURPLE, () -> {
        if (loggedInUser == null || loggedInUser.trim().isEmpty()) {
            showCustomDialog("No logged in user detected.", "ERROR");
            return;
        }
        dispose();
        new CustomerBooking(loggedInUser).setVisible(true);
    });
    bookingPanel.setBounds(20, 150, 240, 60);
    leftPanel.add(bookingPanel);
    
    MenuPanel managePanel = new MenuPanel("MANAGE BOOKING", new Color(88, 41, 88), () -> {
        if (loggedInUser == null || loggedInUser.trim().isEmpty()) {
            showCustomDialog("No logged in user detected.", "ERROR");
            return;
        }
        dispose();
        new ManageBooking().setVisible(true);
    });
    managePanel.setBounds(20, 215, 240, 60);
    leftPanel.add(managePanel);
    
    MenuPanel logoutPanel = new MenuPanel("LOGOUT", new Color(139, 0, 0), () -> {
        int choice = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", "Logout", 
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (choice == JOptionPane.YES_OPTION) {
            new LoginPage().setVisible(true);
            dispose();
        }
    });
    logoutPanel.setBounds(20, 280, 240, 60);
    leftPanel.add(logoutPanel);
    
    // Add decorative elements to left panel
    JLabel decorLine1 = new JLabel();
    decorLine1.setBounds(20, 350, 240, 2);
    decorLine1.setOpaque(true);
    decorLine1.setBackground(new Color(255, 215, 0, 100));
    leftPanel.add(decorLine1);
    
    JLabel decorLine2 = new JLabel();
    decorLine2.setBounds(20, 360, 240, 2);
    decorLine2.setOpaque(true);
    decorLine2.setBackground(new Color(255, 215, 0, 50));
    leftPanel.add(decorLine2);
    
    mainPanel.add(leftPanel);
    
    // Create right panel for image (jPanel5 equivalent)
    JPanel rightPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            // Draw elegant frame background
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(40, 40, 40),
                getWidth(), getHeight(), new Color(60, 60, 60)
            );
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            
            // Draw gold border
            g2.setColor(new Color(255, 215, 0, 180));
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 18, 18);
            
            // Draw inner glow
            g2.setColor(new Color(255, 215, 0, 50));
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(5, 5, getWidth()-11, getHeight()-11, 14, 14);
            
            // Draw corner decorations
            g2.setColor(PRIMARY_GOLD);
            g2.setStroke(new BasicStroke(2));
            
            // Top-left corner
            g2.drawLine(10, 10, 30, 10);
            g2.drawLine(10, 10, 10, 30);
            
            // Top-right corner
            g2.drawLine(getWidth()-30, 10, getWidth()-10, 10);
            g2.drawLine(getWidth()-10, 10, getWidth()-10, 30);
            
            // Bottom-left corner
            g2.drawLine(10, getHeight()-30, 10, getHeight()-10);
            g2.drawLine(10, getHeight()-10, 30, getHeight()-10);
            
            // Bottom-right corner
            g2.drawLine(getWidth()-30, getHeight()-10, getWidth()-10, getHeight()-10);
            g2.drawLine(getWidth()-10, getHeight()-30, getWidth()-10, getHeight()-10);
            
            g2.dispose();
        }
    };
    rightPanel.setLayout(null);
    rightPanel.setBounds(310, 80, 390, 380);
    
    // Try to load and display the image
    try {
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/USER.PNG"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(350, 340, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setBounds(20, 20, 350, 340);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        rightPanel.add(imageLabel);
        
        // Add a subtle glow effect behind the image
        JLabel glowEffect = new JLabel();
        glowEffect.setBounds(15, 15, 360, 350);
        glowEffect.setOpaque(true);
        glowEffect.setBackground(new Color(255, 215, 0, 30));
        rightPanel.add(glowEffect);
        rightPanel.setComponentZOrder(glowEffect, 1);
        
    } catch (Exception e) {
        // If image not found, create a beautiful placeholder
        JPanel placeholderPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, MEDIUM_PURPLE,
                    getWidth(), getHeight(), LIGHT_PURPLE
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Draw hotel icon
                g2.setColor(PRIMARY_GOLD);
                g2.setFont(new Font("Constantia", Font.BOLD, 60));
                FontMetrics fm = g2.getFontMetrics();
                String icon = "";
                int x = (getWidth() - fm.stringWidth(icon)) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2 - 20;
                g2.drawString(icon, x, y);
                
                // Draw text
                g2.setFont(new Font("Constantia", Font.BOLD, 18));
                fm = g2.getFontMetrics();
                String text = "LEAF HOTEL";
                x = (getWidth() - fm.stringWidth(text)) / 2;
                y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2 + 30;
                g2.setColor(Color.WHITE);
                g2.drawString(text, x, y);
                
                g2.dispose();
            }
        };
        placeholderPanel.setBounds(20, 20, 350, 340);
        rightPanel.add(placeholderPanel);
    }
    
    // Add user info overlay
    JPanel infoPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            g2.setColor(PRIMARY_GOLD);
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            g2.dispose();
        }
    };
    infoPanel.setLayout(null);
    infoPanel.setBounds(40, 250, 310, 60);
    infoPanel.setOpaque(false);
    
    JLabel userLabel = new JLabel("Logged in as:");
    userLabel.setFont(new Font("Constantia", Font.PLAIN, 12));
    userLabel.setForeground(new Color(200, 200, 200));
    userLabel.setBounds(10, 5, 290, 20);
    infoPanel.add(userLabel);
    
    String displayUser = loggedInUser != null ? loggedInUser : "Guest";
    if (displayUser.length() > 25) {
        displayUser = displayUser.substring(0, 22) + "...";
    }
    
    JLabel emailLabel = new JLabel(displayUser);
    emailLabel.setFont(new Font("Constantia", Font.BOLD, 14));
    emailLabel.setForeground(PRIMARY_GOLD);
    emailLabel.setBounds(10, 25, 290, 25);
    infoPanel.add(emailLabel);
    
    rightPanel.add(infoPanel);
    mainPanel.add(rightPanel);
    
    // Add corner decorations
    addCornerDecorations(mainPanel);
    
    // Add main panel to frame
    getContentPane().add(mainPanel);
    
    // Show welcome message
    if (loggedInUser != null && !loggedInUser.isEmpty() && !loggedInUser.equals("Guest")) {
        SwingUtilities.invokeLater(() -> {
            showCustomDialog("Welcome " + loggedInUser + "!", "WELCOME");
        });
    }
    
    revalidate();
    repaint();
}
 
private void addCornerDecorations(JPanel panel) {
    // Top-left corner
    JLabel tl1 = new JLabel();
    tl1.setBounds(10, 80, 40, 2);
    tl1.setOpaque(true);
    tl1.setBackground(new Color(255, 215, 0, 150));
    panel.add(tl1);
    
    JLabel tl2 = new JLabel();
    tl2.setBounds(10, 80, 2, 40);
    tl2.setOpaque(true);
    tl2.setBackground(new Color(255, 215, 0, 150));
    panel.add(tl2);
    // Bottom-left corner
    JLabel bl1 = new JLabel();
    bl1.setBounds(10, 460, 40, 2);
    bl1.setOpaque(true);
    bl1.setBackground(new Color(255, 215, 0, 150));
    panel.add(bl1);
    
    JLabel bl2 = new JLabel();
    bl2.setBounds(10, 420, 2, 40);
    bl2.setOpaque(true);
    bl2.setBackground(new Color(255, 215, 0, 150));
    panel.add(bl2);
    
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
            
            // Dark background with rounded corners
            g2.setColor(new Color(25, 25, 25, 240));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            
            // Gold border with glow effect
            g2.setColor(new Color(255, 215, 0, 180));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 23, 23);
            
            g2.dispose();
        }
    };
    panel.setLayout(new BorderLayout(15, 15));
    panel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
    panel.setOpaque(false);
    
    // Icon Panel - using simple geometric shapes that display properly
    JPanel iconPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int size = 50;
            int x = (getWidth() - size) / 2;
            int y = 5;
            
            if (title.equals("WELCOME")) {
                // Draw checkmark for welcome
                g2.setColor(PRIMARY_GOLD);
                g2.fillOval(x, y, size, size);
                
                // Draw checkmark in white
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(4));
                
                // Left line of checkmark
                g2.drawLine(x + 12, y + 25, x + 22, y + 35);
                // Right line of checkmark
                g2.drawLine(x + 22, y + 35, x + 38, y + 15);
            } else {
                // Draw exclamation for error/info
                g2.setColor(PRIMARY_GOLD);
                g2.fillOval(x, y, size, size);
                
                // Draw exclamation mark in white
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
    
    // Center panel for title and message
    JPanel centerPanel = new JPanel();
    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
    centerPanel.setOpaque(false);
    
    // Title
    JLabel titleLabel = new JLabel(title);
    titleLabel.setFont(new Font("Constantia", Font.BOLD, 20));
    titleLabel.setForeground(PRIMARY_GOLD);
    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    centerPanel.add(titleLabel);
    
    // Spacing
    centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
    
    // Message - using JLabel with HTML for centering
    String formattedMessage = "<html><div style='text-align: center; width: 250px;'>"
            + "<span style='color: #FFFFFF; font-family: Constantia; font-size: 14px;'>" + message + "</span>"
            + "</div></html>";
    
    JLabel messageLabel = new JLabel(formattedMessage);
    messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    centerPanel.add(messageLabel);
    
    panel.add(centerPanel, BorderLayout.CENTER);
    
    // OK Button - centered
    JButton okButton = new JButton("OK");
    okButton.setFont(new Font("Constantia", Font.BOLD, 15));
    okButton.setForeground(Color.WHITE);
    okButton.setBackground(MEDIUM_PURPLE);
    okButton.setFocusPainted(false);
    okButton.setBorderPainted(false);
    okButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    okButton.setPreferredSize(new Dimension(100, 35));
    
    // Add hover effect
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
    
    // Button panel with centering
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

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(102, 0, 102));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));

        jLabel1.setFont(new java.awt.Font("Constantia", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("HOME");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel1MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 320, 50));

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));

        jLabel4.setFont(new java.awt.Font("Constantia", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 0));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("USER PROFILE");
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel4MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 320, 50));

        jPanel4.setBackground(new java.awt.Color(51, 51, 51));

        jLabel5.setFont(new java.awt.Font("Constantia", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("BOOKING");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel5MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, 320, 50));

        jPanel7.setBackground(new java.awt.Color(51, 51, 51));

        jLabel7.setFont(new java.awt.Font("Constantia", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 0));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("MANAGE BOOKING");
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel7MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel7MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 270, 320, 50));

        jPanel8.setBackground(new java.awt.Color(51, 51, 51));

        jLabel3.setFont(new java.awt.Font("Constantia", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 0));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("LOGOUT");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel3MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, 320, 50));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 370, 430));

        jPanel5.setBackground(new java.awt.Color(51, 51, 51));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/USER.PNG"))); // NOI18N
        jLabel6.setText("jLabel6");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 80, 360, 430));

        jPanel6.setBackground(new java.awt.Color(102, 0, 102));

        jLabel2.setFont(new java.awt.Font("Constantia", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 0));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("USER DASHBOARD");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -10, 730, 90));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    
    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
      if (loggedInUser == null || loggedInUser.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Error: No logged in user detected.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Open user profile
        userProfile profile = new userProfile(loggedInUser);
        profile.setVisible(true);
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
         // Logout
        new LoginPage().setVisible(true);
        this.dispose();          
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseEntered
        jLabel4.setOpaque(true);
        jLabel4.setBackground(new java.awt.Color(240,240,240));
    }//GEN-LAST:event_jLabel4MouseEntered

    private void jLabel4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseExited
        jLabel4.setOpaque(true);
        jLabel4.setBackground(new java.awt.Color(51,51,51));
    }//GEN-LAST:event_jLabel4MouseExited

    private void jLabel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseEntered
        jLabel3.setOpaque(true);
        jLabel3.setBackground(new java.awt.Color(240,240,240));
    }//GEN-LAST:event_jLabel3MouseEntered

    private void jLabel3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseExited
        jLabel3.setOpaque(true);
        jLabel3.setBackground(new java.awt.Color(51,51,51));
    }//GEN-LAST:event_jLabel3MouseExited

    private void jLabel5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseEntered
         jLabel5.setOpaque(true);
        jLabel5.setBackground(new java.awt.Color(240,240,240));
    }//GEN-LAST:event_jLabel5MouseEntered

    private void jLabel7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseEntered
         jLabel7.setOpaque(true);
        jLabel7.setBackground(new java.awt.Color(240,240,240));
    }//GEN-LAST:event_jLabel7MouseEntered

    private void jLabel5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseExited
        jLabel5.setOpaque(true);
        jLabel5.setBackground(new java.awt.Color(51,51,51));
    }//GEN-LAST:event_jLabel5MouseExited

    private void jLabel7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseExited
        jLabel7.setOpaque(true);
        jLabel7.setBackground(new java.awt.Color(51,51,51));
    }//GEN-LAST:event_jLabel7MouseExited

    private void jLabel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseEntered
         jLabel1.setOpaque(true);
        jLabel1.setBackground(new java.awt.Color(240,240,240));
    }//GEN-LAST:event_jLabel1MouseEntered

    private void jLabel1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseExited
        jLabel1.setOpaque(true);
        jLabel1.setBackground(new java.awt.Color(51,51,51));
    }//GEN-LAST:event_jLabel1MouseExited

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
       if (loggedInUser == null || loggedInUser.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "No logged in user detected.");
        return;
    }

    this.dispose();
    new CustomerBooking(loggedInUser).setVisible(true);
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
       if (loggedInUser == null || loggedInUser.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "No logged in user detected.");
        return;
    }

    this.dispose(); // close dashboard
    new ManageBooking().setVisible(true); // open ManageBooking frame
    
    }//GEN-LAST:event_jLabel7MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            // For testing only, pass a dummy email
            new UserDash1("test@example.com").setVisible(true);
        }
    });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    // End of variables declaration//GEN-END:variables
}

