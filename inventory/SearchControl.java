package inventory;
import store.VRSConnection;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author kevin
 */
public class SearchControl
{
    private ArrayList<MovieInfo> results;

    public SearchControl()
    {
        results = new ArrayList<MovieInfo>();
    }
    
    public ArrayList<MovieInfo> getShortMovieInfo()
            throws IOException
    {
        if(results.isEmpty())
            throw new IOException("You have not searched yet. Result is "
                                  + "empty.");
        return results;
    }

    public FullMovieInfo showFullMovieInfo(int selectedUPC)
            throws SQLException, IOException
    {
        return new FullMovieInfo(selectedUPC);
    }

    public void cleanUp()
    {
        results.clear();
    }

    private void search(final String CONDITION)
            throws SQLException, IOException
    {
        final String[] TABLE_NAMES = {MovieInfo.TABLE_NAME};

        //search operation:
        VRSConnection conn = VRSConnection.getInstance();
        ResultSet rawResult = conn.select(TABLE_NAMES, null, CONDITION,
                                          null);

        //constructing the data field results:
        this.cleanUp();//ensure no history is left.
        results = new ArrayList<MovieInfo>();
        while(rawResult.next())
        {
            MovieInfo temp = new ShortMovieInfo(rawResult);//IOException
                                                           //will be thrown
                                                           //here, but in
                                                           //normal
                                                           //operation, it
                                                           //rarely happens.
            results.add(temp);
        }
    }

    public void searchForTitle(String targetTitle)
            throws SQLException, IOException
    {
        final String CONDITION  = "title_ = " + targetTitle;

        try
        {
            search(CONDITION);
        }
        catch(SQLException expt)
        // this catch clause needed to be ensured first!!!!!
        {
            if(expt.getMessage().contains("not found"))
            //Not results error:
                throw new SQLException("The movie with the title \"" +
                                       targetTitle + "\" cannot be found.");
            else
            //Other errors:
                throw expt;
        }
    }

    public void searchForDirector(String targetDirector)
            throws SQLException, IOException
    {
        final String CONDITION  = "director_ = " + targetDirector;

        try
        {
            search(CONDITION);
        }
        catch(SQLException expt)
        // this catch clause needed to be ensured first!!!!!
        {
            if(expt.getMessage().contains("not found"))
            //Not results error:
                throw new SQLException("The movie with the director name \"" +
                                       targetDirector + "\" cannot be found.");
            else
            //Other errors:
                throw expt;
        }
    }

    public void searchForActor(String targetActors)
            throws SQLException, IOException
    {
        final String CONDITION  = "actors_ LIKE " + targetActors;//SQL condition
                                                                 //needs to be
                                                                 //confirmed

        try
        {
            search(CONDITION);
        }
        catch(SQLException expt)
        // this catch clause needed to be ensured first!!!!!
        {
            if(expt.getMessage().contains("not found"))
            //Not results error:
                throw new SQLException("The movie with the actor name \"" +
                                       targetActors + "\" cannot be found.");
            else
            //Other errors:
                throw expt;
        }
    }
    
    public void searchForYear(int targetYear)
            throws SQLException, IOException
    {
        final String CONDITION  = "year_ = " + targetYear;

        try
        {
            search(CONDITION);
        }
        catch(SQLException expt)
        // this catch clause needed to be ensured first!!!!!
        {
            if(expt.getMessage().contains("not found"))
            //Not results error:
                throw new SQLException("The movie with the year \"" +
                                       targetYear + "\" cannot be found.");
            else
            //Other errors:
                throw expt;
        }
    }

    public void searchForGenre(String targetGenre)
            throws SQLException, IOException
    {
        final String CONDITION  = "genre_ = " + targetGenre;

        try
        {
            search(CONDITION);
        }
        catch(SQLException expt)
        // this catch clause needed to be ensured first!!!!!
        {
            if(expt.getMessage().contains("not found"))
            //Not results error:
                throw new SQLException("The movie with the genre \"" +
                                       targetGenre + "\" cannot be found.");
            else
            //Other errors:
                throw expt;
        }
    }
}
