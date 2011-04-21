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
