package tw.funymph.bonjour;

import java.io.IOException;
import java.net.ServerSocket;

import android.app.Activity;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdManager.DiscoveryListener;
import android.net.nsd.NsdManager.RegistrationListener;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class NSDDemoActivity extends Activity implements RegistrationListener, DiscoveryListener {

	private Button _publishButton;
	private Button _discoverButton;
	private ListView _servicesView;

	private ServerSocket _serverSocket;
	private Thread _publishServiceThread;
	private NsdManager _serviceDiscoverManager;
	private ArrayAdapter<String> _discoveredServices;

	private boolean _published;
	private boolean _discovering;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		_published = false;
		_discovering = false;

		_servicesView = (ListView)findViewById(R.id.servicesView);
		_publishButton = (Button)findViewById(R.id.publishButton);
		_discoverButton = (Button)findViewById(R.id.discoverButton);
		_serviceDiscoverManager = (NsdManager)getSystemService(Context.NSD_SERVICE);

		_discoveredServices = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		_servicesView.setAdapter(_discoveredServices);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		depublishService();
		stopDiscovery();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(tw.funymph.bonjour.R.menu.main, menu);
		return true;
    }

	public void publishButtonPressed(View view) {
		if(_published) {
			depublishService();
		}
		else {
			startPublishService();
		}
	}

	public void discoverButtonPressed(View view) {
		if(!_discovering) {
			_discoverButton.setText(R.string.discovering);
			startDiscovery();
		}
		else {
			stopDiscovery();
		}
	}

	// Listener Methods for Network Service Discovery
	public void onRegistrationFailed(NsdServiceInfo serviceInfo, final int errorCode) {
		closeServerSocket();
		final Context context = this;
		runOnUiThread(new Runnable() {
			public void run() {
				_publishButton.setClickable(true);
				_publishButton.setText(R.string.publish);
				Toast.makeText(context, "failed to publish a bonjour service", 3).show();
				Log.d("network", "failed to publish a bounjour serverice, error code: " + errorCode);
			}
		});
	}

	public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
		
	}

	public void onServiceRegistered(NsdServiceInfo serviceInfo) {
		final Context context = this;
		runOnUiThread(new Runnable() {
			public void run() {
				_publishButton.setClickable(true);
				_publishButton.setText(R.string.depublish);
				Toast.makeText(context, "a bonjour service published", 3).show();
			}
		});
	}

	public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
		final Context context = this;
		_published = false;
		runOnUiThread(new Runnable() {
			public void run() {
				_publishButton.setText(R.string.publish);
				Toast.makeText(context, "a bonjour service depublished", 3).show();
			}
		});
	}

	// Listener Methods for Network Service Discovery
	public void onStartDiscoveryFailed(String serviceType, int errorCode) {
		
	}

	public void onStopDiscoveryFailed(String serviceType, int errorCode) {
		
	}

	public void onDiscoveryStarted(String serviceType) {
		Log.d("network", "discovery started");
	}

	public void onDiscoveryStopped(String serviceType) {
		Log.d("network", "discovery stopped");
		_discovering = false;
		runOnUiThread(new Runnable() {

			public void run() {
				_discoverButton.setText(R.string.discover);
			}
		});
	}

	public void onServiceFound(final NsdServiceInfo serviceInfo) {
		Log.d("network", "service found: " + serviceInfo.getServiceName());
		runOnUiThread(new Runnable() {

			public void run() {
				_discoveredServices.add(serviceInfo.getServiceName());
			}
		});
	}

	public void onServiceLost(final NsdServiceInfo serviceInfo) {
		Log.d("network", "service lost: " + serviceInfo.getServiceName());
		runOnUiThread(new Runnable() {

			public void run() {
				_discoveredServices.remove(serviceInfo.getServiceName());
			}
		});
	}

	// Private Methods
	private void stopDiscovery() {
		if (_discovering) {
			_discovering = false;
			_discoveredServices.clear();
			_serviceDiscoverManager.stopServiceDiscovery(this);
		}
	}

	private void startDiscovery() {
		if (!_discovering) {
			_discovering = true;
			_serviceDiscoverManager.discoverServices("_chat._tcp.", NsdManager.PROTOCOL_DNS_SD, this);
		}
	}

	private void depublishService() {
		closeServerSocket();
		_serviceDiscoverManager.unregisterService(this);
	}

	private void startPublishService() {
		_publishButton.setClickable(false);
		_publishButton.setText(R.string.publishing);
		_publishServiceThread = new Thread(new Runnable() {

			public void run() {
				publishService();
			}
		});
		_publishServiceThread.start();
	}

	private void publishService() {
		int port = startServerSocket();
		if (port == 0) {
			Toast.makeText(this, "unable to create a server socket for the service", 3);
			_publishButton.setClickable(true);
			_publishButton.setText(R.string.publish);
			return;
		}
		NsdServiceInfo service = createService(port);
		if (service != null) {
			Log.d("network", "start to register a service " + _serviceDiscoverManager);
			_serviceDiscoverManager.registerService(service, NsdManager.PROTOCOL_DNS_SD, this);
			_published = true;
			Log.d("network", "registering a service " + service);
		}
	}

	private NsdServiceInfo createService(int port) {
		NsdServiceInfo service  = new NsdServiceInfo();
		service.setServiceName("someone on Android");
		service.setServiceType("_chat._tcp.");
		service.setPort(port);
		return service;
	}

	private int startServerSocket() {
		int port = 0;
		// Initialize a server socket on the next available port.
		try {
			_serverSocket = new ServerSocket(0);
			port = _serverSocket.getLocalPort();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.d("network", "server socket created at " + port);
		// Store the chosen port.
		return port;
	}

	private void closeServerSocket() {
		try {
			_serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
