/**
* IndividualMovie class
* Sets and gets the individual movie object with price, format, category and bar-code by calling and setting methods.
* @author Legen
* @version 1.0 March 27, 2011
*  matt: removed format, added condition
*/

package inventory;
import java.sql.*;
import jdbconnection.JDBCConnection;
import java.sql.SQLException;

public class IndividualMovie extends GeneralMovie{

   /**
   *	Empty constructor for decendant classes
   */
   public IndividualMovie()
   {
   }	   
   /**
    * The constructor
    * inherits from GeneralMovie
    * takes 4 default attributes
    * @param barcode the number uniquely identifies each movie and is broken into two parts
    * @param category the pricing category for the movie such as new release, or 7 day rental
    * @param price this cost of the rental in cents
    * @param condition the condition of the movie
    */
   public IndividualMovie(String category, int price, String barcode, GeneralMovie movie, String condition)
   {
	super(movie.getSKU(),movie.getTitle(),movie.getActors(),movie.getDirector(), movie.getProducer(),
			movie.getReleaseDate(), movie.getSynopsis(), movie.getGenre(), movie.getRating(), 
				movie.getStudio(), movie.getRetailPriceInCents(), movie.getFormat(), movie.getLength());
	this.condition = condition;
        setPrice(price);
        setCategory(category);
        setBarcode(barcode);
   }

   public IndividualMovie(String SKU, int saleID, String condition) throws SQLException, ClassNotFoundException
   {
       super(SKU);
       String query = "SELECT " + condition + " FROM videoSale WHERE SaleID = " + saleID;
       ResultSet rs = statement.executeQuery(query);
       rs.next();
       this.condition = rs.getString(condition);
   }
   
    private String category;            //The categories for the movies such as new release, 7 day rental ect.
    private int price;                  //The cost of renting the movie in cents      
    private String barcode;             //The unique number code that is on each movie as barcodes
    private String condition;
    public static final int ID_LENGTH = 9;
  

   
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
    protected void setBarcode(String theBarcode)
    {
    this.barcode = theBarcode;
    }
   
   
    /**
    *Get the individual movies barcode which is a unique string 
    *@return barcode
    */
    public String getBarcode()
    {
    return barcode;
    }
   /**
   *  Set condition
   * @param condition the condition of the movie ( / / / /)
   */
    public void setCondition(String condition)
   {
     this.condition = condition;
   }
   /**
   * Get condition
   * @return condition 
   */
    public String getCondition()
   {
     return condition;
   }



    /**
     * This method gets the copy number of the IndividualMovie.  It corresponds
     * to either the saleID or rentalID in the database.  It has package access.
     * @return the copy number of the IndividualMovie object
     */
    String getCopyNum()
    {
        String copyNum = barcode.replaceAll(getSKU(), "");
        return copyNum;
    }
        private void setupConnection() throws SQLException, ClassNotFoundException
    {
        connection = JDBCConnection.getJDBCConnection();
        statement = connection.createStatement();
    }
}