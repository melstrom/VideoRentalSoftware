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
 *                  producer, rating, studio, retailPriceInCents, format
 *     
 */
package inventory;
import java.sql.*;
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
     * takes 12 parameters
     * @param SKU
     * @param title
     * @param actors
     * @param director
     * @param producer
     * @param releaseDate
     * @param synopsis
     * @param genre
     * @param rating
     * @param studio
     * @param retailPriceInCents
     * @param format
     */
    
    public GeneralMovie
            (String SKU,
            String title,
            String[] actors,
            String director,
            String producer,
            GregorianCalendar releaseDate,
            String synopsis,
            String genre,
	    String rating,
            String studio,
            int retailPriceInCents,
            String format,
            int runtime)
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
        setGenre(genre);
        setFormat(format);
        this.runtime = runtime;
    }

    public GeneralMovie (String SKU, Statement statement) throws SQLException, ClassNotFoundException
    {
        ResultSet rs = statement.executeQuery("SELECT InfoID FROM physicalVideo WHERE SKU = '" + SKU + "'");
        rs.next();
        int infoID = rs.getInt(1);
        this.SKU = SKU;
        this.title = outputInfo("title", infoID);
	this.director = outputInfo("director", infoID);
        String actorsString = outputInfo("actors", infoID);
        this.actors = actorsString.split(",");
        this.synopsis = outputInfo("description", infoID);
	this.studio = outputInfo("studio", infoID);
        rs = statement.executeQuery("SELECT releaseDate FROM videoInfo WHERE InfoID = " + infoID);
        rs.next();
        Date date = rs.getDate(1);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
	this.releaseDate = calendar;
        this.producer = outputInfo("producer", infoID);
        rs = statement.executeQuery("SELECT RetailPrice FROM physicalVideo WHERE InfoID = " + infoID);
        rs.next();
        this.retailPriceInCents = rs.getInt(1);
	setRating(outputInfo("rating", infoID));
        setGenre(outputInfo("genre", infoID));
        rs = statement.executeQuery("SELECT format FROM physicalVideo WHERE InfoID = " + infoID);
        rs.next();
        setFormat(rs.getString(1));
        rs = statement.executeQuery("SELECT length FROM videoInfo WHERE InfoID = " + infoID);
        rs.next();
        this.runtime = rs.getInt(1);
    }

    public int getLength()
    {
        return runtime;
    }


    public void setLength(int runtime)
    {
        if (runtime < 1)
        {
            throw new IllegalArgumentException("Runtime must be positive");
        }
        else
        {
            this.runtime = runtime;
        }

    }

   public String getFormat()
    {
        return format;
    }

    public final void setFormat(String format)
    {
       /* String tableName = "formats";
        String query =
                jdbconnection.JDBCConnection.makeQuery(tableName, null, null);
        JDBCConnection connection = new JDBCConnection();
        try
        {
            ResultSet results = connection.getResults(query);
            while (results.next())
            {
                if (format.equalsIgnoreCase(results.getString("format")))
                {
                    this.format = format;
                    return;
                }
            }
            throw new IllegalArgumentException("Not a valid format");
        }
        finally
        {
            connection.closeConnection();
        }
        */
        this.format = format;
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
    public void setRating(String rating)
    {
        final int CANADIAN = 0;
        final int AMERICAN = 1;
        String[][] possibleRatings = {
            {"G", "PG", "14A", "18A", "R", "A", "NR"},
            {"G", "PG", "PG-13", "18A", "NC-17", "NC-17", "NR"}
        };
        // note: R is not included in the American rating array because otherwise
        // the method will overwrite the Canadian R with 18A as well.

        if (rating == null)
        {
            throw new IllegalArgumentException("Must provide a rating");
        }

        String dashlessRating = rating.replaceAll("-", "");
        dashlessRating = dashlessRating.replaceAll(" ", "");

        for (int i = 0; i < possibleRatings[CANADIAN].length; i++)
        {
            if (possibleRatings[AMERICAN][i].replaceAll("-", "").equalsIgnoreCase(dashlessRating))
            {
                dashlessRating = possibleRatings[CANADIAN][i];
            }
            if (dashlessRating.equalsIgnoreCase(possibleRatings[CANADIAN][i]))
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
    public final String getRating()
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

    public String getGenre()
    {
        return genre;
    }

    /**
     * Sets the genre of this general movie to the specified genre, assuming that
     * it is a valid genre
     * @param genre
     * @throws IllegalArgumentException if it is not a valid genre
     * @pre the passed genre must be a valid genre
     */
    public final void setGenre(String genre)
    {
//        if (genre == null)
//            throw new IllegalArgumentException("Not a valid genre");
//        for (String possibleGenre : possibleGenres)
//        {
//            if (possibleGenre.equalsIgnoreCase(genre))
//            {
                this.genre = genre;
//                return;
//            }
//        }
//        throw new IllegalArgumentException("Not a valid genre");
    }

    private String outputInfo (String info, int infoID) throws SQLException
    {
        String query = "SELECT " + info + " FROM videoInfo WHERE InfoID = " + infoID;
        ResultSet rs = statement.executeQuery(query);
        rs.next();
        return rs.getString(info);
    }

    private String title;
    private String SKU;
    private String studio;
    private String actors[];
    private String director;
    private GregorianCalendar releaseDate;
    private String synopsis;
    private int runtime;
    private ArrayList <Reservation> reservations;
    final private int ACTOR_COUNT=1;
    public static final int MIN_SKU_LENGTH = 10;
    public static final int MAX_SKU_LENGTH = 18;
    public static final int INFO_ID_LENGTH = 9;
    private String producer;
    private String rating;
    private int retailPriceInCents;
    private String genre;
    private String format;
    Connection connection;
    Statement statement;
}

//    public final String[] possibleGenres = { "science fiction", "musical", "action",
//            "drama", "comedy", "romance", "family", "horror", "suspense"
//        };