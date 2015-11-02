import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.json.*;

public class TransactionServlet extends HttpServlet {
	private final String contentType = "application/json";
	private final String failedMessage = "{ \"code\" : \"300\", \"message\" : \"Failed\" }";
	private final String successMessage = "{ \"code\" : \"200\", \"message\" : \"Success\" }";

	public void init() throws ServletException {}
	public void destroy() {}
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
				object.get("amount") == null) {
			out.println(failedMessage);
			return;
		}
		try {
			String cardnumber = object.getString("cardnumber");
			int amount = object.getInt("amount");
			if(db.updateBalance(cardnumber, amount)) {
				db.logTransaction(cardnumber, amount);
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