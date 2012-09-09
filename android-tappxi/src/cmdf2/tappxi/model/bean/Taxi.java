package cmdf2.tappxi.model.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class Taxi {
	private int id;
	private Stand stand;
	private String name;
	private String tagNumber;
	private int status;

	public Taxi(int id, Stand stand, String name, String tagNumber, int status) {
		super();
		this.id = id;
		this.stand = stand;
		this.name = name;
		this.tagNumber = tagNumber;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Stand getStand() {
		return stand;
	}

	public void setStand(Stand stand) {
		this.stand = stand;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTagNumber() {
		return tagNumber;
	}

	public void setTagNumber(String tagNumber) {
		this.tagNumber = tagNumber;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public static Taxi fromJSONObject(JSONObject object) throws JSONException {
		return new Taxi(object.getInt("id"), Stand.fromJSONObject(object.getJSONObject("stand")), object.getString("name"), object.getString("tagNumber"), object.getInt("status"));
	}
}
