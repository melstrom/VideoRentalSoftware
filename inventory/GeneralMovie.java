/**
 *	General Movie Class
 *
 *      	Stores general information of a movie
 *   	@author mattp
 *	@version 1.0 March 27, 2011
 * 	@version 1.1 March 29
 *		-changed the implementation of SKU
 *              -fixed parameter naming
 *              -fixed getActors()
	@version 1.2 April 1
		-removed addNewTitle() from constructor
		-replaced releaseDate's data type to GregorianCalendar type
		-removed Date class (deprecated)
	@version 1.2.5 April 4
		-changed SKU to type String
		-changed actors(type array) to actors(type String)
		-added empty constructor
	@version 1.3
		-moved all database/query stuff out
 *
 *      @version 1.4
 *              - added in missing attributes:
 *                  producer, rating, studio, retailPriceInCents
 */
package inventory;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class GeneralMovie
{
    /**
	Empty Constructor
     */
    public GeneralMovie()
    {
    }



    /**
     * Constructor of GeneralMovie
     * takes 10 parameters
     * @param SKU
     * @param title
     * @param actors
     * @param director
     * @param producer
     * @param releaseDate
     * @param synopsis
     * @param rating
     * @param studio
     * @param retailPriceInCents
     */
    
    public GeneralMovie
            (String SKU,
            String title,
            String[] actors,
            String director,
            String producer,
            GregorianCalendar releaseDate,
            String synopsis,
	    String rating,
            String studio,
            int retailPriceInCents)
    {
        reservations = new ArrayList<Reservation>();
        this.title = title;
        this.SKU = SKU;
	this.director = director;
        this.actors = actors;
        this.synopsis = synopsis;
	this.studio = studio;
	this.releaseDate = releaseDate;
        this.producer = producer;
        this.retailPriceInCents = retailPriceInCents;
	setRating(rating);
    }

	

    /**
     * Sets the rating of this GeneralMovie object to the specified rating.
     * The rating must correspond to either British Columbian classification
     * system, or the Motion Picture Association of America film rating.
     * American film ratings will automatically be converted to the
     * British Columbian equivalent.
     *
     * The ratings are as follows:


        General (G)
        All ages.
        The contents of these motion pictures are suitable for viewing by all ages.

     * Parental Guidance (PG)
        All ages.
        Parental guidance advised. Theme or content may not be suitable for all children.

        14A
        Anyone under 14 years of age must be accompanied by an adult.
        Parents cautioned. These films may contain violence, coarse language, and/or sexually suggestive scenes.

        18A
        Anyone under 18 years of age must be accompanied by an adult.
        Parents strongly cautioned. Will likely contain explicit violence, frequent coarse language, sexual activity and/or horror.

        Restricted (R)
        No one under the age of 18 may view under any circumstances.
        Content not suitable for minors. May contain scenes of explicit sex and/or violence. However, the film classification office considers these films to have some artistic, historical, political, educational or scientific merit.

     * Adult (A)
        No one under the age of 18 may view under any circumstances.
        May contain explicit sexual scenes and/or violence. However, the film classification office considers these films to be tolerable to the community.
     *
     * source: http://www.media-awareness.ca/english/resources/ratings_classification_systems/film_classification/bc_film_classification.cfm
     *
     * No Rating (NR)
     *
     * @param rating the rating of the movie. Dashes and capitalization is ignored.
     * @throws IllegalArgumentException if the provided rating is not one of the
     * given
     */
    public final void setRating(String rating)
    {
        final int CANADIAN = 0;
        final int AMERICAN = 1;
        String[][] possibleRatings = {
            {"G", "PG", "14A", "18A", "R", "A", "NR"},
            {"G", "PG", "PG-13", "R", "NC-17", "NC-17", "NR"}
        };

        if (rating == null)
        {
            throw new IllegalArgumentException("Must provide a rating");
        }

        String dashlessRating = rating.replaceAll("-", "");
        dashlessRating = dashlessRating.replaceAll(" ", "");

        for (String[] possibleRating : possibleRatings)
        {
            if (possibleRating[AMERICAN].replaceAll("-", "").equalsIgnoreCase(dashlessRating))
            {
                dashlessRating = possibleRating[CANADIAN];
            }
            if (dashlessRating.equalsIgnoreCase(possibleRating[CANADIAN]))
            {
                this.rating = dashlessRating;
                return;
            }
        }

        throw new IllegalArgumentException("Not a valid rating");
    }
    
    /**
    * Get rating
    *@return rating 
    */
    public String getRating()
    {
	    return rating;
    }




   /**
    * Add a reservation to the end of reservation list
    * @param aReservation a reservation record
    */
    public void reservationEnqueue(Reservation aReservation)
    {
	    reservations.add(aReservation);
    }
    /**
     * Remove the first reservation from the reservation list
     */
    public void reservationDequeue()
    {
        if(!reservations.isEmpty())
        reservations.remove(0);
    }
    /**
     * Create reservation query
     * @param aReservation a reservation record
     * @throws SQLException
     */
  /*  final protected void addReservation(Reservation reservation) throws SQLException
    {
        String table = "Reservation";
        String query = "insert into "+table
                +"values ("+quote+title+quote+comma //movie title
                           +quote+reservation.getAccountID()+quote+comma //account id of the customer
                            +quote+reservation.getDate()+quote+");";//the date this movie is reserved

       //executeQuery(query);
    }*/
    /**
     * Create remove reservation query
     * @throws SQLException
     */
    /*
    final protected void removeReservation() throws SQLException
    {
        String table = "Reservation";
        String query = "delete from "+table
                +"where title ="+quote+title+quote
                 +";";
       // executeQuery(query);
    }*/


    /**
     * Create query for adding a new movie catalog
     * @throws SQLException
     */
    /*final void addNewTitle()throws java.sql.SQLException
    {
        String table="Videoinfo";
        String query = "insert into"+table
              +"values ("+quote+""+quote+comma//infoID
                        +quote+title+quote+comma
                        +quote+director+quote+comma
                        +quote+releaseDate.get(releaseDate.YEAR)+releaseDate.get(releaseDate.MONTH)+ releaseDate.get(releaseDate.DATE)+quote+comma
                        // that's not how you use the Calendar
                        +quote+actors+quote+comma
                        +quote+synopsis+quote+comma
                        +quote+SKU+quote
                        +");";

        executeQuery(query);
    }*/
    /**
    *	Set retail price in cents
    * @param price the retailPriceInCents
    */
    protected void setRetailPriceInCents(int price)
    {
	    retailPriceInCents = price;
    }
    /**
    * 	Get retailPriceInCents
    * @return the retailPriceInCents
    */
    public int getRetailPriceInCents()
    {
	    return retailPriceInCents;
    }
    
    /**
    *	Set SKU
    * @param SKU the SKU uniquely identifies a movie catalog
    */
    protected void setSKU(String SKU)
    {
	this.SKU = SKU;
    }
    /**
    *   Get SKU
    *  @return SKU - the SKU uniquely identifies a movie catalog
    */
    public String getSKU()
    {
        return SKU;
    }
    /**
     * Set title
     * @param title the title of a movie
     */
    protected void setTitle(String title)
    {
        this.title=title;
    }
    /**
     * Get movie title
     * @return title the title of a movie
     */
    public String getTitle()
    {
        return title;
    }
    /**
     * Set director
     * @param director the director of a movie
     */
    protected void setDirector(String director)
    {
        this.director = director;
    }
    /**
     * Get director
     * @return director the director of a movie
     */
    public String getDirector()
    {
        return director;
    }
    /**
    *	Set producer
    * @param producer the producer of the movie
    */
    protected void setProducer(String producer)
    {
	    this.producer = producer;
    }
    /**
    *	
    */
    public String getProducer()
    {
	    return producer;
    }
    /**
     * Set release date
     * @param releaseDate the release date of a movie
     */
    protected void setReleaseDate(GregorianCalendar releaseDate)
    {
	this.releaseDate = releaseDate;
    }
    /**
     * Get Release date
     * @return the release date of a movie
     */
    public GregorianCalendar getReleaseDate()
    {
        return releaseDate;
    }
    /**
     * Set actors
     * @param actors the actor list of a movie
     */
    protected void setActors(String []actors)
    {
	this.actors = actors;
    }
    /**
     * Get actors
     * @return the actor list of a movie
     */
    public String []getActors()
    {
	return actors;
    }
    /**
    * Set studio
    *@param studio the studio name
    */
    protected void setStudio(String studio)
    {
	    this.studio = studio;
    }
    /**
    *Get studio
    *@return studio the studio name
    */
    public String getStudio()
    {
	    return studio;
    }
    /**
     * Set synopsis/description
     * @param synopsis the synopsis of a movie
     */
    protected void setSynopsis(String synopsis)
    {
        this.synopsis = synopsis;
    }
    /**
     * Get synopsis/description
     * @return synopsis the synopsis of a movie
     */
    public String getSynopsis()
    {
        return synopsis;
    }
    /**
    *  Get reservation list
    *  @return reservations - reservation arraylist
    */
    public ArrayList getReservations()
    {
	    return reservations;
    }
    /**
    * testing class
    
    public String getAll()
    {	    			
	    return getSKU()+" "+getTitle()+" "+getDirector()
			+" "+getReleaseDate().get(releaseDate.YEAR)+" "+getSynopsis();
    }*/

    private String title;
    private String SKU;
    private String studio;
    private String actors[];
    private String director;
    private GregorianCalendar releaseDate;
    private String synopsis;
    private ArrayList <Reservation> reservations;
    final private int ACTOR_COUNT=1;
    private static final int MAX_SKU_LENGTH = 25;
    private String producer;
    private String rating;
    private int retailPriceInCents;
}
