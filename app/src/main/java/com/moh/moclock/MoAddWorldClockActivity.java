package com.moh.moclock;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.moofficial.moessentials.MoEssentials.MoLog.MoLog;
import com.moofficial.moessentials.MoEssentials.MoUI.MoActivity.MoSmartActivity;
import com.moofficial.moessentials.MoEssentials.MoUI.MoInteractable.MoSearchable.MoSearchable;
import com.moofficial.moessentials.MoEssentials.MoUI.MoRecyclerView.MoRecyclerUtils;
import com.moofficial.moessentials.MoEssentials.MoUI.MoRecyclerView.MoRecyclerView;
import com.moofficial.moessentials.MoEssentials.MoUI.MoView.MoViewBuilder.MoMarginBuilder;
import com.moofficial.moessentials.MoEssentials.MoUI.MoView.MoViews.MoBars.MoSearchBar;
import com.moofficial.moessentials.MoEssentials.MoUI.MoView.MoViews.MoNormal.MoCardRecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.moh.moclock.MoClock.MoWorldClock.MoCities.MoCityCoordinate;
import com.moh.moclock.MoClock.MoWorldClock.MoCities.MoCityRecyclerAdapter;
import com.moh.moclock.MoClock.MoWorldClock.MoWorldClock;
import com.moh.moclock.MoClock.MoWorldClock.MoWorldClockManager;

import com.moh.moclock.R;


public class MoAddWorldClockActivity extends MoSmartActivity implements MoCityRecyclerAdapter.MoOnCitySelectedListener, MoWorldClockManager.MoOnWorldClockAvailableObserver {


    private static final String FAILED = "Sorry, something went wrong at this moment, please try again later.";

    private MoCardRecyclerView cardRecyclerView;
    private MoRecyclerView recyclerView;
    private MoCityRecyclerAdapter adapter;
    private MoSearchBar searchBar;
    private MoSearchable searchable;
    private ProgressBar progressBar;

    @Override
    protected void init() {
        setTitle(R.string.worldClockAdd_title);
        initRecyclerView();
        setupToolbars();
        setupSearchable();

        // initially put the user inside search mode when this activity launches
        searchable.activateSearch();

        initProgressbar();
        toggleProgressbar();
    }

    private void initProgressbar() {
        progressBar = new ProgressBar(this);
        l.linearNested.addView(progressBar, MoMarginBuilder.getLinearParams(this, 0,
                (int) (getResources().getDisplayMetrics().density * 80), 0, 0));
    }

    private void toggleProgressbar() {
        runOnUiThread(() -> {
            if (MoWorldClockManager.cities.isEmpty()) {
                this.progressBar.setVisibility(View.VISIBLE);
            } else {
                this.progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onWorldClockAvailable() {
        adapter.update(this, MoWorldClockManager.cities);
        toggleProgressbar();
    }

    @Override
    public void onCitySelected(MoCityCoordinate cityCoordinate) {
        if (MoWorldClockManager.has(cityCoordinate.getName())) {
            Snackbar.make(getRootView(), getString(R.string.worldClockAdd_alreadyHaveIt, cityCoordinate.getName()), Snackbar.LENGTH_SHORT).show();
        } else {
            MoLog.print(cityCoordinate.getName());
            MoWorldClockManager.add(this, MoWorldClock.from(cityCoordinate));
            Toast.makeText(this, getString(R.string.worldClockAdd_success_message, cityCoordinate.getName()), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupSearchable() {
        searchable = new MoSearchable(this, getGroupRootView(), () -> MoWorldClockManager.cities);
        //noinspection unchecked
        searchable.setSearchOnTextChanged(true)
                .setAppBarLayout(this.l.appBarLayout)
                .setActivity(this)
                .setSearchTextView(this.searchBar.getEditText())
                .setClearSearch(this.searchBar.getRightButton())
                .setShowNothingWhenSearchEmpty(true)
                .setOnSearchFinished(list -> adapter.update(this, (List<MoCityCoordinate>) list, getGroupRootView()))
                .addUnNormalViews(this.searchBar);
    }

    private void setupToolbars() {
        searchBar = new MoSearchBar(this);
        searchBar.setSearchHint(R.string.search_city_name);
        searchBar.getLeftButton().setOnClickListener((v) -> onBackPressed());

        l.setupMultipleToolbars(searchBar, searchBar);
    }

    private void initRecyclerView() {
        cardRecyclerView = new MoCardRecyclerView(this);
        adapter = new MoCityRecyclerAdapter(this, new ArrayList<>(), this);
        recyclerView = MoRecyclerUtils.get(cardRecyclerView.getRecyclerView(), this.adapter).setMaxHeight(getHeightPixels());
        recyclerView.show();
        l.linearNested.addView(this.cardRecyclerView);
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, MoAddWorldClockActivity.class));
    }

    public static void startActivityForResult(Activity a, int code) {
        a.startActivityForResult(new Intent(a, MoAddWorldClockActivity.class), code);
    }
}
