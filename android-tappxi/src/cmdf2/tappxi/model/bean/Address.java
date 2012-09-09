package cmdf2.tappxi.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

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
	
	private String reference;

	public Address(int id, String street, String settlement, String city,
			String state, String zipCode, GeoPoint geoPoint, String reference) {
		super();
		this.id = id;
		this.street = street;
		this.settlement = settlement;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.latitude = geoPoint.getLatitudeE6();
		this.longitude = geoPoint.getLongitudeE6();
		this.reference = reference;
	}

	public Address(String street, String settlement, String city, String state,
			String zipCode, GeoPoint geoPoint, String reference) {
		this(0, street, settlement, city, state, zipCode, geoPoint, reference);
	}

	public Address(String street, GeoPoint geoPoint, String reference) {
		this(0, street, "", "", "", "", geoPoint, reference);
	}

	public Address() {
		this(0, "", "", "", "", "", new GeoPoint(0, 0), "");
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
		return getStreet() + ", " + getSettlement() + ", " + getCity() + ", "
				+ getState() + ", " + getZipCode();
	}

	public Collection<NameValuePair> getNameValuePairs(String prefix) {
		Collection<NameValuePair> collection = new ArrayList<NameValuePair>();
		collection.add(new BasicNameValuePair(prefix + "street", getStreet()));
		collection.add(new BasicNameValuePair(prefix + "settlement",
				getSettlement()));
		collection.add(new BasicNameValuePair(prefix + "city", getCity()));
		collection.add(new BasicNameValuePair(prefix + "state", getState()));
		collection
				.add(new BasicNameValuePair(prefix + "zip_code", getZipCode()));
		collection.add(new BasicNameValuePair(prefix + "latitude", String
				.valueOf(latitude)));
		collection.add(new BasicNameValuePair(prefix + "longitude", String
				.valueOf(longitude)));
		collection.add(new BasicNameValuePair(prefix + "reference", getReference()));

		return collection;
	}

	public Collection<NameValuePair> getNameValuePairs() {
		return getNameValuePairs("");
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public static Address fromJSONObject(JSONObject object) throws JSONException {
		return new Address(object.getInt("id"), object.getString("street"),
				object.getString("settlement"), object.getString("city"),
				object.getString("state"), object.getString("zip_code"),
				new GeoPoint(object.getInt("latitude"),
						object.getInt("longitude")), object.getString("reference"));
	}

}
