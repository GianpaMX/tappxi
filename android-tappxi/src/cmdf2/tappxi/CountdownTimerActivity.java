package cmdf2.tappxi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import cmdf2.tappxi.api.Client;

public class CountdownTimerActivity extends Activity {
	Client client;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_countdown_timer);

		client = Client.getInstance();

		countDownTimer.start();
	}

	private CountDownTimer countDownTimer = new CountDownTimer(client.getTrip()
			.getOffer().getEta() * 60 * 1000, 1000) {

		@Override
		public void onTick(long millisUntilFinished) {
			int minutes = (int) (millisUntilFinished / 1000 / 60);
			int seconds = (int) ((millisUntilFinished / 1000) % 60);
			TextView cdtView = (TextView) findViewById(R.id.countdownTimer);
			cdtView.setText(minutes + ":" + seconds);
		}

		@Override
		public void onFinish() {
			AlertDialog.Builder adb = new AlertDialog.Builder(
					CountdownTimerActivity.this);
			adb.setTitle(getString(R.string.title_activity_countdown_timer));
			adb.setMessage(getString(R.string.taxi_should_be_here));
			adb.setPositiveButton("Close", null);
			adb.show();

			Intent intent = new Intent(CountdownTimerActivity.this,
					PayActivity.class);
			startActivity(intent);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_countdown_timer, menu);
		return true;
	}

	public void taxiIsHere(View view) {
		countDownTimer.cancel();

		Intent intent = new Intent(this, PayActivity.class);
		startActivity(intent);
	}

}
