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
import inventory.RentalMovieManagement;
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
 *
 * @author Mitch
 * @version 0.2
 *
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
    /*public enum Genre
    {
        ACTION, COMEDY, DRAMA, FAMILY, HORROR, ROMANCE, SUSPENSE, WESTERN,
        SCIENCE_FICTION, MUSICAL, SILENT, FOREIGN, INDEPENDENT,
        DOCUMENTARY, TV_SERIES, MISC
    }*/


    

    /**
     * This method finds a list of customers matching the search term, choosing
     * a different attribute to search by for every different searchType.
     * The possible searchTypes are ID, last name, or phone num
     * @param searchTerm the term to look for
     * @param searchType what attribute you want to look under
     * @return an arrayList of customers if any are found
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static ArrayList<Customer> searchCustomers(String searchTerm, String searchType)
            throws SQLException, ClassNotFoundException
    {
        
        if (searchType.equalsIgnoreCase("id"))
        {
            ArrayList<Customer> customerList = new ArrayList<Customer>();
            int customerID = Integer.parseInt(searchTerm);
            Customer customer = getCustomer(customerID);
            if (customer == null)
            {
                return null;
            }
            customerList.add(customer);
            return customerList;
        }
        else if (searchType.equalsIgnoreCase("phone num"))
        {
            return searchCustomers(null, null, searchTerm.replaceAll("-",""));
        }
        else if (searchType.equalsIgnoreCase("last name"))
        {
            return searchCustomers(null, searchTerm, null);
        }
        else
        {
            throw new IllegalArgumentException(searchType + " is not a valid search type");
        }
    }
    
    
    
    
    /**
     * 
     * searchCustomers takes the name of a member or his phone number and
     * searches the database for matches.  A maximum of one parameter may be
     * null.  A list of Customers that match are returned.
     * @pasram firstName not implemented yet
     * @param lastName the name of the customer.  Not case sensitive.
     * @param phoneNum the 9 digit phone number of the customer.
     * @return a list of Customers that match the search criteria, or
     * null if no matching accounts could be found
     * @throws SQLException if a connection with the database cannot be made
     * @throws ClassNotFoundException
     * TODO: implementation for firstName
     */
    private static ArrayList<Customer>
            searchCustomers(String firstName, String lastName, String phoneNum)
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
            
            /* old, working code for returning the customers
             while (result.next())
            {
                String matchingAccountID = result.getString("customer.customerID");
                int matchingAccountID_int = Integer.parseInt(matchingAccountID);
                matchingMembers.add(getCustomer(matchingAccountID_int));
            }
             * *
             */

            // New code, using getCustomer(ResultSet result)
            while (result.next())
            {
                Customer customer = getCustomer(result);
                matchingMembers.add(customer);
                customer = getCustomer(result);
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

        // old, working query
        //String query = "SELECT customer.customerID FROM customer, account WHERE ";
        
        // new query for getCustomer(result:ResultSet)
        String query = "SELECT * FROM customer, address, account WHERE ";

        String nameQuery;
        String phoneNumQuery = "account.phoneNum LIKE ";

        if (lastName != null && phoneNum == null)
        {
            nameQuery = "account.lastName LIKE '" + pad(lastName.replaceAll("'", ""))+"'";
            query += nameQuery;
        }
        else if (phoneNum != null && lastName == null)
        {          
            phoneNumQuery = phoneNumQuery + "'"+ pad(phoneNum)+"'";
            query += phoneNumQuery;      
        }
        else
        {
            nameQuery = "account.lastName LIKE '" + pad(lastName.replaceAll("'", ""))+"'";
            phoneNumQuery = phoneNumQuery + "'"+ pad(phoneNum)+"'";
            query += nameQuery + " AND " + phoneNumQuery;
        }
        query += " AND account.accountID = customer.accountID";
        query += " AND account.addressID = address.addressID";
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
                //String barcode = "" + memberID;
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
                //int addressID = result.getInt("address.addressID");

                Address address = new Address(houseNumber, streetName,
                        city, province, country, postalCode);
                
                return new Customer(driversLicense, memberID,
                        firstName, lastName, address, phoneNum);
                // copy and pasted signature from the Customer class
                //public Customer (String DL, int accountID, String Fname, String Lname, Address address, String phoneNum)
                
        }// end try
        finally
        {
            //connection.close();
            conn.closeConnection();
        }
    }



    /**
     * This method creates a Customer object from a result set.
     *
     * @pre the result must select all columns
     * @param the results of a query for customers
     * @return the member's account, or null if the result set does not have next
     * @throws SQLException if a connection with the database cannot be made
     * @throws ClassNotFoundException if the driver cannot be found
     *
     */
    public static Customer getCustomer(ResultSet result)
            throws SQLException, ClassNotFoundException
    {
            
            if (result.isAfterLast())
            {
                //System.out.println("Is afer last"); // TESTING
                return null;
            }
            
            int memberID = result.getInt("customer.customerID");
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
            //int addressID = result.getInt("address.addressID");

            Address address = new Address(houseNumber, streetName,
                    city, province, country, postalCode);
            return new Customer(driversLicense, memberID,
                    firstName, lastName, address, phoneNum);
            // copy and pasted signature from the Customer class
            //public Customer (String DL, int accountID, String Fname, String Lname, Address address, String phoneNum)


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
                //int addressID = result.getInt("address.addressID");

                Address address = new Address(houseNumber, streetName,
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
                    //int addressID = resultSet.getInt("address.addressID");

                    Address address = new Address(houseNumber, streetName,
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
     * This method will search for a movie by a specified attribute.
     * The attributes searchable are title, director, actors, and genre.
     * This method looks for a GeneralMovie and is intended to be used
     * by customers.
     *
     *
     * @param searchTerm the term that the user is searching for
     * @param searchType the kind of search the user is making.  It can be
     * either title, director, actors, or genre
     * @return a list of movies that match the search criteria, or null if
     * no movies are found, or null if the search type is not recognized.
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws MovieNotFoundException
     * @throws IOException
     */
    public static ArrayList<GeneralMovie> searchMovies(String searchTerm, String searchType)
            throws SQLException, ClassNotFoundException, MovieNotFoundException,
            IOException,java.lang.Exception
    {
        if (searchType.equalsIgnoreCase("title"))
        {
            return searchMovies(searchTerm, null, null);
        }
        else if (searchType.equalsIgnoreCase("actors"))
        {
            return searchMovies(null, searchTerm, null);
        }
        else if (searchType.equalsIgnoreCase("director"))
        {
            return searchMovies(null, null, searchTerm);
        }
        else if (searchType.equalsIgnoreCase("genre"))
        {
            return browse(searchTerm);
        }
        else
        {
            return null;
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
            throws SQLException, ClassNotFoundException, MovieNotFoundException,
            IOException, java.lang.Exception
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
            /*
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
             * */
            while (result.next())
            {
                GeneralMovie aMovie = previewGeneralMovie(result);
                searchResults.add(aMovie);
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
        String query = "SELECT * " +
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
            IllegalArgumentException, ClassNotFoundException, IOException, java.lang.Exception
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
            java.io.IOException, java.lang.Exception
    {
        String[] splitBarcode = { null, null };
        inventory.RentalMovieManagement.splitBarcode(barcodeID, splitBarcode);
        String SKU = splitBarcode[0];
        String copyNum = splitBarcode[1];
        if (copyNum == null)
        {
            throw new IllegalArgumentException("Not an individual movie");
        }
        //System.out.println(SKU+" "+copyNum);
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
                    
                    String category = result.getString("videoSale.catagory");
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
                    String condition = result.getString("videoRental.condition");
                    PriceSchemeManagement priceScheme = new PriceSchemeManagement();
                    int priceInCents = priceScheme.getPrice(category, format);
                    int rentalPeriod = RentalMovieManagement.getRentalPeriod(category);

                    IndividualMovie individualMovie = new IndividualMovie(category, priceInCents, barcodeID, generalMovie, condition);
                    RentalMovie rentalMovie = new RentalMovie(rentalPeriod, individualMovie);
                    //System.out.println(SKU+" "+copyNum);
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
            throws SQLException, MovieNotFoundException, ClassNotFoundException, java.lang.Exception
    {
        //System.out.println(barcodeID);
        String query = "SELECT * FROM videoInfo, physicalVideo "
                + "WHERE videoInfo.InfoID = physicalVideo.InfoID "
                + "AND physicalVideo.SKU = '" +barcodeID + "'";
        
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
     * Creates a GeneralMovie based on the result set.
     * @pre the result must contain the columns for
     * title
     * actors
     * director
     * producer
     * releaseDate
     * description
     * studio
     * rating
     * genre
     * infoID
     * SKU
     * from the tables videoInfo and physicalVideo
     * @param results the result of an sql query
     * @return
     * @throws SQLException if the database cannot connect
     * @throws NullPointerException if one of the columns is missing
     * @throws ClassNotFoundException if the driver is not found
     */
    private static GeneralMovie previewGeneralMovie(ResultSet results)
            throws SQLException, ClassNotFoundException
    {
        //System.out.println(barcodeID);

        if (results.isAfterLast())
        {
            return null;
        }

        String barcodeID = results.getString("physicalVideo.SKU");
        String title = results.getString("videoInfo.Title");
        String actors = results.getString("videoInfo.Actors");
        String director = results.getString("videoInfo.Director");
        String producer = results.getString("videoInfo.Producer");
        Date releaseDate = results.getDate("videoInfo.releaseDate");
        String synopsis = results.getString("videoInfo.Description");
        String studio = results.getString("videoInfo.studio");
        String rating = results.getString("videoInfo.Rating");
        String genre = results.getString("videoInfo.Genre");

        int retailPriceInCents = results.getInt("physicalVideo.RetailPrice");
        String format = results.getString("physicalVideo.Format");
        int length = results.getInt("videoInfo.length");

        GregorianCalendar releaseCalendar = new GregorianCalendar();
        releaseCalendar.setTime(releaseDate);


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
            throws SQLException, ClassNotFoundException, IOException,
            MovieNotFoundException,java.lang.Exception
    {
        String query =
                searchRentalsGenerateQuery(title, actors, director, memberID);
        Connection connection = JDBCConnection.getConnection();
        try
        {
            ResultSet result = searchRentalsGetSQLResult(query, title, actors,
                    director, memberID, connection);
            ArrayList<RentalMovie> resultList = searchRentalsResultsList(result);
            result.close();
            return resultList;
        }
        finally
        {
            connection.close();
        }
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
//        String query = "SELECT videoRental.SKU, videoRental.rentalID " +
//                "FROM videoRental, videoInfo, physicalVideo " +
//                "WHERE ";

        String selectQuery = "SELECT videoRental.SKU, videoRental.rentalID ";
        String fromQuery = "FROM videoRental";
        String whereQuery = "WHERE ";

        ArrayList<String> searchCriteria = new ArrayList<String>();
        ArrayList<String> whereClause = new ArrayList<String>();

        String movieFromClause = ", videoInfo, physicalVideo";
        String customerFromClause = ", customer";
        String movieWhereClause = "(videoInfo.infoID = physicalVideo.infoID"
                    + " AND physicalVideo.SKU = videoRental.SKU)";
        String actorsWhereClause = "(customer.customerID = videoRental.customerID";
        boolean needsMovieWhereClause = false;
        boolean needsActorsWhereClause = false;

        if (title != null)
        {
            searchCriteria.add("videoInfo.title LIKE ? ");
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
                actorsQuery += "(videoInfo.actors LIKE ? ";
                for (int i = 1; i < numActors; i++)
                {
                    actorsQuery += "OR videoInfo.actors LIKE ? ";
                }
                actorsQuery += ")";
            }
            searchCriteria.add(actorsQuery);
            needsMovieWhereClause = true;
        }
        
        if (director != null)
        {
            searchCriteria.add("videoInfo.director LIKE ? ");
            needsMovieWhereClause = true;
        }


        if (memberID != null)
        {
            searchCriteria.add("customer.customerID LIKE ? ");
            whereClause.add("videoRental.customerID = customer.customerID ");
            fromQuery += customerFromClause;
        }

        if (needsMovieWhereClause)
        {
            whereClause.add(movieWhereClause);
            fromQuery += movieFromClause;
        }

        String query = selectQuery;
        query += fromQuery;
        query = query + " " + whereQuery;

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
            String title, String[] actors, String director, Integer memberID
            , Connection connection)
            throws SQLException, ClassNotFoundException
    {
        ArrayList<String> searchTerms = consolidateSearchTerms(title, actors,
                director, memberID);
        if (searchTerms == null)
        {
            return null;
        }

        //Connection connection = JDBCConnection.getConnection();

            PreparedStatement statement = connection.prepareStatement(query);
            int numTerms = searchTerms.size();
            int parameterIndex = 1; // SQL starts numbering indecies from 1
            for (int i = 0; i < numTerms; i++)
            {
                if (searchTerms.get(i) != null)
                {
                    statement.setString(parameterIndex, pad(searchTerms.get(i)));
                    parameterIndex++;
                }
            } // end for

            ResultSet result = statement.executeQuery();
            return result;
        

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

            /*if (memberID >= Math.pow(1, account.Account.ID_LENGTH)
                    || memberID < Math.pow(1,account.Account.ID_LENGTH))
            {
                throw new IllegalArgumentException("IllegalArgumentException:"
                        + " customer account ID must be "
                        +account.Account.ID_LENGTH+ " digits long");
            }*/

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
            throws SQLException, MovieNotFoundException, ClassNotFoundException,
            IOException,java.lang.Exception
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
    
    public static ArrayList<GeneralMovie> browse(String genre)
            throws SQLException, ClassNotFoundException, MovieNotFoundException,java.lang.Exception
    {
        String query = JDBCConnection.makeQuery("videoInfo, physicalVideo", "physicalVideo.SKU", "videoInfo.genre = ? AND videoInfo.infoID = physicalVideo.infoID");
        JDBCConnection connection = new JDBCConnection();
        int numParam = 1;
        String[] param = { genre };
        try
        {
            ArrayList<GeneralMovie> movies = new ArrayList<GeneralMovie>();
            ResultSet result = connection.getResults(query, numParam, param);
            while(result.next())
            {
                String SKU = result.getString("physicalVideo.SKU");
                movies.add(previewGeneralMovie(SKU));
            }
            return movies;
            
        }
        finally
        {
            connection.closeConnection();
        }
    }
    
    
    
    /**
     * This is a helper method for browse.  It changes the genre enumeration
     * into a string so that it can be used in an SQL query.
     * 
     * @param genre the genre enumeration to convert
     * @return a string representing the genre.
     * @deprecated
     */
    /*
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
     *
     */



    /**
     * This method is a helper method for browse.  It generates the
     * SQL query for the genre that is to be browsed.
     *
     * @param genreStr the genre to be browsed.
     * @return the SQL query asking for all GeneralMovies in the specified genre
     * @deprecated
     */
    /*
    private static String generateBrowseQuery(String genreStr)
    {
        String query = "SELECT physicalVideo.SKU " +
                "FROM physicalVideo, videoInfo" +
                "WHERE videoInfo.genre = " + genreStr +
                " AND videoInfo.infoID = physicalVideo.infoID";
        return query;
    }
     *
     */



    /**
     * This is a helper method for browse.  It gets the SQL results of the
     * passed query.  A connection to the database is created and closed in
     * this method.
     *
     * @param query the SQL query to send to the database
     * @return the results of the query
     * @throws SQLException
     * @deprecated
     */
    /*
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
     *
     */



    /**
     * This is a helper method for browse.  It takes the results of an SQL
     * query and converts them into a list of GeneralMovies.
     * TODO: verify assumption that GeneralMovie can be constructed using a SKU
     * @param result the results of an SQL query
     * @return a list of GeneralMovies that were in the results
     * @throws SQLException if a database access error occurs or this method
     * is called on a closed result set
     * @deprecated
     */
    /*
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
     *
     */



}

