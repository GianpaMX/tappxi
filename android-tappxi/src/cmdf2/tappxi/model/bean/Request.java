package cmdf2.tappxi.model.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class Request {
	private int id;
	private Address start_address;
	private Address end_address;
	private int status;

	public Request(int id, Address start_address, Address end_address,
			int status) {
		super();
		this.id = id;
		this.start_address = start_address;
		this.end_address = end_address;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Address getStart_address() {
		return start_address;
	}

	public void setStart_address(Address start_address) {
		this.start_address = start_address;
	}

	public Address getEnd_address() {
		return end_address;
	}

	public void setEnd_address(Address end_address) {
		this.end_address = end_address;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public static Request fromJSONObject(JSONObject object) throws JSONException {
		return new Request(object.getInt("id"), Address.fromJSONObject(object.getJSONObject("start_address")), new Address(), object.getInt("status"));
	}
}
