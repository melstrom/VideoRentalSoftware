/**
* SaleMovie class 
* Which provides and initialises information about an individual sale movie, and changes from rental to sale
* @author Legen
* @version 1.0 March 27, 2011
*----------matt: removed setCondition() and setStatus() from constructor (duplicate function calls)
*----------removed changeCategory() it is already in the superclass IndividualMovie as setCategory()
*----------fixed implementation for setStatus() (status datatype mismatch)
*----------removed history 
*----------moved out querys 
 */
package inventory;

public class SaleMovie extends IndividualMovie{

	private String condition;
	private String status;
	final private String WRITEOFF = "WRITEOF"; //movie is written off for excessive damage

        /**
         * Empty constructor
         */
        public SaleMovie()
        {
        }
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
		this.status = status;	
		setSKU(movie.getSKU());
		setTitle(movie.getTitle());
		setActors(movie.getActors());
		setDirector(movie.getDirector());
		setSynopsis(movie.getSynopsis());
		setReleaseDate(movie.getReleaseDate());
		setPrice(movie.getPrice());
		setFormat(movie.getFormat());
		setCategory(movie.getCategory());
		setBarcode(movie.getBarcode());
		setStudio(movie.getStudio());
		setRating(movie.getRating());
		setRetailPriceInCents(movie.getRetailPriceInCents());
		setProducer(movie.getProducer());
                setLength(movie.getLength());
	 }
	
	
	    /**
	     *Set the condition for the individual movie which can be damaged, good, or scratched
	     *@param condition is the condition of the movie
	     */
	     public void setCondition(String condition)
	     {
	     		 String table = "SaleMovie";
	    	        /*String query = "delete from "+table
	    	                +"where barcode ="+quote+barcode+quote                 //delete the movie
	    	                 +";";;*/
	     	
	     	this.condition = condition;
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
	      *@param aStatus is the sale status
	      */
	      public void setStatus(String aStatus)
                {
			/*if(aStatus.equals("Sold")){
	    		  String table = "SaleMovie";
	    	       /* String query = "delete from "+table
	    	                +"where barcode ="+quote+barcode+quote
	    	                 +";";*/
	    	     
	    	/*  }
	    	  else if(aStatus.equals("Sale")){
	    		  String table = "SaleMovie";
	    	       /* String query = "insert into "+table
                        +"values ("+quote+title+quote+comma      //movie title
                                   +quote+barcode+quote+comma    //movie id
                                   +quote+category+quote+");";  //the category of movie discount, sale.

			
		}*/
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
