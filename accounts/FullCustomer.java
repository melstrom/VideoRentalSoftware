package accounts;

import inventory.Copy;
import java.sql.SQLException;
import inventory.RentedCopy;
import java.sql.ResultSet;
import store.VRSConnection;

/**
 * The Implementation of Customer
 * Implementation is on hold until RentedCopy is finished.
 * @author mtee00
 */
class FullCustomer extends Customer
{
    // instance fields
    RentedCopy[] rentals;

    // constants
    private static int DEFAULT_RENTALS_SIZE = 10;

    // constructors

    FullCustomer(int customerID) throws SQLException
    {
        super(customerID);
        String[] tables = { "Customers_", "Rented_Out_Copies" };
        String[] columns = { "Rented_Out_Copies.SKU_" };
        String condition = "Customers_.customer_ID = " + accountID +
                " AND Customers_.customer_ID = Rented_Out_Copies.customer_ID ";
        VRSConnection conn = VRSConnection.getInstance();
        ResultSet rs = conn.select(tables, columns, condition, null);
        if (rs.next())
        {
            rentals = new RentedCopy[DEFAULT_RENTALS_SIZE];
            int i = 0;
            do
            {
                if (rentals.length == i)
                {
                    increaseRentalsSize();
                }
                int SKU = rs.getInt(1);
                rentals[i] = new RentedCopy(SKU);
                i++;
                
            } while (rs.next());
        }
        else
        {
            rentals = null;
        }
    }

    @Override
    /**
     * Returns all the movies that the customer currently has rented out.
     * Returns null if there is nothing rented out.
     */
    public RentedCopy[] getRentals() {
        return rentals;
    }

    
    
    /**
     * Adds a rental movie to the Customer's account
     * Meant to act as a manual override.
     * @param copy
     * @throws SQLException
     */
    @Override
    void addRental(Copy copy) throws SQLException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    void removeRental(RentedCopy copy) {
        throw new UnsupportedOperationException("Not supported yet.");
    }



    @Override
    void removeRental(int rentalIndex) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // private methods

    /**
     * Doubles the size of the rentals array.
     * @return the new size.
     */
    private int increaseRentalsSize()
    {
        int newSize = rentals.length << 1;
        RentedCopy[] newRentals = new RentedCopy[newSize];
        System.arraycopy(rentals, 0, newRentals, 0, rentals.length);
        rentals = newRentals;
        return newSize;
    }

}
