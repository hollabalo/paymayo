public class CreditCardBean {
	private int id;
	private String cardnumber;
	private String ownername;
	private int creditlimit;
	private int currentbalance;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setCardnumber(String cardnumber) {
		this.cardnumber = cardnumber;
	}

	public String getCardnumber() {
		return cardnumber;
	}

	public void setOwnername(String ownername) {
		this.ownername = ownername;
	}

	public String getOwnername() {
		return ownername;
	}

	public void setCreditlimit(int creditlimit) {
		this.creditlimit = creditlimit;
	}

	public int getCreditlimit() {
		return creditlimit;
	}

	public void setCurrentbalance(int currentbalance) {
		this.currentbalance = currentbalance;
	}

	public int getCurrentbalance() {
		return currentbalance;
	}

	public String toJsonString() {
		StringBuilder str = new StringBuilder();
		str.append("{");
		str.append("\"cardnumber\" : \"" + cardnumber + "\",");
		str.append("\"ownername\" : \"" + ownername + "\",");
		str.append("\"creditlimit\" : " + creditlimit + ",");
		str.append("\"currentbalance\" : " + currentbalance);
		str.append("}");
		return str.toString();
	}
}