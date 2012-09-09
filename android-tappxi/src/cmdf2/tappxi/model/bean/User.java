package cmdf2.tappxi.model.bean;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String email;
	private float balance;
	private int status;

	public User(int id, String name, String email, float balance, int status) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.balance = balance;
		this.status = status;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public static User fromJSONObject(JSONObject object) throws JSONException {
		return new User(object.getInt("id"), object.getString("name"),
				object.getString("email"), (float) object.getDouble("balance"),
				object.getInt("status"));
	}
}
