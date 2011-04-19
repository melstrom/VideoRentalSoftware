//package pos;
//
//
//import java.sql.*;
//import java.io.IOException;
////import jdbconnection.JDBCConnection;
///**
// *
// * @author xliu01
// */
//public class Refund implements TransactionItem
//{
//    public Refund(String invoiceID, String barcode)/////////barcode
//            throws SQLException, ClassNotFoundException, IOException
//    {
//        setInstanceField(Integer.parseInt(invoiceID),
//                Integer.parseInt(barcode.substring(barcode.length() - 9)));
//    }
//
//    private void setInstanceField(int invoiceID, int sale_ID)/////////barcode
//            throws SQLException, ClassNotFoundException, IOException
//    {
//        JDBCConnection conn = new JDBCConnection();
//        conn.getConnection();
//        try
//        {
//            PreparedStatement stat = conn.prepareStatement("SELECT * FROM item" +
//                    " WHERE SaleID=" + sale_ID +" AND invoiceID=" + invoiceID + ";");
//            boolean hasResult = stat.execute();
//            if(hasResult)//BUG HERE!! if the movie has been sold and refund a
//                         //few time, the result may not be accurate
//            {
//                ResultSet result = stat.getResultSet();
//                result.next();
//                priceInCents = result.getInt(3);
//                saleID = result.getInt(4);
//            }
//            else
//                throw new IOException("Item not found in the database.");
//        }
//        finally
//        {
//            conn.closeConnection();
//        }
//    }
//
//    public String getType()
//    {
//        return "Refund";
//    }
//
//    public String getName()
//    {
//        return "Refund for movie:" + saleID;
//    }
//
//    public String getBarcode()
//    {
//        return "" + saleID;
//    }
//
//    public int getPrice()
//    {
//        return priceInCents;
//    }
//
//    public boolean updateItemInfoAtCheckOut(int invoiceID, JDBCConnection conn)
//                throws SQLException
//    {
//        PreparedStatement stat = conn.prepareStatement("INSERT INTO item " +
//                "VALUES (((MAX(itemID))+1), 'refund', "+  priceInCents + ", " +
//                saleID + ", null, 'for sale', null," + invoiceID + ");");
//        return !stat.execute();
//    }
//
//    private int saleID;
//    private int priceInCents;
//}
