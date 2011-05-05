package inventory;

import java.sql.SQLException;
import java.sql.ResultSet;
import jdbcconnection.JDBCConnection;
import java.util.ArrayList;

/**
 * This is a singleton class responsible for keeping track of an up to date
 * list of possible Genres, using the singleton design pattern.
 *
 * Get an instance by calling getInstance()
 * Then you can find out what Genres are in the database by calling
 * getGenres()
 * If you change the Genres taht are in the database then call
 * update()
 *
 * Reference: http://www.oodesign.com/singleton-pattern.html
 * Reference: http://stackoverflow.com/questions/2284502/singleton-and-exception
 *
 */
public class Genres
{
    private static final Genres INSTANCE;
    private String[] genres;

    static
    {
        try
        {
            INSTANCE = new Genres();
        }
        catch (Exception exception)
        {
            //exception.printStackTrace(); // TESTING
            throw new ExceptionInInitializerError(exception);
        }
    }

    private Genres() throws SQLException, ClassNotFoundException
    {
        update();
    }



    /**
     * Returns an instance of Genres ensuring that only one instance is ever
     * created.
     * @return the instance of Genres
     */
    public static Genres getInstance()
    {
            return INSTANCE;
    }



    /**
     * Returns the possible Genres that a video can have
     * @return an array of Genres, held as strings
     */
    public String[] getGenres()
    {
        return genres;
    }



    /**
     * Updates the possible Genres.  Should be called when new Genres are
     * added to the database or old Genres are removed.
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void update()
            throws SQLException, ClassNotFoundException
    {
        JDBCConnection connection = new JDBCConnection();
        try
        {
            String query = JDBCConnection.makeQuery("genres", "genre", null);
            ResultSet result = connection.getResults(query);
            ArrayList<String> genreList = new ArrayList<String>();
            while (result.next())
            {
                genreList.add(result.getString(1));
            }
            genres = new String[genreList.size()];
            for (int i = 0; i < genres.length; i++)
            {
                genres[i] = genreList.get(i);
            }
        }
        finally
        {
            connection.closeConnection();
        }
    }
}
