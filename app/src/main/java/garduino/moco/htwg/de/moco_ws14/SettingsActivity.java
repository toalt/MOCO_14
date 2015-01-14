package garduino.moco.htwg.de.moco_ws14;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class SettingsActivity extends Activity {

    private Button button7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        addListenerOnButton7();
    }

    //back to launchView
    public void addListenerOnButton7 () {
        final Context context = this;
        button7 = (Button) findViewById(R.id.button7);
        button7.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context,GarduinoMainActivity.class);
                startActivity(intent);
            }
        });
    }


}
