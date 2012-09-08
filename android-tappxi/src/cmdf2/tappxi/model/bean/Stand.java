package cmdf2.tappxi.model.bean;

public class Stand {
	private int id;
	private String name;
	private float startFare;
	private int status;
	private Address address;
	
	public Stand(int id, String name, float startFare, int status,
			Address address) {
		super();
		this.id = id;
		this.name = name;
		this.startFare = startFare;
		this.status = status;
		this.address = address;
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
	
	public String toString() {
		return getName();
	}
}
