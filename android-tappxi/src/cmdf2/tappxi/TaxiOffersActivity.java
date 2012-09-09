package cmdf2.tappxi;

import java.util.ArrayList;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import cmdf2.tappxi.model.bean.Address;
import cmdf2.tappxi.model.bean.Offer;
import cmdf2.tappxi.model.bean.Stand;

import com.google.android.maps.GeoPoint;

public class TaxiOffersActivity extends ExpandableListActivity {
	ExpandableListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras.containsKey("cmdf.tappxi.address")) {
        	Address address = (Address)extras.get("cmdf.tappxi.address");
        	Log.d("tappxi", address.toString());
        }
        
        adapter = new TaxiOffersExpandableListAdapter();
        setListAdapter(adapter);
        registerForContextMenu(getExpandableListView());
    }

    @Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
    	
    	if(childPosition == 3) {
            Intent intent = new Intent(this, CountdownTimerActivity.class);
            startActivity(intent);
    	}
    	
		return super.onChildClick(parent, v, groupPosition, childPosition, id);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_taxi_offers, menu);
        return true;
    }
            
    public class TaxiOffersExpandableListAdapter extends BaseExpandableListAdapter {
    	ArrayList<Offer> offers = new ArrayList<Offer>();

		public TaxiOffersExpandableListAdapter() {
			super();
			offers.add(new Offer(1, 10, (float) 25.3, new Stand(1, "Sitio centro", 20, 1, new Address(1, "República de BVrsil", "Centro", "México", "D.F.", "0300", new GeoPoint(99,99)))));
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
				return TaxiOffersActivity.this.getString(R.string.eta) + ": " + offers.get(groupPosition).getEta();
			case 1:
				return TaxiOffersActivity.this.getString(R.string.fare) + ": " + offers.get(groupPosition).getFare();
			case 2:
				return TaxiOffersActivity.this.getString(R.string.stand) + ": " + offers.get(groupPosition).getStand();
			case 3:
				return TaxiOffersActivity.this.getString(R.string.select);
			}
			return null;
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
            
            if(childPosition == 3 ) {
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
