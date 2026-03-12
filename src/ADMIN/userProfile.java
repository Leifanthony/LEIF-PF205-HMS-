package ADMIN;

import config.DataBaseCon;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author Administrator
 */
public class userProfile extends javax.swing.JFrame {
    
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
    
    // Custom value label
    private class ValueLabel extends JLabel {
        public ValueLabel() {
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setForeground(Color.WHITE);
            setBackground(new Color(60, 60, 60));
            setOpaque(true);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
        }
    }

    public userProfile(String email) {
        initComponents();
        applyModernDesign();
        
        System.out.println("Opening profile for: " + email);
        
        if (email == null || email.trim().isEmpty()) {
            showCustomDialog("Invalid user session", "ERROR");
            dispose();
            return;
        }

        loadUserProfile(email);
    }

    // ================= LOAD USER DATA + IMAGE =================
    private void loadUserProfile(String email) {
        String sql =
        "SELECT first_name, last_name, email, type, status, idpicture " +
        "FROM tbl_users " +
        "WHERE LOWER(TRIM(email)) = LOWER(TRIM(?))";

    try (Connection con = DataBaseCon.connectDB();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            txtFirstname.setText(rs.getString("first_name"));
            txtLastname.setText(rs.getString("last_name"));
            txtEmail.setText(rs.getString("email"));
            txtUsertype.setText(rs.getString("type"));
            txtStatus.setText(rs.getString("status"));

            byte[] imgBytes = rs.getBytes("idpicture");
            if (imgBytes != null && imgBytes.length > 0) {
                ImageIcon icon = new ImageIcon(imgBytes);
                Image img = icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
                
                // Create circular image
                BufferedImage circularImg = new BufferedImage(180, 180, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circularImg.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, 180, 180));
                g2.drawImage(img, 0, 0, 180, 180, null);
                g2.dispose();
                
                lbl_photo.setIcon(new ImageIcon(circularImg));
            } else {
                // Create default avatar
                createDefaultAvatar();
            }

        } else {
            showCustomDialog("User not found!", "ERROR");
        }

    } catch (Exception e) {
        showCustomDialog("Error loading profile: " + e.getMessage(), "ERROR");
    }
    }
    
    private void createDefaultAvatar() {
        BufferedImage defaultImg = new BufferedImage(180, 180, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = defaultImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw circle background
        g2.setColor(new Color(80, 80, 80));
        g2.fillOval(0, 0, 180, 180);
        
        // Draw user icon
        g2.setColor(PRIMARY_GOLD);
        g2.setFont(new Font("Arial", Font.BOLD, 80));
        FontMetrics fm = g2.getFontMetrics();
        String text = "U";
        int x = (180 - fm.stringWidth(text)) / 2;
        int y = (180 + fm.getAscent() - fm.getDescent()) / 2;
        g2.drawString(text, x, y - 5);
        
        g2.dispose();
        lbl_photo.setIcon(new ImageIcon(defaultImg));
    }
    
private void applyModernDesign() {
    // Clear content pane
    getContentPane().removeAll();
    getContentPane().setLayout(null);
    
    // Set frame size
    setSize(950, 500);
    setLocationRelativeTo(null);
    
    // Make background completely transparent
    setBackground(new Color(0, 0, 0, 0));
    
    // Create main panel with transparent background
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(null);
    mainPanel.setBounds(0, 0, 950, 500);
    mainPanel.setOpaque(false);
    mainPanel.setBackground(new Color(0, 0, 0, 0));
    
    // Left panel - Profile Image
    JPanel leftPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(40, 40, 40),
                getWidth(), getHeight(), new Color(60, 60, 60)
            );
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            
            g2.setColor(new Color(255, 215, 0, 100));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 23, 23);
            
            g2.dispose();
        }
    };
    leftPanel.setLayout(null);
    leftPanel.setBounds(30, 30, 250, 440);
    leftPanel.setOpaque(false);
    
    // Photo label
    lbl_photo.setBounds(35, 50, 180, 180);
    lbl_photo.setHorizontalAlignment(SwingConstants.CENTER);
    leftPanel.add(lbl_photo);
    
    // Upload button
    ModernButton uploadBtn = new ModernButton("UPLOAD PHOTO");
    uploadBtn.setBounds(35, 250, 180, 40);
    uploadBtn.addActionListener(this::jButton3ActionPerformed);
    leftPanel.add(uploadBtn);
    
    // User type panel
    JPanel typePanel = new JPanel() {
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
    typePanel.setLayout(null);
    typePanel.setBounds(35, 310, 180, 50);
    typePanel.setOpaque(false);
    
    JLabel typeTitle = new JLabel("USER TYPE");
    typeTitle.setFont(new Font("Constantia", Font.BOLD, 10));
    typeTitle.setForeground(PRIMARY_GOLD);
    typeTitle.setBounds(10, 5, 160, 15);
    typePanel.add(typeTitle);
    
    txtUsertype.setFont(new Font("Constantia", Font.BOLD, 14));
    txtUsertype.setForeground(Color.WHITE);
    txtUsertype.setBounds(10, 22, 160, 20);
    typePanel.add(txtUsertype);
    leftPanel.add(typePanel);
    
    // Status panel
    JPanel statusPanel = new JPanel() {
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
    statusPanel.setLayout(null);
    statusPanel.setBounds(35, 370, 180, 50);
    statusPanel.setOpaque(false);
    
    JLabel statusTitle = new JLabel("STATUS");
    statusTitle.setFont(new Font("Constantia", Font.BOLD, 10));
    statusTitle.setForeground(PRIMARY_GOLD);
    statusTitle.setBounds(10, 5, 160, 15);
    statusPanel.add(statusTitle);
    
    txtStatus.setFont(new Font("Constantia", Font.BOLD, 14));
    txtStatus.setForeground(Color.WHITE);
    txtStatus.setBounds(10, 22, 160, 20);
    statusPanel.add(txtStatus);
    leftPanel.add(statusPanel);
    
    mainPanel.add(leftPanel);
    
    // Right panel - Profile Information
    JPanel rightPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(40, 40, 40),
                getWidth(), getHeight(), new Color(60, 60, 60)
            );
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            
            g2.setColor(new Color(255, 215, 0, 100));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 23, 23);
            
            g2.dispose();
        }
    };
    rightPanel.setLayout(null);
    rightPanel.setBounds(310, 30, 610, 440);
    rightPanel.setOpaque(false);
    
    // Title
    JLabel titleLabel = new JLabel("PROFILE INFORMATION") {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            Font font = new Font("Constantia", Font.BOLD, 28);
            g2.setFont(font);
            FontMetrics fm = g2.getFontMetrics();
            
            String text = "PROFILE INFORMATION";
            int x = 20;
            int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            
            g2.setColor(new Color(0, 0, 0, 100));
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
    titleLabel.setBounds(30, 20, 400, 50);
    rightPanel.add(titleLabel);
    
    
    // Form fields
    int startY = 90;
    int labelWidth = 100;
    int fieldWidth = 350;
    int height = 35;
    int spacing = 50;
    
    // First Name
    JLabel fnLabel = new GlowLabel("First Name:");
    fnLabel.setBounds(30, startY, labelWidth, height);
    rightPanel.add(fnLabel);
    
    txtFirstname.setBounds(140, startY, fieldWidth, height);
    rightPanel.add(txtFirstname);
    
    // Last Name
    JLabel lnLabel = new GlowLabel("Last Name:");
    lnLabel.setBounds(30, startY + spacing, labelWidth, height);
    rightPanel.add(lnLabel);
    
    txtLastname.setBounds(140, startY + spacing, fieldWidth, height);
    rightPanel.add(txtLastname);
    
    // Email
    JLabel emailLabel = new GlowLabel("Email:");
    emailLabel.setBounds(30, startY + (spacing * 2), labelWidth, height);
    rightPanel.add(emailLabel);
    
    txtEmail.setBounds(140, startY + (spacing * 2), fieldWidth, height);
    rightPanel.add(txtEmail);
    
    // Buttons
    ModernButton saveBtn = new ModernButton("SAVE CHANGES");
    saveBtn.setBounds(140, startY + (spacing * 3) + 20, 160, 40);
    saveBtn.addActionListener(this::jButton1ActionPerformed);
    rightPanel.add(saveBtn);
    
    ModernButton closeBtn = new ModernButton("CLOSE");
    closeBtn.setBounds(320, startY + (spacing * 3) + 20, 140, 40);
    closeBtn.addActionListener(e -> dispose());
    rightPanel.add(closeBtn);
    
    mainPanel.add(rightPanel);
    
    getContentPane().add(mainPanel);
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
                
                if (title.equals("WELCOME") || title.contains("SUCCESS")) {
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

        jPanel3 = new javax.swing.JPanel();
        lbl_photo = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtUsertype = new javax.swing.JLabel();
        txtStatus = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        txtEmail = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtFirstname = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtLastname = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel3.setBackground(new java.awt.Color(51, 51, 51));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_photo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 0), 2));
        lbl_photo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lbl_photo.setPreferredSize(new java.awt.Dimension(150, 150));
        jPanel3.add(lbl_photo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 150, 150));

        jButton3.setBackground(new java.awt.Color(255, 255, 0));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton3.setText("Upload");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 150, 30));

        jLabel7.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 0));
        jLabel7.setText("User Type:");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, -1, 30));

        jLabel8.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 0));
        jLabel8.setText("Status:");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 50, 30));

        txtUsertype.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtUsertype.setForeground(new java.awt.Color(255, 255, 0));
        jPanel3.add(txtUsertype, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 100, 20));

        txtStatus.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtStatus.setForeground(new java.awt.Color(255, 255, 0));
        jPanel3.add(txtStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 110, 20));

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Cambria", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 0));
        jLabel1.setText("Profile Information");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(56, 0, -1, -1));

        jButton1.setBackground(new java.awt.Color(255, 255, 0));
        jButton1.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(51, 51, 51));
        jButton1.setText("Save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 230, 100, -1));

        jButton2.setBackground(new java.awt.Color(255, 255, 0));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(51, 51, 51));
        jButton2.setText("Close");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 230, 100, 30));

        txtEmail.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtEmail.setForeground(new java.awt.Color(255, 255, 0));
        jPanel2.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 150, 250, 20));

        jLabel5.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 0));
        jLabel5.setText("Email:");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, -1, -1));

        txtFirstname.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtFirstname.setForeground(new java.awt.Color(255, 255, 0));
        jPanel2.add(txtFirstname, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 70, 250, 20));

        jLabel3.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 0));
        jLabel3.setText("First Name:");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, -1));

        txtLastname.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtLastname.setForeground(new java.awt.Color(255, 255, 0));
        jPanel2.add(txtLastname, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 110, 250, 21));

        jLabel4.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 225, 0));
        jLabel4.setText("Last Name:");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, 21));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 5, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      String email = txtEmail.getText();
    String fname = txtFirstname.getText();
    String lname = txtLastname.getText();

   
    String sql = "UPDATE tbl_users SET first_name = ?, last_name = ? WHERE email = ?";

    
    DataBaseCon conf = new DataBaseCon();
    
    try {
        
        conf.addRecord(sql, fname, lname, email);
        
        JOptionPane.showMessageDialog(this, "Saved Successfully!");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Save Error: " + e.getMessage());
    }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("Select Image");
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        File file = chooser.getSelectedFile();

        try {
            BufferedImage bi = ImageIO.read(file);

            if (bi == null) {
                JOptionPane.showMessageDialog(this, "Invalid image file!");
                return;
            }

            // Show image
            Image img = bi.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            lbl_photo.setIcon(new ImageIcon(img));

            // Convert to byte[]
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", baos);
            byte[] imageBytes = baos.toByteArray();

            // DEBUG
            System.out.println("Image bytes length: " + imageBytes.length);
            System.out.println("Saving for email: " + txtEmail.getText());

            // SAVE TO DB
            Connection conn = DataBaseCon.connectDB();
            String sql = "UPDATE tbl_users SET idpicture = ? WHERE email = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setBytes(1, imageBytes);
            pst.setString(2, txtEmail.getText().trim());

            int updated = pst.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Image saved to database!");
            } else {
                JOptionPane.showMessageDialog(this,
                    "NO ROW UPDATED ❌\nCheck email match.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
         
    java.awt.EventQueue.invokeLater(() -> {
        new userProfile("user1").setVisible(true); // must EXIST in tbl_users
    });
    
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
            java.util.logging.Logger.getLogger(userProfile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(userProfile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(userProfile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(userProfile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lbl_photo;
    private javax.swing.JLabel txtEmail;
    private javax.swing.JLabel txtFirstname;
    private javax.swing.JLabel txtLastname;
    private javax.swing.JLabel txtStatus;
    private javax.swing.JLabel txtUsertype;
    // End of variables declaration//GEN-END:variables
}
