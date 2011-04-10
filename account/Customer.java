package account;

/**
 *	Customer Class
 *      subclass of account class
 *
 *      Stores information of a customer account
 *      @author mattp
 *	@version 1.0 March 30, 2011
 *
 */
public class Customer extends Account
{
    /**
     * Default constructor of Customer
     * @param DL the driver license number of the user
     * @param accountID the accountID of the account
     * @param Fname the first name of the user
     * @param Lname the last name of the user
     * @param address the address of the user
     * @param phoneNum the phone number of the user
     */
    public Customer (String DL, int accountID, String Fname, String Lname, String address, String phoneNum)
    {
        super(accountID, Fname, Lname, address,phoneNum);
        this.DL = DL;
    }
    /**
     * Set driver license number
     * @param DL the driver license number of the user
     */
    public void setDL(String DL)
    {
        this.DL=DL;
    }
    /**
     * Get driver license number
     * @return DL the driver license number of the user
     */
    public String getDL()
    {
        return DL;
    }
    
    private String DL;
}