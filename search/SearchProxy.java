/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

/**
 *
 * @author melstrom
 */
public class SearchProxy implements proxy.Search
{

    public void searchVideo()
    {
        Search search = new Search();
        search.searchMovies(title, actor, director);
    }

    /**
     * <p>Does ...</p>
     *
     * @poseidon-object-id [Im68939bd3m12f379ec5cemm615b]
     */
    public void previewVideo()
    {
        Search search = new Search();
        search.previewMovie(barcode);
    }
/**
 * <p>Does ...</p>
 *
 * @poseidon-object-id [Im68939bd3m12f379ec5cemm6136]
 */
    public void browse()
    {
        Search search new Search();
        search.browse(genre);
    }
}
