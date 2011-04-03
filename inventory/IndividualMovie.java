/**
* IndividualMovie class
* Sets and gets the individual movie object with price, format, category and bar-code by calling and setting methods.
* @author Legen
* @version 1.0 March 27, 2011
*/

package inventory;

public class IndividualMovie extends GeneralMovie{

    /**
    * The constructor
    * inherits from GeneralMovie
    * takes 4 default attributes
    * @param barcode the number uniquely identifies each movie and is broken into two parts
    * @param category the pricing category for the movie such as new release, or 7 day rental
    * @param price this cost of the rental in cents
    * @param format is the type of movie format, such as DVD, VHS, Bluray
    */
   
   public IndividualMovie(String category, int price, String format, String barcode, GeneralMovie movie)
   {
        setTitle(movie.getTitle());
        setActors(movie.getActors());
        setDirector(movie.getDirector());
        setSynopsis(movie.getSynopsis());
        setReleaseDate(movie.getReleaseDate());
        setPrice(price);
        setFormat(format);
        setCategory(category);
        setBarCode(barcode);
        setupConnection();
   }
   
    private String category;            //The categories for the movies such as new release, 7 day rental ect.
    private int price;                  //The cost of renting the movie in cents
    private String format;              //The format of the movie, such as vhs, bluray, dvd
    private String barcode;             //The unique number code that is on each movie as barcodes
  
    /**
    *Set the format for the individual movie which could be DVD, VHS, Blu-ray for example
    *input: aFormat
    *@param aFormat a format type
    */
    
    protected void setFormat(String aFormat)
    {
    	this.format = aFormat;
    }
   
    /**
    *Get the format for the individual movie 
    *@return format
    */
    public String getFormat()
    {
    return format;
    }
   
    /**
    *Set the price for the individual movie in cents
    *input: cost
    *@param cost is how much the rental price is in cents
    */
    protected void setPrice(int cost)
    {
    this.price = cost;
    }
   
    /**
    *Get the price for the individual movie in cents
    *@return price
    */
    public int getPrice()
    {
    return price;
    }
   
    /**
    *Set the category for the individual movie, which is used for pricing schemes and rental times
    *input: aCategory
    *@param aCategory is the category the movie belongs to
    */
    protected void setCategory(String aCategory)
    {
    this.category = aCategory;
    }
   
   
    /**
    *Get the type of category for the individual movie
    *@return category
    */
    public String getCategory()
    {
    return category;
    }
   
    /**
    *Set the bar-code for the individual movie
    *input: theBarcode
    *@param theBarcode the unique string on the movie the identifies it
    */
    protected void setBarCode(String theBarcode)
    {
    this.barcode = theBarcode;
    }
   
   
    /**
    *Get the individual movies barcode which is a unique string 
    *@return barcode
    */
    public String getBarCode()
    {
    return barcode;
    }
}