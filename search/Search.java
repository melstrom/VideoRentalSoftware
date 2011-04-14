package search;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import account.Customer;
import inventory.GeneralMovie;
import inventory.IndividualMovie;
import inventory.MovieNotFoundException;
import inventory.RentalMovie;
import jdbconnection.JDBCConnection;
import java.util.Date;
import java.util.Calendar;
import java.io.IOException;
import java.util.GregorianCalendar;
import account.Address;
import account.Employee;
import pos.*;

/**
 * The Search class is a utility class that searches the database for Movies,
 * or Members.  It can also retrieve specific movies or members given their
 * unique barcode numbers.
 *
 * Currently there are only four methods that can be called:
 * Searching for a GeneralMovie
 * Searching for a RentalMovie
 * Searching for a Customer
 * Browsing by genre
 *
 * Generating lists of Top 10 or most recent releases are to be implemented
 * at a later date, in Tier 3 functionality.
 * 
 * TODO: go through and make sure all the Class names and attributes and method
 * calls match.
 * TODO: make sure all SQL queries match the database tables
 *
 * Last updated:
 * 7 April
 *
 * @author Mitch
 * @version 0.2
 *
 * 7 April:
 * removed Sanitizer since PreparedStatement takes care of that already
 *
 *
 */
public class Search
{

    
    /**
     * This enumeration represents all the possible genres of movies that are
     * carried by the video store.
     * TODO: decide maybe this is standalone enum, or mabye it goes in Movie class
     */
    public enum Genre
    {
        ACTION, COMEDY, DRAMA, FAMILY, HORROR, ROMANCE, SUSPENSE, WESTERN,
        SCIENCE_FICTION, MUSICAL, SILENT, FOREIGN, INDEPENDENT,
        DOCUMENTARY, TV_SERIES, MISC
    }


    /**
     * 
     * searchCustomers takes the name of a member or his phone number and
     * searches the database for matches.  A maximum of one parameter may be
     * null.  A list of Customers that match are returned.
     * @param lastName the name of the customer.  Not case sensitive.
     * @param phoneNum the 9 digit phone number of the customer.
     * @return a list of Customers that match the search criteria, or
     * null if no matching accounts could be found
     * @throws SQLException if a connection with the database cannot be made
     * @throws ClassNotFoundException
     * TODO: is memberID a string or an integer? how long is it?
     */
    public static ArrayList<Customer>
            searchCustomers(String lastName, String phoneNum)
            throws SQLException, ClassNotFoundException
    {
        if (lastName == null && phoneNum == null)
        {
            return null;
        }
        ArrayList<Customer> matchingMembers
                    = new ArrayList<Customer>();
        Connection connection = JDBCConnection.getConnection();
        try
        {
            String query = generateCustomerQuery(lastName, phoneNum);
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet result = statement.executeQuery(query);
            //ResultSet result = statement.executeQuery("SELECT customer.customerID FROM customer, account WHERE account.lastName LIKE '%c%' AND account.accountID = customer.accountID");
            
            while (result.next())
            {
                String matchingAccountID = result.getString("customer.customerID");
                int matchingAccountID_int = Integer.parseInt(matchingAccountID);
                matchingMembers.add(getCustomer(matchingAccountID_int));
            }
            result.close();
        } // end try
        finally
        {
            connection.close();
        }
        if (matchingMembers.size() > 0)
        {
            return matchingMembers;
        }
        else
        {
            return null;
        }
    }
    
    
    
    /**
     * Helper function for searchCustomers.  Generates an SQL query.
     * @pre the case where lastName and phoneNum should be filtered out before
     * passing it to this.  However, there is a redundant check anyways.
     * @param lastName member's last name to search for
     * @param phoneNum member's phone number
     * @return a string containing the query or null if lastName and phoneNum are
     * both null
     */
    private static String generateCustomerQuery(String lastName, String phoneNum)
    {
        if (lastName == null && phoneNum == null)
        {
            return null;
        }

        String query = "SELECT customer.customerID FROM customer, account WHERE ";
        String nameQuery = "account.lastName LIKE '" + pad(lastName.replaceAll("'", ""))+"'";
        String phoneNumQuery = "account.phoneNum LIKE ";

        if (lastName != null && phoneNum == null)
        {
            query += nameQuery;
        }
        else if (phoneNum != null && lastName == null)
        {          
            phoneNumQuery += pad(phoneNum);
            query += phoneNumQuery;      
        }
        else
        {     
            phoneNumQuery += phoneNum;
            query += nameQuery + " AND " + phoneNumQuery;
        }
        query += " AND account.accountID = customer.accountID";
        return query;
    }



    /**
     * This method retrieves the Customer account with the provided member ID from
     * the database.  Depends on both the Customer class and the customer
     * table in the database.
     *
     *
     * @param memberID the unique identifying number of the member
     * @return the member's account, or null if the member ID did not match any
     * records
     * @throws SQLException if a connection with the database cannot be made
     * @throws ClassNotFoundException if the driver cannot be found
     *
     */
    public static Customer getCustomer(int memberID)
            throws SQLException, ClassNotFoundException
    {
        JDBCConnection conn = new JDBCConnection();
        //Connection connection = JDBCConnection.getConnection();
        try
        {
                String query = conn.makeQuery("address, account, customer", 
                        null, 
                        "address.addressID = account.addressID " +
                        "AND account.accountID = customer.accountID " +
                        "AND customer.customerID = ?");
                int numParam = 1;
                String[] params = { "" + memberID };
                ResultSet result = conn.getResults(query, numParam, params);
                if (!result.next())
                {
                    return null;
                }
                String barcode = "" + memberID;
                String driversLicense = result.getString("customer.driversLicense");
                String firstName = result.getString("account.firstName");
                String lastName = result.getString("account.lastName");
                String phoneNum = result.getString("account.phoneNum");

                // copy and pasted address constructor
//                public Address(
//                    int houseNumber,
//                    String streetName,
//                    String city,
//                    String province,
//                    String country,
//                    String postalCode)

                int houseNumber = result.getInt("address.houseNumber");
                String streetName = result.getString("address.streetName");
                String city = result.getString("address.city");
                String province = result.getString("address.province");
                String country = result.getString("address.country");
                String postalCode = result.getString("address.postalCode");
                int addressID = result.getInt("address.addressID");

                Address address = new Address(addressID, houseNumber, streetName,
                        city, province, country, postalCode);

                return new Customer(barcode, driversLicense, memberID,
                        firstName, lastName, address, phoneNum);
                // copy and pasted signature from the Customer class
                //public Customer (String barcode, String DL, int accountID,
                //String Fname, String Lname, Address address, String phoneNum)
                
        }// end try
        finally
        {
            //connection.close();
            conn.closeConnection();
        }
    }
    
    
    
    /**
     * This method retrieves the Employee account with the provided member ID from
     * the database.  Depends on both the Employee class and the employee
     * table in the database.
     *
     *
     * @param employeeID the unique identifying number of the member
     * @return the member's account, or null if the member ID did not match any
     * records
     * @throws SQLException if a connection with the database cannot be made
     * @throws ClassNotFoundException if the driver cannot be found
     *
     */
    public static Employee getEmployee(int employeeID)
            throws SQLException, ClassNotFoundException
    {
        JDBCConnection conn = new JDBCConnection();
        //Connection connection = JDBCConnection.getConnection();
        try
        {
                String query = conn.makeQuery("address, account, employee", 
                        null, 
                        "address.addressID = account.addressID " +
                        "AND account.accountID = employee.accountID " +
                        "AND employee.employeeID = ?");
                int numParam = 1;
                String[] params = { "" + employeeID };
                ResultSet result = conn.getResults(query, numParam, params);
                if (!result.next())
                {
                    return null;
                }
                String barcode = "" + employeeID;
                String position = result.getString("employee.position");
                String firstName = result.getString("account.firstName");
                String lastName = result.getString("account.lastName");
                String phoneNum = result.getString("account.phoneNum");

                // copy and pasted address constructor
//                public Address(
//                    int houseNumber,
//                    String streetName,
//                    String city,
//                    String province,
//                    String country,
//                    String postalCode)

                int houseNumber = result.getInt("address.houseNumber");
                String streetName = result.getString("address.streetName");
                String city = result.getString("address.city");
                String province = result.getString("address.province");
                String country = result.getString("address.country");
                String postalCode = result.getString("address.postalCode");
                int addressID = result.getInt("address.addressID");

                Address address = new Address(addressID, houseNumber, streetName,
                        city, province, country, postalCode);

                return new Employee(position, employeeID, firstName, lastName,
                        address, phoneNum);
                // copy and pasted signature from the Employee class
                //public Employee (String position, int accountID, String Fname, String Lname, Address address, String phoneNum)
                
        }// end try
        finally
        {
            //connection.close();
            conn.closeConnection();
        }
    }
    
    
    
    /**
		Method to get an ArrayList of all employees in the database.
		@throws ClassNotFoundException if JDBC driver is not in CLASSPATH
		@throws SQLException if a database access error occurs or this method is called on a closed connection or no results
	*/
	public static ArrayList<Employee> getAllEmployees()
                throws ClassNotFoundException, SQLException
	{
        ArrayList<Employee> allEmployees = new ArrayList<Employee>();
        String queryString = JDBCConnection.makeQuery("employee, account, address",
                null, 
                "employee.accountID=account.accountID AND account.accountID=address.addressID");
        JDBCConnection connection = new JDBCConnection();
        try
        {
            ResultSet resultSet = connection.getResults(queryString);
            while (resultSet.next())
            {
                    String position = resultSet.getString("position");
                    int accountID = resultSet.getInt("accountID");
                    String firstName = resultSet.getString("firstName");
                    String lastName = resultSet.getString("lastName");
                    int houseNumber = resultSet.getInt("houseNumber");
                    String streetName = resultSet.getString("streetName");
                    String city = resultSet.getString("city");
                    String province = resultSet.getString("province");
                    String postalCode = resultSet.getString("postalCode");
                    String country = resultSet.getString("country");
                    String phoneNum = resultSet.getString("phoneNum");
                    int addressID = resultSet.getInt("address.addressID");

                    Address address = new Address(addressID, houseNumber, streetName,
                        city, province, country, postalCode);

                    allEmployees.add(new Employee(position, accountID, firstName, lastName, address, phoneNum));
            }
            return allEmployees;
        }
        finally
        {
            connection.closeConnection();
        }
        
    }


    /**
     * This method searches the database for a GeneralMovie matching the passed
     * search criteria.  At least one of the fields must be filled in.
     * The main purpose of this search is to give the searcher an idea of what
     * movies are carried by the store and their availability, without
     * overwhelming him with information about the individual copies.
     *
     * This differs from searchRentals because the searchRentals will return
     * a list of each individual RentalMovies wheras this method only returns
     * the general movie information shared by all copies with the same SKU.
     *
     * 11 April: Mitch
     * - redid the sql
     *
     * 12 April: Mitch
     * - it works for GeneralMovie, sorting out some issues with IndividualMovie
     *
     * @param title The title of the movie.
     * @param actors A list of actors who are in the movie. Multiple actors should be separated by commas.
     * @param director The director of the movie
     * @return a list of movies matching the search criteria.  May return null
     * if no matches could be found.
     * @throws SQLException if a connection with the database cannot be made
     * @throws SanitizerException if the passed parameters contain SQL queries
     */
    public static ArrayList<GeneralMovie> searchMovies(
            String title,
            String actors,
            String director)
            throws SQLException, ClassNotFoundException, MovieNotFoundException, Exception
    {
        JDBCConnection connection = new JDBCConnection();
        try
        {
            String query = generateMovieQuery(title, actors, director);
            int numParameters = 0;
            ArrayList<String> parameterList = new ArrayList<String>();
            if (title != null && title.trim().length() > 0)
            {
                numParameters++;
                parameterList.add(pad(title.trim()));
            }
            if (actors != null)
            {
                String[] actorList = actors.split(",");

                for (String actor : actorList)
                {
                    if (actor != null && actor.trim().length() > 0)
                    {
                        parameterList.add(pad(actor.trim()));
                        numParameters++;
                    }
                }
            }
            if (director != null && director.trim().length() > 0)
            {
                numParameters++;
                parameterList.add(pad(director.trim()));
            }
            if (numParameters == 0)
                throw new IllegalArgumentException("Must provide at least one search term");
            String[] parameters = new String[numParameters];
            for (int i = 0; i < numParameters; i++)
            {
                parameters[i] = parameterList.get(i);
            }
            ResultSet result = connection.getResults(query, numParameters, parameters);
            ArrayList<GeneralMovie> searchResults = new ArrayList<GeneralMovie>();
            if (result.next())
            {
                String sku = result.getString("physicalVideo.SKU");
                if (result.wasNull())
                {
                    return null;
                }
                searchResults.add(previewMovie(sku));
            }
            else
            {
                return null;
            }
            while (result.next())
            {
                String sku = result.getString("physicalVideo.SKU");
                searchResults.add(previewMovie(sku));
            }
            
            return searchResults;
            
        }
        finally
        {
            connection.closeConnection();
        }
        /*ResultSet result
                = searchMoviesGetSQLResult(title, actor, director);
        ArrayList<GeneralMovie> searchResults = new ArrayList<GeneralMovie>();
        while (result.next())
        {
            searchResults.add(previewMovie(result.getString("physicalVideo.SKU")));
        }
        result.close();

        if (searchResults.isEmpty())
        {
            return null;
        }
        else
        {
            return searchResults;
        }*/
    }



    /**
     * Pads a string with wildcard % symbols so that we can search for
     * matches that only contain the term instead of equalling exactly
     * @param str
     * @return
     */
    private static String pad(String str)
    {
        return "%" + str + "%";
    }


    /**
     * This method queries the database for a GeneralMovie matching the passed
     * search criteria.  It returns the results of the query.
     *
     * @param title The title of the movie
     * @param actor An actor who was in the movie
     * @param director The director of the movie
     * @return the results of an SQL query for all movies or rental movies that
     * match the search criteria
     * @throws SQLException
     * TODO: verify assumption that GeneralMovie infoID will be a String
     * TODO: change to be able to search for multiple actors (low priority)
     */
    /*
    private static ResultSet searchMoviesGetSQLResult(
            String title,
            String actor,
            String director)
            throws SQLException, ClassNotFoundException
    {
        String query = generateMovieQuery(title, actor, director);
        if (query == null)
        {
            return null;
        }

        String[] searchTerms = {title, actor, director};

        if (searchTerms[1] != null)
        {
            searchTerms[1] = "%" + searchTerms[1] + "%";
            // padding the actor with wildcards before the search
        }

        Connection connection = JDBCConnection.getConnection();
        try
        {
            PreparedStatement statement = connection.prepareStatement(query);
            int numTerms = 3; // depends on number of parameters
            int parameterIndex = 1; // SQL starts numbering indecies from 1
            for (int i = 0; i < numTerms; i++)
            {
                if (searchTerms[i] != null)
                {
                    statement.setString(parameterIndex, searchTerms[i]);
                    parameterIndex++;
                }
            } // end for

            ResultSet result = statement.executeQuery();
            return result;
            
        } // end try
        finally
        {
            connection.close();
        }

    }*/



    /**
     * This is a helper method for searchMovies.  It generates an SQL query
     * of the form:
     * SELECT physicalVideo.SKU
     *   FROM physicalVideo, videoInfo
     *   WHERE videoInfo.title = ?
     *     AND videoInfo.actors LIKE ?
     *     AND videoInfo.director = ?
     *     AND physicalVideo.infoID = videoInfo.infoID
     * It is up to searchVideos to fill the ? placeholders in using
     * connectionObject.prepareStatement(int parameterIndex, String sql)
     * At least one of the parameters must be non-null.
     * Null parameters will not be included in the SQL query.
     * If all the parameters are null, the method returns a null string.
     * @param title the title of the movie
     * @param actor the name of one actor to appear in the movie
     * @param director the director of the movie
     * @return a string with an SQL query, or null if all the parameters were
     * null.
     */
    private static String generateMovieQuery(
            String title,
            String actor,
            String director)
    {
        String query = "SELECT physicalVideo.SKU " +
                "FROM videoInfo, physicalVideo " +
                "WHERE ";
        ArrayList<String> searchCriteria = new ArrayList<String>();

        // adding the query forms of the non-null parameters to the array list
        if (title != null)
        {
            searchCriteria.add("videoInfo.title LIKE ?");
        }
        
        
        
        if (actor != null)
        {
            String[] actors = actor.split(",");
            for (String singleActor : actors)
            {
                searchCriteria.add("videoInfo.actors LIKE ?");
            }
        }
        if (director != null)
        {
            searchCriteria.add("videoInfo.director LIKE ?");
        }

        int numSearchCriteria = searchCriteria.size();
        if (numSearchCriteria == 0)
        {
            return null;
        }
        // generating the WHERE clause
        // if there are n non-null search criteria, then there are n-1 ANDs
        String searchCriteriaStr = searchCriteria.get(0);
        for (int i = 1; i < numSearchCriteria; i++)
        {
            searchCriteriaStr += " AND ";
            searchCriteriaStr += searchCriteria.get(i);
        }

        searchCriteriaStr += " AND physicalVideo.InfoID = videoInfo.InfoID";

        // putting it all together
        query += searchCriteriaStr;

        return query;
    }



    /**
     * This method decides if the passed barcode is a single SKU, in which case
     * it returns a GeneralMovie object, or an IndividualMovie barcode.
     *
     * This method is used when the caller knows the exact barcode number.
     * If this is not the case, use the search method.
     *
     *
     * @param barcodeID the unique identifying number of the movie
     * @return the movie that corresponds to the barcodeID
     */
    public static GeneralMovie previewMovie(String barcodeID)
            throws MovieNotFoundException, SQLException,
            IllegalArgumentException, ClassNotFoundException, IOException
    {
        int barcodeLength = barcodeID.length();
        GeneralMovie movie;
        
        if (GeneralMovie.MIN_SKU_LENGTH <= barcodeLength 
                && barcodeLength <= GeneralMovie.MAX_SKU_LENGTH)
        {
            movie = previewGeneralMovie(barcodeID);
        }
        else if (barcodeLength > GeneralMovie.MAX_SKU_LENGTH 
                && barcodeLength <= (GeneralMovie.MAX_SKU_LENGTH + IndividualMovie.ID_LENGTH))
        {
            movie = previewIndividualMovie(barcodeID);
        }
        else
        {
            throw new IllegalArgumentException("IllegalArgumentException: "
                    + "not a valid barcode number");
        }
     
        
        return movie;
        
    }



    /**
     * BOOKMARK April 11
     * Returns an individual movie that matches the barcodeID
     * @param barcodeID
     * @return
     * @throws MovieNotFoundException
     * @throws SQLException
     * @throws IllegalArgumentException
     */
    public static IndividualMovie previewIndividualMovie(String barcodeID)
            throws MovieNotFoundException, SQLException, 
            IllegalArgumentException, ClassNotFoundException,
            java.io.IOException
    {
        String[] splitBarcode = { null, null };
        inventory.RentalMovieManagement.splitBarcode(barcodeID, splitBarcode);
        String SKU = splitBarcode[0];
        String copyNum = splitBarcode[1];
        if (copyNum == null)
        {
            throw new IllegalArgumentException("Not an individual movie");
        }

        GeneralMovie generalMovie = previewGeneralMovie(SKU);
        
        
        String rentalQuery = "SELECT * FROM videoRental, physicalVideo"
                + " WHERE videoRental.SKU = ? AND videoRental.rentalID = ?";
        String saleQuery = "SELECT * FROM videoSale, physicalVideo"
                + " WHERE videoSale.SKU = ? AND videoSale.saleID = ?";
        Connection conn = JDBCConnection.getConnection();
        try
        {
            PreparedStatement statement = conn.prepareStatement(saleQuery);
            int parameterIndex = 1;
            // sql starts numbering parameter indecies from 1
            statement.setString(parameterIndex, SKU);
            parameterIndex++;
            statement.setString(parameterIndex, copyNum);
            //ResultSet result = statement.executeQuery(rentalQuery);
            ResultSet result = statement.executeQuery();
            if (result.next())
            {
//                String isNull = result.getString(1);
//                if (!result.wasNull())
//                {
                    
                    String category = result.getString("videoSale.category");
                    String format = result.getString("physicalVideo.format");
                    String condition = result.getString("videoSale.condition");


                    // copy and pasted IndividualMovie constructor:
                    //   public IndividualMovie(String category, int price, String barcode, GeneralMovie movie, String condition

                    PriceSchemeManagement priceScheme = new PriceSchemeManagement();
                    int priceInCents = priceScheme.getPrice(category, format);
                    IndividualMovie individualMovie = new IndividualMovie(category, priceInCents, barcodeID, generalMovie, condition);
                    return individualMovie;
//                }
            }
            else
            {
                result.close();
                statement.close();
                statement = conn.prepareStatement(rentalQuery);
                parameterIndex = 1;
                // reset parameter index
                statement.setString(parameterIndex, SKU);
                parameterIndex++;
                statement.setString(parameterIndex, copyNum);
                result = statement.executeQuery();
                if (result.next())
                {
                    String category = result.getString("videoRental.catagory");
                    String format = result.getString("physicalVideo.format");
                    System.out.println(format); // TESTING
                    String condition = result.getString("videoRental.condition");
                    PriceSchemeManagement priceScheme = new PriceSchemeManagement();
                    int priceInCents = priceScheme.getPrice(category, format);
                    int rentalPeriod = PriceSchemeManagement.getRentalPeriod(category);

                    IndividualMovie individualMovie = new IndividualMovie(category, priceInCents, barcodeID, generalMovie, condition);
                    RentalMovie rentalMovie = new RentalMovie(rentalPeriod, individualMovie);
                    return rentalMovie;
                // copy and pasted signature for RentalMovie
                //public RentalMovie(int rentalPeriod, IndividualMovie movie)
                }
                else
                {
                    throw new MovieNotFoundException("MovieNotFoundException:"
                            + " could not find that barcode");
                }


            }
        }
        finally
        {
            conn.close();
        }

    }
    
    

    /**
     * Creates a GenrealMovie object based on the SKU
     * @param barcodeID
     * @return
     * @throws SQLException
     * @throws MovieNotFoundException
     * @throws ClassNotFoundException
     */
    private static GeneralMovie previewGeneralMovie(String barcodeID)
            throws SQLException, MovieNotFoundException, ClassNotFoundException
    {
        String query = "SELECT * FROM videoInfo, physicalVideo "
                + "WHERE videoInfo.InfoID = physicalVideo.InfoID "
                + "AND physicalVideo.SKU = '" + barcodeID + "'";
        
        Connection connection = JDBCConnection.getConnection();
        try
        {
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(query);

            if (!results.next())
            {
                throw new MovieNotFoundException("MovieNotFoundException:"
                        + "the specified SKU does not exist");
            }
            String title = results.getString("Title");
            String actors = results.getString("Actors");
            String director = results.getString("Director");
            String producer = results.getString("Producer");
            Date releaseDate = results.getDate("releaseDate");
            String synopsis = results.getString("Description");
            String studio = results.getString("studio");
            String rating = results.getString("Rating");
            String genre = results.getString("Genre");

            int retailPriceInCents = results.getInt("RetailPrice");
            String format = results.getString("Format");
            int length = results.getInt("length");

            GregorianCalendar releaseCalendar = new GregorianCalendar();
            releaseCalendar.setTime(releaseDate);

            if (results.next())
            {
                throw new MovieNotFoundException("MovieNotFoundException: "
                        + "Movies sharing the same SKU found.");
            }
            String[] actorList = actors.split(", ");

/*
            // the format of releaseDate is YYYYMMDD, or an 8 digit integer
            // To get its four most significant digits, we want to get rid of
            // the four least significant, so divide it by 10^4
            int year = releaseDate / 10000;
            // To get the month, divide by 10^2 to get rid of the first two
            // digits, then mod it by 10^2 to get the months
            int month = (releaseDate / 100) % 100;
            // mod by 10^2 to get the days
            int day = (releaseDate % 100);
            java.util.GregorianCalendar release =
                    new java.util.GregorianCalendar(year, month, day);
 *
 */
            // copy and pasted GeneralMovie signature
//            public GeneralMovie
//            (String SKU,
//            String title,
//            String[] actors,
//            String director,
//            String producer,
//            GregorianCalendar releaseDate,
//            String synopsis,
//            String genre,
//	    String rating,
//            String studio,
//            int retailPriceInCents,
//            String format,
//            int runtime)
            return new GeneralMovie(barcodeID, title, actorList, director, 
                    producer, releaseCalendar, synopsis, genre,
                    rating, studio, retailPriceInCents, format, length);
        } // end try

        finally
        {
            connection.close();
        }
        
    }
    



    /**
     * This method searches the database for a rental movie matching the search
     * criteria.  At least one of the fields must not be null.  This differs
     * from the searchVideo method by finding every single rental copy
     * available.  Therefore it will not search movies that are not carried by
     * the store.
     *
     * TODO: possibly make this IndividualMovie to cover saleSearch too
     *
     * @param title the title of the movie
     * @param actor an actor in the movie
     * @param director the director of the movie
     * @param memberID the member who is currently renting this
     * @return a list of videos that match the search criteria, or null if
     * no matching videos could be found.
     * @throws SQLException if a database connection cannot be made
     * @throws SanitizerException if any search terms contain SQL commands
     */
    public static ArrayList<RentalMovie> searchRentals(
            String title,
            String[] actors,
            String director,
            Integer memberID)
            throws SQLException
    {
        String query =
                searchRentalsGenerateQuery(title, actors, director, memberID);
        ResultSet result = searchRentalsGetSQLResult(query, title, actors,
                director, memberID);
        ArrayList<RentalMovie> resultList = searchRentalsResultsList(result);
        result.close();
        return resultList;
    }



    /** 
     * This is a helper method for searchRentals.  It generates an SQL query of
     * the form:
     * SELECT videoRental.SKU, videoRental.rentalID
     *   FROM physicalVideo, videoRental, videoInfo
     *   WHERE videoInfo.title = ? AND
     *     (videoInfo.actor LIKE ? 
     *       OR videoInfo.actor LIKE ? OR ... ?) AND
     *     videoInfo.director = ? AND
     *     cutomer.accountID = ? AND
     *      (movieInfo.infoID = physicalVideo.infoID
     *          AND physicalVideo.SKU = videoRental.SKU) AND
     *      customer.rentalID = videoRental.rentalID
     *
     * It does this by collecting each attribute query in an arrayList
     * then forming a string
     *
     * @param title
     * @param actors
     * @param director
     * @param memberID
     * @return an SQL query for all rental movies matching the passed search
     * criteria, or null if no movies could be found.
     *
     * TODO: need to fix the SQL, especially for customer.
     * It might make more sense to give every RentalVideo a memberID instead.
     */
    private static String searchRentalsGenerateQuery(
            String title,
            String[] actors,
            String director,
            Integer memberID)
    {
        String query = "SELECT videoRental.SKU, videoRental.rentalID WHERE ";

        ArrayList<String> searchCriteria = new ArrayList<String>();
        ArrayList<String> whereClause = new ArrayList<String>();

        String movieWhereClause = "(movieInfo.infoID = physicalVideo.infoID"
                    + " AND physicalVideo.SKU = videoRental.SKU)";
        boolean needsMovieWhereClause = false;
        
        if (title != null)
        {
            searchCriteria.add("movieInfo.title = ? ");
            needsMovieWhereClause = true;
        }

        if (actors != null)
        {
            String actorsQuery = "";
            /* actorsQuery should have the form
             * (movieInfo.actors LIKE ?
             *   {optional lines movieInfo.actors LIKE ? OR }
             *  )
             */
            int numActors = actors.length;
            if (numActors > 0)
            {
                actorsQuery += "(movieInfo.actors LIKE ? ";
                for (int i = 1; i < numActors; i++)
                {
                    actorsQuery += "OR movieInfo.actors LIKE ? ";
                }
                actorsQuery += ")";
            }
            searchCriteria.add(actorsQuery);
            needsMovieWhereClause = true;
        }
        
        if (director != null)
        {
            searchCriteria.add("movieInfo.director = ? ");
            needsMovieWhereClause = true;
        }


        if (memberID != null)
        {
            searchCriteria.add("customer.customerID = ? ");
            whereClause.add("customer.rentalID = videoRental.rentalID");
        }

        if (needsMovieWhereClause)
        {
            whereClause.add(movieWhereClause);
        }

        if (!searchCriteria.isEmpty())
        {
            query += searchCriteria.get(0);
            for (int i = 1; i < searchCriteria.size(); i++)
            {
                query += " AND ";
                query += searchCriteria.get(i);
            }
            for (String clause : whereClause)
            {
                query += " AND " + clause;
            }
            return query;

        }
        else
        {
            return null;
        }
    }



    /**
     * This is a helper function for searchRentals that takes a string containing
     * and SQL query, opens a connection to the database, queries the database,
     * then returns the results of the query.
     *
     * @param query
     * @param title
     * @param actors
     * @param director
     * @param memberID
     * @throws SQLException if the database cannot be connected to
     * @return
     */
    private static ResultSet searchRentalsGetSQLResult(String query,
            String title, String[] actors, String director, Integer memberID)
            throws SQLException, ClassNotFoundException
    {
        ArrayList<String> searchTerms = consolidateSearchTerms(title, actors,
                director, memberID);
        if (searchTerms == null)
        {
            return null;
        }

        Connection connection = JDBCConnection.getConnection();
        try
        {

            PreparedStatement statement = connection.prepareStatement(query);
            int numTerms = searchTerms.size();
            int parameterIndex = 1; // SQL starts numbering indecies from 1
            for (int i = 0; i < numTerms; i++)
            {
                if (searchTerms.get(i) != null)
                {
                    statement.setString(parameterIndex, searchTerms.get(i));
                    parameterIndex++;
                }
            } // end for

            ResultSet result = statement.executeQuery();
            return result;
        }
        finally
        {
            connection.close();
        }

    }



    /**
     * This method takes the parameters, filters out the null ones, then
     * consolidates them into an array list.
     *
     * @param title
     * @param actors
     * @param director
     * @param memberID
     * @return an array list of the non-null parameters, or null if they are all
     * null
     * @throws IllegalArgumnetException if the length of the memberID is not
     * correct
     */
    private static ArrayList<String> consolidateSearchTerms(String title,
            String[] actors, String director, Integer memberID)
    {
        ArrayList<String> searchTerms = new ArrayList<String>();
        if (title != null)
        {
            searchTerms.add(title);
        }
        if (actors != null)
        {
            for (String actor : actors)
            {
                searchTerms.add("%" + actor + "%");
            }
        }
        if (director != null)
        {
            searchTerms.add(director);
        }
        if (memberID != null)
        {

            if (memberID >= Math.pow(1, account.Account.ID_LENGTH)
                    || memberID < Math.pow(1,account.Account.ID_LENGTH))
            {
                throw new IllegalArgumentException("IllegalArgumentException:"
                        + " customer account ID must be "
                        +account.Account.ID_LENGTH+ " digits long");
            }

            searchTerms.add("" + memberID);
        }
        if (searchTerms.isEmpty())
        {
            return null;
        }
        else
        {
            return searchTerms;
        }
    }



    /**
     * This moves the Results into an ArrayList
     * @param result
     * @return a list of rental movies from the result set.  May return null
     * if the list is empty.
     * @throws SQLException
     */
    private static ArrayList<RentalMovie>
            searchRentalsResultsList(ResultSet result)
            throws SQLException
    {
        ArrayList<RentalMovie> rentalList = new ArrayList<RentalMovie>();
        while (result.next())
        {
            String barcode = "";
            barcode += result.getString("VideoRental.SKU");
            barcode += result.getString("VideoRental.rentalID");
            rentalList.add((RentalMovie) previewMovie(barcode));
        }
        if (rentalList.isEmpty())
        {
            return null;
        }
        else
        {
            return rentalList;
        }
    }



    /**
     * This method lets the caller see all GeneralMovies corresponding to the
     * specified genre.
     * @param genre the genre of movie that we should view
     * @return a list of all videos in that genre
     * @throws SQLException if a connection with the database cannot be made
     */
    public static ArrayList<GeneralMovie> browse(Genre genre)
            throws SQLException
    {
        String genreStr = generateGenreStr(genre);
        String query = generateBrowseQuery(genreStr);
        ResultSet result = browseGetSQLResult(query);
        ArrayList<GeneralMovie> browseResults = browseResultsList(result);
        result.close();
        return browseResults;
    }
    
    
    
    /**
     * This is a helper method for browse.  It changes the genre enumeration
     * into a string so that it can be used in an SQL query.
     * 
     * @param genre the genre enumeration to convert
     * @return a string representing the genre.
     */
    private static String generateGenreStr(Genre genre)
    {
        String genreStr;
        switch (genre)
        {
            case ACTION:
                genreStr = "action";
                break;
            case COMEDY:
                genreStr = "comedy";
                break;            
            case DRAMA:
                genreStr = "drama";
                break;            
            case FAMILY:
                genreStr = "family";
                break;            
            case HORROR:
                genreStr = "horror";
                break;            
            case ROMANCE:
                genreStr = "romance";
                break;            
            case SUSPENSE:
                genreStr = "suspense";
                break;            
            case WESTERN:
                genreStr = "western";
                break;            
            default:
                genreStr = null;
                // should never get here
        } // end switch
        return genreStr;
    }



    /**
     * This method is a helper method for browse.  It generates the
     * SQL query for the genre that is to be browsed.
     *
     * @param genreStr the genre to be browsed.
     * @return the SQL query asking for all GeneralMovies in the specified genre
     */
    private static String generateBrowseQuery(String genreStr)
    {
        String query = "SELECT physicalVideo.SKU " +
                "FROM physicalVideo, videoInfo" +
                "WHERE videoInfo.genre = " + genreStr +
                " AND videoInfo.infoID = physicalVideo.infoID";
        return query;
    }



    /**
     * This is a helper method for browse.  It gets the SQL results of the
     * passed query.  A connection to the database is created and closed in
     * this method.
     *
     * @param query the SQL query to send to the database
     * @return the results of the query
     * @throws SQLException
     */
    private static ResultSet browseGetSQLResult(String query)
            throws SQLException
    {
        Connection connection = JDBCConnection.getConnection();
        try
        {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            return result;
        }
        finally
        {
            connection.close();
        }
    }



    /**
     * This is a helper method for browse.  It takes the results of an SQL
     * query and converts them into a list of GeneralMovies.
     * TODO: verify assumption that GeneralMovie can be constructed using a SKU
     * @param result the results of an SQL query
     * @return a list of GeneralMovies that were in the results
     * @throws SQLException if a database access error occurs or this method
     * is called on a closed result set
     */
    private static ArrayList<GeneralMovie> browseResultsList(ResultSet result)
            throws SQLException, MovieNotFoundException, ClassNotFoundException,
            Exception
    {
        ArrayList<GeneralMovie> resultsList = new ArrayList<GeneralMovie>();
        while (result.next())
        {
            resultsList.add(
                    previewMovie(result.getString("physicalVideo.SKU")));
        }
        return resultsList;
    }



}

