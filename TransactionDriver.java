/**
	This is a small program that tests the Transaction class.
*/

/*	sample output

* Contents of the Invoice/Transaction:
Transaction ID: 12333
Date: null
Number of Items: 0
Paid: No

* Contents of the Invoice/Transaction:
Transaction ID: 12333
Date: null
Number of Items: 1
      - Item Type - Item name - Item price
Item 1: Rental Video - Batman - 320
Paid: No

* Contents of the Invoice/Transaction:
Transaction ID: 12333
Date: null
Number of Items: 2
      - Item Type - Item name - Item price
Item 1: Rental Video - Batman - 320
Item 2: Sale Video - Batman Returns - 500
Paid: No

* Contents of the Invoice/Transaction:
Transaction ID: 12333
Date: null
Number of Items: 2
      - Item Type - Item name - Item price
Item 1: Rental Video - Batman - 320
Item 2: Sale Video - Batman Returns - 500
Paid: Yes
Method of Payment: Credit Card
Amount of money given (in cents): 918
Change: 0


*/

public class TransactionDriver
{
	public static void main(String[] args)
	{
		// create new Transaction
		String customerFirstName = "Steve";
		String customerLastName = "Smith";
		String customerID = "1005673";
		String employeeName = "John";
		String employeeID = "123";
		int transactionID = 12333;
		double taxPercent = 0.12;
		
		Transaction myInvoice = new Transaction(customerFirstName, customerLastName, customerID, employeeName, employeeID, transactionID, taxPercent);
		
		System.out.println("* Contents of the Invoice/Transaction:");
		System.out.println(myInvoice.toString());
		
		// create some items
		TransactionItem item1 = new TransactionItem("Rental Video", "Batman", 320);
		TransactionItem item2 = new TransactionItem("Sale Video", "Batman Returns", 500);
		
		// add those items to the transaction
		myInvoice.addTransactionItem(item1);
		System.out.println("* Contents of the Invoice/Transaction:");
		System.out.println(myInvoice.toString());
		myInvoice.addTransactionItem(item2);
		System.out.println("* Contents of the Invoice/Transaction:");
		System.out.println(myInvoice.toString());
		
		
		// make a payment
		Payment myPayment = new Payment(myInvoice.getTotal(), "Credit Card");
		
		
		CardPaymentMachine visa = new CardPaymentMachine();
		
		// get customer to swipe card and enter pin
		int customerCreditCardNumber = 123;
		int customerPIN = 555;
		
		// verify the payment
		if (visa.verify(customerCreditCardNumber, myInvoice.getTotal(), customerPIN))
		{
			//mark paid
			myInvoice.markPaid(myPayment);
		}
		
		
		System.out.println("* Contents of the Invoice/Transaction:");
		System.out.println(myInvoice.toString());
	}
}