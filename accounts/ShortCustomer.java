  package accounts;

import inventory.Copy;
import java.sql.SQLException;
import inventory.RentedCopy;
/**
 * The proxy for the Customer type
 * @author mtee00
 */
public class ShortCustomer extends Customer
{
    // instance fields

    private FullCustomer customer;
    
    // constructors
    
    /**
     * Constructs a local ShortCustomer that is not already in the database
     * After the object is constructed it can be added to the database using
     * the add() method
     * @param name
     * @param address
     * @param postalCode
     * @param phoneNumber
     */
    public ShortCustomer(String name, String address, String postalCode,
            String phoneNumber)
    {
        super(name, address, postalCode, phoneNumber);
    }



    /**
     * Constructs a ShortCustomer object from the database
     * @param customerID the ID number of the customer account
     * @throws SQLException
     */
    ShortCustomer(int customerID) throws SQLException
    {
        super(customerID);
        customer = null;
    }

    // public methods

    
    
    /**
     * The rentals that the customer has is not known to the proxy.  If this method
     * is called, it instantiates a FullCustomer and asks it what the
     * list of rentals is.
     * @return all movies that the Customer currently has rented.
     */
    public RentedCopy[] getRentals() 
    {
        initImpl();
        
        return customer.getRentals();
    }

    
    // package methods

    // Meant to act as a manual override
    // adds a new copy to the customer's renals.
    void addRental(Copy copy) throws SQLException
    {
        initImpl();
        customer.addRental(copy);
    }

    void removeRental(int rentalIndex) throws SQLException {
        initImpl();
        customer.removeRental(rentalIndex);
    }

    void removeRental(RentedCopy copy) throws SQLException {
        initImpl();
        customer.removeRental(copy);
    }
    

    // private methods

    /**
     * Initializes the implementation of Customer if it is not already initialized.
     * If it is this method does nothing.
     */
    private void initImpl()
    {
        try
        {
            if (customer == null)
            {
                customer = new FullCustomer(accountID);
            }
        }
        catch (SQLException e)
        {
            throw new ExceptionInInitializerError(e);
        }
    }
}
