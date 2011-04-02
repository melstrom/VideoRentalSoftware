/*
 * Inventory / MovieRequest
 * Entity Class
 */

package Inventory;

import java.util.Date;
import Account.CustomerAccount;


/**
 * This class is a container for entity object Movie Request
 * @author alby
 */
public class MovieRequest {

    /**
     * Movies title
     */
    private String title;

    /**
     * Movie's media type
     */
    private String format;

    /**
     * Movie's actor info. Optional
     */
    private String[] actors;

    /**
     * Movie's director info. Optional
     */
    private String director;

    /**
     * This movie's releaseDate
     */
    private Date releaseDate;

    /**
     * Customer who make the reservation.
     */
    private CustomerAccount customer;


    /**
     * MovieRequest is the constructor for this class.
     * It takes a customer Account object and some movie informations to make a request.
     * @param title Movie title to be request.
     * @param format Movie's media format.
     * @param releaseDate Movie's Release Date.
     * @param requestAcc Customer Account object for customer who want this movie.
     * @pre reserveCustomer a valid CustomerAccount object.
     * @pre title size > 0 and size < .
     * @pre format size > 0.
     * @pre releaseDate a proper Date class.
     * @post Object Request created.
     */
    public MovieRequest(String title, String format, Date releaseDate, CustomerAccount requestAcc){

        this.title = title;
        this.format = format;
        this.releaseDate = releaseDate;
        customer = requestAcc;

    }

    /**
     * Accessors - Get movie title.
     * @return movie title in string.
     * @pre title not empty.
     * @post return customer.
     */
    public String getTitle(){
        return title;
    }

    /**
     * Accessors - Get media format.
     * @return movie's media format.
     * @pre format not empty.
     * @post return pickupDate.
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
    public Date getReleaseDate(){
        return releaseDate;
    }

    /**
     * Accessors - Get the customer account.
     * @return account of the customer who makes the request.
     * @pre customer not null.
     * @post return customer.
     */
    public CustomerAccount getAccount(){
        return customer;
    }

    /**
     * Accessors - (Optional) Get actors in this movie.
     * @return actors, an array contain actors.
     * @pre actor array not empty.
     * @post return array index.
     */
    public String[] getActors(){
        return actors;
    }

    /**
     * Accessors - (Optional) Get director name of this movie.
     * @return director name in string.
     * @pre director not empty.
     * @post return director.
     */
    public String getDirector(){
        return director;
    }


    /**
     * Mutator - Set movie title.
     * @param title new title to be set.
     * @pre title is not a empty string.
     * @post new title replaced.
     */
    public void setTitle(String title){
        this.title = title;
    }

    /**
     * Mutator - Set media format.
     * @param format new media format to be set.
     * @pre format a valid media format.
     * @post new media format set.
     */
    public void setformat(String format){
        this.format = format;
    }

    /**
     * Mutator - Set the release date.
     * @param date new release date to be set.
     * @pre date is a valid Date object.
     * @post new date replaced.
     */
    public void setDate(Date date){
        releaseDate = date;
    }

     /**
     * Mutator - Set the customer account.
     * @param acc account to replace existing account.
     * @pre acc is a valid CustomerAccount object.
     * @post new account replaced.
     */
    public void setAccount(CustomerAccount acc){
        customer = acc;
    }

    /**
     * Mutator - (Optional) Set actors will replace existing actor array with new one.
     * @param actors new actors array to be set.
     * @pre actors a valid array index.
     * @post new actor array set.
     */
    public void setActors(String[] actors){
        this.actors = actors;
    }

    /**
     * Mutator - (Optional) Set director will reset director name.
     * @param director new director name to be set.
     * @pre director a valid is not a empty string.
     * @post new director name set.
     */
    public void setDirector(String director){
        this.director = director;
    }

    /**
     * Create queue will create a sql queue that will add a request record.
     * @return Queue to create a request record.
     */
    public String creatQueue(){

    return "";
    }


}
