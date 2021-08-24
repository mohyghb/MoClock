package com.moh.moclock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.moofficial.moessentials.MoEssentials.MoUI.MoInflatorView.MoInflaterView;

import com.moh.moclock.MoAnimation.MoAnimation;
import com.moh.moclock.MoClock.MoTimer.MoTimer;
import com.moh.moclock.MoClock.MoTimer.MoTimerPresetPackage.MoPresetRecyclerAdapter;
import com.moh.moclock.MoClock.MoTimer.MoTimerPresetPackage.MoTimerPreset;
import com.moh.moclock.MoClock.MoTimer.MoTimerPresetPackage.MoTimerPresetManager;
import com.moh.moclock.MoDate.MoTimeUtils;
import com.moh.moclock.MoRunnable.MoRunnable;
import com.moh.moclock.MoSection.MoSectionManager;
import com.moh.moclock.MoSensor.MoShakeListener;
import com.moh.moclock.MoUI.MoTextInput;

import com.moh.moclock.R;

public class MoTimerSectionManager implements MainActivity.SelectModeInterface {
    private final MainActivity mainActivity;
    /**
     * timer
     */

    private final String SMART_SHAKE_TIMER_VALUE = "10";

    private final String ENTER_TIME = "Please enter a time above first";
    ConstraintLayout timer_liner_layout;
    private TextInputEditText hourTimer;
    private TextInputEditText minuteTimer;
    private TextInputEditText secondTimer;
    private ProgressBar progressBar;
    private Button cancelTimer;
    private Button startTimer;
    private Button pauseTimer;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageButton addPresetButton;
    private LinearLayout timer_text_linear_layout;

    Button cancelDeleteButton;
    Button delete;
    private BottomNavigationView bottomNavigation;
    private LinearLayout linearDeleteMode;
    private TextView counterPreset;
    private ConstraintLayout mainLayout;


    public MoTimerSectionManager(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    void initTimerSection() {
        this.hourTimer = mainActivity.findViewById(R.id.hour_timer_tv);
        this.hourTimer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (hourTimer.getText().toString().length() == 2) {
                    minuteTimer.requestFocus();
                }
            }
        });



        this.minuteTimer = mainActivity.findViewById(R.id.minute_timer_tv);
        this.minuteTimer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (minuteTimer.getText().toString().length() == 2) {
                    secondTimer.requestFocus();
                }
            }
        });

        this.secondTimer = mainActivity.findViewById(R.id.seconds_timer_tv);


        this.hourTimer.setOnFocusChangeListener(MoTextInput.twoDigitFocusChangeListener());
        this.minuteTimer.setOnFocusChangeListener(MoTextInput.twoDigitFocusChangeListener());
        this.secondTimer.setOnFocusChangeListener(MoTextInput.twoDigitFocusChangeListener());


        this.cancelTimer = mainActivity.findViewById(R.id.cancel_time_timer_button);
        this.pauseTimer = mainActivity.findViewById(R.id.pause_time_timer_button);
        this.cancelTimer.setVisibility(View.GONE);
        this.pauseTimer.setVisibility(View.GONE);
        this.startTimer = mainActivity.findViewById(R.id.start_timer_button);
        this.progressBar = mainActivity.findViewById(R.id.barTimer);

        this.pauseTimer.setOnClickListener((v) -> {
            if(MoTimer.universalTimer != null && MoTimer.universalTimer.isCreated()) {
                MoTimer.universalTimer.pause(false);
                updatePauseButton(this.pauseTimer, MoTimer.universalTimer.getPauseButtonText());

            }
        });

        this.cancelTimer.setBackgroundColor(mainActivity.getColor(R.color.error_color));
        this.cancelTimer.setOnClickListener((v) -> {
            if(MoTimer.universalTimer.isCreated()){
                MoTimer.universalTimer.cancel(mainActivity);
                changeButtonLayout(true, false, false);
            }
        });

        this.startTimer.setOnClickListener(this::startTimer);


        MoTimerPresetManager.load("",mainActivity);
        initRecyclerView();


        this.addPresetButton = mainActivity.findViewById(R.id.add_timer_preset);
        this.addPresetButton.setOnClickListener(this::addPreset);


        this.cancelDeleteButton = mainActivity.findViewById(R.id.cancel_delete_preset_mode);
        this.cancelDeleteButton.setOnClickListener(view -> {
            cancelDeleteAlarmMode();
        });
        this.bottomNavigation = mainActivity.findViewById(R.id.bottom_navigation);
        this.linearDeleteMode = mainActivity.findViewById(R.id.delete_mode_preset);
        this.delete = mainActivity.findViewById(R.id.delete_preset_button);
        this.delete.setOnClickListener(view -> {
            MoTimerPresetManager.deleteSelected(mainActivity);
            cancelDeleteAlarmMode();
        });
        this.timer_text_linear_layout = mainActivity.findViewById(R.id.timer_text_linear_layout);
        this.counterPreset = mainActivity.findViewById(R.id.preset_counter_textview);
        this.counterPreset.setVisibility(View.INVISIBLE);
        this.mainLayout = mainActivity.findViewById(R.id.main_layout);
        turnDelete(false);


        MoSectionManager.getInstance().subscribe(value -> MoTimer.universalTimer.setUpdateTextViews(value == MoSectionManager.TIMER_SECTION));

    }

    private void updatePauseButton(TextView pauseTimer, String pauseButtonText) {
        pauseTimer.setText(pauseButtonText);
        this.pauseTimer.setBackgroundColor(MoTimer.universalTimer.showingResume()?
                mainActivity.getColor(R.color.resumeButton):mainActivity.getColor(R.color.colorPrimary));
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) mainActivity.findViewById(R.id.preset_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MoPresetRecyclerAdapter(MoTimerPresetManager.getPresets(), mainActivity, new MoRunnable() {
            @Override
            public <T> void run(T... args) {
                setTextsMilli((Long) args[0]);
            }
        }, () -> showDeleteMode(true), new MoRunnable() {
            @Override
            public <T> void run(T... args) {
                onSizeOfSelectedChanged((Integer) args[0]);
            }
        });
        recyclerView.setAdapter(mAdapter);
    }






    private long getMilliSeconds(){
        String hour = this.hourTimer.getText().toString();
        String minute = this.minuteTimer.getText().toString();
        String second = this.secondTimer.getText().toString();
        if(hour.isEmpty() && minute.isEmpty() && second.isEmpty()){
            return MoTimeUtils.getTimeInMilli(hourTimer.getHint().toString(),
                    minuteTimer.getHint().toString(),secondTimer.getHint().toString());
        }
        return MoTimeUtils.getTimeInMilli(hour,minute,second);
    }

    private void showErrorInput(){
        Toast toast= Toast.makeText(mainActivity.getApplicationContext(),
                ENTER_TIME, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    private void setTextsMilli(long milli){
        if(!MoTimer.universalTimer.isCreated()){
            String[] parts = MoTimeUtils.convertMilli(milli);
            this.hourTimer.setText(parts[0]);
            this.minuteTimer.setText(parts[1]);
            this.secondTimer.setText(parts[2]);
        }

    }

    private void addPreset(View v){
        long milliSeconds = getMilliSeconds();
        if (milliSeconds==0) {
            showErrorInput();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
            builder.setTitle("Timer Preset (" + MoTimeUtils.convertToReadableFormat(milliSeconds) + ")");
            builder.setMessage("Set a name to add this timer as a preset");

            View dialogView = MoInflaterView.inflate(R.layout.alert_dialog_timer_preset, mainActivity);
            TextInputEditText input = dialogView.findViewById(R.id.textField_alertDialogTimerPreset);
            builder.setView(dialogView);

            // Set up the buttons
            builder.setPositiveButton("Add", null);
            builder.setNegativeButton("Cancel", null);
            AlertDialog alertDialog = builder.create();

            alertDialog.setOnShowListener(dialogInterface -> {
                Button button = ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(view -> {
                    if (input.getText().toString().isEmpty()) {
                        input.setError("Please enter a name to save this preset");
                        return;
                    }
                    MoTimerPresetManager.add(new MoTimerPreset(input.getText().toString(),milliSeconds),mainActivity);
                    mAdapter.notifyDataSetChanged();
                    //Dismiss once everything is OK.
                    alertDialog.dismiss();
                });
            });

            alertDialog.show();
        }
    }

    public void onSmartShake(MoShakeListener shakeListener){
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        boolean active = s.getBoolean(mainActivity.getString(R.string.smart_timer),false);
        if(active){
            if(MoTimer.universalTimer.isCreated()){
                Snackbar.make(cancelTimer,"Shake detected. However, " +
                        "there is a timer running already. Stop that first.",Snackbar.LENGTH_LONG)
                        .setAnchorView(cancelTimer)
                        .setBackgroundTint(mainActivity.getColor(R.color.error_color))
                        .setTextColor(mainActivity.getColor(R.color.snack_bar_text))
                        .show();
                return;
            }

            shakeListener.checkToVibrate();
            String minutes = s.getString(mainActivity.getString(R.string.smart_timer_text),SMART_SHAKE_TIMER_VALUE);
            if(minutes.isEmpty()){
                minutes = SMART_SHAKE_TIMER_VALUE;
            }
            int mins = Integer.parseInt(minutes);
            //set the timer time on shake
            setTextsMilli(mins*60*1000);
            // and start it
            startTimer(startTimer);
        }

    }

    private void startTimer(View v){
        long milliSeconds = getMilliSeconds();
        if (milliSeconds == 0) {
            showErrorInput();
        } else {
            // startService the timer
            if(MoTimer.universalTimer == null || !MoTimer.universalTimer.isCreated()){
                hideKeyboardFrom(mainActivity,v);
                MoTimer.universalTimer = new MoTimer(new MoRunnable() {
                    @Override
                    public <T> void run(T... args) {
                        changeButtonLayout((Boolean) args[0],(Boolean) args[1],(Boolean) args[2]);
                    }
                }
                        , mainActivity, milliSeconds,
                        this.progressBar,
                        new Button[]{this.startTimer, this.cancelTimer, this.pauseTimer},
                        this.hourTimer, this.minuteTimer, this.secondTimer);
                MoTimer.universalTimer.startTimer();
                updatePauseButton(this.pauseTimer, MoTimer.universalTimer.getPauseButtonText());
                changeButtonLayout(false, true, true);
            }

        }
    }

    private void setTimerValues(int minutes){
        if(minutes > 59){
            this.hourTimer.setText(minutes/60+"");
            this.minuteTimer.setText(minutes%60+"");
        }else {
            this.minuteTimer.setText(minutes <10?"0":""+ minutes);
        }
    }

    private void changeButtonLayout(boolean start, boolean pause, boolean cancel) {
        showRecyclerPreset(start);
        showAddPreset(start);
        MoAnimation.animateNoTag(startTimer,start ? View.VISIBLE : View.GONE,start?MoAnimation.APPEAR:MoAnimation.DISAPPEAR);
        MoAnimation.animateNoTag(cancelTimer,cancel ? View.VISIBLE : View.GONE,cancel?MoAnimation.APPEAR:MoAnimation.DISAPPEAR);
        MoAnimation.animateNoTag(pauseTimer,pause ? View.VISIBLE : View.GONE,pause?MoAnimation.APPEAR:MoAnimation.DISAPPEAR);
    }

    void closeTimerService() {
        if(MoTimerPreset.isInDeleteMode)
            return;

        MoTimer.universalTimer.setTimerTextInputs(this.hourTimer, this.minuteTimer, this.secondTimer);
        MoTimer.universalTimer.setProgressBar(this.progressBar);
        MoTimer.universalTimer.setButtons(this.startTimer, this.cancelTimer, this.pauseTimer);
        MoTimer.universalTimer.cancel(mainActivity, true, true);
        /**
         * ui changes
         */
        if (MoTimer.universalTimer.isCreated()) {
            //mainActivity.changeLayout(false, false, true, true);
            changeButtonLayout(false, true, true);

            updatePauseButton(this.pauseTimer, MoTimer.universalTimer.getPauseButtonText());
        }else {
            MoTimer.universalTimer.update();
           // changeButtonLayout(true, false, false);
            //this.pauseTimer.setText(MoTimer.universalTimer.getPauseButtonText());
        }
    }




    private void onSizeOfSelectedChanged(int size){
        if(size==0){
            // none of them are selected
            cancelDeleteAlarmMode();
        }else{
            turnDelete(true);
        }
        updatePauseButton(counterPreset, String.format("%d Selected",size));
    }



    public void cancelDeleteAlarmMode(){
        showDeleteMode(false);
    }

    private void showDeleteMode(boolean b){
        MoTimerPreset.isInDeleteMode = b;

        if(!b)
            MoAnimation.clearLog();

        showLinearDelete(b);
        showNavigation(!b);
        showStartButton(!b);
        showTimerText(!b);
        showCounter(b);
        showAddPreset(!b);
        mAdapter.notifyDataSetChanged();

        if(!b){
            // reset all the presets
            MoTimerPresetManager.selectAllPresets(false);
        }

        if(!b)
            MoAnimation.clearLog();
    }

    private void showLinearDelete(boolean b){
        MoAnimation.animate(this.linearDeleteMode,b?View.VISIBLE:View.INVISIBLE,
                b?MoAnimation.BOTTOM_TO_TOP_FADE_IN:MoAnimation.MOVE_DOWN_FADE_OUT);
    }

    private void showNavigation(boolean b){
        MoAnimation.animate(this.bottomNavigation,b?View.VISIBLE:View.INVISIBLE,
                b?MoAnimation.BOTTOM_TO_TOP_FADE_IN:MoAnimation.MOVE_DOWN_FADE_OUT);
    }

    private void showStartButton(boolean b){
        MoAnimation.animate(this.startTimer,b?View.VISIBLE:View.INVISIBLE,
                b?MoAnimation.FADE_IN:MoAnimation.FADE_OUT);
    }

    private void showTimerText(boolean b){
        MoAnimation.animate(this.timer_text_linear_layout,b?View.VISIBLE:View.INVISIBLE,
                b?MoAnimation.FADE_IN:MoAnimation.FADE_OUT);
    }

    private void showRecyclerPreset(boolean b){
        MoAnimation.animateNoTag(this.recyclerView,b?View.VISIBLE:View.INVISIBLE,
                b?MoAnimation.FADE_IN:MoAnimation.FADE_OUT);
    }

    private void showCounter(boolean b){
        MoAnimation.animateNoTag(this.counterPreset,b?View.VISIBLE:View.INVISIBLE,
                b?MoAnimation.FADE_IN:MoAnimation.FADE_OUT);
    }

    private void showAddPreset(boolean b){
        MoAnimation.animateNoTag(this.addPresetButton,b?View.VISIBLE:View.INVISIBLE,
                b?MoAnimation.APPEAR:MoAnimation.DISAPPEAR);
    }

    private void turnDelete(boolean on) {
        this.delete.setVisibility(on?View.VISIBLE:View.GONE);
        this.cancelDeleteButton.setVisibility(on?View.GONE:View.VISIBLE);
    }


    public void onWindowFocusChanged(){
        if(MoTimerPreset.isInDeleteMode){
            showDeleteMode(true);
            mAdapter.notifyDataSetChanged();
        }
    }

//    public static void hideKeyboard(Activity activity) {
//        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
//        //Find the currently focused view, so we can grab the correct window token from it.
//        View view = activity.getCurrentFocus();
//        //If no view currently has focus, create a new one, just so we can grab a window token from it
//        if (view == null) {
//            view = new View(activity);
//        }
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean isSelecting() {
        return MoTimerPreset.isInDeleteMode;
    }
}