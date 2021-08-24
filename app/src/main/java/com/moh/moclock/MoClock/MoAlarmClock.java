package com.moh.moclock.MoClock;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoFile;
import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoLoadable;
import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoSavable;
import com.moofficial.moessentials.MoEssentials.MoUI.MoInteractable.MoSelectable.MoSelectableInterface.MoSelectableItem;

import java.util.Calendar;
import java.util.Objects;

import com.moh.moclock.MoAnimation.MoAnimation;
import com.moh.moclock.MoClock.MoSnooze.MoSnooze;
import com.moh.moclock.MoColor.MoColor;
import com.moh.moclock.MoCreateAlarmActivity;
import com.moh.moclock.MoDate.MoDate;
import com.moh.moclock.MoId.MoId;
import com.moh.moclock.MoInflatorView.MoInflaterView;
import com.moh.moclock.MoInflatorView.MoViewDisplayable;
import com.moh.moclock.MoUI.MoTextInput;
import com.moh.moclock.MoVibration.MoVibration;
import com.moh.moclock.MoVibration.MoVibrationTypes;
import com.moh.moclock.R;

public class MoAlarmClock implements MoSavable, MoViewDisplayable, MoLoadable, MoSelectableItem {

    private MoId id;
    private String title;
    private MoVibration vibration;
    private MoSnooze snooze;
    private MoRepeating repeating;
    private MoDate dateTime;
    private boolean isActive;
    private boolean pathToMusic;


    // UI
    private TextView titleTextView;
    private TextView time;
    private TextView date;
    private TextView repeatingDays;
    private CheckBox checkBox;
    private SwitchMaterial active;
    private CardView cardView;
    private View clockLayoutView;


    private MoAlarmListView arrayAdapter;
    private MoAlarmClockListener listener;

    private boolean isSelected = false;
    public static boolean isInDeleteMode = false;

    public MoAlarmClock(){
        this.title = "";
        this.vibration = new MoVibration(MoVibrationTypes.BASIC,false);
        this.snooze = new MoSnooze();
        this.repeating = new MoRepeating();
        this.dateTime = new MoDate();
        this.id = new MoId();
        this.isActive = true;
        this.pathToMusic = true;
    }

    public MoAlarmClock setListener(MoAlarmClockListener listener) {
        this.listener = listener;
        return this;
    }

    public int getId() {
        return id.getId();
    }

    public void setId(MoId id) {
        this.id = id;
    }

    public void setDateTime(MoDate dateTime) {
        this.dateTime = dateTime;
    }

    public boolean hasMusic() {
        return pathToMusic;
    }

    public void setPathToMusic(boolean pathToMusic) {
        this.pathToMusic = pathToMusic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MoVibration getVibration() {
        return vibration;
    }

    public void setVibration(MoVibration vibration) {
        this.vibration = vibration;
    }

    public MoSnooze getSnooze() {
        return snooze;
    }

    public void setSnooze(MoSnooze snooze) {
        this.snooze = snooze;
    }

    public MoRepeating getRepeating() {
        return repeating;
    }

    public void setRepeating(MoRepeating repeating) {
        this.repeating = repeating;
        if(!this.repeating.isEmpty()){
            // if repeating is not empty we need to change the date
            this.dateTime.setCalendar(MoDate.getNextOccuring(this.repeating.getRepeating(),this.dateTime.getCalendar()));
            if (this.listener != null) {
                this.listener.onRepeatingChanged();
            }
        }
    }

    public Calendar getDateTime() {
        return dateTime.getCalendar();
    }

    public void setDateTime(Calendar dateTime) {
        this.dateTime.setCalendar(dateTime);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
        if (listener != null) {
            listener.onActiveChanged(isActive);
        }
    }

    public void setActiveWithoutInvokingListener(boolean active) {
        isActive = active;
    }


    public void setInDeleteMode(boolean inDeleteMode) {
        isInDeleteMode = inDeleteMode;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public MoDate getDate(){
        return this.dateTime;
    }

    public void snooze(Context context){
        // amount can be changed later for dynamic things
        this.isActive = true;
        //this is to make sure if the alarm has repeating, the repeating wont take over
        this.dateTime = new MoDate();
        this.snooze.applySnooze(this.dateTime,context);

    }

    public MoId getMoId(){
        return this.id;
    }

    public void addDateField(int field, int amount){
        this.dateTime.getCalendar().add(field, amount);
    }

    public void setDateField(int field,int value){
        this.dateTime.getCalendar().set(field,value);
    }


    public String getReadableDifference(){
        return this.dateTime.getReadableDifference(Calendar.getInstance());
    }

    /**
     * @return the data that is going to be saved by the save method
     * inside the class which implements MoSavable
     */
    @Override
    public String getData() {
        return MoFile.getData(id.getData(),title,vibration,
                snooze.getData(),repeating,dateTime.getData(),isActive,pathToMusic);
    }

    /**
     * takes args and makes the class which implements it
     * displayable for the user
     * for example this can be used in the U.I
     *
     * @param args arguments that are passed to this method via other classes
     */
    @Override
    public View display(Object... args) {
        // we assume that the first element that is passed is the context
        Context c = (Context) args[0];
        // and assume that the second element is array adapter
        this.arrayAdapter = (MoAlarmListView) args[1];
        Boolean doDeleteAnimation = (Boolean) args[2];

        clockLayoutView = MoInflaterView.inflate(R.layout.card_view_alarm,c);
        assert clockLayoutView != null;


        initTexts();
        initCheckBox();
        initSwitches(c);
        initCardView(c);
        updateUIState(c,doDeleteAnimation);


        return clockLayoutView;
        // turn on or off

    }

    private void initCardView(Context c) {
        cardView = clockLayoutView.findViewById(R.id.alarm_card_view);
        cardView.setOnClickListener(view -> {
            if(!isInDeleteMode){
                // go to edit mode
                MoCreateAlarmActivity.clock = MoAlarmClock.this;
                Intent intent = new Intent(c, MoCreateAlarmActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(intent);
            }else{
                // change selected
                onSelect(checkBox);
            }
        });
        cardView.setOnLongClickListener(view -> {
            if(!isInDeleteMode){
                arrayAdapter.activateDeleteMode();
                onSelect(checkBox);
            }
            return true;
        });
    }

    private void initSwitches(Context c) {
        active = clockLayoutView.findViewById(R.id.is_active_switch);
        active.setChecked(this.isActive);
    }

    private void initTexts() {
        titleTextView = clockLayoutView.findViewById(R.id.alarm_title_textView);
        if(this.title.isEmpty()){
            titleTextView.setVisibility(View.GONE);
        }
        titleTextView.setText(this.title);
        time = clockLayoutView.findViewById(R.id.alarm_time_textView);
        time.setText(this.dateTime.getReadableTime());
        date = clockLayoutView.findViewById(R.id.alarm_date_TextView);
        date.setText(this.dateTime.getReadableDate());
        repeatingDays = clockLayoutView.findViewById(R.id.repeatingDaysTextView);
        if(!this.repeating.isEmpty()){
            repeatingDays.setText(this.repeating.readableFormat());
        } else {
            repeatingDays.setVisibility(View.GONE);
        }
    }

    private void initCheckBox() {

    }

    /**
     * updates the alarm clock layout based on the different events
     * @param c
     */
    private void updateUIState(Context c, boolean doDeleteAnimation){
        if(isInDeleteMode){
            MoAnimation.animate(active,View.INVISIBLE,MoAnimation.DISAPPEAR,id,doDeleteAnimation);
        }else{
            MoAnimation.animate(active,View.VISIBLE,MoAnimation.APPEAR,id);
        }

        // changing the state of this check box
        if(isInDeleteMode){
            checkBox.setChecked(this.isSelected);
            MoAnimation.animate(checkBox,View.VISIBLE,MoAnimation.APPEAR,id,doDeleteAnimation);
        }else{
            MoAnimation.animate(checkBox,View.GONE,MoAnimation.DISAPPEAR,id);
        }


        /**
         * setting the correct color for textViews, if they are enabled or not
         */
        MoTextInput.setColorCondition(isActive,MoColor.color_text_on_highlight,MoColor.color_text_disabled,time,c);
        MoTextInput.setColorCondition(isActive,MoColor.color_text_on_highlight,MoColor.color_text_disabled,date,c);
        MoTextInput.setColorCondition(isActive,MoColor.color_text_on_highlight,MoColor.color_text_disabled,repeatingDays,c);
        MoTextInput.setColorCondition(isActive,MoColor.color_text_on_normal,MoColor.color_text_disabled,titleTextView,c);
    }


    private void onSelect(CheckBox checkBox){
        isSelected = !isSelected;
        checkBox.setChecked(isSelected);
        arrayAdapter.onSelect();
    }


    public String getReadableDateTime(){
        return this.dateTime.getReadableDate()+", "+this.dateTime.getReadableTime();
    }


    public void activate(Context context){
        if(this.repeating.isEmpty()){
            // work on date
            Calendar current = Calendar.getInstance();
            if(this.dateTime.getCalendar().before(current)){
                // the time of the current clock is less than what the time is right now in real life
                this.dateTime.set(Calendar.YEAR,current.get(Calendar.YEAR));
                this.dateTime.set(Calendar.MONTH,current.get(Calendar.MONTH));
                this.dateTime.set(Calendar.DATE,current.get(Calendar.DATE));
                // if works here as well but just in case
                while(this.dateTime.getCalendar().before(current)){
                    this.dateTime.getCalendar().add(Calendar.DATE,1);
                }
            }
            // check whether the date is also before
        }else{
            this.dateTime.setCalendar(MoDate.getNextOccuring(this.repeating.getRepeating(),this.dateTime.getCalendar()));
            // work on which day is the next
            // and set the date based on that
        }
        MoAlarmClockManager.getInstance().saveActivate(context);
    }


    public void cancel(){
        if(this.repeating.isEmpty()){
            this.setActive(false);
        } else {
            this.setRepeating(this.repeating);
        }
    }

    /**
     * loads a savable object into its class
     *
     * @param context
     */
    @Override
    public void load(String data, Context context) {
        String[] components = MoFile.loadable(data);
        this.id.load(components[0],context);
        this.title = components[1];
        this.vibration.load(components[2],context);
        this.snooze.load(components[3],context);
        this.repeating.load(components[4],context);
        this.dateTime.load(components[5],context);
        //this.dateTime
        this.isActive = Boolean.parseBoolean(components[6]);
        this.pathToMusic = Boolean.parseBoolean(components[7]);
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public boolean onSelect() {
        this.isSelected = !this.isSelected;
        return this.isSelected;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoAlarmClock that = (MoAlarmClock) o;
        return this.dateTime.equals(that.dateTime) &&
                this.repeating.getRepeating().equals(that.repeating.getRepeating());
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime,repeating.getRepeating());
    }

    public interface MoAlarmClockListener {
        void onActiveChanged(boolean isActive);
        void onRepeatingChanged();
    }
}
