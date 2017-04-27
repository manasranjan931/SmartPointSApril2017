package in.bizzmark.smartpoints_user;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.widget.Toast;

import in.bizzmark.smartpoints_user.wifidirect.DeviceListFragment;

/**
 * Created by User on 17-Mar-17.
 */

public class CustomProgressbarActivity extends Activity {

    private WifiP2pManager manager;
    private boolean isWifiP2pEnabled = false;
    private WifiP2pManager.Channel channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_progressbar);

     //   discoverPeers();
    }

    private void discoverPeers() {

        if (!isWifiP2pEnabled){
            Toast.makeText(CustomProgressbarActivity.this, "Enable P2P from action bar button above or system settings",
                    Toast.LENGTH_SHORT).show();
        }

        DeviceListFragment deviceListFragment = (DeviceListFragment) getFragmentManager()
                .findFragmentById(R.id.frag_list);
        deviceListFragment.onInitiateDiscovery();
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(CustomProgressbarActivity.this, "Discovery initiated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reason) {
                // Turn on wifi
                WifiManager wifi = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifi.setWifiEnabled(true);
                //Toast.makeText(WiFiDirectActivity.this, "wifi turned on !", Toast.LENGTH_SHORT).show();

                Toast.makeText(CustomProgressbarActivity.this, "Discovery failed :" + reason,
                        Toast.LENGTH_SHORT).show();

            }
        });

    }
}
