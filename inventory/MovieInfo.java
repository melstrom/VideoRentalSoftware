package inventory;

/**
 *
 * @author kevin
 */
public abstract class MovieInfo
{
    private String upc;

    /**
     * this method sets the instance data field upc
     * @param newUPC the UPC that an object of this class wanted to have
     */
    private void setUPC(String newUPC)
    {
        this.upc = newUPC;
    }

    /**
     * this method return the UPC of an object of this class
     * @return the UPC of this object
     */
    public String getUPC()
    {
        return this.upc;
    }

    abstract String getTitile();
    abstract String[] getActors();
    abstract String getDirector();
    abstract int getYear();
    abstract String getStudio();
    abstract String getDistro();
    abstract String getGenre();
    abstract String getFormat();
}
