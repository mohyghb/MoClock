package Mo.moclock.MoClock.MoWorldClock;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moofficial.moessentials.MoEssentials.MoUI.MoInflatorView.MoInflaterView;
import com.moofficial.moessentials.MoEssentials.MoUI.MoRecyclerView.MoRecyclerAdapters.MoSelectableAdapter;

import java.util.List;

import Mo.moclock.R;

public class MoWorldClockRecyclerAdapter extends MoSelectableAdapter<MoWorldClockRecyclerAdapter.MoWorldClockViewHolder, MoWorldClock> {


    public MoWorldClockRecyclerAdapter(Context c, List<MoWorldClock> dataSet) {
        super(c, dataSet);
    }

    @NonNull
    @Override
    public MoWorldClockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = MoInflaterView.inflate(R.layout.view_city_world_clock, parent.getContext());
        return new MoWorldClockViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MoWorldClockViewHolder holder, int position) {
        MoWorldClock worldClock = dataSet.get(position);
        holder.name.setText(worldClock.getName());
        holder.date.setText(worldClock.getMoDate().getReadableDate());
        holder.time.setText(worldClock.getMoDate().getReadableTime());
    }

    public static class MoWorldClockViewHolder extends RecyclerView.ViewHolder {

        private TextView name, date, time;

        public MoWorldClockViewHolder(@NonNull View v) {
            super(v);
            this.name = v.findViewById(R.id.name_city_world_clock);
            this.date = v.findViewById(R.id.date_world_clock);
            this.time = v.findViewById(R.id.time_world_clock);
        }
    }

}
