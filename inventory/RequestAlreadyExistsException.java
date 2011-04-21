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
