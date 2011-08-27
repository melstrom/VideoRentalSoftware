/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inventory;

/**
 *
 * @author kevin
 */
public class AvailabilityException extends Exception {

    /**
     * Creates a new instance of <code>AvailabilityException</code> without detail message.
     */
    public AvailabilityException() {
    }


    /**
     * Constructs an instance of <code>AvailabilityException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public AvailabilityException(String msg) {
        super(msg);
    }
}
