/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ReseveMovieDialog.java
 *
 * Created on 11-Apr-2011, 7:40:03 AM
 */

package ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;

/**
 *
 * @author alby
 */
public class ReseveMovieDialog extends javax.swing.JDialog {

    private UiController localUIC;

    /** Creates new form ReseveMovieDialog */
    public ReseveMovieDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
    }
    public ReseveMovieDialog(java.awt.Frame parent, boolean modal, ui.UiController UIC) {
        this(parent, modal);
        localUIC = UIC;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        idLabel = new javax.swing.JLabel();
        movieLabel = new javax.swing.JLabel();
        idDisplayLabel = new javax.swing.JLabel();
        movieDisplayLabel = new javax.swing.JLabel();
        pickupDateLabel = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        ReserveButton = new javax.swing.JButton();
        CancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        titleLabel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        titleLabel.setText("Reserve Movie");

        idLabel.setText("Account ID:");

        movieLabel.setText("Movie Title:");

        idDisplayLabel.setText("123456");

        movieDisplayLabel.setText("ET");

        pickupDateLabel.setText("PickUp Date:");

        ReserveButton.setText("Reserve");
        ReserveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReserveButtonActionPerformed(evt);
            }
        });

        CancelButton.setText("Cancel");
        CancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addComponent(titleLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(movieLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(movieDisplayLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(idLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(idDisplayLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(pickupDateLabel)
                                .addGap(26, 26, 26)
                                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(82, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(183, Short.MAX_VALUE)
                .addComponent(ReserveButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(CancelButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idLabel)
                    .addComponent(idDisplayLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(movieLabel)
                    .addComponent(movieDisplayLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pickupDateLabel)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CancelButton)
                    .addComponent(ReserveButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ReserveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReserveButtonActionPerformed
        // TODO add your handling code here:
        localUIC.PopReservationDone();
        this.setVisible(false);
}//GEN-LAST:event_ReserveButtonActionPerformed

    private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelButtonActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_CancelButtonActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ReseveMovieDialog dialog = new ReseveMovieDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

   // 


  private void init(){


      //pickUpDateField = new JFormattedTextField( new SimpleDateFormat( "mm/dd/yyyy" ) );

      Calendar now = Calendar.getInstance();
      Calendar earliestDate = Calendar.getInstance();
      earliestDate.add(Calendar.DAY_OF_WEEK, -1);
      Calendar latestDate = Calendar.getInstance();
      latestDate.add(Calendar.DAY_OF_WEEK, 14);
      SpinnerModel model = new SpinnerDateModel( now.getTime(),
      earliestDate.getTime(), latestDate.getTime(), Calendar.WEEK_OF_YEAR );
      //JSpinner spinner = new JSpinner( model );
      jSpinner1.setModel(model);
      //jSpinner1.setEditor(new JSpinner.DateEditor(jSpinner1, "MM/yyyy"));

  }




    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CancelButton;
    private javax.swing.JButton ReserveButton;
    private javax.swing.JLabel idDisplayLabel;
    private javax.swing.JLabel idLabel;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JLabel movieDisplayLabel;
    private javax.swing.JLabel movieLabel;
    private javax.swing.JLabel pickupDateLabel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables

}