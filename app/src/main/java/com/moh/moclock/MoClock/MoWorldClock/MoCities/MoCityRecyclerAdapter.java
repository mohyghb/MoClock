package com.moh.moclock.MoClock.MoWorldClock.MoCities;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moofficial.moessentials.MoEssentials.MoUI.MoRecyclerView.MoRecyclerAdapters.MoRecyclerAdapter;

import java.util.List;

import com.moh.moclock.MoInflatorView.MoInflaterView;
import com.moh.moclock.R;

public class MoCityRecyclerAdapter extends MoRecyclerAdapter<MoCityRecyclerAdapter.MoCityViewHolder,MoCityCoordinate> {

    private MoOnCitySelectedListener onCitySelectedListener;

    public MoCityRecyclerAdapter(Context c, List<MoCityCoordinate> dataSet, MoOnCitySelectedListener onCitySelectedListener) {
        super(c, dataSet);
        this.onCitySelectedListener = onCitySelectedListener;
    }

    @NonNull
    @Override
    public MoCityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = MoInflaterView.inflate(R.layout.cell_city_suggestion, context);
        view.setLayoutParams(getMatchWrapParams());
        return new MoCityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoCityViewHolder holder, int position) {
        holder.name.setText(dataSet.get(position).getName());
        holder.name.setOnClickListener((v)-> onCitySelectedListener.onCitySelected(dataSet.get(position)));
    }

    public static class MoCityViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public MoCityViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_citySuggestion_name);
        }
    }

    public interface MoOnCitySelectedListener {
        void onCitySelected(MoCityCoordinate cityCoordinate);
    }
}

