package cmdf2.tappxi.model.bean;

import com.google.android.maps.GeoPoint;

public class Address {
	private int id;
	private String street;
	private String settlement;
	private String city;
	private String state;
	private String zipCode;
	private GeoPoint geoPoint;
	
	public Address(int id, String street, String settlement, String city,
			String state, String zipCode, GeoPoint geoPoint) {
		super();
		this.id = id;
		this.street = street;
		this.settlement = settlement;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.geoPoint = geoPoint;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getSettlement() {
		return settlement;
	}
	public void setSettlement(String settlement) {
		this.settlement = settlement;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public GeoPoint getGeoPoint() {
		return geoPoint;
	}
	public void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}
}
