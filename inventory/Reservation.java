/*
 * Inventory / Reservation
 * Entity Class
* -------matt: replaced Date object with GregorianCalendar. (Date is deprecated)
*-------replaced Customer parameter with customerID (this class has no access to Customer Class which is in a package outside of inventory package)
 */

//package inventory;

import java.util.GregorianCalendar;

/**
 *
 * @author alby
 */
public class Reservation {

    /**
     * The id of the customer who made the reservation.
     */
    private int customerID;

    /**
     * Date for pickup.
     */
    private GregorianCalendar pickupDate;


    /**
     * Reservation is the constructor for this class. 
     * It takes a customer object and a Date object to make a reservation.
     * @param reserveAcc Account object of Customer who make the reservation.
     * @param reservePickupdate Date for pickup.
     * @pre a valid customer id
     * @pre pickupDate > today.
     * @post Object Reservation created.
     */
    public Reservation(int reserveAccID, GregorianCalendar reservePickupdate){
      customerID = reserveAccID;
      pickupDate = reservePickupdate;
    }


    /**
     * Accessors - Get the customer account.
     * @return account of the customer who makes the reservation
     * @post return customer id.
     */
    public int getAccountID(){
        return customerID;
    }

    /**
     * Accessors - Get the reservation date.
     * @return reservation date.
     * @pre pickupDate not null.
     * @post return pickupDate.
     */
    public GregorianCalendar getDate(){
        return pickupDate;
    }

     /**
     * Mutator - Set the customer account.
     * @param acc account to replace existing account.
     */
    public void setAccountID(int id){
        customerID = id;
    }

    /**
     * Mutator - Set the reservation date.
     * @param date new reservation date to be set.
     * @pre date is a valid Date object.
     * @post new date replaced.
     */
    public void GetDate(GregorianCalendar date){
        pickupDate = date;
    }   

}
