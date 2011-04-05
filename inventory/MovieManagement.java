package inventory;
import java.util.Date;
import account.Customer;
/**
 * April 4 (Monday)
 * -to-do
 *      understand and setup SQL connection
 *      read on java exceptions and implement them (may reimpl. checkDupl&NULL as exceptions)
 * -movieRequest does not perform any functions
 *      -on top of that, there is no place for it in the db
 *
 * April 3 (Sunday)
 * -start implementation of most methods
 * -to-do
 *      private bool checkDuplicateSKU (String SKU)
 *      private bool checkDuplicateBarcode (String barcode)
 *      private bool checkNULLinfo (String[] info)
 * -bugs
 *      line 90, wrong return type
 *
 * March 31 (Thursday)
 * -add/remove methods to reflect changes in class diagram
 *
 * March 29 (Tuesday)
 * -creation of MovieManagement class
 * -initial methods:
 *      createGeneralMovie
 *      addCopy #need to add types for category, format, and barcode#
 *      editInfo
 *      setPrice
 *      getQuantity
 *          -where is copies:ArrayList<IndividualMovie> being moved to?
 *      getReservations
 *          -should this method be in MovieManagement?
 *      requestMovie
 * -added JavaDoc
 * -too many unknowns; try later
 *
 * @author melstrom
 */
public class MovieManagement
{

    private GeneralMovie movie;
    private IndividualMovie copy;
    private MovieRequest request;

    /**
     *
     */
    public MovieManagement()
    {
        this.movie = null;
    }

    /**
     *
     * @param movie
     */
    public MovieManagement(GeneralMovie movie)
    {
        this.movie = movie;
    }

    /**
     * Constructs a new movie as part of the movie catalog
     * @param info contains the 7 required information to identify a movie
     */
    public void createGeneralMovie(String[] info)
    {
        if (this.checkNULLinfo(info) == false)
        {
            String SKU = info[0];

            if (this.checkDuplicateSKU(SKU) == false)
            {
                //passed all tests, create the movie
                String title = info[1];
                String actors = info[2];
                String director = info[3];
                String releaseDate = info[4];
                String synopsis = info[5];
                String genre = info[6];

                            //splits the actors into an array of actors
            String[] actorArray = actors.split(",");

                this.movie = new GeneralMovie(SKU, title, actors, director, releaseDate, synopsis); //pass in actors? or actorArray?
                movie.addNewTitle();
            }
        }
    }

    /**
     * Constructs a new single copy of a movie
     * @param category the category the movie belongs to (need to find out more about this)
     * @param format the media format of the movie (VHS, DVD, Bluray)
     * @param barcode the unique identification of the individual movie copy
     */
    public void addCopy(String category, String format, String barcode)
    {
        if (this.checkDuplicateBarcode(barcode) == false)
        {
            String SKU = this.movie.getSKU();       //getSKU returns wrong type:int
            barcode = BarCodeChecker.assign(SKU);

            //passed all tests, create the copy
            this.copy = new IndividualMovie(category,format, barcode, this.movie);

            copy.setCategory(category);
            copy.setFormat(format);
            copy.setBarCode(barcode);
        }
    }

    /**
     * Changes the information of a movie
     * @param info contains the 7 required information to identify a movie
     */
    public void editInfo(String[] info)
    {
        if (this.checkNULLinfo(info) == false)
        {
            String SKU = info[0];
            String title = info[1];
            String actors = info[2];
            String director = info[3];
            String releaseDate = info[4];
            String synopsis = info[5];
            String genre = info[6];
            
            //splits the actors into an array of actors
            String[] actorArray = actors.split(",");

            //movie.setSKU(SKU);                //GM does not have setSKU:allowed to edit at all?
            movie.setTitle(title);
            movie.setActors(actorArray);
            movie.setDirector(director);
            movie.setReleaseDate(releaseDate);  //GregorianCalendar
            movie.setSynopsis(synopsis);
            movie.setGenre(genre);              //missing setGenre in GM
        }
    }

//    /**
//     *
//     * @return quantity of...something
//     */
//    public int getQuantity()
//    {
//        String table = "videoInfo";
//        int quantity = movie.getSKU();
//        //SELECT COUNT(SKU) FROM videoInfo WHERE SKU='SKU'
//
//        return quantity;
//    }

    /**
     * 
     */
    public MovieRequest[] getRequest()
    {
        String[] movieRequest = new String[0];
        //where does request go in the db?
        //can't fetch any data...
        return movieRequest;
    }

    public void addRequest(Customer account)
    {
        //String title, String format, Date releaseDate, CustomerAccount requestAcc
        String title = copy.getTitle();
        String format = copy.getFormat();      //missing method?
        Date releaseDate = copy.getReleaseDate();  //need to fix type to match return
        Customer requestAcc = account;

        this.request = new MovieRequest(title, format, releaseDate, requestAcc);
        request.createQueue();
        //createQueue does nothing, moreover, it's spelt wrong the MovieRequest class
    }

/**
 * Manually removes a request from the list of requests
 * @param request
 */
    public void removeRequest(MovieRequest request)
    {
        //how do we identify a single request?
        //I want to call request.getXY to identify a particular row in the table, and just delete that row!
        this.request = request;
    }

/**
 *         //check if this movie already exists in the db
 * @param SKU
 * @return
 */
    private boolean checkDuplicateSKU(String SKU)
    {

        //SELECT SKU FROM videoInfo WHERE SKU = 'newSKU#'
        String table = "videoInfo";
        String column = "SKU";
        String query = "SELECT " + column + " FROM " + table + " WHERE " + column + "='" + SKU + "'";

        if (execute(query) != 0)
        {
            //Exception:Movie already exists
        }
        return false;
    }

    /**
     *         //checks for duplicate barcode in the db
     * @param barcode
     * @return
     */
    private boolean checkDuplicateBarcode(String barcode)
    {

        //Query:SELECT barcode FROM physicalVideo WHERE barcode = 'barcode'
        String table = "barcode";
        String column = "physicalVideo";
        String query = "SELECT " + column + " FROM " + table + " WHERE " + column + "='" + barcode + "'";

        if (execute(query) != 0)
        {
            //Exception:Movie already exists
        }
        return false;
    }

    /**
     *         //Since a new movie should have all the info, test for null values,        //checks if there are any null fields
     * @param info
     * @return
     */
    private boolean checkNULLinfo(String[] info)
    {
        for (int i = 0; i < 6; i++)
        {
            String test = info[i];
            if (test == null)
            {
                //Cannot have null values
            }
        }
        return false;
    }
}
