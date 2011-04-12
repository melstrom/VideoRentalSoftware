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



	private Statement statement;
	private PreparedStatement pstatement;
	private Connection connection;
	
	
	/**
		Gets the total number of rows of a table.
		@param table the table name.
		@return the total number of invoices stored in the db.
	*/
	public int getTotalNumberOfInvoices(String table, String key) throws SQLException, ClassNotFoundException
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
		String employeeID = transaction.getEmployeeID() ;
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
			+ "tax) "
			+ "VALUES (?, ?, ?, ?, ?, ?)";
			
		initDB2(queryString);	// this can throw an exception
		
		pstatement.setInt(1, invoiceID);
		pstatement.setString(2, paymentMethod);
		pstatement.setString(3, currentTime);
		pstatement.setInt(4, customerID);
		pstatement.setString(5, employeeID);
		pstatement.setInt(6, taxRate);

		pstatement.executeUpdate();
		connection.close();
	}
	
	/**
		Init connection to the db and setup the PreparedStatement
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
		connection = DriverManager.getConnection(url); // this can throw SQLException
	}
	
}
