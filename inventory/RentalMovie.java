/**
* RentalMovie class 
* Which provides and initialises information about an individual rental movie, by giving it a
* status, history and condition which will be stored in history
* @author Legen
* @version 1.0 March 27, 2011
* -----------matt: removed setCondition() and setStatus() from constructor (duplicate function calls)
**----------moved out querys 
*/
package inventory;


public class RentalMovie extends IndividualMovie{

    
    public static final int ID_LENGTH = IndividualMovie.ID_LENGTH;
    public static final int RENTAL_HOLD_PERIOD = 3;
    // period of time a rental movie is held for before it goes back to rental circulation

    private int rentalPeriod;



    /**
     * Empty constructor
     */
    public RentalMovie()
    {
    }
    /**
    * The constructor
    * inherits from IndividualMovie
    * takes 3 default attributes
    * @param status The status of the movie, if it is store "rent" or is out "rented out" or "reserved"
    * @param condition The physical condition of the movie, states as a string whether its damaged, good, scratched etc
    * @param movie inherited movie information
    */
    public RentalMovie(int rentalPeriod, IndividualMovie movie) throws java.lang.Exception
    {
        this.rentalPeriod = rentalPeriod;
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
	setGenre(movie.getGenre());
    }
   

  
  /**
  * Set rental period for this movie
  * @param rentalPeriod the rental period for this movie
  */
   public void setRentalPeriod(int rentalPeriod)
  {
    this.rentalPeriod = rentalPeriod;
  }
  /**
 * Get rental period 
 * @return rental period
 */
  public int getRentalPeriod()
  {
    return rentalPeriod;
  }

  

   
  

}
