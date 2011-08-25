package inventory;
import store.*;
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

    public FullMovieInfo(final String UPC)
            throws SQLException, IOException
    {
        VRSConnection conn = VRSConnection.getInstance();

        final String[] TABLE_NAME = {" Movie_Info "};
        final String CONDITION = MovieInfo.PK_NAME + " = " + UPC;

        ResultSet result = conn.select(TABLE_NAME, null, CONDITION, null);

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

    private String[] splitActors(String allActors)
    {
        final String DELIMETER = "::";
        return allActors.split(DELIMETER);
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
    public String getGenre()
    {
        return this.genre;
    }

    public String getFormat()
    {
        return this.format;
    }

    public String getTitile()
    {
        return this.title;
    }

    public int getYear()
    {
        return this.year;
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

    public int getMSRP()
    {
        return this.msrp;
    }
}
