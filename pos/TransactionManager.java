/*
Things that are unclear:
1. What happens if you add barcode twice. Should you be able to do that? What if a promo is rent 2 movies and get a discount and the guy rents 4 movies.


*/
package pos;

import java.lang.ClassNotFoundException;
import java.sql.SQLException;
import inventory.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Date;


/**
	This object manages one transaction, the current one.
	@author Peter
*/
public class TransactionManager
{
	private Transaction myTransaction;
	private Payment myPayment;
	private PreparedStatement pstatement;
	private Statement statement;
	private Connection connection;
	private SQLhelper mySQLhelper;
	final private double TAX_RATE = 0.12;
	
	/**
		Constructor stuff
	*/
	public TransactionManager()
	{
		mySQLhelper = new SQLhelper();
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
	public void createTransaction(String firstName, String lastName, String customerID, String employeeName, String employeeID)
	{
		myTransaction = new Transaction(firstName, lastName, customerID, employeeName, employeeID, TAX_RATE);
	}
	
	/**
		This adds/saves the current transaction to the database.
		@throws IllegalStateException if the transaction has not been paid for.
		<dt><b>Precondition:</b><dd>
		<ul>
				<li>Payment for the transaction has been made.</li>
		</ul>
		
	*/
	public void process() throws IllegalStateException, SQLException, ClassNotFoundException
	{
		if (myTransaction.isPaid() == false)
		{
			throw new IllegalStateException("The invoice has not been paid, not saving info.");
		}
		mySQLhelper.insertInvoiceTable(myTransaction);
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
	/*public void addItem(String barcode) throws Exception
	{
		//TransactionItem myItem = someHelperMethod(barcode);
		// TODO find out who is working on a method that does this (get a item when given a barcode), for now, a dummy object will be made
		
		TempInventoryItem myItem = new TempInventoryItem();
		myTransaction.addTransactionItem(myItem);
	}*/
        
        public void addMovie(String barcode)
                throws SQLException,ClassNotFoundException, IOException, 
                       MovieNotFoundException, Exception
        {
            MovieInterface movie = new MovieInterface(barcode);
            myTransaction.addTransactionItem(movie);
        }

        /*public void addDiscount()
        {

        }*/

        public void addPenalty(int price)
                throws IOException,Exception
        {
            Penalty pen = new Penalty(price);
            myTransaction.addTransactionItem(pen);
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
		@return no idea what this is suppose to return.
		@throws SQLException if sql error.
		@throws ClassNotFoundException if sql error.
		@throws IllegalStateException if there are no items on the invoice.
	*/
	public double pay(String paymentMethod, int amount ) throws IllegalStateException, SQLException, ClassNotFoundException, Exception
	{
		if (myTransaction.getNumberOfItems() == 0)
		{
			throw new IllegalStateException("There are no items on the Invoice.");
		}
	
		// use these two lines if you want invoices to be numbered 1000 and up
		// int starting = 999;
		// int nextInvoiceID = mySQLhelper.getTotalNumberOfInvoices() + 1;
		
		int nextInvoiceID = mySQLhelper.getTotalNumberOfRows(SQLhelper.TRANSACTION_TABLE_NAME, SQLhelper.TRANSACTION_TABLE_PK) + 1;
		//Payment myPayment = new Payment(amount, paymentMethod);
		return myTransaction.markPaid(paymentMethod, amount, nextInvoiceID);
	}
	/**
		Gets the total price that needs to be collected for this Transaction
		@return total price for transaction in cents
		@throws IlelgalStateException if the transaction has not been created with this.createTransaction()
	*/
	public int getTotal() throws IllegalStateException
	{
		if (myTransaction == null)
			throw new IllegalStateException();
		return myTransaction.getTotal();
	}
}
