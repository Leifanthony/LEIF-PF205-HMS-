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

    public class ManageBooking extends JFrame {

        // Color scheme
        private final Color navcolor = new Color(140, 104, 141);
        private final Color headcolor = new Color(102, 102, 255);
        private final Color bodycolor = new Color(153, 153, 255);

        public ManageBooking() {
            initComponents();
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
            rs.close();

        } catch (SQLException ex) {
            System.out.println("Errors: " + ex.getMessage());
        }
        }

        public void searchTable() {
           DefaultTableModel model = (DefaultTableModel) customer_table.getModel();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(model);
        customer_table.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(search.getText().trim()));
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
        jLabel8.setText("EDIT");
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

    private void editMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editMouseClicked
       int rowIndex = customer_table.getSelectedRow();

    if (rowIndex < 0) {
        JOptionPane.showMessageDialog(this, "Please select a booking first!");
        return;
    }

    String id = customer_table.getValueAt(rowIndex, 0).toString();
    String currentCheckout = customer_table.getValueAt(rowIndex, 10).toString();

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
        JOptionPane.showMessageDialog(this, "Checkout extended successfully!");
        displayData();
    } else {
        JOptionPane.showMessageDialog(this, "Failed to update booking!");
    }
    }//GEN-LAST:event_editMouseClicked

    private void editMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editMouseEntered
      edit.setBackground(bodycolor);
    }//GEN-LAST:event_editMouseEntered

    private void editMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editMouseExited
      edit.setBackground(navcolor);
    }//GEN-LAST:event_editMouseExited

    private void searchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchKeyPressed
searchTable();
    }//GEN-LAST:event_searchKeyPressed

    private void search_buttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_search_buttonMouseClicked
       searchTable();
    }//GEN-LAST:event_search_buttonMouseClicked

    private void customer_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customer_tableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_customer_tableMouseClicked

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked

    }//GEN-LAST:event_jLabel8MouseClicked

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        displayData();
    }//GEN-LAST:event_jLabel7MouseClicked


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
