package inventory;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import search.Search;
import inventory.MovieNotFoundException;
import inventory.MovieNotAvailableException;
import inventory.GeneralMovie;
import inventory.IndividualMovie;
import inventory.RentalMovie;
import inventory.Reservation;
import pos.Penalty;
import account.CustomerNotFoundException;
import account.Customer;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.Locale;
import jdbconnection.JDBCConnection;

/**
 * This is a utility class that encapsulates the interface of all classes that
 * deal with managing rental movies.
 *
 *
 * Last updated: 1 April
 * @author Mitch
 *
 *-editor matt
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
      this.formats = new String[3];
      this.conditions = new String[6];
      this.categories = new String[3];
      String []conditions = {"lost", "broken","reserved", "available", "rented", "overdue"};
      String []categories = {"new release", "7 day", "for sale"};
      setFormatList(formats);
      setConditionList(conditions);
      setCategoryList(categories);
      getCurrentTime();
      dueDate = new GregorianCalendar();
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
            //movie.reservationEnqueue(reservation);
           reservationQuery(""+customer.getAccountID());
            
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
	    //movie.reservationDequeue();
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
   public void setCurrentCopy(String barcode)throws MovieNotFoundException,SQLException,ClassNotFoundException,java.lang.Exception
   {
	Search search = new Search();
	movie = search.previewIndividualMovie(barcode);
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
   public void setCurrentCopy(IndividualMovie movie)
   {
	this.movie = movie;
	barcode = movie.getBarcode();
       splitBarcode();

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
    public void setCondition(String condition)
            throws IllegalArgumentException, SQLException,
                MovieNotFoundException, Exception
    {
        if (condition == null)
            return;
	
	movie.setCondition(condition.toLowerCase());
        setConditionQuery(condition);
       
    }
    
    
    
    /**
     * This method finds the rental period in days of a particular movie,
     * given its barcode number.
     * @param barcode the barcode of the IndividualMovie, or the SKU of a
     * GeneralMovie
     * @return -1 if it is not a proper ID
     * @throws SQLException
     * @throws MovieNotFoundException
     * @throws ClassNotFoundException
     * @throws Exception
     */
    public static int getRentalPeriod(String barcode) 
            throws SQLException, MovieNotFoundException, ClassNotFoundException,java.io.IOException
    {
        String category;
        String query = JDBCConnection.makeQuery("catagories", 
                "catagories.rentalLength", 
                "catagories.catagory = ?");
        if (barcode.length() < GeneralMovie.MIN_SKU_LENGTH)
        {
            return -1;
        }
        else if (barcode.length() <= GeneralMovie.MAX_SKU_LENGTH)
        {
            category = getGeneralMovieCategory(barcode);
        }
        else
        {
            IndividualMovie movie =  Search.previewIndividualMovie(barcode);
            category = movie.getCategory();
        }

        if (category == null)
        {
            return -1;
        }

        int numParam = 1;
        String[] params = { category };
        JDBCConnection connection = new JDBCConnection();
        try
        {
            ResultSet result = connection.getResults(query, numParam, params);
            result.next();
            int rentalPeriod = result.getInt(1);
            return rentalPeriod;
        }
        finally
        {
            connection.closeConnection();
        }
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
     * Finds the category that a GeneralMovie's rental copies belong to.
     * @pre A GeneralMovie does not have rental copies belonging to more than
     * one category.  It may have for sale copies, however.
     * @param SKU the SKU of the GeneralMovie
     * @return null if no rental copies exist, or the category that the 
     * rental copies belong to
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private static String getGeneralMovieCategory(String SKU)
            throws SQLException, ClassNotFoundException
    {
        String query = JDBCConnection.makeQuery("videoRental",
                "videoRental.catagory",
                "videoRental.SKU = ? AND NOT videoRental.catagory = ?");
        int numParam = 2;
        String[] param = { SKU, "for sale" };
        JDBCConnection connection = new JDBCConnection();
        try
        {
            ResultSet result = connection.getResults(query, numParam, param);
            result.next();
            if (result.wasNull())
            {
                return null;
            }
            return result.getString(1);
        }
        finally
        {
            connection.closeConnection();
        }
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
    public void setFormat(String format)throws java.lang.Exception
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
    public int getPenalty(int customerID)throws SQLException, java.lang.Exception
    {
        Search search = new Search();
        Customer customer = search.getCustomer(customerID);
        //Penalty penalty = new Penalty();

       // String id = customer.getFname();
        //System.out.println(id);

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
    public void setCategory(String category)
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
    public GregorianCalendar checkOut(Customer customer)
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
  
            
            }
       dueDate.set(today.get(today.YEAR), today.get(today.MONTH), today.get(today.DATE+rentalPeriod));
       return dueDate;
    }


    public void checkIn()throws SQLException, Exception,java.lang.Exception
    {
        checkInQuery();
    }
    /**
     * Change a movie from type rental to sale
     * @throws SQLException
     * @throws MovieNotFoundException
     * @throws java.lang.Exception
     */
    public void changeToSales()throws SQLException,MovieNotFoundException,java.lang.Exception
    {
        changeToSaleQuery();
    }
    /**
     * change a movie from type sale to rental
     * @throws SQLException
     * @throws MovieNotFoundException
     * @throws java.lang.Exception
     */
    public void changeToRental()throws SQLException,MovieNotFoundException,java.lang.Exception
    {
        changeToRentalQuery();
    }
    
    //Utilities
    //--------------------------------------------------------------------------------------------------------------------------------  

    
    
    /**
     * This method splits a barcode into its SKU and rentalID 
     * @param barcode the full barcode of a movie
     * @param splitBarcode a working array where the two halves of the split
     * barcode will be written
     * @return splitBarcode will contain SKU in its first index, and the 
     * rentalID or saleID portion of the barcode in the second index.  If there
     * is not rentalID or saleID, null will be written there.
     * @throws IllegalArguementException
     * @pre barcode is set/not null
     * @pre splitBarcode has length 2, the first index is used to store the SKU
     * and the second index will be used to store the rentalID or saleID
     * @post if the barcode is longer than MAX_SKU_LENGTH, it is split into rentalID and SKU
     * @post if the barcode is between MIN_SKU_LENGTH and MAX_SKU_LENGTH, barcode is assign to SKU
     */
    private static void splitBarcode(String barcode, String[] splitBarcode)
            throws IllegalArgumentException
    {
        if (barcode == null )
            throw new IllegalArgumentException("IllegalArgumentException: Invalid barcode number");
        int barcodeLength = barcode.length();
        final int SKU_INDEX = 0;
        final int COPY_NUM_INDEX = 1;
        if (barcodeLength >= GeneralMovie.INFO_ID_LENGTH 
                && barcodeLength <= GeneralMovie.MAX_SKU_LENGTH)
        {
            splitBarcode[SKU_INDEX] = barcode;
            splitBarcode[COPY_NUM_INDEX] = null;
        }
        else if (barcodeLength <= GeneralMovie.MAX_SKU_LENGTH + IndividualMovie.ID_LENGTH)
        {
            
            int copyNumStartIndex = barcodeLength - IndividualMovie.ID_LENGTH;
            String copyNum = barcode.substring(copyNumStartIndex);
            String SKU = barcode.substring(0, copyNumStartIndex);
            splitBarcode[SKU_INDEX] = SKU;
            splitBarcode[COPY_NUM_INDEX] = copyNum;
        }
        else
        {
            throw new IllegalArgumentException("Not a valid barcode");
        }
	/*
        if(barcode.length() > MAX_SKU_LENGTH)
        {
            rentalID = barcode.substring(barcode.length());
            SKU = barcode.substring(0, barcode.length());
        }
        else if(barcode.length() >= MIN_SKU_LENGTH && barcode.length()<= MAX_SKU_LENGTH)
        SKU = barcode;
         * 
         */
    }
    
    
    
    /**
     * 
     * @throws IllegalArgumentException
     */
    private void splitBarcode()
            throws IllegalArgumentException
    {
        int barcodeLength = barcode.length();
        if (barcodeLength >= GeneralMovie.MIN_SKU_LENGTH)
        {
            int copyNumStartIndex = barcodeLength - RENTAL_ID_LENGTH;
            String copyNum = barcode.substring(RENTAL_ID_LENGTH+3);
            String SKU = barcode.substring(0, copyNumStartIndex);
            this.SKU = SKU;
            rentalID = copyNum;
        }
        else
        {
            throw new IllegalArgumentException("Not a valid barcode");
        }
        System.out.println(this.SKU+"-"+rentalID);
    }

    /**
     * Set currentTime 
     */
    private void getCurrentTime()
    {
         Calendar today = Calendar.getInstance();
         this.today = new GregorianCalendar();
         this.today.set(today.get(today.YEAR),today.get(today.MONTH),today.get(today.DATE));
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
        String query = generateUpdateSQL (table, "videoRental.condition", "rented", where);
        //updateDatabase(query);
        String anum = "794043140624";
    
        table = "madeReservations";
        String []columns = {"dateTime","SKU", "customerID"};
        String date = ""+today.get(today.YEAR)+"/"+today.get(today.MONTH)+"/"+today.get(today.DATE);
        String []values = {date, SKU, "1"};
        query = generateInsertSQL(table, columns, values);
        updateDatabase(query);
    }

    private void checkInQuery()throws SQLException, Exception,java.lang.Exception
    {
        String tablename = "videoRental";
        String attribute = "videoRental.condition";
        String attributeTo = "available";
        String where = " where rentalID="+quote+rentalID+quote+" and SKU="+quote+SKU+quote;
        String query = generateUpdateSQL(tablename, attribute, attributeTo, where);
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
    private void reservationQuery(String accountID)throws SQLException, Exception
    {
       String tablename = "Reservation";
       String []columnNames = {"reservationID","datetime","SKU"};
       String date = ""+today.get(today.YEAR)+"/"+today.get(today.MONTH)+"/"+today.get(today.DATE);
       String []values ={accountID,date ,SKU };
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
        String where = "where reservationID ="+quote+accountID+quote+ " and SKU = "+quote+SKU+quote;
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
    private void changeToRentalQuery() throws SQLException, MovieNotFoundException,Exception
    {
        if(!movie.getCondition().equals("broken"))
        {
            //add new line in sales table, remove from rentals table
            String table = "videoSale";
            String where = "where SKU ="+quote+SKU+quote;
            String query = generateDeleteSQL(table, where);
            updateDatabase(query);
            String[]columnNames = {"RentalID","condition","catagory", "SKU"};
            String[] values = { rentalID, movie.getCondition(), movie.getCategory(),SKU };
            table = "videoRental";
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
                String table = "videoRental", attribute = "videoRental.format", attributeTo = format;
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
                String table = "videoRental", attribute = "videoRental.condition", attributeTo = condition;
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
        System.out.println(query);
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

        System.out.println(query);
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
                + " SET " + attributeName + " = "+"'" + setAttributeTo + "'"
                +whereCondition;
         System.out.println(command);
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
         System.out.println(query);
       return query;
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



   /* public void reserve(Customer customer)
            throws MovieNotFoundException, ClassNotFoundException, SQLException
    {
        // TODO: check for customer holds
        int accountID = customer.getAccountID();
        JDBCConnection connection = new JDBCConnection();
        try
        {
            if (movie.getCondition().equalsIgnoreCase("available"))
            {
                String query = JDBCConnection.makeUpdate("rentalMovie", "condition = ?", "rentalID = ?");
                String condition = "reserved";
                String rentalID = movie.getBarcode().replaceAll(movie.getSKU(), "");
                int numParam = 2;
                String[] params = { condition, rentalID };
                connection.update(query, numParam, params);
                movie.setCondition(condition);
            }
            else
            {
                String dateTime = "" + today.get(Calendar.YEAR) + "-";
                dateTime = dateTime + today.get(Calendar.MONTH) + "-";
                dateTime = dateTime + today.get(Calendar.DATE+7);
                String[][] information = {
                    {"dateTime", "madeReservations.SKU", "customerID"},
                    {dateTime, movie.getSKU(), "" + accountID}
                };
                String query = JDBCConnection.makeInsert("madeReservations", information);
                connection.update(query);
            }
        }
        finally
        {
            connection.closeConnection();
        }
    }*/





    /**
     * This method gets the first available pick up day for a rental movie.
     * It does not make any reservations.
     * @param movie the movie you want to reserve
     * @return the first available pick up date, or null if the general movie
     * does not have any rental copies
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public GregorianCalendar getPickupDate(GeneralMovie movie)
            throws SQLException, ClassNotFoundException, MovieNotFoundException,
            MovieNotAvailableException,java.io.IOException
    {
        return getPickupDate(movie.getSKU());
    }




    /**
     * This method gets the first available pick up day for a rental movie.
     * It does not make any reservations.
     * @param SKU the SKU of the movie you want to reserve
     * @return null if the SKU passed does not correspond to any rental movies,
     * otherwise the first available pick up date.
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws MovieNotFoundException
     * @throws MovieNotAvailableException
     */
    public GregorianCalendar getPickupDate(String SKU)
            throws SQLException, ClassNotFoundException, MovieNotFoundException,
            MovieNotAvailableException, java.io.IOException
    {
        int numTotalRentalCopies = getTotalRentalCopies(SKU);
        if (numTotalRentalCopies < 1)
        {
            throw new MovieNotAvailableException("There are no copies available" +
                    " for rental or reservation");
        }
        int numAvailableCopies = getAvailableCopies(SKU);
        if (numAvailableCopies > 0)
        {
            return new GregorianCalendar(); // the pick up day is today
        }

        ArrayList<Reservation> reservations = getReservations(SKU);
        int numReservations = reservations.size();
        
        int rentalPeriod = getRentalPeriod(SKU);
        
        if (rentalPeriod < 1)
        {
            return null;
            // should never get here
        }
        
        ArrayList<GregorianCalendar> dueDates = new ArrayList<GregorianCalendar>();
        for (Reservation reservation : reservations)
        {
            GregorianCalendar dueDate = reservation.getDate();
            dueDates.add(dueDate);
        }

        while (numReservations >= numTotalRentalCopies)
        {
            for (GregorianCalendar dueDate : dueDates)
            {
                dueDate.add(GregorianCalendar.DATE, rentalPeriod + RentalMovie.RENTAL_HOLD_PERIOD + 1);
                // the extra 1 is because the pick up day should be 1 day after the movie is returned
            }
            numReservations -= numTotalRentalCopies;
        }
        
        return dueDates.get(numReservations);
    }



    /**
     * This method creates a list of Reservation objects for the GeneralMovie
     * that the SKU belongs to.  Check the size of the arraylist after it is
     * returned in case there are 0 reservations.
     * TODO: needs testing
     * @param SKU
     * @return an arraylist of reservation objects
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static ArrayList<Reservation> getReservations(String SKU)
            throws SQLException, ClassNotFoundException
    {
        String query = JDBCConnection.makeQuery("madeReservations",
                "madeReservations.dateTime, madeReservations.accountID",
                "madeReservations.SKU = ?");
        int numParam = 1;
        String[] params = { SKU };
        JDBCConnection connection = new JDBCConnection();
        try
        {
            ResultSet results = connection.getResults(query, numParam, params);
            ArrayList<Reservation> reservations = new ArrayList<Reservation>();
            while (results.next())
            {
                Date date = results.getDate("madeReservations.dateTime");
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                int accountID = results.getInt("madeReservations.accountID");
                Reservation reservation = new Reservation(accountID, calendar);
                reservations.add(reservation);
            }
            return reservations;
        }
        finally
        {
            connection.closeConnection();
        }
    }



    /**
     * This method finds the total number of rentalCopies in the store.  It
     * considers anything that is not lost, broken, overdue, or for sale to be
     * a valid contributing member to the total.
     * @param SKU the SKU of the movie
     * @return -1 if no such SKU exists, or else the number of total copies
     * @throws MovieNotFoundException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static int getTotalRentalCopies(String SKU)
            throws MovieNotFoundException, SQLException, ClassNotFoundException
    {

        if (!exists(SKU))
        {
            return -1;
        }

        String query = JDBCConnection.makeQuery("videoRental",
                "COUNT(*)",
                "(NOT videoRental.condition = ?) " +
                "AND (NOT videoRental.condition = ?) " +
                "AND (NOT videoRental.condition = ?) " +
                "AND (NOT videoRental.catagory = ?) " +
                "AND (videoRental.SKU = ?)");

        int numParam = 5;
        String[] params = { "broken", "lost", "overdue", "for sale", SKU };

        JDBCConnection connection = new JDBCConnection();
        try
        {
            ResultSet result = connection.getResults(query, numParam, params);
            result.next();
            return result.getInt(1);
        }
        finally
        {
            connection.closeConnection();
        }
    }



    /**
     * This method finds the number of available copies of a GeneralMovie,
     * given that GeneralMovie's SKU
     * @param SKU the SKU of the GeneralMovie
     * @return a non-negative number indicating the number of copies available
     * or a negative number if the store does not carry any copies.
     * @throws MovieNotFoundException if the movie passed is not known to
     * the video store
     *
     */
    public static int getAvailableCopies(String SKU)
            throws MovieNotFoundException, SQLException, ClassNotFoundException
    {
        String query =
                JDBCConnection.makeQuery("videoRental",
                "COUNT(*)",
                "videoRental.SKU = ? AND videoRental.condition = ?");
        if (!exists(SKU))
        {
            return -1;
        }
        JDBCConnection connection = new JDBCConnection();
        try
        {
            String[] parameters = { SKU, "available" };
            int numParameters = 2;
            ResultSet result = connection.getResults(query, numParameters, parameters);
//            if (!result.next())
//            {
//                return -1;
//            }
            result.next();
            Integer numAvailableCopies = result.getInt(1);
//            if (result.wasNull())
//            {
//                return -1;
//            }
            return numAvailableCopies;

        }
        finally
        {
            connection.closeConnection();
        }
    }



    /**
     * This method finds out if a GeneralMovie is known to the database.
     * @param SKU the unique SKU of the GeneralMovie
     * @return true if it exists, false if it does not
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static boolean exists(String SKU)
            throws SQLException, ClassNotFoundException
    {
        String query = JDBCConnection.makeQuery("physicalVideo", "COUNT(*)", "physicalVideo.SKU = ?");
        int numParam = 1;
        String[] params = { SKU };
        JDBCConnection connection = new JDBCConnection();
        try
        {
            ResultSet result = connection.getResults(query, numParam, params);
            result.next();
            int numFound = result.getInt(1);
            return (numFound > 0);
        }
        finally
        {
            connection.closeConnection();
        }
    }
    
    
    /**
     * This method finds the number of available copies of a GeneralMovie
     * @param movie the GeneralMovie
     * @return a non-negative number indicating the number of copies available
     * or a negative number if the store does not carry any copies.
     * @throws MovieNotFoundException if the movie passed is not known to
     * the video store
     *
     */
    public static int getAvailableCopies(GeneralMovie movie)
            throws MovieNotFoundException, SQLException, ClassNotFoundException
    {
        return getAvailableCopies(movie.getSKU());
    }





    private String barcode;
    private String SKU;
    private String rentalID;
    private JDBCConnection JDBC;
    private Connection connection;
    private GregorianCalendar dueDate; 
    private GregorianCalendar today;
    private IndividualMovie movie;
    final public int RENTAL_ID_LENGTH = 9;
    final public static int MIN_SKU_LENGTH = 10;
    final public int MAX_SKU_LENGTH = 18;
    private String []formats;
    private String []conditions;
    private String []categories;
    final private int rentalPeriod = 7;
    final char quote = '\'';
    final char comma = ',';

}
