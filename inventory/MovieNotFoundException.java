package inventory;

/**
 * This exception means that a movie barcode supplied does not exist in the
 * database or does not correspond to any movie.
 * @author Mitch
 */
public class MovieNotFoundException extends Exception
{
    public MovieNotFoundException()
    {
        super();
    }

    public MovieNotFoundException(String message)
    {
        super(message);
    }

}
