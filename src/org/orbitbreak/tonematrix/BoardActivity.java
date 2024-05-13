
package org.orbitbreak.tonematrix;

import org.orbitbreak.tonematrix.R;
import org.orbitbreak.tonematrix.sequencer.Sequencer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

public class BoardActivity extends Activity {
    public static final int TOTAL_BEATS = 16;

    public static final int TOTAL_SAMPLES = 16;

    FrameLayout rootLayout;

    LinearLayout mainLayout;

    ProgressBarView progressBarView;

    Sequencer sequencer;

    ToggleButton samplersButtons[][] = new ToggleButton[TOTAL_SAMPLES][TOTAL_BEATS];

    LinearLayout boardLayouts[] = new LinearLayout[TOTAL_SAMPLES];

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sequencer = new Sequencer(this, TOTAL_SAMPLES, TOTAL_BEATS);
        sequencer.setSample(0, R.raw.a6);
        sequencer.setSample(1, R.raw.g6);
        sequencer.setSample(2, R.raw.e6);
        sequencer.setSample(3, R.raw.d6);
        sequencer.setSample(4, R.raw.c6);
        sequencer.setSample(5, R.raw.a5);
        sequencer.setSample(6, R.raw.g5);
        sequencer.setSample(7, R.raw.e5);
        sequencer.setSample(8, R.raw.d5);
        sequencer.setSample(9, R.raw.c5);
        sequencer.setSample(10, R.raw.a4);
        sequencer.setSample(11, R.raw.g4);
        sequencer.setSample(12, R.raw.e4);
        sequencer.setSample(13, R.raw.d4);
        sequencer.setSample(14, R.raw.c4);
        sequencer.setSample(15, R.raw.a3);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        prepareBoard();
    }

    @Override
    public void onPause() {
        super.onPause();
        sequencer.stop();
    }

    @Override
    protected void onResume() {
        sequencer.play();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("TEST", "Menu item click");
        switch (item.getItemId()) {
            case R.id.select_sample:
                // file picker
                Intent i = new Intent(BoardActivity.this, AndroidExplorer.class);
                startActivityForResult(i, 0);
                break;
            case R.id.toggle_sequencer:
                sequencer.toggle();
                break;
            case R.id.preferences:
                Intent preferencesActivity = new Intent(getBaseContext(), Preferences.class);
                startActivityForResult(preferencesActivity, 1);
                break;
            case R.id.add_column:
                Log.e("TEST", "Adding columns");
                Intent addColumnActivity = new Intent(getBaseContext(), AddColumnPicker.class);
                startActivityForResult(addColumnActivity, 2);
                // sequencer.addColumns(amount);
                break;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    String path = data.getStringExtra("path");
                    Log.i("ANDROIDEXPLORER", "path: " + path);
                    sequencer.setSample(3, path);
                } else if (resultCode == RESULT_CANCELED) {
                }
                break;
            case 1:
                if (resultCode == 1) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                    String newBpm = prefs.getString("bpm", "120");
                    Log.e("TEST", "new bpm is " + Integer.parseInt(newBpm));
                    sequencer.setBpm(Integer.parseInt(newBpm));
                }
                break;
            case 2:
                Bundle b = data.getExtras();
                int amount = b.getInt("amount");
                Log.e("TEST", "Adding " + amount + " columns");
                break;
            default:
                break;
        }
    }

    private void prepareBoard() {
        createLayouts();
        setContentView(rootLayout);
        createBoardButtons();
    }

    private void createLayouts() {
        rootLayout = new FrameLayout(this);

        mainLayout = new LinearLayout(this);

        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));
        for (int samplePos = 0; samplePos < TOTAL_SAMPLES; samplePos++) {
            boardLayouts[samplePos] = new LinearLayout(this);
            boardLayouts[samplePos].setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
            boardLayouts[samplePos].setBackgroundColor(Color.rgb(255, 0, 0));
            mainLayout.addView(boardLayouts[samplePos]);
        }
        rootLayout.addView(mainLayout);
    }

    private void createBoardButtons() {
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        int buttonWidth = display.getWidth() / TOTAL_BEATS;
        int buttonHeight = display.getHeight() / TOTAL_SAMPLES;

        progressBarView = new ProgressBarView(this, display.getWidth(), display.getHeight(),
                buttonWidth, sequencer.getBpm());
        sequencer.setOnBPMListener(progressBarView);
        rootLayout.addView(progressBarView);

        SamplerToggleListener samplerListener = new SamplerToggleListener(sequencer, this,
                TOTAL_SAMPLES, TOTAL_BEATS);

        for (int samplePos = 0; samplePos < TOTAL_SAMPLES; samplePos++) {
            Log.d("Board", "Button width: " + buttonWidth);
            for (int beatPos = 0; beatPos < TOTAL_BEATS; beatPos++) {
                samplersButtons[samplePos][beatPos] = new ToggleButton(this);
                samplersButtons[samplePos][beatPos].setTextOff("");
                samplersButtons[samplePos][beatPos].setTextOn("");
                samplersButtons[samplePos][beatPos].setText("");
                samplersButtons[samplePos][beatPos].setWidth(buttonWidth);
                samplersButtons[samplePos][beatPos].setHeight(buttonHeight);
                samplersButtons[samplePos][beatPos].setId(TOTAL_BEATS * samplePos + beatPos);
                samplersButtons[samplePos][beatPos].setOnClickListener(samplerListener);
                boardLayouts[samplePos].addView(samplersButtons[samplePos][beatPos]);
            }
        }
    }
}
