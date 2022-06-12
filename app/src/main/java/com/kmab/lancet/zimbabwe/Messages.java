package com.kmab.lancet.zimbabwe;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Messages extends AppCompatActivity {

    Context context;
    List list;

    ListView listView;
    ProgressBar progressBar;

    DBOperations dbOperations;
    SQLiteDatabase db;
    List listKeys;
    Adapter adapter;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        dbOperations = new DBOperations(this);
        db = dbOperations.getWritableDatabase();
        listKeys = dbOperations.getMessageKeys(db);
        list = new ArrayList<>();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        adapter = new Adapter(context, R.layout.row_messages);

        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressBar);

        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new BackTask().execute();
    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance()
                .getReference()
                .child(StaticVals.childMessages)
                .child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())
                            return;

                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            SetterMessage setter = snapshot.getValue(SetterMessage.class);

                            new InsertMessages(context, listKeys).execute(setter);
                        }

                        new BackTask().execute();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //
                    }
                });
    }





    private class BackTask extends AsyncTask<Void, SetterMessage, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            Cursor cursor = dbOperations.getMessages(db);
            int count = cursor.getCount();

            String key, uid, message;
            boolean isSeen, isReplied;
            long timestamp;

            list = new ArrayList();
            while (cursor.moveToNext()) {

                key = cursor.getString(cursor.getColumnIndex(DBContract.Messages.KEY));
                uid = cursor.getString(cursor.getColumnIndex(DBContract.Messages.UID));
                message = cursor.getString(cursor.getColumnIndex(DBContract.Messages.MESSAGE));
                timestamp = Long.parseLong(cursor.getString(cursor.getColumnIndex(DBContract.Messages.TIMESTAMP)));
                String quest = cursor.getString(cursor.getColumnIndex(DBContract.Messages.IS_SEEN));
                isReplied = cursor.getString(cursor.getColumnIndex(DBContract.Messages.IS_REPLIED)).equals("yes");
                isSeen = quest.equals("yes");

                SetterMessage setter = new SetterMessage(key, uid, message, isSeen, isReplied, timestamp);

                if (!isReplied) {
                    count--;
                    continue;
                }
                if (uid.equals(setter.getUid()))
                    publishProgress(setter);
                else
                    count--;
            }

            return count;
        }

        @Override
        protected void onProgressUpdate(SetterMessage... values) {
            adapter.add(values[0]);
        }

        @Override
        protected void onPostExecute(Integer count) {
            if (count != 0) {
                listView.setAdapter(adapter);
                listView.setSelection(listView.getAdapter().getCount() - 1);
            }
        }
    }

    private class Adapter extends ArrayAdapter {

        Adapter(Context context, int resource) {
            super(context, resource);
        }

        public void add(SetterMessage object) {
            list.add(object);
            super.add(object);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @NonNull
        @Override
        public View getView(final int position, final View convertView, ViewGroup parent) {
            View row = convertView;
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final Holder holder;
            if (row == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = layoutInflater.inflate(R.layout.row_messages, parent, false);
                holder = new Holder();

                holder.tvMessage = (TextView) row.findViewById(R.id.tvMessage);
                holder.tvDate = (TextView) row.findViewById(R.id.tvDate);
                holder.tvTime = (TextView) row.findViewById(R.id.tvTime);
                holder.ivSeen = row.findViewById(R.id.ivSeen);

                row.setTag(holder);
            } else {
                holder = (Holder) row.getTag();
            }

            SetterMessage setter = (SetterMessage) getItem(position);

            holder.tvMessage.setText(setter.getMessage());
            holder.tvTime.setText(getDate(setter.getTimestamp(), true));
            holder.tvDate.setText(getDate(setter.getTimestamp(), false));
            if (setter.isSeen())
                holder.ivSeen.setImageResource(R.drawable.ic_seen);
            else {
                holder.ivSeen.setImageResource(R.drawable.ic_sent);
                setter.setSeen(true);
                FirebaseDatabase.getInstance().getReference()
                        .child(StaticVals.childMessages)
                        .child(setter.getUid())
                        .child(setter.getKey())
                        .setValue(setter)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                    adapter.notifyDataSetChanged();
                            }
                        });
            }
            holder.ivSeen.setVisibility(View.GONE);

            return row;

        }

        private class Holder {
            ImageView ivSeen;
            TextView tvMessage, tvTime, tvDate;
        }

        String getDate(long timeReceived, boolean isTime) {
            String[] monthsSmall = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timeReceived);


            if (isTime) {
                return "" + getTheValue(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + getTheValue(calendar.get(Calendar.MINUTE));
            } else {
                return "" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + monthsSmall[calendar.get(Calendar.MONTH) +1] + "-" +
                        getYear(calendar.get(Calendar.YEAR));
            }
        }

        private String getTheValue(int value){
            String theValue = String.valueOf(value);
            if (theValue.length() == 1){
                return "0"+theValue;
            } else {
                return theValue;
            }
        }

        private String getYear(int year) {
            String year2 = String.valueOf(year);
            return year2.substring(2, 4);
        }

    }

}
