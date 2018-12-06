package edu.weber.nk60019.animalcrossingalarmclock;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSnooze extends Fragment {

    private SnoozeSelection mCallBack;

    public interface SnoozeSelection{
        String getSnoozeSetting(Activity activity);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (SnoozeSelection) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement the SnoozeSelection interface");
        }
    }

    public FragmentSnooze() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView selectedSnooze = (TextView) getActivity().findViewById(R.id.snooze_settings);
        if (selectedSnooze != null)
            selectedSnooze.setText(mCallBack.getSnoozeSetting(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_snooze, container, false);
    }

}
