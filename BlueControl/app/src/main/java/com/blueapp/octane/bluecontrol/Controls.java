package com.blueapp.octane.bluecontrol;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class Controls extends AppCompatActivity {

    Button lock_text_lock,lock_text_unlock;
    Button light_text_on,light_text_off;
    String address = null;
    String deviceName = null;

    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    private boolean connectionLost = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_controls);

        Intent newint = getIntent();
        deviceName = newint.getStringExtra(Devices.EXTRA_NAME);
        address = newint.getStringExtra(Devices.EXTRA_ADDRESS);

        TextView statusView = (TextView) findViewById(R.id.status);

        lock_text_lock=(Button) findViewById(R.id.door_lock);
        lock_text_unlock=(Button) findViewById(R.id.door_unlock);
        light_text_on=(Button) findViewById(R.id.light_bar_on);
        light_text_off=(Button) findViewById(R.id.light_bar_off);

        final View door_lock = findViewById(R.id.door_lock);
        final View door_unlock = findViewById(R.id.door_unlock);
        final View light_bar_on = findViewById(R.id.light_bar_on);
        final View light_bar_off = findViewById(R.id.light_bar_off);
        final View reboot = findViewById(R.id.reboot);
        final View shutdown = findViewById(R.id.shutdown);
        final View start = findViewById(R.id.start);

        statusView.setText("Connecting to " + deviceName);

        new ConnectBT().execute();

        door_lock.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    send(buildMessage("1", door_lock, event));
                    lock_text_lock.setTextColor(Color.RED);
                    lock_text_unlock.setTextColor(Color.BLACK);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    send(buildMessage("2", door_lock, event));
                }
                return false;
            }
        });

        door_unlock.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    send(buildMessage("3", door_unlock, event));
                    lock_text_unlock.setTextColor(Color.RED);
                    lock_text_lock.setTextColor(Color.BLACK);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    send(buildMessage("4", door_unlock, event));
                }
                return false;
            }
        });

        light_bar_off.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    send(buildMessage("5", light_bar_off, event));
                    light_text_off.setTextColor(Color.RED);
                    light_text_on.setTextColor(Color.BLACK);
                }
                return false;
            }
        });

        light_bar_on.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    send(buildMessage("6", light_bar_on, event));
                    light_text_on.setTextColor(Color.RED);
                    light_text_off.setTextColor(Color.BLACK);
                }
                return false;
            }
        });

        start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    send(buildMessage("7", start, event));

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    send(buildMessage("8", start, event));
                }
                return false;
            }
        });

        reboot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    send(buildMessage("9", reboot, event));

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    send(buildMessage("10", reboot, event));
                }
                return false;
            }
        });

        shutdown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    send(buildMessage("11", shutdown, event));

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    send(buildMessage("12", shutdown, event));
                }
                return false;
            }
        });

    }

    private String buildMessage(String operation, View Buttons, MotionEvent event) {
        return operation;
    }

    private void send(String message) {
        if (btSocket!=null) {
            try {
                btSocket.getOutputStream().write(message.getBytes());
            } catch (IOException e) {
                msg("Error : " + e.getMessage());
                if(e.getMessage().contains("Broken pipe")) Disconnect();
            }
        } else {
            msg("Error : btSocket == null");
        }
    }

    private void Disconnect() {
        if (btSocket!=null) {
            try {
                isBtConnected = false;
                btSocket.close();
            } catch (IOException e) {
                msg("Error");
            }
        }
        Toast.makeText(getApplicationContext(),"Disconnected",Toast.LENGTH_LONG).show();
        finish();
    }

    private void msg(String message) {
        TextView statusView = (TextView)findViewById(R.id.status);
        statusView.setText(message);
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(Controls.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) { //while the progress dialog is shown, the connection is done in background
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            } catch (IOException e) {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) { //after the doInBackground, it checks if everything went fine
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                Toast.makeText(getApplicationContext(), "Failed to connect", Toast.LENGTH_LONG).show();
                finish();
            } else {
                msg("Connected to " + deviceName);
                isBtConnected = true;
                // start the connection monitor
                new MonitorConnection().execute();
            }
            progress.dismiss();
        }
    }

    private class MonitorConnection extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... devices) {
            while (!connectionLost) {
                try {
                    //read from the buffer, when this errors the connection is lost
                    // this was the only reliable way I found of monitoring the connection
                    // .isConnected didnt work
                    // BluetoothDevice.ACTION_ACL_DISCONNECTED didnt fire
                    btSocket.getInputStream().read();
                } catch (IOException e) {
                    connectionLost = true;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // if the bt is still connected, the connection must have been lost
            if (isBtConnected) {
                try {
                    isBtConnected = false;
                    btSocket.close();
                } catch (IOException e) {
                    // nothing doing, we are ending anyway!
                }
                Toast.makeText(getApplicationContext(), "Connection lost", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Disconnect();
    }
}

