package cmdf2.tappxi;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class PickUpItemizedOverlay extends ItemizedOverlay<OverlayItem> {
	private LocationManager locationManager;
	private Location currentBestLocation;

	private static final int TWO_MINUTES = 1000 * 60 * 2;
	
	private OverlayItem pickUpOverlayItem;
	
	private LocationListener locationListener;

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	private Drawable marker = null;
	private OverlayItem inDrag = null;
	private ImageView dragImage = null;
	private int xDragImageOffset = 0;
	private int yDragImageOffset = 0;
	private int xDragTouchOffset = 0;
	private int yDragTouchOffset = 0;

	public PickUpItemizedOverlay(Drawable defaultMarker, ImageView drag,
			Context context) {
		super(boundCenterBottom(defaultMarker));
		marker = defaultMarker;
		mContext = context;

		dragImage = drag;

		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		currentBestLocation = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
		
		locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				if (isBetterLocation(location, currentBestLocation)) {
					currentBestLocation = location;
					
					pickUpOverlayItem = moveItem(pickUpOverlayItem, getGeoPointFromLocation(location));
				}
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		
		pickUpOverlayItem = new OverlayItem(getGeoPointFromLocation(currentBestLocation),
				"Pick me up", "I'm in address");

		addOverlay(pickUpOverlayItem);
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected boolean onTap(int index) {
		OverlayItem item = mOverlays.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		final int action = event.getAction();
		final int x = (int) event.getX();
		final int y = (int) event.getY();
		boolean result = false;

		if (action == MotionEvent.ACTION_DOWN) {
			for (OverlayItem item : mOverlays) {
				Point p = new Point(0, 0);

				mapView.getProjection().toPixels(item.getPoint(), p);

				if (hitTest(item, marker, x - p.x, y - p.y)) {
					result = true;
					inDrag = item;
					mOverlays.remove(inDrag);
					populate();

					xDragTouchOffset = 0;
					yDragTouchOffset = 0;

					setDragImagePosition(p.x, p.y);
					dragImage.setVisibility(View.VISIBLE);

					xDragTouchOffset = x - p.x;
					yDragTouchOffset = y - p.y;

					break;
				}
			}
		} else if (action == MotionEvent.ACTION_MOVE && inDrag != null) {
			setDragImagePosition(x, y);
			result = true;
		} else if (action == MotionEvent.ACTION_UP && inDrag != null) {
			dragImage.setVisibility(View.GONE);

			GeoPoint pt = mapView.getProjection().fromPixels(
					x - xDragTouchOffset, y - yDragTouchOffset);
			pickUpOverlayItem = new OverlayItem(pt, inDrag.getTitle(),
					inDrag.getSnippet());

			mOverlays.add(pickUpOverlayItem);
			populate();

			inDrag = null;
			result = true;

			locationManager.removeUpdates(locationListener);
		}

		return (result || super.onTouchEvent(event, mapView));
	}

	public OverlayItem moveItem(OverlayItem item, GeoPoint to) {
		mOverlays.clear();
		OverlayItem movedItem = new OverlayItem(to, item.getTitle(),
				item.getSnippet());
		mOverlays.add(movedItem);

		populate();

		return movedItem;
	}

	private void setDragImagePosition(int x, int y) {
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) dragImage
				.getLayoutParams();

		lp.setMargins(x - xDragImageOffset - xDragTouchOffset, y
				- yDragImageOffset - yDragTouchOffset, 0, 0);
		dragImage.setLayoutParams(lp);
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


	private GeoPoint getGeoPointFromLocation(Location location) {
		return (new GeoPoint((int) (location.getLatitude() * 1E6),
				(int) (location.getLongitude() * 1E6)));
	}
	
	public Location getCurrentBestLocation() {
		return currentBestLocation;
	}
	
	public GeoPoint getPickUpPoint() {
		return pickUpOverlayItem.getPoint();
	}
	
	public void setPickUpPoint(GeoPoint pickupPoint) {
		pickUpOverlayItem = moveItem(pickUpOverlayItem, pickupPoint);
	}

}
