package com.exxeta.bibleschedule.realm;

import android.content.res.Resources;
import android.util.Log;

import com.exxeta.bibleschedule.R;
import com.exxeta.bibleschedule.model.Schedule;

import java.io.IOException;
import java.io.InputStream;

import io.realm.Realm;

public class RealmImporter {
    private static final String TAG = "RealmImporter";

    private RealmImporter() {
    }

    public static void importFromJson(final Resources resources) {
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(realmInner -> {
            InputStream inputStream = resources.openRawResource(R.raw.coordinates_2019);
            try {
                realmInner.createAllFromJson(Schedule.class, inputStream);
            } catch (IOException e) {
                Log.e(TAG, "Error with importing data from json " + e.getMessage());
            } finally {
                realmInner.close();
            }
        });

        Log.d("Realm", "createAllFromJson Task completed");
    }
}
