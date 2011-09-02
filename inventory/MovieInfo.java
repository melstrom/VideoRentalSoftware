package inventory;
import java.sql.*;
import java.io.*;

/**
 *
 * @author kevin
 */
public abstract class MovieInfo
{
    private int upc;

    //class public static field:
    protected final static String PK_NAME  = " Movie_Info.UPC_ ";
    protected final static String TABLE_NAME = " Movie_Info ";
    public final static String ACTORS_SEPERATOR = "::";
    //the following data fields only valid if the calling function is selecting
    //all the columns in the table
    public final static int UPC_INDEX = 1;
    public final static int FORMAT_INDEX =2;
    public final static int TITLE_INDEX = 3;
    public final static int YEAR_INDEX = 4;
    public final static int ACTORS_INDEX = 5;
    public final static int DIRECTOR_INDEX = 6;
    public final static int STUDIO_INDEX = 7;
    public final static int DISTRO_INDEX = 8;
    public final static int MSRP_INDEX = 9;
    public final static int AVAILABILITY_INDEX =10;
    public final static int TOTAL_COPY_INDEX = 11;
    public final static int RATING_INDEX = 12;
    public final static int GENRE_INDEX = 13;

    /**
     * this method sets the instance data field upc
     * @param newUPC the UPC that an object of this class wanted to have
     */
    protected void setUPC(int newUPC)
    {
        this.upc = newUPC;
    }

    /**
     * this method return the UPC of an object of this class
     * @return the UPC of this object
     */
    public int getUPC()
    {
        return this.upc;
    }

    //methods that are providing services from ShrotMovieInfo. You can also
    //access these method by an object of FullMovieInfo
    abstract String getFormat();
    abstract String getTitle();
    abstract int getYear();
    abstract int getAvailability();
    abstract int getTotalCopies();

    //methods that is acutally providing services from FullMovieInfo
    abstract String[] getActors()
            throws SQLException,IOException;
    abstract String getDirector()
            throws SQLException,IOException;
    abstract String getStudio()
            throws SQLException,IOException;
    abstract String getDistro()
            throws SQLException,IOException;
    abstract String getGenre()
            throws SQLException,IOException;
    abstract String getRating()
            throws SQLException, IOException;
    abstract int getMSRP()
            throws SQLException, IOException;
}