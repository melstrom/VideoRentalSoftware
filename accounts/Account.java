package accounts;

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
    protected Status status;

    // constructor
    public Account(String name, String address, String postalCode, String phoneNumber,
            Status status)
    {
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.status = status;
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
    public Status getStatus() {
        return status;
    }



    // package methods


    /**
     * @param name the name to set
     */
    abstract void setName(String name);

    /**
     * @param address the address to set
     */
    abstract void setAddress(String address);

    /**
     * @param postalCode the postalCode to set
     */
    abstract void setPostalCode(String postalCode);

    /**
     * @param phoneNumber the phoneNumber to set
     */
    abstract void setPhoneNumber(String phoneNumber);

    /**
     * @param status the status to set
     */
    abstract void setStatus(Status status);
    
    
}
