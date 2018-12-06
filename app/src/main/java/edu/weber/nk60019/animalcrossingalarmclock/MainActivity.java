package edu.weber.nk60019.animalcrossingalarmclock;

import android.app.Activity;
import android.media.MediaPlayer;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements DialogClockFragment.PassAlarm,
        FragmentSong.TuneSelection,
        FragmentClock.SetAlarmInfo,
        DialogTuneFragment.TuneSet,
        FragmentVolume.SetVolume,
        FragmentMode.ModeSelection,
        FragmentRepeat.RepeatSelection,
        FragmentSnooze.SnoozeSelection,
        DialogRepeatFragment.CheckRepeat{

    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.fragmentClock, new FragmentClock(), "FragmentClock")
                .replace(R.id.fragmentVolume, new FragmentVolume(), "FragmentVolume")
                .replace(R.id.fragmentTune, new FragmentSong(), "FragmentSong")
                .replace(R.id.fragmentRepeat, new FragmentRepeat(), "FragmentRepeat")
                .replace(R.id.fragmentMode, new FragmentMode(), "FragmentMode")
                .replace(R.id.fragmentSnooze, new FragmentSnooze(), "FragmentSnooze")
                .replace(R.id.fragmentName, new FragmentName(), "FragmentName")
                .commit();

        setUpHandlers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.save_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save:
                saveAlarmMain();
                return true;

            case R.id.snooze:
                snoozeAlarmMain();
                return true;

            case R.id.close:
                stopAlarmMain();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setUpAlarmDisplay(Alarm alarm) {

        FragmentClock fragmentClock = (FragmentClock) fm.findFragmentById(R.id.fragmentClock);
        if (fragmentClock != null) {
            fragmentClock.setAlarmDisplay(alarm);
        }
    }

    private void saveAlarmMain() {

        FragmentClock fragmentClock = (FragmentClock) fm.findFragmentById(R.id.fragmentClock);
        if (fragmentClock != null) {
            fragmentClock.saveAlarmDisplay();
            Alarm alarm = fragmentClock.getAlarm();
            fragmentClock.saveAlarm(alarm);
        }
    }

    private void snoozeAlarmMain() {

        FragmentClock fragmentClock = (FragmentClock) fm.findFragmentById(R.id.fragmentClock);
        if (fragmentClock != null) {
            fragmentClock.snoozeAlarm();
        }
    }

    private void stopAlarmMain() {

        FragmentClock fragmentClock = (FragmentClock) fm.findFragmentById(R.id.fragmentClock);

        if (fragmentClock != null) {
            fragmentClock.stopAlarm();
        }
    }

    @Override
    public String getTuneSelection(Activity activity) {
        DialogTuneFragment dialogTuneFragment = new DialogTuneFragment();
        return dialogTuneFragment.getSelectedTune(activity);
    }

    @Override
    public String getModeSelection(Activity activity) {
        DialogModeFragment dialogModeFragment = new DialogModeFragment();
        return dialogModeFragment.getMode(activity);
    }

    @Override
    public boolean[] getDaysBooleanSelection(Activity activity) {
        DialogRepeatFragment dialogRepeatFragment = new DialogRepeatFragment();
        return dialogRepeatFragment.getDaysBool(activity);
    }

    @Override
    public String getSnoozeSetting(Activity activity) {
        DialogSnoozeFragment dialogSnoozeFragment = new DialogSnoozeFragment();
        return dialogSnoozeFragment.getSnooze(activity);
    }

    @Override
    public String getRepeatSelection(Activity activity) {
        DialogRepeatFragment dialogRepeatFragment = new DialogRepeatFragment();
        return dialogRepeatFragment.getDaysString(activity);
    }

    @Override
    public int getSongAlarmId(Activity activity) {
        DialogTuneFragment dialogTuneFragment = new DialogTuneFragment();
        return dialogTuneFragment.getSongResId(activity);
    }

    @Override
    public String getAlarmName(Activity activity) {
        FragmentName fragmentName = (FragmentName) fm.findFragmentById(R.id.fragmentName);
        if (fragmentName != null)
            return fragmentName.getAlarmName(activity);
        else
            return "";
    }

    @Override
    public int[] getAlarmVolume(Activity activity) {
        FragmentVolume fragmentVolume = (FragmentVolume) fm.findFragmentById(R.id.fragmentVolume);
        if (fragmentVolume != null)
            return fragmentVolume.getVolume(activity);
        else{
            return new int[]{4,4};
        }
    }

    @Override
    public void runTuneDialog() {
        DialogTuneFragment dialogTuneFragment = new DialogTuneFragment();
        dialogTuneFragment.setCancelable(false);
        dialogTuneFragment.show(getSupportFragmentManager(), "tune_dialog");
    }

    @Override
    public void checkTuneSet(int which) {
        if (which == -1) {
            DialogTuneFragment dialogTuneFragment = new DialogTuneFragment();
            dialogTuneFragment.setCancelable(false);
            dialogTuneFragment.show(getSupportFragmentManager(), "tune_dialog");

            Toast toast = Toast.makeText(this, R.string.please_select_tune, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void checkRepeat(boolean set) {
        if (!set){
            DialogRepeatFragment dialogRepeatFragment = new DialogRepeatFragment();
            dialogRepeatFragment.setCancelable(true);
            dialogRepeatFragment.show(getSupportFragmentManager(), "repeat_dialog");

            Toast toast = Toast.makeText(this, R.string.please_select_repeat, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void setUpHandlers() {

        findViewById(R.id.fragmentClock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogClockFragment dialogClockFragment = new DialogClockFragment();
                dialogClockFragment.setCancelable(true);
                dialogClockFragment.show(getSupportFragmentManager(), "clock_dialog");
            }
        });

        findViewById(R.id.fragmentTune).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogTuneFragment dialogTuneFragment = new DialogTuneFragment();
                dialogTuneFragment.setCancelable(false);
                dialogTuneFragment.show(getSupportFragmentManager(), "tune_dialog");
            }
        });

        findViewById(R.id.fragmentRepeat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogRepeatFragment dialogRepeatFragment = new DialogRepeatFragment();
                dialogRepeatFragment.setCancelable(true);
                dialogRepeatFragment.show(getSupportFragmentManager(), "repeat_dialog");
            }
        });

        findViewById(R.id.fragmentMode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogModeFragment dialogModeFragment = new DialogModeFragment();
                dialogModeFragment.setCancelable(true);
                dialogModeFragment.show(getSupportFragmentManager(), "mode_dialog");
            }
        });

        findViewById(R.id.fragmentSnooze).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSnoozeFragment dialogSnoozeFragment = new DialogSnoozeFragment();
                dialogSnoozeFragment.setCancelable(true);
                dialogSnoozeFragment.show(getSupportFragmentManager(), "snooze_dialog");
            }
        });
    }
}
