package beans;

public class CustomerType {
	private String typeName; //zlatni, srebrni, bronzani
	private int discount; //popust(koristi se za obracunavanje cene)
	private int points; //potrebni broj bodova
	
	public CustomerType(String typeName, int discount, int points) {
		this.typeName = typeName;
		this.discount = discount;
		this.points = points;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
	
}
