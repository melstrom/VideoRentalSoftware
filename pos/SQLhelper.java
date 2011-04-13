/**
	This is a helper object for TransactionManager
*/


//import pos.Transaction;
package pos;


// sql stuff
import java.lang.ClassNotFoundException;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Date;

public class SQLhelper
{
	// constant for table name
	// The string variable is the Class name, the value is the db table name
	public static final String Transaction = "invoice";
	public static final String GeneralMovie = "videoInfo";



	private Statement statement;
	private PreparedStatement pstatement;
	private Connection connection;
	
	
	/**
		Gets the total number of rows of a table.
		@param table the table name.
		@param key the primary key of the table.
		@return the total number of invoices stored in the db.
	*/
	public int getTotalNumberOfRows(String table, String key) throws SQLException, ClassNotFoundException
	{
		int total = 0;
		initDB();
		ResultSet resultSet = statement.executeQuery("SELECT COUNT(" + key + ") FROM " + table);
		while (resultSet.next())
		{
			total = Integer.parseInt(resultSet.getString(1));
		}
		connection.close();
		return total;
	}
	
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
		Date dateTime = transaction.getDate();
		int customerID = Integer.parseInt(transaction.getCustomerID()) ;
		int employeeID = Integer.parseInt(transaction.getEmployeeID()) ;
		int taxRate = transaction.getTaxRateAtTimeOfSale() ;
		
		//convert java Date object into String format sql insert command expects
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(dateTime).toString();

		String queryString = "INSERT INTO invoice ("
			+ "invoiceID,"
			+ "paymentMethod,"
			+ "dateTime,"
			+ "customerID,"
			+ "employeeID,"
			+ "tax,"
			+ "storeID) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
			
		initDB2(queryString);	// this can throw an exception
		
		pstatement.setInt(1, invoiceID);
		pstatement.setString(2, paymentMethod);
		pstatement.setString(3, currentTime);
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
		int infoID = this.getTotalNumberOfRows("videoInfo", "InfoID") + 1;
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
			
		initDB2(queryString);	// this can throw an exception
		
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
		connection.close();
		return infoID;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/**
		Init connection to the db and setup the PreparedStatement
		@param sql the sql prepared statement
		@throws SQLException if a database access error occurs or this method is called on a closed connection
	*/
	private void initDB2(String sql) throws SQLException, ClassNotFoundException
	{
		initDBcommon();
		pstatement = connection.prepareStatement(sql);
	}
	/**
		Init connection to the db and setup the Statement
		@throws SQLException if a database access error occurs or this method is called on a closed connection.
	*/
	private void initDB() throws SQLException, ClassNotFoundException
	{
		initDBcommon();
		statement = connection.createStatement(); // this can throw SQLException
	}
	private void initDBcommon() throws SQLException, ClassNotFoundException
	{
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://174.132.159.251:3306/kpoirier_CPSC2301?user=kpoirier_User&password=foobar";
//		String url = "jdbc:mysql://192.168.0.16:3306/storedb?user=videoStore&password=over9000";
		connection = DriverManager.getConnection(url); // this can throw SQLException
	}
	
}
