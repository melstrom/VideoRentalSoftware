package inventory;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
//import search.Search;
import inventory.MovieNotFoundException;
import inventory.MovieNotAvailableException;
import inventory.GeneralMovie;
import inventory.IndividualMovie;
import inventory.RentalMovie;
import inventory.Reservation;
import account.CustomerNotFoundException;
import account.Customer;
import java.util.Calendar;
import java.util.GregorianCalendar;
import jdbconnection.JDBCConnection;

/**
 * This is a utility class that encapsulates the interface of all classes that
 * deal with managing rental movies.
 *
 *
 * Last updated: 1 April
 * @author Mitch
 *
 *
 * Class usage sequence 
 * 
 *  RentalMovieManagement() -> setCurrentCopy(RentalMovie) -> action()
 *  
 *  special case: getAvailability(ArrayList<GeneralMovie> movies)
 */
public class RentalMovieManagement {

    /**
     * Default constructor
     * initialize JDBC connection
     */
    public RentalMovieManagement()throws SQLException, ClassNotFoundException
    {
      JDBC = new JDBCConnection();
      connection = JDBC.getConnection();
    }
    
    public enum Status
    {
        AVAILABLE, RENTED, OVERDUE
    }
    
	/**
     * These are the possible conditions of a RentalMovie
     */
    public enum Condition
    {
        GOOD, LOST, DAMAGED, RESERVED, AVAILABLE, RENTED, OVERDUE
    }

        /**
     * all possible categories
     */
    public enum Category
    {
        NEW_RELEASE, SEVEN_DAY_RENTAL, FOR_SALE, DISCOUNT
    }
    
    //Reservation 
    //--------------------------------------------------------------------------------------------------------------------------------
    
    public void makeReservation(Customer customer)
    {
	Calendar today = Calendar.getInstance();
        dueDate.setTime(today.getTime());
        dueDate.set(dueDate.get(dueDate.YEAR),dueDate.get(dueDate.MONTH),dueDate.get(dueDate.DATE));
	Reservation reservation = new Reservation (customer.getAccountID(), dueDate);
	movie.reservationEnqueue(reservation);
    }
    
    public void removeReservation()
    {
	movie.reservationDequeue();
    }
    
      /*  final protected void addReservation(Reservation reservation) throws SQLException
    {
        String table = "Reservation";
        String query = "insert into "+table
                +"values ("+quote+title+quote+comma //movie title
                           +quote+reservation.getAccountID()+quote+comma //account id of the customer
                            +quote+reservation.getDate()+quote+");";//the date this movie is reserved

    }*/
    /**
     * Create remove reservation query
     * @throws SQLException
     */
    /*
    final protected void removeReservation() throws SQLException
    {
        String table = "Reservation";
        String query = "delete from "+table
                +"where title ="+quote+title+quote
                 +";";
    }*/
    
    
    //Gets and sets
    //--------------------------------------------------------------------------------------------------------------------------------
   
   public void setCurrentCopy(String barcode)
   {
	this.barcode = barcode;
	//Search search = new search();
	//movie = search.previewRentalMovie(barcode);
   }
   
   public void setCurrentCopy(RentalMovie movie)
   {
	this.movie = movie;
   }

    // TODO: Problem with putting category in VideoRental
    /**
     * Alternate call of editCopy(String,String[])
     * Changes the attributes of the RentalMovie to the specified ones.
     * If you don't want to change any attributes, then send them as null
     * @param category
     * @param format
     * @param condition
     * @throws MovieNotFoundException
     * @throws SQLException
     */
    public void editCopy(String category, String format, String condition)
            throws MovieNotFoundException, SQLException, java.lang.Exception
    {
     //   RentalMovie movie = (RentalMovie) Search.previewMovie(barcodeNum);
        
	setCategory(category);
        setFormat(format);
        setConditionByMovie(condition);
    }
        /**
     * This method updates all the attributes of the RentalMovie associated with
     * the barcodeNum
     * @param barcodeNum the barcode number of the RentalMovie whose information
     * you want to change
     * @param info an array of information, with each String in the array
     * corresponding to an attribute of RentalMovie
     * It should be an array of length 4.
     * The order should be category, format, condition, status.
     * If you don't want to change one, it should be null in the array
     * @throws SQLException
     * @throws MovieNotFoundException
     
    public void editCopy(String[] info)
            throws SQLException, MovieNotFoundException, java.lang.Exception
    {
        final int EXPECTED_INFO_LENGTH = 4;
        if (info.length != EXPECTED_INFO_LENGTH)
        {
            throw new IllegalArgumentException("info length wrong");
        }
        editCopy(info[0], info[1], info[2], info[3]);
    }*/

/**
    * 
    * @return a list of availability numbers
    */
    public String [] getAvailability(ArrayList<GeneralMovie> movies)
    {
	    //Search SKU and sum the total of lines from the result
	    String [] arr = createGetAvailabilitySQL(movies);
	    return new String[3];
    }
    
    /**
     * This method gets the condition of a Rental Movie specified by its
     * barcode number.  Possible conditions are good, lost, and damaged.
     * @param barcodeNum the unique barcode number corresponding to the
     * RentalMovie you are interested in
     * @return A string specifying the condition of the movie
     * @throws MovieNotFoundException if the barcode does not correspond to
     * any rental movie.
     * @throws SQLException if the RentalMovie object cannot be created
     * because of lack of connection to the database.
     * @pre the barcode number must correspond to an existing RentalMovie
     */
    public String getCondition()
            throws MovieNotFoundException, SQLException
    {
        return movie.getCondition();
    }

    /** This method sets the condition of the instanced RentalMovie to a specified
     * value and updates the database.
     * @param barcodeNum The barcode number of the Rental Movie whose condition
     * you want to change
     * @param condition The condition that you want to set for the instanced
     * RentalMovie.  It must match either good, lost, or broken, case insensitive.
     * @throws IllegalArgumentException if the condition does not match
     * @throws SQLException if a connection to the database cannot be made or if
     * more than one row is changed
     * @throws MovieNotFoundException if the barcode cannot be found
     * @post one and only one row is changed
     */
    private void setConditionByBarcode(String condition)
            throws IllegalArgumentException, SQLException,
                MovieNotFoundException, Exception
    {
        if (condition == null)
            return;
        Condition.valueOf(condition.toUpperCase());
        String query = setConditionGenerateSQL(condition);
        int rowsChanged = updateDatabase(query);
        if (rowsChanged > 1)
        {
            throw new SQLException("SQLException: database corrupted");
        }
        if (rowsChanged < 1)
        {
            throw new MovieNotFoundException("MovieNotFoundException: "
                    + "cannot find barcode number");
        }

    }

    /** This method sets the condition of the instanced RentalMovie to a specified
     * value and updates the database.
     * @param rentalMovie The  Rental Movie whose condition
     * you want to change
     * @param condition The condition that you want to set for the instanced
     * RentalMovie.  It must match either good, lost, or broken, case insensitive.
     * @throws IllegalArgumentException if the condition does not match
     * @throws SQLException if a connection to the database cannot be made or if
     * more than one row is changed
     * @throws MovieNotFoundException if the barcode cannot be found
     * @post one and only one row is changed
     */
    private void setConditionByMovie(String condition)
            throws IllegalArgumentException, SQLException, Exception, MovieNotFoundException
    {
        setConditionByBarcode(condition);
    }
    
      /**
     * Gets the category of the movie
     * @param barcodeNum the barcodeNumber of the RentalMovie
     * @return the category of the movie
     * @throws SQLException
     * @throws MovieNotFoundException
     */
    public String getCategory()
            throws SQLException, MovieNotFoundException
    {
        //Search search = new Search();
        //RentalMovie movie = search.previewIndividualMovie(barcodeNum);
        return movie.getCategory();
    }
    
    /**
     * Gets the format of the specified movie
     * @param barcodeNum
     * @return the format of the movie
     * @throws SQLException
     * @throws MovieNotFoundException
     */
    public String getFormat()
            throws SQLException, MovieNotFoundException
    {
        //RentalMovie movie = search.previewIndividualMovie(barcodeNum);
        return movie.getFormat();
    }
    
        /**
     * This method gets the specified movie's title
     * @param barcodeNum
     * @return
     * @throws SQLException
     * @throws MovieNotFoundException
     */
    public static String getTitle()
            throws SQLException, MovieNotFoundException
    {
      //  Search search = new Search();
       // GeneralMovie movie = search.previewMovie(barcodeNum);
	RentalMovie movie = new RentalMovie();
        return movie.getTitle();
    }
    
        private void setFormat(String format)throws java.lang.Exception
    {
	    movie.setFormat(format);
	    
    }

    /**
     * Synonym for changeCategory
     * @param movie
     * @param category
     * @throws SQLException
     * @throws IllegalArgumentException
     * @throws MovieNotFoundException
     */
    private void setCategory(String category)
            throws SQLException, IllegalArgumentException, MovieNotFoundException, Exception
    {
        String barcodeID = movie.getBarcode();
        changeCategory(category);
        movie.setCategory(category);

    }    
    
    //General rental
    //--------------------------------------------------------------------------------------------------------------------------------

    /**
     * This method rents out a RentalMovie, updating both the RentalMovie
     * and the Customer's account in the database.
     * @param barcodeNum the barcode number of the RentalMovie
     * @param memberID the unique ID of the customer who is renting
     * @return the due date of the movie
     * @pre the barcode number must correspond to an existing movie
     * @pre the memberID must correspond to an existing member
     * @pre the movie's status must be available
     * @throws MovieNotFoundException if the movie does not exist
     * @throws CustomerNotFoundException if the customer does not exist
     * @throws MovieNotAvailableException if the movie is not available
     */
    public GregorianCalendar rentWithBarcode(String memberID)
            throws MovieNotFoundException, CustomerNotFoundException,
            MovieNotAvailableException, SQLException, Exception
    {
        //Search search = new Search();
        //RentalMovie movie = search.previewIndividualMovie(barcodeNum);
	RentalMovie movie = new RentalMovie();
	rentMovieUpdateDatabase("VideoRental", "condition", Condition.RENTED.name());

        Calendar today = Calendar.getInstance();
        dueDate.setTime(today.getTime());
        dueDate.set(dueDate.get(dueDate.YEAR),dueDate.get(dueDate.MONTH),dueDate.get(dueDate.DATE)+rental_period);

	return dueDate;
    }


    /**
     * This method rents out a RentalMovie, updating both the RentalMovie
     * and the Customer's account in the database.
     * @param movie the movie that you want to rent out
     * @param customer the customer who is renting
     * @return the due date of the movie
     * @pre the barcode number must correspond to an existing movie
     * @pre the memberID must correspond to an existing member
     * @pre the movie's status must be available
     * @throws MovieNotFoundException if the movie does not exist
     * @throws CustomerNotFoundException if the customer does not exist
     * @throws MovieNotAvailableException if the movie is not available
     * @post the movie's status is changed to rented.
     */
    private GregorianCalendar rentWithMovieInfo(Customer customer)
            throws MovieNotFoundException, CustomerNotFoundException,
            MovieNotAvailableException, SQLException, Exception
    {
        if (movie == null || !movie.getCondition().equalsIgnoreCase("available"))
        {
            throw new MovieNotAvailableException("MovieNotAvailableException:"
                    + " movie is not available");
        }

        if (customer == null)
        {
            throw new CustomerNotFoundException("CustomerDoesNotExistException:"
                    + " no Customer specified");
        }

        rentMovieUpdateDatabase( "VideoRental", "condition", Status.RENTED.name());
        
        rentMovieUpdateDatabase("VideoRental", "MemberID", "" + customer.getAccountID());


        movie.setCondition(Status.RENTED.name());

        Calendar today = Calendar.getInstance();
        dueDate.setTime(today.getTime());
        dueDate.set(dueDate.get(dueDate.YEAR),dueDate.get(dueDate.MONTH),dueDate.get(dueDate.DATE)+rental_period);
        return dueDate;
    }

    
    //Utilities
    //--------------------------------------------------------------------------------------------------------------------------------  
    /**
     * Updates the database that the movie is rented out
     * @param movie
     * @throws MovieNotFoundException if the movie is not found in the db
     */
    private void rentMovieUpdateDatabase(String tableName, String attributeName, String setAttributeTo)
            throws MovieNotFoundException, SQLException, Exception
    {
        String barcodeNum = movie.getBarcode();
        String movieWhere = generateMovieWhere();
        
        String movieCommand = generateUpdateSQL(tableName, attributeName,
                setAttributeTo, movieWhere);

        int rowsChanged = updateDatabase(movieCommand);
        
        if (rowsChanged == 0)
        {
            throw new MovieNotFoundException("MovieNotFoundException:"
                    + " Could not find movie in database.");
        }
    }
 
    
    
    /**
     * This method splits a barcode into its SKU and rentalID and returns them
     * by writing them into a passed array of length 2.
     * @param barcodeNum
     * @param overwriteThisArray
     * @throws IllegalArguementException if the barcode is not the right length
     * or the array is not length 2
     */
    private void splitBarcode()
            throws IllegalArgumentException
    {
        if (barcode == null )
            throw new IllegalArgumentException("IllegalArgumentException: Invalid barcode number");
	
	if(barcode.length() > MAX_SKU_LENGTH)
	{
		SKU = barcode.substring(0, MAX_SKU_LENGTH);
		rentalID = barcode.substring(MAX_SKU_LENGTH+1, barcode.length());
	}
	else if(barcode.length() >= MIN_SKU_LENGTH && barcode.length()<= MAX_SKU_LENGTH)
	SKU = barcode;
    }
    
    /**
     * Sets the category of the movie to one of the following: new release, 
     * seven day rental, for sale
     * When the category is set to for sale, the database is changed to reflect
     * this.
     * TODO: where are the categories in the database? assuming that they're
     * in VideoRental table for now.
     * @param barcodeNum the unique barcodeNumber of the movie
     * @param category the category to change to
     * @throws SQLException
     * @throws MovieNotFoundException
     * @throws IllegalArgumentException if the category is not a valid category
     */
    public void changeCategory(String category)
            throws SQLException, MovieNotFoundException, Exception
    {
        Category cat = Category.valueOf((category.replaceAll(" ", "_")).toUpperCase());
        if (cat == Category.FOR_SALE)
        {
            changeToSale();
            return;
        }

        String where = generateMovieWhere();
        String command = generateUpdateSQL("VideoRental", "Category",
                category.toLowerCase(), where);
        updateDatabase(command);
        // TODO: check for number of rows changed
    }
    /**
     * Changes a rental movie to a sale movie and updates the database
     * @param barcodeNum
     * @throws MovieNotFoundException
     * @throws SQLException
     */
    private void changeToSale()
            throws MovieNotFoundException, SQLException
    {
        //RentalMovie movie = (RentalMovie) Search.previewMovie(barcodeNum);

        // TODO: disallow if movie is not available or condition not good?
	RentalMovie movie = new RentalMovie();
        splitBarcode();
        String[] deleteColumnNames = {"SKU", "rentalID"};
        String deleteQuery = generateDeleteSQL("VideoRental", deleteColumnNames);
        String[] insertColumnNames = { "condition", "SKU", "saleID" };
        String[] values = { movie.getCondition(), SKU, rentalID };
        String insertQuery = generateInsertSQL("VideoSales", insertColumnNames, values);
        
        try
        {

            Statement stat = connection.createStatement();
            stat.executeQuery(deleteQuery);
            stat.execute(insertQuery);
        }
        finally
        {
            connection.close();
        }

    }


//SQL
//--------------------------------------------------------------------------------------------------------------------------------  
        /**
     * This method generates an SQL query to set the condition of the RentalMovie
     * eg
     * UPDATE VideoRental
     * SET Condition = 'lost'
     * WHERE (RentalID = 1021 AND SKU = 10010010011)
     * @param barcodeNum
     * @param condition
     * @return
     */
    private String setConditionGenerateSQL(String condition)
    {
        String where = "(RentalID = '" + rentalID
                + "' AND SKU = '" + SKU+ "')";
        String query = generateUpdateSQL("VideoRental", "Condition", condition, where);
        
        return query;
    }
    /**
     * Refactored code.  Generates a WHERE statement to specify the specific
     * RentalMovie that has the barcodeNum
     * @param barcodeNum
     * @return
     */
    private String generateMovieWhere()
    {
        splitBarcode();
        String where = "RentalID = '" + rentalID + "'"
                + "AND SKU = '" + SKU+ "'";
        return where;
    }
    
     /**
     * Generates a delete query
     * DELETE FROM tableName WHERE columns[0] = 'keys[0]' AND ... AND
     * columns[n] = 'keys[n]'
     * @param tableName
     * @param columns
     * @param keys
     * @return
     * @throws SQLException
     */
    private String generateDeleteSQL(String tableName, String[] columns)
            throws SQLException
    {
        String query = "DELETE FROM "+tableName+" WHERE ";
       
            query = query + columns[0] + " = '"+SKU+"'"
                    + " AND "+columns[1] +" = '"+rentalID+"'";
          
        
        return query;

    }

    /**
     * Generates an sql query for inserting a new row into a table
     * @param tableName
     * @param columnNames an array containing the names of the columns to change
     * @param values an array containing the values ot put in the columns
     * @return
     * @throws SQLExceptoin
     */
    private static String generateInsertSQL(String tableName, String[] columnNames, String[] values)
            throws SQLException
    {
        if (columnNames.length != values.length)
        {
            throw new SQLException("SQLException: column/value mismatch");
        }
        String query = "INSERT INTO "+tableName+" (";
        for (int i = 0; i < columnNames.length; i++)
        {
            query += columnNames[i];
            if (i != columnNames.length - 1)
            {
                query += ", ";
            }
        }
        query += ") VALUES (";
        for (int i = 0; i < values.length; i++)
        {
            query = query + "'"+values[i]+"'";
            if (i != values.length - 1)
            {
                query += ", ";
            }
        }
        query += ")";
        return query;
        
    }
	
        /**
     * This method generates a simple sql query for updating a table
     * @param tableName
     * @param attributeName
     * @param setAttributeTo
     * @param whereCondition
     * @return
     */
    private static String generateUpdateSQL(String tableName, String attributeName,
            String setAttributeTo, String whereCondition)
    {
        String command = "UPDATE " + tableName
                + " SET " + attributeName + " = '" + setAttributeTo + "'"
                + " WHERE " + whereCondition;
        return command;

    }
    private String [] createGetAvailabilitySQL(ArrayList<GeneralMovie> movies)
    {
	return new String[0];
    }
    
         /**
     * This method sends an update query to the Database
     * @param query an SQL query
     * @return the number of rows affected.
     * @throws SQLException
     */
    private int updateDatabase(String query) throws Exception, SQLException
    {        
        try
        {
		int rows = JDBC.update(query);
		return rows;
        }
        finally
        {
            connection.close();
        }

    }
	private String barcode;
	private String SKU;
	private String rentalID;
        private JDBCConnection JDBC;
        private Connection connection;
	private GregorianCalendar dueDate;
	private RentalMovie movie;
        final private int rental_period= 7;
	final public int RENTAL_ID_LENGTH = 9;
	final public static int MIN_SKU_LENGTH = 10;
	final public int MAX_SKU_LENGTH = 18;
}
