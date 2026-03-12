/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leif_hms;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.AbstractBorder;

/**
 *
 * @author User
 */
public class Landingpage extends javax.swing.JFrame {
    
    // Custom colors for hotel theme
    private static final Color PRIMARY_GOLD = new Color(255, 215, 0);
    private static final Color SECONDARY_GOLD = new Color(218, 165, 32);
    private static final Color DARK_PURPLE = new Color(75, 0, 130);
    private static final Color MEDIUM_PURPLE = new Color(128, 0, 128);
    private static final Color LIGHT_PURPLE = new Color(186, 85, 211);
    private static final Color HOVER_GOLD = new Color(255, 223, 0);
    
    // Custom button with glow and hover effects
    private class ModernButton extends JButton {
        private float glowIntensity = 0f;
        private Timer glowTimer;
        private boolean isHovered = false;
        
        public ModernButton(String text) {
            super(text);
            setFont(new Font("Constantia", Font.BOLD, 18));
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
                    setForeground(PRIMARY_GOLD);
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    startGlowAnimation(false);
                    setForeground(Color.WHITE);
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
            
            int w = getWidth();
            int h = getHeight();
            
            // Draw gradient background
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(60, 60, 60),
                w, 0, new Color(80, 80, 80)
            );
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, w, h, 25, 25);
            
            // Draw inner glow
            if (glowIntensity > 0) {
                // Inner border glow
                g2.setColor(new Color(255, 215, 0, (int)(150 * glowIntensity)));
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(2, 2, w-5, h-5, 22, 22);
                
                // Outer glow
                for (int i = 1; i <= 3; i++) {
                    int alpha = (int)(50 * glowIntensity * (1 - i/4f));
                    g2.setColor(new Color(255, 215, 0, alpha));
                    g2.setStroke(new BasicStroke(i));
                    g2.drawRoundRect(i, i, w-2*i-1, h-2*i-1, 25-i, 25-i);
                }
            }
            
            // Draw subtle border
            g2.setColor(new Color(255, 255, 255, 50));
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(0, 0, w-1, h-1, 25, 25);
            
            g2.dispose();
            
            super.paintComponent(g);
        }
    }
    
    // Custom label with hover glow
    private class NavLabel extends JLabel {
        private boolean isHovered = false;
        private float glowIntensity = 0f;
        private Timer glowTimer;
        
        public NavLabel(String text) {
            super(text);
            setFont(new Font("Constantia", Font.BOLD, 18));
            setForeground(new Color(220, 220, 220));
            setHorizontalAlignment(SwingConstants.CENTER);
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
                        255 - (int)(35 * (1 - glowIntensity)), 
                        255 - (int)(35 * (1 - glowIntensity))
                    ));
                } else {
                    glowIntensity = Math.max(0f, glowIntensity - 0.15f);
                    setForeground(new Color(
                        220 + (int)(35 * glowIntensity), 
                        220 + (int)(35 * glowIntensity), 
                        220 + (int)(35 * glowIntensity)
                    ));
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
            if (glowIntensity > 0) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                
                // Draw glow
                g2.setColor(new Color(255, 215, 0, (int)(100 * glowIntensity)));
                for (int i = 1; i <= 2; i++) {
                    g2.drawString(getText(), x, y - i);
                    g2.drawString(getText(), x, y + i);
                    g2.drawString(getText(), x - i, y);
                    g2.drawString(getText(), x + i, y);
                }
                
                g2.dispose();
            }
            super.paintComponent(g);
        }
    }
    
    // Custom close button
    private class CloseButton extends JLabel {
        private boolean isHovered = false;
        
        public CloseButton() {
            super("X");
            setFont(new Font("Arial Black", Font.BOLD, 36));
            setForeground(Color.WHITE);
            setHorizontalAlignment(SwingConstants.CENTER);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    setForeground(new Color(255, 80, 80));
                    setFont(getFont().deriveFont(Font.BOLD, 42f));
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    setForeground(Color.WHITE);
                    setFont(getFont().deriveFont(Font.BOLD, 36f));
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
                g2.fillOval(getWidth()/2 - 22, getHeight()/2 - 22, 44, 44);
                
                g2.dispose();
            }
            super.paintComponent(g);
        }
    }

    /**
     * Creates new form Landingpage
     */
    public Landingpage() {
        initComponents();
        applyModernDesign();
    }
    
    private void applyModernDesign() {
        // Remove all components from jPanel2 first
        jPanel2.removeAll();
        
        // Create enhanced panel with gradient background
        jPanel2 = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create beautiful gradient
                GradientPaint gradient = new GradientPaint(
                    0, 0, MEDIUM_PURPLE,
                    getWidth(), getHeight(), LIGHT_PURPLE
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Add subtle pattern
                g2.setColor(new Color(255, 255, 255, 20));
                g2.setStroke(new BasicStroke(1.5f));
                
                // Draw diagonal lines pattern
                for (int i = -getHeight(); i < getWidth() + getHeight(); i += 40) {
                    g2.drawLine(i, 0, i + getHeight(), getHeight());
                }
                
                // Add corner accents
                g2.setColor(new Color(255, 215, 0, 40));
                g2.setStroke(new BasicStroke(3));
                // Top left corner
                g2.drawLine(10, 10, 50, 10);
                g2.drawLine(10, 10, 10, 50);
                // Bottom right corner
                g2.drawLine(getWidth()-60, getHeight()-10, getWidth()-10, getHeight()-10);
                g2.drawLine(getWidth()-10, getHeight()-60, getWidth()-10, getHeight()-10);
                
                g2.dispose();
            }
        };
        jPanel2.setLayout(null);
        
        // Add navigation labels
        NavLabel homeLabel = new NavLabel("HOME");
        homeLabel.setBounds(10, 10, 100, 40);
        homeLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                // Home action - maybe refresh or stay
            }
        });
        jPanel2.add(homeLabel);
        
        NavLabel aboutLabel = new NavLabel("ABOUT US");
        aboutLabel.setBounds(100, 10, 130, 40);
        aboutLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                dispose();
                new AboutUs().setVisible(true);
            }
        });
        jPanel2.add(aboutLabel);
        
        NavLabel contactLabel = new NavLabel("CONTACT");
        contactLabel.setBounds(250, 10, 100, 40);
        contactLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                dispose();
                new Contact().setVisible(true);
            }
        });
        jPanel2.add(contactLabel);
        
        // Add enhanced welcome text with gold gradient
        JLabel welcomeLabel = new JLabel("WELCOME") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Font font = new Font("Constantia", Font.BOLD, 44);
                g2.setFont(font);
                FontMetrics fm = g2.getFontMetrics();
                
                String text = "WELCOME";
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                
                // Draw shadow
                g2.setColor(new Color(0, 0, 0, 120));
                g2.drawString(text, x + 3, y + 3);
                
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
        welcomeLabel.setBounds(110, 90, 230, 80);
        jPanel2.add(welcomeLabel);
        
        JLabel toLabel = new JLabel("TO") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                Font font = new Font("Constantia", Font.BOLD, 44);
                g2.setFont(font);
                FontMetrics fm = g2.getFontMetrics();
                
                String text = "TO";
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                
                // Draw shadow
                g2.setColor(new Color(0, 0, 0, 100));
                g2.drawString(text, x + 2, y + 2);
                
                // Draw gold text
                g2.setColor(PRIMARY_GOLD);
                g2.drawString(text, x, y);
                
                g2.dispose();
            }
        };
        toLabel.setBounds(180, 160, 90, 80);
        jPanel2.add(toLabel);
        
        JLabel hotelLabel = new JLabel("LEAF HOTEL") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                Font font = new Font("Constantia", Font.BOLD, 44);
                g2.setFont(font);
                FontMetrics fm = g2.getFontMetrics();
                
                String text = "LEAF HOTEL";
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                
                // Draw multiple shadows for depth
                for (int i = 3; i > 0; i--) {
                    g2.setColor(new Color(0, 0, 0, 40 - i*10));
                    g2.drawString(text, x + i, y + i);
                }
                
                // Draw metallic gradient
                GradientPaint gradient = new GradientPaint(
                    0, y-20, PRIMARY_GOLD,
                    0, y+20, SECONDARY_GOLD
                );
                g2.setPaint(gradient);
                g2.drawString(text, x, y);
                
                g2.dispose();
            }
        };
        hotelLabel.setBounds(70, 240, 290, 100);
        jPanel2.add(hotelLabel);
        
        // Add modern buttons
        ModernButton loginBtn = new ModernButton("LOGIN");
        loginBtn.setBounds(20, 430, 180, 50);
        loginBtn.addActionListener(e -> {
            dispose();
            new LoginPage().setVisible(true);
        });
        jPanel2.add(loginBtn);
        
        ModernButton registerBtn = new ModernButton("REGISTER");
        registerBtn.setBounds(230, 430, 180, 50);
        registerBtn.addActionListener(e -> {
            dispose();
            new Registrationform().setVisible(true);
        });
        jPanel2.add(registerBtn);
        
        // Add close button
        CloseButton closeBtn = new CloseButton();
        closeBtn.setBounds(370, 0, 60, 60);
        closeBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                System.exit(0);
            }
        });
        jPanel2.add(closeBtn);
        
        // Update jPanel1 with elegant border
        jPanel1.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_GOLD, 2),
            BorderFactory.createEmptyBorder(3, 3, 3, 3)
        ));
        
        // Re-add jPanel2 to the frame
        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jPanel1, BorderLayout.WEST);
        getContentPane().add(jPanel2, BorderLayout.CENTER);
        
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        Register = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        Login = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/hotel.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 599, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(102, 0, 102));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Constantia", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(220, 220, 220));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("HOME");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, 40));

        jLabel3.setFont(new java.awt.Font("Constantia", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(220, 220, 220));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("ABOUT US");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 110, 40));

        jLabel4.setFont(new java.awt.Font("Constantia", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(220, 220, 220));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("CONTACT");
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, 100, 40));

        Register.setBackground(new java.awt.Color(102, 102, 102));
        Register.setFont(new java.awt.Font("Constantia", 1, 18)); // NOI18N
        Register.setText("REGISTER");
        Register.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RegisterMouseClicked(evt);
            }
        });
        Register.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RegisterActionPerformed(evt);
            }
        });
        jPanel2.add(Register, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 430, 180, 50));

        jLabel5.setFont(new java.awt.Font("Constantia", 1, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("WELCOME");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 110, 230, 80));

        jLabel6.setFont(new java.awt.Font("Constantia", 1, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(225, 225, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("TO");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 170, 90, 110));

        jLabel7.setFont(new java.awt.Font("Constantia", 1, 36)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(225, 225, 0));
        jLabel7.setText("LEAF HOTEL");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 260, 230, 100));

        jLabel8.setFont(new java.awt.Font("Arial Black", 1, 36)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("X");
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 0, 60, 60));

        Login.setBackground(new java.awt.Color(102, 102, 102));
        Login.setFont(new java.awt.Font("Constantia", 1, 18)); // NOI18N
        Login.setText("LOGIN");
        Login.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LoginMouseClicked(evt);
            }
        });
        Login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoginActionPerformed(evt);
            }
        });
        jPanel2.add(Login, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 180, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void RegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RegisterActionPerformed
         this.dispose ();
        Registrationform lp = new Registrationform();
        lp.setVisible(true);    }//GEN-LAST:event_RegisterActionPerformed

    private void RegisterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RegisterMouseClicked

    }//GEN-LAST:event_RegisterMouseClicked

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
         this.dispose ();
        Contact lp = new Contact();
        lp.setVisible(true);       
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        this.dispose ();
        AboutUs lp = new AboutUs();
        lp.setVisible(true);
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
                System.exit(0);
    }//GEN-LAST:event_jLabel8MouseClicked

    private void LoginMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LoginMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_LoginMouseClicked

    private void LoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoginActionPerformed
         this.dispose ();
        LoginPage lp = new LoginPage();
        lp.setVisible(true);
    }//GEN-LAST:event_LoginActionPerformed

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
            java.util.logging.Logger.getLogger(Landingpage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Landingpage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Landingpage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Landingpage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Landingpage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Login;
    private javax.swing.JButton Register;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
