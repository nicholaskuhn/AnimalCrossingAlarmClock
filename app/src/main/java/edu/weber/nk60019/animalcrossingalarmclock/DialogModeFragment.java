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
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogModeFragment extends DialogFragment {

    int selected = -1;

    public DialogModeFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final CharSequence[] stringList = new CharSequence[]{
                (getString(R.string.sound_only)),
                (getString(R.string.vibrate_only)),
                (getString(R.string.vibrate_with_sound))
        };

        builder.setTitle(R.string.select_mode);

        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        selected = sp.getInt("modeSetting", -1);
        if (selected != -1)
            setModeText();
        builder.setSingleChoiceItems(stringList, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selected = which;
            }
        })
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setModeText();
                    }
                });

        return builder.create();
    }

    private void setModeText(){

        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        TextView selectedMode = (TextView) getActivity().findViewById(R.id.selected_mode);
        switch(selected){
            case 0:
                selectedMode.setText(R.string.sound_only);
                spEdit.putString("modeName", getString(R.string.sound_only));
                break;
            case 1:
                selectedMode.setText(R.string.vibrate_only);
                spEdit.putString("modeName", getString(R.string.vibrate_only));
                break;
            case 2:
                selectedMode.setText(R.string.vibrate_with_sound);
                spEdit.putString("modeName", getString(R.string.vibrate_with_sound));
                break;
            default:
                selectedMode.setText(R.string.select_mode);
                spEdit.putString("modeName", getString(R.string.sound_only));
                break;
        }
        spEdit.commit();
    }

    public String getMode(Activity activity) {
        SharedPreferences sp = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        return sp.getString("modeName", activity.getString(R.string.default_mode));
    }
}
