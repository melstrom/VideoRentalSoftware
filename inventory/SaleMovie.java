/**
* SaleMovie class 
* Which provides and initialises information about an individual sale movie, and changes from rental to sale
* @author Legen
* @version 1.0 March 27, 2011
*----------matt: removed setCondition() and setStatus() from constructor (duplicate function calls)
*----------removed changeCategory() it is already in the superclass IndividualMovie as setCategory()
*----------fixed implementation for setStatus() (status datatype mismatch)
 */
package inventory;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

class SaleMovie extends IndividualMovie{

	private String condition;
	private String status;
	final private String WRITEOFF = "write off"; //movie is written off for excessive damage

	/**
         * The constructor
	 * inherits from IndividualMovie
	 * takes 2 default attributes
         * @param condition the condition of the movie item
         * @param status the status of the movie (sold/for sale)
         * @param movie the inherited individualMovie
         */
	public SaleMovie(String condition, String status, IndividualMovie movie)
	{
	   this.condition = condition;                     //condition of movie
	   this.status = status;						//if it is sold or for sale
	   setPrice(movie.getPrice());
	   setFormat(movie.getFormat());
	   setCategory(movie.getCategory());
	   setBarCode(movie.getBarCode());
	 }
	
	/*
	*change the category for the individual movie from rental to sale, which is used for pricing schemes
	*input: saleMovie
	*@param saleMovie is a movie category for sale movies
	
	 protected void changeCategory(RentalMovie saleMovie)
	 {
	   	this.category = saleMovie;
	 }
         */
	
	    /**
	     *Set the condition for the individual movie which can be damaged, good, or scratched, or write off
	     *input: state
	     *@param state is the condition of the movie
	     */
	     private void setCondition(String state)
	     {
	     	if (state.equals(WRITEOFF))
	     	{
	     		  String table = "SaleMovie";
	    	        String query = "delete from "+table
	    	                +"where barcode ="+quote+barcode+quote                 //delete the movie
	    	                 +";";
	    	        super.executeQuery(query);
	     	}
	     	this.condition = state;
	     }
	    
	     /**
	     *Get the condition for the individual movie which can be damaged, good, or scratched
	     *@return condition
	     */
	     public String getCondition()
	     {
	     	return condition;
	     }
	     
	     /**
	      *Set or change the status for the individual movie which cannot be rented out, and can only be sold or for sale
	      *input: aStatus
	      *@param aStatus is the sale status
	      */
	      private void setStatus(String aStatus)
                {
	    	  if(aStatus.equals("Sold")){
	    		  String table = "SaleMovie";
	    	        String query = "delete from "+table
	    	                +"where barcode ="+quote+barcode+quote
	    	                 +";";
	    	        super.executeQuery(query);
	    	  }
	    	  else if(aStatus.equals("Sale")){
	    		  String table = "SaleMovie";
	    	        String query = "insert into "+table
                        +"values ("+quote+title+quote+comma      //movie title
                                   +quote+barcode+quote+comma    //movie id
                                   +quote+category+quote+");";  //the category of movie discount, sale.

                  super.executeQuery(query);
		}
	      	  this.status = aStatus;
	      }
	     
	      /**
	      *Get the status for the individual movie which cannot be rented out, and can only be sold or for sale
	      *@return status
	      */
	      public String getStatus()
	      {
	      	return status;
	      }
}
