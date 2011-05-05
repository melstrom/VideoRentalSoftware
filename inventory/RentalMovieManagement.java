package inventory;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import search.Search;
import account.Customer;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import jdbcconnection.JDBCConnection;

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
 *
 * 16 April: 
 * need to test all setters and getters
 */
public class RentalMovieManagement
{
    /**
     * Default constructor
     * initialize JDBC connection, default formats, conditions, categories, and currentTime,
     */
    public RentalMovieManagement() throws SQLException, ClassNotFoundException
    {
        JDBC = new JDBCConnection();
        connection = JDBC.getConnection();
        String[] formats = {"Blu-ray", "DVD", "VHS"};
        this.formats = new String[3];
        this.conditions = new String[6];
        this.categories = new String[3];
        String[] conditions = {"lost", "broken", "reserved", "available", "rented", "overdue"};
        String[] categories = {"new release", "7 day", "for sale"};
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
    public void makeReservation(Customer customer) throws SQLException, Exception
    {
        if (movie.getCondition().equals("available"))
        {
            Calendar currentTime = Calendar.getInstance();
            today.setTime(currentTime.getTime());

            today.set(today.get(today.YEAR), today.get(today.MONTH), today.get(today.DATE));

            Reservation reservation = new Reservation(customer.getAccountID(), today);
            reservationQuery("" + customer.getAccountID());
        }
        else
        {
            throw new Exception("movie not available");
        }
    }

    /**
     * Remove a reservation from top of the list
     * @param customer the customer that has made the reservation
     * @throws SQLException
     * @throws Exception
     * @pre there is at least one reservation that is recorded for this customer
     */
    public void removeReservation(Customer customer) throws SQLException, Exception {
        if (movie.getCondition().equals("reserved"))
        {
            removeReservationQuery(customer.getAccountID());
        }
        else
        {
            throw new Exception("movie was not reserved");
        }
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
    public void setCurrentCopy(String barcode) throws MovieNotFoundException, SQLException, ClassNotFoundException, java.lang.Exception
    {
        Search search = new Search();
        movie = search.previewIndividualMovie(barcode);
        if (movie != null)
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



    /**
     * This method sets a new condition for the specified movie.
     * The condition attribute of the passed IndividualMovie will be changed
     * accordingly.
     *
     * @param newCondition the new condition for the movie
     * @param movie the movie that you want to change
     * @throws SQLException if a connection to the database cannot be made
     * @throws ClassNotFoundException if the JDBC driver is not installed
     * @throws IllegalArgumentException if the condition is not found in the
     * database
     * @throws MovieNotFoundException if the movie cannot be found in the database
     */
    public static void setCondition(String newCondition, IndividualMovie movie)
            throws SQLException, ClassNotFoundException, MovieNotFoundException
    {
        // TODO: checkc condition against database once the condition table is live
        String tableName = "videoSale";
        String set = tableName+".condition = ?";
        String constraint = tableName+".saleID = ? AND "+tableName+".SKU = ?";
        String saleID = movie.getCopyNum();
        String SKU = movie.getSKU();
        int numParam = 3;
        String[] params = {newCondition, saleID, SKU};

        String updateQuery = JDBCConnection.makeUpdate(tableName, set, constraint);

        // TODO: get rid of JDBCConnection object and try/finally block once
        // we start passing in JDBCConnection objects
        JDBCConnection connection = new JDBCConnection();
        try
        {
            int numLinesChanged = connection.update(updateQuery, numParam, params);
            if (numLinesChanged < 1)
            {
                throw new MovieNotFoundException("Movie does not exist in database");
            }
            // assert (numLinesChanged == 1);
        }
        finally
        {
            connection.closeConnection();
        }
        movie.setCondition(newCondition);
    }

    /**
     * This method sets a new condition for the specified movie.
     * The condition attribute of the passed RentalMovie will be changed
     * accordingly.
     *
     * @param newCondition the new condition for the movie
     * @param movie the movie that you want to change
     * @throws SQLException if a connection to the database cannot be made
     * @throws ClassNotFoundException if the JDBC driver is not installed
     * @throws IllegalArgumentException if the condition is not found in the
     * database
     * @throws MovieNotFoundException if the movie cannot be found in the database
     */
    public static void setCondition(String newCondition, RentalMovie movie)
            throws SQLException, ClassNotFoundException, MovieNotFoundException
    {
        // TODO: checkc condition against database once the condition table is live
        String tableName = "videoRental";
        String set = tableName+".condition = ?";
        String constraint = tableName+".rentalID = ? AND "+tableName+".SKU = ?";
        String rentalID = movie.getCopyNum();
        String SKU = movie.getSKU();
        int numParam = 3;
        String[] params = {newCondition, rentalID, SKU};

        String updateQuery = JDBCConnection.makeUpdate(tableName, set, constraint);

        // TODO: get rid of JDBCConnection object and try/finally block once
        // we start passing in JDBCConnection objects
        JDBCConnection connection = new JDBCConnection();
        try
        {
            int numLinesChanged = connection.update(updateQuery, numParam, params);
            if (numLinesChanged < 1)
            {
                throw new MovieNotFoundException("Movie does not exist in database");
            }
            // assert (numLinesChanged == 1);
        }
        finally
        {
            connection.closeConnection();
        }
        movie.setCondition(newCondition);
    }

    /**
     * This method sets a new category for a given RentalMovie.
     * The category is checked to ensure that it exists in the database,
     * and then if it does the category in the videoRental table is updated,
     * and the movie passed's category is changed to the specified newCategory
     *
     * Unlike setCondition, setCategory has no counterpart for IndividualMovies
     * since the only possible category for an IndividualMovie object is "for sale"
     *
     * @param newCategory the new category of the movie
     * @param movie the rental movie
     * @throws SQLException if a database connection cannot be made
     * @throws ClassNotFoundException if the jdbc driver is not installed
     * @throws MovieNotFoundException if the movie does not exist in the database
     * @throws IllegalArgumentException if the newCategory does not exist
     */
    public static void setCategory(String newCategory, RentalMovie movie)
            throws SQLException, ClassNotFoundException, MovieNotFoundException,
            IllegalArgumentException
    {
        String categoryCheckQuery  = JDBCConnection.makeQuery("catagories", "catagory", null);

        String tableName = "videoRental";
        String set = tableName+".catagory = ?";
        String constraint = tableName+".rentalID = ? AND "+tableName+".SKU = ?";
        String rentalID = movie.getCopyNum();
        String SKU = movie.getSKU();
        int numParam = 3;
        String[] params = {newCategory, rentalID, SKU};

        String updateQuery = JDBCConnection.makeUpdate(tableName, set, constraint);

        // TODO: get rid of JDBCConnection object and try/finally block once
        // we start passing in JDBCConnection objects
        JDBCConnection connection = new JDBCConnection();
        try
        {
            ResultSet categoryCheckResults = connection.getResults(categoryCheckQuery);
            boolean categoryExists = false;
            while(categoryCheckResults.next())
            {
                if (categoryCheckResults.getString(1).equalsIgnoreCase(newCategory))
                {
                    categoryExists = true;
                }
            }
            if (!categoryExists)
            {
                throw new IllegalArgumentException("The category "+newCategory+" " +
                        "does not exist");
            }

            int numLinesChanged = connection.update(updateQuery, numParam, params);
            if (numLinesChanged < 1)
            {
                throw new MovieNotFoundException("Movie does not exist in database");
            }
            // assert (numLinesChanged == 1);
        }
        finally
        {
            connection.closeConnection();
        }
        movie.setCategory(newCategory);
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
            throws SQLException, MovieNotFoundException, ClassNotFoundException, java.io.IOException, java.lang.Exception
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
            IndividualMovie movie = Search.previewIndividualMovie(barcode);
            category = movie.getCategory();
        }
        if (category == null)
        {
            return -1;
        }

        int numParam = 1;
        String[] params = {category};
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
     * Gets the category of the movie
     * @param barcodeNum the barcodeNumber of the RentalMovie
     * @return the category of the movie
     * @throws SQLException
     * @throws MovieNotFoundException
     * @pre the current movie is selected/not null
     */
    public String getCategory() throws SQLException, MovieNotFoundException
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
    public static String getGeneralMovieCategory(String SKU)
            throws SQLException, ClassNotFoundException
    {
        if (SKU == null)
        {
            return null;
        }
        String query = JDBCConnection.makeQuery("videoRental",
                "videoRental.catagory",
                "videoRental.SKU = ? AND NOT videoRental.catagory = ?");
        int numParam = 2;
        String[] param = {SKU, "for sale"};
        JDBCConnection connection = new JDBCConnection();
        try
        {
            ResultSet result = connection.getResults(query, numParam, param);

            if (result.next())
            {
                return result.getString(1);
            } else
            {
                return null;
            }

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
            throws SQLException, MovieNotFoundException {
        return movie.getTitle();
    }

    //General rental
    //--------------------------------------------------------------------------------------------------------------------------------
    /**
     * This method checks out a RentalMovie to a particular customer.  It updates
     * the videoRental table in the database and calculates when the movie is due.
     * It does not check that the movie is available, or that the customer has
     * no holds.
     *
     * @param customerID the unique customerID of the the customer who is
     * renting the video
     * @param barcode the full barcode of the RentalMovie being rented
     * @param connection an open connection to the database
     * @return the due date of the movie
     * @throws SQLException
     * @throws ClassNotFoundException
     * @pre the customerID and barcode must exist in the database
     * @pre the connection must be open
     */
    public static GregorianCalendar checkOut(int customerID, String barcode, JDBCConnection connection)
            throws SQLException, ClassNotFoundException, MovieNotFoundException, IOException, java.lang.Exception {
        // Checking for valid parameters
        if (customerID > Math.pow(10, Customer.ID_LENGTH)
                || customerID < 0)
        {
            throw new IllegalArgumentException("Not a valid customerID");
        }
        if (barcode == null)
        {
            throw new IllegalArgumentException("Not a valid barcode");
        }
        if (connection == null || connection.isClosed())
        {
            throw new IllegalArgumentException("Requires an open connection");
        }

        String[] splitBarcode = {null, null};
        splitBarcode(barcode, splitBarcode);
        String SKU = splitBarcode[0];
        String rentalID = splitBarcode[1];

        // Checking if the video is available for rental
        String availabilityQuery = JDBCConnection.makeQuery("videoRental",
                "videoRental.condition",
                "videoRental.SKU = ? AND videoRental.rentalID = ?");
        int numParam = 2;
        String[] availabilityParam = {SKU, rentalID};
        ResultSet availabilityResults = connection.getResults(availabilityQuery,
                numParam,
                availabilityParam);
        if (availabilityResults.next())
        {
            if (!availabilityResults.getString("videoRental.condition").equalsIgnoreCase("available"))
            {
                throw new MovieNotAvailableException("The movie is not available for rental.");
            }
        }
        else
        {
            throw new MovieNotFoundException("That barcode cannot be matched.");
        }

        // changing the videoRental table to have the customerID of the
        // renting customer, and the condition of rented
        String tableName = "videoRental";
        String set = "videoRental.customerID = ?, videoRental.condition = ?, "
                + "videoRental.checkout_time = NOW()";
        String constraint = "videoRental.SKU = ? AND videoRental.rentalID = ?";

        String query = JDBCConnection.makeUpdate(tableName, set, constraint);

        numParam = 4;
        String[] params = {"" + customerID, "rented", SKU, rentalID};

        int linesChanged = connection.update(query, numParam, params);

        // assert(linesChagned == 1);
        String select = "catagories.rentalLength";
        String from = "videoRental, catagories";
        String where = "catagories.catagory = videoRental.catagory AND"
                + " videoRental.rentalID = ?";

        String rentalLengthQuery = JDBCConnection.makeQuery(from, select, where);
        numParam = 1;
        String[] param = {rentalID};
        ResultSet result = connection.getResults(rentalLengthQuery, numParam, param);
        int rentalPeriod = 0;
        if (result.next())
        {
            rentalPeriod = result.getInt("catagories.rentalLength");
        }
        else
        {
            // throw new Exception("there's no format in the table");
        }
        GregorianCalendar dueDate = new GregorianCalendar();
        dueDate.add(Calendar.DATE, rentalPeriod);
        dueDate.set(Calendar.HOUR_OF_DAY, 23);
        dueDate.set(Calendar.MINUTE, 59);

        return dueDate;
    }

    public ArrayList<RentalMovie> viewCheckedOutMovies (int customerID)
    {
        ArrayList<RentalMovie> array = new ArrayList();
        RentalMovie rental = new inventory.RentalMovie();
        return array;
    }
    /**
     * This method is used to check in a RentalMovie.  The RentalMovie is
     * identified by its barcode, and a newCondition is assigned to it.
     * If the condition is good, the condition shall be "available".  If the
     * movie has been damaged, the condition shall be "broken".
     *
     * @param barcode the unique barcode of the RentalMovie.
     * @param newCondition the condition of the RentalMovie.  It shall be
     * either available, or broken
     * @throws SQLException if a connection to the database cannot be made
     * @throws ClassNotFoundException if the driver cannot be found
     *
     * TODO: checks for the validity of input
     */
    public static void checkIn(String barcode, String newCondition)
            throws SQLException, ClassNotFoundException
    {
        checkInQuery(barcode, newCondition);
    }


    /**
     * This method removes a RentalMovie from rental circulation and makes it
     * a sale movie.  It does this by changing the category of
     * the videoRental table to "for sale" in
     * the database, and creating a new entry in the videoSale table.
     * Except for the change of category and possibly condition in the
     * videoRental table, the RentalMovie's old information is preserved so
     * that transactions that involved renting it in the past can still be
     * looked up.
     *
     * This method does not check the condition of the movie beforehand.
     * It assumes that if the movie is lost, or broken by a customer during
     * a rental period, that the movie can be sold to the customer in lieu of
     * paying exorbitant overdue fees.
     *
     * Because the old RentalMovie object no longer makes any sense once the
     * IndividualMovie is created, please set the reference to null.
     *
     * @param movie the RentalMovie to change to a sale movie.  Note that the
     * type of this object MUST be a RentalMovie.
     * @throws SQLException if a connection to the database cannot be made
     * @throws ClassNotFoundException if the driver is not installed
     * @throws IOException if the price cannot be found
     */
    public static IndividualMovie changeToSale(RentalMovie movie)
            throws SQLException, ClassNotFoundException, IOException
    {
        // Attributes of videoSale that need to be filled in:
        // saleID: int
        // condition: String
        // catagory: String
        // SKU: String
        String insertTableName = "videoSale";
        String saleID = getCopyNum(movie.getBarcode());
        String condition = "available"; //movie.getCondition();  // check if condition is valid?

        String category = "for sale";
        String SKU = movie.getSKU();

        String[][] information = {
            {insertTableName + ".saleID", insertTableName + ".condition", insertTableName + ".catagory", insertTableName + ".SKU"},
            {saleID, condition, category, SKU}};
        String insertQuery = JDBCConnection.makeInsert(insertTableName, information);

        // Attributes of videoRental that form a key:
        // rentalID
        // SKU
        String updateTableName = "videoRental";
        String constraint = updateTableName + ".SKU = ? AND " + updateTableName + ".rentalID = ?";
        String rentalID = saleID;
        //String updateQuery = JDBCConnection.makeUpdate(updateTableName, constraint);
        String set = updateTableName+".condition = ?, "+updateTableName+".catagory = ?";
        String updateQuery = JDBCConnection.makeUpdate(updateTableName, set, constraint);

        int numParams = 4;
        String[] params = {condition, category, SKU, rentalID};

        JDBCConnection connection = new JDBCConnection();
        try
        {
            connection.update(insertQuery);
            connection.update(updateQuery, numParams, params);
        }
        finally
        {
            connection.closeConnection();
        }
        GeneralMovie generalMovie =
                new GeneralMovie(movie.getSKU(),
                movie.getTitle(),
                movie.getActors(),
                movie.getDirector(),
                movie.getProducer(),
                movie.getReleaseDate(),
                movie.getSynopsis(),
                movie.getGenre(),
                movie.getRating(),
                movie.getStudio(),
                movie.getRetailPriceInCents(),
                movie.getFormat(),
                movie.getLength());
        //public IndividualMovie(String category, int price, String barcode, GeneralMovie movie, String condition)
        pos.PriceSchemeManagement priceScheme = new pos.PriceSchemeManagement();
        int price = priceScheme.getPrice(movie.getCategory(), movie.getFormat());
        return new IndividualMovie("for sale", price, SKU, generalMovie, condition);
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
    public static void splitBarcode(String barcode, String[] splitBarcode)
            throws IllegalArgumentException
    {
        if (barcode == null)
        {
            throw new IllegalArgumentException("IllegalArgumentException: Invalid barcode number");
        }
        int barcodeLength = barcode.length();
        final int SKU_INDEX = 0;
        final int COPY_NUM_INDEX = 1;
        if (barcodeLength >= GeneralMovie.INFO_ID_LENGTH
                && barcodeLength <= GeneralMovie.MAX_SKU_LENGTH) {
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
    }

    /**
     * This method finds the SKU of a movie from the barcode of a movie
     * @param barcode the barcode of the movie
     * @return the SKU of the movie
     */
    public static String getSKU(String barcode)
    {
        String[] splitBarcode = {null, null};
        splitBarcode(barcode, splitBarcode);
        return splitBarcode[0];
    }

    /**
     * This method finds the copy number (either rentalID or saleID) of a
     * movie based on its barcode
     * @param barcode the barcode of the movie
     * @return the copy number, or null if the passed barcode is really a SKU.
     */
    public static String getCopyNum(String barcode)
    {
        String[] splitBarcode = {null, null};
        splitBarcode(barcode, splitBarcode);
        return splitBarcode[1];
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
            String copyNum = barcode.substring(RENTAL_ID_LENGTH + 3);
            String SKU = barcode.substring(0, copyNumStartIndex);
            this.SKU = SKU;
            rentalID = copyNum;
        }
        else
        {
            throw new IllegalArgumentException("Not a valid barcode");
        }
    }

    /**
     * Set currentTime
     */
    private void getCurrentTime()
    {
        Calendar today = Calendar.getInstance();
        this.today = new GregorianCalendar();
        this.today.set(today.get(today.YEAR), today.get(today.MONTH), today.get(today.DATE));
    }

    /**
     * This method changes the status of all movies that have exceeded their
     * rental period from "rented" to "overdue".  It also changes the penalty
     * flag in the Customer account to 1 to indicated that they have penalties.
     * It does not calculate the amount of penalty that the customer owes.
     *
     * This method is only intended to be called upon system start up.
     *
     * @return the number of overdue movies set
     * @throws SQLException if a database connection cannot be made
     * @throws ClassNotFoundException if the driver is not installed
     */
    public static int setOverdueMovies()
            throws SQLException, ClassNotFoundException
    {
        String tableName = "videoRental, customer, catagories";
        String set = "customer.penalty = 1, videoRental.condition = 'overdue'";
        String constraint = "videoRental.condition = 'rented'";
        constraint += " AND ";
        constraint += "videoRental.customerID = customer.customerID";
        constraint += " AND ";
        constraint += "videoRental.condition = 'rented'";
        constraint += " AND ";
        constraint += "videoRental.catagory = catagories.catagory";
        constraint += " AND ";
        constraint += "((SUBTIME(videoRental.checkout_time, TIME(videoRental.checkout_time)) + INTERVAL (1 + catagories.rentalLength) DAY) <= NOW())";
        // This last line does the following:
        // Considers the movie checked out at 12am the day of
        // Adds the rentalPeriod to the checkout time, and adds an extra day
        // compares this calculated day to the current time
        // i.e. Someone rented a movie on Monday 8, at 1pm
        // The query checks to see if Tuesday 16 12am is before the current time
        String query = JDBCConnection.makeUpdate(tableName, set, constraint);
        JDBCConnection connection = new JDBCConnection();
        try
        {
            return connection.update(query);
        }
        finally
        {
            connection.closeConnection();
        }
    }

//SQL
//--------------------------------------------------------------------------------------------------------------------------------
    /**
     * This method executes an Update query, which changes the RentalMovie
     * corresponding to the passed barcode so that its customerID is NULL,
     * its checkout_time is NULL, and its condition is the passed condition,
     * which will correspond to either available or broken.
     * @param barcode the unique barcode corresponding to a RentalMovie
     * @param newCondition the condition of the RentalMovie upon its check in.
     * If the condition was good, the newCondition is available.  Otherwise,
     * the condition is broken
     * @throws SQLException if a connection to the database cannot be made
     * @throws ClassNotException if the driver cannot be loaded
     */
    private static void checkInQuery(String barcode, String newCondition) throws SQLException, ClassNotFoundException
    {
        JDBCConnection conn = new JDBCConnection();
        String command = "UPDATE videoRental SET videoRental.condition='" + newCondition
                + "', videoRental.customerID = NULL, "
                + "videoRental.checkout_time = NULL "
                + "WHERE videoRental.rentalID='" + barcode.substring(barcode.length() - 9) + "';";
        PreparedStatement stat = conn.prepareStatement(command);
        stat.execute();
    }

    /**
     * Create a query to make a reservation
     * @throws SQLException
     * @throws Exception
     * @pre currentMovie is set/not null
     * @post a line is added to Reservation table
     * @post a line is added to madeReservation table
     */
    private void reservationQuery(String accountID) throws SQLException, Exception
    {
        String tablename = "Reservation";
        String[] columnNames = {"reservationID", "datetime", "SKU"};
        String date = "" + today.get(today.YEAR) + "/" + today.get(today.MONTH) + "/" + today.get(today.DATE);
        String[] values = {accountID, date, SKU};
        String query = generateInsertSQL(tablename, columnNames, values);
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
    private void removeReservationQuery(int accountID) throws SQLException, Exception
    {
        String tablename = "Reservation";
        String where = "where reservationID =" + quote + accountID + quote + " and SKU = " + quote + SKU + quote;
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
    private String getRentalPeriodQuery() throws SQLException, Exception
    {
        String category = movie.getCategory();
        String tablename = "catagories", field = "rentalLength";
        String where = "where videoRental.catagory = catagories.catagory "
                + "and catagory =" + quote + category + quote;
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
    private void changeToRentalQuery() throws SQLException, MovieNotFoundException, Exception
    {
        if (movie.getCondition().equals("available"))
        {
            String table = "videoRental";
            String attribute = "videoRental.catagory";
            String attributeTo = "7 day";
            String where = " where rentalID=" + quote + rentalID + quote + " and SKU=" + quote + SKU + quote;
            String query = generateUpdateSQL(table, attribute, attributeTo, where);
            updateDatabase(query);

            /*String[]columnNames = {"RentalID","condition","catagory", "SKU"};
            String[] values = { rentalID, movie.getCondition(), movie.getCategory(),SKU };
            table = "videoRental";
            query = generateInsertSQL(table, columnNames,values);
            updateDatabase(query);*/
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
        for (int i = 0; i < categories.length; i++) {
            if (category.equals(categories[i])) {
                String table = "videoRental", attribute = "catagory", attributeTo = category;
                String where = " where rentalID=" + quote + rentalID + quote;
                String query = generateUpdateSQL(table, attribute, attributeTo, where);
                updateDatabase(query);
            }
        }
    }

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
            MovieNotAvailableException, java.io.IOException, java.lang.Exception
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
            MovieNotAvailableException, java.io.IOException, java.lang.Exception
    {
        int numTotalRentalCopies = getTotalRentalCopies(SKU);
        if (numTotalRentalCopies < 1)
        {
            throw new MovieNotAvailableException("There are no copies available"
                    + " for rental or reservation");
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
        String[] params = {SKU};
        JDBCConnection connection = new JDBCConnection();
        try {
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
                "(NOT videoRental.condition = ?) "
                + "AND (NOT videoRental.condition = ?) "
                + "AND (NOT videoRental.condition = ?) "
                + "AND (NOT videoRental.catagory = ?) "
                + "AND (videoRental.SKU = ?)");

        int numParam = 5;
        String[] params = {"broken", "lost", "overdue", "for sale", SKU};

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
                "videoRental.SKU = ? AND videoRental.condition = ? AND videoRental.catagory <> ?");
        if (!exists(SKU))
        {
            return -1;
        }
        JDBCConnection connection = new JDBCConnection();
        try
        {
            String rentalQuery = JDBCConnection.makeQuery("videoRental",
                null,
                "videoRental.SKU = ? AND videoRental.condition = ? AND videoRental.catagory <> ?");


            String[] parameters = {SKU, "available", "for sale"};
            int numParameters = 3;
            ResultSet firstSet = connection.getResults(rentalQuery, numParameters, parameters);
            if (!firstSet.next())
            {
                return -1;
            }

            ResultSet result = connection.getResults(query, numParameters, parameters);
//            if (!result.next())
//            {
//                return -1;
//            }
            if (result.next())
            {
                Integer numAvailableCopies = result.getInt(1);
                
                return numAvailableCopies;
            }
            else
            {
                return -1;
            }

        }
        finally
        {
            connection.closeConnection();
        }
    }



    /**
     * This method finds the number of available copies of a GeneralMovie,
     * given that GeneralMovie's SKU.
     * For checking multiple movies this is faster than getAvailableCopies
     * (SKU:String) because it does not open and close connections.
     * @param SKU the SKU of the GeneralMovie
     * @param connection an open JDBCConnection object
     * @return a non-negative number indicating the number of copies available
     * or a negative number if the store does not carry any copies.
     * @throws MovieNotFoundException if the movie passed is not known to
     * the video store
     *
     */
    public static int getAvailableCopies(String SKU, JDBCConnection connection)
            throws MovieNotFoundException, SQLException, ClassNotFoundException
    {
        String query =
                JDBCConnection.makeQuery("videoRental",
                "COUNT(*)",
                "videoRental.SKU = ? AND videoRental.condition = ? AND videoRental.catagory <> ?");
        if (!exists(SKU))
        {
            return -1;
        }

            // checks to see if
            String rentalQuery = JDBCConnection.makeQuery("videoRental",
                null,
                "videoRental.SKU = ? AND (videoRental.condition = ? OR videoRental.condition = ? OR videoRental.condition = ?) AND videoRental.catagory <> ?");
            String[] rentalParams = {SKU, "available", "rented", "reserved", "for sale"};
            int rentalNumParams = 5;

            String[] parameters = {SKU, "available", "for sale"};
            int numParameters = 3;
            ResultSet firstSet = connection.getResults(rentalQuery, rentalNumParams, rentalParams);
            if (!firstSet.next())
            {
                return -1;
            }

            ResultSet result = connection.getResults(query, numParameters, parameters);
//            if (!result.next())
//            {
//                return -1;
//            }
            if (result.next())
            {
                Integer numAvailableCopies = result.getInt(1);

                return numAvailableCopies;
            }
            else
            {
                return -1;
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
        String[] params = {SKU};
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



    /**
     * This method finds the number of available copies of a GeneralMovie.
     * It is much faster than getAvailableCopies(movie:GeneralMovie) because it
     * does not need to open and close connections.
     * @param movie the GeneralMovie
     * @param conn an open JDBCConnection object
     * @return a non-negative number indicating the number of copies available
     * or a negative number if the store does not carry any copies.
     * @throws MovieNotFoundException if the movie passed is not known to
     * the video store
     *
     */
    public static int getAvailableCopies(GeneralMovie movie, JDBCConnection conn)
            throws MovieNotFoundException, SQLException, ClassNotFoundException
    {
        return getAvailableCopies(movie.getSKU(), conn);
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
    private void setConditionQuery(String condition) throws SQLException, MovieNotFoundException, Exception
    {
        for (int i = 0; i < conditions.length; i++)
        {
            if (condition.equals(conditions[i]))
            {
                String table = "videoRental", attribute = "videoRental.condition", attributeTo = condition;
                String where = " where rentalID=" + quote + rentalID + quote;
                String query = generateUpdateSQL(table, attribute, attributeTo, where);
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
    private String generateDeleteSQL(String tableName, String where)
            throws SQLException
    {
        String query = "DELETE FROM " + tableName + " " + where;
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
            throws SQLException {
        if (columnNames.length != values.length)
        {
            throw new SQLException("SQLException: column/value mismatch");
        }
        String query = "INSERT INTO " + tableName + " (";
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
            query = query + "'" + values[i] + "'";
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
                + " SET " + attributeName + " = " + "'" + setAttributeTo + "'"
                + whereCondition;
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
        String query = "select " + field + " from " + tablename + " " + where;
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
    private String[] formats;
    private String[] conditions;
    private String[] categories;
    final private int rentalPeriod = 7;
    final char quote = '\'';
    final char comma = ',';
}