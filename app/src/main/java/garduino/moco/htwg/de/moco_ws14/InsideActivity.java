package garduino.moco.htwg.de.moco_ws14;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class InsideActivity extends Activity {

    private Button button4;
    private Button button5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside);
        addListenerOnButton4();
        addListenerOnButton5();
    }

    //back to StartView
    public void addListenerOnButton4 () {
        final Context context = this;
        button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context,GarduinoMainActivity.class);
                startActivity(intent);
            }
        });
    }

    //launch ManualModeView
    public void addListenerOnButton5 () {
        final Context context = this;
        button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context,ManualModeActivity.class);
                startActivity(intent);
            }
        });
    }

}
