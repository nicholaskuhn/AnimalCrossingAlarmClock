package edu.weber.nk60019.animalcrossingalarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        // Fetch extras from intent
        String onOff = intent.getExtras().getString("Extra");
        int songId = intent.getExtras().getInt("ExtraSongId");
        String alarmName = intent.getExtras().getString("ExtraAlarmName");
        int alarmVolume = intent.getExtras().getInt("ExtraAlarmVolume");
        int currVolume = intent.getExtras().getInt("ExtraCurrVolume");
        String modeSelection = intent.getExtras().getString("ExtraModeSelection");

        // Create intent
        Intent service_intent = new Intent(context, AlarmService.class);

        // Pass the string back to ringtone service
        service_intent.putExtra("Extra", onOff);
        service_intent.putExtra("ExtraSongId", songId);
        service_intent.putExtra("ExtraAlarmName", alarmName);
        service_intent.putExtra("ExtraAlarmVolume", alarmVolume);
        service_intent.putExtra("ExtraCurrVolume", currVolume);
        service_intent.putExtra("ExtraModeSelection", modeSelection);

        // Start service
        ContextCompat.startForegroundService(context, service_intent);
    }
}
