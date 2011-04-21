/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CustomerFrame.java
 *
 * Created on 6-Apr-2011, 1:15:02 PM
 */

package ui;
import inventory.MovieNotFoundException;
import inventory.RentalMovieManagement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import search.Search;
import javax.swing.JOptionPane;


/**
 *
 * @author alby
 * Edited by Kristan
 */


public class CustomerFrame extends javax.swing.JFrame {


    /*public class MyTableModel extends javax.swing.table.AbstractTableModel{

        public int getRowCount() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public int getColumnCount() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
}*/


    private UiController localUIC;
    private Vector <Vector <String>> tableContent;
    private Vector <String> header;
    final String[] sList =
        {
	 "Canada",
	 "USA",
	 "Australia",
	 "Bolivia",
	 "Denmark",
	 "Japan"
	 };




    /** Creates new form CustomerFrame
     * @param UIC
     */
    public CustomerFrame() throws Exception {
        MakeTable();
        initComponents();       
    }
    
    public CustomerFrame(ui.UiController UIC)throws Exception {
        localUIC = UIC;
        MakeTable();
        initComponents();
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private Vector ReturnVector(ArrayList list){
        return new Vector(list);
    }

    private void MakeTable(){

      tableContent = new Vector<Vector <String>>();
      for(int i = 0; i < 1; i++){
          Vector temp = new Vector <String>();
          temp.add(" ");
          temp.add(" ");
          temp.add(" ");
          temp.add(" ");
          temp.add(" ");
          tableContent.add(temp);
      }

      header = new Vector<String>();
      header.add("Title"); //Empid
      header.add("Release Date"); // employee name
      header.add("Director"); // employee position
      header.add("Actor(s)"); // employee department
      header.add("Rating"); // employee department
      header.add("Format");
      header.add("Availability");

    }

    private void UpdateTable() {
      String searchItem = searchField.getText();
      String selectedItem = (String)searchComboBox.getSelectedItem();
      tableContent = new Vector<Vector <String>>();

        DefaultTableModel table = (DefaultTableModel)jTable1.getModel();
        while (table.getRowCount()>0)
            table.removeRow(0);

        try {
            result = Search.searchMovies(searchItem, selectedItem);

            jdbconnection.JDBCConnection conn = new jdbconnection.JDBCConnection();
            for (int i = 0; i < result.size();i++) //inventory.GeneralMovie movie : result)
            {
                inventory.GeneralMovie singleMovie = (inventory.GeneralMovie)result.get(i);

                Vector <String> row = new Vector<String>();
                row.add(singleMovie.getTitle());
                row.add("" + singleMovie.getReleaseDate().get(java.util.Calendar.YEAR));
                row.add("" + singleMovie.getDirector());
                String actors = "";
                for (String actor : singleMovie.getActors())
                {
                    actors += actor;
                    actors += ", ";
                }
                actors = actors.trim().substring(0, actors.length() -2);
                row.add("" + actors);
                row.add("" + singleMovie.getRating());
                row.add(singleMovie.getFormat());
                if(RentalMovieManagement.getAvailableCopies(singleMovie, conn)> 0)
                    row.add(""+RentalMovieManagement.getAvailableCopies(singleMovie, conn));
                else if(RentalMovieManagement.getAvailableCopies(singleMovie, conn)== 0)
                    row.add("Rented");
                else if(RentalMovieManagement.getAvailableCopies(singleMovie, conn) < 0)
                    row.add("Not in Store");
                //tableContent.add(row);
                table.addRow(row);
            }

        } catch (SQLException ex) {
            Logger.getLogger(CustomerFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CustomerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }catch (MovieNotFoundException ex) {
            searchField.setText("Movie Not Found");
        }
        catch (Exception ex)
        {
            javax.swing.JOptionPane.showMessageDialog(null, ex.getMessage(), "An error occurred", javax.swing.JOptionPane.ERROR_MESSAGE);
        }



/*
      header = new Vector<String>();
      header.add("Title"); //Empid
      header.add("Release Date"); // employee name
      header.add("Director"); // employee position
      header.add("Actor(s)"); // employee department
      header.add("Rating"); // employee department
      for(String i : header){
      }
 */


        table.fireTableStructureChanged();

    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        searchField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        searchComboBox = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        RequestButton = new javax.swing.JButton();
        ReserveButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        searchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchFieldActionPerformed(evt);
            }
        });

        jButton1.setText("Search");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        searchComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Title", "Director", "Actors", "Genre" }));
        searchComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jButton1)
                .addContainerGap(52, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable1.setAutoCreateColumnsFromModel(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            tableContent, header
        ));
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24));
        jLabel1.setText("Search-O-Film");

        RequestButton.setText("Request Movie");
        RequestButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RequestButtonActionPerformed(evt);
            }
        });

        ReserveButton.setText("Reserve Movie");
        ReserveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReserveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(401, Short.MAX_VALUE)
                .addComponent(RequestButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ReserveButton)
                .addGap(18, 18, 18))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ReserveButton)
                    .addComponent(RequestButton))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(227, 227, 227)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(231, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchFieldActionPerformed

    private void searchComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchComboBoxActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      // TODO add your handling code here:
 /*       java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try{
                    new CustomerFrame().setVisible(true);
                }catch(Exception e){}
            }
        });
  *
  */
   // MyTableModel tableOfDoom;
   // tableOfDoom = (MyTableModel) jTable1.getModel();
   // jTable1.fireTableDataChanged();
        UpdateTable();
        DefaultTableModel table = (DefaultTableModel)jTable1.getModel();
        table.fireTableStructureChanged();

        //SearchTableModel
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void ReserveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReserveButtonActionPerformed
                                                      
        JOptionPane.showMessageDialog(null, "This feature is not implemented yet", "Sorry", JOptionPane.INFORMATION_MESSAGE);
        /*
        if(localUIC.getKey().isLogin())
           new ReseveMovieDialog(this, false, localUIC).setVisible(true);
        else
        {
            new LoginDialog(localUIC).setVisible(true);
        }
         *
         */
           //new LoginDialog(localUIC.getKey(),localUIC,false).setVisible(true);
            // commented out because there is no such constructor
}//GEN-LAST:event_ReserveButtonActionPerformed

    private void RequestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RequestButtonActionPerformed
        //JOptionPane.showMessageDialog(null, "This feature is not implemented yet", "Sorry", JOptionPane.INFORMATION_MESSAGE);
        try
        {


            String accountID = JOptionPane.showInputDialog(null, "Please enter your acocunt number", "Request", JOptionPane.QUESTION_MESSAGE);
            int customerID = 0;
            try
            {
                customerID = Integer.parseInt(accountID);
            }
            catch (NumberFormatException e)
            {
                throw new NumberFormatException("Account ID must be a number.  Cancelling Request");
            }
            account.Customer customer = Search.getCustomer(customerID);
            if (customer == null)
            {
                throw new account.CustomerNotFoundException("That ID does not exist");
            }
            int rowNum = jTable1.getSelectedRow();
            inventory.GeneralMovie movie = result.get(rowNum);
            if (movie == null)
            {
                throw new inventory.MovieNotFoundException("That movie does not exist");
            }
            new RequestMovieDialog(this, false, localUIC, movie, customer).setVisible(true);
//            if(localUIC.getKey().isLogin())
//            {
//                //public RequestMovieDialog(java.awt.Frame parent, boolean modal, ui.UiController UIC, inventory.GeneralMovie movie, account.Customer customer) {
//
//            }
//            else
//            {
//                new LoginDialog(localUIC).setVisible(true);
//            }
               //new LoginDialog(localUIC.getKey(),localUIC,false).setVisible(true);
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage(), "An error occurred", JOptionPane.ERROR_MESSAGE);
        }


}//GEN-LAST:event_RequestButtonActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        int rowNum = jTable1.getSelectedRow();
        inventory.GeneralMovie movie = result.get(rowNum);
        
        new ui.searchDetailDialog(this, false, movie).setVisible(true);
}//GEN-LAST:event_jTable1MouseClicked

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try{
                    new CustomerFrame().setVisible(true);
                }catch(Exception e){}
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton RequestButton;
    private javax.swing.JButton ReserveButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox searchComboBox;
    private javax.swing.JTextField searchField;
    // End of variables declaration//GEN-END:variables
    private ArrayList<inventory.GeneralMovie> result;
}
