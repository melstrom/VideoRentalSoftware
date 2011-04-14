/*
 * Status: Employee class not working
 */

/**
 * This is a small program to test Employee class
 * @author Peter
 */
import account.Employee;
public class Test_Employee
{
    public static void main(String[] args)
    {
	String position = "slave";
	int accountID = 5;
	String firstName = "Tom";
	String lastName = "Smith";
	String address = "4321 Electric Ave.";
	String phoneNum = "604-555-9876";
	Employee myEmployee = new Employee(position, accountID, firstName, lastName, address, phoneNum);
    }

}


