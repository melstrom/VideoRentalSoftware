package pos;


import java.io.IOException;
import java.sql.*;
//import jdbconnection.JDBCConnection;
/**
 *
 * @author xliu01
 */
public class Penalty_1 implements TransactionItem
{
    public Penalty_1(int price)
            throws IOException
    {
        setPrice(price);
    }

    public String getType()
    {
        return new String("Penalty");
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

    public boolean updateItemInfoAtCheckOut(int invoiceID, JDBCConnection conn)
            throws SQLException
    {
        PreparedStatement stat = conn.prepareStatement("INSERT INTO item " +
                "VALUES (((MAX(itemID))+1), 'penalty', "+ priceInCents +", null," +
                " null, null, null, " + invoiceID + ");");
        return !(stat.execute());
    }

    private int priceInCents;
}
