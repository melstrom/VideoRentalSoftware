/**
 *	Account Class
 *
 *      Stores basic information of an account
 *      @author mattp
 *	@version 1.0 March 30, 2011
 */
public class Account
{
    /**
     * Account default constructor
     * @param accountID the account ID of the user
     * @param Fname the first name of the user
     * @param Lname the last name of the user
     * @param address the address of the user
     * @param phoneNum the phone number of the user
     */
    public Account(int accountID, String Fname, String Lname, String address, String phoneNum)
    {
        this.accountID = accountID;
        this.Fname = Fname;
        this.Lname = Lname;
        this.address = address;
        this.phoneNum = phoneNum;
        status = "Active";
    }
    /**
     * Set log in information
     * @param loginID the log in ID/username of the account
     * @param password the password of the account
     */
    public void setLogin(String loginID, String password)
    {
        setPassword(password);
        setLoginID(loginID);
    }
    /**
     * Set personal information 
     * @param Fname the first name of the user
     * @param Lname the last name of the user
     * @param address the address of the user
     * @param phoneNum the phone number of the user
     */
    public void setPersonalInfo(String Fname, String Lname, String address, String phoneNum)
    {
        setFname(Fname);
        setLname(Lname);
        setAddress(address);
        setPhoneNum(phoneNum);
    }
    /**
     * Set status of the account 
     * @param status the status of the account(Active/Inactive)
     */
    public void setStatus(String status)
    {
        this.status = status;
    }
    /**
     * Set password
     * @param password the password of the account
     */
    protected void setPassword(String password)
    {
        this.password = password;
    }
    /**
     * Set login ID
     * @param loginID the log in ID/username of the account
     */
    protected void setLoginID(String loginID)
    {
        this.loginID = loginID;
    }
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
    protected void setAddress(String address)
    {
        this.address = address;
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
    public String getAddress()
    {
        return address;
    }
    /**
     * Get password
     * @return password the password of the account
     */
    public String getPassword()
    {
        return password;
    }
    /**
     * Get status
     * @return status the status of the account (Active/Inactive)
     */
    public String getStatus()
    {
        return status;
    }
    private int accountID;
    private String loginID;
    private String Fname;
    private String Lname;
    private String phoneNum;
    private String address;
    private String password;
    private String status;
}