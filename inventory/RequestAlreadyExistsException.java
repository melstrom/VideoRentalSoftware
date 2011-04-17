/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inventory;

/**
 *
 * @author melstrom
 */
public class RequestAlreadyExistsException extends Exception
{
    RequestAlreadyExistsException()
    {

    }

    RequestAlreadyExistsException(String message)
    {
        super(message);
    }
}
