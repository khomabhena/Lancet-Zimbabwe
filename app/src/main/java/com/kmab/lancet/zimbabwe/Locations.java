package com.kmab.lancet.zimbabwe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class Locations extends AppCompatActivity {

    FloatingActionButton fabAdd;
    Context context;
    List list;

    DBOperations dbOperations;
    SQLiteDatabase db;
    List listKeys;
    ListView listView;
    ProgressBar progressBar;

    Adapter adapter;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        dbOperations = new DBOperations(this);
        db = dbOperations.getWritableDatabase();
        listKeys = dbOperations.getLocationKeys(db);
        list = new ArrayList<>();
        prefs = getSharedPreferences(AppInfo.USER_INFO, Context.MODE_PRIVATE);
        adapter = new Adapter(context, R.layout.row_locations);

        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressBar);
        fabAdd = findViewById(R.id.fabAdd);

        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);

        loadAllComments();
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, AddLocation.class));
            }
        });

        if (!prefs.getBoolean(AppInfo.IS_ADMIN, false))
            fabAdd.setVisibility(View.GONE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new BackTask().execute();
    }

    private void loadAllComments() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance()
                .getReference()
                .child(StaticVals.childLocations)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        if (!dataSnapshot.exists())
                            return;

                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            SetterLocation setter = snapshot.getValue(SetterLocation.class);

                            new InsertLocations(context, listKeys).execute(setter);
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





    private class BackTask extends AsyncTask<Void, SetterLocation, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            Cursor cursor = dbOperations.getLocations(db);
            int count = cursor.getCount();

            String key, building, address, city;

            list = new ArrayList();
            while (cursor.moveToNext()) {

                key = cursor.getString(cursor.getColumnIndex(DBContract.Locations.KEY));
                building = cursor.getString(cursor.getColumnIndex(DBContract.Locations.BUILDING));
                address = cursor.getString(cursor.getColumnIndex(DBContract.Locations.ADDRESS));
                city = cursor.getString(cursor.getColumnIndex(DBContract.Locations.CITY));

                SetterLocation setter = new SetterLocation(key, building, address, city);

                publishProgress(setter);
            }

            return count;
        }

        @Override
        protected void onProgressUpdate(SetterLocation... values) {
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

        public void add(SetterLocation object) {
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
                row = layoutInflater.inflate(R.layout.row_locations, parent, false);
                holder = new Holder();

                holder.tvBuilding = (TextView) row.findViewById(R.id.tvBuilding);
                holder.tvAddress = (TextView) row.findViewById(R.id.tvAddress);
                holder.tvCity = (TextView) row.findViewById(R.id.tvCity);
                holder.cardLocation = row.findViewById(R.id.cardLocation);
                holder.cardRow = row.findViewById(R.id.cardRow);

                row.setTag(holder);
            } else {
                holder = (Holder) row.getTag();
            }

            final SetterLocation setter = (SetterLocation) getItem(position);

            holder.tvBuilding.setText(setter.getBuilding());
            holder.tvAddress.setText(setter.getAddress());
            holder.tvCity.setText(setter.getCity());

            if (prefs.getBoolean(AppInfo.IS_ADMIN, false))
                holder.cardRow.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        FirebaseDatabase.getInstance().getReference()
                                .child(StaticVals.childLocations)
                                .child(setter.getKey())
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();

                                        adapter.notifyDataSetChanged();
                                    }
                                });
                        return true;
                    }
                });
            holder.cardLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=1600 " +
                            setter.getBuilding().trim().replace(" ", "+") + ", " +
                            setter.getAddress().trim().replace(" ", "+") + ", " +
                            setter.getCity());

                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            });


            return row;

        }

        private class Holder {
            CardView cardLocation, cardRow;
            TextView tvBuilding, tvAddress, tvCity;
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
