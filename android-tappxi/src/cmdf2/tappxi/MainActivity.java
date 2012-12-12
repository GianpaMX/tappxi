package cmdf2.tappxi;

import java.io.IOException;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import cmdf2.tappxi.api.Client;

import com.facebook.android.Facebook;

public class MainActivity extends FragmentActivity {
	Facebook facebook;
	Client client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        client = Client.getInstance();
        client.setApiServer("http://192.168.0.100/tappxi/web/app_dev.php/api");
        
        new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
            	try {
					return client.login("dummy");
				} catch (IOException e) {
					Log.e("tappxi", "IOException", e);
					return false;
				}
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if(!result) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							MainActivity.this);
					adb.setTitle(getString(R.string.title_activity_main));
					adb.setMessage(getString(R.string.error_connecting_to_server));
					adb.setPositiveButton("Close", null);
					adb.show();					
				}
			}
        	
        }.execute();
        
//        facebook = new Facebook(getString(R.string.facebook_app_id));
        
//        facebook.authorize(this, new DialogListener() {
//            @Override
//            public void onComplete(Bundle values) {
//                client = new Client(values.getString("access_token"));
//            	
//                new AsyncTask<Void, Void, Void>() {
//                    protected Void doInBackground(Void... params) {
//                    	try {
//							client.connect();
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							Log.e("tappxi", "IOException", e);
//						}
//						return null;
//                    }
//                    protected void onPostExecute(Void result) {
//                    	
//                    }
//                }.execute();
//            }
//
//            @Override
//            public void onFacebookError(FacebookError error) {
//            }
//
//            @Override
//            public void onError(DialogError e) {
//            }
//
//            @Override
//            public void onCancel() {
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        facebook.authorizeCallback(requestCode, resultCode, data);
    }
    
    public void searchDestinations(View view) {
        Intent intent = new Intent(this, SearchDestinationsActivity.class);
        startActivity(intent);
    }
}
