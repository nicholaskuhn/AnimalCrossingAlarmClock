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

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogRepeatFragment extends DialogFragment {

    private TextView selectedRepeat;
    private String[] days;
    private CheckRepeat mCallBack;
    private boolean hasChecked = false;


    public interface CheckRepeat {
        void checkRepeat(boolean set);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (CheckRepeat) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement the CheckRepeat interface");
        }
    }

    public DialogRepeatFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        days = new String[]{
                getString(R.string.sunday),
                getString(R.string.monday),
                getString(R.string.tuesday),
                getString(R.string.wednesday),
                getString(R.string.thursday),
                getString(R.string.friday),
                getString(R.string.saturday)
        };

        final boolean[] checkedDays = new boolean[]{
                false, // Sunday
                false, // Monday
                false, // Tuesday
                false, // Wednesday
                false, // Thursday
                false, // Friday
                false // Saturday

        };

        builder.setTitle(R.string.select_days_to_repeat);

        final List<String> dayList = Arrays.asList(days);

        builder.setMultiChoiceItems(days, checkedDays, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checkedDays[which] = isChecked;
                hasChecked = true;
            }
        })
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveDaysInPref(checkedDays);
                        mCallBack.checkRepeat(hasChecked);
                    }
                });

        return builder.create();
    }

    private void saveDaysInPref(boolean[] checks){
        StringBuilder setDaysString = new StringBuilder();
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        selectedRepeat = (TextView) getActivity().findViewById(R.id.selected_repeat);
        if (selectedRepeat != null) {
            for(int i = 0; i < checks.length; i++){
                spEdit.putBoolean("savedDaysBool" + i, checks[i]);
                if(checks[i]){
                    if (!setDaysString.toString().equals("")){
                        setDaysString.append(", " + days[i]);
                    }
                    else {
                        setDaysString.append(days[i]);
                    }
                }
            }
            spEdit.putString("savedDaysString", setDaysString.toString());
            spEdit.commit();

            selectedRepeat.setText(sp.getString("savedDaysString", getString(R.string.selected_repeat)));
        }
    }

    public String getDaysString(Activity activity){
        SharedPreferences sp = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date date = new Date();
        String dayOfTheWeek = sdf.format(date);
        return sp.getString("savedDaysString", dayOfTheWeek);
    }

    public boolean[] getDaysBool(Activity activity){
        SharedPreferences sp = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        boolean[] daysSetBool = new boolean[7];
        for (int i = 0; i < daysSetBool.length; i++){
            daysSetBool[i] = sp.getBoolean("savedDaysBool" + i, false);
        }
        return daysSetBool;
    }
}
