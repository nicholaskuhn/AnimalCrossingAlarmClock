package edu.weber.nk60019.animalcrossingalarmclock;

public class Alarm {


    private int hour;
    private String minute;
    private String period;

    public Alarm(int hour, String minute, String period) {
        this.hour = hour;
        this.minute = minute;
        this.period = period;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
