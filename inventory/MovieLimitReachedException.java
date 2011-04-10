/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inventory;

/**
 * This exception indicates that the maximum number of Movies is reached
 * @author Mitch
 */
public class MovieLimitReachedException extends Exception{

    public MovieLimitReachedException(){}
    public MovieLimitReachedException(String msg)
    {
        super(msg);
    }
}
