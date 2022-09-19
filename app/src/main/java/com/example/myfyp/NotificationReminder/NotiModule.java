package com.example.myfyp.NotificationReminder;

import java.io.Serializable;

public class NotiModule implements Serializable {
    String time;
    boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public NotiModule () {

    }

    public NotiModule(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
