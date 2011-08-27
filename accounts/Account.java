package accounts;

import java.sql.SQLException;

/**
 * This abstract class is for all account types and holds contact information.
 * @author Mitch
 */
public abstract class Account
{
    // instance variables
    protected int accountID;
    protected String name;
    protected String address;
    protected String postalCode;
    protected String phoneNumber;
    protected int status;

    // constants
    public static final int ACTIVE = 1;
    public static final int INACTIVE = 0;

    // constructor
    public Account()
    {
        accountID = -1;
        status = ACTIVE;
    }


    public Account(String name, String address, String postalCode, String phoneNumber)
    {
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        status = ACTIVE;
    }

    // public methods

    /**
     * @return the accountID
     */
    public int getAccountID() {
        return accountID;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return the postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }



    // package methods

    /**
     * Adds the account to the database, provided it does not already exist
     * @return the new account ID number
     * @throws SQLException
     */
    abstract int add() throws SQLException;


    /**
     * @param name the name to set
     */
    abstract void setName(String name) throws SQLException;

    /**
     * @param address the address to set
     */
    abstract void setAddress(String address) throws SQLException;

    /**
     * @param postalCode the postalCode to set
     */
    abstract void setPostalCode(String postalCode) throws SQLException;

    /**
     * @param phoneNumber the phoneNumber to set
     */
    abstract void setPhoneNumber(String phoneNumber) throws SQLException;

    /**
     * @param status the status to set
     */
    abstract void setStatus(int status) throws SQLException;
    
    
}
