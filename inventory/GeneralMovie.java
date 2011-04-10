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
     *  General movie constructor
     *  takes 6 default attributes
     * @param SKU the number uniquely identifies each movie catalog
     * @param title the title of a movie
     * @param actors the actors of a movie
     * @param director the director of a movie
     * @param releaseDate  the release date of a movie 
     * @param synopsis the brief description of a movie
     */
    public GeneralMovie(String SKU, String title, String actors, String director, GregorianCalendar releaseDate, String synopsis)
    {
        reservations = new ArrayList<Reservation>();
        // need to populate this arraylist of reservations
	this.releaseDate = new GregorianCalendar();
        this.title = title;
        this.SKU = SKU;
	this.director = director;
        this.actors = actors;
        this.synopsis = synopsis;
	this.releaseDate = releaseDate;

        // missing some attributes that are in the database like genre and rating and retail price
        // TODO: update when the database is online
        // is actors really just a string, or is it an array of strings?
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
   /* final protected void addNewTitle()throws SQLException
    {
        String table="Videoinfo";
        String query = "insert into"+table
              +"values ("+quote+""+quote+comma//infoID
                        +quote+title+quote+comma
                        +quote+director+quote+comma
                        +quote+releaseDate.get(releaseDate.YEAR)+releaseDate.get(releaseDate.MONTH)+ releaseDate.get(releaseDate.DATE)+quote+comma
                        +quote+actors+quote+comma
                        +quote+synopsis+quote+comma
                        +quote+SKU+quote
                        +");";

        executeQuery(query);
    }*/
    /**
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
    protected String getSKU()
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
    protected String getTitle()
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
    protected String getDirector()
    {
        return director;
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
    protected GregorianCalendar getReleaseDate()
    {
        return releaseDate;
    }
    /**
     * Set actors
     * @param actors the actor list of a movie
     */
    protected void setActors(String actors)
    {
	this.actors = actors;
    }
    /**
     * Get actors
     * @return the actor list of a movie
     */
    protected String getActors()
    {
	return actors;
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
    protected String getSynopsis()
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
    */
    public String getAll()
    {
	    return getSKU()+" "+getTitle()+" "+getActors()+" "+getDirector()
			+" "+getReleaseDate().get(releaseDate.YEAR)+" "+getSynopsis();
    }
    
    private String title;
    private String SKU;
    private String actors;
    private String director;
    private GregorianCalendar releaseDate;
    private String synopsis;
    private ArrayList <Reservation> reservations;
    final private int ACTOR_COUNT=1;
    private static final int MAX_SKU_LENGTH = 25;
}