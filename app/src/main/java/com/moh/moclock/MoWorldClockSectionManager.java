package com.moh.moclock;

import android.os.Handler;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.moofficial.moessentials.MoEssentials.MoDate.MoDate;
import com.moofficial.moessentials.MoEssentials.MoUI.MoFragment.MoOnBackPressed;
import com.moofficial.moessentials.MoEssentials.MoUI.MoInteractable.MoSelectable.MoSelectable;
import com.moofficial.moessentials.MoEssentials.MoUI.MoRecyclerView.MoRecyclerUtils;
import com.moofficial.moessentials.MoEssentials.MoUI.MoRecyclerView.MoRecyclerView;
import com.moofficial.moessentials.MoEssentials.MoUI.MoView.MoViews.MoBars.MoToolBar;
import com.moofficial.moessentials.MoEssentials.MoUI.MoView.MoViews.MoNormal.MoCardRecyclerView;

import java.util.Calendar;

import com.moh.moclock.MoClock.MoWorldClock.MoWorldClock;
import com.moh.moclock.MoClock.MoWorldClock.MoWorldClockManager;
import com.moh.moclock.MoClock.MoWorldClock.MoWorldClockRecyclerAdapter;

import com.moh.moclock.R;

public class MoWorldClockSectionManager implements MoOnBackPressed, MainActivity.SelectModeInterface {

    public static String UPDATE_TIME_PAYLOAD = "update_time";

    View root;
    private MainActivity activity;
    private TextView title;
    private TextView subTitle;
    private MoToolBar toolBar;
    private MoCardRecyclerView cardRecyclerView;
    private MoRecyclerView recyclerView;
    private MoWorldClockRecyclerAdapter adapter;
    private MoSelectable<MoWorldClock> selectable;
    private MoDate date = new MoDate();
    private Handler handler = new Handler();
    private View emptyView;

    public MoWorldClockSectionManager(MainActivity a) {
        this.activity = a;
    }

    private void updateTime() {
        activity.runOnUiThread(() -> {
            if (!this.selectable.isInActionMode()) {
                updateTimeTitle();
            } else {
                this.date = new MoDate();
            }
            adapter.notifyItemRangeChanged(0, adapter.getItemCount(), UPDATE_TIME_PAYLOAD);
        });
        handler.postDelayed(this::updateTime, getDelayMillis());
    }

    private void updateTimeTitle() {
        this.date = new MoDate();
        title.setText(date.getReadableTime());
    }

    private long getDelayMillis() {
        return (60 - date.getCalendar().get(Calendar.SECOND)) * 1000;
    }

    void initWorldClockSection() {
        MoWorldClockManager.loadCities(this.activity);
        MoWorldClockManager.load(this.activity);
        initEmptyView();
        initTitleSubtitle();
        initToolbar();
        initRecyclerView();
        initSelectable();
        updateTime();
    }

    private void initEmptyView() {
        this.emptyView = activity.findViewById(R.id.layout_worldClock_emptyView);
        this.emptyView.findViewById(R.id.button_emptyWorldClock_addCity)
                .setOnClickListener((v) -> MoAddWorldClockActivity.startActivityForResult(activity, MainActivity.ADD_WORLD_CLOCK_CODE));
    }

    private void initTitleSubtitle() {
        this.title = root.findViewById(R.id.mo_lib_title);
        this.subTitle = root.findViewById(R.id.mo_lib_subtitle);
        this.subTitle.setText(R.string.worldClock_localTime);
    }

    private void initSelectable() {
        this.selectable = new MoSelectable<>(this.activity, (ViewGroup)this.root, adapter);
        this.selectable.setCounterView(this.title)
                .setSelectAllCheckBox(this.toolBar.getCheckBox())
                .addNormalViews(toolBar.getRightButton(), activity.getBottomNavigation(), subTitle)
                .addUnNormalViews(toolBar.getCheckBox(), toolBar.getMiddleButton())
                .setOnEmptySelectionListener(() -> selectable.removeAction())
                .setOnCanceledListener(this::updateTimeTitle)
                .setTransitionIn(new TransitionSet().addTransition(new ChangeBounds()).addTransition(new Fade()))
                .setTransitionOut(new TransitionSet().addTransition(new ChangeBounds()).addTransition(new Fade()));
    }

    private void initRecyclerView() {
        this.cardRecyclerView = activity.findViewById(R.id.cardRecycler_worldClock_list);
        this.cardRecyclerView.getCardView().makeTransparent();
        this.adapter = new MoWorldClockRecyclerAdapter(activity, MoWorldClockManager.worldClocks);
        this.recyclerView = MoRecyclerUtils.get(cardRecyclerView.getRecyclerView(), adapter)
                .setLayoutManagerType(MoRecyclerView.STAGGERED_GRID_LAYOUT_MANAGER)
                .setDynamicallyCalculateSpanCount(true)
                .show();
        this.recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        this.adapter.setEmptyView(this.emptyView)
                .setRecyclerView(this.recyclerView)
                .setEmptyViewCallback((showEmpty) -> {
                    if (showEmpty) {
                        toolBar.hideRight();
                    } else {
                        toolBar.showRight();
                    }
                })
                .notifyEmptyState();
    }

    private void initToolbar() {
        this.toolBar = activity.findViewById(R.id.toolbar_worldClock_main);
        this.toolBar.hideMiddle()
                .hideTitle()
                .hideLeft()
                .setMiddleIcon(R.drawable.ic_baseline_delete_outline_24)
                .setMiddleOnClickListener((v) -> onDeleteClicked())
                .setRightIcon(R.drawable.ic_add_black_24)
                .setRightOnClickListener((v) -> MoAddWorldClockActivity.startActivityForResult(activity, MainActivity.ADD_WORLD_CLOCK_CODE));
    }

    private void onDeleteClicked() {
        MoWorldClockManager.removeSelected(activity, adapter.getSelectedItems());
        selectable.removeAction();
        adapter.notifyEmptyState().notifyDataSetChanged();
    }

    public void onWorldClockChanged() {
        this.adapter.notifyEmptyState().notifyDataSetChanged();
    }

    @Override
    public boolean onBackPressed() {
        if (selectable.isInActionMode()) {
            selectable.removeAction();
            return true;
        }
        return false;
    }

    @Override
    public boolean isSelecting() {
        return adapter.isSelecting();
    }
}