package com.moengage.machinecoding.network;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

class NetworkManager {

    private static final String TAG = "NetworkManager";
    private static final int BUFFER_SIZE = 1024;
    private static final int MAX_DELAY = 5;

    static byte[] getJson(@NonNull Context context, @NonNull String filename) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];

        InputStream inputStream = getJsonStream(context, filename);

        if (inputStream == null) {
            return new byte[0];
        }

        try {
            int length;
            while(-1 != (length = inputStream.read(buffer))) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            Log.e(TAG, "Exception when reading JSON", e);
        }

        return outputStream.toByteArray();
    }

    @Nullable
    static InputStream getJsonStream(@NonNull Context context, @NonNull String filename) {
        artificialLatency();

        try {
            return context.getAssets().open(filename);
        } catch (IOException e) {
            Log.e(TAG, "Couldn't load JSON file " + filename, e);
            return null;
        }
    }

    private static void artificialLatency() {
        long delay = (long) (Math.random() * MAX_DELAY + 1);

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(delay));
        } catch (InterruptedException e) {
            Log.d(TAG, "Thread sleep interrupted");
        }
    }
}