package garduino.moco.htwg.de.moco_ws14;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ToggleButton;


public class SettingsActivity extends Activity {

    private NumberPicker np,np2;
    private Button back,setInterval;
    private ToggleButton startService,sendNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        addListenerOnButtonBack();
        addNumberPicker();
        addSetInterval();
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
    }

    public void addNotification () {
        sendNotification = (ToggleButton) findViewById(R.id.toggleButtonNotification);
    }

}
