/*
 * Inventory / MovieRequest
 * Entity Class
 */
//--matt: changed Date to Gregorian Calendar
//--fixed object naming
//--accountID and SKU as constructor parameters
//--removed extra methods (get and sets for title, director and actors) 
//--removed sql stuff
package inventory;

import java.util.GregorianCalendar;

/**
 * This class is a container for entity object Movie Request
 * @author alby
 */
public class MovieRequest {

    private String SKU;
    /**
     *  The date the movie is available in store and to checkout.
     */
    private GregorianCalendar requestDate;
    /**
     * The id of the Customer who make the reservation.
     */
    private int customerID;
    /**
     * MovieRequest is the constructor for this class.
     * It takes a customer Account object and some movie informations to make a request.
     * @param SKU the SKU number of the movie
     * @param format Movie's media format.
     * @param releaseDate the date the movie is available in store and to checkout.
     * @param accountID Customer Account id for customer who want this movie.
     * @pre format size > 0.
     * @pre releaseDate is not null
     * @post Object Request created.
     */
    public MovieRequest(String SKU, int customerID, GregorianCalendar requestDate)
    {
        this.SKU = SKU;
        this.customerID = customerID;
        this.requestDate = requestDate;
    }

    /**
     * Accessors - Get movie SKU.
     * @return movie SKU in string.
     * @pre SKU not empty.
     * @post return SKU.
     */
    public String getSKU()
    {
        return SKU;
    }

    /**
     * Accessors - Get release date.
     * @return release date of this movie.
     * @pre releaseDate not null.
     * @post return releaseDate.
     */
    public GregorianCalendar getRequestDate()
    {
        return requestDate;
    }

    /**
     * Accessors - Get the customer account.
     * @return account id of the customer who makes the request.
     * @post return customer id.
     */
    public int getCustomerID()
    {
        return customerID;
    }
}
