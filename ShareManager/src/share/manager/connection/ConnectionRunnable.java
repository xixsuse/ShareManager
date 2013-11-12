package share.manager.connection;

import java.io.*;
import java.net.*;

public class ConnectionRunnable implements Runnable {

	private String link, resultString;
	private int readTimeout = 10000, connectionTimeout = 15000;

	public ConnectionRunnable(String link) {
		this.link = link;
	}

	@Override
	public void run() {
		connect();
	}

	private void connect() {
		HttpURLConnection con = null;
		String line = "";
		StringBuffer sb = new StringBuffer();
		try {

			URL url = new URL(link);
			con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(readTimeout);
			con.setConnectTimeout(connectionTimeout);
			con.setRequestMethod("GET");
			con.setDoInput(true);

			// Start the connection
			con.connect();

			// Read results from the query
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream(), "UTF-8"));

			while ((line = reader.readLine()) != null)
				sb.append(line);

			resultString = sb.toString();
			reader.close();
		} catch (IOException e) {
			//Can't connect to the server
		} finally {
			if (con != null)
				con.disconnect();
		}
	}

	/*private String getQuery(List<NameValuePair> params)
			throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (NameValuePair pair : params) {
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
		}

		return result.toString();
	}*/

	public String getResultObject() {
		return resultString;
	}

}