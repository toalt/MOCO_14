package garduino.moco.htwg.de.moco_ws14;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class ManualModeActivity extends Activity {

    private ToggleButton air,water;
    private Button back;
    private SeekBar seekBar;
    private TextView seekView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manueal_mode);
        addListenerOnButtonBack();
        addListenerOnSeekBar();
    }

    //back into Greenhouse
    public void addListenerOnButtonBack () {
        final Context context = this;
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context,InsideActivity.class);
                startActivity(intent);
            }
        });
    }


    //manual mode cooling on/off
    public void addListenerOnButtonAir () {
        final Context context = this;
        air = (ToggleButton) findViewById(R.id.toggleButtonAir);

    }

    //manual mode water on/off
    public void addListenerOnButtonWater () {
        final Context context = this;
        water = (ToggleButton) findViewById(R.id.toggleButtonWater);

    }

    //SeekBar and setText to View
    public void addListenerOnSeekBar () {
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekView = (TextView) findViewById(R.id.setProgress);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
                seekView.setText(String.valueOf(progress)+" %");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
     }
}

