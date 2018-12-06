package edu.weber.nk60019.animalcrossingalarmclock;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRepeat extends Fragment {

    private RepeatSelection mCallBack;
    private View root;

    public interface RepeatSelection{
        String getRepeatSelection(Activity activity);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (RepeatSelection) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement the RepeatSelection interface");
        }
    }


    public FragmentRepeat() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView selectedRepeat = (TextView) getActivity().findViewById(R.id.selected_repeat);
        if (selectedRepeat != null)
            selectedRepeat.setText(mCallBack.getRepeatSelection(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_repeat, container, false);

    }
}
