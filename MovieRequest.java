/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * Can't figure out what this does.
 */

package Inventory;

import accounts.Customer;

/**
 *
 * @author alby
 */
public class MovieRequest {


    /**
     * MovieRequest is the constructor for this class.
     */
    public MovieRequest(){
      //default class
    }

    /**
     * process Movie Request.
     * @param movie movie to be request.
     * @param customer Customer who whats to request the movie.
     * @return reqID request ID.
     */
    public int process(IndividualMovie movie, Customer customer){

        int reqID;

        reqID = updateDB(prepareQuery(movie, customer));

        return reqID;

    }

    private String prepareQuery(IndividualMovie movie, Customer customer){
    // formate date into Quere Sting
        String query = "123";
        return query;
    }

    private int updateDB( String query){
      int reqID = 99;
    // Database connction code.

      return reqID;
    }





    //IndividualMovie movie;
    //CustomerAccount customer;


}
