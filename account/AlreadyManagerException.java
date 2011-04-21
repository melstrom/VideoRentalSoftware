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