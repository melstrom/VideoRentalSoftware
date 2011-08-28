package pricing;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * Interface for PriceScheme.
 * File must exactly match on both client and server.
 *
 * To get a copy of the PriceScheme object use the code
 *
 * PriceScheme priceScheme = (PriceScheme) Naming.lookup("PriceScheme");
 *
 * You will need to import the following:
 * 
 * java.net.MalformedURLException;
 * java.rmi.Naming;
 * java.rmi.NotBoundException;
 * java.rmi.RemoteException;
 *
 * @author Mitch
 */
public interface PriceScheme extends Remote
{
    /**
     * Finds the price in cents of a movie with the supplied category and format
     * @param category
     * @param format
     * @return the amount of cents the movie costs to rent
     */
    public abstract int getPrice(String category, String format) throws RemoteException;

    /**
     * Finds the fine in cents per day of the movie
     * @param category
     * @param format
     * @return the amount in cents that the customer will have to pay in fines
     */
    public abstract int getFineRate(String category, String format) throws RemoteException;

    /**
     * Finds the number of days that a movie in the supplied category
     * can be rented for
     * @param category
     * @return the number of days that a movie in the supplied category can be
     * rented for
     * @throws RemoteException
     */
    public abstract int getRentalPeriod(String category) throws RemoteException;

    /**
     * Gets all possible formats.
     * @return all possible formats
     * @throws RemoteException
     */
    public abstract String[] getFormats() throws RemoteException;

    /**
     * Gets all possible categories
     * @return all possible categories
     * @throws RemoteException
     */
    public abstract String[] getCategories() throws RemoteException;

    //****************************************************************
    // WARNING
    // Do not include any of the code beyond this point in the client build.
    //*****************************************************************

    /**
     * WARNING
     * Do not include the following code in the client build.
     * 
     * Adds the price scheme to the database
     * @throws RemoteException
     */
    void add() throws RemoteException, SQLException;

    /**
     * WARNING
     * Do not include the following code in the client build.
     *
     * Sets a new price in cents for all movies that have the supplied category
     * and format
     * @param cents
     * @param category
     * @param format
     *
     */
    void setPrice(int cents, String category, String format)
            throws RemoteException, SQLException;
}
