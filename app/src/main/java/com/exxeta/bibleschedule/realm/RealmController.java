package com.exxeta.bibleschedule.realm;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;

import com.exxeta.bibleschedule.model.Schedule;

import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmController {
    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {
        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {
        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {
        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {
        return instance;
    }

    public Realm getRealm() {
        return realm;
    }

    //Refresh the realm istance
    public void refresh() {
        realm.refresh();
    }

    //clear all objects from Book.class
    public void clearAll() {
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }

    //find all objects in the Book.class
    public RealmResults<Schedule> getBooks() {
        return realm.where(Schedule.class).findAll();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public RealmResults<Schedule> getScheduleFromMonth(YearMonth actMonth) {
        
        Date dateFrom = Date.from(actMonth.atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateTo = Date.from(actMonth.atEndOfMonth().atStartOfDay(ZoneId.systemDefault()).toInstant());
        return realm.where(Schedule.class).between("date", dateFrom, dateTo).findAll();
    }


    //query a single item with the given id
    public Schedule getBook(String id) {
        return realm.where(Schedule.class).equalTo("id", id).findFirst();
    }

    //check if Book.class is empty
    public boolean hasBooks() {
        return !realm.where(Schedule.class).findAll().isEmpty();
    }

    //query example
    public RealmResults<Schedule> queryedBooks() {
        return realm.where(Schedule.class)
                .contains("author", "Author 0")
                .or()
                .contains("title", "Realm")
                .findAll();

    }
}
