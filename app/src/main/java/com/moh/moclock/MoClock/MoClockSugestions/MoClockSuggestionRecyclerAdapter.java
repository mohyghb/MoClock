package com.moh.moclock.MoClock.MoClockSugestions;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.moh.moclock.MoInflatorView.MoInflaterView;
import com.moh.moclock.MoRunnable.MoRunnable;
import com.moh.moclock.R;

public class MoClockSuggestionRecyclerAdapter extends RecyclerView.Adapter<MoClockSuggestionRecyclerAdapter.TabViewHolder> {


    private ArrayList<MoClockSuggestion> suggestions;
    private Context context;
    private MoRunnable moRunnable;
    private Runnable deleteModeRunnable;
    private MoRunnable onSelectedItemsChanged;

    public static class TabViewHolder extends RecyclerView.ViewHolder {
        Button suggestionButton;


        public TabViewHolder(View v) {
            super(v);
            suggestionButton = v.findViewById(R.id.suggestion_button);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MoClockSuggestionRecyclerAdapter(ArrayList<MoClockSuggestion> myDataset,Context c) {
        this.suggestions = myDataset;
        this.context = c;
    }








    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return suggestions.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public TabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TabViewHolder(MoInflaterView.inflate(R.layout.clock_suggestion_layout,parent.getContext()));
    }


    @Override
    public void onBindViewHolder(@NonNull TabViewHolder holder, int position) {
        holder.suggestionButton.setText(suggestions.get(position).getSetFor().getReadableTime());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }





}
