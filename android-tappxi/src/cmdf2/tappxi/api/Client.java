package cmdf2.tappxi.api;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;
import cmdf2.tappxi.model.bean.Address;
import cmdf2.tappxi.model.bean.Offer;
import cmdf2.tappxi.model.bean.Request;
import cmdf2.tappxi.model.bean.Trip;

public class Client {
	private static Client instance = null;

	private String apiServer;
	private HttpClient httpClient;

	private String token;
	private Request request;

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
			Log.d("tappxi", token);
		} catch (JSONException e) {
			Log.e("tappxi", "JSONException", e);
		}
	}

	public void taxiRequest(Address address) throws IOException {
		HttpPost httpPost = new HttpPost(apiServer + "/taxi/request");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("token", token));

		nameValuePairs.addAll(address.getNameValuePairs("start_address_"));
		
		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		
		HttpResponse httpResponse = httpClient.execute(httpPost);
		String json = EntityUtils.toString(httpResponse.getEntity());

		Log.d("tappxi", json);

		try {
			JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
			request = Request.fromJSONObject(object);
			Log.d("tappxi", String.valueOf(request.getId()));;
		} catch (JSONException e) {
			Log.e("tappxi", "JSONException", e);
		}
	}

	public Collection<Offer> offers() throws IOException {
		HttpPost httpPost = new HttpPost(apiServer + "/offers");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("token", token));

		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		HttpResponse httpResponse = httpClient.execute(httpPost);
		String json = EntityUtils.toString(httpResponse.getEntity());

		Log.d("tappxi", json);
		
		Collection<Offer> offers = new ArrayList<Offer>();
		try {
			JSONArray array = (JSONArray) new JSONTokener(json).nextValue();
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				offers.add(Offer.fromJSONObject(object));
			}
		} catch (JSONException e) {
			Log.e("tappxi", "JSONException", e);
		}
		return offers;
	}

	public Trip confirmTrip(Offer offer) throws IOException {
		HttpPost httpPost = new HttpPost(apiServer + "/trip/confirm");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("token", token));
		
		nameValuePairs.add(new BasicNameValuePair("id_offer", String.valueOf(offer.getId())));

		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		HttpResponse httpResponse = httpClient.execute(httpPost);
		String json = EntityUtils.toString(httpResponse.getEntity());

		Log.d("tappxi", json);
		Trip trip = null;
		try {
			JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
			trip = Trip.fromJSONObject(object);
		} catch (JSONException e) {
			Log.e("tappxi", "JSONException", e);
		} catch (ParseException e) {
			Log.e("tappxi", "ParseException", e);
		}
		return trip;
	}

	public String getApiServer() {
		return apiServer;
	}

	public void setApiServer(String apiServer) {
		this.apiServer = apiServer;
	}

}
