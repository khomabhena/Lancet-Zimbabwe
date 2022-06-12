package com.kmab.lancet.zimbabwe;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

class AdapterSubmissions extends RecyclerView.Adapter<AdapterSubmissions.Holder> {

    private List listAdapter;
    Context context;
    List<String> listTime;

    public AdapterSubmissions(List listAdapter, Context context) {
        this.listAdapter = listAdapter;
        this.context = context;
        listTime = new ArrayList<>();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.row_submissions;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        Holder holder = new Holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bind((SetterForm) listAdapter.get(position));
    }

    @Override
    public int getItemCount() {
        return listAdapter.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView tvTime, tvPatientName, tvSurname, tvDOB, tvRelationship, tvGender, tvSpecimenType, tvTissueSample;
        CardView cardRow, cardMedicalAid, cardReply, cardForm;

        Holder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            cardRow = itemView.findViewById(R.id.cardRow);
            tvPatientName = itemView.findViewById(R.id.tvPatientName);
            tvSurname = itemView.findViewById(R.id.tvSurname);
            tvDOB = itemView.findViewById(R.id.tvDOB);
            tvRelationship = itemView.findViewById(R.id.tvRelationship);
            tvGender = itemView.findViewById(R.id.tvGender);
            tvSpecimenType = itemView.findViewById(R.id.tvSpecimenType);
            cardMedicalAid = itemView.findViewById(R.id.cardMedicalAid);
            cardReply = itemView.findViewById(R.id.cardReply);
            cardForm = itemView.findViewById(R.id.cardForm);
            tvTissueSample = itemView.findViewById(R.id.tvTissueSample);
        }

        void bind(final SetterForm setter) {
            String gender;
            if (setter.isMale())
                gender = "Male";
            else
                gender = "Female";

            tvTime.setText(getSubmissionDate(setter.getTimestamp()));
            tvPatientName.setText(setter.getPatientName());
            tvSurname.setText(setter.getPatientSurname());
            tvDOB.setText(getDate(setter.getPatientDOB(), false));
            tvRelationship.setText(setter.getRelationshipToMember());
            tvGender.setText(gender);
            tvSpecimenType.setText(setter.getSpecimenType());
            tvTissueSample.setText(setter.getTissueSample());

            cardMedicalAid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchIntent(setter.getMedicalLink());
                }
            });
            cardForm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchIntent(setter.getFormLink());
                }
            });
            cardReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, Reply.class);
                    intent.putExtra("position", getAdapterPosition());
                    context.startActivity(intent);
                }
            });
            cardRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MemberDetails.class);
                    intent.putExtra("isTimeline", false);
                    intent.putExtra("position", getAdapterPosition());
                    context.startActivity(intent);
                }
            });
        }

        private void launchIntent(String medicalLink) {
            Intent intent = new Intent(context, DisplayImage.class);
            intent.putExtra("link", medicalLink);
            context.startActivity(intent);
        }

        private String getSubmissionDate(long timestamp) {
            if (getDate(timestamp, false).equals(getDate(System.currentTimeMillis(), false))) {
                return "Today, " + getDate(timestamp, true);
            } else {
                return getDate(timestamp, false) + ", " + getDate(timestamp, true);
            }
        }

        String getDate(long timeReceived, boolean isTime) {
            String[] monthsSmall = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timeReceived);


            if (isTime) {
                return "" + getTheValue(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + getTheValue(calendar.get(Calendar.MINUTE));
            } else {
                return "" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + monthsSmall[calendar.get(Calendar.MONTH) + 1] + "-" +
                        getYear(calendar.get(Calendar.YEAR));
            }
            //

        }

        private String getYear(int year) {
            String year2 = String.valueOf(year);
            return year2.substring(2, 4);
        }

        public String getTheValue(int value) {
            String theValue = String.valueOf(value);
            if (theValue.length() == 1) {
                return "0" + theValue;
            } else {
                return theValue;
            }
        }

    }

}