package edu.weber.nk60019.animalcrossingalarmclock;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogSnoozeFragment extends DialogFragment {

    private int selected;

    public DialogSnoozeFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        TextView alarmDisplay = (TextView) getActivity().findViewById(R.id.alarmDisplay);


        final CharSequence[] stringList = new CharSequence[]{
                (getString(R.string.off)),
                (getString(R.string.three_minutes)),
                (getString(R.string.five_minutes)),
                (getString(R.string.ten_minutes)),
                (getString(R.string.fifteen_minutes)),
                (getString(R.string.thirty_minutes))
        };

        builder.setTitle(R.string.select_snooze_setting);

        builder.setSingleChoiceItems(stringList, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selected = which;
            }
        })
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setSnoozeText();
                    }
                });
        builder.create();

        return builder.show();
    }

    private void setSnoozeText(){
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        TextView selectedSnooze = (TextView) getActivity().findViewById(R.id.snooze_settings);
        switch(selected){
            case 0:
                selectedSnooze.setText(R.string.off);
                spEdit.putString("snoozeSetting", getString(R.string.off));
                break;
            case 1:
                selectedSnooze.setText(R.string.three_minutes);
                spEdit.putString("snoozeSetting", getString(R.string.three_minutes));
                break;
            case 2:
                selectedSnooze.setText(R.string.five_minutes);
                spEdit.putString("snoozeSetting", getString(R.string.five_minutes));
                break;
            case 3:
                selectedSnooze.setText(R.string.ten_minutes);
                spEdit.putString("snoozeSetting", getString(R.string.ten_minutes));
                break;
            case 4:
                selectedSnooze.setText(R.string.fifteen_minutes);
                spEdit.putString("snoozeSetting", getString(R.string.fifteen_minutes));
                break;
            case 5:
                selectedSnooze.setText(R.string.thirty_minutes);
                spEdit.putString("snoozeSetting", getString(R.string.thirty_minutes));
                break;
        }
        spEdit.commit();
    }

    public String getSnooze(Activity activity) {
        SharedPreferences sp = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        return sp.getString("snoozeSetting", activity.getString(R.string.default_snooze));
    }

}
