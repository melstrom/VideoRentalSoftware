/**
* RentalMovie class 
* Which provides and initialises information about an individual rental movie, by giving it a
* status, history and condition which will be stored in history
* @author Legen
* @version 1.0 March 27, 2011
* -----------matt: removed setCondition() and setStatus() from constructor (duplicate function calls)
*/
//package inventory;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;


class RentalMovie extends IndividualMovie{

    /**
    * The constructor
    * inherits from IndividualMovie
    * takes 3 default attributes
    * @param status The status of the movie, if it is store "rent" or is out "rented out" or "reserved"
    * @param condition The physical condition of the movie, states as a string whether its damaged, good, scratched etc
    * @param history The rental history of a movie, information such as who has the movie rented out, and when it will be returned
    */
   
    public RentalMovie(String condition, String status, IndividualMovie movie)
   {
    	this.condition = condition;
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
    }
   
    private ArrayList history;           //The rental history of a movie, who has it, and when it will be returned
    private String status;              //The status of the movie, if it is store "rent" or is out "rented out" or reserved
    private String condition;           //The physical condition of the movie, states as a string whether its damaged, good, scratched etc
    private Statement statement;
    final char quote ='\'';
    final char comma =',';
    Scanner myScanner = new Scanner(System.in);
    /**
    *Set or change the status for the individual movie which can be rented out, rent ,or reserved
    *input: aStatus
    *@param aStatus is the rental status of the movie, three states rent, rented out, and reserved
    */
    private void setStatus(String aStatus)
    {
    		this.status = aStatus;
    }
   
    /**
    *Get the status for the individual movie which can be rented out, rent,or reserved
    *@return status
    */
    public String getStatus()
    {
    	return status;
    }
  
    /**
    *Set the condition for the individual movie which can be damaged, good, or scratched
    *input: state
    *@param state is the condition of the movie
    */
    private void setCondition(String state)
    {
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
}
