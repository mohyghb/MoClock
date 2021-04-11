package Mo.moclock;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moofficial.moessentials.MoEssentials.MoUI.MoFragment.MoOnBackPressed;

import java.util.Calendar;

import Mo.moclock.MoAnimation.MoAnimation;
import Mo.moclock.MoClock.MoWorldClock.MoWorldClock;
import Mo.moclock.MoClock.MoWorldClock.MoWorldClockManager;
import Mo.moclock.MoDate.MoDate;
import Mo.moclock.MoListView.MoListView;
import Mo.moclock.MoRunnable.MoRunnable;

public class MoWorldClockSectionManager implements MoOnBackPressed {
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
//        this.handlerTask = new Runnable() {
//            @Override
//            public void run() {
//                if(!MoWorldClock.isInDeleteMode){
//                    // only update the current time if they are not deleting
//                    updateCurrentTime();
//                }
//                moListView.update(!MoWorldClock.isInDeleteMode);
//                updateTimeHandler.postDelayed(this, 1000 * (59 - Calendar.getInstance().get(Calendar.SECOND)));
//            }
//        };
    }

    void initWorldClockSection() {
        MoWorldClockManager.init(this.activity);
        this.title = activity.findViewById(R.id.World_Clock_Title);
        this.subTitle = activity.findViewById(R.id.World_Clock_Sub_Title);
        this.floatingActionButton = activity.findViewById(R.id.world_clock_empty_list_view);
        this.floatingActionButton.setOnClickListener((v) -> MoAddWorldClockActivity.startActivity(this.activity));
        this.addButton = activity.findViewById(R.id.add_world_clock_button);
        activity.findViewById(R.id.settings_world_clock).setVisibility(View.GONE);
        this.addButton.setOnClickListener((v) -> MoAddWorldClockActivity.startActivity(this.activity));
        this.listView = activity.findViewById(R.id.world_clocks_list_view);
        this.bottomNavigation = activity.findViewById(R.id.bottom_navigation);
        this.delete = activity.findViewById(R.id.delete_world_button);
        this.cancelDeleteButton = activity.findViewById(R.id.cancel_delete_world);
        this.linearDeleteMode = activity.findViewById(R.id.delete_mode_world);
        this.selectAll = activity.findViewById(R.id.select_all_world_clocks);
        this.selectAll.setVisibility(View.INVISIBLE);

    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}