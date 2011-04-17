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
import authentication.*;
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
    public UiController(){
        mainKey = new Authentication();

    }
    /**
     * Start UI
     */
    public void startUI(){

        boolean logIn = false;

        switch (mode) {
            case Employee:

                try {
                    currFrame = new EmployeeFrame(this);
                    new LoginDialog(mainKey,this,false).setVisible(true);
                    // Employee Frame will be bring up the login process.
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
                    currFrame.setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(UiController.class.getName()).log(Level.SEVERE, null, ex);
                }
		break;
	}

    }

    private boolean login(){

        return true;
    }


    /**
     * Returns current Frame
     * @return current Frame
     */
    public java.awt.Frame getCurrentFrame(){
        return currFrame;
    }

    /**
     * Returns current Authentication object
     * @return Authentication object.
     */
    public Authentication getKey(){
        return this.mainKey;
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
