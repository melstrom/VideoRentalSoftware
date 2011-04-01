/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Inventory;

import Account.CustomerAccount;

/**
 *
 * @author alby
 */
public class Reservation {

    /**
     * Reservation is the constructor for this class.
     */
    public Reservation(){
      //default class
    }

    /**
     * process Movie Reservation.
     * @param movie movie to be reserve.
     * @param customer Customer who whats to reserve the movie.
     * @return reqID request ID.
     */
    public int process(IndividualMovie movie, CustomerAccount customer){

        int reservationID;

        reservationID = updateDB(prepareQuery(movie, customer));

        return reservationID;

    }

    private String prepareQuery(IndividualMovie movie, CustomerAccount customer){
    // formate date into Quere Sting
        String query = "123";
        return query;
    }

    private int updateDB( String query){
      int reservationID = 99;
    // Database connction code.

      return reservationID;
    }

    //IndividualMovie movie;
    //CustomerAccount customer;

}
