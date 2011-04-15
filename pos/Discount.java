package pos;


import java.io.IOException;
import java.sql.*;
//import jdbconnection.JDBCConnection;
/**
 *
 * @author xliu01
 */
public class Discount implements TransactionItem
{
    public Discount(int deduction, String description, String tagName, int promoID)
            throws IOException
    {
        if(deduction < 0)
            throw new IOException("Price is less than 0, please check the database.");
        priceInCents = (-1) * deduction;
        this.description = new String(description);
        this.tagName = new String(tagName);
        this.barcode = promoID;
    }

    public String getType()
    {
        return "Discount";
    }

    public String getDescription()
    {
        return this.description;
    }

    public String getName()
    {
        return tagName;
    }

    public int getPrice()
    {
        return priceInCents;
    }

    public String getBarcode()
    {
        return "" + barcode;
    }

    public boolean updateItemInfoAtCheckOut(int invoiceID, JDBCConnection conn)
                throws SQLException
    {
        PreparedStatement stat = conn.prepareStatement("INSERT INTO item " +
                "VALUES (((MAX(itemID))+1), 'promo', "+ (-1 * priceInCents) +
                ", nul, " + barcode +", null, null, " + invoiceID + ");");
        return !stat.execute();
    }

    private int priceInCents;
    private String description;
    private String tagName;
    private int barcode;
}
