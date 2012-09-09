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
import android.widget.SearchView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class SearchDestinationsActivity extends MapActivity {
	Geocoder geoCoder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_destinations);

		MapView mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.taxi_stand_pin);
		TaxiStandItemizedOverlay itemizedoverlay = new TaxiStandItemizedOverlay(
				drawable, this);

		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			mapOverlays.clear();

			geoCoder = new Geocoder(this);

			String query = intent.getStringExtra(SearchManager.QUERY);
			try {
				List<Address> addresses = geoCoder
						.getFromLocationName(query, 5);
				if (addresses.size() > 0) {
					GeoPoint point = new GeoPoint((int) (addresses.get(0)
							.getLatitude() * 1E6), (int) (addresses.get(0)
							.getLongitude() * 1E6));
					OverlayItem overlayitem = new OverlayItem(point,
							"End Address", query);

					itemizedoverlay.addOverlay(overlayitem);
					mapOverlays.add(itemizedoverlay);
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

	public void listOffers(View view) {
		Intent intent = new Intent(this, TaxiOffersActivity.class);
		startActivity(intent);
	}

}
