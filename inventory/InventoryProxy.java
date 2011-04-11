/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inventory;

import proxy.Inventory;

/**
 *
 * @author melstrom
 */
public class InventoryProxy implements proxy.Inventory {
    public void addMovieRequest()
    {
        MovieManagement movieManagement = new MovieManagement();
        movieManagement.addRequest(copy, account);
    }

    public void reserveMovie()
    {
        RentalMovieManagement rentalMovieManagement = new RentalMovieManagement();
        //todo: update method call after method implementation
        rentalMovieManagement.reserve();
    }
}
