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
 *  RentalMovieManagement() -> setCurrentCopy(RentalMovie / String barcode) -> action()
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
    
    public void makeReservation(Customer customer)throws SQLException, Exception
    {
        if(!movie.getCondition().equals("reserved"))
        {
            Calendar today = Calendar.getInstance();
            dueDate.setTime(today.getTime());
            dueDate.set(dueDate.get(dueDate.YEAR),dueDate.get(dueDate.MONTH),dueDate.get(dueDate.DATE));
            Reservation reservation = new Reservation (customer.getAccountID(), dueDate);
            movie.reservationEnqueue(reservation);
            reservationQuery();
        }

    }
    
    public void removeReservation(Customer customer)throws SQLException, Exception
    {
	    movie.reservationDequeue();
            removeReservationQuery(customer.getAccountID());
    }
    
    
    
    //Gets and sets
    //--------------------------------------------------------------------------------------------------------------------------------
   
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
   
   public void setCurrentCopy(RentalMovie movie)
   {
	this.movie = movie;
	barcode = null;
   }
   
   public void setFormatList(String formats[])
   {
        System.arraycopy(formats, 0, this.formats, 0, formats.length);
   }

   public void setConditionList(String conditions[])
   {
       System.arraycopy(conditions, 0, this.conditions, 0, conditions.length);
   }

   public void setCategoryList(String categories[])
   {
       System.arraycopy(categories, 0, this.categories, 0, categories.length);
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
    private void setCondition(String condition)
            throws IllegalArgumentException, SQLException,
                MovieNotFoundException, Exception
    {
        if (condition == null)
            return;
	
	movie.setCondition(condition.toLowerCase());
        setConditionQuery(condition);
       
    }
    
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
     */
    public String getCategory()
            throws SQLException, MovieNotFoundException
    {
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
        return movie.getFormat();
    }
    
        /**
     * This method gets the specified movie's title
     * @param barcodeNum
     * @return
     * @throws SQLException
     * @throws MovieNotFoundException
     */
    public String getTitle()
            throws SQLException, MovieNotFoundException
    {
        return movie.getTitle();
    }
    
    private void setFormat(String format)throws java.lang.Exception
    {
	    if(format!= null)
	    {
            for(int i = 0; i < formats.length; i++)
            {
                if(format.equals(formats[i]))
                movie.setFormat(format);
            }
	    }
    }	
    
    public int getPenalty()
    {
        return 0;
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
     * @pre the barcode number must correspond to an existing movie
     * @pre the memberID must correspond to an existing member
     * @pre the movie's status must be available
     * @throws MovieNotFoundException if the movie does not exist
     * @throws CustomerNotFoundException if the customer does not exist
     * @throws MovieNotAvailableException if the movie is not available
     */
    public GregorianCalendar checkOut(String memberID)
            throws MovieNotFoundException, CustomerNotFoundException,
            MovieNotAvailableException, SQLException, Exception
    {
	    
        if(getPenalty()==0)
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
        if(getPenalty()==0)
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
            rentalID = barcode.substring(barcode.length());
            SKU = barcode.substring(0, barcode.length());
        }
        else if(barcode.length() >= MIN_SKU_LENGTH && barcode.length()<= MAX_SKU_LENGTH)
        SKU = barcode;
    }

    private void getCurrentTime()
    {
         Calendar today = Calendar.getInstance();
         this.today.setTime(today.getTime());
    }
    


//SQL
//--------------------------------------------------------------------------------------------------------------------------------  
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

    private void reservationQuery( )throws SQLException, Exception
    {
       String tablename = "Reservation";
       String []columnNames = {"reservationID","datetime","SKU"};
       String date = ""+today.get(today.YEAR)+"/"+today.get(today.MONTH)+"/"+today.get(today.DATE);
       String []values = {"1",date ,SKU };
       String query = generateInsertSQL(tablename, columnNames,values);
       updateDatabase(query);
    }
    
    private void removeReservationQuery(int accountID)throws SQLException, Exception
    {
        String tablename = "Reservation";
        String where = "where accountID = customer.accountID and accountID ="+accountID+ "and SKU = "+SKU;
        String query = generateDeleteSQL(tablename, where);
        updateDatabase(query);
    }
    
    
    
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
     * This method generates an SQL query to set the condition of the RentalMovie
     * eg
     * UPDATE VideoRental
     * SET Condition = 'lost'
     * WHERE (RentalID = 1021 AND SKU = 10010010011)
     * @param barcodeNum
     * @param condition
     * @return
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
     * @param tableName
     * @param columns
     * @param keys
     * @return
     * @throws SQLException
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
     * @return
     * @throws SQLExceptoin
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

    private String generateSelectSQL(String tablename, String field, String where)
    {
        String query="select "+field+" from "+tablename +" "+where;
       return query;
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
