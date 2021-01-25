package beans;

public class CustomerType implements Comparable<CustomerType>{
	private String typeName; //zlatni, srebrni, bronzani
	private double discount; //popust(koristi se za obracunavanje cene)
	private int points; //potrebni broj bodova
	
	public CustomerType(String typeName, double discount, int points) {
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

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	@Override
	public String toString() {
		return "CustomerType [typeName=" + typeName + ", discount=" + discount + ", points=" + points + "]";
	}

	@Override
	public int compareTo(CustomerType o) {
		// TODO Auto-generated method stub
		return this.getPoints() - o.getPoints();
	}
}
