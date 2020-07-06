package Mo.moclock.MoClock.MoTimer.MoTimerPresetPackage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

import Mo.moclock.MoInflatorView.MoInflaterView;
import Mo.moclock.MoRunnable.MoRunnable;
import Mo.moclock.R;

public class MoPresetRecyclerAdapter extends RecyclerView.Adapter<MoPresetRecyclerAdapter.TabViewHolder> {


    private ArrayList<MoTimerPreset> presets;
    private Context context;
    private MoRunnable moRunnable;
    private Runnable deleteModeRunnable;
    private MoRunnable onSelectedItemsChanged;

    public static class TabViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView time;
        CardView cardView;

        LinearLayout linearLayout;


        public TabViewHolder(View v) {
            super(v);

            title = v.findViewById(R.id.timer_preset_text);
            time = v.findViewById(R.id.timer_preset_time);
            cardView = v.findViewById(R.id.preset_card_view);
            linearLayout = v.findViewById(R.id.preset_linear_layout);
            //tab = v.findViewById(R.id.tab_view_holder);
        }

        public void removeView(){
            ((ViewGroup) itemView).removeAllViews();
        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MoPresetRecyclerAdapter(ArrayList<MoTimerPreset> myDataset,
                                   Context c,MoRunnable change,
                                   Runnable deleteModeRunnable, MoRunnable onSelectedItemsChanged) {
        this.presets = myDataset;
        this.context = c;
        this.moRunnable = change;
        this.deleteModeRunnable = deleteModeRunnable;
        this.onSelectedItemsChanged = onSelectedItemsChanged;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TabViewHolder onCreateViewHolder(ViewGroup parent,
                                            int viewType) {
        View v = MoInflaterView.inflate(R.layout.timer_preset_layout,parent.getContext());
        return new TabViewHolder(v);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(TabViewHolder holder, int position) {
        holder.title.setText(presets.get(position).getName());
        holder.time.setText(presets.get(position).getReadableTime());
        holder.cardView.setOnClickListener(view -> onClickCardView(position));

        holder.cardView.setOnLongClickListener(view -> {
            if(!MoTimerPreset.isInDeleteMode){
                deleteModeRunnable.run();
                onClickCardView(position);
                notifyDataSetChanged();
            }
            return true;
        });

        if(presets.get(position).isSelected()){
            // red background
            Drawable drawable = context.getDrawable(R.drawable.glow_edge_red);
            holder.linearLayout.setBackground(drawable);
        }else{
            // normal background
            Drawable drawable = context.getDrawable(R.drawable.glow_edge);
            holder.linearLayout.setBackground(drawable);
        }
    }


    private void onClickCardView(int position){
        if(MoTimerPreset.isInDeleteMode){
            // select this preset
            presets.get(position).click();
            notifyItemChanged(position);
            onSelectedItemsChanged.run(MoTimerPresetManager.getSelectedSize());
        }else{
            // load the timer with preset value
            moRunnable.run(presets.get(position).getMilliseconds());
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return presets.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }





}
