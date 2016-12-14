package io.github.d1v1nation.fourtwenty;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;


/**
 * @author d1v1nation (catanaut@gmail.com)
 *         <p>
 *         30.11.16 of fourtwenty | io.github.d1v1nation.fourtwenty
 */

public class AsyncLoader extends AsyncTask<Bundle, Unit, FourTwentyService.State> {
    private static final String TAG = "420L";
    private static final int BUFSZ = 8192;
    private final FourTwentyService callback;

    public AsyncLoader(FourTwentyService callback) {
        this.callback = callback;
    }

    @Override
    protected FourTwentyService.State doInBackground(Bundle... params) {
        int index = 0;
        for (Bundle b : params) {
            URL url = (URL) b.getSerializable("url");
            File file = (File) b.getSerializable("file");

            Log.d(TAG, "doInBackground: url " + url.toString());
            try {
                URLConnection c = url.openConnection();
                c.setConnectTimeout(10000);
                c.setReadTimeout(30000);

                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                BufferedInputStream bis = new BufferedInputStream(c.getInputStream());


                byte[] buf = new byte[BUFSZ];
                int size;
                while ((size = bis.read(buf)) != -1) {
                    bos.write(buf, 0, size);
                }

                bis.close();
                bos.close();

            } catch (IOException e) {
                Log.e(TAG, "doInBackground: fetching shenanigans on Bundle no. " + index);
                e.printStackTrace();
                return FourTwentyService.State.IT_ISNT;
            }

        }

        return FourTwentyService.State.IT_IS;
    }

    @Override
    protected void onPostExecute(FourTwentyService.State st) {
        super.onPostExecute(st);

        callback.setImageState(FourTwentyService.State.IT_IS);
        Log.d(TAG, "onPostExecute: fetch complete");
    }
}
