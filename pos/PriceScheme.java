package pos;
import java.sql.*;
import java.util.ArrayList;
import jdbcconnection.*;

/**
This class contains the information of prices and the information of categories
and formats.
@author kevin
 */
public class PriceScheme
{
    /**
    This constructor will extract the prices of each kind of category for each
    format; they will be stored into the price attribute.
    The information of categories will be extracted from the catagories table and
    stored into the array list of category.
    The information of formats will be extracted from the format table and stored
    into the array list of format.

    This class does NOT guarentee that every category of a format has a price.
    This means that some column in the prices attribute may be 0.
     @throws SQLException If there is anything wrong with the database, this exception will be thrown.
     */
    public PriceScheme()
            throws SQLException, ClassNotFoundException
    {
	category = new ArrayList<String>();
	format = new ArrayList<String>();
        JDBCConnection conn = new JDBCConnection();
	conn.getConnection();
        try
        {
                //find out the categories first
		PreparedStatement stat = conn.prepareStatement("SELECT * FROM catagories;");
		boolean hasResult = stat.execute();
		if(hasResult)
		{
                    ResultSet catResults = stat.getResultSet();
                    while(catResults.next())
                        category.add(catResults.getString(1).trim().toLowerCase());
		}
                else
                    throw new SQLException("Catagories table in the database is empty.");

                //find out the formats.
                stat = conn.prepareStatement("SELECT * FROM formats;");
                hasResult = stat.execute();
                if(hasResult)
                {
                    ResultSet formResults = stat.getResultSet();
                    while(formResults.next())
                        format.add(formResults.getString(1).trim().toLowerCase());
                }
                else
                    throw new SQLException("Format table in the database is empty.");

                //finally, find out the prices.
                prices = new int [category.size()][format.size()];
                stat = conn.prepareStatement("SELECT * FROM pricing;");
                hasResult = stat.execute();
                if(hasResult)
                {
                    ResultSet priceResults = stat.getResultSet();
                    while(priceResults.next())
                    {
                        int x = category.indexOf(priceResults.getString(2).trim().toLowerCase());
                        int y = format.indexOf(priceResults.getString(3).trim().toLowerCase());
                        prices[x][y] = priceResults.getInt(1);
                    }
                }
                else
                    throw new SQLException("Pricing table in the database is empty.");
        }
        finally
        {
            conn.closeConnection();
        }
    }

    /**
     * This method return the price with the index numbers provided.
     * @param cat the index number that identify the category
     * @param form the index number that identify the format;
     * @return the price
     */
    public int getPrice(int cat, int form)
    {
        return prices[cat][form];
    }

    /**
     * For the PriceSchemeManagement to find out the index number a category.
     * -1 will be returned if it is not found.
     * @param cat the name of the category.
     * @return
     */
    public int getIndexOfCategory(String cat)
    {
        return category.indexOf(cat.trim().toLowerCase());
    }

   /**
     * For the PriceSchemeManagement to find out the index number a format.
     * -1 will be returned if it is not found.
     * @param form the name of the format.
     * @return
     */
    public int getIndexOfFormat(String form)
    {
        return format.indexOf(form.trim().toLowerCase());
    }

    /**
     * Return how many categories, in other words, how many columns of the prices
     * @return the number of categories
     */
    public int getNumOfCategory()
    {
        return category.size();
    }

    /**
     * Return how many format, in other words, how many rows of the prices
     * @return the number of format.
     */
    public int getNumOfFormats()
    {
        return format.size();
    }
    
    public int[][] getAllPrices()
    {
        return this.prices;
    }
    
    public ArrayList<String> getAllCategories()
    {
        return this.category;
    }
    
    public ArrayList<String> getAllFormats()
    {
        return this.format;
    }

    private int[][] prices;
    private ArrayList<String> category;
    private ArrayList<String> format;
}
