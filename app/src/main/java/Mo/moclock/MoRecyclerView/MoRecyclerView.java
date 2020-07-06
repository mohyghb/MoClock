package Mo.moclock.MoRecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MoRecyclerView {


    public static final int DEFAULT_GRID_COUNT = 2;
    public static final boolean DEFAULT_REVERSE_LAYOUT = false;



    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private Activity activity;
    private int recyclerViewResources;
    private int orientation = LinearLayoutManager.VERTICAL;
    private int gridCount = DEFAULT_GRID_COUNT;
    private boolean reverseLayout = DEFAULT_REVERSE_LAYOUT;
    private RecyclerView.LayoutManager layoutManager;
    private View.OnLayoutChangeListener onLayoutChangeListener;


    public MoRecyclerView(Activity a, int recyclerViewResources, RecyclerView.Adapter adapter){
        this.activity = a;
        this.recyclerViewResources = recyclerViewResources;
        this.mAdapter = adapter;
    }


    public MoRecyclerView setOrientation(int or){
        this.orientation = or;
        return this;
    }

    public MoRecyclerView setLayoutManager(RecyclerView.LayoutManager layoutManager){
        this.layoutManager = layoutManager;
        return this;
    }

    public MoRecyclerView setGridCount(int gc){
        this.gridCount = gc;
        return this;
    }

    public MoRecyclerView setReverseLayout(boolean b){
        this.reverseLayout = b;
        return this;
    }


    public MoRecyclerView setOnLayoutChangeListener(View.OnLayoutChangeListener l){
        this.onLayoutChangeListener = l;
        return this;
    }


    @SuppressLint("WrongConstant")
    public void show() {
        layoutManager= new LinearLayoutManager(activity,orientation, false);
        recyclerView = activity.findViewById(recyclerViewResources);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        if(this.onLayoutChangeListener!=null)
            recyclerView.addOnLayoutChangeListener(this.onLayoutChangeListener);
    }

    /**
     * scroll to the position if position is less than
     * the size of the array adapter data set
     * @param position
     */
    public void scrollTo(int position){
        if(position < mAdapter.getItemCount()) {
            recyclerView.scrollToPosition(position);
        }
    }


    // we only smooth scroll if the position is less than the size of
    // the array adapter
    public void smoothScrollTo(int position){
        if(position < mAdapter.getItemCount()){
            Handler handler = new Handler();
            handler.postDelayed(() -> recyclerView.smoothScrollToPosition(position),100);
        }
    }

    /**
     *
     */
    public void scrollToLastItem(){
        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    /**
     * only notifies item added at last
     * position if the length is higher than 0
     */
    public void notifyItemAddedLastPosition(){
        if(mAdapter.getItemCount()>0){
            mAdapter.notifyItemInserted(bottomPosition());
        }
    }


    public RecyclerView getRecyclerView(){
        return this.recyclerView;
    }

    public int bottomPosition(){
        return mAdapter.getItemCount()-1;
    }




    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    public void notifyLastItemChanged(){
        if(mAdapter.getItemCount()>0)
            mAdapter.notifyItemChanged(bottomPosition());
    }

    public void setAdapter(RecyclerView.Adapter adapter){
        this.mAdapter = adapter;
    }



    @SuppressLint("WrongConstant")
    public void switchView(boolean showInGrid, RecyclerView.Adapter adapter){
        if(showInGrid){
            // we want grid view
            layoutManager = new GridLayoutManager(activity, gridCount);
            recyclerView.setLayoutManager(layoutManager);
        }else{
            // we want list view
            layoutManager = new LinearLayoutManager(activity,orientation, reverseLayout);
            recyclerView.setLayoutManager(layoutManager);
        }
        mAdapter = adapter;
        recyclerView.setAdapter(mAdapter);
    }


}
