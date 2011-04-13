import java.io.IOException;
import java.sql.*;

/**
 This class is the control object of the class PriceScheme.
 It provides services of getting price, setting price, adding a new price,
 and removing an existing price.

 Every time that changes are made to the price scheme, the pricing table and only
 the pricing table will be changed. If the manager remove a price for format VHS,
 for example, only that specific record in the pricing table will be remove. 'VHS'
 in the format table will not.
 * @author kevin
 */
public class PriceSchemeManagement
{
    public PriceSchemeManagement()
            throws SQLException
    {
        PRICE_SCHEME = new PriceScheme();
    }

    /**
     * Return the price in cents with the category name and format name provided.
     * @param cat the name of the category
     * @param form the name of the format
     * @return the price related to the category and format
     * @throws IOException Either/both of the name of the category and format is
     * not stored in the PRICE_SCHEME.
     */
    public int getPrice(String cat, String form)
            throws IOException
    {
        int x = PRICE_SCHEME.getIndexOfCategory(cat.trim().toLowerCase());
        int y = PRICE_SCHEME.getIndexOfFormat(form.trim().toLowerCase());
        if(x < 0 || y < 0 )
            throw new IOException("The name of the category or format "
                    + "cannot be recognized.");
        return PRICE_SCHEME.getPrice(x, y);
    }

    /**
     * Changing the existing price for a category and format.
     * @param cat the name of the category.
     * @param form the name of the format.
     * @param price the new price
     * @pre the name of the category and format must be in the instance field
     * of PRICE_SCHEME, and price must not less than 0.
     * @post the database will be updated, the PRICE_SCHEME will be re-constructed.
     * @throws IOException the parameters cannot meet the requirement of the pre-
     * condition.
     * @throws SQLException database error.
     */
    public void setPrice(String cat, String form, int price)
            throws IOException, SQLException
    {
        //examine whether the parameters are valid.
        int x = PRICE_SCHEME.getIndexOfCategory(cat.trim().toLowerCase());
        int y = PRICE_SCHEME.getIndexOfFormat(form.trim().toLowerCase());
        if(x < 0 || y < 0 || price < 0)
            throw new IOException("The category name or format or price is not"
                    + "valid");

        JDBCConnection conn = new JDBCConnection();
        conn.getConnction();
        try
        {
            PreparedStatement stat = conn.prepareStatement("UPDATE pricing "
                    + "SET price = " + price
                    + "WHERE catagory LIKE '" + cat.trim().toLowerCase() + "' AND "
                    + "format LIKE '" + form.trim().toLowerCase() + "';");
            boolean successful = stat.execute();
            if(successful)
                PRICE_SCHEME = new PriceScheme();
            else
                throw new SQLException("Setting price fails");
        }
        finally
        {
            conn.close();
        }
    }

    /**
     * Adding a new price, in other words, adding a new record to the database
     * @param cat the name of the category
     * @param form the name of the format
     * @param price the price
     * @pre the parameter price must not less than 0
     * @pre there is not price for the category and format provided
     * @pre the category and format provided must be in the relevant table in the
     * database.
     * @post the pricing table in the database will be updated, and the PRICE_SCHEME
     * will be re-constructed.
     * @throws SQLException database error.
     * @throws IOException any of the pre-condition is not satisfied.
     */
    public void addPrice(String cat, String form, int price)
            throws SQLException, IOException
    {
        if(price < 0)
            throw new IOException("Price cannot be less than 0.");

        if(PRICE_SCHEME.getIndexOfCategory(cat.trim().toLowerCase()) >=0 ||
                PRICE_SCHEME.getIndexOfFormat(form.trim().toLowerCase()) >= 0)
            throw new IOException("Price already exist.");

        if(isValid(cat.trim().toLowerCase(), form.trim().toLowerCase()))
            //if every parameter is valid, then adding the new record to the
            //database
        {
            JDBCConnection conn = new JDBCConnection();
            conn.getConnection();
            try
            {
                PreparedStatement stat = conn.prepareStatement("INSERT INTO pricing "
                    + "VALUES ("+ price + ", '" + cat.trim().toLowerCase()
                    + "', '" + form.trim().toLowerCase() + ");");
                boolean successful = stat.execute();
                if(successful)
                    PRICE_SCHEME = new PriceScheme();
                else
                    throw new SQLException("Fail to update the database.");
            }
            finally
            {
                conn.close();
            }
        }
    }

    /**
     * This method will remove a record in the database with the name of the category
     * and format provided.
     * @param cat the name of the category
     * @param form the name of the form
     * @pre the category and format provided must exist in the relevant tables in
     * the database.
     * @post the pricing table in the database will be updated, and PRICE_SCHEME
     * will be re-constructed.
     * @throws SQLException database error.
     */
    public void removePrice(String cat, String form)
            throws SQLException
    {
        if(isValid(cat.trim().toLowerCase(), form.trim().toLowerCase()))
        {
            JDBCConnection conn = new JDBCConnection();
            conn.getConnection();
            try
            {
                PreparedStatement stat = conn.prepareStatement("DELEETE FROM pricing "
                        + "WHERE catagory LIKE '" + cat.trim().toLowerCase() + "' AND "
                        + "format LIKE '" + form.trim().toLowerCase() + "';");
                boolean foo = stat.execute(); // beware that even if I delete a
                                              // "row" that is not exist in the
                                              // table, I will still get the update
                                              // count, which means the stat.execute()
                                              // will return false as same as
                                              // "no result" state.
                PRICE_SCHEME = new PriceScheme();
            }
            finally
            {
                conn.close();
            }
        }
    }

    /**
     * A private helper method to test whether the name category exist in the
     * catagories table and whether the name of the format exist in the format
     * table.
     * @param cat the name of the category
     * @param form the name of the format
     * @return true if they both do exist. if any/both of them does not exist,
     * there will be an SQLException thrown
     * @throws SQLException database error, or cat and form does not exist in the
     * relevant tables in the database.
     */
    private boolean isValid(String cat, String form)
            throws SQLException
    {
        JDBCConnection conn = new JDBCConnection();
        conn.getConnection();
        try
        {
            PreparedStatement stat = conn.prepareStatement("SELECT * FROM catagories "
                    + "WHERE catagory LIKE '" + cat + "';");
            boolean hasResult = stat.execute();
            if(!hasResult)
                throw new SQLException("Category provided not in the database.");
            stat = conn.prepareStatement("SELECT * FROM formats "
                    + "WHERE format LIKE '" + form + "';");
            hasResult = stat.execute();
            if(!hasResult)
                throw new SQLException("Format provided not in the database.");
            return true;
        }
        finally
        {
            conn.close();
        }
    }
    
    private PriceScheme PRICE_SCHEME;
}
