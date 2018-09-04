package com.blueapp.octane.bluepad;

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

public class Dpad extends AppCompatActivity {

    Button sound_text_on,sound_text_off;
    Button video_text_on,video_text_off;
    String address = null;
    String deviceName = null;
    TextView volume_value_showValue;
    int vol_counter = 100;

    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    private boolean connectionLost = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dpad);

        Intent newint = getIntent();
        deviceName = newint.getStringExtra(Devices.EXTRA_NAME);
        address = newint.getStringExtra(Devices.EXTRA_ADDRESS);

        TextView statusView = (TextView) findViewById(R.id.status);
        volume_value_showValue = (TextView) findViewById(R.id.volume_value);

        sound_text_on=(Button) findViewById(R.id.sound_on);
        sound_text_off=(Button) findViewById(R.id.sound_off);
        video_text_on=(Button) findViewById(R.id.video_on);
        video_text_off=(Button) findViewById(R.id.video_off);

        final View up_dpad = findViewById(R.id.up_dpad);
        final View down_dpad = findViewById(R.id.down_dpad);
        final View left_dpad = findViewById(R.id.left_dpad);
        final View right_dpad = findViewById(R.id.right_dpad);
        final View manual = findViewById(R.id.manual);
        final View auto = findViewById(R.id.auto);
        final View e_stop = findViewById(R.id.e_stop);
        final View shutdown = findViewById(R.id.shutdown);
        final View reboot = findViewById(R.id.reboot);
        final View sound_on = findViewById(R.id.sound_on);
        final View sound_off = findViewById(R.id.sound_off);
        final View volume_up = findViewById(R.id.volume_up);
        final View volume_down = findViewById(R.id.volume_down);
        final View picture = findViewById(R.id.picture);
        final View video_on = findViewById(R.id.video_on);
        final View video_off = findViewById(R.id.video_off);
        final View send = findViewById(R.id.send);

        statusView.setText("Connecting to " + deviceName);

        new ConnectBT().execute();

        up_dpad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    send(buildMessage("1", up_dpad, event));

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    send(buildMessage("2", up_dpad, event));
                }
                return false;
            }
        });

        down_dpad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    send(buildMessage("3", down_dpad, event));

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    send(buildMessage("4", down_dpad, event));
                }
                return false;
            }
        });

        left_dpad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    send(buildMessage("5", left_dpad, event));

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    send(buildMessage("6", left_dpad, event));
                }
                return false;
            }
        });

        right_dpad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    send(buildMessage("7", right_dpad, event));

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    send(buildMessage("8", right_dpad, event));
                }
                return false;
            }
        });

        manual.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    send(buildMessage("9", manual, event));

                }
                return false;
            }
        });

        auto.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    send(buildMessage("10", auto, event));

                }
                return false;
            }
        });

        e_stop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    send(buildMessage("11", e_stop, event));

                }
                return false;
            }
        });

        shutdown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    send(buildMessage("12", shutdown, event));

                }
                return false;
            }
        });

        reboot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    send(buildMessage("17", reboot, event));

                }
                return false;
            }
        });

        sound_on.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    send(buildMessage("13", sound_on, event));
                    sound_text_on.setTextColor(Color.GREEN);
                    sound_text_off.setTextColor(Color.BLACK);
                }
                return false;
            }
        });

        sound_off.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    send(buildMessage("14", sound_off, event));
                    sound_text_off.setTextColor(Color.RED);
                    sound_text_on.setTextColor(Color.BLACK);
                }
                return false;
            }
        });

        volume_up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    send(buildMessage("15", volume_up, event));

                }
                return false;
            }
        });

        volume_down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    send(buildMessage("16", volume_down, event));

                }
                return false;
            }
        });

        picture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    send(buildMessage("18", picture, event));

                }
                return false;
            }
        });

        video_on.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    send(buildMessage("19", video_on, event));
                    video_text_on.setTextColor(Color.GREEN);
                    video_text_off.setTextColor(Color.BLACK);
                }
                return false;
            }
        });

        video_off.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    send(buildMessage("20", video_off, event));
                    video_text_off.setTextColor(Color.RED);
                    video_text_on.setTextColor(Color.BLACK);
                }
                return false;
            }
        });

        send.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    send(buildMessage("21", send, event));

                }
                return false;
            }
        });

    }

    public void volume_up (View view) {
        if (vol_counter < 100) {
            vol_counter++;
            volume_value_showValue.setText(Integer.toString(vol_counter));
        }
        else {
            return;
        }
    }
    public void volume_down (View view) {
        if (vol_counter > 0) {
            vol_counter--;
            volume_value_showValue.setText(Integer.toString(vol_counter));
        }
        else {
            return;
        }
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
            progress = ProgressDialog.show(Dpad.this, "Connecting...", "Please wait!!!");  //show a progress dialog
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

