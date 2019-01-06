package com.exxeta.bibleschedule.app;

import android.app.Application;
import android.util.Log;

import com.exxeta.bibleschedule.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Start this class, before MainActivity,
 * rewrite realm database if boolean value is set.
 * <p/>
 */
public class App extends Application {
    private static final String TAG = "App";

    private static final String TEST_DB_FILE_NAME = "scheduleBible.realm";
    private static final boolean SHOULD_OVERWRITE_DATABASE_ON_APP_STARTUP = true;

    @Override
    public void onCreate() {
        super.onCreate();

        if (SHOULD_OVERWRITE_DATABASE_ON_APP_STARTUP) {
            copyBundledRealmFile(this.getResources().openRawResource(R.raw.testdb));
        } else {
            if (!fileFound(TEST_DB_FILE_NAME, this.getFilesDir())) {
                copyBundledRealmFile(this.getResources().openRawResource(R.raw.testdb));
            }
        }
        //Config Realm for the application
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(TEST_DB_FILE_NAME)
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);
    }

    private void copyBundledRealmFile(InputStream inputStream) {
        File file = new File(this.getFilesDir(), App.TEST_DB_FILE_NAME);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error in copy bundle realm file " + e.getMessage());
        }
    }

    public boolean fileFound(String name, File file) {
        File[] list = file.listFiles();
        if (list != null)
            for (File fil : list) {
                if (fil.isDirectory()) {
                    fileFound(name, fil);
                } else if (name.equalsIgnoreCase(fil.getName())) {
                    return true;
                }
            }
        return false;
    }
}
