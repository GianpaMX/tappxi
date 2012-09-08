package cmdf2.tappxi.model.bean;

public class Offer {
	private int id;
	private int eta;
	private float fare;
	private Stand stand;
	

	public Offer(int id, int eta, float fare, Stand stand) {
		super();
		this.id = id;
		this.eta = eta;
		this.fare = fare;
		this.stand = stand;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getEta() {
		return eta;
	}
	public void setEta(int eta) {
		this.eta = eta;
	}
	public float getFare() {
		return fare;
	}
	public void setFare(float fare) {
		this.fare = fare;
	}
	public Stand getStand() {
		return stand;
	}
	public void setStand(Stand stand) {
		this.stand = stand;
	}
	
	public String toString() {
		return String.valueOf(eta) + " " + String.valueOf(fare) + " " + stand.toString();
	}
}
