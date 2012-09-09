package cmdf2.tappxi.model.bean;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Stand implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private float startFare;
	private int status;
	private Address address;
	private String phone;
	
	public Stand(int id, String name, float startFare, int status,
			Address address, String phone) {
		super();
		this.id = id;
		this.name = name;
		this.startFare = startFare;
		this.status = status;
		this.address = address;
		this.phone = phone;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getStartFare() {
		return startFare;
	}
	public void setStartFare(float startFare) {
		this.startFare = startFare;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String toString() {
		return getName();
	}

	public static Stand fromJSONObject(JSONObject object) throws JSONException {
		return new Stand(object.getInt("id"), object.getString("name"), (float)object.getDouble("start_fare"), object.getInt("status"), Address.fromJSONObject(object.getJSONObject("address")), object.getString("phone"));
	}
}
