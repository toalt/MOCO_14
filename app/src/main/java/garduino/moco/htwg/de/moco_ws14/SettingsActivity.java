package garduino.moco.htwg.de.moco_ws14;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import service.GarduinoService;
import utils.ArduinoDataBean;


public class SettingsActivity extends Activity {

    private NumberPicker np,np2;
    private Button back,setInterval;
    private ToggleButton startService,sendNotification;
    private Intent garduinoIntent;


    private GarduinoService garduinoService;

    private ArduinoDataBean arduinoDataBean;

    private SettingsActivity settingsActivity;

    private boolean mIsBounded = false;

    private boolean serviceIsStarted = false;

    ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIsBounded = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            GarduinoService.LocalBinder binder = (GarduinoService.LocalBinder) service;
            garduinoService = binder.getService();
            garduinoService.addGuiListener(settingsActivity);
            mIsBounded = true;

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        addListenerOnButtonBack();
        addNumberPicker();
        addSetInterval();
        addStartService();
        addNotification();
    }

    //back to launchView
    public void addListenerOnButtonBack () {
        final Context context = this;
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context,GarduinoMainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void addNumberPicker () {
        np = (NumberPicker) findViewById(R.id.numberPicker);
        np.setMinValue(0);
        np.setMaxValue(60);

        np2 = (NumberPicker) findViewById(R.id.numberPicker2);
        np2.setMinValue(0);
        np2.setMaxValue(60);
    }


    public void addSetInterval () {
        setInterval = (Button) findViewById(R.id.setInterval);
        setInterval.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                int min = np.getValue();
                int sec = np2.getValue();
                Log.i("Minutes",String.valueOf(min));
                Log.i("Seconds",String.valueOf(sec));
            }
        });
    }

    public void addStartService () {
        startService = (ToggleButton) findViewById(R.id.toggleButtonService);
        if(isMyServiceRunning(GarduinoService.class)) {
            startService.setChecked(true);
            serviceIsStarted = true;
        }
        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startService.isChecked()) {
                    Log.i("DAAA","Checked");
                    garduinoIntent = new Intent(v.getContext(),
                            GarduinoService.class);
                    startService(garduinoIntent);
                    bindService(new Intent(v.getContext(),
                                    GarduinoService.class), serviceConnection,
                            Context.BIND_AUTO_CREATE);
                    serviceIsStarted = true;


                }
                else {
                    unbindService(serviceConnection);
                    stopService(garduinoIntent);
                    mIsBounded = false;
                    serviceIsStarted = false;

                }

            }

        });
    }

    public void addNotification () {
        sendNotification = (ToggleButton) findViewById(R.id.toggleButtonNotification);
        if(mIsBounded) {
            if(!(garduinoService.isNotificationHumiAirIsShowing() && garduinoService.isNotificationTemperatureIsShowing())) {
                sendNotification.setChecked(true);
            }

        }
        sendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mIsBounded) {
                    if(sendNotification.isChecked()) {
                        garduinoService.setNotificationHumiAirIsShowing(false);
                        garduinoService.setNotificationTemperatureIsShowing(false);
                    }
                    else {
                        garduinoService.setNotificationHumiAirIsShowing(true);
                        garduinoService.setNotificationTemperatureIsShowing(true);
                    }
                }
                else {
                    Toast.makeText(v.getContext(), "Service ist nicht verbunden!", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void addBeanListener(ArduinoDataBean bean) {
        if(bean != null) {
            this.arduinoDataBean = bean;
        }

    }

}
