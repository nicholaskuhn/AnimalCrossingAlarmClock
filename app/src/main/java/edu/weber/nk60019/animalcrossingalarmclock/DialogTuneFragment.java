package edu.weber.nk60019.animalcrossingalarmclock;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
public class DialogTuneFragment extends DialogFragment {

    private MediaPlayer mediaPlayer;
    private int hour;
    private String period;
    private int selected;
    private TextView selectedSongView;
    String songName;
    private int checkedItem = -1;
    private AudioManager manager;
    private TuneSet mCallBack;

    public interface TuneSet {
        void checkTuneSet(int which);
        int[] getAlarmVolume(Activity activity);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (TuneSet) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement the TuneSet interface");
        }
    }

    public DialogTuneFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        loadTuneInPref();
        manager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        TextView alarmDisplay = (TextView) getActivity().findViewById(R.id.alarmDisplay);

        String[] alarmTime = alarmDisplay.getText().toString().split(":");
        hour = Integer.parseInt(alarmTime[0].trim());
        period = ((alarmTime[1].split("\\s+"))[1]).toLowerCase();

        final CharSequence[] stringList = new CharSequence[]{
                (getString(R.string.pop_growing) + hour + period),
                (getString(R.string.ww_cf) + hour + period),
                (getString(R.string.ww_cf_s) + hour + period),
                (getString(R.string.new_leaf) + hour + period),
                (getString(R.string.new_leaf_r) + hour + period),
                (getString(R.string.new_leaf_s) + hour + period)
        };

        builder.setTitle(R.string.select_tune);

        builder.setSingleChoiceItems(stringList, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }

                manager.setStreamVolume(AudioManager.STREAM_MUSIC, mCallBack.getAlarmVolume(getActivity())[0], 0);

                selected = which;
                int recId = getSongId(which);
                mediaPlayer = MediaPlayer.create(getActivity(), recId);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.start();
                checkedItem = which;
            }
        })
        .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                manager.setStreamVolume(AudioManager.STREAM_MUSIC, mCallBack.getAlarmVolume(getActivity())[1], 0);
                saveTuneInPref((String) stringList[selected], selected);
                mCallBack.checkTuneSet(checkedItem);
            }
        });

        builder.create();

        return builder.show();
    }

    private int getSongId(int which){

        switch(which){
            case 0:
                songName = "af" + hour + period;
                break;
            case 1:
                songName = "cf" + hour + period;
                break;
            case 2:
                songName = "cfs" + hour + period;
                break;
            case 3:
                songName = "nl" + hour + period;
                break;
            case 4:
                songName = "nlr" + hour + period;
                break;
            case 5:
                songName = "nls" + hour + period;
                break;

            default:
                songName = "nl7pm";
                break;
        }
        int resId = getActivity().getResources().getIdentifier(songName, "raw", getActivity().getPackageName());
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        spEdit.putInt("resId", resId);
        spEdit.commit();
        return resId;
    }

    private void saveTuneInPref(String name, int selected){
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        selectedSongView = (TextView) getActivity().findViewById(R.id.selected_tune);
        if (selectedSongView != null) {
            spEdit.putString("savedSong", name);
            spEdit.putInt("savedSelection", selected);
            spEdit.commit();
            selectedSongView.setText(sp.getString("savedSong", getString(R.string.selected_tune)));
        }
    }

    private void loadTuneInPref(){
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        selectedSongView = (TextView) getActivity().findViewById(R.id.selected_tune);
        if (selectedSongView != null) {
            selectedSongView.setText(sp.getString("savedSong", getString(R.string.default_song)));
            spEdit.commit();
        }
    }

    public String getSelectedTune(Activity activity){
        SharedPreferences sp = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        return sp.getString("savedSong", activity.getString(R.string.default_song));
    }

    public int getSongResId(Activity activity){
        SharedPreferences sp = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        return sp.getInt("resId", activity.getResources().getIdentifier("nl12am", "raw", activity.getPackageName()));
    }
}
