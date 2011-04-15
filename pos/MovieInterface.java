package pos;


import java.sql.*;
import java.io.IOException;
import search.Search;
import inventory.IndividualMovie;
import jdbconnection.JDBCConnection;
//import pos.PriceSchemeManagement;
/**
 *
 * @author xliu01
 */
public class MovieInterface implements TransactionItem
{
    public MovieInterface(String barcode, PriceSchemeManagement PSM)
            throws SQLException, ClassNotFoundException, IOException
    {
        IndividualMovie movie = (IndividualMovie)(Search.preview(barcode));
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

    public void updateItemInfoAtCheckOut(int invoiceID, JDBCConnection conn)
                throws SQLException
    {
        // rental or sale movie has different query to execute
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

    private String barcode;
    private String title;
    private String category;
    private String format;
    private int priceInCents;
}
