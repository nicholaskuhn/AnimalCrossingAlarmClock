package edu.weber.nk60019.animalcrossingalarmclock;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSong extends Fragment {

    private View root;
    private TuneSelection mCallBack;

    public interface TuneSelection {
        String getTuneSelection(Activity activity);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (TuneSelection) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement the TuneSelection interface");
        }
    }

    public FragmentSong() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView selectedTune = (TextView) getActivity().findViewById(R.id.selected_tune);
        if (selectedTune != null)
            selectedTune.setText(mCallBack.getTuneSelection(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_song, container, false);

        return root;
    }

    public void setDefault(){
        TextView selected_tune = root.findViewById(R.id.selected_tune);
        selected_tune.setText(R.string.default_song);
    }
}
