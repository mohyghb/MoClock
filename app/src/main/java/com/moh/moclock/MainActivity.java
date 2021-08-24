package com.moh.moclock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.moh.moclock.MoAnimation.MoAnimation;
import com.moh.moclock.MoClock.MoAlarmClockManager;
import com.moh.moclock.MoClock.MoStopWatch.MoStopWatch;
import com.moh.moclock.MoClock.MoTimer.MoTimer;
import com.moh.moclock.MoClock.MoTimer.MoTimerPresetPackage.MoTimerPreset;
import com.moh.moclock.MoSection.MoSectionManager;
import com.moh.moclock.MoSensor.MoShakeListener;
import com.moh.moclock.MoSharedPref.MoSharedPref;
import com.moh.moclock.MoTheme.MoTheme;

import com.moh.moclock.R;


public class MainActivity extends AppCompatActivity {


    public static boolean isInApp = true;
    private final MoTimerSectionManager moTimerSectionManager = new MoTimerSectionManager(this);
    private final MoStopWatchManager moStopWatchManager = new MoStopWatchManager(this);
    private final MoWorldClockSectionManager moWorldClockSectionManager = new MoWorldClockSectionManager(this);
    private MoAlarmSectionManager moAlarmSectionManager;
    private MoShakeListener smartShake;
    /**
     * world clock
     */
    public static final int ADD_WORLD_CLOCK_CODE = 0;
    private BottomNavigationView bottomNavigation;
    private View bottomDeleteBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case MoAlarmSectionManager.CREATE_ALARM_CODE:
                moAlarmSectionManager.updateAll();
                break;
            case ADD_WORLD_CLOCK_CODE:
                moWorldClockSectionManager.onWorldClockChanged();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void init() {
        MoSharedPref.loadAll(this);
        MoAnimation.initAllAnimations(this);
        this.moWorldClockSectionManager.root = findViewById(R.id.layout_worldClock);
        this.moTimerSectionManager.timer_liner_layout = findViewById(R.id.linear_timer_layout);
        this.bottomDeleteBar = findViewById(R.id.delete_mode_preset);
        this.moTimerSectionManager.initTimerSection();
        this.moStopWatchManager.initStopWatchSection();
        this.initBottomNavigation();
        this.initAlarmSection();
        moWorldClockSectionManager.initWorldClockSection();
        this.initSmartShakeListeners();
        moTimerSectionManager.closeTimerService();
        // adding all the animations to a sparse array
        MoTheme.updateTheme(this);
    }


    private void initSmartShakeListeners() {
        smartShake = new MoShakeListener(true) {
            /**
             * when this class detects a shake
             */
            @Override
            public void onShakeDetected() {
                switch (MoSectionManager.getInstance().getSection()) {
                    case MoSectionManager.ALARM_SECTION:
                        moAlarmSectionManager.onSmartShake(this);
                        break;
                    case MoSectionManager.TIMER_SECTION:
                        moTimerSectionManager.onSmartShake(this);
                        break;
                }
            }
        };
    }


    private void initBottomNavigation() {
        this.bottomNavigation = findViewById(R.id.bottom_navigation);
        this.moStopWatchManager.root.setVisibility(View.INVISIBLE);
        this.moTimerSectionManager.timer_liner_layout.setVisibility(View.INVISIBLE);
        this.bottomNavigation.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.Alarm_Section:
                    changeLayout(true, false, false, false, false);
                    MoSectionManager.getInstance().setSection(MoSectionManager.ALARM_SECTION);
                    return true;
                case R.id.StopWatch_Section:
                    changeLayout(false, true, false, false, false);
                    MoSectionManager.getInstance().setSection(MoSectionManager.STOP_WATCH_SECTION);
                    return true;
                case R.id.Timer_Section:
                    changeLayout(false, false, true, false, false);
                    MoSectionManager.getInstance().setSection(MoSectionManager.TIMER_SECTION);
                    return true;
                case R.id.WorldClock_Section:
                    changeLayout(false, false, false, true, false);
                    MoSectionManager.getInstance().setSection(MoSectionManager.WORLD_CLOCK_SECTION);
                    return true;
                default:
                    return false;
            }
        });
    }

    private void switchSection() {
        switch (MoSectionManager.getInstance().getSection()) {
            case MoSectionManager.ALARM_SECTION:
                changeLayout(true, false, false, false, true);
                break;
            case MoSectionManager.STOP_WATCH_SECTION:
                changeLayout(false, true, false, false, true);
                break;
            case MoSectionManager.TIMER_SECTION:
                changeLayout(false, false, true, false, true);
                break;
            case MoSectionManager.WORLD_CLOCK_SECTION:
                changeLayout(false, false, false, true, true);
                break;
        }
    }


    private void changeLayout(boolean alarm, boolean stopwatch, boolean timer, boolean world, boolean setSelected) {
        this.moStopWatchManager.root.setVisibility(stopwatch ? View.VISIBLE : View.INVISIBLE);
        this.moTimerSectionManager.timer_liner_layout.setVisibility(timer ? View.VISIBLE : View.INVISIBLE);
        this.moAlarmSectionManager.root.setVisibility(alarm ? View.VISIBLE : View.INVISIBLE);
        this.moWorldClockSectionManager.root.setVisibility(world ? View.VISIBLE : View.INVISIBLE);
        if (!setSelected) {
            return;
        }
        if (alarm) {
            this.bottomNavigation.setSelectedItemId(R.id.Alarm_Section);
        } else if (timer) {
            this.bottomNavigation.setSelectedItemId(R.id.Timer_Section);
        } else if (stopwatch) {
            this.bottomNavigation.setSelectedItemId(R.id.StopWatch_Section);
        } else if (world) {
            this.bottomNavigation.setSelectedItemId(R.id.WorldClock_Section);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeIsInApp(true);
        this.moAlarmSectionManager.onResume();

        // if we are in select mode and they come through the notification, we must still show the select
        // mode sections instead of jumping to other states
        boolean alarmIsSelecting = moAlarmSectionManager.isSelecting();
        boolean worldClockIsSelecting = moWorldClockSectionManager.isSelecting();
        boolean timerPresetIsSelecting = moTimerSectionManager.isSelecting();

        if (alarmIsSelecting) {
            MoSectionManager.getInstance().setSection(MoSectionManager.ALARM_SECTION);
        } else if (worldClockIsSelecting) {
            MoSectionManager.getInstance().setSection(MoSectionManager.WORLD_CLOCK_SECTION);
        } else if (timerPresetIsSelecting) {
            MoSectionManager.getInstance().setSection(MoSectionManager.TIMER_SECTION);
        }
        switchSection();
    }


    private void changeIsInApp(boolean b) {
        isInApp = b;
    }


    @Override
    public void onBackPressed() {
        if (moAlarmSectionManager.onBackPressed()) {
           // we have consumed the back press there, so we can ignore it here
        } else if (MoTimerPreset.isInDeleteMode) {
            moTimerSectionManager.cancelDeleteAlarmMode();
        } else if (moWorldClockSectionManager.onBackPressed()) {
            // we have consumed the back press there, so we can ignore it here
        } else {
            super.onBackPressed();
            finishAffinity();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MoAlarmClockManager.getInstance().onDestroy();
    }

    /**
     * whenever the window is not infocus we should make a service for the timer
     * if the timer is running
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        moTimerSectionManager.onWindowFocusChanged();
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            MoTimer.universalTimer.startService(this);
            MoStopWatch.universal.startNotificationService(this);
            this.smartShake.stop();
            changeIsInApp(false);
        } else {
            moAlarmSectionManager.updateSubTitle();
            moTimerSectionManager.closeTimerService();
            MoStopWatch.universal.cancelNotificationService(this);
            moStopWatchManager.update();
            this.smartShake.start(this);
            changeIsInApp(true);
        }
    }


    private void initAlarmSection() {
        moAlarmSectionManager = new MoAlarmSectionManager(this);
        moAlarmSectionManager.initAlarmSection();
    }

    public BottomNavigationView getBottomNavigation() {
        return bottomNavigation;
    }

    public View getBottomDeleteBar() {
        return bottomDeleteBar;
    }


    public interface SelectModeInterface {
        boolean isSelecting();
    }
}
