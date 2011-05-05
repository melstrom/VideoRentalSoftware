package inventory;

import java.sql.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Calendar;
import jdbcconnection.JDBCConnection;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Apr 13
 *  -added queries for addRequestMovie(), removeRequestMovie(), createGeneralMovie()
 *  -createGeneralMovie() takes 13 attributes
 *  -added internal comments
 *  -converted Date and Calendar to GregorianCalendar
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
 *      DONE: private bool checkDuplicateBarcode (String SKU)
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
 *      addCopy #need to add types for category, format, and SKU#
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
    private MovieRequest request = null;
    private static Connection connection;
    private static Statement statement;

    /**
     * Default constructor
     */
    public MovieManagement() throws SQLException, ClassNotFoundException
    {
            this.setupConnection();
    }

    /**
     * Constructor takes a parameter
     * @param movie A movie to be managed
     */
    public MovieManagement(GeneralMovie movie) throws SQLException, ClassNotFoundException
    {
        this.setupConnection();
        this.movie = movie;
    }

    /**
     * Adds a GeneralMovie to the database by inserting into the videoInfo
     * and physicalVideo tables.  It first searches to see if the information
     * contained in the GeneralMovie object
     * @param movie the movie to add
     * @pre movie is not null
     * @pre the SKU for the GeneralMovie object does not already exist
     */
    public static void createGeneralMovie(GeneralMovie movie) throws Exception
    {
        if (movie == null)
        {
            throw new IllegalArgumentException("Null movie passed");
        }
        inventory.MovieManagement.checkDuplicateBarcode(movie.getSKU());
        Integer infoID = findMatchingVideoInfo(movie);
        if (infoID == null)
        {
            infoID = nextInfoID();
            addVideoInfo(infoID, movie);
        }
        addPhysicalVideo(infoID, movie);
    }

    /**
     * This method creates new GeneralMovies in the Database
     * given a .csv file name
     * The file passed must be a comma delimited file with every entry on a
     * new line.  It must have the following information, in the specific order:
     * SKU, title, releaseDate, studio, genre, director, producer, rating, priceInDollars, length, format, actor1, actor2, ..., actor_n
     * Any deviation from the order will corrupt the database.
     * Any deviation from the number of parameters will result in either
     * database corruption or an ArrayOutOfBoundsException.
     * There must be at least one actor, but many may be added so long as they
     * are separated by commas.
     * Do not put any commas in the synopsis field.
     * @param filename the filename. it must be a .csv file
     * @throws IOException
     * @throws Exception
     */
    public static void createGeneralMovie(String filename)
            throws IOException, Exception
    {
        final int NON_ACTOR_FIELDS = 12;
        // There are 12 non actor fields:
        // SKU, title, releaseDate, studio, genre, director, 
        // producer, rating, priceInDollars, length, format
        // the rest of the fields are actors
        final int SKU = 0;
        final int TITLE = 1;
        final int RELEASE_DATE = 2;
        final int STUDIO = 3;
        final int GENRE = 4;
        final int DIRECTOR = 5;
        final int PRODUCER = 6;
        final int SYNOPSIS = 7;
        final int RATING = 8;
        final int PRICE_IN_DOLLARS = 9;
        final int LENGTH = 10;
        final int FORMAT = 11;
        BufferedReader fin = new BufferedReader(new FileReader(filename));
        String currentLine = "";
        while ((currentLine = fin.readLine()) != null)
        {
            String[] fields = currentLine.split(",");
            int numFields = fields.length;
            int numActors = numFields - NON_ACTOR_FIELDS;
            
            String sku = fields[SKU];
            String title = fields[TITLE];
            int year = Integer.parseInt(fields[RELEASE_DATE]);
            GregorianCalendar releaseDate = new GregorianCalendar();
            releaseDate.set(Calendar.YEAR, year);
            String studio = fields[STUDIO];
            String genre = fields[GENRE];
            String director = fields[DIRECTOR];
            String producer = fields[PRODUCER];
            String rating = fields[RATING];
            double priceInDollars = Double.parseDouble(fields[PRICE_IN_DOLLARS]);
            int priceInCents = (int) (priceInDollars * 100);
            int length = Integer.parseInt(fields[LENGTH]);
            String format = fields[FORMAT];
            String synopsis = fields[SYNOPSIS];
            
            String[] actors = new String[numActors];
            int fieldIndex = NON_ACTOR_FIELDS;
            int actorIndex = 0;
            while ( fieldIndex < numFields)
            {
                actors[actorIndex] = fields[fieldIndex].trim();
                actorIndex++;
                fieldIndex++;
            }

            createGeneralMovie(new GeneralMovie(sku, title, actors, director, producer, releaseDate,
                    synopsis, genre, rating, studio, priceInCents, format, length));
        }
    }

    /**
     * Creates a new copy of a movie in the database
     * @param generalMovie
     * @param type The type of movie to default to (sale or rental)
     * @return The generated SKU that uniquely identifies the copy (printed out and labelled on the copy)
     * @throws SQLException
     */
    public String addCopy(GeneralMovie generalMovie, String type) throws SQLException
    {
        String barcode = null;
        try
        {
            if (type.equals("sale"))
            {
                barcode = this.addSaleMovie(generalMovie, type);
            }
            else
                 if(type.equals("rental"))
            {
                barcode = this.addRentalMovie(generalMovie, type);
            }
            return barcode;
        }
        finally
        {
            connection.close();
        }
    }

    /**
     * Changes the information of a movie
     * @param info contains the 13 required information to identify a movie
     */
    public void editInfo(String SKU,
            String title,
            String[] actors,
            String director,
            String producer,
            GregorianCalendar releaseDate,
            String synopsis,
            String genre,
	    String rating,
            String studio,
            int retailPrice,
            String format,
            int runtime) throws SQLException, MissingFieldException
    {
        try
        {
            if (connection.isClosed())
            {
                connection = JDBCConnection.getConnection();
            }
        }
        catch (ClassNotFoundException e)
        {
            throw new SQLException(e.getMessage());
        }
        try
        {
            this.movie = new GeneralMovie(SKU,title,actors,director,producer,releaseDate, synopsis,
                    genre,rating,studio,retailPrice,format, runtime);
            String actorsList="";
            actorsList+= actors[0];
            for(int i=1; i<actors.length; i++)
            {
                actorsList = actorsList + "," + actors[i];
            }
            String dateString = ""+releaseDate.get(Calendar.YEAR);
            dateString += "-";
            dateString += releaseDate.get(Calendar.MONTH);
            dateString += "-";
            dateString += releaseDate.get(Calendar.DATE);
            String tables = "videoInfo, physicalVideo";
            String set = "videoInfo.title = ?," +
                    "videoInfo.actors = ?," +
                    "videoInfo.director = ?," +
                    "videoInfo.producer = ?," +
                    "videoInfo.releaseDate = ?," +
                    "videoInfo.description = ?," +
                    "videoInfo.genre = ?," +
                    "videoInfo.rating = ?," +
                    "videoInfo.studio = ?," +
                    "physicalVideo.RetailPrice = ?," +
                    "physicalVideo.Format = ?," +
                    "videoInfo.length = ?";
            String constraint =  "physicalVideo.infoID = videoInfo.infoID AND physicalVideo.SKU = ?";
            String query = JDBCConnection.makeUpdate(tables, set, constraint);
            int numParams = 13;
            String[] params = {
                title,
                actorsList,
                director,
                producer,
                dateString,
                synopsis,
                genre,
                rating,
                studio,
                ""+retailPrice,
                format,
                ""+runtime,
                SKU
            };

            java.sql.PreparedStatement preparedStatement = connection.prepareStatement(query);
            for (int i = 1; i <= numParams; i++)
            {
                preparedStatement.setString(i, params[i-1]);
            }

            preparedStatement.executeUpdate();

            /*
             * Need to update all the fields that get passed in

            String table = "physicalVideo";
            String column = "infoID";
            String constraint = "WHERE SKU = " + SKU;
            String query2 = generateQuery(table, column, constraint);
            ResultSet rs = statement.executeQuery(query2);
            rs.next();
            int infoID = rs.getInt("infoID");
>>>>>>> 0b6117953b75bf56208856ed98716d3774eb2686
            String columns[]={"InfoID","Description","Genre","Producer","Title","Actors", "studio", "Rating"};
            String paramaterizedValues[] = new String[columns.length];
            for (int i = 0; i < columns.length; i++)
            {
                paramaterizedValues[i] = "?";
            }
            String values[]={SKU,synopsis, genre, producer, title, actorsList, studio, rating};
<<<<<<< HEAD
            String query = generateUpdateSQL("videoInfo",columns, paramaterizedValues );
            java.sql.PreparedStatement preparedStatement = connection.prepareStatement(query);
            for (int i = 1; i <= values.length; i++)
            {
                preparedStatement.setString(i, values[i-1]);
            }
            preparedStatement.executeUpdate();

             *
             */

            //statement.executeUpdate(query);
        }
        finally
        {
            connection.close();
        }
    }

    /**
     *  Get an arrayList of MovieRequests
     *  @return an arrayList of MovieRequests
     */
    public ArrayList<MovieRequest> getRequest() throws SQLException
    {
        ArrayList<MovieRequest> movieRequest = new ArrayList<MovieRequest>();
        String table = "madeSpecialOrders";
        String column = "*";
        String constraint = "";

        String query = generateQuery(table, column, constraint);
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next())
        {
            String SKU = resultSet.getString("SKU");
            int customerID = resultSet.getInt("CustomerID");
            Date datetime = resultSet.getDate("datetime");
            GregorianCalendar requestDate = new GregorianCalendar();
            requestDate.setTime(datetime);
            this.request = new MovieRequest(SKU, customerID, requestDate);
            movieRequest.add(this.request);
        }
        if (movieRequest.isEmpty())
        {
            throw new SQLException("There are no requests.");
        }
        return movieRequest;
    }

    /**
     * Create query for special order
     * @param copy
     * @param account
     * @throws SQLException
     */
    public void addRequest (int customerID, String SKU) throws SQLException, RequestAlreadyExistsException
    {
        String tablename = "madeSpecialOrders";
        String columns[] = {"datetime", "SKU", "customerID"};

        String query = "SELECT SKU, customerID FROM " + tablename + " WHERE customerID = " + customerID + " AND SKU = '" + SKU + "'";
        ResultSet rs = statement.executeQuery(query);
        if (rs.next())
        {
            throw new RequestAlreadyExistsException ("The same request has already been made");
        }
        Calendar today = Calendar.getInstance();
        today.setTime(today.getTime());
        String time = makeReleaseDateString((GregorianCalendar)today);
        String values[] = {time,SKU,""+customerID};
        String SQL = generateInsertSQL(tablename, columns, values);
        statement.executeUpdate(SQL);
    }

    /**
     * Manually removes a request from the list of requests
     * @param request
     */
    public void removeRequest(MovieRequest request) throws SQLException
    {
        this.request = request;
        int customerID = request.getCustomerID();
        String SKU = request.getSKU();

        String table = "madeSpecialOrders";
        String SQL = "DELETE FROM " + table + " WHERE SKU= " + SKU + " and customerID= " + customerID;
        statement.executeUpdate(SQL);
    }

    public void removeRequest(String SKU)
            throws SQLException
    {
        String command = "DELETE FROM madeSpecialOrders WHERE SKU = " + SKU + ";";
        statement.executeUpdate(command);
    }

    public void clearAllRequests()
            throws SQLException
    {
        String command = "DELETE FROM madeSpecialOrders;";
        statement.executeUpdate(command);
    }

    /**
     * This method looks at the attributes of a GeneralMovie and tries to
     * find matches in the attributes of the videoInfo table.  If it finds an
     * exact match, it returns the infoID of the videoInfo.  Otherwise, it
     * returns null
     * Assumes that there is only one such primary key in database
     * @param movie the movie whose information we are trying to match
     * @return the infoID that matches the information you provided, or null if
     * no such infoID exists
     */
    private static Integer findMatchingVideoInfo(GeneralMovie movie)
            throws Exception
    {
        String table = "videoInfo";
        String column = "InfoID";
        //String constraint = makeConstraint(movie);
        String constraint = makePreparedConstraint(movie);
        String query = JDBCConnection.makeQuery(table, column, constraint);
        JDBCConnection conn = new JDBCConnection();
        try
        {
            String title = movie.getTitle();
            String director = movie.getDirector();
            String producer = movie.getProducer();
            String studio = movie.getStudio();
            String synopsis = movie.getSynopsis();
            String rating = movie.getRating();
            String genre = movie.getGenre();

            int numParameters = 7;
            String[] parameters =
            {
                title, director, producer, studio, synopsis, rating, genre
            };

            ResultSet result = conn.getResults(query, numParameters, parameters);
            if (result.next())
            {
                int infoID = result.getInt(column);
                return infoID;
            }
            else
            {
                return null;
            }
        }
        finally
        {
            conn.closeConnection();
        }
    }

    /**
     * Generic method to create a constraint for a movie
     * @param movie
     * @return A prepared statement
     */
    private static String makePreparedConstraint(GeneralMovie movie)
    {
        java.util.GregorianCalendar releaseDate = movie.getReleaseDate();
        int runtime = movie.getLength();
        String[] actors = movie.getActors();
        String constraint = "";

        constraint += "Title = ?";
        constraint += " AND ";
        constraint += makeActorConstraint(actors);
        constraint += " AND ";
        constraint += "director = ?";
        constraint += " AND ";
        constraint += "Producer = ?";
        constraint += " AND ";
        constraint += "studio = ?";
        constraint += " AND ";
        constraint += "Description = ?";
        constraint += " AND ";
        constraint += "Rating = ?";
        constraint += " AND ";
        constraint += "releaseDate = '" + makeReleaseDateString(releaseDate) + "'";
        constraint += " AND ";
        constraint += "Genre = ?";
        constraint += " AND ";
        constraint += "length = '" + runtime + "'";
        return constraint;
    }

    /**
     * Creates a string of the form
     * (actors LIKE '%Tom Cruise%' AND actors LIKE '%Kelly McGillis')
     * This is intended to identify a movie with identical actors, regardless
     * of the order in which they are stored in the database.
     * This method assumes that the actors are being stored in the database
     * as a single string, delimited by commas
     * @param actors an array of the actors you want to include in the query
     * @return a string that can be suffixed to WHERE to form a WHERE clause
     */
    private static String makeActorConstraint(String[] actors)
    {
        int numActors = actors.length;
        int actorIndex = 0; // start from the first actor
        String actorConstraint = "(";
        actorConstraint = actorConstraint
                + "actors LIKE '%" + actors[actorIndex].replaceAll("'", "") + "%' ";
        actorIndex++;
        while (actorIndex < numActors)
        {
            actorConstraint += "AND ";
            actorConstraint = actorConstraint
                    + "actors LIKE '%" + actors[actorIndex].replaceAll("'", "") + "%' ";
            actorIndex++;
        }
        actorConstraint += ")";
        return actorConstraint;
    }

    /**
     * Retrieves a string of form YYYY-MM-DD from the Calendar object
     * assumes that this form is the form of SQL datetime
     * @param releaseDate
     * @return A string containing the formatted date
     */
    private static String makeReleaseDateString(GregorianCalendar releaseDate)
    {
        String year = "" + releaseDate.get(GregorianCalendar.YEAR);
        String month = "-" + releaseDate.get(GregorianCalendar.MONTH);
        String day = "-" + releaseDate.get(GregorianCalendar.DATE);
        String datetime = year + month + day;
        return datetime;
    }

    /**
     * This method adds information to the videoInfo table in the database
     * @param infoID the new key for the videoInfo
     * @param movie the movie to add
     * @throws Exception
     * TODO: Use PreparedStatement to prevent escaping '
     */
    private static void addVideoInfo(int infoID, GeneralMovie movie)
            throws Exception
    {
        if (movie == null)
        {
            throw new IllegalArgumentException("No movie provided");
        }
        String title = movie.getTitle();
        String[] actors = movie.getActors();
        String director = movie.getDirector();
        String producer = movie.getProducer();
        String studio = movie.getStudio();
        String synopsis = movie.getSynopsis();
        String rating = movie.getRating();
        java.util.GregorianCalendar releaseDate = movie.getReleaseDate();
        String genre = movie.getGenre();
        String length = "" + movie.getLength();

        if (actors == null || actors.length < 1)
        {
            throw new IllegalArgumentException("No actors provided");
        }
        String actorsString = actors[0];
        for (int i = 1; i < actors.length; i++)
        {
            actorsString += ", ";
            actorsString += actors[i];
        }
        actorsString = actorsString.replaceAll("'", "");

        String tableName = "videoInfo";
        String[][] videoInfo =
        {
            {
                "InfoID", "Title", "Actors", "director", "Producer", "studio",
                "Description", "Rating", "releaseDate", "Genre", "length"
            },
            /*
            {
            "" + infoID, title, actorsString, director, producer,
            studio, synopsis, rating, makeReleaseDateString(releaseDate),
            genre, length
            }*/
            {
                "" + infoID, null, actorsString, null, null,
                null, null, null, makeReleaseDateString(releaseDate),
                null, length
            }
        };

        String query = JDBCConnection.makeInsert(tableName, videoInfo);
        JDBCConnection conn = new JDBCConnection();
        try
        {
            int numParameters = 7;
            String[] parameters =
            {
                title, director, producer, studio, synopsis,
                rating, genre
            };

            int linesChanged = conn.update(query, numParameters, parameters);
            if (linesChanged > 1)
            {
                // throw new SQLException("" + linesChanged + "rows were changed");
                // undo the update
            } else if (linesChanged == 0)
            {
                throw new java.sql.SQLException("Updates were not completed");
            }
        }
        finally
        {
            conn.closeConnection();
        }
    }

    /**
     * This method adds a physical video to the database tables
     * @param infoID the id for a physical video
     * @param movie A GeneralMovie object
     * @throws Exception
     */
    private static void addPhysicalVideo(int infoID, GeneralMovie movie)
            throws Exception
    {
        String SKU = movie.getSKU();
        String format = movie.getFormat();
        String retailPriceInCents = "" + movie.getRetailPriceInCents();

        String tableName = "physicalVideo";
        String[][] information =
        {
            {
                "SKU", "Format", "InfoID", "RetailPrice"
            },
            {
                SKU, format, "" + infoID, retailPriceInCents
            }
        };

        String insert = JDBCConnection.makeInsert(tableName, information);
        JDBCConnection conn = new JDBCConnection();
        try
        {
            int linesChanged = conn.update(insert);
            if (linesChanged > 1)
            {
                // throw new SQLException("" + linesChanged + "rows were changed");
                // undo the update
            }
            else if (linesChanged == 0)
            {
                throw new java.sql.SQLException("Updates were not completed");
            }
        }
        finally
        {
            conn.closeConnection();
        }
    }

    /**
     * Check if the movie information already exist in the database
     * @param SKU
     */
    private void checkDuplicateSKU(String SKU) throws SQLException, MovieExistsException
    {

        //SELECT SKU FROM videoInfo WHERE SKU = 'newSKU#'
        String table = "physicalVideo";
        String column = "SKU";
        String constraint = "WHERE SKU = '" + SKU + "'";

        String query = generateQuery(table, column, constraint);
        //String query = "SELECT " + column + " FROM " + table + " WHERE " + column + "='" + SKU + "'";

        boolean found = !statement.execute(query);

        if (found == true)
        {
            throw new MovieExistsException("This movie already exists (in the database)");
        }
    }

    /**
     * Checks if the movie copy already exist in the database
     * Despite its name, this method checks the SKU, not the barcode
     * @param SKU
     */
    private static void checkDuplicateBarcode(String SKU) throws Exception
    {
        if (SKU == null || SKU.trim().length() < GeneralMovie.MIN_SKU_LENGTH || SKU.trim().length() > GeneralMovie.MAX_SKU_LENGTH)
        {
            throw new IllegalArgumentException("Not a valid SKU");
        }

        for (int i = 0; i < SKU.trim().length(); i++)
        {
            if (!Character.isDigit(SKU.trim().charAt(i)))
            {
                throw new IllegalArgumentException("Not a valid SKU");
            }
        }

        String table = "physicalVideo";
        String column = "SKU";
        String constraint = "SKU = '" + SKU.trim().replaceAll("'", "") + "'";

        //String query = generateQuery(table, column, SKU);
        String query = JDBCConnection.makeQuery(table, column, constraint);
        //String query = "SELECT " + column + " FROM " + table + " WHERE " + column + "='" + SKU + "'";
        //boolean found = statement.execute(query);
        JDBCConnection conn = new JDBCConnection();
        try
        {
            ResultSet results = conn.getResults(query);
//            if (found)
            if (results.next())
            {
                throw new MovieExistsException("This copy already exists (in the database)");
            }
        } finally
        {
            conn.closeConnection();
        }
    }
    
    /**
     * Generate generic insert queries
     * @param tableName
     * @param columnNames
     * @param values
     * @return a query
     */
    private String generateInsertSQL(String tableName, String[] columnNames, String[] values)
    {
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
     * Generates a query using a predefined table, column name, and constraint
     * @param table The table being queried
     * @param column The column the query will return
     * @param constraint The constraint of the query
     * @return a query
     */
    private static String generateQuery(String table, String column, String constraint)
    {
        String query = "SELECT " + column + " FROM " + table + " " + constraint;
        return query;
    }

    /**
     * Tests whether inputs for movie info are NULLs
     * @param info any attribute
     */
    private void checkNULLinfo(String[] info) throws MissingFieldException
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
     * Add a line to videoSale table
     * @param generalMovie
     * @return a SKU for this saleMovie
     * @throws SQLException
     */
    private String addSaleMovie(GeneralMovie generalMovie, String type) throws SQLException
    {
        this.movie = generalMovie;
        String SKU = this.movie.getSKU();
        String table = "videoSale";
        int newSaleID = this.generateNewID(type, table);

        //Prepare INSERT SQL
        String condition = "available";
        String category = "for sale";
        String query = "INSERT INTO " + table + " (videoSale.SaleID, videoSale.condition, videoSale.catagory, videoSale.SKU) " + "VALUES (" + newSaleID + ",'" + condition + "','" + category + "','" + SKU + "')";
        statement.executeUpdate(query);

        String newBarCode = SKU + newSaleID;
        return newBarCode;
    }

    /**
     * Add a line to videoSale table
     * @param generalMovie
     * @return a SKU for this rentalMovie
     * @throws SQLException
     */
    private String addRentalMovie(GeneralMovie generalMovie, String type) throws SQLException
    {
        this.movie = generalMovie;
        String SKU = this.movie.getSKU();
        String table = "videoRental";
        int newRentalID = this.generateNewID(type, table);

        //Prepare INSERT SQL
        String condition = "available";
        String category = "7 day";
        String query = "INSERT INTO " + table + " (videoRental.RentalID, videoRental.condition, videoRental.catagory, videoRental.SKU) " + "VALUES (" + newRentalID + ",'" + condition + "','" + category + "','" + SKU + "')";
        statement.executeUpdate(query);


        String newBarCode = SKU + newRentalID;
        return newBarCode;
    }

    private String generateUpdateSQL(String tableName, String[] columnNames, String[] values, int constraint)
    {
        String query = "UPDATE "+tableName+" SET ";
        for (int i = 1; i < columnNames.length; i++)
        {
            query += columnNames[i] + " = '";
            query += values[i] + "' ";
            if (i != columnNames.length - 1)
            {
                query += ", ";
            }
        }
        query += "WHERE " +columnNames[0]+ " = " + constraint;
        return query;
    }

    /**
     * Sets up the database connection for the class
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void setupConnection() throws SQLException, ClassNotFoundException
    {
        connection = JDBCConnection.getJDBCConnection();
        statement = connection.createStatement();
    }
}