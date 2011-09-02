package inventory;
import store.VRSConnection;
import java.sql.*;
import java.io.*;

/**
 *
 * @author kevin
 */
public class FullMovieInfo extends MovieInfo
{
    private String format;
    private String title;
    private int year;
    private String[] actors;
    private String director;
    private String studio;
    private String distro;
    private int msrp;
    private int availability;
    private int totalCopies;
    private String rating;
    private String genre;

    public FullMovieInfo(final int UPC)
            throws SQLException, IOException
    {
        VRSConnection conn = VRSConnection.getInstance();

        final String[] TABLE_NAMES = {MovieInfo.TABLE_NAME};
        final String CONDITION = MovieInfo.PK_NAME + " = " + UPC;

        ResultSet result = conn.select(TABLE_NAMES, null, CONDITION, null);

        if(!result.next())//Although this will not happen, examining it is still
                          //a good pratice
            throw new IOException("Row with PK not found.");

        super.setUPC(UPC);
        this.title = result.getString(MovieInfo.TITLE_INDEX);
        this.actors = splitActors(result.getString(MovieInfo.ACTORS_INDEX));
        this.director = result.getString(MovieInfo.DIRECTOR_INDEX);
        this.studio = result.getString(MovieInfo.STUDIO_INDEX);
        this.distro = result.getString(MovieInfo.DISTRO_INDEX);
        this.rating = result.getString(MovieInfo.RATING_INDEX);
        this.genre = result.getString(MovieInfo.GENRE_INDEX);
        this.msrp = result.getInt(MovieInfo.MSRP_INDEX);
        this.title = result.getString(MovieInfo.TITLE_INDEX);
        this.year = result.getInt(MovieInfo.YEAR_INDEX);
        this.format = result.getString(MovieInfo.FORMAT_INDEX);
        this.availability = result.getInt(MovieInfo.AVAILABILITY_INDEX);
        this.totalCopies = result.getInt(MovieInfo.TOTAL_COPY_INDEX);
    }

    public FullMovieInfo(String[] movieRawInfo)
    {
        super.setUPC(Integer.parseInt(movieRawInfo[MovieInfo.UPC_INDEX - 1]));
        this.format = movieRawInfo[MovieInfo.FORMAT_INDEX -1];
        this.title = movieRawInfo[MovieInfo.TITLE_INDEX -1];
        this.year = Integer.parseInt(movieRawInfo[MovieInfo.YEAR_INDEX -1]);
        this.actors = splitActors(movieRawInfo[MovieInfo.ACTORS_INDEX -1]);
        this.director = movieRawInfo[MovieInfo.DIRECTOR_INDEX - 1];
        this.studio = movieRawInfo[MovieInfo.STUDIO_INDEX - 1];
        this.distro = movieRawInfo[MovieInfo.DISTRO_INDEX - 1];
        this.msrp = Integer.parseInt(movieRawInfo[MovieInfo.MSRP_INDEX - 1]);
        this.availability = 0;
        this.totalCopies = 0;
        this.rating = movieRawInfo[MovieInfo.RATING_INDEX - 1];
        this.genre = movieRawInfo[MovieInfo.GENRE_INDEX - 1];
    }

    public void record()
            throws SQLException
    {
        VRSConnection conn = VRSConnection.getInstance();

        final String[] COLUMNS = {
                                    MovieInfo.PK_NAME,
                                    " format_ ",
                                    " title_ ",
                                    " year_ ",
                                    " actors_ ",
                                    " director_ ",
                                    " studtio_ ",
                                    " distro_ ",
                                    " MSRP_ ",
                                    " availability_ ",
                                    " total_Copies ",
                                    " rating_ ",
                                    " genre_ "
                                 };
        final String[] VALUES = {
                                    "" + super.getUPC(),
                                    this.format,
                                    this.title,
                                    "" + this.year,
                                    mergeActors(this.actors),
                                    this.director,
                                    this.studio,
                                    this.distro,
                                    "" + this.msrp,
                                    "" + this.availability,
                                    "" + this.totalCopies,
                                    this.rating,
                                    this.genre
                                };
        int rowInsert = conn.insert(MovieInfo.TABLE_NAME, COLUMNS, VALUES,
                                    null);
    }

    private String[] splitActors(String allActors)
    {
        return allActors.split(MovieInfo.ACTORS_SEPERATOR);
    }

    private String mergeActors(String[] allActors)
    {
        String longActors = "";
        for(int row = 0; row < allActors.length; row++)
            longActors += allActors[row] + MovieInfo.ACTORS_SEPERATOR;
        return longActors;
    }

    public String getFormat()
    {
        return this.format;
    }

    public String getTitle()
    {
        return this.title;
    }

    public int getYear()
    {
        return this.year;
    }

    public String[] getActors()
    {
        return this.actors;
    }

    public String getDirector()
    {
        return this.director;
    }

    public String getStudio()
    {
        return this.studio;
    }

    public String getDistro()
    {
        return this.distro;
    }
    
    public int getMSRP()
    {
        return this.msrp;
    }

    public int getAvailability()
    {
        return this.availability;
    }

    public int getTotalCopies()
    {
        return this.totalCopies;
    }

    public String getRating()
    {
        return this.rating;
    }
    
    public String getGenre()
    {
        return this.genre;
    }
}