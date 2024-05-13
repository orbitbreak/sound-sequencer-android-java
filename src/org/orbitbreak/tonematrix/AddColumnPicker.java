
package org.orbitbreak.tonematrix;

import org.orbitbreak.tonematrix.R;
import org.orbitbreak.tonematrix.picker.NumberPicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

public class AddColumnPicker extends Activity {
    NumberPicker np;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("TEST", "Creating picker");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_columns);
        np = (NumberPicker) findViewById(R.id.pref_num_picker);
        np.setRange(1, 10);
        np.setCurrent(1);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            Bundle b = new Bundle();
            Log.e("TEST", "Return " + np.getCurrent());
            b.putInt("amount", np.getCurrent());
            Intent i = new Intent();
            i.putExtras(b);
            setResult(1, i);
            finish();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
