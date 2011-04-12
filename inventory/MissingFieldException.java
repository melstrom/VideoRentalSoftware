/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inventory;

/**
 *
 * @author melstrom
 * FROM MovieExistsException
 */

public class MissingFieldException extends Exception
{
    MissingFieldException()
    {
        super();
    }



    MissingFieldException(String message)
    {
        super(message);
    }
}