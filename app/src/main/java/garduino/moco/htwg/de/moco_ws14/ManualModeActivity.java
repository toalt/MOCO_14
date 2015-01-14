package garduino.moco.htwg.de.moco_ws14;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class ManualModeActivity extends Activity {

    private Button button6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manueal_mode);
        addListenerOnButton6();
    }

    //back into Greenhouse
    public void addListenerOnButton6 () {
        final Context context = this;
        button6 = (Button) findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context,InsideActivity.class);
                startActivity(intent);
            }
        });
    }
}
