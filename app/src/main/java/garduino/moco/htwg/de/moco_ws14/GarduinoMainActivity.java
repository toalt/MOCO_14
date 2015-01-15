package garduino.moco.htwg.de.moco_ws14;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class GarduinoMainActivity extends Activity {

    private Button inside,settings,about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garduino_main);
        addListenerOnButtonInside();
        addListenerOnButtonSettings();
    }

    //launch into the Greenhouse
    public void addListenerOnButtonInside () {
        final Context context = this;
        inside = (Button) findViewById(R.id.inside);
        inside.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context,InsideActivity.class);
                startActivity(intent);
            }
        });
    }

    //launch into Settings
    public void addListenerOnButtonSettings () {
        final Context context = this;
        settings = (Button) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context,SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    //launch into About View
    public void addListenerOnButtonAbout () {
        final Context context = this;
        about = (Button) findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
              //  Intent intent = new Intent(context,SettingsActivity.class);
                //startActivity(intent);
            }
        });
    }

}
