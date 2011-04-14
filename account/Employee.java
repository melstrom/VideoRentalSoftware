package account;

/**
 *	Employee Class
 *      subclass of Account class
 *
 *      Stores information of an employee account
 *      @author mattp
 		@author Peter
 *	@version 1.0 March 30, 2011
 v1.1 added getter and setter method for employeeID attribute as per Kristan's request
 
 */
public class Employee extends Account
{
    private String position;
    private int employeeID;

	
    /**
     * Default constructor of Employee
     * @param position the position of the employee (Manager/Staff)
     * @param accountID the account ID of the account
     * @param Fname the first name of the user
     * @param Lname the last name of the user
     * @param address the address of the user
     * @param phoneNum the phone number of the user
     */
    public Employee (String position, int accountID, String Fname, String Lname, Address address, String phoneNum)
    {
        //public Account(int accountID, String Fname, String Lname, Address address, String phoneNum)
        super(accountID, Fname, Lname, address,phoneNum);
        this.position = position;
    }
    
	/**
     * Constructor that also takes an employeeID as a parameter
		@param employeeID the employee's ID
     * @param position the position of the employee (Manager/Staff)
     * @param accountID the account ID of the account
     * @param Fname the first name of the user
     * @param Lname the last name of the user
     * @param address the address of the user
     * @param phoneNum the phone number of the user
     * @param employeeID the employee's id
     */
    public Employee (String position, int accountID, String Fname, String Lname, Address address, String phoneNum, int employeeID)
    {
        super(accountID, Fname, Lname, address,phoneNum);
		this.employeeID = employeeID;
        this.position = position;
    }

    /**
    	Gets the Employee's ID (employeeID).
    	@return the employee's id
    */
    public int getEmployeeID() { return employeeID; }

    /**
    	Sets the Employee's ID (employeeID).
    	@param employeeID the employee's id
    */
    public void setEmployeeID(int employeeID) { this.employeeID = employeeID; }


    
    /**
     * Get position
     * @return position the position of the employee
     */
    public String getPosition()
    {
        return position;
    }
    /**
     * Set position
     * @param position the position of the employee
     */
    public void setPosition(String position)
    {
        this.position = position;
    }
    /**
     * Check if the employee is a manager
     * @return boolean the value shows this employee is a manager or not
     */
    public boolean isManager()
    {
       return position.equals("Manager");
    }

}
