/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PriceSchemePanel.java
 *
 * Created on 13-Apr-2011, 3:41:03 PM
 */

package ui;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Vector;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import pos.PriceScheme;
import pos.PriceSchemeManagement;

/**
 *
 * @author cwang01
 */
public class PriceSchemePanel extends javax.swing.JPanel {

    private Vector <Vector<String>> data;
    private Vector <String> header;
    private ArrayList<Integer> changedRows;
    private ArrayList<Integer> changedCols;
    private DefaultTableModel table;
    //Search search = new search


        /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */

    /** Creates new form PriceSchemePanel */
    public PriceSchemePanel() {
        ArrayList headerString;
        ArrayList formatString;
        //populates the data Arraylist with the prices
        data = new Vector <Vector<String>>();
        try {
            int[][] prices;
            PriceSchemeManagement init = new PriceSchemeManagement();
            prices = init.getAllPrices();
            headerString = new ArrayList<String>();
            headerString = init.getAllCategories();
            headerString.add(0,"");
            formatString = new ArrayList<String>();
            formatString = init.getAllFormats();
            header = ReturnVector(headerString);
            Vector <String> row;
            
            for (int j = 0; j < prices[j].length; j++)
            {
                row = new Vector <String>();
                row.add(formatString.get(j).toString().toUpperCase());
                for (int i = 0; i < prices.length; i++)
                {
                    row.add(""+prices[i][j]);
                }
                data.add(row);
            }
           
        }

        catch (SQLException ex) {
            Logger.getLogger(PriceSchemePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(PriceSchemePanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        initComponents();
    }

    private Vector ReturnVector(ArrayList list){
        return new Vector(list);
    }


    private void redrawPanel(){
        table = (DefaultTableModel)priceSchemaTable.getModel();
        while (table.getRowCount()>0){
            table.removeRow(0);}
        ArrayList headerString;
        ArrayList formatString;
        //populates the data Arraylist with the prices
        data = new Vector <Vector<String>>();
        try {
            int[][] prices;
            PriceSchemeManagement init = new PriceSchemeManagement();
            prices = init.getAllPrices();
            headerString = new ArrayList<String>();
            headerString = init.getAllCategories();
            headerString.add(0,"");
            formatString = new ArrayList<String>();
            formatString = init.getAllFormats();
            header = ReturnVector(headerString);
            Vector <String> row;

            for (int j = 0; j < prices[j].length; j++)
            {
                row = new Vector <String>();
                row.add(formatString.get(j).toString().toUpperCase());
                for (int i = 0; i < prices.length; i++)
                    row.add(""+prices[i][j]);
                table.addRow(row);
            }

        }
        catch (SQLException ex) {
            Logger.getLogger(PriceSchemePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(PriceSchemePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
       table.fireTableStructureChanged();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addFormatPriceSchemeButton = new javax.swing.JButton();
        addCategoryPriceSchemeButton = new javax.swing.JButton();
        refreshPriceSchemeButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        priceSchemaTable = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        addFormatPriceSchemeButton.setText("Add Format");
        addFormatPriceSchemeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addFormatPriceSchemeButtonActionPerformed(evt);
            }
        });

        addCategoryPriceSchemeButton.setText("Add Category");
        addCategoryPriceSchemeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCategoryPriceSchemeButtonActionPerformed(evt);
            }
        });

        refreshPriceSchemeButton.setText("Refresh");
        refreshPriceSchemeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshPriceSchemeButtonActionPerformed(evt);
            }
        });

        priceSchemaTable.setModel(new javax.swing.table.DefaultTableModel(
            data,header
        ));
        //System.out.println("I'm here" );
        priceSchemaTable.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                priceSchemaTableAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jScrollPane2.setViewportView(priceSchemaTable);

        jButton1.setText("Change Prices");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChangePrice(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(83, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 608, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(26, 26, 26)
                        .addComponent(addFormatPriceSchemeButton)
                        .addGap(18, 18, 18)
                        .addComponent(addCategoryPriceSchemeButton)
                        .addGap(18, 18, 18)
                        .addComponent(refreshPriceSchemeButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(refreshPriceSchemeButton)
                    .addComponent(addCategoryPriceSchemeButton)
                    .addComponent(addFormatPriceSchemeButton)
                    .addComponent(jButton1))
                .addContainerGap(22, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addFormatPriceSchemeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addFormatPriceSchemeButtonActionPerformed
        // TODO add your handling code here:
        new AddFormatDialog(false).setVisible(true);
        redrawPanel();
}//GEN-LAST:event_addFormatPriceSchemeButtonActionPerformed

    public void mouseClicked(MouseEvent e) {
        changedRows = new ArrayList<Integer>();
        changedCols = new ArrayList<Integer>();
        int row = priceSchemaTable.rowAtPoint(e.getPoint());
        int col = priceSchemaTable.columnAtPoint(e.getPoint());
        changedRows.add(row);
        changedCols.add(col);
    }

    private void refreshPriceSchemeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshPriceSchemeButtonActionPerformed
//        try {
//            // TODO add your handling code here:
//            int col;
//            int row;
//            int value = 0;
//            String format = "" ;
//            String category = "";
//            PriceSchemeManagement setNewPrice = new PriceSchemeManagement();
//            for(int i = 0; i < changedCols.size(); i++){
//                col = Integer.parseInt("" + changedCols.get(i));
//                row = Integer.parseInt("" + changedRows.get(i));
//                value = Integer.parseInt("" + priceSchemaTable.getValueAt(col,row));
//                format = "" + priceSchemaTable.getValueAt(row, 0);
//                category = "" + priceSchemaTable.getValueAt(0, col);
//                setNewPrice.setPrice(category,format,value);
//
//            }


            redrawPanel();
//        } catch (IOException ex) {
//            Logger.getLogger(PriceSchemePanel.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SQLException ex) {
//            Logger.getLogger(PriceSchemePanel.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(PriceSchemePanel.class.getName()).log(Level.SEVERE, null, ex);
//        }

}//GEN-LAST:event_refreshPriceSchemeButtonActionPerformed

    private void priceSchemaTableAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_priceSchemaTableAncestorAdded
        // TODO add your handling code here:
}//GEN-LAST:event_priceSchemaTableAncestorAdded

    private void addCategoryPriceSchemeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCategoryPriceSchemeButtonActionPerformed
        // TODO add your handling code here:
        new PriceCategoryDialog(false).setVisible(true);
        redrawPanel();
        //makePanel();
    }//GEN-LAST:event_addCategoryPriceSchemeButtonActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
redrawPanel();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void ChangePrice(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChangePrice
        System.out.println("inside changeprice.");//testing
        new ChangePriceDialog(false).setVisible(true);
        redrawPanel();
    }//GEN-LAST:event_ChangePrice


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addCategoryPriceSchemeButton;
    private javax.swing.JButton addFormatPriceSchemeButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable priceSchemaTable;
    private javax.swing.JButton refreshPriceSchemeButton;
    // End of variables declaration//GEN-END:variables

}
