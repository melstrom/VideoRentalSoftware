

package account;

import pos.SQLhelper;

/**
    This Address holds the info about an address.
*/
public class Address
{
     private int addressID;
     private int houseNumber;
     private String streetName;
     private String city;
     private String province;
     private String country;
     private String postalCode;
     
     private boolean saved = false;

     private SQLhelper mySQLhelper;
     private static final String addressTable = "address";
     private static final String addressKey = "addressID";

    /**
     * This is the constructor you use that will auto generate the accountID for you (the next available one)
     * @throws SQLException if sql error.
     * @throws ClassNotFoundException if sql errors.
    */
    public Address(
    //    int addressID,
        int houseNumber,
        String streetName,
        String city,
        String province,
        String country,
        String postalCode) throws SQLException, ClassNotFoundException
    {
	mySQLhelper = new SQLhelper();
//	this.addressID = addresssID;
	this.addressID = 1 + mySQLhelper.getTotalNumberOfRows(addressTable, addressKey);
	this.houseNumber = houseNumber;
	this.streetName = streetName;
	this.city = city;
	this.province = province;
	this.country = country;
	this.postalCode = postalCode;
    }
    /**
     * This is the constructor that accepts a accountID
     */
    public Address(



    /**
     * Saves this Address in the DB
     * @return 
     */
    public void saveInDB() throws IllegalStateException, SQLException, ClassNotFoundException
    {
	if (saved == true)
	    throw new IllegalStateException("Address already saved");
	mySQLhelper.insertAddressTable(this);
	saved = true;
    }

    public Address getAddress(int addressID) throws IllegalStateException, SQLException, ClassNotFoundException
    {
	return mySQLhelper.getAddress(int addresssID);
    }

    /**
     * the address sound be saved before doing updates to the object.
     */
    public void setHouseNumber(String houseNumber) throws IllegalStateException, SQLException, ClassNotFoundException
    {
	if (saved == false)
	    throws new IllegalStateException("Address can not be updated because it is not in the database");
	this.houseNumber = houseNumber;
	mySQLhelper.updateAddress(this);
    }

    public void setStreetName(String streetName) throws IllegalStateException, SQLException, ClassNotFoundException
    {
	if (saved == false)
	    throws new IllegalStateException("Street name can not be updated because it is not in the database");
	this.streetName = streetName;
	mySQLhelper.updateAddress(this);
    }
    public void setCity(String city) throws IllegalStateException, SQLException, ClassNotFoundException
    {
	if (saved == false)
	    throws new IllegalStateException("City can not be updated because it is not in the database");
	this.city = city;
	mySQLhelper.updateAddress(this);
    }
    public void setProvince(String province) throws IllegalStateException, SQLException, ClassNotFoundException
    {
	if (saved == false)
	    throws new IllegalStateException("Province can not be updated because it is not in the database");
	this.province = province;
	mySQLhelper.updateAddress(this);
    }
    public void setCountry(String country) throws IllegalStateException, SQLException, ClassNotFoundException
    {
	if (saved == false)
	    throws new IllegalStateException("Country can not be updated because it is not in the database");
	this.country = country;
	mySQLhelper.updateAddress(this);
    }
    public void setPostalCode(String postalCode) throws IllegalStateException, SQLException, ClassNotFoundException
    {
	if (saved == false)
	    throws new IllegalStateException("Postal code can not be saved because it is not in the database");
	this.postalCode = postalCode;
	mySQLhelper.updateAddress(this);
    }




    
    public String getPostalCode() { return postalCode; }
    public int getAddressID() { return addressID; }
    public int getHouseNumber() { return houseNumber; }
    public String getStreetName() { return streetName; }
    public String getCity() { return city; }
    public String getProvince() { return province; }
    public String getCountry() { return postalCode; }
}
