package com.kmab.lancet.zimbabwe;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class XDatePicker {

    private static int yearX, monthX, dayX;
    private static int hourX, minuteX;
    private static final int xDATE_DIALOG_ID = 275;
    private static final int xTIME_DIALOG_ID = 282;

    private Context context;
    TextView tvTime, tvDate;

    public XDatePicker(Context context, TextView tvTime, TextView tvDate) {
        this.context = context;
        this.tvTime = tvTime;
        this.tvDate = tvDate;

        Calendar calendar = Calendar.getInstance();
        yearX = calendar.get(Calendar.YEAR);
        monthX = calendar.get(Calendar.MONTH);
        dayX = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void pickDate() {
        createFancyDateTimePicker(xDATE_DIALOG_ID).show();
    }

    public void pickTime() {
        createFancyDateTimePicker(xTIME_DIALOG_ID).show();
    }

    protected Dialog createFancyDateTimePicker(int id) {
        Calendar calendar = Calendar.getInstance();
        switch (id) {
            case xDATE_DIALOG_ID:
                DatePickerDialog dialog = new DatePickerDialog(context, xDateSetListener, yearX, monthX, dayX);
                //dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

                return dialog;

            case xTIME_DIALOG_ID:
                hourX = calendar.get(Calendar.HOUR_OF_DAY);
                minuteX = calendar.get(Calendar.MINUTE);

                return new TimePickerDialog(context, xTimeSetListener, hourX, minuteX, true);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener xDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            yearX = year;
            monthX = monthOfYear;
            dayX = dayOfMonth;
            String builder = getTheValue(dayX) + "-" + StaticVals.monthsSmall[monthX + 1] + "-" + getTheValue(yearX).substring(2, 4);
            tvDate.setText(builder);

            //checkTimeDateValues();
            generateTimestamp(monthX, yearX, dayX);
        }
    };

    private TimePickerDialog.OnTimeSetListener xTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hourX = hourOfDay;
            //hourY = hourOfDay + 5;
            minuteX = minute;
            String builder = getTheValue(hourX) + ":" + getTheValue(minuteX);
            tvTime.setText(builder);

            //checkTimeDateValues("x");
        }
    };

    public void checkTimeDateValues(){
        generateTimestamp(monthX, yearX, dayX);
    }

    public void generateTimestamp(int month, int year, int day) {

        long timestamp;

        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.DAY_OF_MONTH, day);
        timestamp = c.getTimeInMillis();

        SendForm.dob = timestamp;
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
