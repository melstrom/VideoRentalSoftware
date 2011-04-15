package pos;


import java.sql.*;
import java.io.IOException;
import search.*;
import inventory.*;
import jdbconnection.JDBCConnection;
//import pos.PriceSchemeManagement;
/**
 *
 * @author xliu01
 */
public class MovieInterface implements TransactionItem
{
    public MovieInterface(String barcode, PriceSchemeManagement PSM)
            throws SQLException, ClassNotFoundException, IOException,
            MovieNotFoundException
    {
        IndividualMovie movie = (IndividualMovie)(Search.previewMovie(barcode));
        this.barcode = barcode;
        this.title = movie.getTitle();
        this.category = movie.getCategory();
        this.priceInCents = movie.getPrice();
    }

    public String getType()
    {
        return category;
    }

    public String getName()
    {
        return title;
    }

    public String getBarcode()
    {
        return barcode;
    }

    public String getFormat()
    {
        return format;
    }

    public int getPrice()
    {
        return priceInCents;
    }

    public void updateItemInfoAtCheckOut(int invoiceID)
                throws SQLException,ClassNotFoundException
    {
        JDBCConnection conn = new JDBCConnection();
        conn.getConnection();
        // rental or sale movie has different query to execute
        try
        {
            if(category.equals("for sale"))
            {
                Statement stat = conn.createStatement();

                String table = "item";
                String column = "itemID";
                String SQL = "SELECT " + column + " FROM " + table;
                ResultSet rs = stat.executeQuery(SQL);
                rs.last();
                int largestItemID = rs.getInt(column);

                stat.executeQuery("INSERT INTO item VALUES (" + largestItemID + ","
                        + "'sale', "+ priceInCents +", '"+ barcode.substring(barcode.length() - 9)
                        +"', null, '"+ category +"', null,"+ invoiceID +");");
            }
            else
            {
               Statement stat = conn.createStatement();

                String table = "item";
                String column = "itemID";
                String SQL = "SELECT " + column + " FROM " + table;
                ResultSet rs = stat.executeQuery(SQL);
                rs.last();
                int largestItemID = rs.getInt(column);

                stat.executeQuery("INSERT INTO item VALUES (" + largestItemID + ", "
                        + priceInCents + ", null, null, " + category + ", " +
                        barcode.substring(barcode.length() - 9) + ", " + invoiceID +");");
            }
        }
        finally
        {
            conn.closeConnection();
        }
    }

    private String barcode;
    private String title;
    private String category;
    private String format;
    private int priceInCents;
}
