/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import authentication.Authentication;
import ui.util.UiMode;

/**
 *
 * @author alby
 */



public class UiController {




    /**
     * 
     */
    private UiMode mode;

    /**
     *
     */
    private JFrame currFrame;

    /*
     *
     */
    private Authentication mainKey;

    /**
    * 
    */
    public void UiController(){
        mainKey = new Authentication();

    }
    /**
     * Start UI
     */
    public void startUI(){
        switch (mode) {
            case Employee:
                if (login()){

                }
                try {
                    currFrame = new EmployeeFrame();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(UiController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(UiController.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

            case Customer:
            default:
                try {
                    currFrame = new CustomerFrame(this);
                } catch (Exception ex) {
                    Logger.getLogger(UiController.class.getName()).log(Level.SEVERE, null, ex);
                }
		break;
	}
        currFrame.setVisible(true);
    }

    private boolean login(){





        return true;
    }

    /**
     * Change modeID.
     * @param modeID enum UiMode Customer and Employee.
     */
    public void setMode(UiMode modeID){
        mode = modeID;
    }


    /**
     * Pop-up an warming message login fail
     */
    public void popLoginFail(){
        JOptionPane.showMessageDialog(currFrame,
        "Sorry, system can't log you using current ID, Password.",
        "Login not successful",
        JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Pop-up an warming message over due item
     */
    public void popDueNotice(){
        JOptionPane.showMessageDialog(currFrame,
        "Sorry, you have over due item(s). Please clear over due item with Casher.",
        "Over Due Item(s)",
        JOptionPane.WARNING_MESSAGE);
    }

    /**
     *Request done message
     */
    public void PopRequestDone(){
        JOptionPane.showMessageDialog(currFrame,
        "Your Request Done.",
        "Request Done",
        JOptionPane.PLAIN_MESSAGE);
    }

    /**
     *Reservation done message
     */
    public void PopReservationDone(){
        JOptionPane.showMessageDialog(currFrame,
        "Your Reservation Done.",
        "Reservation Done",
        JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Pop-up an error message in current frame
     * @param msg Error Message.
     * @param title Title / Error Type.
     */
    public void PopErrorMsg(String msg,String title){

        JOptionPane.showMessageDialog(currFrame,
        msg,
        title,
        JOptionPane.ERROR_MESSAGE);
    }
}
