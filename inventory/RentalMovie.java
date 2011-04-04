/**
* RentalMovie class 
* Which provides and initialises information about an individual rental movie, by giving it a
* status, history and condition which will be stored in history
* @author Legen
* @version 1.0 March 27, 2011
* -----------matt: removed setCondition() and setStatus() from constructor (duplicate function calls)
*/
package inventory;
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
    	history = new ArrayList<RentalHistory>();
    	this.condition = condition;
        this.status = status;
        setPrice(movie.getPrice());
        setFormat(movie.getFormat());
        setCategory(movie.getCategory());
        setBarCode(movie.getBarCode());
    }
   
    private ArrayList history;           //The rental history of a movie, who has it, and when it will be returned
    private String status;              //The status of the movie, if it is store "rent" or is out "rented out" or reserved
    private String condition;           //The physical condition of the movie, states as a string whether its damaged, good, scratched etc
    private Statement statement;
    final char quote ='\'';
    final char comma =',';
    Scanner myScanner = new Scanner(System.in);
   
        
    /**
    * Create a history query that updates the rental history table
    * @param history a history record of the rental movie
    * * @throws SQLException
    */
    final protected void appendHistory(RentalHistory history) throws SQLException
    {
       setStatus("rented out");                            //changes the status of the movie to rented out
       if(!getCondition().equals("good")){                 //checks the condition of the movie
    	   System.out.print("what is the movie condition? " );
   		   String state = myScanner.nextLine();               //gets input from staff about condition of movie
    	   setCondition(state);                            //changes the condition of the movie to rented out
       }
       history.add("Barcode= "+ barcode + " Customer= "+ CustomerAccount.customer()+" Rented= "+ Date.rentDate()+" Due= "
    		   + Date.returnDate()+" Status=  "+ status +" Condition= "+ condition); //adds information to arraylist as a single index
       String table = "RentalHistory";
           String query = "insert into "+table
          				+"where barcode ="+quote+barcode+quote+comma			   //makes sure the correct movie is selected by barcode
          				+"values ("+quote+history.getAccount().getAccountID()+quote+comma //account id of the customer
          					+quote+history.rentDate().getDate()+quote+comma			//the date this movie is checked out, todays date
                            +quote+history.returnDate().getDate()+quote+comma			//the date this movie is to be returned
            				+quote+history.getStatus()+quote+comma 		    //the status of the movie
            				+quote+history.getCondition()+quote		//the condition of the movie
            			+");";
           super.executeQuery(query);
          
    }
    
    /**
    * Show movies whole rental history
    */
    protected void showHistory()
    {
     System.out.println("The movie " + barcode + " history is :");
    //display elements of history
    for(int index=0; index < history.size(); index++)
    System.out.println(history.get(index));	
    //this shows the last line only
    //if (!history.isEmpty()) {
    //	  history.get(history.size()-1);
    //	}

    }
    
    
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
