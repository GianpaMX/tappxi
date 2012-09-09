package cmdf2.tappxi;

import java.io.IOException;
import java.util.List;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

public class SearchDestinationsActivity extends MapActivity {
	private Geocoder geoCoder;
	private PickUpItemizedOverlay pickUpItemizedOverlay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_destinations);

		geoCoder = new Geocoder(this);

		MapView mapView = (MapView) findViewById(R.id.mapview);
		final List<Overlay> mapOverlays = mapView.getOverlays();
		mapView.setBuiltInZoomControls(true);

		Drawable drawable = this.getResources().getDrawable(
				R.drawable.marker_green_a);

		pickUpItemizedOverlay = new PickUpItemizedOverlay(drawable,
				(ImageView) findViewById(R.id.drag), this);
		mapOverlays.add(pickUpItemizedOverlay);

		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(this,
				mapView);
		myLocationOverlay.enableMyLocation();
		myLocationOverlay.enableCompass();
		mapOverlays.add(myLocationOverlay);

		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			try {
				List<Address> addresses = geoCoder
						.getFromLocationName(query, 5);
				if (addresses.size() > 0) {
					GeoPoint point = new GeoPoint((int) (addresses.get(0)
							.getLatitude() * 1E6), (int) (addresses.get(0)
							.getLongitude() * 1E6));
					String address = query;
					List<Address> reverseAddresses = geoCoder.getFromLocation(
							point.getLatitudeE6() / 1E6,
							point.getLongitudeE6() / 1E6, 1);
					if (reverseAddresses.size() > 0) {
						address = reverseAddresses.get(0).getThoroughfare();
						address += " "
								+ reverseAddresses.get(0).getSubThoroughfare();
						address += ", "
								+ reverseAddresses.get(0).getSubLocality();
						address += ", "
								+ reverseAddresses.get(0).getSubAdminArea();
						address += ", "
								+ reverseAddresses.get(0).getPostalCode();
						address += ", " + reverseAddresses.get(0).getLocality();
						address += ", "
								+ reverseAddresses.get(0).getCountryName();
						address += ", "
								+ reverseAddresses.get(0).getAdminArea();
					}

					MapController mc = mapView.getController();
					mc.animateTo(point);
					mc.setZoom(17);
					mapView.invalidate();
				} else {
					AlertDialog.Builder adb = new AlertDialog.Builder(this);
					adb.setTitle(getString(R.string.title_activity_search_destinations));
					adb.setMessage(getString(R.string.noaddresses));
					adb.setPositiveButton("Close", null);
					adb.show();
				}
			} catch (IOException e) {
				Log.e("tappxi", "IOException", e);
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the options menu from XML
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_search_destinations, menu);

		// Get the SearchView and set the searchable configuration
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false); // Do not iconify the widget;
													// expand it by default

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_search:
			onSearchRequested();
			return true;
		default:
			return false;
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	public void listOffers(View view) throws IOException {
		String address = new String();
		List<Address> reverseAddresses = geoCoder.getFromLocation(
				pickUpItemizedOverlay.getPickUpPoint().getLatitudeE6() / 1E6,
				pickUpItemizedOverlay.getPickUpPoint().getLongitudeE6() / 1E6, 1);
		if (reverseAddresses.size() > 0) {
			address = reverseAddresses.get(0).getThoroughfare();
			address += " " + reverseAddresses.get(0).getSubThoroughfare();
			address += ", " + reverseAddresses.get(0).getSubLocality();
			address += ", " + reverseAddresses.get(0).getSubAdminArea();
			address += ", " + reverseAddresses.get(0).getPostalCode();
			address += ", " + reverseAddresses.get(0).getLocality();
			address += ", " + reverseAddresses.get(0).getCountryName();
			address += ", " + reverseAddresses.get(0).getAdminArea();

			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setTitle(getString(R.string.title_activity_search_destinations));
			adb.setMessage(address);
			adb.setPositiveButton("Close", null);
			adb.show();
		}

		// Intent intent = new Intent(this, TaxiOffersActivity.class);
		// startActivity(intent);
	}

}
