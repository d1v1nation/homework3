package io.github.d1v1nation.fourtwenty;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    FourTwentyService mService;
    boolean mBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            FourTwentyService.FTBinder binder = (FourTwentyService.FTBinder) service;
            mService = binder.get();
            mBound = true;

            Log.d("f", "onServiceConnected: i am alive");

            dispatch(mService.getStates());
        }

        public void onServiceDisconnected(ComponentName className) {
            mBound = false;
        }
    };
    private ProgressBar pb;
    private ImageView img;
    private TextView erroru;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = (ImageView) findViewById(R.id.meme2);
        pb = (ProgressBar) findViewById(R.id.progress);
        erroru = (TextView) findViewById(R.id.error_text);

        Intent i = new Intent(getApplicationContext(), FourTwentyService.class);
        getApplicationContext().bindService(i, mConnection, BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int status = intent.getExtras().getInt("status");
                dispatch(status);
                Log.d("main", "onReceive: bc recv: " + status);
            }
        }, new IntentFilter("internal.meme"));
    }

    private void dispatch(int status) {
        if (status == 4) {
            displayNotTheTime();
        } else if (status == 1) {
            displayLoading();
        } else if (status == 0) {
            displayYes();
        } else {
            displayErroru();
        }
    }

    private void displayErroru() {

        img.setVisibility(View.GONE);
        pb.setVisibility(View.GONE);
        erroru.setVisibility(View.VISIBLE);

        erroru.setText("Things are not what they seem.");
    }

    private void displayYes() {

        img.setVisibility(View.VISIBLE);
        pb.setVisibility(View.GONE);
        erroru.setVisibility(View.GONE);

        try {
            img.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(new File(getFilesDir(), "420.jpg"))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void displayLoading() {
        img.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);
        erroru.setVisibility(View.GONE);
    }

    private void displayNotTheTime() {
        img.setVisibility(View.GONE);
        pb.setVisibility(View.GONE);
        erroru.setVisibility(View.VISIBLE);
        erroru.setText("Snake! Remember to use your Phantom Cigar to skip time!");
    }

}
