package garduino.moco.htwg.de.moco_ws14;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import utils.ArduinoDataBean;


public class InsideActivity extends Activity {

    private Button manualMode,back;
    private ArduinoDataBean arduinoDataBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside);
        addListenerOnButtonBack();
        addListenerOnButtonManualMode();
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

    public void addBeanListener(ArduinoDataBean bean) {
        if(bean != null) {
            this.arduinoDataBean = bean;
        }

    }

}
