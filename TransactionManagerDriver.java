/**
	The purpose of this program is to do unit testing on TransactionManager Class, status is finished and working, some stubs are used for somestuff not implemented yet by others.
*/
/* sample output
$ java TransactionManagerDriver
Hi
Hi
Hi
Hi
Hi
Hi
Transaction ID: 7
Date: Mon Apr 11 21:27:19 PDT 2011
Number of Items: 1
      - Item Type - Item name - Item price
Item 1: video rental - Batman - 320
Paid: Yes
Method of Payment: cash
Amount of money given (in cents): 358
Change: 0


*/

import pos.TransactionManager;


public class TransactionManagerDriver
{

	public static void main(String[] args) throws Exception
	{
		System.out.println("Hi");
		TransactionManager myTM = new TransactionManager();
		System.out.println("Hi");
		
		String firstName = "Peter";
		String lastName = "C.";
		String customerID = "1";
		String employeeName = "Homer";
		String employeeID = "1";
		double taxPercent = 0.12;
		
		
		myTM.createTransaction(firstName, lastName, customerID, employeeName, employeeID, taxPercent);
		System.out.println("Hi");
		
		myTM.addItem("ignored"); // <- barcode goes there
		System.out.println("Hi");
		myTM.pay(myTM.getTotal(), "cash");
		System.out.println("Hi");
		myTM.process(); // add info to db
		System.out.println("Hi");
		System.out.println(myTM.printReciept());
	}
}