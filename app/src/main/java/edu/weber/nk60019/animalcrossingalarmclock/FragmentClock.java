package edu.weber.nk60019.animalcrossingalarmclock;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentClock extends Fragment {

    private View root;
    private TextView alarmDisplay;
    private Alarm mainAlarm;
    private AlarmManager[] alarmManager = new AlarmManager[7];
    private Context context;
    private Calendar calendar;
    private Intent[] intent = new Intent[7];
    private PendingIntent pendingIntent;
    private SetAlarmInfo mCallBack;

    public interface SetAlarmInfo {
        int getSongAlarmId(Activity activity);

        String getAlarmName(Activity activity);

        int[] getAlarmVolume(Activity activity);

        String getModeSelection(Activity activity);

        boolean[] getDaysBooleanSelection(Activity activity);

        String getSnoozeSetting(Activity activity);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (SetAlarmInfo) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement the SetAlarmInfo interface");
        }
    }

    public FragmentClock() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_clock, container, false);

        // Set Context
        context = getActivity();
        for (int i = 0; i < alarmManager.length; i++)
            alarmManager[i] = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        calendar = Calendar.getInstance();
        for (int i = 0; i < alarmManager.length; i++)
            intent[i] = new Intent(context, AlarmReceiver.class);

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();

        saveAlarmDisplay();
    }

    @Override
    public void onResume() {
        super.onResume();

        retrieveAlarmDisplay();
    }

    public void saveAlarmDisplay() {
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        alarmDisplay = (TextView) root.findViewById(R.id.alarmDisplay);
        if (alarmDisplay != null) {
            spEdit.putString("alarmDisplay", alarmDisplay.getText().toString());
            spEdit.commit();
        }
    }

    private void retrieveAlarmDisplay() {
        alarmDisplay = (TextView) root.findViewById(R.id.alarmDisplay);
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        if (alarmDisplay != null) {
            alarmDisplay.setText(sp.getString("alarmDisplay", getString(R.string.def_alarm_value)));
        }
        Gson gson = new Gson();
        String json = sp.getString("alarmObj", "");
        if (!json.equals(""))
            mainAlarm = gson.fromJson(json, Alarm.class);
    }

    public void setAlarmDisplay(Alarm alarm) {

        mainAlarm = alarm;

        String timeSet = new StringBuilder()
                .append(alarm.getHour())
                .append(':')
                .append(alarm.getMinute())
                .append(' ')
                .append(alarm.getPeriod()).toString();

        alarmDisplay = root.findViewById(R.id.alarmDisplay);
        alarmDisplay.setText(timeSet);
    }

    public Alarm getAlarm() {

        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("alarmObj", "");
        if (!json.equals("") || mainAlarm != null) {
            if (mainAlarm.getHour() == 0 && mainAlarm.getMinute().equals("0") && mainAlarm.getPeriod().equals("AM")) {
                mainAlarm = gson.fromJson(json, Alarm.class);
            }

            return mainAlarm;
        } else {
            return new Alarm(0, "0", "AM");
        }
    }

    public void saveAlarm(Alarm alarm) {

        // Save alarm in shared preferences
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(alarm);
        spEdit.putString("alarmObj", json);
        spEdit.commit();
        if (mainAlarm == null)
            mainAlarm = new Alarm(0, "0", "AM");
        boolean daySelected = false;
        long[] toastArray = new long[7];
        Calendar currCalendar = Calendar.getInstance();
        int currDay = currCalendar.get(Calendar.DAY_OF_WEEK);

        boolean[] daysBool = mCallBack.getDaysBooleanSelection(getActivity());

        for (boolean day : daysBool) {
            if (day)
                daySelected = true;
        }
        if (!daySelected) {
            daysBool[currDay - 1] = true;
        }

        for (int i = 0; i < daysBool.length; i++) {
            if (daysBool[i]) {

                int songId = mCallBack.getSongAlarmId(getActivity());
                String alarmName = mCallBack.getAlarmName(getActivity());
                int alarmVolume = mCallBack.getAlarmVolume(getActivity())[0];
                String modeSelection = mCallBack.getModeSelection(getActivity());

                // Set extras in intent
                intent[i].putExtra("Extra", "on");
                intent[i].putExtra("ExtraSongId", songId);
                intent[i].putExtra("ExtraAlarmName", alarmName);
                intent[i].putExtra("ExtraAlarmVolume", alarmVolume);
                intent[i].putExtra("ExtraModeSelection", modeSelection);

                // Setup alarm to run in background
                if (alarm.getPeriod().equals("PM"))
                    calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour() + 12);
                else{
                    if (alarm.getHour() == 12){
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                    } else {
                        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
                    }
                }

                long addMillis = 0;

                if (i < currDay - 1) {
                    int daysToAdd = getNextWeekDate(i, currDay - 1);
                    calendar.set(Calendar.DAY_OF_WEEK, currDay + daysToAdd);
                    addMillis = 86400000 * (daysToAdd + (currDay - 1) - i);
                } else if (i == currDay - 1) {
                    calendar.set(Calendar.DAY_OF_WEEK, currDay + 7);
                    addMillis = 86400000 * 7;
                } else {
                    calendar.set(Calendar.DAY_OF_WEEK, i + 1);
                }
                calendar.set(Calendar.MINUTE, Integer.valueOf(alarm.getMinute()));
                calendar.set(Calendar.SECOND, 0);

                saveModeInPref(mCallBack.getModeSelection(getActivity()));

                // Setup pending intent
                pendingIntent = PendingIntent.getBroadcast(context,
                        i, intent[i], PendingIntent.FLAG_UPDATE_CURRENT);

                long alarmTimeMillis = calendar.getTimeInMillis();

                if (alarmTimeMillis < System.currentTimeMillis())
                    alarmTimeMillis += addMillis;

                toastArray[i] = alarmTimeMillis;

                // Set the alarm manager
                alarmManager[i].set(AlarmManager.RTC_WAKEUP, alarmTimeMillis,
                        pendingIntent);
            }
        }
        findLowest(toastArray);
    }

    public void snoozeAlarm() {

        boolean daySelected = false;

        stopAlarm();

        Calendar currCalendar = Calendar.getInstance();
        int currDay = currCalendar.get(Calendar.DAY_OF_WEEK);

        boolean[] daysBool = mCallBack.getDaysBooleanSelection(getActivity());

        for (boolean day : daysBool) {
            if (day)
                daySelected = true;
        }
        if (!daySelected) {
            daysBool[currDay - 1] = true;
        }

        int i = currDay - 1;
        int snoozeTime = 0;

        String snoozeSetting = mCallBack.getSnoozeSetting(getActivity());
        if (!snoozeSetting.equals("Off")) {
            snoozeTime = Integer.parseInt((snoozeSetting.split("\\s+"))[0]);
        }

        int songId = mCallBack.getSongAlarmId(getActivity());
        String alarmName = mCallBack.getAlarmName(getActivity());
        int alarmVolume = mCallBack.getAlarmVolume(getActivity())[0];
        String modeSelection = mCallBack.getModeSelection(getActivity());

        // Set extras in intent
        intent[i].putExtra("Extra", "on");
        intent[i].putExtra("ExtraSongId", songId);
        intent[i].putExtra("ExtraAlarmName", alarmName);
        intent[i].putExtra("ExtraAlarmVolume", alarmVolume);
        intent[i].putExtra("ExtraModeSelection", modeSelection);

        // Setup pending intent
        pendingIntent = PendingIntent.getBroadcast(context,
                i, intent[i], PendingIntent.FLAG_UPDATE_CURRENT);

        // Set the alarm manager
        alarmManager[i].set(AlarmManager.RTC_WAKEUP, (snoozeTime * 60000) + System.currentTimeMillis(),
                pendingIntent);

        if (snoozeTime != 0){
            Toast toast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
            toast.setText(getString(R.string.snoozing_for) + snoozeTime + getString(R.string.minutes));
        }
    }


    public void stopAlarm() {

        Calendar currCalendar = Calendar.getInstance();
        int currDay = currCalendar.get(Calendar.DAY_OF_WEEK);
        // Cancel alarm
        if (pendingIntent != null) {
            alarmManager[currDay - 1].cancel(pendingIntent);
        }
        // Set extra in intent

        intent[currDay - 1].putExtra("Extra", "off");
        intent[currDay - 1].putExtra("ExtraCurrVolume", mCallBack.getAlarmVolume(getActivity())[1]);

        // Stop alarm music
        context.sendBroadcast(intent[currDay - 1]);
    }

    public void saveModeInPref(String selected) {
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();

        switch (selected) {
            case "Sound only":
                spEdit.putString("modeName", getString(R.string.sound_only));
                spEdit.putInt("modeSetting", 0);
                break;
            case "Vibrate only":
                spEdit.putString("modeName", getString(R.string.vibrate_only));
                spEdit.putInt("modeSetting", 1);
                break;
            case "Vibrate with sound":
                spEdit.putString("modeName", getString(R.string.vibrate_with_sound));
                spEdit.putInt("modeSetting", 2);
                break;
        }
        spEdit.commit();
    }

    private void findLowest(long[] toastArray) {
        long lowest = Long.MAX_VALUE;

        for (long time : toastArray) {

            if (time < lowest && time != 0) {
                lowest = time;
            }
        }
        sendToast(lowest);
    }

    private void sendToast(long alarmTimeMillis) {
        long diffMills = alarmTimeMillis - System.currentTimeMillis();

        int days = (int) (diffMills / (1000 * 60 * 60 * 24));
        int hours = (int) ((diffMills / (1000 * 60 * 60)) % 24);
        int minutes = (int) ((diffMills / (1000 * 60)) % 60);

        String message = "";

        Toast toast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);

        if (days != 0) {
            toast.setText(message += (days + getString(R.string._days_)));
        }
        if (hours != 0) {
            toast.setText(message += (hours + getString(R.string._hours_)));
        }
        if (minutes != 0) {
            toast.setText(message += (minutes + getString(R.string._minutes_)));
        }
        if (days == 0 && hours == 0 && minutes == 0) {
            toast.setText(message += getString(R.string.less_than));
        }

        toast.setText(message + getString(R.string.from_now));
        toast.show();
    }

    private int getNextWeekDate(int i, int currDay) {
        int daysUntil = 0;
        while (currDay != i) {
            daysUntil++;
            currDay++;

            if (currDay == 7) {
                currDay = 0;
            }
        }
        return daysUntil;
    }
}
