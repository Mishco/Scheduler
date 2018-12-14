package com.exxeta.bibleschedule.app;

import android.app.Application;

import com.exxeta.bibleschedule.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {
    private static final String testDbFileName = "scheduleBible.realm";
    // FIXME reset database after all run
    private static final boolean shouldOverwriteDatabaseOnAppStartup = false;

    @Override
    public void onCreate() {
        super.onCreate();

        if(shouldOverwriteDatabaseOnAppStartup) {
            copyBundledRealmFile(this.getResources().openRawResource(R.raw.testdb),testDbFileName );
        } else {
            if(!fileFound(testDbFileName, this.getFilesDir())) {
                copyBundledRealmFile(this.getResources().openRawResource(R.raw.testdb), testDbFileName);
            }
        }
        //Config Realm for the application
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(testDbFileName)
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);
    }

    private void copyBundledRealmFile(InputStream inputStream, String outFileName) {
        try {
            File file = new File(this.getFilesDir(), outFileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
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
