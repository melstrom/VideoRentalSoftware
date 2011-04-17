/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inventory;

import jdbconnection.JDBCConnection;

import java.util.ArrayList;
import java.sql.*;
/**
 *
 * @author kevin
 */
public class DiscountViewer
{
    public DiscountViewer()
            throws SQLException, ClassNotFoundException
    {
        DISCOUNTS = new ArrayList<ArrayList<String>>();
        JDBCConnection conn = new JDBCConnection();
        try
        {
            PreparedStatement stat = conn.prepareStatement("SELECT * FROM promo;");
            ResultSet results = stat.executeQuery();
            while(results.next())
            {
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(results.getString(1));
                temp.add(results.getString(2));
                temp.add(results.getString(3));
                temp.add(results.getString(4));
                DISCOUNTS.add(temp);
            }
        }
        finally
        {
            conn.closeConnection();
        }
    }

    public ArrayList<ArrayList<String>> getAllDiscounts()
    {
        return DISCOUNTS;
    }

    private ArrayList<ArrayList<String>> DISCOUNTS;
}
