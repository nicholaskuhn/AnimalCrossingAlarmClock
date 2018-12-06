package edu.weber.nk60019.animalcrossingalarmclock;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

public class AlarmService extends Service {

    private MediaPlayer mp;
    private boolean running;
    int startId;
    private Vibrator v;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Get extra string values
        String state = intent.getExtras().getString("Extra");
        int songId = intent.getExtras().getInt("ExtraSongId");
        String alarmName = intent.getExtras().getString("ExtraAlarmName");
        int alarmVolume = intent.getExtras().getInt("ExtraAlarmVolume");
        int currVolume = intent.getExtras().getInt("ExtraCurrVolume");
        String modeSelection = intent.getExtras().getString("ExtraModeSelection");
        AudioManager manager = (AudioManager) this.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        if(state != null){
            switch (state) {
                case "on":
                    startId = 1;
                    break;
                case "off":
                    startId = 0;
                    break;
                default:
                    startId = 0;
                    break;
            }
        }

        // if no music playing and user pressed "alarm on"
        // music start playing
        if (!running && startId == 1) {

            if(modeSelection.equals("Sound only") || modeSelection.equals("Vibrate with sound")){
                manager.setStreamVolume(AudioManager.STREAM_MUSIC, alarmVolume, 0);
                // Create and play song file
                mp = MediaPlayer.create(this, songId);
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mp.setLooping(true);
                mp.start();
            }
            if(modeSelection.equals("Vibrate only") || modeSelection.equals("Vibrate with sound")){
                long[] pattern = {0, 500, 1000};
                v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(pattern, 0);
            }

            // Set notification
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // Set Main Activity intent
            Intent intentMain = new Intent(this.getApplicationContext(), MainActivity.class);
            // Set pending intent
            PendingIntent pendingIntentMain = PendingIntent.getActivity(this, 0, intentMain, 0);

            if (alarmName.equals("")){
                alarmName = getString(R.string.alarm);
            } else {
                alarmName += getString(R.string._alarm);
            }
            // Define notification parameters
            Notification notification = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.ic_alarm_on_black_24dp)
                    .setContentTitle(alarmName.substring(0,1).toUpperCase() + alarmName.substring(1) + getString(R.string.going_off))
                    .setContentText(getString(R.string.click_me))
                    .setContentIntent(pendingIntentMain)
                    .setAutoCancel(true)
                    .build();

            // Set notification call
            notificationManager.notify(0, notification);

            running = true;
            this.startId = 1;
        }
        // if there is music playing, and the user pressed "alarm off"
        // music should stop playing
        else if (running && startId == 0) {

            // Stop the song file
            if(mp != null){
                mp.stop();
                mp.release();
                mp = null;
                manager.setStreamVolume(AudioManager.STREAM_MUSIC, currVolume, 0);
            }
            if(v != null){
                v.cancel();
            }

            running = false;
            this.startId = 0;
        }

        // these are to bugproof app
        // if no music playing and user presses alarm off
        else if (!running && startId == 0) {

            this.running = false;
            this.startId = 0;
        }
        // if there is music playing and the user pressed alarm on
        else if (running && startId == 1) {
            running = false;
            this.startId = 0;
        }
        // just to catch any odd events
        else {
            running = false;
            this.startId = 0;
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

        // Tell the user we stopped.
        super.onDestroy();
        running = false;
    }

}
