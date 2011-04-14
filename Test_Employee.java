/*
 * Status: Employee class not working
 */

/**
 * This is a small program to test Employee class
 * @author Peter
 */
import account.Employee;
import account.Address;
import pos.SQLhelper;

public class Test_Employee
{
    public static void main(String[] args) throws Throwable
    {
	SQLhelper mySQLhelper = new SQLhelper();

	Address empAddress = new Address(223, "Cambie st.", "Vancouver", "B.C.", "Canada", "V6V 1V1");


	String position = "slave";
	int accountID = 5;
	int employeeID = 6;
	String firstName = "Tom";
	String lastName = "Smith";
	//String address = "4321 Electric Ave.";
	String phoneNum = "604-555-9876";
	Employee myEmployee = new Employee(position, accountID, firstName, lastName, empAddress, phoneNum, employeeID);

    }

}


