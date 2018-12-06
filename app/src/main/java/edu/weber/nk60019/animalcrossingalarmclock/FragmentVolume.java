package edu.weber.nk60019.animalcrossingalarmclock;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentVolume extends Fragment {

    private SeekBar seekBar;
    private View root;
    private SetVolume mCallBack;
    private MediaPlayer mediaPlayer;
    private int songId;
    private int currVolume = 4;
    private AudioManager manager;


    public interface SetVolume {
        int getSongAlarmId(Activity activity);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (SetVolume) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement the SetVolume interface");
        }
    }

    public FragmentVolume() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_volume, container, false);

         manager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        saveSeekBar(currVolume);
    }

    @Override
    public void onResume() {
        super.onResume();

        seekBar = (SeekBar)root.findViewById(R.id.seekBar);
        int maxVolume = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        seekBar.setMax(maxVolume);
        loadSeekBar();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                manager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                currVolume = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
                songId = mCallBack.getSongAlarmId(getActivity());
                mediaPlayer = MediaPlayer.create(getActivity(), songId);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.start();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                saveSeekBar(currVolume);
            }
        });


    }

    private void saveSeekBar(int currVolume){

        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        spEdit.putInt("seekValue", seekBar.getProgress());
        spEdit.putInt("currVolume", currVolume);
        manager.setStreamVolume(AudioManager.STREAM_MUSIC, currVolume, 0);
        spEdit.commit();
    }

    private void loadSeekBar(){
        seekBar = (SeekBar)root.findViewById(R.id.seekBar);

        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        int seekValue = sp.getInt("seekValue", 50);
        seekBar.setProgress(seekValue);
    }

    public int[] getVolume(Activity activity){
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        int seekValue = sp.getInt("seekValue", 4);
        currVolume = sp.getInt("currVolume", 4);
        return new int[]{seekValue,currVolume};
    }
}
