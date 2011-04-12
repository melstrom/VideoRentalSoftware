/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inventory;

import java.sql.SQLException;
import proxy.Inventory;
import account.Customer;

/**
 *
 * @author melstrom
 */
public class InventoryProxy implements proxy.Inventory {
    public void addMovieRequest(IndividualMovie copy, Customer account) throws SQLException, ClassNotFoundException
    {
        MovieManagement movieManagement = new MovieManagement();
        movieManagement.addRequest(copy, account);
    }

    public void reserveMovie(Customer customer, GeneralMovie movie) throws SQLException, ClassNotFoundException
    {
        RentalMovieManagement rentalMovieManagement = new RentalMovieManagement();
        //todo: update method call after method implementation
        rentalMovieManagement.makeReservation(customer, movie);
    }
}
