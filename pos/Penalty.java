package pos;


import java.io.IOException;
import java.sql.*;
import jdbconnection.JDBCConnection;
/**
 *
 * @author xliu01
 */
public class Penalty implements TransactionItem
{
    public Penalty(int price)
            throws IOException
    {
        setPrice(price);
    }

    public String getType()
    {
        return "Penalty";
    }

    public String getName()
    {
        return getType();
    }

    public int getPrice()
    {
        return this.priceInCents;
    }

    public String getBarcode()
    {
        return "";
    }

    private void setPrice(int price)
            throws IOException
    {
        if(price < 0)
            throw new IOException("Penalty price cannot be less than 0");
        this.priceInCents = price;
    }

    public void updateItemInfoAtCheckOut(int invoiceID)
            throws SQLException,ClassNotFoundException
    {
        JDBCConnection conn = new JDBCConnection();
        Statement stat = conn.createStatement();
        String table = "item";
        String column = "itemID";
        String SQL = "SELECT " + column + " FROM " + table;
        ResultSet rs = stat.executeQuery(SQL);
        int LastAccountID = 0;
        if (rs.last())
        {
            LastAccountID = rs.getInt(column);
        }
        int newAccountID = LastAccountID + 1;

        stat.executeQuery("INSERT INTO item VALUES (" + newAccountID + ", 'penalty', "
                + priceInCents + ", null, null, null, null, " + invoiceID + ");");
    }

    private int priceInCents;
}
