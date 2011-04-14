

package account;

//import pos.SQLhelper;
//import java.lang.ClassNotFoundException;
//import java.sql.SQLException;
/**
    This Address holds the info about an address.
    @author Peter
*/
public class Address
{
     //private int addressID;// this is the tuple's primary key, this is set after the address object is inserted into the db, no human employee should ever have to set this
     private int houseNumber;
     private String streetName;
     private String city;
     private String province;
     private String country;
     private String postalCode;

     //private boolean saved; // this is if the info has been saved in the db or not

 //    private SQLhelper mySQLhelper;

    /**
     * This is the constructor you use that will auto generate the accountID for you (the next available one) after you call saveInDB().
     @param houseNumber the house number of the address
     @param streetName the street name of the address
     @param city the city of the address
     @param province the province of the address
     @param country the country of the address
     @param postalCode the postal code of the address
    */
    public Address(
        int houseNumber,
        String streetName,
        String city,
        String province,
        String country,
        String postalCode)
    {
//		this.saved = false; // this is a new address object, it has not been saved in the database.
		constructAddress(houseNumber, streetName, city, province, country, postalCode);
    }

	/*
     * This is the constructor you use when you get info from the db and create the object (difference from the other constructor is this takes a addressID, the primary key of the Address Table).
     @param addressID the primary key of the tuple in the database
     @param houseNumber the house number of the address
     @param streetName the street name of the address
     @param city the city of the address
     @param province the province of the address
     @param country the country of the address
     @param postalCode the postal code of the address
    
    public Address(
    	int addressID,
        int houseNumber,
        String streetName,
        String city,
        String province,
        String country,
        String postalCode)
    {
		this.addressID = addressID;
		this.saved = true; // all the info in this constructor was retrieved from the database.
		constructAddress(houseNumber, streetName, city, province, country, postalCode);
    }
    */
    /**
    	Common Constructor code
    	@param houseNumber the house number of the address
		@param streetName the street name of the address
		@param city the city of the address
		@param province the province of the address
		@param country the country of the address
		@param postalCode the postal code of the address
    */
    private void constructAddress(
    	int houseNumber,
        String streetName,
        String city,
        String province,
        String country,
        String postalCode)
    {
		this.houseNumber = houseNumber;
		this.streetName = streetName;
		this.city = city;
		this.province = province;
		this.country = country;
		this.postalCode = postalCode;
		//mySQLhelper = new SQLhelper();
	}


    /**
     * This updates the house number of the address; the address should be saved before doing updates to the object.
     * @param houseNumber the house number of the address
     * @throws ClassNotFoundException if JDBC driver is not in CLASSPATH
     * @throws SQLException if if a database access error occurs
     * @throws IllegalStateException if info is not in the database yet, so what are you trying to update?
    public void updateHouseNumber(int houseNumber) throws IllegalStateException, SQLException, ClassNotFoundException
    {
		check("House number");
		this.houseNumber = houseNumber;
		mySQLhelper.updateAddress(this);
    }

     * This updates the house number of the address; the address should be saved before doing updates to the object.
     * @param streetName the street name of the address
     * @throws ClassNotFoundException if JDBC driver is not in CLASSPATH
     * @throws SQLException if if a database access error occurs
     * @throws IllegalStateException if info is not in the database yet, so what are you trying to update?

    public void updateStreetName(String streetName) throws IllegalStateException, SQLException, ClassNotFoundException
    {
		check("Street name");
		this.streetName = streetName;
		mySQLhelper.updateAddress(this);
    }
    
     * This updates the house number of the address; the address should be saved before doing updates to the object.
     * @param city the city of the address
     * @throws ClassNotFoundException if JDBC driver is not in CLASSPATH
     * @throws SQLException if if a database access error occurs
     * @throws IllegalStateException if info is not in the database yet, so what are you trying to update?
    public void updateCity(String city) throws IllegalStateException, SQLException, ClassNotFoundException
    {
		check("City");
		this.city = city;
		mySQLhelper.updateAddress(this);
    }
    
     * This updates the house number of the address; the address should be saved before doing updates to the object.
     * @param province the province of the address
     * @throws ClassNotFoundException if JDBC driver is not in CLASSPATH
     * @throws SQLException if if a database access error occurs
     * @throws IllegalStateException if info is not in the database yet, so what are you trying to update?
    public void updateProvince(String province) throws IllegalStateException, SQLException, ClassNotFoundException
    {
		check("Province");
		this.province = province;
		mySQLhelper.updateAddress(this);
    }
    
     * This updates the house number of the address; the address should be saved before doing updates to the object.
     * @param country the country of the address
     * @throws ClassNotFoundException if JDBC driver is not in CLASSPATH
     * @throws SQLException if if a database access error occurs
     * @throws IllegalStateException if info is not in the database yet, so what are you trying to update?
    public void updateCountry(String country) throws IllegalStateException, SQLException, ClassNotFoundException
    {
		check("Country");
		this.country = country;
		mySQLhelper.updateAddress(this);
    }
    
     * Returns if this Address is already saved in the database.
     * @returns true if the address is saved already
    public boolean isSaved()
    {
	return saved;
    }


    	Updates the postal code for the Address; the address should be saved before doing updates to the object.
    	@param postalCode the postal code of the address
    	@throws ClassNotFoundException if JDBC driver is not in CLASSPATH
    	@throws SQLException if if a database access error occurs
    	@throws IllegalStateException if info is not in the database yet, so what are you trying to update?
    public void updatePostalCode(String postalCode) throws IllegalStateException, SQLException, ClassNotFoundException
    {
		check("Postal code");
		this.postalCode = postalCode;
		mySQLhelper.updateAddress(this);
    }
    
		Checks if this Address is saved.
		@param theVariable the variable name that is wanting to be changed
	private void check(String theVariable) throws IllegalStateException
	{
		if (saved == false)
		{
			throw new IllegalStateException(theVariable + " can not be updated because the address is not in the database");
			// maybe delete above line and save it if it is not saved.
			// saveInDB();
		}
	}


	
		This returns a Address object given a attribute (the primary key)
		@param addressID the primary key of the tuple
		@return the Address object asscociated with the addressID
    public Address getAddress(int addressID) throws IllegalStateException, SQLException, ClassNotFoundException
    {
		return mySQLhelper.getAddress(addressID);
    }

     * Saves this Address in the DB (maybe this should be a private method and be called from the constructor, but if it is in the constructor then you can't instantiate this object if a db connection isn't available).
     * @return  the addressID (the primary key) of the address
    public int saveInDB() throws IllegalStateException, SQLException, ClassNotFoundException
    {
		if (saved == true)
			throw new IllegalStateException("Address already saved");
		int addressID = mySQLhelper.insertAddressTable(this);
		saved = true;
		return addressID;
    }
   */ 
       //
	// getter methods
   
    
    /*
    	Gets the primary key of the Address object.
    	@return the primary key of the row this address is in the database
    	@throws IllegalStateException if the address has not been saved in the database yet
    public int getAddressID() throws IllegalStateException
    {
    	if (saved == false)
    		throw new IllegalStateException("The Address has not been saved in the database");
    	return addressID;
    }
    */
    
    /**
    	Gets the house number of the Address.
    	@return the house number of the Address
    */
    public int getHouseNumber() { return houseNumber; }
    
    /**
    	Gets the street name of the Address.
    	@return the street name of the address
    */
    public String getStreetName() { return streetName; }
    /**
    	Gets the city of the address.
    	@return the city of the address
    */
    public String getCity() { return city; }
    /**
    	Gets the province of the address.
    	@return the province of the address
    */
    public String getProvince() { return province; }
    /**
    	Gets the country of the address.
    	@return the country of the address
    */
    public String getCountry() { return country; }
    /**
    	Gets the postal code of the address.
    	@return the postal code of the address
    */
    public String getPostalCode() { return postalCode; }
    /**
    	A string representation of the Address.
    	@return the address
    */
    public String toString()
    {
    	return  houseNumber + " " + streetName + ", " + city + ", " + province + ", " + postalCode + ", " + country;
    }
}
