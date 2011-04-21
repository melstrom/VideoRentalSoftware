

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
		constructAddress(houseNumber, streetName, city, province, country, postalCode);
    }

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
	}
    
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
    	return  houseNumber + " " + streetName + ",\n" + city + ", " + province + ",\n" + postalCode + ", " + country;
    }
}
