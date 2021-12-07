package com.orangeelephant.sobriety.counter;

import java.io.Serializable;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.NoSuchElementException;

public class Counter implements Serializable {
    private final int _id;
    private final String name;
    private final long start_time_in_millis;
    private final long record_time_sober_in_millis;
    private long time_sober_in_millis;
    private final Dictionary reasons_dict;
    private final String time_sober_string;

    public Counter(int _id, String name, Long start_time_in_millis, Long record_time_sober_in_millis,
                   Dictionary reasons_dict, String time_sober_string) {
        this._id = _id;
        this.name = name;
        this.start_time_in_millis = start_time_in_millis;
        this.record_time_sober_in_millis = record_time_sober_in_millis;
        this.reasons_dict = reasons_dict;
        this.time_sober_string = time_sober_string;
    }

    public String getName() {
        return this.name;
    }

    public int get_id(){
        return this._id;
    }
    
    public String getTimeSoberMessage(Long timeSoberInMillis) {
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = timeSoberInMillis / daysInMilli;
        timeSoberInMillis = timeSoberInMillis % daysInMilli;

        long elapsedHours = timeSoberInMillis / hoursInMilli;
        timeSoberInMillis = timeSoberInMillis % hoursInMilli;

        long elapsedMinutes = timeSoberInMillis / minutesInMilli;
        timeSoberInMillis = timeSoberInMillis % minutesInMilli;

        long elapsedSeconds = timeSoberInMillis / secondsInMilli;

        String time_sober_message = String.format(this.time_sober_string, elapsedDays, elapsedHours,
                elapsedMinutes, elapsedSeconds);

        return time_sober_message;
    }

    public Long getRecordTimeSoberInMillis() {
        return Math.max(time_sober_in_millis, record_time_sober_in_millis);
    }

    public Long getCurrentTimeSoberInMillis() {
        Date now = new Date();
        long timeNow = now.getTime();

        long timeSoberInMillis = timeNow - this.start_time_in_millis;
        this.time_sober_in_millis = timeSoberInMillis;

        return timeSoberInMillis;
    }

    public String getSobrietyReason() {
        try {
            return (String) this.reasons_dict.elements().nextElement();
        } catch (NoSuchElementException elementException) {
            return null;
        }
    }

    public Dictionary getReasons_dict() {
        return this.reasons_dict;
    }
}
