/**
 *	General Movie Class
 *
 *      	Stores general information of a movie
 *   	@author mattp
 *	@version 1.0 March 27, 2011
 * 	@version 1.1 March 29
 *		-changed the implementation of SKU
 *              	-fixed parameter naming
 *              	-fixed getActors()
	@version 1.2 April 1
		-removed addNewTitle() from constructor
		-replaced releaseDate's data type to GregorianCalendar type 
		-removed Date class (deprecated)
 */
package inventory;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.GregorianCalendar;

public class GeneralMovie
{
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
    public GeneralMovie(int SKU, String title, String actors[], String director, GregorianCalendar releaseDate, String synopsis)throws SQLException
    {
        reservations = new ArrayList<Reservation>();
        this.title = title;
        this.SKU = SKU;
        this.actors = actors;
        this.synopsis = synopsis;
        setActors(actors);
        setReleaseDate(releaseDate);
        setupConnection();
    }
   /**
    * Add a reservation to the end of reservation list
    * @param aReservation a reservation record
    * @throws SQLException
    */
    public void reservationEnqueue(Reservation aReservation)throws SQLException
    {
            reservations.add(aReservation);
            addReservation(aReservation);
    }
    /**
     * Remove the first reservation from the reservation list
     * @throws SQLException
     */
    public void reservationDequeue()throws SQLException
    {
        if(!reservations.isEmpty())
        reservations.remove(0);
        removeReservation();
    }
    /**
     * Create reservation query
     * @param aReservation a reservation record
     * @throws SQLException
     */
    final protected void addReservation(Reservation reservation) throws SQLException
    {
        String table = "Reservation";
        String query = "insert into "+table
                +"values ("+quote+title+quote+comma //movie title
                           +quote+reservation.getAccount().getAccountID()+quote+comma //account id of the customer
                            +quote+reservation.getDate()+quote+");";//the date this movie is reserved 

        executeQuery(query);
    }
    /**
     * Create remove reservation query
     * TODO: change the SQL query when we know what the table looks like
     * @throws SQLException
     */
    final protected void removeReservation() throws SQLException
    {
        String table = "Reservation";
        String query = "delete from "+table
                +"where title ="+quote+title+quote
                 +";";
        executeQuery(query);
    }
    /**
     * Send query to the database
     * @param query the sql query
     */
    final protected void executeQuery(String query)throws SQLException
    {
        try
        {
            statement.execute(query);
        }
        finally
        {
            connection.close();
        }
    }
    /**
     * Set up connection between the application and the database
     * @throws SQLException
     */
    final protected void setupConnection()throws SQLException
    {
        connection = DriverManager.getConnection(url,username,password); //url, username and password for the database is still unknown
        statement = connection.createStatement();
    }
    /**
     * Create query for adding a new movie catalog
     * @throws SQLException
     */
    final protected void addNewTitle()throws SQLException
    {
        String table="Videoinfo";
        String query = "insert into"+table
              +"values ("+quote+""+quote+comma//infoID
                        +quote+title+quote+comma
                        +quote+director+quote+comma
                        +quote+releaseDate.get(YEAR), releaseDate.get(MONTH), releaseDate.get(DATE)+quote+comma 
                        +quote+actors[0]+quote+comma
                        +quote+synopsis+quote+comma
                        +quote+SKU+quote
                        +");";

        executeQuery(query);
    }
    /**
    *   Get SKU
    *  @param SKU the SKU uniquely identifies a movie catalog 
    */
    protected int getSKU()
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
	this.releaseDate.set(releaseDate.get(YEAR), releaseDate.get(MONTH), releaseDate.get(DATE));
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
    protected void setActors(String actors[])
    {
        if(actors.length>ACTOR_COUNT)
        {
            int count = ACTOR_COUNT;
            while(actors.length > count)//if the actor array doesn't have enough space, double its size
               count*=2;
            actors = new String[count];
        }
        else
            actors = new String[ACTOR_COUNT];

        System.arraycopy(actors, 0, this.actors, 0, actors.length); //copy the actor list
    }
    /**
     * Get actors
     * @return the actor list of a movie
     */
    protected String getActors()
    {
        String actorList="";
        for(int i =0;actors[i]!=null&&i<actors.length;i++)
	{
		actorList+=actors[i];
		if(i!=actors.length-1)
		actorList+=" ,";
	}
      
        return actorList;
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
    private String title;
    private int SKU;
    private String actors[];
    private String director;
    private GregorianCalendar releaseDate;
    private String synopsis;
    private ArrayList reservations;
    private Connection connection;
    private Statement statement;
    final private int ACTOR_COUNT=1;
    final char quote ='\'';
    final char comma =',';
}