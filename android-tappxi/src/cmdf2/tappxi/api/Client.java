package cmdf2.tappxi.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;
import cmdf2.tappxi.model.bean.Address;

public class Client {
	private static Client instance = null;

	private String apiServer;
	private HttpClient httpClient;

	private String token;

	protected Client() {
		httpClient = new DefaultHttpClient();
	}

	public static Client getInstance() {
		if (instance == null) {
			instance = new Client();
		}
		return instance;
	}

	public void login(String fbToken) throws IOException {
		HttpPost httpPost = new HttpPost(apiServer + "/login");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("fb_token", fbToken));

		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		HttpResponse httpResponse = httpClient.execute(httpPost);
		String json = EntityUtils.toString(httpResponse.getEntity());

		try {
			JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
			token = object.getString("token");
		} catch (JSONException e) {
			Log.e("tappxi", "JSONException", e);
		}
	}

	public void taxiRequest(Address address) throws IOException {
		HttpPost httpPost = new HttpPost(apiServer + "/taxi/request");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("token", token));

		nameValuePairs.addAll(address.getNameValuePairs());

		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		HttpResponse httpResponse = httpClient.execute(httpPost);
		String json = EntityUtils.toString(httpResponse.getEntity());

		Log.d("tappxi", json);

		// try {
		// JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
		// token = object.getString("token");
		// } catch (JSONException e) {
		// Log.e("tappxi", "JSONException", e);
		// }
	}

	public String getApiServer() {
		return apiServer;
	}

	public void setApiServer(String apiServer) {
		this.apiServer = apiServer;
	}

}
