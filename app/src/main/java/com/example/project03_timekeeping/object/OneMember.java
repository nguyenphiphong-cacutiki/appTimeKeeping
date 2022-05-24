package com.example.project03_timekeeping.object;

public class OneMember {
    private String date;
    private String time;
    private String note;

    public OneMember(String date, String time, String note) {
        this.note = note;
        this.date = date;
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
