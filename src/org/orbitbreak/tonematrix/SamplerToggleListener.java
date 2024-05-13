
package org.orbitbreak.tonematrix;

import org.orbitbreak.tonematrix.sequencer.Sequencer;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

public class SamplerToggleListener implements OnClickListener {

    Sequencer sequencer;

    int beats;

    public SamplerToggleListener(Sequencer sequencer, Context ctx, int samples, int beats) {
        this.sequencer = sequencer;
        this.beats = beats;
    }

    @Override
    public void onClick(View v) {
        int buttonId = v.getId();

        int samplerId = buttonId / this.beats;
        int beatId = buttonId % this.beats;
        if (!(v instanceof ToggleButton)) {
            Log.e("SampleToggleListener", "Bad View type: " + v.getClass());
            return;
        }
        ToggleButton currentButton = (ToggleButton) v;
        if (currentButton.isChecked())
            sequencer.enableCell(samplerId, beatId);
        else
            sequencer.disableCell(samplerId, beatId);
    }
}
