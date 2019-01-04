package com.exxeta.bibleschedule.adapters;

import android.content.Context;

import com.exxeta.bibleschedule.model.Schedule;

import io.realm.RealmResults;

public class RealmScheduleAdapter extends RealmModelAdapter<Schedule> {

    public RealmScheduleAdapter(Context context, RealmResults<Schedule> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }
}