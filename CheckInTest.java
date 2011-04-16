/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import inventory.*;
import java.sql.*;
/**
 *
 * @author kevin
 */
public class CheckInTest
{
    public static void main(String[] args)
            throws SQLException, ClassNotFoundException,Exception
    {
        RentalMovieManagement rentalManager = new RentalMovieManagement();
        int customerID = 0;
        String barcode = "678149496420" + "100000011";
        String newCondition = "broken";

        //rentalManager.checkIn(customerID, barcode, newCondition);
    }
}
