/*
 * Inventory / Reservation
 * Entity Class
 */

package Inventory;

import java.util.Date;
import Account.CustomerAccount;

/**
 *
 * @author alby
 */
public class Reservation {

    /**
     * Customer who make the reservation.
     */
    private CustomerAccount customer;

    /**
     * Date for pickup.
     */
    private Date pickupDate;


    /**
     * Reservation is the constructor for this class. 
     * It takes a customer object and a Date object to make a reservation.
     * @param reserveAcc Account object of Customer who make the reservation.
     * @param reservePickupdate Date for pickup.
     * @pre reserveCustomer a valid CustomerAccount object.
     * @pre pickupDate > today.
     * @post Object Reservation created.
     */
    public Reservation(CustomerAccount reserveAcc, Date reservePickupdate){
      customer = reserveAcc;
      pickupDate = reservePickupdate;
    }


    /**
     * Accessors - Get the customer account.
     * @return account of the customer who makes the reservation
     * @pre customer not null.
     * @post return customer.
     */
    public CustomerAccount getAccount(){
        return customer;
    }

    /**
     * Accessors - Get the reservation date.
     * @return reservation date.
     * @pre pickupDate not null.
     * @post return pickupDate.
     */
    public Date getDate(){
        return pickupDate;
    }

     /**
     * Mutator - Set the customer account.
     * @param acc account to replace existing account.
     * @pre acc is a valid CustomerAccount object.
     * @post new account replaced.
     */
    public void setAccount(CustomerAccount acc){
        customer = acc;
    }

    /**
     * Mutator - Set the reservation date.
     * @param date new reservation date to be set.
     * @pre date is a valid Date object.
     * @post new date replaced.
     */
    public void GetDate(Date date){
        pickupDate = date;
    }   

}
