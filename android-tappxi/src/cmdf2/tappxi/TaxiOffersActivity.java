package cmdf2.tappxi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import cmdf2.tappxi.api.Client;
import cmdf2.tappxi.model.bean.Address;
import cmdf2.tappxi.model.bean.Offer;
import cmdf2.tappxi.model.bean.Trip;

public class TaxiOffersActivity extends ExpandableListActivity {
	TaxiOffersExpandableListAdapter adapter;
	Client client;

	ProgressDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		client = Client.getInstance();

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras.containsKey("cmdf.tappxi.address")) {
			final Address address = (Address) extras.get("cmdf.tappxi.address");

			new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					try {
						client.taxiRequest(address);
					} catch (IOException e) {
						Log.e("tappxi", "IOException", e);
					}
					return null;
				}

				@Override
				protected void onPreExecute() {
					dialog = ProgressDialog.show(TaxiOffersActivity.this,
							getString(R.string.getting),
							getString(R.string.getting_wait));
				}

				@Override
				protected void onPostExecute(Void result) {
					updateTimer.start();
				}
			}.execute();

		}

		adapter = new TaxiOffersExpandableListAdapter();
		setListAdapter(adapter);
		registerForContextMenu(getExpandableListView());
		// adapter.registerDataSetObserver(observer);
	}

	private CountDownTimer updateTimer = new CountDownTimer(10 * 60 * 1000,
			15 * 1000) {
		@Override
		public void onTick(long millisUntilFinished) {
			new AsyncTask<Void, Void, List<Offer>>() {

				@Override
				protected void onPreExecute() {
					if (dialog == null) {
						dialog = ProgressDialog.show(TaxiOffersActivity.this,
								getString(R.string.getting),
								getString(R.string.getting_wait));
					}
				}

				@Override
				protected List<Offer> doInBackground(Void... params) {
					try {
						return client.offers();
					} catch (IOException e) {
						Log.e("tappxi", "IOException", e);
					}
					return null;
				}

				@Override
				protected void onPostExecute(List<Offer> offers) {
					adapter.refresh(offers);
					dialog.dismiss();
					dialog = null;
				}

			}.execute();
		}

		@Override
		public void onFinish() {
			AlertDialog.Builder adb = new AlertDialog.Builder(
					TaxiOffersActivity.this);
			adb.setTitle(getString(R.string.title_activity_taxi_offers));
			adb.setMessage(getString(R.string.notaxis));
			adb.setPositiveButton("Close", null);
			adb.show();

			Intent intent = new Intent(TaxiOffersActivity.this,
					MainActivity.class);
			startActivity(intent);
		}
	};

	@Override
	public boolean onChildClick(final ExpandableListView parent, View v,
			final int groupPosition, int childPosition, long id) {

		if (childPosition == 3) {
			updateTimer.cancel();
			
			new AsyncTask<Void, Void, Trip>() {
				@Override
				protected Trip doInBackground(Void... params) {
					Trip trip = null;
					
					Offer offer = (Offer)((TaxiOffersExpandableListAdapter)parent.getAdapter()).getGroup(groupPosition);
					try {
						trip = client.confirmTrip(offer);
					} catch (IOException e) {
						Log.e("tappxi", "IOException", e);
					}
					
					return trip;
				}
				@Override
				protected void onPostExecute(Trip trip) {
					Intent intent;
					if(trip != null) {
						intent = new Intent(TaxiOffersActivity.this, CountdownTimerActivity.class);
					} else {
						AlertDialog.Builder adb = new AlertDialog.Builder(
								TaxiOffersActivity.this);
						adb.setTitle(getString(R.string.title_activity_taxi_offers));
						adb.setMessage(getString(R.string.error_selecting_trip));
						adb.setPositiveButton("Close", null);
						adb.show();
						intent = new Intent(TaxiOffersActivity.this, MainActivity.class);
					}
					startActivity(intent);
				}
			};
		}

		return super.onChildClick(parent, v, groupPosition, childPosition, id);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_taxi_offers, menu);
		return true;
	}

	public class TaxiOffersExpandableListAdapter extends
			BaseExpandableListAdapter {
		List<Offer> offers;

		public TaxiOffersExpandableListAdapter(List<Offer> newOffers) {
			super();
			this.offers = new ArrayList<Offer>(newOffers);
		}

		public TaxiOffersExpandableListAdapter() {
			this(new ArrayList<Offer>());
		}

		@Override
		public int getGroupCount() {
			return offers.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return 4;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return offers.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			switch (childPosition) {
			case 0:
				return TaxiOffersActivity.this.getString(R.string.eta) + ": "
						+ offers.get(groupPosition).getEta();
			case 1:
				return TaxiOffersActivity.this.getString(R.string.fare) + ": "
						+ offers.get(groupPosition).getFare();
			case 2:
				return TaxiOffersActivity.this.getString(R.string.stand) + ": "
						+ offers.get(groupPosition).getStand();
			case 3:
				return TaxiOffersActivity.this.getString(R.string.select);
			}
			return null;
		}

		public void refresh(List<Offer> newOffers) {
			offers.clear();
			offers.addAll(newOffers);
			notifyDataSetChanged();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return offers.get(groupPosition).getId();
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			TextView textView = getGenericView();
			textView.setText(getGroup(groupPosition).toString());
			return textView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			TextView textView = getGenericView();
			textView.setText(getChild(groupPosition, childPosition).toString());

			if (childPosition == 3) {
				textView.setTypeface(null, Typeface.BOLD);
			}

			return textView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return childPosition == 2 || childPosition == 3;
		}

		public TextView getGenericView() {
			// Layout parameters for the ExpandableListView
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, 64);

			TextView textView = new TextView(TaxiOffersActivity.this);
			textView.setLayoutParams(lp);
			// Center the text vertically
			textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			// Set the text starting position
			textView.setPadding(36, 0, 0, 0);
			return textView;
		}
	}
}
