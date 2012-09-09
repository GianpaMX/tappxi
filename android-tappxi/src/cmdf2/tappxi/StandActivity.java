package cmdf2.tappxi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import cmdf2.tappxi.model.bean.Stand;

public class StandActivity extends Activity {
	private Stand stand;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stand);
        
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras.containsKey("cmdf2.tappxi.stand")) {
			stand = (Stand) extras.get("cmdf2.tappxi.stand");
			
			TextView textView;

			textView = (TextView)findViewById(R.id.nameText);
			textView.setText(stand.getName());

			textView = (TextView)findViewById(R.id.fareText);
			textView.setText(String.valueOf(stand.getStartFare()));

			textView = (TextView)findViewById(R.id.phoneText);
			textView.setText(stand.getPhone());

			textView = (TextView)findViewById(R.id.addressText);
			textView.setText(stand.getAddress().toString());
		}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_stand, menu);
        return true;
    }
    
    public void call(View view) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + stand.getPhone()));
        startActivity(intent);
    }
}
