/*
Things that are unclear:
1. What happens if you add barcode twice. Should you be able to do that? What if a promo is rent 2 movies and get a discount and the guy rents 4 movies.


*/
package pos;

//import inventory.MovieManagement;
//import jdbconnection.JDBCConnection;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;

import java.util.Date;

/**
	This object manages one transaction, the current one.
*/
public class TransactionManager
{
	private Transaction myTransaction;
	private Payment myPayment;
	private PreparedStatement pstatement;
	private Statement statement;
	Connection connection;
	
	
	/**
		This constructor does nothing right now.
	*/
	public TransactionManager()
	{
	}
	
	/**
		This creates a "current transaction" to work on.
		@param firstName the first name of the customer account.
		@param lastName the last name of the customer account.
		@param customerID the customer's membership card number.
		@param employeeName the first name of the employee that initially handled the transaction.
		@param employeeID the employee's id.
		@param taxPercent the percent the tax amount is as a double.
	*/
	public void createTransaction(String firstName, String lastName, String customerID, String employeeName, String employeeID, double taxPercent)
	{
		myTransaction = new Transaction(firstName, lastName, customerID, employeeName, employeeID, taxPercent);
	}
	
	/**
		This adds/saves the current transaction to the database.
		@throws IllegalStateException if the transaction has not been paid for.
		<dt><b>Precondition:</b><dd>
		<ul>
				<li>Payment for the transaction has been made.</li>
		</ul>
		
	*/
	public void process() throws IllegalStateException
	{
		if (myTransaction.isPaid() == false)
		{
			throw new IllegalStateException("The invoice has not been paid, not saving info.");
		}
		int transactionID = myTransaction.getInvoiceID();
		Date transactionDate = myTransaction.getDate();
		//ArrayList<TransactionItem> items;
//int paymentAmount;
		String paymentMethod = myTransaction.getPaymentMethod();
		//int subtotalInCents = myTransaction.getSubTotal();
		//int taxInCents = myTransaction.getTax();
		//boolean paid = myTransaction.isPaid();
		//private Customer account; // the customer account being worked on 
		//String customerFirstName = myTransaction.getCustomerFirstName();
		//String customerLastName = myTransaction.getCustomerLastName();
		String customerID = myTransaction.getCustomerID();
		//String employeeFirstName = myTransaction.getEmployeeName();
		String employeeID = myTransaction.getEmployeeID();
		int taxRate = myTransaction.getTaxRateAtTimeOfSale();
		
		insertInvoiceTable(transactionID, paymentMethod, transactionDate, customerID, employeeID, taxRate);
		
	}
	
	/**
		Method to insert transaction info into the db table.
	*/
	private void insertInvoiceTable(int invoiceID, String paymentMethod, Date dateTime, String customerID, String employeeID, int taxRate)
	{
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
			
		initDB2(queryString);	
		
		pstatement.setInt(1, invoiceID);
		pstatement.setString(2, paymentMethod);
		pstatement.setString(3, currentTime);
		pstatement.setString(4, customerID);
		pstatement.setString(5, employeeID);
		pstatement.setInt(6, taxRate);

		pstatement.executeUpdate();
		connection.close();
	}
	
	/**
		Init connection to the db and setup the PreparedStatement
	*/
	private void initDB2(String sql)
	{
		initDBcommon();
		pstatement = connection.prepareStatement(sql);
	}
	/**
		Init connection to the db and setup the Statement
	*/
	private void initDB()
	{
		initDBcommon();
		statement = connection.createStatement();
	}
	private void initDBcommon()
	{
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://174.132.159.251:3306/kpoirier_CPSC2301?user=kpoirier_User&password=foobar";
		connection = DriverManager.getConnection(url);
	}
	
	/**
		This prints a reciept as a String.
		@return a String representing the reciept
		@throws IllegalStateException if the Transaction has not been paid for.
	*/
	public String printReciept() throws IllegalStateException
	{
		if (myTransaction.isPaid() == false)
			throw new IllegalStateException("Transaction has not been paid for.");
		return myTransaction.toString();
	}
	
	/**
		This adds a item to the current transaction.
		@param barcode the item's barcode number.
	*/
	public void addItem(String barcode)
	{
		TransactionItem myItem = someHelperMethod(barcode);
		myTransaction.addTransactionItem(myItem);
	}
	
	/**
		This removes a item from the current transaction.
		@param barcode the item's barcode number.
		@throws IllegalStateException if there are no items on the invoice to remove.
	*/
	public void removeItem(String barcode) throws IllegalStateException
	{
		int itemCount = myTransaction.getNumberOfItems();
		if (itemCount == 0)
		{
			throw new IllegalStateException("There are no items on the Invoice.");
		}

		// compare each item that is currently on the invoice with the barcode given
		boolean found = false;
		int line = 1; // the line in the invoice where this item is, start looking on line 1.
		for (line = 1; line <= itemCount; line++)
		{
			TransactionItem itemOnInvoice = myTransaction.getItem(line);
			if (itemOnInvoice.getBarcode() == barcode)
			{
				found = true;
				break;
			}
		}
		if (!found)
			throw new IllegalStateException("Item not in list");
		else	
			myTransaction.removeTransactionItem(line);
	}
	
	/**
		This method collects payment for the current transaction.
		@param amount the amount of money collected from the customer.
		@param payment no idea what this is (TODO: ask about class diagram)
	*/
	public int pay(int amount, String paymentMethod) throws IllegalStateException
	{
		int nextInvoiceID = getTotalNumberOfInvoices() + 1;
		Payment myPayment = new Payment(amount, paymentMethod);
		myTransaction.markPaid(myPayment, nextInvoiceID);
	}
	
	/**
		Gets the total number of invoices
		@return the total number of invoices stored in the db.
	*/
	private int getTotalNumberOfInvoices()
	{
		int total;
		initDB();
		ResultSet resultSet = statement.executeQuery("SELECT COUNT(invoiceID) FROM invoice");
		while (resultSet.next())
		{
			total = Integer.parseInt(resultSet.getString(1));
		}
		connection.close();
		return total;
	}
	


}
