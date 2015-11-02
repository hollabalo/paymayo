import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.json.*;

public class CardServlet extends HttpServlet {
	private final String contentType = "application/json";
	private final String failedMessage = "{ \"code\" : \"300\", \"message\" : \"Failed\" }";
	private final String successMessage = "{ \"code\" : \"200\", \"message\" : \"Success\" }";

	public void init() throws ServletException {}
	public void destroy() {}
	public void doGet(HttpServletRequest request, 
		    HttpServletResponse response) throws ServletException, IOException {
		PaymentDatabase db = PaymentDatabase.getInstance();
		response.setContentType(contentType);
		PrintWriter out = response.getWriter();
		System.out.println(request.getContentType());
		if(request.getContentType() == null) {
			out.println(failedMessage);
			return;
		}
		if(!request.getContentType().equals(contentType)) {
			out.println(failedMessage);
			return;
		}
		if(request.getPathInfo() == null) {
			out.println(failedMessage);
			return;
		}
		String[] s = request.getPathInfo().split("/");
		if(s == null) {
			out.println(failedMessage);
			return;
		}
		if(s.length < 2) {
			out.println(failedMessage);
			return;
		}
		if(s[1].equals("")) {
			out.println(failedMessage);
			return;
		}
		try {
			CreditCardBean cc = db.getCard(s[1].toString());
			if(cc != null) {
				out.println(cc.toJsonString());
			}
			else {
				out.println(failedMessage);
			}
		}
		catch(Exception e) {
			out.println(failedMessage);
		}
	}
	public void doPut(HttpServletRequest request, 
		    HttpServletResponse response) throws ServletException, IOException {
		PaymentDatabase db = PaymentDatabase.getInstance();
		response.setContentType(contentType);
		PrintWriter out = response.getWriter();
		if(!request.getContentType().equals(contentType)) {
			out.println(failedMessage);
			return;
		}
		if(request.getPathInfo() == null) {
			out.println(failedMessage);
			return;
		}
		String[] s = request.getPathInfo().split("/");
		if(s == null) {
			out.println(failedMessage);
			return;
		}
		if(s.length < 2) {
			out.println(failedMessage);
			return;
		}
		if(s[1].equals("")) {
			out.println(failedMessage);
			return;
		}
		String body = ResponseReader.getBody(request.getReader());
		JsonReader reader = Json.createReader(new StringReader(body));
		JsonObject object = reader.readObject();
		if(object.get("ownername") == null ||
				object.get("creditlimit") == null) {
			out.println(failedMessage);
			return;
		}
		try {
			String cardnumber = s[1].toString();
			String ownername = object.getString("ownername");
			int creditlimit = object.getInt("creditlimit");
			if(db.updateCard(cardnumber, ownername, creditlimit)) {
				out.println(successMessage);
			}
			else {
				out.println(failedMessage);
			}
		}
		catch(Exception e) {
			out.println(failedMessage);
		}
	}
	public void doPost(HttpServletRequest request, 
		    HttpServletResponse response) throws ServletException, IOException {
		PaymentDatabase db = PaymentDatabase.getInstance();
		response.setContentType(contentType);
		PrintWriter out = response.getWriter();
		if(!request.getContentType().equals(contentType)) {
			out.println(failedMessage);
			return;
		}
		String body = ResponseReader.getBody(request.getReader());
		JsonReader reader = Json.createReader(new StringReader(body));
		JsonObject object = reader.readObject();
		if(object.get("cardnumber") == null ||
				object.get("ownername") == null ||
				object.get("creditlimit") == null) {
			out.println(failedMessage);
			return;
		}
		try {
			String cardnumber = object.getString("cardnumber");
			String ownername = object.getString("ownername");
			int creditlimit = object.getInt("creditlimit");
			if(db.addCard(cardnumber, ownername, creditlimit)) {
				out.println(successMessage);
			}
			else {
				out.println(failedMessage);
			}
		}
		catch(Exception e) {
			out.println(failedMessage);
		}
	}
	public void doDelete(HttpServletRequest request, 
		    HttpServletResponse response) throws ServletException, IOException {
		PaymentDatabase db = PaymentDatabase.getInstance();
		response.setContentType(contentType);
		PrintWriter out = response.getWriter();
		if(!request.getContentType().equals(contentType)) {
			out.println(failedMessage);
			return;
		}
		if(request.getPathInfo() == null) {
			out.println(failedMessage);
			return;
		}
		String[] s = request.getPathInfo().split("/");
		if(s == null) {
			out.println(failedMessage);
			return;
		}
		if(s.length < 2) {
			out.println(failedMessage);
			return;
		}
		if(s[1].equals("")) {
			out.println(failedMessage);
			return;
		}
		try {
			String cardnumber = s[1].toString();
			if(db.cancelCard(cardnumber)) {
				out.println(successMessage);
			}
			else {
				out.println(failedMessage);
			}
		}
		catch(Exception e) {
			out.println(failedMessage);
		}
	}
}