package com.exxeta.bibleschedule;

import android.content.res.Resources;
import android.util.Log;

import com.exxeta.bibleschedule.Model.Schedule;

import java.io.IOException;
import java.io.InputStream;

import io.realm.Realm;

public class RealmImporter {
    static void importFromJson(final Resources resources) {
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                InputStream inputStream = resources.openRawResource(R.raw.coordinates_2019);
                try {
                    realm.createAllFromJson(Schedule.class, inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    realm.close();
                }
            }
        });

        Log.d("Realm", "createAllFromJson Task completed");
    }
}
