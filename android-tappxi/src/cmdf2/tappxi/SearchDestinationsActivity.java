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
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class SearchDestinationsActivity extends MapActivity {
	Geocoder geoCoder;
	LocationManager locationManager;
	Location currentBestLocation;
	TaxiStandItemizedOverlay itemizedoverlay;

	private static final int TWO_MINUTES = 1000 * 60 * 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_destinations);

		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				if (isBetterLocation(location, currentBestLocation)) {
					currentBestLocation = location;
				}
				// Called when a new location is found by the network location
				// provider.
				// makeUseOfNewLocation(location);
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};

		currentBestLocation = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		// Register the listener with the Location Manager to receive location
		// updates
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

		MapView mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.taxi_stand_pin);
		itemizedoverlay = new TaxiStandItemizedOverlay(drawable, this);

		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(this,
				mapView);
		myLocationOverlay.enableMyLocation();
		myLocationOverlay.enableCompass();
		mapOverlays.add(myLocationOverlay);

		geoCoder = new Geocoder(this);

		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			if (itemizedoverlay != null) {
				mapOverlays.remove(itemizedoverlay);
			}

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
					OverlayItem overlayitem = new OverlayItem(point,
							"End Address", address);
					itemizedoverlay.addOverlay(overlayitem);
					mapOverlays.add(itemizedoverlay);

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

	/**
	 * Determines whether one Location reading is better than the current
	 * Location fix
	 * 
	 * @param location
	 *            The new Location that you want to evaluate
	 * @param currentBestLocation
	 *            The current Location fix, to which you want to compare the new
	 *            one
	 */
	protected boolean isBetterLocation(Location location,
			Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
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
				currentBestLocation.getLatitude(),
				currentBestLocation.getLongitude(), 1);
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
