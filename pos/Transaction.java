package pos;


/*
todos/decisions
- when will transactionID be set? when payment is made or when transaction is completed?
or should it be set in the constructor? what if a transaction is canceled in the middle after setting the transactionID in constructor
- same with transactionDate, when should it be set, after payment or in constructor?
- when should tax amount be calculated? after item is added or after payment is made or whenever getTax() method is called?
- Maybe the methods checkOutBy*() should be in another class and the method setPaymentMethod() should be a method in this class
- printReciept() not done: what should be displayed? This sounds like a method for the main program because most of the info for a reciept can be gotten from methods in the transaction class (getDate(), getTotal(), etc).
- should transactionID be an int?
*/

/*
todo: update class diagram for transaction to version2
- remove printReciept() method
- add precondition to printReciept(), where ever it goes, it can only print if the transaction has been paid

- add methods so a printReciept method can be done in the main program
- add method isPaid() : boolean
- add method getCustomerFirstName()
- add method getCustomerLastName()
- add method getCustomerID()
- add method getNumberOfItems()
- add method getItemType(int itemNumber)
- add method getItemName(int itemNumber)
- add method getItemPrice(int itemNumber)

- add method markPaid(PaymentMethod payment)


- maybe add another class PaymentMethod with attributes: ammountInCents and type and methods: int getAmountInCents() and String getType()
- maybe add anouther class CreditCardMachine with methods: public boolean verify(int CCnumber, int amountInCents, int pin)
- maybe add another class DebitMachine in similar fashion as CreditCardMachine
- CrediCardMachine and DebitMachine could be subclass of PaymentMachine class with methods: boolean verify(int CCnumber, int amountInCents, int pin)
*/



import java.util.ArrayList;
import java.util.Date;
/**
	A point of sales transaction.
	@author Peter
	@see "class diagram from SRS+SDD from teamMitch"
*/

public class Transaction
{
	private int transactionID; // transactionID is set when payment is made and is the next transactionID
	// TODO: or should transactinID be set in constructor? what if a transaction is created and not paid (transaction is canceled)
	private Date transactionDate; // date is set when payment is made
	private ArrayList<TransactionItem> items;
	
	private int paymentAmount;
	private String paymentMethod;
	private int subtotalInCents; // this will be modified whenever addTransaction() is called
	private int taxInCents; // this will be modified whenever addTransaction() is called
	private boolean paid; // set to true if the transaction is finished and paid for. addTransactionITem() should be disabled after payment is made.
	//private Customer account; // the customer account being worked on 
	private String customerFirstName;
	private String customerLastName;
	private String customerID;
	private String employeeFirstName;
	private String employeeID;
	private double taxPercent;
	
	
	
	//the signiture of this constructor in the class diagram seems incomplete, how would I know the employee name/id and tax percent
	// the Customer object is passed to me, but then I can do things like account.setName("John likes poo"), that sounds bad. Maybe you shouldn't pass the Customer object and instead pass the information needed, the name/id/etc
	/*
		Constructs an empty transaction for the given customer.
		@param account the customer account.
		@param transactionID the id number of the transaction (aka invoice number).
		@param taxPercent the percent the tax amount is as a double.
		<dt><b>Precondition:</b><dd>
			<ul>
				<li>The customer account exists and is able to rent movies (account.getStatus() == true).</li>
				<li>The transactionID should be a positive number and be the smallest id that does not yet exist in the database.</li>
			</ul>
		<dt><b>Postcondition:</b><dd>
			<ul>
				<li>The transaction/invoice is associated with the customer</li>
			</ul>
	
	//status: 
	public Transaction(Customer account, int transactionID, double taxPercent)
	{
		
		this(account.getFname(),
			account.getLname(),
			account.getAccountID(),
			"employeeName goes here",
			"employeeID goes here",
			transactionID,
			taxPercent);
	}*/
	/**
		Constructs an empty transaction for the given customer name and id. I made this other constructor because I don't think you should pass the customer's Account object to the 1st Transaction constructor. If you did, then this class can do things like thisAccount.setName("newName").
		@param firstName the first name of the customer account.
		@param lastName the last name of the customer account.
		@param customerID the customer's membership card number.
		@param employeeName the first name of the employee that initially handled the transaction.
		@param employeeID the employee's id.
		@param taxPercent the percent the tax amount is as a double.
		<dt><b>Precondition:</b><dd>
			<ul>
				<li>The customer account exists and is able to rent movies (account.getStatus() == true).</li>
				<li>The transactionID should be a positive number and be the smallest id that does not yet exist in the database.</li>
			</ul>
		<dt><b>Postcondition:</b><dd>
			<ul>
				<li>The transaction/invoice is associated with the customer</li>
			</ul>
	*/
	public Transaction(String firstName, String lastName, String customerID, String employeeName, String employeeID, double taxPercent)
	{
		this.customerFirstName = firstName;
		this.customerLastName = lastName;
		this.customerID = customerID;
		this.employeeFirstName = employeeName;
		this.employeeID = employeeID;
		//this.transactionID = transactionID;
		
		paymentAmount = 0;
	    subtotalInCents = 0; // this will be modified whenever addTransaction() is called
	    taxInCents = 0;
	    
		this.taxPercent = taxPercent;
		paid = false;
		paymentMethod = "";
		items = new ArrayList<TransactionItem>();
	}
	

	/**
		Returns an item given the line number.
		@param line the line number of the item on the invoice.
		@throws IllegalStateException if the line is out of bounds.
	*/
	public TransactionItem getItem(int line) throws IllegalStateException
	{
		if ((items.size() < line) || (line <= 0))
		{
			throw new IllegalStateException("Line number out of bounds.");
		}
		return items.get(line - 1);
	}
	
	
	
	
	
	/**
		Checks if this transaction has been paid yet.
		@return true if this transaction has been paid for.
	*/
	public boolean isPaid()
	{
		return paid;
	}
	
	/**
		Gets the customer's first name that is associated with this transaction.
		@return the customer's first name.
	*/
	public String getCustomerFirstName()
	{
		return customerFirstName;
	}
	
	/**
		Gets the customer's last name that is associated with this transaction.
		@return the customer's first name.
	*/
	public String getCustomerLastName()
	{
		return customerLastName;
	}
	
	/**
		Gets the customer's ID that is associated with this transaction.
		@return the customer's ID.
	*/
	public String getCustomerID()
	{
		return customerID;
	}
	
	/**
		Gets the employee's first name that handled this transaction.
		@return the employee's first name.
	*/
	public String getEmployeeName()
	{
		return employeeFirstName;
	}
	
	/**
		Gets the employee's ID that handled this transaction.
		@return the employee's ID name.
	*/
	public String getEmployeeID()
	{
		return employeeID;
	}
	
	/**
		Gets the number of items in this transaction.
		@return the number of transaction items.
	*/
	public int getNumberOfItems()
	{
		return items.size();
	}
	/**
		Returns the type that the item is (Video Rental, Video Sale, Discount, etc).
		@param lineNumber the line number of the transaction item you wish to get info on (first item is index 1).
		@return the type of transaction item.
		@throws IndexOutOfBoundsException if the index is out of range (lineNumber less than 0 or greater than this.getNumberOfItems())
	*/
	public String getItemType(int lineNumber)
	{	
		int numberOfItems = items.size();
		if (numberOfItems == 0)
		{
			throw new IndexOutOfBoundsException("There are no items.");
		}
		else if (lineNumber > numberOfItems || lineNumber < 0)
		{
			throw new IndexOutOfBoundsException("lineNumber is out of range.");
		}
		
		return items.get(lineNumber).getType();
	}
		
	/**
		Gets the name of the item (Name of the movie or name of the discount, etc).
		@param lineNumber the line number of the transaction item you wish to get info on (first item is index 1).
		@return the name of the transaction item.
		@throws IndexOutOfBoundsException if the index is out of range (lineNumber less than 0 or greater than this.getNumberOfItems())
	*/
	public String getItemName(int lineNumber)
	{		
		int numberOfItems = items.size();
		if (numberOfItems == 0)
		{
			throw new IndexOutOfBoundsException("There are no items.");
		}
		else if (lineNumber > numberOfItems || lineNumber < 0)
		{
			throw new IndexOutOfBoundsException("lineNumber is out of range.");
		}
		
		return items.get(lineNumber).getName();
	}
	
	/**
		Gets the barcode of the item .
		@param lineNumber the line number of the transaction item you wish to get info on (first item is index 1).
		@return the barcode of the transaction item.
		@throws IndexOutOfBoundsException if the index is out of range (lineNumber less than 0 or greater than this.getNumberOfItems())
	*/
	public String getItemBarcode(int lineNumber)
	{		
		int numberOfItems = items.size();
		if (numberOfItems == 0)
		{
			throw new IndexOutOfBoundsException("There are no items.");
		}
		else if (lineNumber > numberOfItems || lineNumber < 0)
		{
			throw new IndexOutOfBoundsException("lineNumber is out of range.");
		}
		
		return items.get(lineNumber).getBarcode();
	}
	
	/**
		Gets the price of the item.
		@param lineNumber the line number of the transaction item you wish to get info on (first item is index 1).
		@return the price of the transaction item in cents.
		@throws IndexOutOfBoundsException if the index is out of range (lineNumber less than 0 or greater than this.getNumberOfItems())
	*/
	public int getItemPrice(int lineNumber)
	{		
		int numberOfItems = items.size();
		if (numberOfItems == 0)
		{
			throw new IndexOutOfBoundsException("There are no items.");
		}
		else if (lineNumber > numberOfItems || lineNumber < 0)
		{
			throw new IndexOutOfBoundsException("lineNumber is out of range.");
		}
		
		return items.get(lineNumber).getPrice();
	}
	
	
	
	
	/**
	Marks the transaction as finalized/paid.
	@param payment the payment method and ammount.
	@param nextAvailableInvoiceID the invoice id that this ivoice is associated with.
	@throws IllegalStateException if the Transaction has already been paid for or if the amount is not enough
	<dt><b>Precondition:</b><dd>
		<ul>
			<li>The transaction has not been paid for.</li>
			<li>payment.getAmount() >= thisTransaction.getSubTotal() + thisTransaction.getTax()</li>
			<li>payment has been verified</li>
				<ul>
					<li>for cash, the employee should visually inspect it.</li>
					<li>for credit, the dummy credit card machine class should verify it.</li>
					<li>for debit, the dummy debit machine class should verify it.</li>
				</ul>
		</ul>
	<dt><b>Postcondition:</b><dd>
		<ul>
			<li>The payment method and ammount is recorded in the transaction/invoice</li>
			<li>Transaction items can not be added anymore.</li>
			<li>The transaction has been marked as paid and this.isPaid() returns true.</li>
		</ul>
	
	*/
	public boolean markPaid(Payment payment, int nextAvailableInvoiceID) throws IllegalStateException, Exception
	{
		if (paid == true)
		{
			throw new IllegalStateException("Transaction has already been paid for.");
		}
		else if (payment.getAmount() < this.getSubTotal() + this.getTax())
		{
			throw new IllegalStateException("Payment amount is not enough.");
		}
		paid = true;
		paymentMethod = payment.getPaymentMethod();
		paymentAmount = payment.getAmount();
		transactionID = nextAvailableInvoiceID;
		updateItemInfo();
		setDate();
		return true;
	}
	
	
	/**
		helper method to set date
	*/
	private void setDate()
	{
		
		transactionDate = new Date();
	}
	/**
		helper method to update the items info once it is checked out
	*/	
	private void updateItemInfo()
                throws Exception
	{
		int total = items.size(); // total number of items	
		int counter;
		for (counter = 0; counter < total; counter++)
		{
			items.get(counter).updateItemInfoAtCheckOut(transactionID);
		}
		
	}



// Note: maybe this method doesnt belong in this class.
//	/**
//	 * Get the next transactinID.
//	 * @return the next transactionID that is not used.
//	// */
//	private long int getNextTransactionID()
//	{
//	    return 100000;
//	}


	/**
		Returns the invoice ID.
		@return the invoice number
		@throws IllegalStateException if the transaction has not been paid for.
		<dt><b>Precondition:</b><dd>
		<ul>
				<li>Payment for the transaction has been made. Invoice number is set when the customer has paid.</li>
		</ul>
	*/
	public int getInvoiceID() throws IllegalStateException
	{
		if (this.isPaid() == false)
		{
			throw new IllegalStateException("The invoice has not been paid, Invoice ID has not been set.");
		}
		return transactionID;
	}
	
	/**
		Adds a transaction item to the current transaction.
		@param line the transaction item to add
		<dt><b>Precondition:</b><dd>
			<ul>
				<li>Payment for the transaction has not been made (thisTransaction.isPaid() == false).</li>
			</ul>
		<dt><b>Postcondition:</b><dd>
			<ul>
				<li>The subtotal for the transaction is updated.</li>
				<li>The total tax amount is updated.</li>
			</ul>
	*/
	public void addTransactionItem(TransactionItem item) throws Exception
	{
		if (paid == true)
		{
			throw new IllegalStateException("Transaction has already been paid for.");
		}
		//System.out.println("Size 1: " + items.size());
		items.add(item);
		//System.out.println("Size 2: " + items.size());
		subtotalInCents += item.getPrice();
		taxInCents = (int)(taxPercent * subtotalInCents);
	}
	
	/**
		Removes a transaction item from the transaction.
		@param lineNumber the item you want to remove from the transaction.
		@return returns true if the transaction item was removed.
		@throws IndexOutOfBoundsException if the index is out of range (lineNumber less than 0 or greater than this.getNumberOfItems())
		<dt><b>Precondition:</b><dd>
		<ul>
			<li>(lineNumber is betwen 1 and this.getNumberOfItems(), inclusive)</li>
		</ul>
		<dt><b>Postcondition:</b><dd>
		<ul>
			<li>The transaction item associated with the lineNumber is removed. All other item's line numbers are shifted down</li>
		</ul>
	*/
	public boolean removeTransactionItem(int lineNumber) throws IndexOutOfBoundsException
	{
		int numberOfItems = items.size();
		if (numberOfItems == 0)
		{
			throw new IndexOutOfBoundsException("There are no items to remove, list is empty.");
		}
		else if (lineNumber > numberOfItems || lineNumber < 0)
		{
			throw new IndexOutOfBoundsException("lineNumber is out of range.");
		}
		items.remove(lineNumber - 1);
		if (numberOfItems - 1 == items.size())
			return true;
		else
			return false;
	}
	
	/**
		Removes the last transaction item added.
		@return returns true if the last transaction was successfuly removed.
		@throws IndexOutOfBoundsException if the list is empty
		<dt><b>Precondition:</b><dd>
		<ul>
			<li>The number of items in the transaction is not zero</li>
		</ul>
		<dt><b>Postcondition:</b><dd>
		<ul>
			<li>The last transaction item added is removed.</li>
		</ul>
	 */
	public boolean removeLastTransactionItem() throws IndexOutOfBoundsException
	{
		int numberOfItems = items.size();
		if (numberOfItems == 0)
		{
			throw new IndexOutOfBoundsException("There are no items to remove, list is empty");
		}
		items.remove(items.size() - 1);
		if (numberOfItems - 1 == items.size())
			return true;
		else
			return false;
	}

	/**
		Gets the date of the transaction. The date of the transaction is set after payment is made and the transaction is finished.
		@return the date the transaction was completed.
		@throws IllegalStateException if the transaction has not been paid for (no payment == no transaction has happened).
	*/
	public Date getDate() throws IllegalStateException
	{
		if (this.isPaid() == false)
		{
			throw new IllegalStateException("The invoice has not been paid, no date set.");
		}
		return transactionDate;
	}
	
	/**
		Gets the total tax ammount for the transaction in cents.
		@return the total tax ammount for the transaction in cents.
	*/
	public int getTax()
	{
		return taxInCents;
	}
	
	/**
		Gets the payment method.
		@return the payment method.
		@throws IllegalStateException if the transaction has not been paid for yet.
	*/
	public String getPaymentMethod() throws IllegalStateException
	{
		if (paid == false)
		{
			throw new IllegalStateException("Transaction has not been paid for.");
		}
		return paymentMethod;
	}
	
	/**
		Gets the sub total for the transaction in cents.
		@return the sub total for the transaction in cents.
	*/
	public int getSubTotal()
	{
		return subtotalInCents;
	}
	
	
	/**
		Gets the total for the transaction in cents.
		@return the total for the transaction cents.
	*/
	public int getTotal()
	{
		return subtotalInCents + taxInCents;
	}


/*
////////////////////////////////////////////////////////////////////////////////////////////////////////

		Make a payment for the transaction by cash. After a transaction is paid for, items should not be allowed to be added to the transaction anymore.
		@param ammount the ammount of cash used to pay for the transaction if the payment method is cash.
		<dt><b>Postcondition:</b><dd>
		addTransactionItem() method will not add anymore items

	public boolean checkoutByCash(int ammount)
	{
	}
	

		Make a payment for the transaction by credit card.
		@return true if the payment was successful.
		<dt><b>Postcondition:</b><dd>
		addTransactionItem() method will not add anymore items

	public boolean checkoutByCredit()
	{
	}
	

		Make a payment for the transaction by debit.
		@param ammount the ammount of cash used to pay for the transaction if the payment method is cash.
		<dt><b>Postcondition:</b><dd>
		addTransactionItem() method will not add anymore items

	public boolean checkoutByDebit()
	{
	}
	

		Prints a reciept.
		<dt><b>Precondition:</b><dd>
		The transaction has been paid for.

	public void printReciept()
	{
	}
	

		Returns the contents of the reciept as a string.
		<dt><b>Precondition:</b><dd>
		The transaction has been paid for.

	public String printRecieptString()
	{
	}
////////////////////////////////////////////////////////////////////////////////////////////////////////	
*/
	/**
		Returns a String object representing this Transaction (for debuging).
		@return a string representatin of this transaction.
	*/
	public String toString()
	{
		String newline = System.getProperty("line.separator");
		String myTransaction = "Transaction ID: " + transactionID + newline;
		myTransaction += "Date: " + transactionDate + newline;
		
		myTransaction += "Number of Items: " + items.size() + newline;
		
		int i = 1;
		if (items.size() != 0)
			myTransaction += "      - Item Type - Item name - Item price" + newline;
		for (i = 0; i < items.size(); i++)
		{
			myTransaction += "Item " + (i+1) + ": " + this.getItemType(i) + " - " + this.getItemName(i) + " - " + this.getItemPrice(i) + newline;
		}
		
		if (paid)
		{
			myTransaction += "Paid: Yes" + newline;
			myTransaction += "Method of Payment: " + paymentMethod + newline;
			myTransaction += "Amount of money given (in cents): " + paymentAmount + newline;
			myTransaction += "Change: " + (paymentAmount - subtotalInCents - taxInCents) + newline;
		}
		else
		{
			myTransaction += "Paid: No" + newline;
		}
		
		return myTransaction;
	}

	/**
	 * This returns the tax rate at the time of the transaction as an integer (12 means 12 percent)(assumptions: only 1 tax).
	 * @return the tax rate as an integer (12 means 12 percent).
	 */
	public int getTaxRateAtTimeOfSale()
	{
	    return (int)(taxPercent*100);
	}
	
	
	
}
