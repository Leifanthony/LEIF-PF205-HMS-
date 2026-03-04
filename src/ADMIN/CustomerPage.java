    package ADMIN;

    import config.DataBaseCon;
    import java.awt.Color;
    import java.sql.ResultSet;
    import java.sql.SQLException;
import java.util.regex.Pattern;
    import javax.swing.JFrame;
    import javax.swing.JOptionPane;
    import javax.swing.RowFilter;
    import javax.swing.SwingUtilities;
    import javax.swing.table.DefaultTableModel;
    import javax.swing.table.TableModel;
    import javax.swing.table.TableRowSorter;
    import net.proteanit.sql.DbUtils;

    public class CustomerPage extends JFrame {

        // Color scheme
        private final Color navcolor = new Color(140, 104, 141);
        private final Color headcolor = new Color(102, 102, 255);
        private final Color bodycolor = new Color(153, 153, 255);

        public CustomerPage() {
            initComponents();
            displayData();
            System.out.println("CustomerPage constructor called");
        }

        public void displayData() {
           try {
        DataBaseCon dbc = new DataBaseCon();
        ResultSet rs = dbc.getData(
            "SELECT id, first_name, last_name, email, type, status FROM tbl_users"
        );

        customer_table.setModel(DbUtils.resultSetToTableModel(rs));
        rs.close();

    } catch (SQLException ex) {
        ex.printStackTrace(); // show real error
    }
        }

        public void searchTable() {
            DefaultTableModel model = (DefaultTableModel) customer_table.getModel();
    TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(model);
    customer_table.setRowSorter(tr);

    // ✅ Safe search handling
    String text = search.getText().trim();
    if (text.length() == 0) {
        tr.setRowFilter(null);
    } else {
        tr.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text)));
    }
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
        add = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        edit = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        delete = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
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
        jLabel1.setText("CUSTOMER PAGE");
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

        jLabel6.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("ADD");
        add.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 60, 30));

        jPanel1.add(add, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 80, 30));

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
        jLabel8.setText("UPDATE");
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });
        edit.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 60, 30));

        jPanel1.add(edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, 80, 30));

        delete.setBackground(new java.awt.Color(140, 104, 141));
        delete.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        delete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                deleteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                deleteMouseExited(evt);
            }
        });
        delete.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 0));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("DELETE");
        delete.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 60, 30));

        jPanel1.add(delete, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 80, 80, 30));

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
        refresh.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 30));

        jPanel1.add(refresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 80, 80, 30));

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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
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

    private void addMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addMouseClicked
            // Open CustomerForm without closing main page
    CustomerForm ctf = new CustomerForm();
    ctf.setVisible(true);
    ctf.action = "Add";
    ctf.st_label.setText("SAVE");
    ctf.browse.setVisible(false);
    }//GEN-LAST:event_addMouseClicked

    private void addMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addMouseEntered
        add.setBackground(bodycolor);
    }//GEN-LAST:event_addMouseEntered

    private void addMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addMouseExited
        add.setBackground(navcolor);
    }//GEN-LAST:event_addMouseExited

    private void deleteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteMouseEntered
       delete.setBackground(bodycolor);
    }//GEN-LAST:event_deleteMouseEntered

    private void deleteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteMouseExited
        delete.setBackground(navcolor);
    }//GEN-LAST:event_deleteMouseExited

    private void refreshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshMouseClicked
       displayData();
    }//GEN-LAST:event_refreshMouseClicked

    private void editMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editMouseClicked
       int viewRow = customer_table.getSelectedRow();

    if (viewRow < 0) {
        JOptionPane.showMessageDialog(null, "Please Select an Item!");
        return;
    }

    int modelRow = customer_table.convertRowIndexToModel(viewRow);
    TableModel model = customer_table.getModel();
    String id = model.getValueAt(modelRow, 0).toString();

    CustomerForm stf = new CustomerForm();
stf.setVisible(true);

try {
    DataBaseCon dbc = new DataBaseCon();
    ResultSet rs = dbc.getData("SELECT * FROM tbl_users WHERE id = " + id);

    if (rs.next()) {

    stf.st_id.setText(rs.getString("id"));
    stf.st_fname.setText(rs.getString("first_name"));
    stf.st_lname.setText(rs.getString("last_name"));

    String email = rs.getString("email");
    stf.st_email.setText(email != null ? email : "");

    stf.st_status1.setSelectedItem(rs.getString("type"));
    stf.st_status.setSelectedItem(rs.getString("status"));

    try {
        byte[] imgBytes = rs.getBytes("idpicture");
        if (imgBytes != null && imgBytes.length > 0) {
            stf.image.setIcon(stf.ResizeImage(null, imgBytes, stf.image));
        } else {
            stf.image.setIcon(null);
        }
    } catch (Exception e) {
        stf.image.setIcon(null);
    }

    stf.action = "Update";
    stf.st_label.setText("UPDATE");
    }

    rs.close();

} catch (SQLException e) {
    e.printStackTrace();
}
    }//GEN-LAST:event_editMouseClicked

    private void editMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editMouseEntered
      edit.setBackground(bodycolor);
    }//GEN-LAST:event_editMouseEntered

    private void editMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editMouseExited
      edit.setBackground(navcolor);
    }//GEN-LAST:event_editMouseExited

    private void deleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteMouseClicked
        int viewRow = customer_table.getSelectedRow();
    if (viewRow < 0) {
        JOptionPane.showMessageDialog(null, "Please select data first from the table!");
        return;
    }

    int modelRow = customer_table.convertRowIndexToModel(viewRow);
    TableModel model = customer_table.getModel();
    int id = Integer.parseInt(model.getValueAt(modelRow, 0).toString()); // ensure INTEGER type

    int confirm = JOptionPane.showConfirmDialog(null,
        "Are you sure to delete ID: " + id,
        "Confirm Delete",
        JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {
        boolean success = DataBaseCon.deleteData("tbl_users", "id", id); // type-safe delete
        if(success){
            displayData();
            JOptionPane.showMessageDialog(null, "Record Deleted Successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "Failed to delete record!");
        }
    }
    }//GEN-LAST:event_deleteMouseClicked

    private void searchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchKeyPressed
searchTable();
    }//GEN-LAST:event_searchKeyPressed

    private void search_buttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_search_buttonMouseClicked
       searchTable();
    }//GEN-LAST:event_search_buttonMouseClicked

    private void customer_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customer_tableMouseClicked
        
    }//GEN-LAST:event_customer_tableMouseClicked

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked

    }//GEN-LAST:event_jLabel8MouseClicked

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        displayData();
    }//GEN-LAST:event_jLabel7MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel add;
    private javax.swing.JTable customer_table;
    private javax.swing.JPanel delete;
    private javax.swing.JPanel edit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel refresh;
    private javax.swing.JTextField search;
    private javax.swing.JPanel search_button;
    // End of variables declaration//GEN-END:variables
}
