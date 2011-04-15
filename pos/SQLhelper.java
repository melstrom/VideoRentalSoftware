
package pos;
//import pos.Transaction;
import account.Address;
import account.Employee;
import account.Customer;
import account.Account;
import java.util.ArrayList;

// sql stuff
import java.lang.ClassNotFoundException;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.GregorianCalendar;
import java.util.Date;

/**
	Helper class for my stuff; given a Object (Address or Transaction), save it in the database; given a primary key of a row, get that Object (Address).
	@author Peter
*/
public class SQLhelper
{	
	private static final boolean DEBUG_MODE = true;
	
	// constants for table names and their primary keys
	// the variable names are the class diagram names
	public static final String TRANSACTION_TABLE_NAME = "invoice";
	public static final String TRANSACTION_TABLE_PK = "invoiceID";
	public static final String GENERALMOVIE_TABLE_NAME = "videoInfo";
	public static final String GENERALMOVIE_TABLE_PK = "InfoID";
	public static final String ADDRESS_TABLE_NAME = "address";
	public static final String ADDRESS_TABLE_PK = "addressID";
	public static final String EMPLOYEE_TABLE_NAME = "employee";
	public static final String EMPLOYEE_TABLE_PK = "employeeID";
	public static final String ACCOUNT_TABLE_NAME = "account";
	public static final String ACCOUNT_TABLE_PK = "accountID";
	public static final String CUSTOMER_TABLE_NAME = "customer";
	public static final String CUSTOMER_TABLE_PK = "customerID";
	
	
	












	private Statement statement;
	private PreparedStatement pstatement;
	private Connection connection;
	
	
	/**
		Gets the total number of rows of a table.
		@param table the table name
		@param key the primary key of the table
		@return the total number of rows of a table, when given its primary key
		@throws SQLException if a sql error.
		@throws ClassNotFoundException if connection to db error
	*/
	public int getTotalNumberOfRows(String table, String key) throws SQLException, ClassNotFoundException
	{
		int total = 0;
		setupStatement();
		ResultSet resultSet = statement.executeQuery("SELECT COUNT(" + key + ") FROM " + table);
		while (resultSet.next())
		{
			total = Integer.parseInt(resultSet.getString(1));
		}
		closeConnection();
		return total;
	}
	
	/**
	 * Method to add a customer or employee to the database.
	 * @param theAccount an Account object that has not been saved
	 * @return barcode the account id
	 * @throws IllegalStateException if the customer or employee is already in the db (based on dl for customer and based on ph for employee)
	 */
	public int createAccount(Account theAccount) throws ClassNotFoundException, IllegalStateException, SQLException
	{
	    int addressPrimaryKey = insertAddressTable(theAccount.getAddress());
            int accountPrimaryKey = insertAccountTable(addressPrimaryKey, theAccount.getFname(), theAccount.getLname(), theAccount.getPhoneNum());
	    int barcode = 0;
	    if (theAccount instanceof Employee)
	    {
		barcode = insertNewEmployee((Employee)theAccount, accountPrimaryKey);
	    }
	    else if (theAccount instanceof Customer)
	    {
		barcode = insertNewCustomer((Customer)theAccount, accountPrimaryKey);
	    }
	    return barcode;

	}
	/**
	 * Method to add employee info to the database.
	 * @param theEmployee the employee you wish to add to the database
	 * @return the primary key of hte tuple entry
	 * @throws IllegalStateException if the employee is already in the db
	 */
	private int insertNewEmployee(Employee theEmployee, int accountID) throws ClassNotFoundException, IllegalStateException, SQLException
	{
	   // int employeePrimaryKey = 1 + getTotalNumberOfRows(EMPLOYEE_TABLE_NAME, EMPLOYEE_TABLE_PK);
	   //              ^- the key will be found and set when the Customer or Employee object is created.
	    String queryString = "INSERT INTO " + EMPLOYEE_TABLE_NAME + " ("
		+ "employeeID,"
		+ "accountID,"
		+ "position"
		+ ") VALUES (?, ?, ?)";
	    setupPreparedStatement(queryString);
	    pstatement.setInt(1, theEmployee.getAccountID());
	    pstatement.setInt(2, accountID);
	    pstatement.setString(3, theEmployee.getPosition());
	    pstatement.executeUpdate();
	    closeConnection();
	    return employeePrimaryKey;
	}


	/**
	 * Method to add a customer to the database.
	 * @param theCustomer the customer account to add to the database
	 * @return the primary key of the tuple
	 * @throws IllegalStateException if the customer is already in the database (based on dl)
	 */
	private int insertNewCustomer(Customer theCustomer, int accountID) throws ClassNotFoundException, IllegalStateException, SQLException
	{
	    // todo add code to check if phone number exists
	    //
	    //
	    //int customerPrimaryKey = 1 + getTotalNumberOfRows(CUSTOMER_TABLE_NAME, CUSTOMER_TABLE_PK);


	    String queryString = "INSERT INTO " + CUSTOMER_TABLE_NAME + " ("
		+ "customerID,"
		+ "accountID,"
		+ "driversLicense"
		+ ") VALUES (?, ?, ?)";
	    setupPreparedStatement(queryString);
	    pstatement.setInt(1, theCustomer.getAccountID());
	    pstatement.setInt(2, accountID);
	    pstatement.setString(3, theCustomer.getDL());
	    pstatement.executeUpdate();
	    closeConnection();
	    return customerPrimaryKey;
	}
	/**
	 * Method to add account info to the db.
	 * @param addressID the primary key of the new address
	 * @param firstName the first name of the new account
	 * @param lastName the last name of the new account
	 * @param phoneNum the account's phone number
	 * @return the accountID the primary key of the account tuple
	 */
	private int insertAccountTable(int addressID, String firstName, String lastName, String phoneNum) throws ClassNotFoundException, SQLException
	{
	    int accountID = 1 + getTotalNumberOfRows(ACCOUNT_TABLE_NAME, ACCOUNT_TABLE_PK);
	    String queryString = "INSERT INTO " + ACCOUNT_TABLE_NAME + " ("
		+ "accountID, "
		+ "addressID, "
		+ "firstName, "
		+ "lastName, "
		+ "phoneNum"
		+ ") VALUES (?, ?, ?, ?, ?)";
	    setupPreparedStatement(queryString);
	    pstatement.setInt(1, accountID);
	    pstatement.setInt(2, addressID);
	    pstatement.setString(3, firstName);
	    pstatement.setString(4, lastName);
	    pstatement.setString(5, phoneNum);
	    pstatement.executeUpdate();
	    closeConnection();
	    return accountID;
	}
	
	/**
	 * Method to insert a Address into the db
	 * @param address the address to save
	 * @return the addressID (primary key of the tuple) of the address is returned
	 */
	public int insertAddressTable(Address address) throws IllegalStateException, SQLException, ClassNotFoundException, NumberFormatException
	{
	    int addressID = 1 + getTotalNumberOfRows(ADDRESS_TABLE_NAME, ADDRESS_TABLE_PK);
	    int houseNumber = address.getHouseNumber();
	    String streetName = address.getStreetName();
	    String city = address.getCity();
	    String province = address.getProvince();
	    String country = address.getCountry();
	    String postalCode = address.getPostalCode();

	    String queryString = "INSERT INTO " + ADDRESS_TABLE_NAME + " ("
		+ "addressID,"
		+ "houseNumber,"
		+ "streetName,"
		+ "city,"
		+ "province,"
		+ "country,"
		+ "postalCode"
		+ ") VALUES (?, ?, ?, ?, ?, ?, ?)";
		
	    setupPreparedStatement(queryString);
	    pstatement.setInt(1, addressID);
	    pstatement.setInt(2, houseNumber);
	    pstatement.setString(3, streetName);
	    pstatement.setString(4, city);
	    pstatement.setString(5, province);
	    pstatement.setString(6, country);
	    pstatement.setString(7, postalCode);
	    pstatement.executeUpdate();
	    closeConnection();
	    
	    return addressID;
	}
	/* removed this method cause Account object has a method to change the addresss when given an address object
		Method to update info in the database of a Address
		@throws ClassNotFoundException if JDBC driver is not in CLASSPATH
		@throws SQLException if a database access error occurs or this method is called on a closed connection
	public void updateAddress(Address address) throws ClassNotFoundException, SQLException
	{
	     addressID = address.getAddressID();
	    int houseNumber = address.getHouseNumber();
	    String streetName = address.getStreetName();
	    String city = address.getCity();
	    String province = address.getProvince();
	    String country = address.getCountry();
	    String postalCode = address.getPostalCode();
	    
	    
	    
	    String queryString = "UPDATE " + ADDRESS_TABLE_NAME + " SET "
		+ "houseNumber=?,"	// 1
		+ "streetName=?,"	// 2
		+ "city=?,"			// 3
		+ "province=?,"		// 4
		+ "country=?,"		// 5
		+ "postalCode=?"	//6
		+ " WHERE addressID=" + addressID;
		
	    setupPreparedStatement(queryString);
	    pstatement.setInt(1, houseNumber);
	    pstatement.setString(2, streetName);
	    pstatement.setString(3, city);
	    pstatement.setString(4, province);
	    pstatement.setString(5, country);
	    pstatement.setString(6, postalCode);
	    
	    pstatement.executeUpdate();
	    closeConnection();
	}
	*/
	/*
		Method to get an Address when given the primary key.
		@throws ClassNotFoundException if JDBC driver is not in CLASSPATH
		@throws SQLException if a database access error occurs or this method is called on a closed connection or no results
	public Address getAddress(int addressID) throws ClassNotFoundException, SQLException
	{
		String queryString = "SELECT "
			+ "houseNumber, "
			+ "streetName, "
			+ "city, "
			+ "province, "
			+ "country, "
			+ "postalCode"
			+ " FROM " + ADDRESS_TABLE_NAME + " WHERE addressID=?";
		
		setupPreparedStatement(queryString);
		pstatement.setInt(1, addressID);
		
		ResultSet rs = pstatement.executeQuery();
		
		rs.next();
		
		Address myAddress = new Address(
			addressID,
			rs.getInt("houseNumber"),
			rs.getString("streetName"),
			rs.getString("city"),
			rs.getString("province"),
			rs.getString("country"),
			rs.getString("postalCode"));
		closeConnection();
		
		return myAddress;
	}
	*/
	
	/**
		Method to get an ArrayList of all employees in the database.
		@throws ClassNotFoundException if JDBC driver is not in CLASSPATH
		@throws SQLException if a database access error occurs or this method is called on a closed connection or no results
	*/
        /* moved this method to the search package
	public ArrayList<Employee> getAllEmployees() throws ClassNotFoundException, SQLException
	{
        ArrayList<Employee> allEmployees = new ArrayList<Employee>();
        String queryString = "SELECT "
        	+ "position, "
        	+ "employee.accountID, "
        	+ "firstName, "
        	+ "lastName, "
        	+ "houseNumber, "
        	+ "streetName, "
        	+ "city, "
        	+ "province, "
        	+ "postalCode, "
        	+ "country, "
        	+ "phoneNum "
        	+ "FROM employee, account, address WHERE employee.accountID=account.accountID AND account.accountID=address.addressID";
        
        
        setupStatement();
        ResultSet resultSet = statement.executeQuery(queryString);
        while (resultSet.next())
        {
        	String position = resultSet.getString("position");
        	int accountID = resultSet.getInt("accountID");
        	String firstName = resultSet.getString("firstName");
        	String lastName = resultSet.getString("lastName");
        	String houseNumber = resultSet.getString("houseNumber");
        	String streetName = resultSet.getString("streetName");
        	String city = resultSet.getString("city");
        	String province = resultSet.getString("province");
        	String postalCode = resultSet.getString("postalCode");
        	String country = resultSet.getString("country");
        	String phoneNum = resultSet.getString("phoneNum");
        	
        	String address =  houseNumber +" "+ streetName +", "+ city +", "+ province +", "+ postalCode +", "+ country;
        	
        	allEmployees.add(new Employee(position, accountID, firstName, lastName, address, phoneNum));
        }
		return allEmployees;
    }*/
	
	/**
		Method to insert transaction info into the db table.
		@param transaction the info of the Transaction object you want to save in the database.
		@pre transaction has been paid for and has a invoice id
		@throws IllegalStateException if the transaction has not been paid for.
		@throws SQLException if sql errrrrr.
		@throws ClassNotFoundException if sql err.
		@throws NumberFormatException if customerID is not a integer
		
	*/
	public void insertInvoiceTable(Transaction transaction) throws IllegalStateException, SQLException, ClassNotFoundException, NumberFormatException

	{
		// get info to insert into table
		int invoiceID = transaction.getInvoiceID();
		String paymentMethod = transaction.getPaymentMethod() ;
		String dateTime = transaction.getDate();
		int customerID = transaction.getCustomerID();
		int employeeID = transaction.getEmployeeID() ;
		int taxRate = transaction.getTaxRateAtTimeOfSale() ;
		
		//convert java Date object into String format sql insert command expects
		//java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//String currentTime = sdf.format(dateTime).toString();

		String queryString = "INSERT INTO " + TRANSACTION_TABLE_NAME + " ("
			+ "invoiceID,"
			+ "paymentMethod,"
			+ "dateTime,"
			+ "customerID,"
			+ "employeeID,"
			+ "tax,"
			+ "storeID) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
			
		setupPreparedStatement(queryString);	// this can throw an exception
		
		pstatement.setInt(1, invoiceID);
		pstatement.setString(2, paymentMethod);
		pstatement.setString(3, dateTime);
		pstatement.setInt(4, customerID);
		pstatement.setInt(5, employeeID);
		pstatement.setInt(6, taxRate);
		pstatement.setInt(7, 1); // set storeID to 1
	
		pstatement.executeUpdate();
		connection.close();
	}
	
	
	
	
	
	
	
	
	
	
	
	/**
		Method to insert movie info into the db table (this is for the cvs file reader).
		
		@param 
		@return the infoID of the video info added to the db
		
		@throws IllegalStateException if the transaction has not been paid for.
		@throws SQLException if sql errrrrr.
		@throws ClassNotFoundException if sql err.
		@throws NumberFormatException if customerID is not a integer
		
	*/
	public int insertVideoInfoTable() throws IllegalStateException, SQLException, ClassNotFoundException, NumberFormatException

	{
		// get info to insert into table
		int infoID = 1 + this.getTotalNumberOfRows("videoInfo", "InfoID");
		String description = "description goes here";
		String genre = "genre goes here";
		String producer = "producer goes here";
		String title = "some title";
		String actors = "Brad Pit";
		String studio = "studio goes here";
		String rating = "ratinggg";
		Date releaseDate = new Date();
		int length = 90;
		String director = "director goes here";
		
		//convert java Date object into String format sql insert command expects
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(releaseDate).toString();

		// if table names change, edit info here
		String queryString = "INSERT INTO videoInfo ("
			+ "InfoID,"
			+ "Description,"
			+ "Genre,"
			+ "Producer,"
			+ "Title,"
			+ "Actors,"
			+ "studio,"
			+ "Rating,"
			+ "releaseDate,"
			+ "length,"
			+ "director) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
		setupPreparedStatement(queryString);	// this can throw an exception
		
		pstatement.setInt(1, infoID);
		pstatement.setString(2, description);
		pstatement.setString(3, genre);
		pstatement.setString(4, producer);
		pstatement.setString(5, title);
		pstatement.setString(6, actors);
		pstatement.setString(7, studio);
		pstatement.setString(8, rating);
		pstatement.setString(9, time);
		pstatement.setInt(10, length);
		pstatement.setString(11, director);
		
		pstatement.executeUpdate();
		closeConnection();
		return infoID;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/**
		Init connection to the db for a SQL PreparedStatement (remember to close the connection after you are done).
		@param sql the sql prepared statement query string
		@throws ClassNotFoundException if JDBC driver is not in CLASSPATH
		@throws SQLException if a database access error occurs or this method is called on a closed connection
	*/
	private void setupPreparedStatement(String sql) throws ClassNotFoundException, SQLException
	{
		openConnection();
		pstatement = connection.prepareStatement(sql);
	}
	
	/**
		Init connection to the db for a SQL Statement (remember to close the connection after you are done).
		@throws ClassNotFoundException if JDBC driver is not in CLASSPATH
		@throws SQLException if a database access error occurs or this method is called on a closed connection
	*/
	private void setupStatement() throws ClassNotFoundException, SQLException
	{
		openConnection();
		statement = connection.createStatement(); // throws SQLException if a database access error occurs or this method is called on a closed connection
	}
	
	/**
		Code that is common to connecting to the db (This opens a connection).
		@throws ClassNotFoundException if JDBC driver is not in CLASSPATH
		@throws SQLException if a database access error occurs
	*/
	private void openConnection() throws ClassNotFoundException, SQLException
	{
		Class.forName("com.mysql.jdbc.Driver"); // can throw ClassNotFoundException
		String url = "jdbc:mysql://174.132.159.251:3306/kpoirier_CPSC2301?user=kpoirier_User&password=foobar";
//		String url = "jdbc:mysql://192.168.0.16:3306/storedb?user=videoStore&password=over9000";
		connection = DriverManager.getConnection(url); // this can throw SQLException
	}
	
	/**
		Closes a connection to the db server
		@throws SQLException if a database access error occurs
	*/
	private void closeConnection() throws SQLException
	{
		connection.close();
	}
	
}
