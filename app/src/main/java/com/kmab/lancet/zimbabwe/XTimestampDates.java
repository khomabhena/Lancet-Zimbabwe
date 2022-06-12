package com.kmab.lancet.zimbabwe;

import java.util.Calendar;

public class XTimestampDates {


    public XTimestampDates() {
    }

    String getDate(long timestamp) {
        String[] monthsSmall = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        Calendar calendar = Calendar.getInstance();

            calendar.setTimeInMillis(timestamp);
            return "" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + monthsSmall[calendar.get(Calendar.MONTH) +1] + "-" +
                    calendar.get(Calendar.YEAR);
    }

    public String getTheValue(int value){
        String theValue = String.valueOf(value);
        if (theValue.length() == 1){
            return "0"+theValue;
        } else {
            return theValue;
        }
    }

}
