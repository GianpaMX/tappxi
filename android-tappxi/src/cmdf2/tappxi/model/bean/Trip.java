package cmdf2.tappxi.model.bean;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

public class Trip {
	private int id;
	private Movement movement;
	private Request request;
	private Offer offer;
	private float fare;
	private int status;
	private Taxi taxi;
	
	public Trip(int id, Movement movement, Request request, Offer offer,
			float fare, int status, Taxi taxi) {
		super();
		this.id = id;
		this.movement = movement;
		this.request = request;
		this.offer = offer;
		this.fare = fare;
		this.status = status;
		this.taxi = taxi;
	}
	
	public static Trip fromJSONObject(JSONObject object) throws JSONException, ParseException {
		return new Trip(object.getInt("id"), Movement.fromJSONObject(null), Request.fromJSONObject(object.getJSONObject("request")), Offer.fromJSONObject(object.getJSONObject("offer")), (float)object.getDouble("offer"), object.getInt("status"), Taxi.fromJSONObject(object.getJSONObject("taxi")));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Movement getMovement() {
		return movement;
	}

	public void setMovement(Movement movement) {
		this.movement = movement;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}

	public float getFare() {
		return fare;
	}

	public void setFare(float fare) {
		this.fare = fare;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Taxi getTaxi() {
		return taxi;
	}

	public void setTaxi(Taxi taxi) {
		this.taxi = taxi;
	}
}
