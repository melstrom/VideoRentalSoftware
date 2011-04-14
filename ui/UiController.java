/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import jdbconnection.JDBCConnection;

import java.sql.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author alby
 */



public class UiController {

    /**
     * 
     */
    private int mode;

    /**
     *
     */
    private JFrame currFrame;

    /**
    * 
    */
    public void UiController(){


    }
    /**
     * Start UI
     */
    public void startUI()throws Exception{
      //currFrame = new CustomerFrame(this);
            currFrame = new EmployeeFrame();
            currFrame.setVisible(true);
    }
    /**
     * Change modeID.
     * @param modeID 1=Customer, 2=Employee.
     */
    public void setMode(int modeID){
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
