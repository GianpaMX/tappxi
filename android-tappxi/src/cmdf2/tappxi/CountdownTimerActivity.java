package cmdf2.tappxi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class CountdownTimerActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown_timer);
        
        new CountDownTimer(30000, 1000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				int minutes = (int) (millisUntilFinished/1000/60);
				int seconds = (int) ((millisUntilFinished/1000) % 60);
				TextView cdtView = (TextView) findViewById(R.id.countdownTimer);
				cdtView.setText(minutes + ":" + seconds);
			}
			
			@Override
			public void onFinish() {
		        Intent intent = new Intent(CountdownTimerActivity.this, PayActivity.class);
		        startActivity(intent);
			}
		}.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_countdown_timer, menu);
        return true;
    }
    
    public void taxiIsHere(View view) {
        Intent intent = new Intent(this, PayActivity.class);
        startActivity(intent);
    }
    
}
