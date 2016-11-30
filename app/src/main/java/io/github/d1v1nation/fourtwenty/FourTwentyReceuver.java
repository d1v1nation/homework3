package io.github.d1v1nation.fourtwenty;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * @author d1v1nation (catanaut@gmail.com)
 *         <p>
 *         30.11.16 of fourtwenty | io.github.d1v1nation.fourtwenty
 */

public class FourTwentyReceuver extends BroadcastReceiver {
    private final FourTwentyService callback;

    public FourTwentyReceuver(FourTwentyService callback) {
        super();
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar c = Calendar.getInstance();
        if (c.get(Calendar.HOUR) == 4 && c.get(Calendar.MINUTE) == 20) {
            Toast.makeText(context, "4:20", Toast.LENGTH_LONG).show();
            callback.setTimeState(FourTwentyService.State.IT_IS);
        } else {
            callback.setTimeState(FourTwentyService.State.IT_ISNT);
        }
    }


}
