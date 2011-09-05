
package accounts;

import java.sql.SQLException;

/**
 * This class encapsulates all the functions for organizing customers.
 * 
 * @author Mitch
 */
public class CustomerControl 
{
    // instance field
    Customer customer;
    
    // methods
    
    /**
     * Gets the current customer
     * @return 
     */
    public Customer getCustomer()
    {
        return customer;
    }
    
    
    
    /**
     * Sets the customer to the one with the provided ID and returns it.
     * @param cuID the unique customer ID number
     * @return the Customer object
     * @throws SQLException 
     */
    public Customer getCustomer(int cuID) throws SQLException
    {
        return new ShortCustomer(cuID);
    }
    
    
    
    /**
     * Sets the customer to the existing provided customer.
     * @param cu 
     */
    public void setCustomer(Customer cu)
    {
        customer = cu;
    }
    
    
    
    /**
     * Creates a new Customer and returns the object.
     * @param name
     * @param address
     * @param postalCode
     * @param phoneNumber
     * @param position
     * @return
     * @throws SQLException 
     */
    public Customer createCustomer(String name, String address, String postalCode,
            String phoneNumber) throws SQLException
    {
        Customer cu = new ShortCustomer(name, address, postalCode, phoneNumber);
        return cu;
    }
    
    
    
    /**
     * Edits the current customer to reflect the new data.  No field may be null.
     * This only edits the contact information of the customer.
     * To edit fines and rentals you must use the appropriate methods.
     * @param name
     * @param address
     * @param postalCode
     * @param phoneNmber
     * @param position
     * @return the updated customer
     * @throws SQLException 
     */
    public Customer updateCustomer(String name, String address, String postalCode,
            String phoneNumber, int status) throws SQLException
    {
        boolean hasNull = name == null || address == null || 
                postalCode == null || phoneNumber == null;
        boolean hasBlank = name.length() < 1 || address.length() < 1 ||
                postalCode.length() < 1 || phoneNumber.length() < 1;
        boolean validStatus = status == Account.ACTIVE || status == Account.INACTIVE;
        if (hasNull || hasBlank || !validStatus)
        {
            throw new IllegalArgumentException("No blank fields allowed");
        }
        if (customer == null)
        {
            throw new NullPointerException("No customer is currently set");
        }
        
        customer.setName(name);
        customer.setAddress(address);
        customer.setPostalCode(postalCode);
        customer.setPhoneNumber(phoneNumber);
        customer.setStatus(status);
        
        return customer;
    }
    
    
    
    /**
     * Removes the amount from the customer's fines
     * @pre must provide a non negative number
     * @param cents the amount in cents to remove. Must be non-negative.
     * @throws SQLException 
     */
    public void removeFine(int cents) throws SQLException
    {
        customer.removeFine(cents);
    }
    
    
    
    /**
     * Adds the specified amount to the customer's fines.
     * @pre must be non-negative
     * @param cents The amount in cents to add. Must be non-negative.
     * @throws SQLException 
     */
    public void addFine(int cents) throws SQLException
    {
        customer.addFine(cents);
    }
    
    
    
    
}
