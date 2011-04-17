package pos;


import java.io.IOException;
import java.sql.*;
import jdbconnection.JDBCConnection;
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

    public void updateItemInfoAtCheckOut(int invoiceID)
                throws SQLException, ClassNotFoundException
    {
        JDBCConnection conn = new JDBCConnection();
        conn.getConnection();
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

        stat.executeQuery("INSERT INTO item VALUES ("+ newAccountID + ", promo, "
                + (-1 * priceInCents) + "null," + barcode +", null, null," + invoiceID
                +");");
    }

    private int priceInCents;
    private String description;
    private String tagName;
    private int barcode;
}
