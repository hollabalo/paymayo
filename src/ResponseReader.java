import java.io.*;

public class ResponseReader {
	public static String getBody(BufferedReader buf) throws IOException {
		String line =  null;
		StringBuilder builder = new StringBuilder();
		while((line = buf.readLine()) != null) {
			builder.append(line);
		}
		return builder.toString();
	}
}