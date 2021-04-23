package Mo.moclock;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.moofficial.moessentials.MoEssentials.MoLog.MoLog;
import com.moofficial.moessentials.MoEssentials.MoUI.MoActivity.MoSmartActivity;
import com.moofficial.moessentials.MoEssentials.MoUI.MoInteractable.MoSearchable.MoSearchable;
import com.moofficial.moessentials.MoEssentials.MoUI.MoRecyclerView.MoRecyclerUtils;
import com.moofficial.moessentials.MoEssentials.MoUI.MoRecyclerView.MoRecyclerView;
import com.moofficial.moessentials.MoEssentials.MoUI.MoView.MoViewBuilder.MoMarginBuilder;
import com.moofficial.moessentials.MoEssentials.MoUI.MoView.MoViews.MoBars.MoSearchBar;
import com.moofficial.moessentials.MoEssentials.MoUI.MoView.MoViews.MoBars.MoToolBar;
import com.moofficial.moessentials.MoEssentials.MoUI.MoView.MoViews.MoNormal.MoCardRecyclerView;

import java.util.ArrayList;
import java.util.List;

import Mo.moclock.MoClock.MoWorldClock.MoCities.MoCityCoordinate;
import Mo.moclock.MoClock.MoWorldClock.MoCities.MoCityRecyclerAdapter;
import Mo.moclock.MoClock.MoWorldClock.MoWorldClock;
import Mo.moclock.MoClock.MoWorldClock.MoWorldClockManager;


public class MoAddWorldClockActivity extends MoSmartActivity implements MoCityRecyclerAdapter.MoOnCitySelectedListener, MoWorldClockManager.MoOnWorldClockAvailableObserver {


    private static final String FAILED = "Sorry, something went wrong at this moment, please try again later.";

    private MoCardRecyclerView cardRecyclerView;
    private MoRecyclerView recyclerView;
    private MoCityRecyclerAdapter adapter;
    private MoToolBar mainToolbar;
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
    protected void onStart() {
        super.onStart();
        MoWorldClockManager.subscribe(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MoWorldClockManager.unsubscribe(this);
    }

    @Override
    public void onCitySelected(MoCityCoordinate cityCoordinate) {
        MoLog.print(cityCoordinate.getName());
        MoWorldClockManager.add(this, MoWorldClock.from(cityCoordinate));
        Toast.makeText(this, getString(R.string.worldClockAdd_success_message, cityCoordinate.getName()), Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setupSearchable() {
        searchable = new MoSearchable(this, getGroupRootView(), () -> MoWorldClockManager.cities);
        //noinspection unchecked
        searchable.setSearchOnTextChanged(true)
                .setAppBarLayout(this.l.appBarLayout)
                .setActivity(this)
                .setSearchButton(this.mainToolbar.getRightButton())
                .setCancelSearch(this.searchBar.getLeftButton())
                .setSearchTextView(this.searchBar.getEditText())
                .setClearSearch(this.searchBar.getRightButton())
                .setShowNothingWhenSearchEmpty(true)
                .setOnSearchFinished(list -> adapter.update(this, (List<MoCityCoordinate>) list, getGroupRootView()))
                .addNormalViews(this.mainToolbar)
                .addUnNormalViews(this.searchBar);
    }

    private void setupToolbars() {
        mainToolbar = new MoToolBar(this);
        mainToolbar.hideMiddle().setRightIcon(R.drawable.ic_baseline_search_24).setLeftOnClickListener((v) -> onBackPressed());

        searchBar = new MoSearchBar(this);
        searchBar.setSearchHint(R.string.search_city_name);

        l.setupMultipleToolbars(mainToolbar, mainToolbar, searchBar);
        syncTitle(mainToolbar.getTitle());
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
