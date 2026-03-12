package ADMIN;

import java.sql.*;
import config.DataBaseCon;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author SCC-MIKE
 */
public class CustomerForm extends javax.swing.JFrame {
    
    // Custom colors for hotel theme
    private static final Color PRIMARY_GOLD = new Color(255, 215, 0);
    private static final Color SECONDARY_GOLD = new Color(218, 165, 32);
    private static final Color DARK_PURPLE = new Color(75, 0, 130);
    private static final Color MEDIUM_PURPLE = new Color(128, 0, 128);
    private static final Color LIGHT_PURPLE = new Color(186, 85, 211);
    private static final Color PANEL_DARK = new Color(30, 30, 30);
    private static final Color PANEL_LIGHT = new Color(45, 45, 45);
    
    public String destination = "";
    File selectedFile;
    public String oldpath;
    public String path;
    public String gender;
    public String action;
    
    // Custom button with glow and hover effects
    private class ModernButton extends JPanel {
        private boolean isHovered = false;
        private float glowIntensity = 0f;
        private Timer glowTimer;
        private String text;
        private Runnable onClick;
        private Color bgColor;
        
        public ModernButton(String text, Color bgColor, Runnable onClick) {
            this.text = text;
            this.bgColor = bgColor;
            this.onClick = onClick;
            setPreferredSize(new Dimension(150, 40));
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
            g2.fillRoundRect(0, 0, w, h, 20, 20);
            
            // Draw glow effect
            if (glowIntensity > 0) {
                g2.setColor(new Color(255, 215, 0, (int)(150 * glowIntensity)));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(2, 2, w-5, h-5, 18, 18);
            }
            
            // Draw text
            g2.setFont(new Font("Constantia", Font.BOLD, 14));
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
                    close();
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
    
    // Custom password field
    private class ModernPasswordField extends JPasswordField {
        private boolean isFocused = false;
        private float glowIntensity = 0f;
        private Timer glowTimer;
        
        public ModernPasswordField() {
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
    
    // Custom combo box
    private class ModernComboBox extends JComboBox<String> {
        public ModernComboBox(String[] items) {
            super(items);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setBackground(new Color(60, 60, 60));
            setForeground(Color.WHITE);
            setBorder(new RoundedCornerBorder(10));
            setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    setBackground(isSelected ? MEDIUM_PURPLE : new Color(60, 60, 60));
                    setForeground(Color.WHITE);
                    setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    setHorizontalAlignment(SwingConstants.CENTER);
                    return this;
                }
            });
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

    public CustomerForm() {
        initComponents();
        applyModernDesign();
    }
    
    private void applyModernDesign() {
        // Clear content pane
        getContentPane().removeAll();
        getContentPane().setLayout(null);
        
        
        // Set frame size
        setSize(900, 620);
        setLocationRelativeTo(null);
        
        // Make background completely transparent
        setBackground(new Color(0, 0, 0, 0));
        
        // Create main panel with transparent background
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBounds(0, 0, 900, 620);
        mainPanel.setOpaque(false);
        
        // Create content panel
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
        contentPanel.setBounds(20, 20, 860, 560);
        contentPanel.setOpaque(false);
        
        // Title
        JLabel titleLabel = new JLabel("CUSTOMER FORM") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                Font font = new Font("Constantia", Font.BOLD, 28);
                g2.setFont(font);
                FontMetrics fm = g2.getFontMetrics();
                
                String text = "CUSTOMER FORM";
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
        titleLabel.setBounds(20, 15, 300, 50);
        contentPanel.add(titleLabel);
        
        // Close button
        CloseButton closeBtn = new CloseButton();
        closeBtn.setBounds(800, 15, 40, 30);
        contentPanel.add(closeBtn);
        
        // Left side - Form fields
        int labelX = 40;
        int fieldX = 150;
        int startY = 90;
        int spacing = 55;
        int fieldWidth = 250;
        
        // Customer ID
        GlowLabel idLabel = new GlowLabel("Customer ID:");
        idLabel.setBounds(labelX, startY, 100, 25);
        contentPanel.add(idLabel);
        
        st_id.setBounds(fieldX, startY, fieldWidth, 35);
        ((JTextField)st_id).setBorder(new RoundedCornerBorder(10));
        contentPanel.add(st_id);
        
        // First Name
        GlowLabel fnameLabel = new GlowLabel("First Name:");
        fnameLabel.setBounds(labelX, startY + spacing, 100, 25);
        contentPanel.add(fnameLabel);
        
        st_fname.setBounds(fieldX, startY + spacing, fieldWidth, 35);
        ((JTextField)st_fname).setBorder(new RoundedCornerBorder(10));
        contentPanel.add(st_fname);
        
        // Last Name
        GlowLabel lnameLabel = new GlowLabel("Last Name:");
        lnameLabel.setBounds(labelX, startY + (spacing * 2), 100, 25);
        contentPanel.add(lnameLabel);
        
        st_lname.setBounds(fieldX, startY + (spacing * 2), fieldWidth, 35);
        ((JTextField)st_lname).setBorder(new RoundedCornerBorder(10));
        contentPanel.add(st_lname);
        
        // Email
        GlowLabel emailLabel = new GlowLabel("Email:");
        emailLabel.setBounds(labelX, startY + (spacing * 3), 100, 25);
        contentPanel.add(emailLabel);
        
        st_email.setBounds(fieldX, startY + (spacing * 3), fieldWidth, 35);
        ((JTextField)st_email).setBorder(new RoundedCornerBorder(10));
        contentPanel.add(st_email);
        
        // Password
        GlowLabel passLabel = new GlowLabel("Password:");
        passLabel.setBounds(labelX, startY + (spacing * 4), 100, 25);
        contentPanel.add(passLabel);
        
        jPasswordField1.setBounds(fieldX, startY + (spacing * 4), fieldWidth, 35);
        ((JPasswordField)jPasswordField1).setBorder(new RoundedCornerBorder(10));
        contentPanel.add(jPasswordField1);
        
        // Type
        GlowLabel typeLabel = new GlowLabel("Type:");
        typeLabel.setBounds(labelX, startY + (spacing * 5), 100, 25);
        contentPanel.add(typeLabel);
        
        st_status1.setBounds(fieldX, startY + (spacing * 5), fieldWidth, 35);
        ((JComboBox)st_status1).setBorder(new RoundedCornerBorder(10));
        contentPanel.add(st_status1);
        
        // Status
        GlowLabel statusLabel = new GlowLabel("Status:");
        statusLabel.setBounds(labelX, startY + (spacing * 6), 100, 25);
        contentPanel.add(statusLabel);
        
        st_status.setBounds(fieldX, startY + (spacing * 6), fieldWidth, 35);
        ((JComboBox)st_status).setBorder(new RoundedCornerBorder(10));
        contentPanel.add(st_status);
        
        // Right side - Image panel
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(50, 50, 50),
                    getWidth(), getHeight(), new Color(70, 70, 70)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                g2.setColor(new Color(255, 215, 0, 100));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 18, 18);
                
                g2.dispose();
            }
        };
        imagePanel.setLayout(null);
        imagePanel.setBounds(450, 70, 350, 380);
        imagePanel.setOpaque(false);
        
        // Image title
        JLabel imgTitle = new JLabel("PROFILE IMAGE");
        imgTitle.setFont(new Font("Constantia", Font.BOLD, 16));
        imgTitle.setForeground(PRIMARY_GOLD);
        imgTitle.setBounds(120, 10, 150, 25);
        imagePanel.add(imgTitle);
        
        // Image label
        image.setBounds(70, 40, 210, 180);
        image.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        image.setHorizontalAlignment(SwingConstants.CENTER);
        imagePanel.add(image);
        
        // Browse button
        ModernButton browseBtn = new ModernButton("BROWSE", MEDIUM_PURPLE, () -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg");
            fileChooser.setFileFilter(filter);
            
            int returnValue = fileChooser.showOpenDialog(null);
            
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                path = selectedFile.getAbsolutePath();
                destination = "src/images/" + selectedFile.getName();
                
                try {
                    if (FileExistenceChecker(path) == 1) {
                        showCustomDialog("File already exists. Please rename or choose another!", "WARNING");
                        destination = "";
                        path = "";
                    } else {
                        ImageIcon icon = new ImageIcon(path);
                        Image img = icon.getImage().getScaledInstance(210, 180, Image.SCALE_SMOOTH);
                        image.setIcon(new ImageIcon(img));
                    }
                } catch (Exception ex) {
                    System.out.println("File Error!");
                }
            }
        });
        browseBtn.setBounds(50, 240, 250, 40);
        imagePanel.add(browseBtn);
        
        // Remove button
        ModernButton removeBtn = new ModernButton("REMOVE", new Color(139, 0, 0), () -> {
            image.setIcon(null);
            destination = "";
            path = "";
        });
        removeBtn.setBounds(50, 300, 250, 40);
        imagePanel.add(removeBtn);
        
        contentPanel.add(imagePanel);
        
        // Buttons panel at the bottom
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBounds(450, 470, 350, 60);
        buttonPanel.setOpaque(false);

        // Save button
        ModernButton saveBtn = new ModernButton("SAVE", MEDIUM_PURPLE, () -> {
            saveRecord();
        });
        saveBtn.setBounds(30, 10, 130, 40);
        buttonPanel.add(saveBtn);

        // Cancel button
        ModernButton cancelBtn = new ModernButton("CANCEL", new Color(100, 100, 100), () -> {
            close();
        });
        cancelBtn.setBounds(190, 10, 130, 40);
        buttonPanel.add(cancelBtn);

        // Add button panel to content panel
        contentPanel.add(buttonPanel);

        // Update references
        add = saveBtn;
        st_label.setText("");
        
        // ===== IMPORTANT: Add contentPanel to mainPanel =====
        mainPanel.add(contentPanel);  // <-- ADD THIS LINE HERE
        
        // Add main panel to frame
        getContentPane().add(mainPanel);

        revalidate();
        repaint();
    }
    
    private void saveRecord() {
        String idText = st_id.getText().trim();
        int id = -1;

        if (!idText.isEmpty()) {
            try {
                id = Integer.parseInt(idText);
            } catch (NumberFormatException e) {
                showCustomDialog("Invalid Customer ID!", "ERROR");
                return;
            }
        }

        try {
            Connection conn = DataBaseCon.connectDB();

            if ("Add".equals(action)) {
                String sql = "INSERT INTO tbl_users(first_name, last_name, email, password, type, status) VALUES(?,?,?,?,?,?)";
                PreparedStatement pst = conn.prepareStatement(sql);

                pst.setString(1, st_fname.getText());
                pst.setString(2, st_lname.getText());
                pst.setString(3, st_email.getText());
                pst.setString(4, new String(jPasswordField1.getPassword()));
                pst.setString(5, st_status1.getSelectedItem().toString());
                pst.setString(6, st_status.getSelectedItem().toString());

                pst.executeUpdate();
                showCustomDialog("Record Added Successfully!", "SUCCESS");
                pst.close();
            }
            else if ("Update".equals(action)) {
                if (id == -1) {
                    showCustomDialog("No customer selected!", "ERROR");
                    return;
                }

                String sql = "UPDATE tbl_users SET first_name=?, last_name=?, email=?, password=?, type=?, status=? WHERE id=?";
                PreparedStatement pst = conn.prepareStatement(sql);

                pst.setString(1, st_fname.getText());
                pst.setString(2, st_lname.getText());
                pst.setString(3, st_email.getText());
                pst.setString(4, new String(jPasswordField1.getPassword()));
                pst.setString(5, st_status1.getSelectedItem().toString());
                pst.setString(6, st_status.getSelectedItem().toString());
                pst.setInt(7, id);

                int rows = pst.executeUpdate();

                if (rows > 0) {
                    showCustomDialog("Record Updated Successfully!", "SUCCESS");
                } else {
                    showCustomDialog("Update Failed!", "ERROR");
                }

                pst.close();
            } else {
                showCustomDialog("Action not set!", "ERROR");
            }

            conn.close();

        } catch (Exception e) {
            showCustomDialog("Database Error: " + e.getMessage(), "ERROR");
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
    
    public void imageUpdater(String existingFilePath, String newFilePath) {
        File existingFile = new File(existingFilePath);
        if (existingFile.exists()) {
            String parentDirectory = existingFile.getParent();
            File newFile = new File(newFilePath);
            String newFileName = newFile.getName();
            File updatedFile = new File(parentDirectory, newFileName);
            existingFile.delete();
            try {
                Files.copy(newFile.toPath(), updatedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image updated successfully.");
            } catch (IOException e) {
                System.out.println("Error occurred while updating the image: " + e);
            }
        } else {
            try {
                Files.copy(selectedFile.toPath(), new File(destination).toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.println("Error on update!");
            }
        }
    }
    
    public static int getHeightFromWidth(String imagePath, int desiredWidth) {
        try {
            File imageFile = new File(imagePath);
            BufferedImage image = ImageIO.read(imageFile);
            
            int originalWidth = image.getWidth();
            int originalHeight = image.getHeight();
            
            int newHeight = (int) ((double) desiredWidth / originalWidth * originalHeight);
            
            return newHeight;
        } catch (IOException ex) {
            System.out.println("No image found!");
        }
        
        return -1;
    }
    
    public ImageIcon ResizeImage(String imagePath, byte[] pic, JLabel label) {
        ImageIcon myImage;
        
        if (imagePath != null) {
            myImage = new ImageIcon(imagePath);
        } else {
            myImage = new ImageIcon(pic);
        }
        
        Image img = myImage.getImage();
        int labelWidth = label.getWidth();
        int labelHeight = label.getHeight();
        Image newImg = img.getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);
        
        return new ImageIcon(newImg);
    }
    
    public int FileExistenceChecker(String path) {
        File file = new File(path);
        String fileName = file.getName();
        Path filePath = Paths.get("src/images", fileName);
        boolean fileExists = Files.exists(filePath);
        
        return fileExists ? 1 : 0;
    }
    
public void close() {
    this.dispose();
    CustomerPage customerPage = new CustomerPage();
    customerPage.setVisible(true);
}


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        canvas1 = new java.awt.Canvas();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        close = new javax.swing.JLabel();
        add = new javax.swing.JPanel();
        st_label = new javax.swing.JLabel();
        st_email = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        st_fname = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        st_lname = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        st_status = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        st_id = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        image = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        browse = new javax.swing.JLabel();
        st_status1 = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        browse1 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(104, 0, 104));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(153, 153, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jPanel2.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel1.setText("CUSTOMER FORM");
        jPanel2.add(jLabel1);
        jLabel1.setBounds(20, 10, 300, 40);

        close.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        close.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        close.setText("X");
        close.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                closeMouseClicked(evt);
            }
        });
        jPanel2.add(close);
        close.setBounds(630, 10, 40, 40);

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 680, 60));

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

        st_label.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        st_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        st_label.setText("SAVE");
        st_label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                st_labelMouseClicked(evt);
            }
        });
        add.add(st_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 150, 20));

        jPanel1.add(add, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 400, 210, 40));

        st_email.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        st_email.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        st_email.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        st_email.setOpaque(false);
        jPanel1.add(st_email, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 240, 210, 30));

        jLabel6.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 0));
        jLabel6.setText("Customer ID:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, 90, 30));

        jLabel7.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 0));
        jLabel7.setText("First Name:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, 90, 30));

        st_fname.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        st_fname.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        st_fname.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        st_fname.setOpaque(false);
        jPanel1.add(st_fname, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 140, 210, 30));

        jLabel8.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 0));
        jLabel8.setText("Status:");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, 50, 30));

        st_lname.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        st_lname.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        st_lname.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        st_lname.setOpaque(false);
        jPanel1.add(st_lname, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 190, 210, 30));

        jLabel10.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 0));
        jLabel10.setText("Last Name:");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 190, 90, 30));

        st_status.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        st_status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ACTIVE", "INACTIVE" }));
        st_status.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                st_statusActionPerformed(evt);
            }
        });
        jPanel1.add(st_status, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 400, 210, 30));

        jLabel11.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 0));
        jLabel11.setText("Password:");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 290, 70, 30));

        st_id.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        st_id.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        st_id.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        st_id.setEnabled(false);
        st_id.setOpaque(false);
        jPanel1.add(st_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 90, 210, 30));

        jPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel3MouseClicked(evt);
            }
        });

        image.setBackground(new java.awt.Color(153, 153, 255));
        image.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        image.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        image.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imageMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(image, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(image, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 160, 210, 180));

        jLabel13.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 0));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Image");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 110, 220, 30));

        browse.setBackground(new java.awt.Color(153, 153, 255));
        browse.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        browse.setForeground(new java.awt.Color(255, 255, 0));
        browse.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        browse.setText("BROWSE");
        browse.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                browseMouseClicked(evt);
            }
        });
        jPanel1.add(browse, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 360, 210, 20));

        st_status1.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        st_status1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ADMIN", "USER" }));
        st_status1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                st_status1ActionPerformed(evt);
            }
        });
        jPanel1.add(st_status1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 340, 210, 30));

        jLabel15.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 0));
        jLabel15.setText("Type:");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 340, 90, 30));

        browse1.setBackground(new java.awt.Color(153, 153, 255));
        browse1.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        browse1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        browse1.setText("SELECT");
        browse1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                browse1MouseClicked(evt);
            }
        });
        jPanel1.add(browse1, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 340, 210, 20));

        jLabel12.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 0));
        jLabel12.setText("Email:");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, 70, 30));
        jPanel1.add(jPasswordField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 290, 210, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void addMouseEntered(java.awt.event.MouseEvent evt) {                                 
        // Handled in modern design
    }                                

    private void addMouseExited(java.awt.event.MouseEvent evt) {                                
        // Handled in modern design
    }                               

    private void closeMouseClicked(java.awt.event.MouseEvent evt) {                                   
        close();
    }                                  

    private void addMouseClicked(java.awt.event.MouseEvent evt) {                                 
        // Handled in modern design
    }                                

    private void jPanel3MouseClicked(java.awt.event.MouseEvent evt) {                                     
        // Handled in modern design
    }                                    

    private void browseMouseClicked(java.awt.event.MouseEvent evt) {                                    
        // Handled in modern design
    }                                   

    private void imageMouseClicked(java.awt.event.MouseEvent evt) {                                   
        // Handled in modern design
    }                                  

    private void st_labelMouseClicked(java.awt.event.MouseEvent evt) {                                      
        // Handled in modern design
    }                                     

    private void st_statusActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // Handled in modern design
    }                                         

    private void st_status1ActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // Handled in modern design
    }                                          

    private void browse1MouseClicked(java.awt.event.MouseEvent evt) {                                     
        // Handled in modern design
    }                                    

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CustomerForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel add;
    public javax.swing.JLabel browse;
    private javax.swing.JLabel browse1;
    private java.awt.Canvas canvas1;
    private javax.swing.JLabel close;
    public javax.swing.JLabel image;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPasswordField jPasswordField1;
    public javax.swing.JTextField st_email;
    public javax.swing.JTextField st_fname;
    public javax.swing.JTextField st_id;
    public javax.swing.JLabel st_label;
    public javax.swing.JTextField st_lname;
    public javax.swing.JComboBox<String> st_status;
    public javax.swing.JComboBox<String> st_status1;
    // End of variables declaration//GEN-END:variables
}
