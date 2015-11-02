import java.sql.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class PaymentDatabase {
	private static PaymentDatabase instance = null;

	private PaymentDatabase() {
	}

	public static PaymentDatabase getInstance() {
		if(instance == null) {
			instance = new PaymentDatabase();
		}
		return instance;
	}

	private Connection getDb() {
		Connection v = null;
		try {
			Class.forName("org.sqlite.JDBC");
			v = DriverManager.getConnection("jdbc:sqlite:payment.db");
			Statement stmt = v.createStatement();
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS creditcards " +
				"( id INTEGER PRIMARY KEY, cardnumber CHAR(16) NOT NULL," +
				" ownername VARCHAR(50), creditlimit INTEGER, currentbalance INTEGER DEFAULT 0);");
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS transactions (date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, cardnumber CHAR(16), amount INTEGER);");
			stmt.close();
		}
		catch(Exception e) {
			v = null;
			e.printStackTrace(); 
		}
		return v;
	}

	public CreditCardBean getCard(String cardnumber) {
		CreditCardBean creditcard = null;
		try {
			creditcard =  new CreditCardBean();
			Connection db = getDb();
			PreparedStatement stmt = db.prepareStatement("SELECT * FROM creditcards WHERE cardnumber = ?;");
			stmt.setString(1, cardnumber);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				creditcard.setId(rs.getInt("id"));
				creditcard.setCardnumber(rs.getString("cardnumber"));
				creditcard.setOwnername(rs.getString("ownername"));
				creditcard.setCreditlimit(rs.getInt("creditlimit"));
				creditcard.setCurrentbalance(rs.getInt("currentbalance"));
			}
			stmt.close();
		}
		catch(Exception e) {
			creditcard = null;
		}
		return creditcard;
	}

	public boolean addCard(String cardnumber, String ownername, int creditlimit) {
		try {
			Connection db = getDb();
			PreparedStatement stmt = db.prepareStatement("INSERT INTO creditcards (cardnumber, ownername, creditlimit) VALUES (?,?,?);");
			stmt.setString(1, cardnumber);
			stmt.setString(2, ownername);
			stmt.setInt(3, creditlimit);
			stmt.executeUpdate();
			stmt.close();
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}

	public boolean updateCard(String cardnumber, String ownername, int creditlimit) {
		try {
			Connection db = getDb();
			PreparedStatement stmt = db.prepareStatement("UPDATE creditcards SET ownername = ?, creditlimit = ? WHERE cardnumber = ?;");
			stmt.setString(1, ownername);
			stmt.setInt(2, creditlimit);
			stmt.setString(3, cardnumber);
			stmt.executeUpdate();
			stmt.close();
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}

	public boolean cancelCard(String cardnumber) {
		try {
			Connection db = getDb();
			PreparedStatement stmt = db.prepareStatement("DELETE FROM creditcards WHERE cardnumber = ?;");
			stmt.setString(1, cardnumber);
			stmt.executeUpdate();
			stmt.close();
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}

	public boolean logTransaction(String cardnumber, int amount) {
		try {
			Connection db = getDb();
			PreparedStatement stmt = db.prepareStatement("INSERT INTO transactions (cardnumber, amount) VALUES (?,?);");
			stmt.setString(1, cardnumber);
			stmt.setInt(2, amount);
			stmt.executeUpdate();
			stmt.close();
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}

	public boolean updateBalance(String cardnumber, int amount) {
		try {
			CreditCardBean cc = getCard(cardnumber);
			if(!(cc.getCreditlimit() >= (cc.getCurrentbalance() + amount))) {
				return false;
			}
			Connection db = getDb();
			PreparedStatement stmt = db.prepareStatement("UPDATE creditcards SET currentbalance = ? WHERE cardnumber = ?;");
			stmt.setInt(1, cc.getCurrentbalance() + amount);
			stmt.setString(2, cardnumber);
			stmt.executeUpdate();
			stmt.close();
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
}