/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inventory;

/**
 *
 * @author Mitch
 */
public class MovieNotAvailableException extends Exception{
    public MovieNotAvailableException(){}

    public MovieNotAvailableException(String msg)
    {
        super(msg);
    }

}
