package inventory;

import java.sql.SQLException;
import java.sql.ResultSet;
import jdbconnection.JDBCConnection;
import java.util.ArrayList;

/**
 * This is a singleton class responsible for keeping track of an up to date
 * list of possible Ratings, using the singleton design pattern.
 *
 * Get an instance by calling getInstance()
 * Then you can find out what Ratings are in the database by calling
 * getRatings()
 * If you change the Ratings taht are in the database then call
 * update()
 *
 * Reference: http://www.oodesign.com/singleton-pattern.html
 * Reference: http://stackoverflow.com/questions/2284502/singleton-and-exception
 *
 */
public class Ratings
{
    private static final Ratings INSTANCE;
    private String[] ratings;

    static
    {
        try
        {
            INSTANCE = new Ratings();
        }
        catch (Exception exception)
        {
            throw new ExceptionInInitializerError(exception);
        }
    }

    private Ratings() throws SQLException, ClassNotFoundException
    {
        update();
    }

    /**
     * Returns an instance of Ratings ensuring that only one instance is ever
     * created.
     * @return the instance of Ratings
     */
    public static Ratings getInstance()
    {
            return INSTANCE;
    }

    /**
     * Returns the possible Ratings that a video can have
     * @return an array of Ratings, held as strings
     */
    public String[] getRatings()
    {
        return ratings;
    }

    /**
     * Updates the possible Ratings.  Should be called when new Ratings are
     * added to the database or old Ratings are removed.
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void update()
            throws SQLException, ClassNotFoundException
    {
        JDBCConnection connection = new JDBCConnection();
        try
        {
            String query = JDBCConnection.makeQuery("ratings", "rating", null);
            ResultSet result = connection.getResults(query);
            ArrayList<String> genreList = new ArrayList<String>();
            while (result.next())
            {
                genreList.add(result.getString(1));
            }
            ratings = new String[genreList.size()];
            for (int i = 0; i < ratings.length; i++)
            {
                ratings[i] = genreList.get(i);
            }
        }
        finally
        {
            connection.closeConnection();
        }
    }
}
