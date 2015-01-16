package garduino.moco.htwg.de.moco_ws14;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import service.GarduinoService;
import utils.ArduinoDataBean;


public class InsideActivity extends Activity {

    private Button manualMode, back;
    private ArduinoDataBean arduinoDataBean = null;
    GarduinoService garduinoService;

    TextView tempView;

    private InsideActivity myActivity;

    boolean mIsBound = false;

    ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {

            mIsBound = false;
            Toast.makeText(getBaseContext(),
                    "Verbindung zum Greenhouse Service wurde unterbrochen!",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            GarduinoService.LocalBinder binder = (GarduinoService.LocalBinder) service;
            garduinoService = binder.getService();
            garduinoService.addGuiListener(myActivity);


            mIsBound = true;
            Toast.makeText(getBaseContext(),
                    "Verbindung zum Greenhouse Service wurde hergestellt!",
                    Toast.LENGTH_LONG).show();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside);

        myActivity = this;

        addListenerOnButtonBack();
        addListenerOnButtonManualMode();
        addTempView();

        if (bindService(new Intent(this.getApplicationContext(),
                        GarduinoService.class), serviceConnection,
                Context.BIND_AUTO_CREATE)) {
            mIsBound = true;
        } else
            mIsBound = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    public void addTempView() {
        tempView = (TextView) findViewById(R.id.textViewTemp);
    }

    //back to StartView
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

    //launch ManualModeView
    public void addListenerOnButtonManualMode () {
        final Context context = this;
        manualMode = (Button) findViewById(R.id.manualMode);
        manualMode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context,ManualModeActivity.class);
                startActivity(intent);
            }
        });
    }

    //Set Temp View
    public void addBeanListener(ArduinoDataBean bean) {
        if(bean != null) {
            this.arduinoDataBean = bean;
            tempView.setText(String.valueOf(bean.getCurrentTemp_1()));
        }

    }

}
