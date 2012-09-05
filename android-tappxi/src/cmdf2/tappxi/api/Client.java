package cmdf2.tappxi.api;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class Client {
	private String accessToken;

	public Client(String accessToken) {
		setAccessToken(accessToken);
	}

	public void connect() throws IOException {
		URL url = new URL("http://192.168.0.100/tappxi/?access_token=" + accessToken);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		try {
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			readStream(in);
		} finally {
			urlConnection.disconnect();
		}
	}
	
	private void readStream(InputStream inputStream) throws IOException {
		BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder total = new StringBuilder();
		String line;
		while ((line = r.readLine()) != null) {
		    total.append(line);
		}
		Log.d("tappxi", total.toString());
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
}
