package inventory;
import java.sql.*;
import java.io.*;

/**
 *
 * @author kevin
 */
public class ShortMovieInfo extends MovieInfo
{
    private String title;
    private int year;
    private String format;
    private int availability;
    private int totalCopies;
    private FullMovieInfo fullInfo;

    /**
     *
     * !!! TO THIS CONSTRUCTOR USER: It is your job to move the pointer of the
     * ResultSet to the next position.
     * @pre the ResultSet must not be empty
     * @pre the pointer of the ResultSet is not pointing the end position
     * @param result
     *
     */
    public ShortMovieInfo(ResultSet result)
            throws SQLException, IOException
    {
        if(!result.next())//the next() method has a side-effect! but if it
                          //return false, the side-effect would be ok.
            throw new IOException("This is the last row in your result.\nPlease"
                                  + " contact technical support.");

        //moving the pointer of the ResultSet back to what it was
        result.last();

        super.setUPC(result.getInt(MovieInfo.UPC_INDEX));
        this.title = result.getString(MovieInfo.TITLE_INDEX);
        this.year = result.getInt(MovieInfo.YEAR_INDEX);
        this.format = result.getString(MovieInfo.FORMAT_INDEX);
        this.availability = result.getInt(MovieInfo.AVAILABILITY_INDEX);
        this.totalCopies = result.getInt(MovieInfo.TOTAL_COPY_INDEX);
        fullInfo = null;
    }

    private void instantiateFullMovieInfo()
            throws SQLException,IOException
    {
        fullInfo = new FullMovieInfo(super.getUPC());
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
            throws SQLException,IOException
    {
        if(fullInfo == null)
            instantiateFullMovieInfo();
        return fullInfo.getActors();
    }

    public String getDirector()
            throws SQLException,IOException
    {
        if(fullInfo == null)
            instantiateFullMovieInfo();
        return fullInfo.getDirector();
    }

    public String getStudio()
            throws SQLException,IOException
    {
        if(fullInfo == null)
            instantiateFullMovieInfo();
        return fullInfo.getStudio();
    }

    public String getDistro()
            throws SQLException,IOException
    {
        if(fullInfo == null)
            instantiateFullMovieInfo();
        return fullInfo.getDistro();
    }

    public int getMSRP()
            throws SQLException, IOException
    {
        if(fullInfo == null)
            instantiateFullMovieInfo();
        return fullInfo.getMSRP();
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
            throws SQLException, IOException
    {
        if(fullInfo == null)
            instantiateFullMovieInfo();
        return fullInfo.getRating();
    }

    public String getGenre()
            throws SQLException,IOException
    {
        if(fullInfo == null)
            instantiateFullMovieInfo();
        return fullInfo.getGenre();
    }
}
