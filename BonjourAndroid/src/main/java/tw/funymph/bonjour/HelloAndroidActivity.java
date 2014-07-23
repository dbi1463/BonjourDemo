package tw.funymph.bonjour;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

public class HelloAndroidActivity extends Activity {

	private JmDNS _jmsDNS;

	private Button _publishButton;
	private Button _discoverButton;

	private boolean _publishing;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			_jmsDNS = JmDNS.create();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setContentView(R.layout.activity_main);
		_publishButton = (Button)findViewById(R.id.publishButton);
		_discoverButton = (Button)findViewById(R.id.discoverButton);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(tw.funymph.bonjour.R.menu.main, menu);
		return true;
    }

	public void publishButtonPressed(View view) {
		_publishButton.setClickable(false);
		_publishButton.setText(R.string.publishing);
		ServiceInfo service = ServiceInfo.create("_tcp.local.", "someone on Android", 9000, "a simple example of Bonjour on Android");
		try {
			_jmsDNS.registerService(service);
		} catch (IOException e) {
			_publishButton.setClickable(true);
			Log.d("bonjour", "failed to publish service");
			_publishButton.setText(R.string.publish);
		}
		_publishButton.setTag(R.string.published);
		Log.d("bonjour", "service published");
    }

	public void discoverButtonPressed(View view) {
    }
}
