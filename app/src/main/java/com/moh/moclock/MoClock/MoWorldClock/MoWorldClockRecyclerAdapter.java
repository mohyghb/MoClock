package com.moh.moclock.MoClock.MoWorldClock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.moofficial.moessentials.MoEssentials.MoUI.MoInflatorView.MoInflaterView;
import com.moofficial.moessentials.MoEssentials.MoUI.MoInteractable.MoSelectable.MoSelectableUtils;
import com.moofficial.moessentials.MoEssentials.MoUI.MoRecyclerView.MoRecyclerAdapters.MoSelectableAdapter;

import java.util.List;

import com.moh.moclock.MoWorldClockSectionManager;
import com.moh.moclock.R;

public class MoWorldClockRecyclerAdapter extends MoSelectableAdapter<MoWorldClockRecyclerAdapter.MoWorldClockViewHolder, MoWorldClock> {


    public MoWorldClockRecyclerAdapter(Context c, List<MoWorldClock> dataSet) {
        super(c, dataSet);
    }

    @NonNull
    @Override
    public MoWorldClockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = MoInflaterView.inflate(R.layout.view_city_world_clock, parent , context);
        return new MoWorldClockViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MoWorldClockViewHolder holder, int position) {
        MoWorldClock worldClock = dataSet.get(position).update();
        holder.name.setText(worldClock.getName());
        holder.date.setText(worldClock.getMoDate().getReadableDate());
        holder.time.setText(worldClock.getMoDate().getReadableTime());
        holder.cardView.setOnLongClickListener((v) -> {
            if (isNotSelecting()) {
                startSelecting(position);
                return true;
            }
            return false;
        });
        holder.cardView.setOnClickListener((v) -> {
            if (isSelecting()) {
                onSelect(position);
            }
        });
        MoSelectableUtils.applySelectedDrawable(context, R.drawable.drawable_selected_delete_outline, holder.constraintLayout, dataSet.get(position));
    }

    @Override
    public void onBindViewHolder(@NonNull MoWorldClockViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else if (payloads.get(0).equals(MoWorldClockSectionManager.UPDATE_TIME_PAYLOAD)) {
            // updating the time
            holder.time.setText(dataSet.get(position).update().getMoDate().getReadableTime());
        } else {
            // selecting, so just update the color
            MoSelectableUtils.applySelectedDrawable(context, R.drawable.drawable_selected_delete_outline, holder.constraintLayout, dataSet.get(position));
        }
    }

    public static class MoWorldClockViewHolder extends RecyclerView.ViewHolder {

        private TextView name, date, time;
        private CardView cardView;
        private LinearLayout constraintLayout;

        public MoWorldClockViewHolder(@NonNull View v) {
            super(v);
            this.name = v.findViewById(R.id.name_city_world_clock);
            this.date = v.findViewById(R.id.date_world_clock);
            this.time = v.findViewById(R.id.time_world_clock);
            this.cardView = v.findViewById(R.id.card_cityView);
            this.constraintLayout = v.findViewById(R.id.layout_cityView_linear);
        }
    }

}
