package edu.weber.nk60019.animalcrossingalarmclock;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;


/**
 * A simple {@link Fragment} subclass.
 */
public class DialogClockFragment extends DialogFragment {

    private TimePicker timePicker;
    private PassAlarm mCallBack;

    public interface PassAlarm{
        void setUpAlarmDisplay(Alarm alarm);
        void runTuneDialog();
    }

    public DialogClockFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (PassAlarm) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement the PassAlarm interface");
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_clock, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);

        timePicker = (TimePicker) v.findViewById(R.id.timePicker);
        builder.setMessage(R.string.specify_time)
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCallBack.setUpAlarmDisplay(setAlarm(timePicker.getCurrentHour(), timePicker.getCurrentMinute()));
                        mCallBack.runTuneDialog();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }

    private Alarm setAlarm(int hour, int minute) {

        String period;
        String sMinute = String.valueOf(minute);

        if(hour >= 12) {
            period = getString(R.string.PM);
            hour -= 12;
        } else {
            period = getString(R.string.AM);
        }

        if(hour == 0){
            hour = 12;
        }

        if(minute < 10){
            sMinute = getString(R.string.zero) + minute;
        }

        return new Alarm(hour, sMinute, period);
    }
}