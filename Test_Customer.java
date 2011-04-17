/*
 * Status: Employee class not working
 * functionality taht is working, instantiating a Employee object and creating an Employee account based on it (saving it in the db)
 */


/**
 * This is a small program to test Employee class
 * @author Peter
 */
import account.Employee;
import account.Customer;
import account.Address;
import pos.SQLhelper;

public class Test_Customer
{
    public static void main(String[] args) throws Throwable
    {
	SQLhelper mySQLhelper = new SQLhelper();

	Address empAddress = new Address(552, "Cambie st.", "Vancouver", "B.C.", "Canada", "V6V 1V1");


	
	int accountID = 5;
	int employeeID = 6;
	String firstName = "CustomerFName";
	String lastName = "Smith";
	//String address = "4321 Electric Ave.";
	String phoneNum = "604-888-9876";
	String dl = "666123456";
	
	// any accountID given to  "createAccount()" is not used,  The method sets it.
	Customer myCustomer = new Customer(dl, 666, firstName, lastName, empAddress, phoneNum); 
	mySQLhelper.createAccount(myCustomer);
	
	/*
	SELECT customerID, firstName, city, phoneNum FROM address.addressID=account.addressID AND account.accountID=customer.accountID;
	*/

    }

}


