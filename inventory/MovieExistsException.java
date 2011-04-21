package inventory;

/**
 * This exception indicates that you are trying to add a movie to the database
 * that already exists in the database; you are trying to create duplicate
 * information
 * @author Mitch
 */
public class MovieExistsException extends Exception
{
    MovieExistsException()
    {
        super();
    }

    MovieExistsException(String message)
    {
        super(message);
    }
}
