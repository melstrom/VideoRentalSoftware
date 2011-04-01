package inventory;
import accounts.*;
import java.sql.SQLException;
import utility.SanitizerException;
import java.util.ArrayList;
import search.Search;
import inventory.MovieNotFoundException;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;

/**
 * This is a utility class that encapsulates the interface of all classes that
 * deal with managing rental movies.
 *
 *
 * Last updated: 31 March
 * @author Mitch
 * version 0.2
 *
 * still missing:
 * implementation for changeToSale, editCopy, reserve
 */
public class RentalMovieManagement {



    // methods

    public enum Status
    {
        AVAILABLE, RENTED, OVERDUE, RESERVED
    }

    /**
     * This method finds the availability of the specified RentalMovie
     * @param barcode the unique barcode number of the Rental Movie
     * @return the status of the RentalMovie
     * @pre The barcode number must correspond to a RentalMovie
     * @throws MovieNotFoundException if the barcode number does not exist
     * @throws SQLException if the RentalMovie information cannot be read from
     * the database
     */
    public static String getStatus(String barcodeNum)
            throws MovieNotFoundException, SQLException
    {
        RentalMovie rentalMovie = new RentalMovie(barcodeNum);
        return rentalMovie.getStatus();
    }



    /**
     * This method finds the availability status of a specified RentalMovie.
     * @param rentalMovie a reference to the Rental Movie we are interested in
     * @return the status of the RentalMovie
     * @throws MovieNotFoundException if the passed movie is null
     */
    private static String getStatus(RentalMovie rentalMovie)
            throws MovieNotFoundException
    {
        if (rentalMovie == null)
        {
            throw new MovieNotFoundException("MovieDoesNotExistException: "
                    + "No movie was supplied.");
        }
        return rentalMovie.getStatus();
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
    public static String getCondition(String barcodeNum)
            throws MovieNotFoundException, SQLException
    {
        RentalMovie rentalMovie = new RentalMovie(barcodeNum);
        return rentalMovie.getCondition();
    }


    /**
     * These are the possible conditions of a RentalMovie
     */
    public enum Condition
    {
        GOOD, LOST, DAMAGED
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
    private static void setCondition(String barcodeNum, String condition)
            throws IllegalArgumentException, SQLException,
                MovieNotFoundException
    {
        Condition.valueOf(condition.toUpperCase());
        String query = setConditionGenerateSQL(barcodeNum, condition);
        int rowsChanged = updateDatabase(query);
        if (rowsChanged > 1)
        {
            throw new SQLException("SQLException: database corrupted");
        }
        if (rowsChanged < 1)
        {
            throw new MovieNotFoundException("MovieDoesNotExistException: "
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
    private static void setCondition(RentalMovie rentalMovie, String condition)
            throws IllegalArgumentException, SQLException,
                MovieNotFoundException
    {
        String barcodeNum = rentalMovie.getBarcodeNum();
        setCondition(barcodeNum, condition);
    }



    /**
     * This method sends an update query to the Database
     * @param query an SQL query
     * @return the number of rows affected.
     * @throws SQLException
     */
    private static int updateDatabase(String query)
            throws SQLException
    {
        java.sql.Connection connection = utility.DataSource.getConnection();
        try
        {
            java.sql.Statement statement = connection.createStatement();
            return statement.executeUpdate(query);
        }
        finally
        {
            connection.close();
        }

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
    private static String setConditionGenerateSQL(String barcodeNum, String condition)
    {
        String[] splitBarcode = {null, null};
        splitBarcode(barcodeNum, splitBarcode);
        final int SKU_INDEX = 0;
        final int RENTAL_ID_INDEX = 1;

        String where = "(RentalID = '" + splitBarcode[RENTAL_ID_INDEX]
                + "' AND SKU = '" + splitBarcode[SKU_INDEX] + "')";
        String query = generateUpdateSQL("VideoRental", "Condition", condition, where);
        
        return query;
    }



    /**
     * This method splits a barcode into its SKU and rentalID and returns them
     * by writing them into a passed array of length 2.
     * @param barcodeNum
     * @param overwriteThisArray
     * @throws IllegalArguementException if the barcode is not the right length
     * or the array is not length 2
     * TODO: move this method to the appropriate class, probably RentalMovie
     */
    private static void splitBarcode(String barcodeNum, String[] overwriteThisArray)
            throws IllegalArgumentException
    {
        int SKU_length = 11; // TODO: make this a static constant in GenrealMovie
        int rentalID_length = 4; // TODO: make this a static constant in RentalMovie

        if (barcodeNum == null || barcodeNum.length() != SKU_length + rentalID_length)
        {
            throw new IllegalArgumentException("IllegalArgumentException: Invalid barcode number");
        }

        if (overwriteThisArray == null || overwriteThisArray.length != 2)
            // the number of results from splitting the barcode number is 2
        {
            throw new IllegalArgumentException("IllegalArgumentException: Array to overwrite is the wrong size.");
        }

        String SKU = barcodeNum.substring(0, SKU_length);
        String rentalID = barcodeNum.substring(SKU_length);

        overwriteThisArray[0] = SKU;
        overwriteThisArray[1] = rentalID;

    }



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
     * @throws CustomerDoesNotExistException if the customer does not exist
     * @throws MovieNotAvailableException if the movie is not available
     */
    public static GregorianCalendar rent(String barcodeNum, String memberID)
            throws MovieNotFoundException, CustomerDoesNotExistException,
            MovieNotAvailableException, SQLException
    {
        return rent(new RentalMovie(barcodeNum), new Customer(memberID));
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
     * @throws CustomerDoesNotExistException if the customer does not exist
     * @throws MovieNotAvailableException if the movie is not available
     * @post the movie's status is changed to rented.
     */
    private static GregorianCalendar rent(RentalMovie movie, Customer customer)
            throws MovieNotFoundException, CustomerDoesNotExistException,
            MovieNotAvailableException, SQLException
    {
        if (movie == null || !movie.getStatus().equalsIgnoreCase("available"))
        {
            throw new MovieNotAvailableException("MovieNotAvailableException:"
                    + " movie is not available");
        }

        if (customer == null)
        {
            throw new CustomerDoesNotExistException("CustomerDoesNotExistException:"
                    + " no Customer specified");
        }

        rentMovieUpdateDatabase(movie, "VideoRental", "Status",
                "rented");
        
        rentMovieUpdateDatabase(movie, "VideoRental", "MemberID", "" + customer.getMemberID());

        int rentalTime = movie.getRentalTime();

        GregorianCalendar dueDate = new GregorianCalendar();
        dueDate.add(Calendar.DATE, rentalTime);

        movie.setStatus("rented");

        return dueDate;
        
        
    }

    
    
    
    /**
     * Updates the database that the movie is rented out
     * @param movie
     * @throws MovieNotFoundException if the movie is not found in the db
     */
    private static void rentMovieUpdateDatabase(RentalMovie movie, String tableName,
            String attributeName, String setAttributeTo)
            throws MovieNotFoundException, SQLException
    {
        String barcodeNum = movie.getBarcodeNum();
        String movieWhere = generateMovieWhere(barcodeNum);
        
        String movieCommand = generateUpdateSQL(tableName, attributeName,
                setAttributeTo, movieWhere);

        int rowsChanged = updateDatabase(movieCommand);
        
        if (rowsChanged == 0)
        {
            throw new MovieNotFoundException("MovieDoesNotExistException:"
                    + " Could not find movie in database.");
        }
    }
    
    
    
    /**
     * Refactored code.  Generates a WHERE statement to specify the specific
     * RentalMovie that has the barcodeNum
     * @param barcodeNum
     * @return
     */
    private static String generateMovieWhere(String barcodeNum)
    {
        
        String[] splitBarcode = {null, null};
        splitBarcode(barcodeNum, splitBarcode);
        final int SKU_INDEX = 0;
        final int RENTAL_ID_INDEX = 1;
        String where = "RentalID = '" + splitBarcode[RENTAL_ID_INDEX] + "'"
                + "AND SKU = '" + splitBarcode[SKU_INDEX] + "'";
        return where;
    }
    
    
    
    
    /**
     * Gets the category of the movie
     * @param barcodeNum the barcodeNumber of the RentalMovie
     * @return the category of the movie
     * @throws SQLException
     * @throws MovieNotFoundException
     */
    public static String getCategory(String barcodeNum)
            throws SQLException, MovieNotFoundException
    {
        RentalMovie movie = new RentalMovie(barcodeNum);
        return movie.getCategory();
    }
    
    
    
    /**
     * Gets the format of the specified movie
     * @param barcodeNum
     * @return the format of the movie
     * @throws SQLException
     * @throws MovieNotFoundException
     */
    public static String getFormat(String barcodeNum)
            throws SQLException, MovieNotFoundException
    {
        RentalMovie movie = new RentalMovie(barcodeNum);
        return movie.getFormat();
    }
    

    /**
     * all possible categories
     */
    public enum Category
    {
        NEW_RELEASE, SEVEN_DAY_RENTAL, FOR_SALE, DISCOUNT
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
    public static void changeCategory(String barcodeNum, String category)
            throws SQLException, MovieNotFoundException
    {
        Category cat = Category.valueOf((category.replaceAll(" ", "_")).toUpperCase());
        if (cat == Category.FOR_SALE)
        {
            changeToSale(barcodeNum);
            return;
        }

        String where = generateMovieWhere(barcodeNum);
        String command = generateUpdateSQL("VideoRental", "Category",
                category.toLowerCase(), where);
        updateDatabase(command);
        // TODO: check for number of rows changed
    }



    /**
     * Changes a rental movie to a sale movie and updates the database
     * @param barcodeNum
     */
    private static void changeToSale(String barcodeNum)
    {
        // create the RentalMovie object

        // TODO: disallow if movie is not available or condition not good?

        // generate queries for deleting the rental movie

        // generate queries for inserting a sale movie

        // update the database
    }



    /**
     * This method gets the specified movie's title
     * @param barcodeNum
     * @return
     * @throws SQLException
     * @throws MovieNotFoundException
     */
    public static String getTitle(String barcodeNum)
            throws SQLException, MovieNotFoundException
    {
        RentalMovie movie = new RentalMovie(barcodeNum);
        return movie.getTitle();
    }



    /**
     * This method updates all the attributes of the RentalMovie associated with
     * the barcodeNum
     * @param barcodeNum
     * @param info
     * @throws SQLException
     * @throws MovieNotFoundException
     */
    public static void editCopy(String barcodeNum, String[] info)
            throws SQLException, MovieNotFoundException
    {
        // check to make sure info is the proper length



    }






    
    
    

}
