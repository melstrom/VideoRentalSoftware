/**
 *	General Movie Class
 *
 *      Stores general information of a movie
 *      @author mattp
 *	@version 1.0 March 27, 2011
 */
import java.util.Date;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GeneralMovie
{
    /**
     *  General movie constructor
     *  takes 5 default attributes
     * @param title1 the title of a movie
     * @param actors1 the actors of a movie
     * @param director1 the director of a movie
     * @param releaseDate1  the release date of a movie
     * @param synopsis1 the brief description of a movie
     */
    public GeneralMovie(String title1, String actors1[], String director1, Date releaseDate1, String synopsis1)throws SQLException
    {
        titleCounter = 1;
        reservations = new ArrayList<Reservation>();
        title = title1;
        actors = actors1;
        synopsis = synopsis1;
        setActors(actors1);
        setReleaseDate(releaseDate1);
        setupConnection();
        addNewTitle();
    }
   /**
    * Add a reservation to the end of reservation list
    * @param aReservation a reservation record
    * @throws SQLException
    */
    public void reservationEnqueue(Reservation aReservation)throws SQLException
    {
            reservations.add(aReservation);
            addReservation();
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
     * @throws SQLException
     */
    final protected void addReservation() throws SQLException
    {
        String table = "Reservation";
        String query = "insert into "+table
                +"values ("+quote+title+quote+comma //movie title
                           +quote+reservation.get(reservations.size())getCustomer().getAccountID()+quote+comma //account id of the customer
                           +quote+reservation.get(reservations.size()).getDate()+quote //the date this movie is reserved
                           +");";

        executeQuery(query);
    }
    /**
     * Create remove reservation query
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
        String table="Video";
        String query = "insert into"+table
              +"values ("+quote+""+quote+comma
                        +quote+title+quote+comma
                        +quote+director+quote+comma
                        +quote+releaseDate.getYear()+releaseDate.getMonth()+releaseDate.getDate()+quote+comma 
                        +quote+actors[0]+quote+comma
                        +quote+synopsis+quote+comma
                        +quote+titleCounter+quote //sku 
                        +");";

        executeQuery(query);
        titleCounter++; //auto increment sku number for each new catalog
    }
    /**
     * Set title
     * @param title1 the title of a movie
     */
    protected void setTitle(String title1)
    {
        title=title1;
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
     * @param director1 the director of a movie
     */
    protected void setDirector(String director1)
    {
        director = director1;
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
     * @param releaseDate1 the release date of a movie
     */
    protected void setReleaseDate(Date releaseDate1)
    {
        releaseDate.setYear(releaseDate1.getYear());
        releaseDate.setMonth(releaseDate1.getMonth());
        releaseDate.setDate(releaseDate1.getDate());
    }
    /**
     * Get Release date
     * @return the release date of a movie
     */
    protected String getReleaseDate()
    {
        return ""+releaseDate.getYear()+releaseDate.getMonth()+releaseDate.getDate();
    }
    /**
     * Set actors
     * @param actors1 the actor list of a movie
     */
    protected void setActors(String actors1[])
    {
        if(actors1.length>ACTOR_COUNT)
        {
            int count = ACTOR_COUNT;
            while(actors1.length > count)//if the actor array doesn't have enough space, double its size
               count*=2;
            actors = new String[count];
        }
        else
            actors = new String[ACTOR_COUNT];

        System.arraycopy(actors1, 0, actors, 0, actors1.length); //copy the actor list 
    }
    /**
     * Get actors
     * @return the actor list of a movie
     */
    protected String getActors()
    {
        String actorList="";
        for(int i =0;i<actors.length;i++)
        actorList+=actors[i];
      
        return actorList;
    }
    /**
     * Set synopsis/description 
     * @param synopsis1 the synopsis of a movie
     */
    protected void setSynopsis(String synopsis1)
    {
        synopsis = synopsis1;
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
    private String actors[];
    private String director;
    private Date releaseDate;
    private String synopsis;
    private ArrayList reservations;
    private int titleCounter;
    private Connection connection;
    private Statement statement;
    final private int ACTOR_COUNT=1;
    final char quote ='\'';
    final char comma =',';
}