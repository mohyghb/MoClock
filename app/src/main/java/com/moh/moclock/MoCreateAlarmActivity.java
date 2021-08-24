package com.moh.moclock;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.moh.moclock.MoClock.MoAlarmClock;
import com.moh.moclock.MoClock.MoAlarmClockManager;
import com.moh.moclock.MoClock.MoRepeating;
import com.moh.moclock.MoClock.MoSnooze.MoSnooze;
import com.moh.moclock.MoDate.MoDate;
import com.moh.moclock.MoVibration.MoVibration;
import com.moh.moclock.MoVibration.MoVibrationTypes;

import com.moh.moclock.R;

public class MoCreateAlarmActivity extends AppCompatActivity {


    // this is for when the user is trying to edit the clock
    // this is what changed
    public static MoAlarmClock clock;


    TimePicker timePicker;
    ImageButton datePicker;
    TextView datePickerTv;
    ChipGroup weekdaysChip;
    Chip sunday;
    Chip monday;
    Chip tuesday;
    Chip wednesday;
    Chip thursday;
    Chip friday;
    Chip saturday;
    List<Chip> chips;
    TextInputEditText alarmName;
    SwitchMaterial snooze;
    SwitchMaterial vibration;
    SwitchMaterial music;
    Calendar myCalendar;
    Button save;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mo_create_alarm);

        init();
        setListeners();
    }


    private void init() {
        this.datePicker = findViewById(R.id.date_picker_image_button);
        this.datePickerTv = findViewById(R.id.date_text_view);
        this.weekdaysChip = findViewById(R.id.week_days_chip);
        this.alarmName = findViewById(R.id.name_alarm_text_field);
        this.snooze = findViewById(R.id.snooze_switch);
        this.vibration = findViewById(R.id.vibration_switch);
        this.music = findViewById(R.id.music_switch);
        this.save = findViewById(R.id.save_button_alarm);
        this.cancel = findViewById(R.id.cancel_alarm_button);
        this.timePicker = findViewById(R.id.time_picker_alarm);
        this.timePicker.setIs24HourView(true);
        this.myCalendar = Calendar.getInstance();
        this.myCalendar.set(Calendar.SECOND, 0);
        initPrefMode();
        initChips();
        initEditMode();
    }

    // loads the preferences of the user inside
    private void initPrefMode() {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this);
        this.snooze.setChecked(s.getBoolean(getString(R.string.snooze_general), true));
        this.vibration.setChecked(s.getBoolean(getString(R.string.vibration_general), true));
        this.music.setChecked(s.getBoolean(getString(R.string.music_general), true));
    }

    private void initEditMode() {
        if (isEditMode()) {
            // time
            this.timePicker.setMinute(clock.getDateTime().get(Calendar.MINUTE));
            this.timePicker.setHour(clock.getDateTime().get(Calendar.HOUR_OF_DAY));

            // title
            this.alarmName.setText(clock.getTitle());
            this.vibration.setChecked(clock.getVibration().isActive());
            this.snooze.setChecked(clock.getSnooze().isActive());

            this.music.setChecked(clock.hasMusic());

            this.activatePositionChips(clock.getRepeating().getRepeating());

            this.myCalendar = clock.getDateTime();

            // if activated chips are not empty
            // then show the days
            if (this.weekdaysChip.getCheckedChipIds().size() > 0) {
                //List<String> texts = getTextChips();
                updateDateTextView(MoRepeating.readableFormat(this.getPositionChips()));
            } else {
                updateDateTextView(MoDate.getReadableDate(this.myCalendar));
            }
            // then this alarm is already created just change things
            // change the interface
        }
    }

    private boolean isEditMode() {
        return clock != null;
    }


    private void initChips() {
        this.sunday = findViewById(R.id.Sunday_Chip);
        this.monday = findViewById(R.id.Monday_Chip);
        this.tuesday = findViewById(R.id.Tuesday_Chip);
        this.wednesday = findViewById(R.id.Wednesday_Chip);
        this.thursday = findViewById(R.id.Thursday_Chip);
        this.friday = findViewById(R.id.Friday_Chip);
        this.saturday = findViewById(R.id.Saturday_Chip);

        this.chips = new ArrayList<>();
        this.chips.add(sunday);
        this.chips.add(monday);
        this.chips.add(tuesday);
        this.chips.add(wednesday);
        this.chips.add(thursday);
        this.chips.add(friday);
        this.chips.add(saturday);

        CompoundButton.OnCheckedChangeListener listener = (compoundButton, b) -> {
            if (this.weekdaysChip.getCheckedChipIds().size() > 0) {
                //List<String> texts = getTextChips();
                updateDateTextView(MoRepeating.readableFormat(this.getPositionChips()));
            } else {
                updateDateTextView(MoDate.getReadableDate(this.myCalendar));
            }


        };

        for (Chip c : this.chips) {
            c.setOnCheckedChangeListener(listener);
        }
    }

    private List<String> getTextChips() {
        List<String> texts = new ArrayList<>();
        if (this.weekdaysChip.getCheckedChipIds().size() == 7) {
            // every day is checked
            texts.add("Everyday");
            return texts;
        }
        for (Integer id : this.weekdaysChip.getCheckedChipIds()) {
            for (Chip c : this.chips) {
                if (c.getId() == id) {
                    texts.add(c.getText().charAt(0) + "");
                    break;
                }
            }
        }
        return texts;
    }

    private List<Integer> getPositionChips() {
        List<Integer> positions = new ArrayList<>();
        for (Integer id : this.weekdaysChip.getCheckedChipIds()) {
            int index = 0;
            for (Chip c : this.chips) {
                if (c.getId() == id) {
                    positions.add(index);
                    break;
                }
                index++;
            }
        }
        return positions;
    }

    private void activatePositionChips(List<Integer> index) {
        for (Integer i : index) {
            activateChip(i);
        }
    }

    private void activateChip(int index) {
        switch (index) {
            case Calendar.SUNDAY:
                this.sunday.setChecked(true);
                break;
            case Calendar.MONDAY:
                this.monday.setChecked(true);
                break;
            case Calendar.TUESDAY:
                this.tuesday.setChecked(true);
                break;
            case Calendar.WEDNESDAY:
                this.wednesday.setChecked(true);
                break;
            case Calendar.THURSDAY:
                this.thursday.setChecked(true);
                break;
            case Calendar.FRIDAY:
                this.friday.setChecked(true);
                break;
            case Calendar.SATURDAY:
                this.saturday.setChecked(true);
                break;
        }
    }

    private void updateDateTextView(String readableDate) {
        this.datePickerTv.setText(readableDate);
    }


    private void setUpTime() {
        this.myCalendar.set(Calendar.MINUTE, this.timePicker.getMinute());
        this.myCalendar.set(Calendar.HOUR_OF_DAY, this.timePicker.getHour());
        this.myCalendar.set(Calendar.SECOND, 0);
        while (this.myCalendar.before(Calendar.getInstance()) && this.getPositionChips().isEmpty()) {
            this.myCalendar.add(Calendar.DATE, 1);
        }
    }

    private void setListeners() {
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            this.updateDateTextView(MoDate.getReadableDate(this.myCalendar));
            this.weekdaysChip.clearCheck();
        };
        DatePickerDialog dialog = new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        // making sure that the user can not choose a timer before the current time
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        this.datePicker.setOnClickListener((v) -> dialog.show());


        this.save.setOnClickListener((v) -> {
            // save alarm
            save();
        });


        this.cancel.setOnClickListener((v) -> finish());


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // removing the reference of editing
        if (clock != null) {
            clock = null;
        }
    }

    private void save() {
        this.setUpTime();
        MoAlarmClock c;
        if (isEditMode()) {
            c = clock;
            // this alarm already has an id
        } else {
            c = new MoAlarmClock();
            // setting an id for this which is unique
            c.setId(MoAlarmClockManager.getInstance().getNextId());
        }
        c.setTitle(this.alarmName.getText().toString());
        c.setDateTime(this.myCalendar);
        c.setActive(true);
        c.setSnooze(new MoSnooze(this, this.snooze.isChecked()));
        c.setVibration(new MoVibration(MoVibrationTypes.BASIC, this.vibration.isChecked()));
        c.setPathToMusic(this.music.isChecked());
        c.setRepeating(new MoRepeating(this.getPositionChips()));
        if (isEditMode()) {
            c.setActive(true);
            // just update/save the changes also activate since this might be earlier than others
            MoAlarmClockManager.getInstance().saveActivate(this);
            Toast.makeText(this, "Alarm changed to " +
                    c.getDate().getReadableDifference(Calendar.getInstance()), Toast.LENGTH_LONG).show();
        } else {
            // add alarm if not editing
            MoAlarmClockManager.getInstance().addAlarm(c, this);
        }

        finish();
    }

    public static void startActivityForResult(Activity a,int code) {
        a.startActivityForResult(new Intent(a, MoCreateAlarmActivity.class), code);
    }


}
