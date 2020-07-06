package Mo.moclock;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

import Mo.moclock.MoAnimation.MoAnimation;
import Mo.moclock.MoClock.MoAlarmClock;
import Mo.moclock.MoClock.MoClockSugestions.MoClockSuggestionManager;
import Mo.moclock.MoClock.MoWorldClock.MoWorldClock;
import Mo.moclock.MoClock.MoWorldClock.MoWorldClockArrayAdapter;
import Mo.moclock.MoClock.MoWorldClock.MoWorldClockManager;
import Mo.moclock.MoDate.MoDate;
import Mo.moclock.MoListView.MoListView;
import Mo.moclock.MoRunnable.MoRunnable;

public class MoWorldClockSectionManager {
    LinearLayout world_clock_linear_layout;
    ImageButton addButton;
    ListView listView;
    MoListView<MoWorldClock> moListView;
    Button floatingActionButton;
    TextView title;
    TextView subTitle;
    Handler updateTimeHandler = new Handler();
    Runnable handlerTask;
    Activity activity;
    private BottomNavigationView bottomNavigation;

    private Button cancelDeleteButton,delete;
    private CheckBox selectAll;
    private LinearLayout linearDeleteMode;

    public MoWorldClockSectionManager(Activity a) {
        this.activity = a;
        this.handlerTask = new Runnable() {
            @Override
            public void run() {
                if(!MoWorldClock.isInDeleteMode){
                    // only update the current time if they are not deleting
                    updateCurrentTime();
                }
                moListView.update(!MoWorldClock.isInDeleteMode);
                updateTimeHandler.postDelayed(this, 1000 * (59 - Calendar.getInstance().get(Calendar.SECOND)));
            }
        };
    }

    void initWorldClockSection() {
        this.title = activity.findViewById(R.id.World_Clock_Title);
        this.subTitle = activity.findViewById(R.id.World_Clock_Sub_Title);
        this.floatingActionButton = activity.findViewById(R.id.world_clock_empty_list_view);
        this.floatingActionButton.setOnClickListener((v) -> openAddWorldClockActivity());
        this.addButton = activity.findViewById(R.id.add_world_clock_button);
        activity.findViewById(R.id.settings_world_clock).setVisibility(View.GONE);
        this.addButton.setOnClickListener((v) -> openAddWorldClockActivity());
        this.listView = activity.findViewById(R.id.world_clocks_list_view);
        this.bottomNavigation = activity.findViewById(R.id.bottom_navigation);
        this.delete = activity.findViewById(R.id.delete_world_button);
        this.cancelDeleteButton = activity.findViewById(R.id.cancel_delete_world);
        this.linearDeleteMode = activity.findViewById(R.id.delete_mode_world);
        this.selectAll = activity.findViewById(R.id.select_all_world_clocks);
        this.selectAll.setVisibility(View.INVISIBLE);
        this.selectAll.setOnCheckedChangeListener((compoundButton, b) -> {
            if(!compoundButton.isPressed()){
                return;
            }
            if(b){
                MoWorldClockManager.manager.selectAll();
                onCountChanged(MoWorldClockManager.manager.size());
            }else{
                MoWorldClockManager.manager.deselectAll();
                onCountChanged(0);
            }
            moListView.update();
        });



        MoWorldClockManager.manager.load("", activity);
        this.moListView = new MoListView<MoWorldClock>(listView,
                new MoWorldClockArrayAdapter(activity, 0, MoWorldClockManager.manager.getWorldClocks(), new MoRunnable() {
                    @Override
                    public <T> void run(T... args) {
                        onCountChanged((Integer)args[0]);
                    }
                }
                ).setActivateDeleteMode(new MoRunnable() {
                    @Override
                    public <T> void run(T... args) {
                        deleteModeUI((Boolean) args[0]);
                    }
                }),
                MoWorldClockManager.manager.getWorldClocks(), activity);
        this.moListView.setDynamicEmptyView(new View[]{floatingActionButton,
                activity.findViewById(R.id.linear_empty_world_clock_layout)}, addButton,
                activity.findViewById(R.id.world_clock_card_view));


        this.cancelDeleteButton.setOnClickListener(view -> cancelDeleteMode());
        this.delete.setOnClickListener(view -> {
            MoWorldClockManager.manager.deleteSelected(activity);
            cancelDeleteMode();
            moListView.update();
        });

        updateSubtitle();
        this.handlerTask.run();
    }

    public void cancelDeleteMode(){
        deleteModeUI(false);
        MoWorldClockManager.manager.deselectAll();
    }

    private void updateSubtitle(){
        this.subTitle.setText("Local Time");
    }

    void updateCurrentTime() {
        MoDate date = new MoDate();
        this.title.setText(date.getReadableTime());
    }

    void removeAt(int index) {
        MoWorldClockManager.manager.remove(index, activity);
        moListView.update();
    }

    void openAddWorldClockActivity() {
        Intent intent = new Intent(activity, MoAddWorldClockActivity.class);
        activity.startActivityForResult(intent, MainActivity.ADD_WORLD_CLOCK_CODE);
    }

    // delete mode
    private void deleteModeUI(boolean on) {
        MoWorldClock.isInDeleteMode = on;

        if(!on){
            MoAnimation.clearLog();
        }

        moListView.updateHideIfEmpty(true);
        showNavigation(!on);
        showTitle(!on);
        showLinearDelete(on);
        showAdd(!on);
        showSelectAll(on);
        turnDelete(MoWorldClockManager.manager.getSelectedSize() > 0);


        if(!on){
            MoAnimation.clearLog();
        }
    }


    private void showNavigation(boolean b){
        MoAnimation.animate(this.bottomNavigation,b?View.VISIBLE:View.INVISIBLE,
                b?MoAnimation.BOTTOM_TO_TOP_FADE_IN:MoAnimation.MOVE_DOWN_FADE_OUT);
    }

    private void showTitle(boolean on){
        if(on){
            updateCurrentTime();
            updateSubtitle();
        }else{
            changeSelectedNumber(MoWorldClockManager.manager.getSelectedSize());
        }
    }

    private void showLinearDelete(boolean b){
        MoAnimation.animate(this.linearDeleteMode,b?View.VISIBLE:View.INVISIBLE,
                b?MoAnimation.BOTTOM_TO_TOP_FADE_IN:MoAnimation.MOVE_DOWN_FADE_OUT);
    }

    private void showAdd(boolean b){
        MoAnimation.animate(this.addButton,b?View.VISIBLE:View.INVISIBLE,
                b?MoAnimation.APPEAR:MoAnimation.DISAPPEAR);
    }

    private void showSelectAll(boolean b){
        MoAnimation.animate(this.selectAll,b?View.VISIBLE:View.INVISIBLE,
                b?MoAnimation.APPEAR:MoAnimation.DISAPPEAR);
        if(MoWorldClockManager.manager.size() == 1 && b){
            selectAll.setChecked(true);
        }else{
            selectAll.setChecked(false);
        }
    }


    private void turnDelete(boolean on){
        this.delete.setVisibility(on?View.VISIBLE:View.GONE);
        this.cancelDeleteButton.setVisibility(on?View.GONE:View.VISIBLE);
    }



    private void onCountChanged(int selected){
        if(selected==0){
            // none of them are selected
            turnDelete(false);
        }else{
            turnDelete(true);
        }
        if(selected == MoWorldClockManager.manager.size()){
            selectAll.setChecked(true);
        }else{
            selectAll.setChecked(false);
        }
        changeSelectedNumber(selected);
    }

    private void changeSelectedNumber(int selected){
        this.title.setText(selected + " Selected");
        this.subTitle.setText("");
    }




    public void onWindowFocusChanged(){
        if(MoWorldClock.isInDeleteMode){
            deleteModeUI(true);
        }
    }

}