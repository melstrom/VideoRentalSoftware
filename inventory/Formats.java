package inventory;

import java.sql.SQLException;
import java.sql.ResultSet;
import jdbcconnection.JDBCConnection;
import java.util.ArrayList;

/**
 * This is a singleton class responsible for keeping track of an up to date
 * list of possible formats, using the singleton design pattern.
 *
 * Get an instance by calling getInstance()
 * Then you can find out what formats are in the database by calling
 * getFormats()
 * If you change the formats taht are in the database then call
 * update()
 *
 * Reference: http://www.oodesign.com/singleton-pattern.html
 * Reference: http://stackoverflow.com/questions/2284502/singleton-and-exception
 * 
 */
public class Formats
{
    private static final Formats INSTANCE;
    private String[] formats;

    static
    {
        try
        {
            INSTANCE = new Formats();
        }
        catch (Exception exception)
        {
            throw new ExceptionInInitializerError(exception);
        }
    }

    private Formats() throws SQLException, ClassNotFoundException
    {
        update();
    }

    /**
     * Returns an instance of Formats ensuring that only one instance is ever
     * created.
     * @return the instance of Formats
     */
    public static Formats getInstance()
    {
            return INSTANCE;
    }

    /**
     * Returns the possible formats that a video can have
     * @return an array of formats, held as strings
     */
    public String[] getFormats()
    {
        return formats;
    }

    /**
     * Updates the possible formats.  Should be called when new formats are
     * added to the database or old formats are removed.
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void update()
            throws SQLException, ClassNotFoundException
    {
        JDBCConnection connection = new JDBCConnection();
        try
        {
            String query = JDBCConnection.makeQuery("formats", "format", null);
            ResultSet result = connection.getResults(query);
            ArrayList<String> formatList = new ArrayList<String>();
            while (result.next())
            {
                formatList.add(result.getString(1));
            }
            formats = new String[formatList.size()];
            for (int i = 0; i < formats.length; i++)
            {
                formats[i] = formatList.get(i);
            }
        }
        finally
        {
            connection.closeConnection();
        }
    }
}
