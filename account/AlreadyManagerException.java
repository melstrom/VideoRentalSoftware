/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package account;

/**
 *
 * @author melstrom
 */
public class AlreadyManagerException extends Exception
{
    AlreadyManagerException()
    {

    }

    AlreadyManagerException(String message)
    {
        super(message);
    }
}