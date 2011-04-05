/**
 * Account Manager class
 * 
 * @author mattp
 * @version 1.0 April 3, 2011
 */
public class AccountManagement
{
    /**
     * Default constructor with no parameters
     */
    public AccountManagement()
    {
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
    {
        account = new Employee(position, accountID, Fname, Lname, address, phoneNum);
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
    {
        account = new Customer(DL, accountID, Fname, Lname, address, phoneNum);
    }
    /**
     * Edit an existing account
     * @param aAccount the account
     * @param accountType the type of the account
     */
    public void editAccount(Object aAccount, String accountType)
    {
        if(accountType.equals("employee"))
        account = (Employee)aAccount;
        else if(accountType.equals("customer"))
        account = (Customer)aAccount;
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
        account.setPersonalInfo(Fname, Lname, address, phoneNum);
        account.setDL(DL);
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
        account.setPosition("Manager");
    }
    /**
     * Demote a manager to employee
     */
    public void demoteManager()
    {
        account.setPosition("Employee");
    }

    private Account account;
}
