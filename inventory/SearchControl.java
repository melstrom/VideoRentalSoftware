package inventory;
import store.VRSConnection;
import java.io.*;
import java.sql.*;

/**
 *
 * @author kevin
 */
public class SearchControl
{
    private MovieInfo[] results;

    public SearchControl()
    {
        results = null;
    }
    
    public MovieInfo[] getShortMovieInfo()
            throws IOException
    {
        if(results.length == 0)
            throw new IOException("You have not searched yet. Result is "
                                  + "empty.");
        return results;
    }

    public FullMovieInfo showFullMovieInfo(int selectedUPC)
            throws SQLException, IOException
    {
        return new FullMovieInfo(selectedUPC);
    }

    public void searchForTitle(String targetTitle)
            throws SQLException
    {
        final String[] TABLE_NAMES = {MovieInfo.TABLE_NAME};
        final String CONDITION  = "title_ = " + targetTitle;

        try
        {
            VRSConnection conn = VRSConnection.getInstance();
            ResultSet rawResult = conn.select(TABLE_NAMES, null, CONDITION, 
                                              null);

            // there is no need to test if the rawResult is empty, since an
            // SQLException will be thrown by the above statement.
            
            while(rawResult.next())
            {
                // PAUSE HERE
            }
        }
        catch(SQLException expt)
        // this catch clause needed to be ensured first!!!!!
        {
            if(expt.getMessage().contains("not found"))
                throw new SQLException("The movie with title \"" + targetTitle +
                                       "\" cannot be found.");
            else
                throw expt;
        }
    }
}
