package cmdf2.tappxi.model.bean;

import java.io.Serializable;

import com.google.android.maps.GeoPoint;

public class Address implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String street;
	private String settlement;
	private String city;
	private String state;
	private String zipCode;
	private int latitude;
	private int longitude;
	
	
	public Address(int id, String street, String settlement, String city,
			String state, String zipCode, GeoPoint geoPoint) {
		super();
		this.id = id;
		this.street = street;
		this.settlement = settlement;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.latitude = geoPoint.getLatitudeE6();
		this.longitude = geoPoint.getLongitudeE6();
	}
	public Address(String street, String settlement, String city,
			String state, String zipCode, GeoPoint geoPoint) {
		this(0, street, settlement, city, state, zipCode,geoPoint);
	}
	public Address(String street, GeoPoint geoPoint) {
		this(0, street, "", "", "", "",geoPoint);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStreet() {
		return (street != null ? street : "").trim();
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getSettlement() {
		return (settlement != null ? settlement : "").trim();
	}
	public void setSettlement(String settlement) {
		this.settlement = settlement;
	}
	public String getCity() {
		return (city != null ? city : "").trim();
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return (state != null ? state : "").trim();
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZipCode() {
		return (zipCode != null ? zipCode : "").trim();
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public GeoPoint getGeoPoint() {
		return new GeoPoint(latitude, longitude);
	}
	public void setGeoPoint(GeoPoint geoPoint) {
		this.latitude = geoPoint.getLatitudeE6();
		this.longitude = geoPoint.getLongitudeE6();
	}
	
	public String toString() {
		return getStreet() + ", " + getSettlement() + ", " + getCity() + ", " + getState() + ", " + getZipCode();
	}
}
