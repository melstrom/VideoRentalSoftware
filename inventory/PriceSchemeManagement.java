package inventory;
import java.sql.*;
import java.io.IOException;
//import JDBCConnection;//kristan's stuff

/**
*This is the control object for the PriceScheme class. It hold a PriceScheme object for quick reference for prices.
*Every time there is a valid change request such as adding a new category, a new format, or setting price for specific
*category and format, the attribute PRICE_SCHEME will be re-constructed again. Because the behavior of those 
*methods directly change the PriceSchem table in the database.  Keep in mind that the classPriceShceme is just a 
*entity class.
*For the PriceSchemeManagement, PriceScheme and for IndividualMovie, category and format pair up implicitly define 
*the price.
*@author kevin
*/
public class PriceSchemeManagement
{
	/**
	*The ordinary constructor. It will initiate the PRICE_SCHEME attribute.
	*/
	public PriceSchemeManagement()
			throws SQLException
	{
		PRICE_SCHEME = new PriceScheme();
	}

	/**
	*This method will return the price of a movie that with valid category name and format name specified.
	* And this should be the only way to get a price.
	*IMPORTANT FOR DEVELOPERS: Situation like "NewRelease" is passed to here, but inside the PriceScheme 
	*table, the category name is "New_Release" may happen!!! So, before you implementing your class, double check
	*the naming scheme of tables among the database! 
	*If you wonder why it takes strings as parameter but not int? Because there is no gaurenteed the order of category
	*in the database. (For more info, please look at the documentation of the PriceScheme class.)
	*@param String cat The name of the category
	*@param String form The name of the format.
	*@return int the price with the parameters specified.
	*@throws IOException If cat and/or form cannot be found in the PriceScheme object, this exception will be
	*thrown.
	*/
	public int getPrice(String cat, String form)
			throws IOException
	{
		int row = 0;
		int col = 0;
		boolean isCatCorrect = false;
		boolean isFormCorrect = false;
		
		//examine the correctness of the input cat
		for(;row <PRICE_SCHEME.getNumOfCategory(); row++)
			if(PRICE_SCHEME.getCategoryName(row).equals(cat))
			{
				isCatCorrect = true;
				break;
			}
		//examine the correctness of the input form
		for(; PRICE_SCHEME.getNumOfFormats(); col++)
			if(PRICE_SCHEME.getFormatName(col).equals(form))
			{
				isFormCorrect = true;
				break;
			}
		
		if(isCatCorrect && isFormCorrect)
		//when both the string cat and form exist in the array category and format
			PRICE_SCHEME.getPrice(row, col);
		else
		//if any or both of them is false
			throw new IOException("Name of the Category or/and Format is not correct.");
	}
	
	/**
	* This method should be used for testing purpose. 
	*@param  int row the index of the row in the array attribute of PRICE_SCHEME
	*@param int col the index of column in the array attribute of PRICE_SCHEME
	*@return int the price
	*/
	public int getPrice(int row, int col)
	{
		return PRICE_SCHEME.getPrice(row, col);
	}
	
	/**
	*This method will set particular value in the database with valid category name and format
	*name given.
	*@post the value at the particular cell will changed to priceInCents
	*@param String cat the category name
	*@param String form the format name
	*@param int priceInCents the new price
	*@throws IOException  If cat and/or form cannot be found in the PriceScheme object, this exception will be
	*thrown.
	*@throws SQLException database error.
	*/
	public void setPrice(String cat, String form, int priceInCents)
			throws IOException, SQLException
	{
		if(priceInCents < 0)
			throw new IOException("Price cannot be smaller than 0.");
		boolean isCatCorrect = false;
		boolean isFormCorrect = false;
		//examine the correctness of the input cat
		for(int row = 0; row <PRICE_SCHEME.getNumOfCategory(); row++)
			if(PRICE_SCHEME.getCategoryName(row).equals(cat))
			{
				isCatCorrect = true;
				break;
			}
		//examine the correctness of the input form
		for(int col = 0; PRICE_SCHEME.getNumOfFormats(); col++)
			if(PRICE_SCHEME.getFormatName(col).equals(form))
			{
				isFormCorrect = true;
				break;
			}
		
		if(isCatCorrect && isFomrCorrect)
		{
			/*
			 *Kristan's stuff here
			 */
			conn.getConnection();
			try
			{
				PreparedStatement stat = conn.prepareStatement("UPDATE PriceScheme "
					+ " SET "+ form + " =" + priceInCents + "WHERE Category_Name LIKE "
					+ cat + ";");//mySQL command for setting price for paritcular format under 
							 //particular category.
				stat.executeUpdate();
				//re-construct the PriceScheme object after change.
				PRICE_SCHEME = new PriceScheme();
			}
			finally
			{
				conn.close();
			}
		}
		else
			throw new IOException("Category and/or Format cannot be found in the database.");
	}
	
	/**
	*This method will add a new category to the PriceScheme table in the database. BUT, all the price for this 
	*category regarding to each format is not defined yet. 
	*For example, originally, it was:
	*
	*[Category_Name]		[VHS]		[DVD]		[Blu-ray]
	*New Release			200		500		800
	*7-Day				150		350		650
	*
	*Then if I call addCategory("go to bed"), it will becomes:
	*
	*[Category_Name]		[VHS]		[DVD]		[Blu-ray]
	*New Release			200		500		800
	*7-Day				150		350		650
	*go to bed									
	*
	*So, the UI guy (Albert) should design the GUI to guild the manager to set the price for the empty columns.
	*@post a new record is added to the PriceScheme table in the database, but the prices are not set up yet.
	*@param String newCat the name of the new category
	*@throws IOException If the newCat has already existed in the PriceScheme table, this exception.
	*@throws SQLException Database error.
	*/
	public void addCategory(String newCat)
			throws IOException, SQLException
	{
		boolean catExist = false;
		for(int i = 0; i < PRICE_SCHEME.getNumOfCategory(); i ++)
			if(PRICE_SCHEME.getCategoryName(i).equals(newCat))
			{
				catExist = true;
				break;
			}
		if(catExist)
			throw new IOException("The category already exists in the database.");
		else
		{
			/*
			 *Kristan' stuff
			 */
			conn.getConnection();
			try
			{
				PreparedStatement stat = conn.prepareStatement("INSERT INTO PriceScheme "
						+ " Category_Name = " + newCat + ";");
				stat.execute();
				PRICE_SCHEME = new PriceScheme();
			}
			finally
			{
				conn.close();
			}
		}
	}
	
	
	/**
	*This method is similar to the addCategory() method that only adds the a new column to the PriceScheme table in
	*the database, but prices are not specified after this method.
	*GUI guy should guild the manager to set the missing prices.
	*@post a new column is added to the table.
	*@param newForm the name of the new format.
	*@throws IOException if the newForm has already existed in the  PriceScheme table.
	*@throws SQLException database error.
	*/
	public void addFormat(String newForm)
			throws IOException, SQLException
	{
		boolean formExist = false;
		for(int i = 0; i < PRICE_SCHEME.getNumOfFormats(); i++)
			if(PRICE_SCHEME.getFormatName(i).equals(newForm))
			{
				formExist = true;
				break;
			}
		if(formExist)
			throw new IOException("The format already exists in the database.");
		else
		{
			/*
			 * Kristan's stuff
			 */
			conn.getConnection();
			try
			{
				PreparedStatement stat = conn.prepareStatement("ALTER TABLE PriceScheme "
						+ "ADD " + newForm + " INT;");
				stat.execute();
				PRICE_SCHEME = new PriceScheme();
			}
			finally
			{
				conn.close();
			}
		}
	}
	
	/**
	*This method delete a category and all the prices that is associate with it.
	*@post the number of record in the PriceScheme table is 1 less than before.
	*@param String cat the name of the category that  wanted to be deleted.
	*@throws IOException If the category name in the parameter does not 
	*exist in the PriceScheme.
	*@throws SQLException database error.
	*/
	public void removeRecord(String cat)
		throws IOException, SQLException
	{
		boolean isCatCorrect = flase;
		for(int i = 0; i < PRICE_SCHEME.getNumOfCategory; i++)
			if(PRICE_SCHEME.getCategoryName(i).equals(cat))
			{
				isCatCorrect = true;
				break;
			}
		if(isCatCorrect)
		{
			/*
			*Kristan's stuff here
			*/
			conn.getConnection();
			try
			{
				PreparedStatement stat = conn.prepareStatement("DELETE  FROM PriceScheme "
						+ "WHERE Category_Name LIKE" + cat);
				stat.execute();
			}
			finally
			{
				conn.close();
			}
		}
		else
			throw new IOException("The category name does not exist in the database.");
	}
	
	/**
	*This method will remove a vertical column in the PriceScheme table by the 
	*parameter specified.
	*@post the number of column in the PriceSchem table will be 1 less than before.
	*@param String form the name of the format.
	*@throws IOException if the "form" in the parameter does not exist
	*@throws SQLException database error.
	*/
	public void removeFormat(String form)
	{
		boolean isFormCorrect = flase;
		for(int i = 0; i < PRICE_SCHEME.getNumOfFormat(); i++)
			if(PRICE_SCHEME.getFormatName(i).equals(form))
			{
				isFormCorrect = true;
				break;
			}
		if(isFormCorrect)
		{
			/*
			*Kristan's stuff here
			*/
			conn.getConnection();
			try
			{
				PreparedStatement stat = conn.prepareStatement("ALTER TABLE PriceScheme "
						+ "DROP "+ form);
				stat.execute();
			}
			finally
			{
				conn.close();
			}
		}
		else
			throw new IOException("The format name does not exist in the database.");
	}
	
	private PriceScheme PRICE_SCHEME;
}
