    package ADMIN;

    import config.DataBaseCon;
    import java.awt.Color;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import javax.swing.JFrame;
    import javax.swing.JOptionPane;
    import javax.swing.RowFilter;
    import javax.swing.SwingUtilities;
    import javax.swing.table.DefaultTableModel;
    import javax.swing.table.TableModel;
    import javax.swing.table.TableRowSorter;
    import net.proteanit.sql.DbUtils;

    import javax.swing.JTextArea;


    public class CustomerDetailBills extends JFrame {

        // Color scheme
        private final Color navcolor = new Color(140, 104, 141);
        private final Color headcolor = new Color(102, 102, 255);
        private final Color bodycolor = new Color(153, 153, 255);

        public CustomerDetailBills() {
            initComponents();
            displayData();
            System.out.println("CustomerPage constructor called");
        }

        public void displayData() {
            try {
        DataBaseCon dbc = new DataBaseCon();
        ResultSet rs = dbc.getData(
            "SELECT id, room_number, name, mobile_no, nationality, gender, email, " +
            "checkin_date, checkout_date, bed_type, room_type, price, status " +
            "FROM customer_checkin WHERE status='CHECKED OUT'"
        );

        customer_table.setModel(DbUtils.resultSetToTableModel(rs));
        rs.close();

    } catch (SQLException ex) {
        System.out.println("Errors: " + ex.getMessage());
    }
        }

        public void searchTable() {
            try {
    DataBaseCon dbc = new DataBaseCon();
    String date = search.getText().trim();

    String query = "SELECT id, room_number, name, mobile_no, nationality, gender, email, " +
                   "checkin_date, checkout_date, bed_type, room_type, price, status " +
                   "FROM customer_checkin WHERE checkout_date = '" + date + "'";

    ResultSet rs = dbc.getData(query);
    customer_table.setModel(DbUtils.resultSetToTableModel(rs));

} catch (Exception e) {
    JOptionPane.showMessageDialog(this, e.getMessage());
}
        }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        customer_table = new javax.swing.JTable();
        refresh = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        search = new javax.swing.JTextField();
        search_button = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();

        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(104, 0, 104));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));
        jPanel2.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 0));
        jLabel1.setText("CUSTOMER DETAIL BILLS");
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

        jLabel10.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 0));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("REFRESH");
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
        });
        refresh.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 30));

        jPanel1.add(refresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 80, 30));

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

        jLabel7.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 0));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("SEARCH BY CHECK OUT DATE");
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 80, 220, 30));

        jPanel3.setBackground(new java.awt.Color(140, 104, 141));

        jLabel11.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 0));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("BACK");
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 80, 30));

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
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void refreshMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshMouseEntered
        refresh.setBackground(bodycolor);
    }//GEN-LAST:event_refreshMouseEntered

    private void refreshMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshMouseExited
        refresh.setBackground(navcolor);
    }//GEN-LAST:event_refreshMouseExited

    private void search_buttonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_search_buttonMouseEntered
        search_button.setBackground(bodycolor);
    }//GEN-LAST:event_search_buttonMouseEntered

    private void search_buttonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_search_buttonMouseExited
        search_button.setBackground(navcolor);
    }//GEN-LAST:event_search_buttonMouseExited

    private void refreshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshMouseClicked
       displayData();
    }//GEN-LAST:event_refreshMouseClicked

    private void searchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchKeyPressed
if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
        searchTable();
    }
    }//GEN-LAST:event_searchKeyPressed

    private void search_buttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_search_buttonMouseClicked
       searchTable();
    }//GEN-LAST:event_search_buttonMouseClicked

    private void customer_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customer_tableMouseClicked
       if (evt.getClickCount() != 2) return;
        int row = customer_table.getSelectedRow();    if (row < 0) return;

    String id = customer_table.getValueAt(row, 0).toString();
    String room = customer_table.getValueAt(row, 1).toString();
    String name = customer_table.getValueAt(row, 2).toString();
    String mobile = customer_table.getValueAt(row, 3).toString();
    String nationality = customer_table.getValueAt(row, 4).toString();
    String gender = customer_table.getValueAt(row, 5).toString();
    String email = customer_table.getValueAt(row, 6).toString();
    String checkin = customer_table.getValueAt(row, 7).toString();
    String checkout = customer_table.getValueAt(row, 8).toString();
    String bed = customer_table.getValueAt(row, 9).toString();
    String roomType = customer_table.getValueAt(row, 10).toString();
    String price = customer_table.getValueAt(row, 11).toString();

    String receipt =
            "              HOTEL RECEIPT\n" +
            "------------------------------------------\n" +
            "Customer ID: " + id + "\n" +
            "Name: " + name + "\n" +
            "Mobile: " + mobile + "\n" +
            "Email: " + email + "\n" +
            "Nationality: " + nationality + "\n" +
            "Gender: " + gender + "\n" +
            "Room Number: " + room + "\n" +
            "Room Type: " + roomType + "\n" +
            "Bed Type: " + bed + "\n" +
            "Check-in: " + checkin + "\n" +
            "Check-out: " + checkout + "\n" +
            "------------------------------------------\n" +
            "Total Price: ₱" + price + "\n" +
            "------------------------------------------\n" +
            "      Thank you for staying with us!";

    JTextArea textArea = new JTextArea(receipt);
    textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));

    try {

    int confirm = JOptionPane.showConfirmDialog(
            this,
            "Print this receipt?",
            "Confirm",
            JOptionPane.YES_NO_OPTION);

    if (confirm != JOptionPane.YES_OPTION) return;

    textArea.print();   // Print only if YES selected

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
    }//GEN-LAST:event_customer_tableMouseClicked

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        displayData();
    }//GEN-LAST:event_jLabel7MouseClicked

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        displayData(); 
    }//GEN-LAST:event_jLabel10MouseClicked

    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseClicked
       
    }//GEN-LAST:event_jLabel11MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable customer_table;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel refresh;
    private javax.swing.JTextField search;
    private javax.swing.JPanel search_button;
    // End of variables declaration//GEN-END:variables
}
