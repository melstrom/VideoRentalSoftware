/*
Things that are unclear:
1. What happens if you add barcode twice. Should you be able to do that? What if a promo is rent 2 movies and get a discount and the guy rents 4 movies.


*/
package pos;

import inventory.MovieManagement;
import jdbconnection.JDBCConnection;

/**
	This object manages one transaction, the current one.
*/
public class TransactionManager
{
	private Transaction myTransaction;
	private Payment myPayment;
	private Statement myStatement;
	
	
	
	/**
		This constructor does nothing.
	*/
	public TransactionManager()
	{
	}
	
	/**
		This creates a "current transaction" to work on.
	*/
	public void createTransaction()
	{
	}
	
	/**
		This adds/saves the current transaction to the database.
	*/
	public void process()
	{
	}
	
	/**
		This prints a reciept as a String.
		@return a String representing the reciept
	*/
	public String printReciept()
	{
	}
	
	/**
		This adds a item to the current transaction.
		@param barcode the item's barcode number.
	*/
	public void addItem(String barcode)
	{
	}
	
	/**
		This removes a item from the current transaction.
		@param barcode the item's barcode number.
	*/
	public void removeItem(String barcode)
	{
	}
	
	/**
		Pays for the current transaction.
		@param amount the amount of money collected from the customer.
		@param payment no idea what this is (TODO: ask creator of class diagram)
	*/
	public int pay(int amount, PaymentMethod payment)
	{
	}
	
	

}
