package cmdf2.tappxi;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.*;
import com.facebook.android.Facebook.*;

public class MainActivity extends Activity {
	Facebook facebook;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        facebook = new Facebook(getString(R.string.facebook_app_id));
        
        final TextView helloWorld = (TextView) findViewById(R.id.helloWorld);
        helloWorld.setText( helloWorld.getText() + " - ");
        
        facebook.authorize(this, new DialogListener() {
            @Override
            public void onComplete(Bundle values) {
            	helloWorld.setText( helloWorld.getText() + "Complete");
            }

            @Override
            public void onFacebookError(FacebookError error) {
            	helloWorld.setText( helloWorld.getText() + "FacebookError");
            }

            @Override
            public void onError(DialogError e) {
            	helloWorld.setText( helloWorld.getText() + "Error");
            }

            @Override
            public void onCancel() {
            	helloWorld.setText( helloWorld.getText() + "Cancel");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
    }
}
