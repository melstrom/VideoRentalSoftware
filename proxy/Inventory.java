
package proxy;
import account.Customer;
import inventory.GeneralMovie;
import inventory.IndividualMovie;
import java.sql.*;
/**
 * 
 * 
 * @poseidon-object-id [Im68939bd3m12f379ec5cemm6254]
 */
public interface Inventory {
/**
 * <p>Does ...</p>
 * 
 * @poseidon-object-id [Im68939bd3m12f379ec5cemm621b]
 */
    public void addMovieRequest(IndividualMovie copy, Customer account) throws SQLException, ClassNotFoundException;
/**
 * <p>Does ...</p>
 * 
 * @poseidon-object-id [Im68939bd3m12f379ec5cemm61f6]
 */
    public void reserveMovie(Customer customer, GeneralMovie movie) throws SQLException, ClassNotFoundException;
}


