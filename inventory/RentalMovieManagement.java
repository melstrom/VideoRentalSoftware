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
import pos.Penalty;
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
 * Class usage - method call sequence
 * 
 *  RentalMovieManagement() -> setCurrentCopy(RentalMovie / String barcode) -> action()
 *  
 *  special case: getAvailability(ArrayList<GeneralMovie> movies)
 *
 * Program logic:
 *
 * -set current copy
 * -call a method that uses or does something to the current copy
 * -the method will automatically calls a query method
 * -the query method then automatically generates a corresponding query using the generic query generators and execute it
 *
 * Very simple but could run a bit slow if you want to call the setters many times at once.
 */
public class RentalMovieManagement {

    /**
     * Default constructor
     * initialize JDBC connection, default formats, conditions, categories, and currentTime,
     */
    public RentalMovieManagement()throws SQLException, ClassNotFoundException
    {
      JDBC = new JDBCConnection();
      connection = JDBC.getConnection();
      String []formats= { "Blu-ray", "DVD", "VHS"};
      String []conditions = {"lost", "broken","reserved", "available", "rented", "overdue"};
      String []categories = {"new release", "7 day", "for sale"};
      setFormatList(formats);
      setConditionList(conditions);
      setCategoryList(categories);
      getCurrentTime();
    }

    
    //Reservation 
    //--------------------------------------------------------------------------------------------------------------------------------

    /**
     * Reserve a movie
     * @param customer the customer that is reserving the movie
     * @throws SQLException
     * @throws Exception
     * @pre the current movie must be available
     */
    public void makeReservation(Customer customer)throws SQLException, Exception
    {
        if(!movie.getCondition().equals("reserved"))
        {
            Calendar currentTime = Calendar.getInstance();
            today.setTime(currentTime.getTime());
            today.set(today.get(today.YEAR),today.get(today.MONTH),today.get(today.DATE));
            Reservation reservation = new Reservation (customer.getAccountID(), today);
            movie.reservationEnqueue(reservation);
            reservationQuery();
        }

    }

    /**
     * Remove a reservation from top of the list
     * @param customer the customer that has made the reservation
     * @throws SQLException
     * @throws Exception
     * @pre there is at least one reservation that is recorded for this customer
     */
    public void removeReservation(Customer customer)throws SQLException, Exception
    {
	    movie.reservationDequeue();
            removeReservationQuery(customer.getAccountID());
    }
    
    
    
    //Gets and sets
    //--------------------------------------------------------------------------------------------------------------------------------

    /**
     * Set the current copy of the movie that is going to be edited/read
     * @param barcode
     * @pre the input barcode must be valid
     * @pre the input barcode exists in the database
     * @post one movie copy is selected
     */
   public void setCurrentCopy(String barcode)
   {
	//Search search = new Search();
	//movie = previewIndividualMovie(barcode);
	if(movie!= null)
	{
		this.barcode = barcode;
		splitBarcode();
	}
   }
   /**
    * Set the current copy of the movie that is going to be edited/read
    * @param movie the selected movie
    * @post one movie copy is selected
    */
   public void setCurrentCopy(RentalMovie movie)
   {
	this.movie = movie;
	barcode = null;
   }
   /**
    * Initialize a set of default movie formats
    * @param formats the movie formats
    */
   private void setFormatList(String formats[])
   {
        System.arraycopy(formats, 0, this.formats, 0, formats.length);
   }
   /**
    * Initialize a set of default movie conditions characteristics
    * @param conditions a set of conditions
    */
   private void setConditionList(String conditions[])
   {
       System.arraycopy(conditions, 0, this.conditions, 0, conditions.length);
   }
   /**
    * Initialize a set of default movie categories
    * @param categories a set of movie categories
    */
   private void setCategoryList(String categories[])
   {
       System.arraycopy(categories, 0, this.categories, 0, categories.length);
   }


    /**
     * Alternate call of editCopy(String,String[])
     * Changes the attributes of the RentalMovie to the specified ones.
     * If you don't want to change any attributes, then send them as null
     * @param category the category of the movie
     * @param format the format of the movie
     * @param condition the condition of the movie
     * @throws MovieNotFoundException
     * @throws SQLException
     * @throws java.lang.Exception
     * @pre current copy is selected/not null
     */
    public void editCopy(String category, String format, String condition)
            throws MovieNotFoundException, SQLException, java.lang.Exception
    {
    	setCategory(category);
        setFormat(format);
        setCondition(condition);
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
     * Get the availability of a list of general movies
     * @param movies a list of movies
     * @return a list of availabilities
     * @post an array of availabilities in String
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
     * @return A string specifying the condition of the movie
     * @throws MovieNotFoundException if the barcode does not correspond to
     * any rental movie.
     * @throws SQLException if the RentalMovie object cannot be created
     * because of lack of connection to the database.
     * @return the condition of the movie
     * @pre current movie is selected / not null
     */
    public String getCondition()
            throws MovieNotFoundException, SQLException
    {
        return movie.getCondition();
    }

    /** This method sets the condition of the instanced RentalMovie to a specified
     * value and updates the database.
     * @param condition The condition that you want to set for the instanced
     * RentalMovie.  It must match either good, lost, or broken, case insensitive.
     * @throws IllegalArgumentException if the condition does not match
     * @throws SQLException if a connection to the database cannot be made or if
     * more than one row is changed
     * @throws MovieNotFoundException
     * @pre the input condition is one of the conditions in conditions[]
     * @post one and only one row is changed
     */
    private void setCondition(String condition)
            throws IllegalArgumentException, SQLException,
                MovieNotFoundException, Exception
    {
        if (condition == null)
            return;
	
	movie.setCondition(condition.toLowerCase());
        setConditionQuery(condition);
       
    }

    /**
     * Get the rental period of the current movie
     * @throws SQLException
     * @throws MovieNotFoundException
     * @throws Exception
     * @post an integer field is returned from the query as a String
     */
    public void getRentalPeriod() throws SQLException, MovieNotFoundException, Exception
    {
	    String period = getRentalPeriodQuery();
	    rentalPeriod = Integer.parseInt(period); 
    }
    
      /**
     * Gets the category of the movie
     * @param barcodeNum the barcodeNumber of the RentalMovie
     * @return the category of the movie
     * @throws SQLException
     * @throws MovieNotFoundException
     * @pre the current movie is selected/not null
     */
    public String getCategory()
            throws SQLException, MovieNotFoundException
    {
        return movie.getCategory();
    }
    
    /**
     * Gets the format of the specified movie
     * @return the format of the movie
     * @throws SQLException
     * @throws MovieNotFoundException
     * @pre the current movie is selected/not null
     */
    public String getFormat()
            throws SQLException, MovieNotFoundException
    {
        return movie.getFormat();
    }
    
    /**
     * This method gets the specified movie's title
     * @return
     * @throws SQLException
     * @throws MovieNotFoundException
     * @pre the current movie is selected /not null
     */
    public String getTitle()
            throws SQLException, MovieNotFoundException
    {
        return movie.getTitle();
    }

    /**
     * Set the format of the current movie
     * @param format the format
     * @throws java.lang.Exception
     * @pre the format is one of the formats in formats[]
     * @post only one row is changed
     */
    private void setFormat(String format)throws java.lang.Exception
    {
	    if(format!= null)
	    {
                for(int i = 0; i < formats.length; i++)
                {
                    if(format.equals(formats[i]))
                    movie.setFormat(format);
                    setFormatQuery(format);
                }
	    }
    }	

    //????????????????????????????????????????????????????????????????????????????
    //method not implemented
    /**
     * Get the penalty of the customer
     * @param customerID the customerID
     * @return the overdue penalty the customer must pay before checkout another movie
     * @pre input customerID exists in the database
     * @post the result is a price in cents
     */
    public int getPenalty(int customerID)
    {
        //Search search = new Search();
        //Customer customer = search.getCustomer(customerID);
        //Penalty penalty = new Penalty();
        return 0;
    }

    /**
     * Set category for the current movie
     * @param category the movie category
     * @throws SQLException
     * @throws IllegalArgumentException
     * @throws MovieNotFoundException
     * @pre input category is one of the categories in categories[]
     * @post only one row is changed
     */
    private void setCategory(String category)
            throws SQLException, IllegalArgumentException, MovieNotFoundException, Exception
    {
        movie.setCategory(category);
        setCategoryQuery(category);
    }    
    
    //General rental
    //--------------------------------------------------------------------------------------------------------------------------------

    /**
     * This method rents out a RentalMovie, updating both the RentalMovie
     * and the Customer's account in the database.
     * @param barcodeNum the barcode number of the RentalMovie
     * @param memberID the unique ID of the customer who is renting
     * @return the due date of the movie
     * @throws MovieNotFoundException if the movie does not exist
     * @throws CustomerNotFoundException if the customer does not exist
     * @throws MovieNotAvailableException if the movie is not available
     * @throws SQLExeption
     * @throws Exception
     * @pre the memberID must correspond to an existing member
     * @pre the movie's condition must be available
     * @pre customer must pay off overdue penalties before checkout another movie
     */
    public GregorianCalendar checkOut(String memberID)
            throws MovieNotFoundException, CustomerNotFoundException,
            MovieNotAvailableException, SQLException, Exception
    {
	    
        if(getPenalty(Integer.parseInt(memberID))==0)
        {
                checkOutQuery(memberID);

                dueDate.set(dueDate.get(dueDate.YEAR),dueDate.get(dueDate.MONTH),dueDate.get(dueDate.DATE)+rentalPeriod);
                movie.setCondition("rented");
        }

        
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
    private GregorianCalendar checkOut(Customer customer)
            throws MovieNotFoundException, CustomerNotFoundException,
            MovieNotAvailableException, SQLException, Exception
    {
        if(getPenalty(customer.getAccountID())==0)
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
    
            checkOutQuery(""+customer.getAccountID());
           
    
    
            movie.setCondition("rented");
    
            dueDate.set(dueDate.get(dueDate.YEAR),dueDate.get(dueDate.MONTH),dueDate.get(dueDate.DATE)+rentalPeriod);
            }

        return dueDate;
    }

    
    //Utilities
    //--------------------------------------------------------------------------------------------------------------------------------  

    
    
    /**
     * This method splits a barcode into its SKU and rentalID 
     * @throws IllegalArguementException
     * @pre barcode is set/not null
     * @post if the barcode is longer than MAX_SKU_LENGTH, it is split into rentalID and SKU
     * @post if the barcode is between MIN_SKU_LENGTH and MAX_SKU_LENGTH, barcode is assign to SKU
     */
    private void splitBarcode()
            throws IllegalArgumentException
    {
        if (barcode == null )
            throw new IllegalArgumentException("IllegalArgumentException: Invalid barcode number");
	
        if(barcode.length() > MAX_SKU_LENGTH)
        {
            rentalID = barcode.substring(barcode.length());
            SKU = barcode.substring(0, barcode.length());
        }
        else if(barcode.length() >= MIN_SKU_LENGTH && barcode.length()<= MAX_SKU_LENGTH)
        SKU = barcode;
    }

    /**
     * Set currentTime 
     */
    private void getCurrentTime()
    {
         Calendar today = Calendar.getInstance();
         this.today.setTime(today.getTime());
    }
    


//SQL
//--------------------------------------------------------------------------------------------------------------------------------  
   /**
    * Create a query to check out a movie
    * @param accountID the accountID of the customer
    * @throws SQLException
    * @throws Exception
    * @pre accountID exists in the database
    * @pre currentMovie is set/not null
    * @post a line is added to Reservation table
    */
    private void checkOutQuery(String accountID)throws SQLException, Exception
    {
        String table = "videoRental";
        String where =" where rentalID = "+quote+rentalID+quote;
        String query = generateUpdateSQL (table, "condition", "rented", where);
        updateDatabase(query);
        table = "madeReservation";
        String []columns = {"dateTime","SKU", "customerID"};
        String date = ""+today.get(today.YEAR)+"/"+today.get(today.MONTH)+"/"+today.get(today.DATE);
        String []values = {date, SKU, accountID};
        query = generateInsertSQL(table, columns, values);
        updateDatabase(query);
    }
    /**
     * Create a query to make a reservation
     * @throws SQLException
     * @throws Exception
     * @pre currentMovie is set/not null
     * @post a line is added to Reservation table
     * @post a line is added to madeReservation table
     */
    private void reservationQuery( )throws SQLException, Exception
    {
       String tablename = "Reservation";
       String []columnNames = {"reservationID","datetime","SKU"};
       String date = ""+today.get(today.YEAR)+"/"+today.get(today.MONTH)+"/"+today.get(today.DATE);
       String []values = {"1",date ,SKU };
       String query = generateInsertSQL(tablename, columnNames,values);
       updateDatabase(query);
    }
    /**
     * Create a query to remove a reservation 
     * @param accountID the accountID of the customer
     * @throws SQLException
     * @throws Exception
     * @pre input accountID exists in the database
     * @post a line is deleted from the Reservation table
     */
    private void removeReservationQuery(int accountID)throws SQLException, Exception
    {
        String tablename = "Reservation";
        String where = "where accountID = customer.accountID and accountID ="+accountID+ "and SKU = "+SKU;
        String query = generateDeleteSQL(tablename, where);
        updateDatabase(query);
    }
    
    
    /**
     * Create a query to get the rental period (in days) for the currentMovie
     * @return result the number of days
     * @throws SQLException 
     * @throws Exception
     * @pre currentMovie is set /not null
     * @post an integer field received from the database
     */
    private String getRentalPeriodQuery()throws SQLException, Exception
    {
        String category = movie.getCategory();
        String tablename = "catagories", field = "rentalLength";
        String where = "where videoRental.catagory = catagories.catagory "
                    +"and catagory ="+quote+category+quote;
        String query = generateSelectSQL(tablename, field, where);
        ResultSet resultSet = JDBC.getResults(query);
        String result = resultSet.getString("rentalLength");

        return result;
    }
    
    /**
     * Changes a rental movie to a sale movie and updates the database
     * @param barcodeNum
     * @throws MovieNotFoundException
     * @throws SQLException
     * @pre the currentMovie is selected/not null
     * @pre the category of the currentMovie is not for_sale
     * @pre the condition of the currentMovie is available
     * @post a line is added to videoRental
     * @post a line is removed from videoSale
     */
    private void changeToSaleQuery() throws SQLException, MovieNotFoundException,Exception
    {
        if(!movie.getCondition().equals("broken"))
        {
            //add new line in sales table, remove from rentals table
            String table = "videoRental";
            String where = "where SKU ="+quote+SKU+quote;
            String query = generateDeleteSQL(table, where);
            updateDatabase(query);
            String[]columnNames = {"SaleID","condition","catagory", "SKU"};
            String[] values = { rentalID, movie.getCondition(), movie.getCategory(),SKU };
            table = "videoSale";
            query = generateInsertSQL(table, columnNames,values);
            updateDatabase(query);
        }
        
    }
    
    /**
     * Sets the category of the currentMovie
     * @param category the category to change to
     * @throws SQLException
     * @throws MovieNotFoundException
     * @throws IllegalArgumentException if the category is not a valid category
     * @pre currentMovie is set/ not null
     * @pre input category is one of the categories from categories[]
     * @pre rentalID is not null
     * @post a line is updated in videoRental
     */
    public void setCategoryQuery(String category)
            throws SQLException, MovieNotFoundException, Exception
    {

         for(int i =0; i<categories.length; i++)
        {
            if(category.equals(categories[i]))
            {
                String table = "videoRental", attribute = "catagory", attributeTo = category;
                String where = " where rentalID="+quote+rentalID+quote;
                String query = generateUpdateSQL(table, attribute,attributeTo, where);
                 updateDatabase(query);
            }
        }
  
    }
    /**
     * Set the format of the currentMovie
     * @param format the format
     * @throws SQLException
     * @throws MovieNotFoundException
     * @throws Exception
     * @pre currentMovie is set/not null
     * @pre rentalID is not null
     * @pre input format is one if the formats from formats[]
     * @post a line is updated in videoRental
     */
    public void setFormatQuery(String format)
            throws SQLException, MovieNotFoundException, Exception
    {

         for(int i =0; i<formats.length; i++)
        {
            if(format.equals(formats[i]))
            {
                String table = "videoRental", attribute = "format", attributeTo = format;
                String where = " where rentalID="+quote+rentalID+quote;
                String query = generateUpdateSQL(table, attribute,attributeTo, where);
                 updateDatabase(query);
            }
        }

    }
    /**
     * This method generates an SQL query to set the condition of the RentalMovie
     * eg
     * UPDATE VideoRental
     * SET Condition = 'lost'
     * WHERE (RentalID = 1021 AND SKU = 10010010011)
     * @param condition
     * @throws SQLException
     * @throws MovieNotFoundException
     * @throws Exception
     * @pre currentMovie is set/not null
     * @pre input condition is one of the condition from conditions[]
     * @pre rentalID is not null
     * @post a line is updated in videoRental
     */
    private void setConditionQuery(String condition)throws SQLException, MovieNotFoundException, Exception
    {
        for(int i =0; i<conditions.length; i++)
        {
            if(condition.equals(conditions[i]))
            {
                String table = "videoRental", attribute = "condition", attributeTo = condition;
                String where = " where rentalID="+quote+rentalID+quote;
                String query = generateUpdateSQL(table, attribute,attributeTo, where);
                updateDatabase(query);
            }
        }
    }


    
     /**
     * Generates a delete query
     * DELETE FROM tableName WHERE columns[0] = 'keys[0]' AND ... AND
     * columns[n] = 'keys[n]'
     * @param tableName the table name in database
     * @param where a where statement
     * @return a query
     * @throws SQLException
     * @pre tablename exists in the database
     * @post a query is generated
     */
    private String generateDeleteSQL(String tableName,String where )
            throws SQLException
    {
        String query = "DELETE FROM "+tableName+ " "+where;
      
        return query;
    }

    /**
     * Generates an sql query for inserting a new row into a table
     * @param tableName
     * @param columnNames an array containing the names of the columns to change
     * @param values an array containing the values ot put in the columns
     * @return a query
     * @throws SQLExceptoin
     * @pre tablename exists in the database
     * @pre value of columnNames[] are column titles of tablename
     * @post a query is generated
     */
    private String generateInsertSQL(String tableName, String[] columnNames, String[] values)
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
     * @param tableName the tablename
     * @param attributeName the selected column name
     * @param setAttributeTo the value the selected attribute will change to
     * @param whereCondition a where clause
     * @return a query
     * @pre tablename exists in the database
     * @pre attributeName is one of the columns of tablename
     * @post a query is generated
     */
    private static String generateUpdateSQL(String tableName, String attributeName,
            String setAttributeTo, String whereCondition)
    {
        String command = "UPDATE " + tableName
                + " SET " + attributeName + " = '" + setAttributeTo + "'"
                + " WHERE " + whereCondition;
        return command;

    }
    /**
     * Generate a select query
     * @param tablename the tablename
     * @param field the field to be selected
     * @param where a where clause
     * @return a query
     * @pre tablename exists in the database
     * @pre field is one of the columns of tablename
     * @post a query is generated
     */
    private String generateSelectSQL(String tablename, String field, String where)
    {
        String query="select "+field+" from "+tablename +" "+where;
       return query;
    }

    //????????????????????????????????????????????????????????????????????????
    //imcomplete
    /**
     * Create a query that gets the number of available copies for a list of movies
     * @param movies
     * @return array of numbers in String
     * @post an array of String is returned
     */
    private String [] createGetAvailabilitySQL(ArrayList<GeneralMovie> movies)
    {
	return new String[0];
    }
    
     /**
     * This method sends an update query to the Database
     * @param query an SQL query
     * @return the number of rows affected.
     * @throws SQLException
     * @pre input query is checked
     * @post database is updated
     * @post number of lines affected is returned for verification
     */
    private int updateDatabase(String query) throws Exception, SQLException
    {        
        try
        {
            int rowsChanged = JDBC.update(query);
            if (rowsChanged > 1)
		    {
			    throw new SQLException("SQLException: database corrupted");
		    }
		    if (rowsChanged < 1)
		    {
                
			throw new MovieNotFoundException("MovieNotFoundException: "
				+ "cannot find barcode number");
		    }
		
		    return rowsChanged;
        }
        finally
        {
            connection.close();
        }

    }


    /**
     * Get the pickup date of currentMovie
     * @param movie 
     * @return a pickup date
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public GregorianCalendar getPickupDate(RentalMovie movie)
            throws SQLException, ClassNotFoundException
    {
        return new GregorianCalendar();
    }



    /**
     * This method finds the number of available copies of a GeneralMovie
     * @return a non-negative number indicating the number of copies available
     * or a negative number if the store does not carry any copies.
     * @throws MovieNotFoundException if the movie passed is not known to
     * the video store
     *
     */
    public int getAvailableCopies()
            throws MovieNotFoundException, SQLException, ClassNotFoundException
    {
        String SKU = movie.getSKU();
        String query =
                JDBCConnection.makeQuery("videoRental", "COUNT(*)", "videoRental.condition = 'available'");
        JDBCConnection connection = new JDBCConnection();
        try
        {
            ResultSet result = connection.getResults(query);
            if (!result.next())
            {
                return -1;
            }
            Integer numAvailableCopies = result.getInt(1);
            if (result.wasNull())
            {
                return -1;
            }
            return numAvailableCopies;

        }
        finally
        {
            connection.closeConnection();
        }
    }





    private String barcode;
    private String SKU;
    private String rentalID;
    private JDBCConnection JDBC;
    private Connection connection;
    private GregorianCalendar dueDate; 
    private GregorianCalendar today;
    private RentalMovie movie;
    final public int RENTAL_ID_LENGTH = 9;
    final public static int MIN_SKU_LENGTH = 10;
    final public int MAX_SKU_LENGTH = 18;
    private String []formats;
    private String []conditions;
    private String []categories;
    private int rentalPeriod;
    final char quote = '\'';
    final char comma = ',';

}
