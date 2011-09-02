/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inventory;
import store.VRSConnection;
import sales.InvoiceItem;
import pricing.PriceScheme;
import java.sql.*;
import java.io.*;
import java.util.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 *
 * @author kevin
 */
public class Copy implements InvoiceItem/*cross package/coupling*/
{
    //instance data field:
    private int sku;
    private String title;
    private String format;
    private String condition;
    private String category;
    private String status;
    private int upc;
    private MovieInfo movieInfo;

    //class static data field:
    protected static final String TABLE_NAME = " Copies_ ";
    protected static final String PK_NAME = " Copies_.SKU_ ";
    public static final String AVAILABLE_STATUS = "available";
    public static final String RENTED_OUT_STATUS = "rented";
    public static final String DAMAGED_STATUS = "overdue";
    public static final String GOOD_CONDITION = "good";
    public static final String DAMAGED_CONDITION = "damaged";
    //fields below are only valid if all the columns are in your result set
    public static final int SKU_INDEX = 1;
    public static final int UPC_INDEX = 2;
    public static final int CONDITION_INDEX = 3;
    public static final int CATEGORY_INDEX = 4;
    public static final int STATUS_INDEX = 5;

    /* Method signiture according to the class diagram
    public Copy(int sku, int upc, String category)
    {

    }
     */

    /**
     * This constructor is for the use of getting data of an existing copy in
     * the database
     * @param targetSKU
     * @throws SQLException
     * @throws IOException
     */
    public Copy(int targetSKU)
            throws SQLException, IOException
    {
        VRSConnection conn = VRSConnection.getInstance();
        
        try
        {
            //needs to confirm the select command with 2 tables (other than 
            //select join) while the database is setup.
            final String[] TABLES = {Copy.TABLE_NAME, MovieInfo.TABLE_NAME};
            final String[] COLUMNS = { Copy.TABLE_NAME + ".*",
                                       MovieInfo.TABLE_NAME + ".title_",
                                       MovieInfo.TABLE_NAME + ".format_"};
            final String CONDITION = Copy.PK_NAME + " = " + targetSKU + " AND "
                                     + MovieInfo.PK_NAME + " = "
                                     + Copy.TABLE_NAME + ".UPC_ ";
            ResultSet result = conn.select(TABLES, COLUMNS, CONDITION, null);

            //condition checking of row found will be ignored, since an
            //SQLException will be thrown if that is the case.
            boolean throwAway = result.next();//this statement move the pointer
                                              //to the row that has been found.
                                              //varibale throwAway has no use,
                                              //but still wanted to be caught.
            this.sku = result.getInt(Copy.SKU_INDEX);
            this.upc = result.getInt(Copy.UPC_INDEX);
            this.condition = result.getString(Copy.CONDITION_INDEX);
            this.category = result.getString(Copy.CATEGORY_INDEX);
            this.status = result.getString(Copy.STATUS_INDEX);
            this.title = result.getString(Copy.STATUS_INDEX + 1);//1st column
                                                                 //after status
            this.format = result.getString(Copy.STATUS_INDEX + 2);//2nd column
                                                                  //after status
            
            this.movieInfo = null;//temporary leave it null until futhur needs
        }
        catch(SQLException expt)
        //refining exception
        {
            if(expt.getMessage().contains("not found"))
            //row not found error:
                throw new IOException("Copy with SKU \"" + targetSKU + "\" "
                                      + "cannot be found.");
            //other error:
            throw expt;

        }
    }

    /**
     * This constructor is for the service of adding new copy of an existing
     * movie.
     * @param upc
     * @param category
     * @throws SQLException
     * @throws IOException
     */
    public Copy(int upc, String category)
            throws SQLException, IOException
    {
        VRSConnection conn = VRSConnection.getInstance();
        final String[] TABLES = {MovieInfo.TABLE_NAME};
        final String CONDITION = MovieInfo.PK_NAME + " = " + upc;
        try
        {
            ResultSet result = conn.select(TABLES, null, CONDITION, null);
            result.next();//this statement will must be executed properly since
                          //an empty result set will cause an SQLException
                          //be thrown and be caught

            movieInfo = new ShortMovieInfo(result);
            this.title = movieInfo.getTitle();
            this.format = movieInfo.getFormat();
            this.condition = Copy.GOOD_CONDITION;
            this.category = category;
            this.status = Copy.AVAILABLE_STATUS;
            this.upc = upc;
        }
        catch(SQLException expt)
        {
            if(expt.getMessage().contains("not found"))//UNSURE
                throw new IOException("The movie with UPC \"" + upc + "\"cannot"
                        + " be found.");
            throw expt;
        }
    }

    public int record()
            throws IOException, SQLException
    {
        if(this.upc <= 0 || !this.condition.equals(Copy.GOOD_CONDITION) ||
           this.category.isEmpty() || !this.status.equals(Copy.AVAILABLE_STATUS))
            throw new IOException("Information of this copy is incorrect.");

        VRSConnection conn = VRSConnection.getInstance();
        final String[] COLUMNS = {
                                    " UPC_ ",
                                    " condition_ ",
                                    " category_ ",
                                    " status_ "
                                 };
        final String[] VALUES = {
                                    "" + this.upc,
                                    this.condition,
                                    this.category,
                                    this.status
                                };
        int rowInsert = conn.insert(Copy.TABLE_NAME, COLUMNS, VALUES, null);

        //return the new auto-generated sku to the calling function:
        final String[] MAX_TABLES = {Copy.TABLE_NAME};
        final String[] MAX_COLUMN = {"MAX(" + Copy.PK_NAME + ")"};
        final String MAX_CONDITION = " UPC_ = " + this.upc;
        ResultSet maxSKUResult = conn.select(MAX_TABLES, MAX_COLUMN,
                                             MAX_CONDITION, null);

        this.sku = maxSKUResult.getInt(1);//The result set contains only one
                                          //columns
        return this.sku;
    }

    public String getName()
    {
        return this.sku + ": " + this.title + "\t" + this.format + "\t" +
               this.category;
    }

    public int getPrice()
            throws NotBoundException, MalformedURLException, RemoteException
    {
        PriceScheme priceScheme = (PriceScheme) Naming.lookup
                                                           ("PriceScheme");
        return priceScheme.getPrice(this.format, this.category);
    }

    public void checkOut(int custID)
            throws AvailabilityException, SQLException, NotBoundException,
                   MalformedURLException, RemoteException
    {
        if(!this.status.equals(Copy.AVAILABLE_STATUS))
        //condition checking
            throw new AvailabilityException("This copy is not available accord"
                                            + "ing to database record.");

        VRSConnection conn = VRSConnection.getInstance();
        
        try
        {
            //update the row in Copy_
            final String[] SETTING = {Copy.TABLE_NAME + ".status = " +
                                     Copy.RENTED_OUT_STATUS};
            final String CONDTION = Copy.PK_NAME + " = " + this.sku;
            int updateCount = conn.update(Copy.TABLE_NAME, SETTING, CONDTION,
                                          null);//should return 1. returned
                                                //value has no used, but still
                                                //wanted to be caught
        
            //insert a row in the Rented_Out_Copies
            final java.sql.Date SQL_TODAY = new
                    java.sql.Date(Calendar.getInstance()//return Calendar object
                                          .getTime()//returns java.util.Date
                                          .getTime()//returns long int
                                          );

            final Calendar RETURN_CAL = Calendar.getInstance();
            PriceScheme priceScheme = (PriceScheme) Naming.lookup
                                                           ("PriceScheme");
            RETURN_CAL.add(Calendar.DATE,
                           priceScheme.getRentalPeriod(this.category));

            final java.sql.Date SQL_RETURN_DATE = new
                    java.sql.Date(RETURN_CAL.getTime()//returns util.Date
                                                 .getTime()//returns long int
                                                 );
            final String[] INSERT_VALUES = {
                                            "" + this.sku,
                                            "" + custID,
                                            SQL_TODAY.toString(),
                                            SQL_RETURN_DATE.toString()
                                           };

            int insertedRow = conn.insert(RentedCopy.TABLE_NAME, null, 
                                          INSERT_VALUES, null);
        }
        catch(SQLException expt)
        //refining the exception message
        {
            throw new SQLException("Database error. Please try again. \nIf this"
                                   + " message still appears, please contact "
                                   + "the technical support.\nSystem error mes"
                                   + "sage: " + expt.getMessage());
        }
    }
}
