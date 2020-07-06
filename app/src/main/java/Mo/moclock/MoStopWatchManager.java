package Mo.moclock;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;


import Mo.moclock.MoAnimation.MoAnimation;
import Mo.moclock.MoClock.MoStopWatch.MoLap;
import Mo.moclock.MoClock.MoStopWatch.MoLap_ListAdapter;
import Mo.moclock.MoClock.MoStopWatch.MoStopWatch;
import Mo.moclock.MoListView.MoListView;

public class MoStopWatchManager {
    /**
     * stop watch
     */

    ConstraintLayout stopWatch_linear_layout;

    public ConstraintLayout getStopWatch_linear_layout() {
        return stopWatch_linear_layout;
    }

    public void setStopWatch_linear_layout(ConstraintLayout stopWatch_linear_layout) {
        this.stopWatch_linear_layout = stopWatch_linear_layout;
    }

    private TextView hour;
    private TextView minute;
    private TextView second;
    private Button start;// stop and resume are the same
    private Button stop;// lap and reset are the same
    private Button lap;
    private MoListView<MoLap> moLapMoListView;

    public MoListView<MoLap> getMoLapMoListView() {
        return moLapMoListView;
    }

    ListView lapListView;

    Activity activity;

    public MoStopWatchManager(Activity a) {
        this.activity = a;
    }

    void initStopWatchSection() {
        this.hour = activity.findViewById(R.id.hour_stopwatch_tv);
        this.minute = activity.findViewById(R.id.minute_stopwatch_tv);
        this.second = activity.findViewById(R.id.seconds_stopwatch_tv);

        this.start = activity.findViewById(R.id.start_stop_watch_button);
        this.stop = activity.findViewById(R.id.stop_time_stop_watch_button);
        this.lap = activity.findViewById(R.id.lap_time_stop_watch_button);

        this.lapListView = activity.findViewById(R.id.lap_list_view);

        // adding header to listview
        View header = activity.getLayoutInflater().inflate(R.layout.header_stopwatch, null);
        lapListView.addHeaderView(header);

        lapListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        MoStopWatch.universal.changeButtonText();

        this.start.setOnClickListener(view -> {
            MoStopWatch.universal.start();
            this.stop.setText(MoStopWatch.universal.getStopString());
            this.lap.setText(MoStopWatch.universal.getLapString());
            this.stop.setBackgroundColor(MoStopWatch.universal.getStopColor());
        });
        this.stop.setBackgroundColor(activity.getColor(R.color.error_color));
        this.stop.setOnClickListener(view -> {
            MoStopWatch.universal.stop();
            this.stop.setBackgroundColor(MoStopWatch.universal.getStopColor());
            this.stop.setText(MoStopWatch.universal.getStopString());
            this.lap.setText(MoStopWatch.universal.getLapString());
        });
        this.lap.setOnClickListener(view -> {
            MoStopWatch.universal.lap();
            this.lap.setText(MoStopWatch.universal.getLapString());
            this.moLapMoListView.updateHideIfEmpty(true);
            this.stop.setBackgroundColor(MoStopWatch.universal.getStopColor());
        });
        MoStopWatch.universal.setActivity(activity);
        MoStopWatch.universal.setStopWatchTv(this.hour, this.minute, this.second);
        MoStopWatch.universal.setButtons(this.start, this.stop, this.lap);

        this.moLapMoListView = new MoListView<MoLap>(lapListView,
                new MoLap_ListAdapter(activity, 0, MoStopWatch.universal.getLaps()),
                MoStopWatch.universal.getLaps(), activity);
        moLapMoListView.setDynamicEmptyView(new View[]{},activity.findViewById(R.id.stop_watch_list_card_view));
    }


    public void update(){
        this.stop.setBackgroundColor(MoStopWatch.universal.getStopColor());
    }
}