package com.exxeta.bibleschedule.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.exxeta.bibleschedule.Prefs;
import com.exxeta.bibleschedule.R;
import com.exxeta.bibleschedule.model.Schedule;
import com.exxeta.bibleschedule.realm.RealmController;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * https://www.androidhive.info/2016/05/android-working-with-realm-database-replacing-sqlite-core-data/
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class ScheduleAdapter extends RealmRecyclerViewAdapter<Schedule> {
    private final Context context;
    private Realm realm;

    public ScheduleAdapter(Context context) {
        this.context = context;
    }

    // create new views (invoked by the layout manager)
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate a new card view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new CardViewHolder(view);
    }

    // replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        realm = RealmController.getInstance().getRealm();

        // get the article
        final Schedule itemOld = getItem(position);
        final Schedule item = findByDate(itemOld.getDate());
        // cast the generic view holder to our specific one
        final CardViewHolder holder = (CardViewHolder) viewHolder;

        // set the title and the snippet
        holder.textDate.setText(new SimpleDateFormat("dd.MMM").format(item.getDate()));
        holder.textCoordinate.setText(item.getCoordinates());
        holder.checkBoxWasRead.setChecked(item.getWasRead());

        // update single match from realm
        holder.card.setOnLongClickListener(v -> {

            RealmResults<Schedule> results = realm.where(Schedule.class).findAll();

            // Get the book title to show it in toast message
            Schedule b = findByDate(itemOld.getDate());
            String title = b.getCoordinates();


            // All changes to data must happen in a transaction
            realm.beginTransaction();

            // was read single match
            Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
            if (b.getWasRead()) {
                b.setWasRead(false);
            } else {
                b.setWasRead(true);
            }

            b.setWhenWasRead(date);
            realm.insertOrUpdate(b);
            realm.commitTransaction();

            if (results.isEmpty()) {
                Prefs.with(context).setPreLoad(false);
            }

            notifyDataSetChanged();

            Toast.makeText(context, title + " was read and save", Toast.LENGTH_SHORT).show();
            return false;
        });

        //update single match from realm
        holder.card.setOnClickListener(v -> {

            Toast.makeText(context, itemOld.getDate() + " was clicked on", Toast.LENGTH_SHORT).show();
            // TODO open bible text
        });
    }

    private Schedule findByDate(Date date) {
        realm = RealmController.getInstance().getRealm();
        return realm.where(Schedule.class).equalTo("date", date).findFirst();
    }


    // return the size of your data set (invoked by the layout manager)
    public int getItemCount() {
        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }


    private static class CardViewHolder extends RecyclerView.ViewHolder {

        private CardView card;
        private TextView textDate;
        private TextView textCoordinate;
        private CheckBox checkBoxWasRead;

        private CardViewHolder(View itemView) {
            // standard view holder pattern with Butterknife view injection
            super(itemView);

            card = itemView.findViewById(R.id.card_schedules);
            textDate = itemView.findViewById(R.id.text_schedule_date);
            textCoordinate = itemView.findViewById(R.id.text_schedule_coordinate);
            checkBoxWasRead = itemView.findViewById(R.id.checkbox_was_read);
        }

    }
}
