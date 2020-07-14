package Mo.moclock;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.preference.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Calendar;
import Mo.moclock.MoAnimation.MoAnimation;
import Mo.moclock.MoClock.MoAlarmClock;
import Mo.moclock.MoClock.MoAlarmClockManager;
import Mo.moclock.MoClock.MoAlarmListView;
import Mo.moclock.MoClock.MoClockSugestions.MoClockSuggestion;
import Mo.moclock.MoClock.MoClockSugestions.MoClockSuggestionManager;
import Mo.moclock.MoClock.MoClockSugestions.MoPrioritySuggestion;
import Mo.moclock.MoClock.MoEmptyAlarmException;
import Mo.moclock.MoListView.MoListView;
import Mo.moclock.MoRunnable.MoRunnable;
import Mo.moclock.MoSensor.MoShakeListener;

public class MoAlarmSectionManager {
    /**
     * alarm section
     */
    public static final int CREATE_ALARM_CODE = 1;
    public static final String ALL_ALARMS_OFF = "All alarms are off";
    public static final String NEXT_ALARM = "Next alarm: ";
    public static final String MOH_YAGHOUB = "By Moh Yaghoub";
    ImageButton addAlarmButton;
    ImageButton settings;
    Button delete;
    CheckBox selectAll;

    ListView alarmsListView;
    MoAlarmListView alarmClockArrayAdapter;
    MoListView<MoAlarmClock> moAlarmClockMoListView;
    LinearLayout alarm_linear_layout;
    Activity activity;
    Button floatingActionButton;
    TextView subTitle;
    private TextView title;

    Button cancelDeleteButton;
    private BottomNavigationView bottomNavigation;
    private LinearLayout linearDeleteMode;


    private Iterable<MoPrioritySuggestion> suggestions;
    private boolean showSuggestions;




    public MoAlarmSectionManager(Activity a) {
        this.activity = a;
    }

    void initAlarmSection() {
        initViews();
        this.selectAll.setOnCheckedChangeListener((compoundButton, b) -> {
            if(!compoundButton.isPressed()){
                return;
            }
            alarmClockArrayAdapter.selectDeselectAll(b);
        });
        this.cancelDeleteButton.setOnClickListener(view -> {
            cancelDeleteAlarmMode();
        });
        turnDelete(false);
        this.delete.setOnClickListener(view -> {
            MoAlarmClockManager.getInstance().removeSelectedAlarms(activity);
            deleteModeUI(false);
            moAlarmClockMoListView.update();
        });
        this.settings.setOnClickListener(this::showMenu);
        this.addAlarmButton.setOnClickListener(this::showSuggestionPopUp);
        // to turn the all check box button on or off
        MoRunnable turnAll = new MoRunnable() {
            @Override
            public <T> void run(T... args) {
                turnSelectAll((Boolean) args[0]);
            }
        };
        MoRunnable turnDelete = new MoRunnable() {
            @Override
            public <T> void run(T... args) {
                turnDelete((Boolean) args[0]);
            }
        };
        MoRunnable changeTitleRun = new MoRunnable() {
            @Override
            public <T> void run(T... args) {
                changeTitleCount((Integer) args[0]);
            }
        };
        MoAlarmClockManager.getInstance().loadIfNotLoaded(activity);
        this.alarmsListView = activity.findViewById(R.id.alarms_list_view);
        this.alarmClockArrayAdapter = new MoAlarmListView(activity.getBaseContext(),
                0, MoAlarmClockManager.getInstance().getAlarms(),this::setSubtitle,
                () -> deleteModeUI(true),turnAll,turnDelete,changeTitleRun);
        moAlarmClockMoListView = new MoListView<MoAlarmClock>(alarmsListView, alarmClockArrayAdapter,
                MoAlarmClockManager.getInstance().getAlarms(), activity.getBaseContext()
                , MoAnimation.get(MoAnimation.FADE_IN),MoAnimation.get(MoAnimation.FADE_OUT));
        moAlarmClockMoListView.update();
        moAlarmClockMoListView.setDynamicEmptyView(new View[]{this.floatingActionButton,
                        activity.findViewById(R.id.linear_empty_alarm_layout)},
                this.addAlarmButton,activity.findViewById(R.id.card_alarm_list_view));
        this.floatingActionButton.setOnClickListener(this::showSuggestionPopUp);
        initClockSuggestions();

        setSubtitle();
    }

    private void initClockSuggestions(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                suggestions = MoClockSuggestionManager.getSuggestions(activity);
            }
        }.start();
    }

    private void initViews() {
        this.title = activity.findViewById(R.id.title_alarm_app_name);
        this.subTitle = activity.findViewById(R.id.alarm_sub_title);
        this.floatingActionButton = activity.findViewById(R.id.alarm_empty_list_view);
        this.addAlarmButton = activity.findViewById(R.id.add_alarm_button);
        this.settings = activity.findViewById(R.id.settings_alarm);
        this.delete = activity.findViewById(R.id.delete_alarm_button);
        this.selectAll = activity.findViewById(R.id.select_all_alarms);
        this.linearDeleteMode = activity.findViewById(R.id.linear_delete_mode_layout);
        this.alarm_linear_layout = activity.findViewById(R.id.linear_alarm_layout);
        this.cancelDeleteButton = activity.findViewById(R.id.cancel_delete_mode);
        this.bottomNavigation = activity.findViewById(R.id.bottom_navigation);
    }

    /**
     * launches the alarm creator activity
     * to let the user make an alarm
     */
    private void launchAlarmCreate(){
        setEnabled(false);
        Intent intent = new Intent(activity, MoCreateAlarmActivity.class);
        activity.startActivityForResult(intent, CREATE_ALARM_CODE);
    }

    /**
     * enables or disables the buttons
     * so that the user can not open an activity twice
     * @param b
     */
    private void setEnabled(boolean b){
        this.floatingActionButton.setEnabled(b);
        this.addAlarmButton.setEnabled(b);
    }


    public void cancelDeleteAlarmMode() {deleteModeUI(false);}



    public void onSmartShake(MoShakeListener shakeListener){
        // smart shake
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(activity);
        boolean active = s.getBoolean(activity.getString(R.string.smart_alarm),false);
        if(active){
            shakeListener.checkToVibrate();
            MoAlarmClock clock = new MoAlarmClock();
            String list = s.getString(activity.getString(R.string.smart_alarm_list),"15");
            try{
                // it is an amount
                int amount = Integer.parseInt(list);
                clock.addDateField(Calendar.MINUTE,amount);

            }catch(Exception e){
                // it is a custom time
                String time = s.getString(activity.getString(R.string.smart_alarm_button),"12:00");
                String[] hrmin = time.split(":");
                int hour = Integer.parseInt(hrmin[0]);
                int minute = Integer.parseInt(hrmin[1]);
                clock.setDateField(Calendar.HOUR_OF_DAY,hour);
                clock.setDateField(Calendar.MINUTE,minute);
            }
            MoAlarmClockManager.getInstance().addAlarm(clock,activity);
            updateAll();
        }
    }



    public boolean showMenu(View anchor) {
        PopupMenu popup = new PopupMenu(activity, anchor);
        popup.getMenuInflater().inflate(R.menu.pop_up_settings, popup.getMenu());
        popup.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.setting_menu_item:
                    onSettingPressed();
                    break;
                case R.id.about_menu_item:
                    onAboutPressed();
                    break;
            }
            return false;
        });
        popup.show();
        return true;
    }


    /**
     * showing smart suggestion when user is trying to make a new alarm
     * this way if our intelligent system understands that they want to set an alarm for
     * a specific time, we just make it much easier for them to do
     * this option can be disabled from the settings
     * by using the shared preferences
     * @param anchor the pop up menu is anchored to this view
     */
    private void showSuggestionPopUp(View anchor) {
        // check for preferences set in the setting
        // to see if they even want a suggestion
        if(!showSuggestions || !suggestions.iterator().hasNext()){
            launchAlarmCreate();
        }else{
            PopupMenu popup = new PopupMenu(activity, anchor);
            popup.getMenu().add("New Alarm");
            popup.getMenu().getItem(0).setOnMenuItemClickListener(menuItem -> {
                launchAlarmCreate();
                return false;
            });
            int i = 1;
            for(MoPrioritySuggestion ps: suggestions){
                final MoPrioritySuggestion suggestion = ps;
                popup.getMenu().add(ps.getTime());
                popup.getMenu().getItem(i).setOnMenuItemClickListener(menuItem -> {
                    suggestion.createAlarm(activity);
                    moAlarmClockMoListView.updateHideIfEmpty();
                    //Toast.makeText(activity,menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                    return false;
                });
                i++;
            }
            popup.show();
        }
    }


    // user pressed setting menu item
    private void onSettingPressed(){
        Intent intent = new Intent(activity,MoSettingsActivity.class);
        activity.startActivity(intent);
    }



    // user pressed about menu item
    private void onAboutPressed() {

    }

    private void deleteModeUI(boolean on) {
        MoAlarmClock.isInDeleteMode = on;

        if(!on){
            MoAnimation.clearLog();
        }

        bringLinearDeleteMode(on);
        bringCancelDelete(on);
        bringDelete(on);
        bringSelectAllAlarms(on);
        changeTitle(on);

        if(!on){
            MoAnimation.clearLog();
        }
    }

    private void bringLinearDeleteMode(boolean on){
        if(on){
            MoAnimation.animate(linearDeleteMode,View.VISIBLE,MoAnimation.BOTTOM_TO_TOP);
        }else{
            MoAnimation.animate(linearDeleteMode,View.GONE,MoAnimation.MOVE_DOWN);
        }
    }

    private void bringCancelDelete(boolean on){
        if(on){
            MoAnimation.animate(bottomNavigation,View.INVISIBLE,MoAnimation.MOVE_DOWN_FADE_OUT);
        }else{
            MoAnimation.animate(bottomNavigation,View.VISIBLE,MoAnimation.BOTTOM_TO_TOP_FADE_IN);
            this.alarmClockArrayAdapter.deActivateDeleteMode();
        }
    }

    private void bringDelete(boolean on){
        if(on){
            MoAnimation.animate(settings,View.INVISIBLE,MoAnimation.DISAPPEAR);
            MoAnimation.animate(addAlarmButton,View.INVISIBLE,MoAnimation.DISAPPEAR);
        }else{
            MoAnimation.animate(settings,View.VISIBLE,MoAnimation.APPEAR);
            MoAnimation.animate(addAlarmButton,View.VISIBLE,MoAnimation.APPEAR);
        }
    }

    private void bringSelectAllAlarms(boolean on){
//        MoAnimation.animate(selectAll,on?View.VISIBLE:View.INVISIBLE,
//                on?MoAnimation.LEFT_TO_RIGHT:MoAnimation.MOVE_LEFT);
        MoAnimation.animate(selectAll,on?View.VISIBLE:View.INVISIBLE,
                on?MoAnimation.APPEAR:MoAnimation.DISAPPEAR);
        if(!on){
            // resetting the select all every time
            this.selectAll.setChecked(false);
        }
    }

    private void changeTitle(boolean on) {
        if(!on){
            this.title.setText(this.activity.getString(R.string.app_name));
            this.setSubtitle();
        }else{
            changeTitleCount(MoAlarmClockManager.getInstance().getSelectedCount());
        }

    }

    private void changeTitleCount(int num){
        //shows the number of selected to be deleted
        this.title.setText(String.format("%d selected",num));
//        this.subTitle.setText("Select alarms that you want to delete");
    }



    public void onWindowFocusChanged(){
        if(MoAlarmClock.isInDeleteMode){
            deleteModeUI(true);
            alarmClockArrayAdapter.notifyDataSetChanged();
        }
    }


    private void turnSelectAll(boolean on){
        this.selectAll.setChecked(on);
    }


    private void turnDelete(boolean on){
        this.delete.setVisibility(on?View.VISIBLE:View.GONE);
        this.cancelDeleteButton.setVisibility(on?View.GONE:View.VISIBLE);
    }



    public void onResume(){
        this.setEnabled(true);
        this.showSuggestions = PreferenceManager.getDefaultSharedPreferences(activity)
                .getBoolean(activity.getString(R.string.show_suggestions),false);
    }


    public void updateSubTitle(){
        setSubtitle();
    }

    public void updateListView(){
        moAlarmClockMoListView.update();
    }

    public void updateAll(){
        updateSubTitle();
        updateListView();
    }

    private void setSubtitle(){
        try {
            this.subTitle.setText(NEXT_ALARM + MoAlarmClockManager.getInstance().getNextAlarm().getReadableDifference());
        } catch (MoEmptyAlarmException e) {
            this.subTitle.setText(MoAlarmClockManager.getInstance().isEmpty()?"":ALL_ALARMS_OFF);
        }
    }
}