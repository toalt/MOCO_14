package garduino.moco.htwg.de.moco_ws14;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class GarduinoMainActivity extends Activity {

    private Button button1;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garduino_main);
        addListenerOnButton1();
        addListenerOnButton2();
    }

    //launch into the Greenhouse
    public void addListenerOnButton1 () {
        final Context context = this;
        button1 = (Button) findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context,InsideActivity.class);
                startActivity(intent);
            }
        });
    }

    //launch into Settings
    public void addListenerOnButton2 () {
        final Context context = this;
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context,SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

}
