import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class Products extends javax.swing.JFrame {

    private Connection conn;
    private DefaultTableModel model;

    public Products() {
        initComponents();
        connectDatabase();  // Initialize database connection
        populateTable();    // Populate the table on startup
    }

    // Method to establish database connection
    private void connectDatabase() {
        try {
            //Class.forName("oracle.jdbc.driver.OracleDriver");
            // Change these to your database connection details
            String url = "jdbc:oracle:thin:@localhost:1521:xe";
            String user = "system";
            String password = "hr";

            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to populate the table with data from the database
    private void populateTable() {
        try {
            String query = "SELECT * FROM products";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);  // Clear existing rows
            while (rs.next()) {
                Object[] row = new Object[4];  // Adjust this based on the number of columns
                row[0] = rs.getInt("id");
                row[1] = rs.getString("name");
                row[2] = rs.getDouble("price");
                row[3] = rs.getString("status");
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to add a new product to the database
    private void addProduct() {
    try {
        // Validate fields
        if (txtid.getText().isEmpty() || txtname.getText().isEmpty() || txtprice.getText().isEmpty() || txtstatus.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Parse and validate numeric fields
        int id = Integer.parseInt(txtid.getText());
        double price = Double.parseDouble(txtprice.getText());

        // Insert query
        String query = "INSERT INTO products (id, name, price, status) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        stmt.setString(2, txtname.getText());
        stmt.setDouble(3, price);
        stmt.setString(4, txtstatus.getText());

        // Execute the query
        int rowsInserted = stmt.executeUpdate();
        if (rowsInserted > 0) {
            JOptionPane.showMessageDialog(this, "Product added successfully!");
        }

        // Refresh the table and reset fields
        populateTable();
        resetFields();
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "ID and Price must be valid numbers!", "Input Error", JOptionPane.ERROR_MESSAGE);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    // Method to update an existing product
    private void updateProduct() {
    try {
        // Validate fields
        if (txtid.getText().isEmpty() || txtname.getText().isEmpty() || txtprice.getText().isEmpty() || txtstatus.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Parse and validate numeric fields
        int id = Integer.parseInt(txtid.getText());
        double price = Double.parseDouble(txtprice.getText());

        // Update query
        String query = "UPDATE products SET name = ?, price = ?, status = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, txtname.getText());
        stmt.setDouble(2, price);
        stmt.setString(3, txtstatus.getText());
        stmt.setInt(4, id);

        // Execute the query
        int rowsUpdated = stmt.executeUpdate();
        if (rowsUpdated > 0) {
            JOptionPane.showMessageDialog(this, "Product updated successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Product ID not found!", "Update Error", JOptionPane.WARNING_MESSAGE);
        }

        // Refresh the table and reset fields
        populateTable();
        resetFields();
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "ID and Price must be valid numbers!", "Input Error", JOptionPane.ERROR_MESSAGE);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    // Method to delete a product from the database
    private void deleteProduct() {
    try {
        // Validate ID field
        if (txtid.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID field is required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Parse and validate numeric ID
        int id = Integer.parseInt(txtid.getText());

        // Delete query
        String query = "DELETE FROM products WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);

        // Execute the query
        int rowsDeleted = stmt.executeUpdate();
        if (rowsDeleted > 0) {
            JOptionPane.showMessageDialog(this, "Product deleted successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Product ID not found!", "Delete Error", JOptionPane.WARNING_MESSAGE);
        }

        // Refresh the table and reset fields
        populateTable();
        resetFields();
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "ID must be a valid number!", "Input Error", JOptionPane.ERROR_MESSAGE);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    // Method to reset the input fields
    private void resetFields() {
        txtid.setText("");
        txtname.setText("");
        txtprice.setText("");
        txtstatus.setText("");
    }

    // Search method to find a product based on ID
  private void searchProduct() {
    try {
        // Validate ID field
        if (txtid.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID field is required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Parse and validate numeric ID
        int id = Integer.parseInt(txtid.getText());

        // Search query
        String query = "SELECT * FROM products WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            txtname.setText(rs.getString("name"));
            txtprice.setText(String.valueOf(rs.getDouble("price")));
            txtstatus.setText(rs.getString("status"));
        } else {
            JOptionPane.showMessageDialog(this, "Product not found!", "Search Error", JOptionPane.WARNING_MESSAGE);
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "ID must be a valid number!", "Input Error", JOptionPane.ERROR_MESSAGE);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


        
        
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtname = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtid = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtstatus = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnupdate = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        btnreset = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtprice = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        btnsearch = new javax.swing.JButton();

        jButton2.setText("jButton2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel1.setText("Product Name");

        txtname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnameActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel2.setText("Product Id");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel3.setText("Status");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product Id", "Product Name", "Price", "Status"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        btnAdd.setBackground(new java.awt.Color(204, 204, 255));
        btnAdd.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnDelete.setBackground(new java.awt.Color(204, 204, 255));
        btnDelete.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnupdate.setBackground(new java.awt.Color(204, 204, 255));
        btnupdate.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnupdate.setText("Update");
        btnupdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnupdateActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(204, 204, 255));
        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jButton1.setText("Exit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnreset.setBackground(new java.awt.Color(204, 204, 255));
        btnreset.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnreset.setText("Reset");
        btnreset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnresetActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel4.setText("Price");

        txtprice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtpriceActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 30)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Cosmetic Record");

        btnsearch.setBackground(new java.awt.Color(204, 204, 255));
        btnsearch.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnsearch.setText("Search");
        btnsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnupdate, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnreset, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnsearch)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtname)
                            .addComponent(txtid)
                            .addComponent(txtstatus, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                            .addComponent(txtprice))
                        .addGap(0, 268, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGap(88, 88, 88)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 607, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtprice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtstatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd)
                    .addComponent(btnDelete)
                    .addComponent(btnupdate)
                    .addComponent(jButton1)
                    .addComponent(btnreset)
                    .addComponent(btnsearch))
                .addGap(33, 33, 33)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnameActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
    
            btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                addProduct();
            }
        });
            
        
//    String productName = txtname.getText().trim();
//    String productId = txtid.getText().trim();
//    String priceText = txtprice.getText().trim();
//    String status = txtstatus.getText().trim();
//
//    
//    if (productName.isEmpty() || !productName.matches("[a-zA-Z\\s]+")) {
//        JOptionPane.showMessageDialog(this, "Please enter a valid product name.");
//        return;
//    }
//
//    if (productId.isEmpty() || !productId.matches("[a-zA-Z0-9]+")) {
//        JOptionPane.showMessageDialog(this, "Please enter a valid product ID.");
//        return;
//    }
//    
//    try {
//        double price = Double.parseDouble(priceText);
//        if (price <= 0) {
//            JOptionPane.showMessageDialog(this, "Price must be a positive value.");
//            return;
//        }
//    } catch (NumberFormatException e) {
//        JOptionPane.showMessageDialog(this, "Please enter a valid info.");
//        return;
//    }
//    
//    if (status.isEmpty()) {
//        JOptionPane.showMessageDialog(this, "Status cannot be empty.");
//        return;
//    }
//
//    
//    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
//    model.addRow(new Object[]{productName, productId, priceText, status});
//
//    
//    txtname.setText("");
//    txtid.setText("");
//    txtprice.setText("");
//    txtstatus.setText(""); 
//    saveToFile();  
//  
    }//GEN-LAST:event_btnAddActionPerformed

    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                deleteProduct();
                resetFields();
            }
        });
//        DefaultTableModel tblModel = (DefaultTableModel)jTable1.getModel();
//        
//        if(jTable1.getSelectedRowCount()==1){
//            // When Single row selected
//             tblModel.removeRow(jTable1.getSelectedRow());
//        }else{
//            if(jTable1.getRowCount()==0){
//                //show message when table is empty
//               JOptionPane.showMessageDialog(this, "Table is Empty");
//        }else{
//             JOptionPane.showMessageDialog(this,"Please Select a Single Row");
//            }
//            }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        DefaultTableModel tblModel = (DefaultTableModel)jTable1.getModel();
        // Set data to text field when row is selected
        String tblid = tblModel.getValueAt(jTable1.getSelectedRow(),0).toString();
         String tblname = tblModel.getValueAt(jTable1.getSelectedRow(),1).toString();
         String tblprice = tblModel.getValueAt(jTable1.getSelectedRow(),2).toString();
          String tblstatus = tblModel.getValueAt(jTable1.getSelectedRow(),3).toString();
          // set to textfield
          txtid.setText(tblid);
          txtname.setText(tblname);
          txtprice.setText(tblprice);
          txtstatus.setText(tblstatus);
    }//GEN-LAST:event_jTable1MouseClicked

    private void btnupdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnupdateActionPerformed
////        DefaultTableModel tblModel = (DefaultTableModel)jTable1.getModel();
////        if(jTable1.getSelectedRowCount()==1){
////            // if single row selected then update
////            String name = txtname.getText();
////            String id = txtid.getText();
////            String price = txtprice.getText();
////            String status = txtstatus.getText();
////            // Set updated value on table row
////            tblModel.setValueAt(name, jTable1.getSelectedRow(),0);
////            tblModel.setValueAt(id, jTable1.getSelectedRow(),1);
////            tblModel.setValueAt(price, jTable1.getSelectedRow(),2);
////            tblModel.setValueAt(status, jTable1.getSelectedRow(),3);
////            // then update message display 
////            JOptionPane.showMessageDialog(this, "Your data is update now");
////        }else{
////            if(jTable1.getRowCount()==0){
////                //show message when table is empty
////               JOptionPane.showMessageDialog(this, "Table is Empty");
////            }else{
////                //select row for update
////                JOptionPane.showMessageDialog(this,"Please Select a Single Row for update");
////            }
////        }
            btnupdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                updateProduct();
                resetFields();
            }
        });
    }//GEN-LAST:event_btnupdateActionPerformed

    private void btnresetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnresetActionPerformed
        btnreset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                resetFields();
            }
        });
       
    }//GEN-LAST:event_btnresetActionPerformed

    private void txtpriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtpriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtpriceActionPerformed

    private void btnsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsearchActionPerformed
//         String searchName = txtname.getText();
//         String searchID = txtid.getText();
//                
//                // Iterate over the rows to find a match
//                for (int row = 0; row < jTable1.getRowCount(); row++) {
//                    String productID = jTable1.getValueAt(row, 1).toString();
//                    String productName = jTable1.getValueAt(row, 0).toString();
//                    if (productID.equals(searchID)|| productName.equals(searchName)) {
//                       
//                        jTable1.setRowSelectionInterval(row, row);
//                        Store store = new Store();
//                        store.displaySelectedRowData();
//                       
//                        return; // Exit the method after finding a match
//                    }
//                }
            btnsearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                searchProduct();
                resetFields();
            }
        });
                
                
                JOptionPane.showMessageDialog(Products.this, "No product found : ");
        

    }//GEN-LAST:event_btnsearchActionPerformed
        
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Products().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnreset;
    private javax.swing.JButton btnsearch;
    private javax.swing.JButton btnupdate;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtid;
    private javax.swing.JTextField txtname;
    private javax.swing.JTextField txtprice;
    private javax.swing.JTextField txtstatus;
    // End of variables declaration//GEN-END:variables

//    private void displaySelectedRowData() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//}
}
