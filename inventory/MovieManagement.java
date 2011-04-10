package inventory;

import java.sql.SQLException;
import java.util.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import account.Customer;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import jdbconnection.JDBCConnection;

/**
 * April 9 (Saturday)
 * -changes to match changes in other classes
 * -issues:
 *      Missing@L157: BarCodeChecker class
 *      Issue@L237: MovieRequest class attributes does not match with database, requiring a lot of workarounds
 *      Missing@L265: createQueue method. See comment
 *
 * April 5 (Tuesday)
 * -changed all Dates to GC (UI: Notice createMovie and editMovie now takes in 2 arguments)
 * -added GenerateQuery method to handle QueryGeneration
 * -question: how will the UI handle MovieRequest objects? What information can the UI use from this?
 * -question: should we add movie genre to the whole system
 *
 * April 4 (Monday)
 * -to-do
 *      understand and setup SQL connection
 *      read on java exceptions and implement them (may reimpl. checkDupl&NULL as exceptions)
 * -movieRequest does not perform any functions
 *      -FIXED: on top of that, there is no place for it in the db
 *
 * April 3 (Sunday)
 * -start implementation of most methods
 * -to-do
 *      DONE: private bool checkDuplicateSKU (String SKU)
 *      DONE: private bool checkDuplicateBarcode (String barcode)
 *      DONE: private bool checkNULLinfo (String[] info)
 * -bugs
 *      FIXED: line 90, wrong return type
 *
 * March 31 (Thursday)
 * -add/remove methods to reflect changes in class diagram
 *
 * March 29 (Tuesday)
 * -creation of MovieManagement class
 * -initial methods:
 *      createGeneralMovie
 *      addCopy #need to add types for category, format, and barcode#
 *      editInfo
 *      setPrice
 *      getQuantity
 *          -where is copies:ArrayList<IndividualMovie> being moved to?
 *      getReservations
 *          -should this method be in MovieManagement?
 *      requestMovie
 * -added JavaDoc
 * -too many unknowns; try later
 *
 * @author melstrom
 */
public class MovieManagement
{

    private GeneralMovie movie = null;
    private IndividualMovie copy = null;
    private MovieRequest request = null;
    private static Connection connection;
    private static Statement statement;

    /**
     *
     */
    public MovieManagement()
    {
        try
        {
            this.setupConnection();
        } catch
        {
            //some stuff here
        } finally
        {
            connection.close();
        }
    }

    /**
     *
     * @param movie
     */
    public MovieManagement(GeneralMovie movie)
    {
        this.setupConnection();
        this.movie = movie;
    }

    /**
     * Constructs a new movie as part of the movie catalog
     * @param info contains the 7 required information to identify a movie
     */
    public void createGeneralMovie(String[] info, GregorianCalendar releaseDate)
    {
        String SKU = info[0];
        try
        {
            this.checkNULLinfo(info);
            this.checkDuplicateSKU(SKU);
            String title = info[1];
            String actors = info[2];
            String director = info[3];
            //String releaseDate = info[4];     //use Calendar type at UI to eliminate conversion
            String synopsis = info[4];
            String genre = info[5];
            //splits the actors into an array of actors
            //String[] actorArray = actors.split(",");


            this.movie = new GeneralMovie(SKU, title, actors, director, releaseDate, synopsis); //pass in actors? or actorArray?

            //INSERT INTO VideoInfo VALUES (SKU, title, director, releaseDate, actors, synopsis)
        } //Catch here
        finally
        {
            connection.close();
        }
    }

//    final protected void addNewTitle() throws SQLException
//    {
//        String table = "Videoinfo";
//        String query = "insert into" + table
//                + "values (" + quote + "" + quote + comma//infoID
//                + quote + title + quote + comma
//                + quote + director + quote + comma
//                + quote + releaseDate.get(releaseDate.YEAR) + releaseDate.get(releaseDate.MONTH) + releaseDate.get(releaseDate.DATE) + quote + comma
//                + quote + actors + quote + comma
//                + quote + synopsis + quote + comma
//                + quote + SKU + quote
//                + ");";
//
//        executeQuery(query);
//    }
    /**
     * Constructs a new single copy of a movie
     * @param category the category the movie belongs to (need to find out more about this)
     * @param format the media format of the movie (VHS, DVD, Bluray)
     * @param barcode the unique identification of the individual movie copy
     */
    public void addCopy(GeneralMovie generalMovie, String category, String format, String barcode)
    {
        try
        {
            this.checkDuplicateBarcode(barcode);
            this.movie = generalMovie;
            String SKU = this.movie.getSKU();
            barcode = BarCodeChecker.assign(SKU);   //no barcodechecker yet

            //Why do we need to add a price here? What does price have to do with individual movies?
            int price = 0;

            //passed all tests, create the copy
            this.copy = new IndividualMovie(category, price, format, barcode, this.movie);

        } //Catch here
        finally
        {
            connection.close();
        }
    }

    /**
     * Changes the information of a movie
     * @param info contains the 7 required information to identify a movie
     */
    public void editInfo(String[] info, GregorianCalendar releaseDate)
    {
        try
        {
            this.checkNULLinfo(info);
            String SKU = info[0];
            String title = info[1];
            String actors = info[2];
            String director = info[3];
            //String releaseDate = info[4];
            String synopsis = info[4];
            String genre = info[5];

            //splits the actors into an array of actors
            //String[] actorArray = actors.split(",");

            //movie.setSKU(SKU);                //GM does not have setSKU:allowed to edit at all?
            movie.setTitle(title);
            movie.setActors(actors);
            movie.setDirector(director);
            movie.setReleaseDate(releaseDate);  //GregorianCalendar
            movie.setSynopsis(synopsis);
            //movie.setGenre(genre);              //missing setGenre in GM

            //UPDATE VideoInfo SET Title = movie.getTitle, Actors = movie.getActors...etc   WHERE SKU = SKU
        } //Catch here
        finally
        {
            connection.close();
        }
    }

//    /**
//     *
//     * @return quantity of...something
//     */
//    public int getQuantity()
//    {
//        String table = "videoInfo";
//        int quantity = movie.getSKU();
//        //SELECT COUNT(SKU) FROM videoInfo WHERE SKU='SKU'
//
//        return quantity;
//    }
    /**
     * 
     */
    public ArrayList<MovieRequest> getRequest() throws SQLException
    {
        ArrayList<MovieRequest> movieRequest = new ArrayList<MovieRequest>();
        String table = "madeSpecialOrders";
        String column = "*";
        String constraint = "*";

        String query = generateQuery(table, column, constraint);

        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.isLast() == false)
        {
            String SKU = resultSet.getString("SKU");

//            We need a format to construct a movieRequest.
//            First of all, why do we need the format if SKU can tell us everything?
//            Second, format does not exist in the table; if it doesn't exists, do we need it?
//            Same for release date
            //SELECT format FROM videoInfo WHERE SKU = SKU
//            String format = resultSet.getString("Format");
            int customerID = resultSet.getInt("CustomerID");
            Date datetime = resultSet.getDate("datetime");
            //Convert Date to GC;
            //Database and entity object mismatch
            this.request = new MovieRequest(SKU, format, releaseDate, customerID);
            movieRequest.add(this.request);
        }
        return movieRequest;
    }

    public void addRequest(IndividualMovie copy, Customer account)
    {
        //String title, String format, Date releaseDate, CustomerAccount requestAcc
        this.copy = copy;

        String title = copy.getTitle();
        String format = copy.getFormat();
        GregorianCalendar releaseDate = copy.getReleaseDate();
        int accountID = account.getAccountID();

        this.request = new MovieRequest(title, format, releaseDate, accountID);
        request.createQueue();
        //createQueue does nothing, needs to be implemented (considering moving it here to implement)
    }

    /**
     * Manually removes a request from the list of requests
     * @param request
     */
    public void removeRequest(MovieRequest request) throws SQLException
    {
        this.request = request;
        String table = "madeSpecialOrders";
        String SKU = request.getSKU();
        int CustomerID = request.getAccountID();

//        DELETE FROM madeSpecialOrders WHERE SKU=SKU and CustomerID=CustomerID
        //Incomplete/wrong query; stub

        String query = this.generateQuery(table, "", table);
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.deleteRow();
    }

    /**
     * Check if the movie information already exist in the database
     * @param SKU
     */
    private void checkDuplicateSKU(String SKU) throws SQLException
    {

        //SELECT SKU FROM videoInfo WHERE SKU = 'newSKU#'
        String table = "videoInfo";
        String column = "SKU";
        String constraint = SKU;

        String query = generateQuery(table, column, constraint);
        //String query = "SELECT " + column + " FROM " + table + " WHERE " + column + "='" + SKU + "'";

        boolean found = statement.execute(query);

        if (found == true)
        {
            throw new MovieExsistsException("This movie already exists (in the database)");
        }
    }

    /**
     * Checks if the movie copy already exist in the database
     * @param barcode
     */
    private void checkDuplicateBarcode(String barcode) throws SQLException
    {

        //Query:SELECT barcode FROM physicalVideo WHERE barcode = 'barcode'
        String table = "barcode";
        String column = "physicalVideo";
        String constraint = barcode;

        String query = generateQuery(table, column, barcode);
        //String query = "SELECT " + column + " FROM " + table + " WHERE " + column + "='" + barcode + "'";

        boolean found = statement.execute(query);

        if (found == true)
        {
            throw new CopyExistsException("This copy already exists (in the database)");
        }
    }

    /**
     * Generates a query using a predefined table, column name, and constraint
     * @param table The table being queried
     * @param column The column the query will return
     * @param constraint The constraint of the query
     * @return
     */
    private String generateQuery(String table, String column, String constraint)
    {
        String query = "SELECT " + column + " FROM " + table + " WHERE " + constraint;
        return query;
    }

    /**
     * Tests whether inputs for movie info are NULLs
     * @param info
     */
    private void checkNULLinfo(String[] info)
    {
        for (int i = 0; i < 6; i++)
        {
            String test = info[i];
            if (test == null)
            {
                throw new MissingFieldException("Information fields cannot be empty.");
            }
        }
    }

    /**
     * Sets up the database connection for the class
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void setupConnection() throws SQLException, ClassNotFoundException
    {
        this.connection = JDBCConnection.getJDBCConnection();
        this.statement = connection.createStatement();
    }
}
