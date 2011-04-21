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