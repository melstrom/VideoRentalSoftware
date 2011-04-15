package inventory;

import jdbconnection.JDBCConnection;
import java.sql.*;
//import search.*;

public class SaleMovieManagement
{
    public SaleMovieManagement()
    {}

    public void sell(String barcode)
            throws SQLException, ClassNotFoundException
    {
        JDBCConnection conn = new JDBCConnection();
        conn.getConnection();
        try
        {
            PreparedStatement stat = conn.prepareStatement("UPDATE videoSale SET"
                    + " condition = 'sold' WHERE saleID = " +
                    barcode.substring(barcode.length() - 9) + ";");
            int hasResult = stat.executeUpdate();
            if(hasResult != 0)
                return;
            else
                throw new SQLException("no sale movie find.");
        }
        finally
        {
            conn.closeConnection();
        }
    }
}
