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
public class FragmentMode extends Fragment {

    private ModeSelection mCallBack;

    public interface ModeSelection{
        String getModeSelection(Activity activity);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (ModeSelection) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement the ModeSelection interface");
        }
    }

    public FragmentMode() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView selectedMode = (TextView) getActivity().findViewById(R.id.selected_mode);
        if (selectedMode != null)
            selectedMode.setText(mCallBack.getModeSelection(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mode, container, false);
    }

}
