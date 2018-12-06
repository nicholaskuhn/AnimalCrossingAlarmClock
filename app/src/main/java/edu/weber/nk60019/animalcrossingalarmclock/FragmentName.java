package edu.weber.nk60019.animalcrossingalarmclock;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentName extends Fragment {

    private View root;
    private TextInputEditText alarmText;

    public FragmentName() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_name, container, false);

        alarmText = (TextInputEditText) root.findViewById(R.id.alarm_name);

        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        alarmText.setText(sp.getString("alarmName", ""));

        alarmText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(alarmText.getWindowToken(), 0);
                    alarmText.clearFocus();
                }
                return false;
            }
        });

        alarmText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor spEdit = sp.edit();
                    spEdit.putString("alarmName", alarmText.getText().toString());
                    spEdit.commit();
                }
            }
        });

        return root;
    }

    public String getAlarmName(Activity activity){
        SharedPreferences sp = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        return sp.getString("alarmName", "");

    }

}
