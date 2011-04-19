package account;

/**
 *	Account Class
 *
 *      Stores basic information of an account
 *      @author mattp
 *      @author Peter
 *	@version 1.0 March 30, 2011
 *      changed it to be an abstract class: Mitch
	@version 1.1 April 2
		-renamed setStatus() to changeStatus()
		-changed status type to boolean
 *
 *      9 April: Mitch
 *          - made the constant with the length of an account ID as 9 to prevent
 *              integer overflow (2^32 - 1 = ~2e9, so ID numbers of 1e8 - 9e8
 *              are safe)
 *      - commented out methods and attributes to do with status
		
 */
public abstract class Account
{
    private int accountID;
    private String Fname;
    private String Lname;
    protected String phoneNum;
    private Address address;
//    private boolean status;

    public static final int ID_LENGTH = 9;

    /**
     * Account default constructor
     * @param accountID the account ID of the user
     * @param Fname the first name of the user
     * @param Lname the last name of the user
     * @param address the address of the user
     * @param phoneNum the phone number of the user
     */
    public Account(int accountID, String Fname, String Lname, Address address, String phoneNum)
    {
        this.accountID = accountID;
        this.Fname = Fname;
        this.Lname = Lname;
        this.address = address;
        this.phoneNum = phoneNum;
 //       status = true;
    }
    /*
     * Set log in information
     * @param loginID the log in ID/username of the account
     * @param password the password of the account
    public void setLogin(String loginID, String password)
    {
        setPassword(password);
        setLoginID(loginID);
    }
    */
    /**
     * Set personal information 
     * @param Fname the first name of the user
     * @param Lname the last name of the user
     * @param address the address of the user
     * @param phoneNum the phone number of the user
     */
    public void setPersonalInfo(String Fname, String Lname, Address address, String phoneNum)
    {
        setFname(Fname);
        setLname(Lname);
        setAddress(address);
        setPhoneNum(phoneNum);
    }
    /*
     * Set status of the account 
     * @param status the status of the account(Active/Inactive)
    public void changeStatus()
    {
        status = !status;
    }
    
    /**
     * Set password
     * @param password the password of the account
   
    protected void setPassword(String password)
    {
        this.password = password;
    }
     * Set login ID
     * @param loginID the log in ID/username of the account
    protected void setLoginID(String loginID)
    {
        this.loginID = loginID;
    }
    */
    /**
     * Set first name
     * @param Fname the first name of the user
     */
    protected void setFname(String Fname)
    {
        this.Fname = Fname;
    }
    /**
     * Set last name
     * @param Lname the last name of the user
     */
    protected void setLname(String Lname)
    {
        this.Lname = Lname;
    }
    /**
     * Set address
     * @param address the address of the user
     */
    protected void setAddress(Address address)
    {
        this.address = address;
    }
    /**
     * Method to get the phone number
     * return the phone number as a String
     */
    public String getPhoneNum()
    {
	return phoneNum;
    }

    /**
     * Set phone number
     * @param phoneNum the phone number of the user
     */
    protected void setPhoneNum(String phoneNum)
    {
        this.phoneNum = phoneNum;
    }
    /**
     * Get account ID
     * @return phoneNum the account ID of the account
     */
    public int getAccountID()
    {
        return accountID;
    }
    /**
     * Get first name
     * @return Fname the first name of the user
     */
    public String getFname()
    {
        return Fname;
    }
    /**
     * Get last name
     * @return Lname the last name of the user
     */
    public String getLname()
    {
        return Lname;
    }
    /**
     * Get address
     * @return address the address of the user
     */
    public Address getAddress()
    {
        return address;
    }
    /*
     * Get status
     * @return status the status of the account (True = Active/False = Inactive)
    public boolean getStatus()
    {
        return status;
    }
    */
}
