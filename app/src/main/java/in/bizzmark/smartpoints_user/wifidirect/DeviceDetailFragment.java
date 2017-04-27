/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package in.bizzmark.smartpoints_user.wifidirect;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.Channel;

import in.bizzmark.smartpoints_user.R;
import in.bizzmark.smartpoints_user.database.EarnAcknowledgement;
import in.bizzmark.smartpoints_user.database.RedeemAcknowledgement;

//import in.bizzmark.smartpoints_user.DeviceListFragment.DeviceActionListener;

/**
 * A fragment that manages a particular peer and allows interaction with device
 * i.e. setting up network connection and transferring data.
 */
public class DeviceDetailFragment extends Fragment implements ConnectionInfoListener {

    private View mContentView = null;
    public static Button btnDisconnect = null;
    private WifiP2pDevice device;
    private WifiP2pInfo info;
    ProgressDialog progressDialog = null;
    ProgressDialog sendProgress;
    private WifiP2pManager manager;
    private Channel channel;
    private String bill_amount;
    private String remoteAddress = null;
    Intent serviceIntent = null;

    private String type;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mContentView = inflater.inflate(R.layout.device_detail, null);

        // retrieve jsonData from sharedPreferences
        SharedPreferences sp = getActivity().getSharedPreferences("MY_BILL_AMOUNT", Context.MODE_PRIVATE);
        bill_amount = sp.getString("key_bill_amount", "");

        mContentView.findViewById(R.id.btn_connect).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Sending connection request
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;
                config.wps.setup = WpsInfo.PBC;
                config.groupOwnerIntent = 0;   //from old code

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                progressDialog = ProgressDialog.show(getActivity(), "Press back to cancel",
                        "Connecting to :" + device.deviceName, true, false);

                // ProgressDialog dismiss after specific time
                Runnable progressRunnable = new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.cancel();
                    }
                };
                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable, 30000);

                ((DeviceListFragment.DeviceActionListener) getActivity()).connect(config);

            }
        });

        btnDisconnect = (Button) mContentView.findViewById(R.id.btn_disconnect);
        btnDisconnect.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ((DeviceListFragment.DeviceActionListener) getActivity()).disconnect();
                    }
                });

        mContentView.findViewById(R.id.btn_send_data).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Allow user to pick an image from Gallery or other
                        // registered apps

                        boolean instance = FileTransferService.isInstanceCreated();
                        if (!instance) {
                            serviceIntent = new Intent(getActivity(), FileTransferService.class);
                        }
                        Log.v("device id",info.groupOwnerAddress.getHostAddress());
                        Intent serviceIntent = new Intent(getActivity(), FileTransferService.class);
                        serviceIntent.setAction(FileTransferService.ACTION_SEND_FILE);
                        serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS, info.groupOwnerAddress.getHostAddress());
                        serviceIntent.putExtra(FileTransferService.MESSAGE, bill_amount);
                        serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_PORT, 8888);
                        getActivity().startService(serviceIntent);

                        sendProgress = new ProgressDialog(getActivity());
                        sendProgress.setMessage("Please wait amount sending....");
                        sendProgress.setCancelable(false);
                        sendProgress.show();

                        // ProgressDialog dismiss after specific time
                        Runnable progressRunnable = new Runnable() {
                            @Override
                            public void run() {
                                sendProgress.cancel();
                           //     Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_SHORT).show();
                            }
                        };
                        Handler pdCanceller = new Handler();
                        pdCanceller.postDelayed(progressRunnable, 13000);

                    }
                });
        return mContentView;
    }

    @Override
    public void onConnectionInfoAvailable(final WifiP2pInfo info) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        this.info = info;
        this.getView().setVisibility(View.VISIBLE);

        // After the group negotiation, we assign the group owner as the file
        // server. The file server is single threaded, single connection server
        // socket.
        if(info.groupFormed ){
            new FileServerAsyncTask(getActivity()).execute();
            mContentView.findViewById(R.id.btn_send_data).setVisibility(View.VISIBLE);
            ((TextView) mContentView.findViewById(R.id.status_text)).setText(getResources()
                    .getString(R.string.client_text));
        }

        // hide the connect button
        mContentView.findViewById(R.id.btn_connect).setVisibility(View.GONE);
    }

    /**
     * Updates the UI with device data
     *
     * @param device the device to be displayed
     */
    public void showDetails(WifiP2pDevice device) {
        this.device = device;
        this.getView().setVisibility(View.VISIBLE);
        TextView view = (TextView) mContentView.findViewById(R.id.device_address);
        view.setText(device.deviceAddress);
        view = (TextView) mContentView.findViewById(R.id.device_info);
        view.setText(device.toString());

    }

    /**
     * Clears the UI fields after a disconnect or direct mode disable operation.
     */
    public void resetViews() {
        mContentView.findViewById(R.id.btn_connect).setVisibility(View.VISIBLE);
        mContentView.findViewById(R.id.btn_send_data).setVisibility(View.GONE);
        this.getView().setVisibility(View.GONE);
    }

    /**
     * A simple server socket that accepts connection and writes some data on
     * the stream.
     */
    public class FileServerAsyncTask extends AsyncTask<Void, Void, String> {
        private Context context;

        /**
         * @param context
         */
        public FileServerAsyncTask(Context context) {
            this.context = context;
        }

        /*
         * (non-Javadoc)
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                /**
                 * Create a server socket and wait for client connections. This
                 * call blocks until a connection is accepted from a client
                 */
                ServerSocket serverSocket;
                serverSocket = new ServerSocket(9999);
                serverSocket.setReuseAddress(true);
                Socket client = serverSocket.accept();
                remoteAddress = ((InetSocketAddress) client.getRemoteSocketAddress()).getAddress().getHostName();
                Log.i("bizzmark", "Client connected.");
                InputStream inputStream = client.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int i;
                while ((i = inputStream.read()) != -1) {
                    baos.write(i);
                }
                String str = baos.toString();
               // serverSocket.close();
                return str;
            } catch (Throwable e) {
                e.printStackTrace();
                return null;
            }
        }

        /*
         * (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(final String result) {
            if (result != null) {
                try{
                    JSONObject obj = new JSONObject(result);
                    type = obj.getString("type");
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (type.equalsIgnoreCase("earn")) {
                    Intent i = new Intent(getActivity(), EarnAcknowledgement.class);
                    i.putExtra("result", result);
                    startActivity(i);
                    getActivity().finish();

                    sendProgress.dismiss();
                    ((DeviceListFragment.DeviceActionListener) getActivity()).disconnect();
                }else if (type.equalsIgnoreCase("redeem")){
                    Intent i = new Intent(getActivity(), RedeemAcknowledgement.class);
                    i.putExtra("result", result);
                    startActivity(i);
                    getActivity().finish();

                    sendProgress.dismiss();
                    ((DeviceListFragment.DeviceActionListener) getActivity()).disconnect();
                }
            }
        }
    }

}
