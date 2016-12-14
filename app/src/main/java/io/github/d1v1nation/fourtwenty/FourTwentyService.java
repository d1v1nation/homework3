package io.github.d1v1nation.fourtwenty;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;
import java.net.URL;


public class FourTwentyService extends Service {
    public static final String INTENT_TYPE = Intent.ACTION_TIME_CHANGED;

    class FTBinder extends Binder {
        FourTwentyService get() {
            return FourTwentyService.this;
        }
    }
    private FTBinder binderInstance = new FTBinder();

    enum State {
        IT_IS,
        IT_ISNT
    }

    private static final String TAG = "420S";
    private static final String link = "http://i.imgur.com/mMXGamh.jpg";
    private BroadcastReceiver bc;
    private State timeState = State.IT_ISNT;
    private State imageState = State.IT_ISNT;

    public FourTwentyService() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);


        Log.d(TAG, "onStartCommand: Service started.");


        bc = new FourTwentyReceiver(this);
        registerReceiver(bc, new IntentFilter(INTENT_TYPE));
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy: Service died.");

        this.unregisterReceiver(bc);
    }

    @Override
    public IBinder onBind(Intent intent) {
        startService(intent); // self start yooo
        return binderInstance;
    }

    void setTimeState(State st) {
        timeState = st;

        if (st == State.IT_IS) {
            File ftjpeg = new File(getApplicationContext().getFilesDir(), "420.jpg");
            try {
                if (ftjpeg.createNewFile()) {
                    Bundle b = new Bundle();
                    b.putSerializable("file", ftjpeg);
                    URL value = new URL(link);
                    Log.d(TAG, "onStartCommand: " + value.toString());
                    b.putSerializable("url", value);

                    AsyncLoader al = new AsyncLoader(this);
                    al.execute(b);
                } else {
                    setImageState(State.IT_IS);
                }
            } catch (Exception e){
                Log.e(TAG, "onStartCommand: something has gone horribly wrong with the filesystem and stuff.", e);
                setTimeState(State.IT_ISNT);
                setImageState(State.IT_ISNT);
            }
        }

        handleStates();
    }

    void setImageState(State st) {
        imageState = st;
        handleStates();
    }

    private void handleStates() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("internal.meme").putExtra("status",
                timeState.ordinal() * 4 + imageState.ordinal()));

        Log.d(TAG, "handleStates: bc sent: " + timeState.ordinal() * 4 + imageState.ordinal());
    }

    int getStates() {
        return timeState.ordinal() * 4 + imageState.ordinal();
    }
}
