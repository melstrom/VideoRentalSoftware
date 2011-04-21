package inventory;

import jdbconnection.JDBCConnection;
import java.sql.*;

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
            String SQL = "UPDATE videoSale SET"
                    + " videoSale.condition = 'sold' WHERE saleID = " +
                    barcode.substring(barcode.length() - 9) + ";";
            PreparedStatement stat = conn.prepareStatement(SQL);
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
