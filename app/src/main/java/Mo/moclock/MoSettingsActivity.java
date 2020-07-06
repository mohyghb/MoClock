package Mo.moclock;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;
import androidx.preference.Preference;
//import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.takisoft.preferencex.EditTextPreference;
import com.takisoft.preferencex.PreferenceFragmentCompat;

import java.util.Objects;

import Mo.moclock.MoIntents.MoIntents;
import Mo.moclock.MoMusic.MoMusicPlayer;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

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

    private void updateMusicPath(int type,Uri uri){
        if(uri!=null){
            settingsFragment.editor.putString(getString(type),getRealPathFromURI(this,uri));
            settingsFragment.editor.commit();
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

    public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {



        private static final int VOICE_RECORDING_PERMISSION = 1;
        private static final int CAMERA_PERMISSION = 2;
        private static final int ALARM_MUSIC_CHANGE = 3;
        private static final int TIMER_MUSIC_CHANGE = 4;
        private static final int READ_EXTERNAL_STORAGE_PERMISSION = 5;

        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        Preference alarmSoundButton;
        Preference timerSoundButton;
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
            init();
        }


        private void init(){
            sharedPreferences = getPreferenceScreen().getSharedPreferences();
            editor = sharedPreferences.edit();
            alarmSoundButton = findPreference(a.getString(R.string.alarm_music));
            if (alarmSoundButton != null) {
                alarmSoundButton.setOnPreferenceClickListener(preference -> {
                    if(getReadExternalStoragePermission()){
                        MoIntents.openMusicPicker(a,ALARM_MUSIC_CHANGE);
                    }

                    return false;
                });
            }
            timerSoundButton = findPreference(a.getString(R.string.timer_music));
            if (timerSoundButton != null) {
                timerSoundButton.setOnPreferenceClickListener(preference -> {
                    if(getReadExternalStoragePermission()) {
                        MoIntents.openMusicPicker(a, TIMER_MUSIC_CHANGE);
                    }
                    return false;
                });
            }
            onSelectItemSmartAlarmShake(sharedPreferences,getString(R.string.smart_alarm_list));
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
            if(s.equals(getString(R.string.smart_alarm_list))){
                // we choose an item from the list of smart alarm shake
                onSelectItemSmartAlarmShake(sharedPreferences,s);
            }else if(s.equals(getString(R.string.smart_timer_text))){
                // only 4 digits allowed
                onSelectTimerText(sharedPreferences,s);
            }else if(s.equals(getString(R.string.always_on_dark))){
                boolean active = sharedPreferences.getBoolean(s,false);
                if(active){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                }
            }else if (s.equals(getString(R.string.smart_voice_cancel))){
                getVoiceRecordingPermission();
            }else if(s.equals(getString(R.string.smart_alarm_cancel_switch))){
                getCameraPermission();
            }

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