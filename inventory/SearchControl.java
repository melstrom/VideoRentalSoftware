package inventory;
import store.VRSConnection;

/**
 *
 * @author kevin
 */
public class SearchControl
{
    private MovieInfo[] results;

    public SearchControl()
    {
        results = null;
    }
    
    public MovieInfo[] getShortMovieInfo()
    {
        return results;
    }

    public void searchForTitle(String targetTitle)
    {

    }
}
