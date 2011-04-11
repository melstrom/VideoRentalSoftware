package account;
// TODO: sepearate account attribute into employee and customer
/**
 * Account Manager class
 * 
 * @author mattp
 * @version 1.0 April 3, 2011
 * @version 1.1 April 4
 *      -added isDuplicatedID()
 * @version 1.2 
 *	-added barcodeGenerator (incomplete)
 *	-added JDBCConnetion package
 *	-fixed demote(), promote() and editAccount()
 */
import java.sql.Connection;
import java.sql.Statement;
//import java.sql.DriverManager;
import jdbconnection.*;
import java.sql.SQLException;
import jdbconnection.JDBCConnection;

public class AccountManagement

{
    /**
     * Default constructor with no parameters
     */
    public AccountManagement()throws SQLException, ClassNotFoundException
    {
	JDBC = new JDBCConnection();
      connection = JDBC.getConnection();
    }
    /**
     * Create an employee account
     * @param position the position of the employee (Manager/Staff)
     * @param accountID the account ID of the account
     * @param Fname the first name of the user
     * @param Lname the last name of the user
     * @param address the address of the user
     * @param phoneNum the phone number of the user
     */
    public void createEmployee(String position, int accountID, String Fname, String Lname, String address, String phoneNum)
            throws SQLException,java.lang.Exception
    {
        if(!isDuplicatedID(accountID, "employee"))
        employee = new Employee(position, accountID, Fname, Lname, address, phoneNum);
    }
    /**
     * Create a customer account
     * @param DL the driver license number of the user
     * @param accountID the accountID of the account
     * @param Fname the first name of the user
     * @param Lname the last name of the user
     * @param address the address of the user
     * @param phoneNum the phone number of the user
     */
    public void createCustomer(String DL, int accountID, String Fname, String Lname, String address, String phoneNum)
            throws SQLException,java.lang.Exception
    {
        if(!isDuplicatedID(accountID, "customer"))
	account = new Customer(generateBarcode(), DL, accountID, Fname, Lname, address, phoneNum);
    }
    /**
    * Generate membership barcode
    * @return barcode
    */
    private String generateBarcode()
    {
	    String barcode = "101";
	    return barcode;
    }
    /**
     * Edit an existing account
     * @param aAccount the account
     * @param accountType the type of the account
     */
    public void editAccount(Object aAccount, String accountType)
    {
        if(accountType.equals("employee"))
	{	
		Employee employee = (Employee)aAccount;
		account = employee;
        }
	else if(accountType.equals("customer"))
        {
		Customer customer=(Customer)aAccount;
		account = customer;
	}
    }
    /**
     * Check if an account id already exists in the database
     * @param ID an account ID
     * @param userType the type of user (employee/customer)
     * @return boolean
     * @throws SQLException 
     */
    private boolean isDuplicatedID(int ID, String accountType)
	throws SQLException,ClassNotFoundException,java.lang.Exception
    {
        String table = accountType, column = accountType + "ID", query = "";
	
        return JDBC.update(query)>0;
    }

    /**
     * Set log in information
     * @param loginID the log in ID/username of the account
     * @param password the password of the account
     */
    public void setLogin(String loginID, String password)
    {
        account.setLogin(loginID, password);
    }
    
    /**
     * Set personal information with 4 attributes
     * @param Fname the first name of the user
     * @param Lname the last name of the user
     * @param address the address of the user
     * @param phoneNum the phone number of the user
     */
    public void setPersonalInfo(String Fname, String Lname, String address, String phoneNum)
    {
        account.setPersonalInfo(Fname, Lname, address, phoneNum);
    }

    /**
     * Set personal information with 5 attributes
     * @param DL the driver license id of the customer
     * @param Fname the first name of the user
     * @param Lname the last name of the user
     * @param address the address of the user
     * @param phoneNum the phone number of the user
     */
    public void setPersonalInfo(String DL, String Fname, String Lname, String address, String phoneNum)
    {
		Customer customer = (Customer)account;
		customer.setPersonalInfo(Fname, Lname, address, phoneNum);
		customer.setDL(DL);
		account = customer;
    }

    /**
     * Change the status of the account (Active/Inactive)
     */
    public void changeStatus()
    {
        account.changeStatus();
    }

    /**
     * Promote an employee to manager
     */
    public void promoteEmployee()
    {
	Employee employee = (Employee)account;
        employee.setPosition("Manager");
	account = employee;
    }

    /**
     * Demote a manager to employee
     */
    public void demoteManager()
    {
	Employee employee = (Employee)account;
        employee.setPosition("Employee");
	account = employee;
    }

    private Account account;
    private Statement statement;
    private JDBCConnection JDBC;
    private Connection connection;
}
