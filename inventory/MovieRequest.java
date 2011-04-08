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
     * Movie's media type
     */
    private String format;
    /**
     *  The date the movie is available in store and to checkout.
     */
    private GregorianCalendar releaseDate;

    /**
     * The id of the Customer who make the reservation.
     */
    private int accountID;
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
    public MovieRequest(String SKU, String format, GregorianCalendar releaseDate, int accountID){

        this.SKU = SKU;
        this.format = format;
        this.releaseDate = releaseDate;
        this.accountID = accountID;

    }

    /**
     * Accessors - Get movie SKU.
     * @return movie SKU in string.
     * @pre SKU not empty.
     * @post return SKU.
     */
    public String getSKU(){
        return SKU;
    }

    /**
     * Accessors - Get media format (dvd / video/ blueray)
     * @return movie's media format.
     * @pre format not empty.
     * @post return format
     */
    public String getFormat(){
        return format;
    }

    /**
     * Accessors - Get release date.
     * @return release date of this movie.
     * @pre releaseDate not null.
     * @post return releaseDate.
     */
    public GregorianCalendar getReleaseDate(){
        return releaseDate;
    }

    /**
     * Accessors - Get the customer account.
     * @return account id of the customer who makes the request.
     * @post return customer id.
     */
    public int getAccountID(){
        return accountID;
    }

    /**
     * Mutator - Set media format.
     * @param format new media format to be set.
     * @pre format a valid media format.
     * @post new media format set.
     */
    public void setFormat(String format){
        this.format = format;
    }

    /**
     * Mutator - Set the release date.
     * @param date the date the movie is available in store and to pickup.
     * @pre date is not null
     * @post new date replaced.
     */
    public void setDate(GregorianCalendar date){
        releaseDate = date;
    }

     /**
     * Mutator - Set the customer account id.
     * @param accountID the account id of the customer
     */
    public void setAccountID(int accountID){
        this.accountID = accountID;
    }
}
