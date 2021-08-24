package com.moh.moclock;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;

import com.takisoft.preferencex.EditTextPreference;
import com.takisoft.preferencex.PreferenceFragmentCompat;

import java.util.Objects;

import com.moh.moclock.MoClock.MoClockSugestions.MoClockSuggestionManager;
import com.moh.moclock.MoIntents.MoIntents;
import com.moh.moclock.MoPreference.MoPreference;
import com.moh.moclock.MoPreference.MoPreferenceManager;
import com.moh.moclock.MoSharedPref.MoSharedPref;
import com.moh.moclock.MoTheme.MoTheme;

import com.moh.moclock.R;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;

//import androidx.preference.PreferenceFragmentCompat;

public class MoSettingsActivity extends AppCompatActivity {


    private static final String CONTRADICTION_SMART_VOICE = "The whole purpose of Smart voice cancel" +
            " is to turn off your alarm without touching your phone. Therefore, we turned of Smart Wake";

    private static final String CONTRADICTION_SMART_WAKE = "Smart wake and Smart voice cancel can not be on at the same time";

    private ImageButton back;
    private SettingsFragment settingsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        settingsFragment = new SettingsFragment(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, settingsFragment)
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        init();
    }


    private void init(){
        this.back = findViewById(R.id.setting_back_button);

        backSetting();
    }

    private void backSetting(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            switch (requestCode){
                case SettingsFragment.ALARM_MUSIC_CHANGE:
                    updateMusicPath(R.string.alarm_music,uri);
                    break;
                case SettingsFragment.TIMER_MUSIC_CHANGE:
                    updateMusicPath(R.string.timer_music,uri);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * update the music uri for either timer or alarm
     * and also update the summary of the preference to
     * the location of the music
     * @param type
     * @param uri
     */
    private void updateMusicPath(int type,Uri uri){
        if(uri!=null){
            settingsFragment.editor.putString(getString(type),getRealPathFromURI(this,uri));
            settingsFragment.editor.commit();
            settingsFragment.onSharedPreferenceChanged(settingsFragment.sharedPreferences,getString(type));
        }
    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
        String[] projection = { MediaStore.Audio.Media.DATA };
        CursorLoader loader = new CursorLoader(context, contentUri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = 0;
        if (cursor != null) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
        }
        String s = Objects.requireNonNull(cursor).getString(column_index);
        cursor.close();
        return s;
    }

    @Override
    public void onRequestPermissionsResult(
            final int requestCode,
            @NonNull final String[] permissions,
            @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case SettingsFragment.VOICE_RECORDING_PERMISSION:
                if (0 < grantResults.length && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Audio recording permissions denied. You can not use this feature. We only use your audio for smart voice canceling and it is not recorded by us.", Toast.LENGTH_LONG).show();
                    settingsFragment.turnOffSmartVoiceCancel();
                }
                break;
                case SettingsFragment.CAMERA_PERMISSION:
                    if (0 < grantResults.length && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Camera permissions denied. You can not use this feature. We only use camera for object detection.", Toast.LENGTH_LONG).show();
                        settingsFragment.turnOffSmartAlarmCancel();
                    }
                    break;
            case SettingsFragment.READ_EXTERNAL_STORAGE_PERMISSION:
                // now they can choose an alarm
                break;
        }

    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements
            SharedPreferences.OnSharedPreferenceChangeListener {



        private static final int VOICE_RECORDING_PERMISSION = 1;
        private static final int CAMERA_PERMISSION = 2;
        private static final int ALARM_MUSIC_CHANGE = 3;
        private static final int TIMER_MUSIC_CHANGE = 4;
        private static final int READ_EXTERNAL_STORAGE_PERMISSION = 5;

        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        MoPreferenceManager preferenceManager;
        MoPreference alarmSoundButton;
        Preference clearAlarmSoundButton;
        MoPreference timerSoundButton;
        Preference clearTimerSoundButton;
        Preference clearSmartSuggestionsButton;
        Preference resetSettingsButton;
        Activity a;

        public SettingsFragment(Activity a){
            this.a = a;
        }


        public SettingsFragment(){}


        /**
         * Called during onCreate(Bundle) to supply the preferences for this fragment. Subclasses are
         * expected to call setPreferenceScreen(PreferenceScreen) either directly or via helper methods
         * such as addPreferencesFromResource(int).
         *
         * @param savedInstanceState If the fragment is being re-created from a previous saved state,
         *                           this is the state.
         * @param rootKey            If non-null, this preference fragment should be rooted at the
         */
        @Override
        public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            this.a = getActivity();
            init();
        }


        @SuppressLint("CommitPrefEdits")
        private void init(){
            sharedPreferences = getPreferenceScreen().getSharedPreferences();
            editor = sharedPreferences.edit();
            initSetAlarmMusic();
            initSetTimerMusic();
            initClearAlarmMusic();
            initClearTimerMusic();
            initClearSuggestions();
            initResetSettings();
            initPrefManager();

            onSelectItemSmartAlarmShake(sharedPreferences,getString(R.string.smart_alarm_list));
        }


        private void initPrefManager(){
            this.preferenceManager = new MoPreferenceManager()
                    .add(this.alarmSoundButton)
                    .add(this.timerSoundButton);
        }

        private void initResetSettings() {
            this.resetSettingsButton = findPreference(getString(R.string.reset_settings));
            if (this.resetSettingsButton != null) {
                this.resetSettingsButton.setOnPreferenceClickListener(preference -> {

                    new AlertDialog.Builder(a).setPositiveButton("Yes", (dialogInterface, i) -> {
                        resetWholeSettings();
                        dialogInterface.dismiss();
                        Toast.makeText(a,"Reset was successful!",Toast.LENGTH_SHORT).show();
                    }).setMessage("Do you want to reset the settings?")
                            .setNegativeButton("No", (dialogInterface, i) -> {
                               dialogInterface.dismiss();
                            }).setTitle("Reset Settings").show();
                    return false;
                });
            }
        }

        /**
         * resets the entire setting
         */
        private void resetWholeSettings(){
            editor.clear().apply();
            // recreating it to see the effects
            a.recreate();
        }


        @SuppressLint("DefaultLocale")
        private void initClearSuggestions() {
            this.clearSmartSuggestionsButton = findPreference(getString(R.string.clear_suggestions));
            if (this.clearSmartSuggestionsButton != null) {
                this.clearSmartSuggestionsButton.setOnPreferenceClickListener(preference -> {
                    new AlertDialog.Builder(a).setPositiveButton("Yes", (dialogInterface, i) -> {
                        MoClockSuggestionManager.reset(a);
                        dialogInterface.dismiss();
                        Toast.makeText(a,"Smart suggestion data was erased!",Toast.LENGTH_SHORT).show();
                    }).setMessage(String.format("We currently have made %d improvements to smart suggestion. " +
                            "Do you want to clear Smart Suggestions Data?", MoClockSuggestionManager.size()))
                            .setNegativeButton("No", (dialogInterface, i) -> {
                                dialogInterface.dismiss();
                            }).setTitle("Clear Smart Suggestions").show();

                    return false;
                });
            }
        }

        private void initClearAlarmMusic() {
            this.clearAlarmSoundButton = findPreference(a.getString(R.string.alarm_music_clear));
            if (this.clearAlarmSoundButton != null) {
                this.clearAlarmSoundButton.setOnPreferenceClickListener(preference -> {
                    editor.putString(getString(R.string.alarm_music),"").apply();
                    Toast.makeText(a,getString(R.string.alarm_music_clear_toast),Toast.LENGTH_SHORT).show();
                    return false;
                });
            }
        }

        private void initClearTimerMusic() {
            this.clearTimerSoundButton = findPreference(a.getString(R.string.timer_music_clear));
            if (this.clearTimerSoundButton != null) {
                this.clearTimerSoundButton.setOnPreferenceClickListener(preference -> {
                    editor.putString(getString(R.string.timer_music),"").apply();
                    Toast.makeText(a,getString(R.string.timer_music_clear_toast),Toast.LENGTH_SHORT).show();
                    return false;
                });
            }
        }

        private void initSetAlarmMusic() {
            alarmSoundButton = new MoPreference(findPreference(a.getString(R.string.alarm_music))).setOnPreferenceClickListener(preference -> {
                if(getReadExternalStoragePermission()){
                    MoIntents.openMusicPicker(a,ALARM_MUSIC_CHANGE);
                }
                return false;
            }).setNormalSummary(getString(R.string.alarm_music_summary)).setUpdateSummary(true);
        }

        private void initSetTimerMusic() {
            timerSoundButton = new MoPreference(findPreference(a.getString(R.string.timer_music))).setOnPreferenceClickListener(preference -> {
                if(getReadExternalStoragePermission()) {
                    MoIntents.openMusicPicker(a, TIMER_MUSIC_CHANGE);
                }
                return false;
            }).setNormalSummary(getString(R.string.timer_music_summary))
            .setUpdateSummary(true);
        }




        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }


        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            MoSharedPref.loadAll(a);
            if(s.equals(getString(R.string.smart_alarm_list))){
                // we choose an item from the list of smart alarm shake
                onSelectItemSmartAlarmShake(sharedPreferences,s);
            }else if(s.equals(getString(R.string.smart_timer_text))){
                // only 4 digits allowed
                onSelectTimerText(sharedPreferences,s);
            }else if(s.equals(getString(R.string.theme_version))){
                MoTheme.updateTheme(a);
            }else if (s.equals(getString(R.string.smart_voice_cancel))){
                getVoiceRecordingPermission();
            }else if(s.equals(getString(R.string.smart_alarm_cancel_switch))){
                getCameraPermission();
            }
            this.preferenceManager.update(sharedPreferences,s);
        }

        // get recording permission
        // turn it off if they dont allow it
        private void getVoiceRecordingPermission() {
            if (ActivityCompat.checkSelfPermission(a,
                    RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(a, new String[]{RECORD_AUDIO}, VOICE_RECORDING_PERMISSION);
            }
        }

        // if they have selected object detector get a permission
        // or change it to tap and tell them via toast
        void turnOffSmartVoiceCancel(){
            SwitchPreference p = findPreference(getString(R.string.smart_voice_cancel));
            if (p != null) {
                p.setChecked(false);
            }
        }

        private void getCameraPermission(){
            if (ActivityCompat.checkSelfPermission(a, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(a, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
            }
        }

        void turnOffSmartAlarmCancel(){
            SwitchPreference p = findPreference(getString(R.string.smart_alarm_cancel_switch));
            if (p != null) {
                p.setChecked(false);
            }
        }


        private boolean getReadExternalStoragePermission() {
            if (ActivityCompat.checkSelfPermission(a,
                    READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(a, new String[]{READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION);
                return false;
            }else{
                return true;
            }
        }



        private void onSelectItemSmartAlarmShake(SharedPreferences sharedPreferences, String s){
            String val = sharedPreferences.getString(s,"null");
            Preference p = findPreference(getString(R.string.smart_alarm_button));
            if(p == null){
                return;
            }

            if(val.equals(getString(R.string.custom))){
                p.setEnabled(true);
            }else{
                p.setEnabled(false);
            }

        }

        private void onSelectTimerText(SharedPreferences sharedPreferences, String s){
            String val = sharedPreferences.getString(s,"");
            if(val.length() > 4){
                // only 4 digits are allowed
               // int color = getColor(R.color.error_color);
                EditTextPreference editTextPreference = findPreference(getString(R.string.smart_timer_text));
                editTextPreference.setText("");
                Toast.makeText(getContext(),"Only four characters are allowed",Toast.LENGTH_LONG).show();
            }
        }




    }
}