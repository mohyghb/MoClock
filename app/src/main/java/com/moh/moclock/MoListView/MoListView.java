package com.moh.moclock.MoListView;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;


public class MoListView <T>{


    private ListView listView;
    private ArrayAdapter<T> arrayAdapter;
    private List<T> list;
    private Context c;

    private View[] notEmptyViews;
    private View[] emptyViews;

    private Animation animationIn;
    private Animation animationOut;

    public MoListView(ListView v, ArrayAdapter<T> adapter, List<T> list, Context c){
        this.listView = v;
        this.arrayAdapter = adapter;
        this.list = list;
        this.c = c;
        this.show();
    }

    public MoListView(ListView v, ArrayAdapter<T> adapter, List<T> list, Context c, Animation in,Animation out){
        this.listView = v;
        this.arrayAdapter = adapter;
        this.list = list;
        this.c = c;
        this.animationIn = in;
        this.animationOut = out;
        this.show();
    }


    public void show() {
        listView.setAdapter(this.arrayAdapter);
    }


    public void setArrayAdapter(ArrayAdapter<T> a){
        this.arrayAdapter = a;
        this.show();
    }

    public void update(boolean ... b) {
        this.arrayAdapter.notifyDataSetChanged();
        switchViews(b);
    }

    private void switchViews(boolean ... b){
        if(b!=null && b.length>0 && !b[0]){
            return;
        }
        if(this.emptyViews!=null){
            if(this.list.isEmpty()){
                changeVisibility(true);
            }else{
                changeVisibility(false);
            }
        }
    }

    public ArrayAdapter getArrayAdapter(){
        return this.arrayAdapter;
    }

    private void changeVisibility(boolean empty){
        int val0 = empty?View.VISIBLE:View.GONE;
        for(View v: this.emptyViews){

//            v.setVisibility(val0);
            runAnimation(v,val0);
        }

        int val = empty?View.GONE:View.VISIBLE;
        for(View v: this.notEmptyViews){
//            v.setVisibility(val);
            runAnimation(v,val);
        }
    }

    private void runAnimation(View v,int visible){
        if(v.getVisibility() == visible) return;
        if(visible == View.VISIBLE){
            // make it visible, animation in
            if(animationIn!=null) v.startAnimation(animationIn);

        }else{
            // make it gone, animation out
            if(animationOut!=null) v.startAnimation(animationOut);
        }
        v.setVisibility(visible);
//        if(animation != null){
//            v.startAnimation(animation);
//        }
    }


    public void updateHideIfEmpty(){
        update();
        hideIfEmpty(false);
    }

    public void updateHideIfEmpty(boolean animation){
        update();
        hideIfEmpty(animation);
    }

    public void hideIfEmpty(boolean animation){
        if(this.list.isEmpty()){
            this.listView.setVisibility(View.GONE);
        }else{
//            if(animation){
//                MoAnimation.animateNoTag(listView,View.VISIBLE,MoAnimation.APPEAR);
//            }
            this.listView.setVisibility(View.VISIBLE);
        }
    }

    public void setEmptyView(View v){
        this.listView.setEmptyView(v);
    }


    /**
     *
     * @param v
     * the empty view that is shown when the list is empty
     * @param views
     * all the other views that are shown when the list is not empty
     */
    public void setDynamicEmptyView(View[] v, View ... views){
        this.emptyViews = v;
        this.notEmptyViews = views;
        update();
    }


}
