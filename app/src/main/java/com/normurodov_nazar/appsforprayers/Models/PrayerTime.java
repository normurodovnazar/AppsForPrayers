package com.normurodov_nazar.appsforprayers.Models;

public class PrayerTime {
    final String type;
    final long time;
    final boolean isEnabled;

    public String getType() {
        return type;
    }

    public long getTime() {
        return time;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public PrayerTime(String type, long time, boolean isEnabled) {
        this.type = type;
        this.time = time;
        this.isEnabled = isEnabled;
    }
}
