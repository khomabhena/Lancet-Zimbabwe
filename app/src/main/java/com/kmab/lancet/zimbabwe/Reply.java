package com.kmab.lancet.zimbabwe;

import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
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

public class Reply extends AppCompatActivity implements View.OnClickListener {

    int position = 0;
    Context context;
    SetterForm setterForm;
    List list;

    ListView listView;
    ProgressBar progressBar;
    EditText etMessage;
    View viewSend;


    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        list = new ArrayList<>();
        adapter = new Adapter(context, R.layout.row_messages);
        position = getIntent().getExtras().getInt("position");
        setterForm = (SetterForm) Submissions.list.get(position);

        listView = findViewById(R.id.listView);
        etMessage = findViewById(R.id.etMessage);
        progressBar = findViewById(R.id.progressBar);
        viewSend = findViewById(R.id.viewSend);

        viewSend.setOnClickListener(this);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        listView.setStackFromBottom(true);

        loadAllComments();

        String text = "Your request made on " + getDate(setterForm.getTimestamp(), false) +
                ", " + getDate(setterForm.getTimestamp(), true) +
                "\nwas declined/approved.\nYour authorization number is ";

        etMessage.setText(text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.viewSend:
                sendMessage();
                break;
        }
    }

    private void sendMessage() {
        if (etMessage.getText().toString().trim().isEmpty())
            return;

        progressBar.setVisibility(View.VISIBLE);
        String message = etMessage.getText().toString().trim();
        etMessage.setText("");

        SetterMessage setter = new SetterMessage(setterForm.getKey(), setterForm.getUid(), message, false, true, System.currentTimeMillis());

        list = new ArrayList();
        if (list.size() == 0) {
            adapter.add(setter);
            listView.setAdapter(adapter);
        } else {
            adapter.add(setter);
            adapter.notifyDataSetChanged();
            listView.setSelection(listView.getAdapter().getCount() - 1);
        }
        listView.setVisibility(View.VISIBLE);
        sendMessageToDB(setter);
    }

    private void sendMessageToDB(final SetterMessage setter) {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance()
                .getReference()
                .child(StaticVals.childMessages)
                .child(setterForm.getUid())
                .child(setterForm.getKey())
                .setValue(setter)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            setterForm.setSeen(true);
                            FirebaseDatabase.getInstance().getReference()
                                    .child(StaticVals.childSubmissions)
                                    .child(setterForm.getUid())
                                    .child(setterForm.getKey())
                                    .setValue(setterForm)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressBar.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }



    private void loadAllComments() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance()
                .getReference()
                .child(StaticVals.childMessages)
                .child(setterForm.getUid())
                .child(setterForm.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())
                            return;

                        SetterMessage setter = dataSnapshot.getValue(SetterMessage.class);

                        adapter.add(setter);

                        listView.setAdapter(adapter);
                        listView.setSelection(listView.getAdapter().getCount() - 1);
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //
                    }
                });
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
            else
                holder.ivSeen.setImageResource(R.drawable.ic_sent);

            return row;

        }

        private class Holder {
            ImageView ivSeen;
            TextView tvMessage, tvTime, tvDate;
        }

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
