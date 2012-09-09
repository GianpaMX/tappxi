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

public class Client {
	private String apiServer;
	private HttpClient httpClient;

	private String token;

	public Client() {
		httpClient = new DefaultHttpClient();
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
	
	public String getApiServer() {
		return apiServer;
	}

	public void setApiServer(String apiServer) {
		this.apiServer = apiServer;
	}

}
