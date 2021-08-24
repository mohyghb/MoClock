package com.moh.moclock.MoClock;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.moofficial.moessentials.MoEssentials.MoLog.MoLog;
import com.moofficial.moessentials.MoEssentials.MoUI.MoInflatorView.MoInflaterView;
import com.moofficial.moessentials.MoEssentials.MoUI.MoInteractable.MoSelectable.MoSelectable;
import com.moofficial.moessentials.MoEssentials.MoUI.MoInteractable.MoSelectable.MoSelectableUtils;
import com.moofficial.moessentials.MoEssentials.MoUI.MoRecyclerView.MoRecyclerAdapters.MoSelectableAdapter;

import java.util.List;

import com.moh.moclock.MoColor.MoColor;
import com.moh.moclock.MoUI.MoTextInput;
import com.moh.moclock.R;

public class MoAlarmClockRecyclerAdapter extends
        MoSelectableAdapter<MoAlarmClockRecyclerAdapter.MoAlarmClockViewHolder, MoAlarmClock> {

    public static String UPDATE_ENABLED_VIEW = "update_enabled";

    private MoOnActiveClockChanged listener;

    public MoAlarmClockRecyclerAdapter(Context c, List<MoAlarmClock> dataSet, MoOnActiveClockChanged onActiveClockChanged) {
        super(c, dataSet);
        this.listener = onActiveClockChanged;
    }

    @NonNull
    @Override
    public MoAlarmClockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = MoInflaterView.inflate(R.layout.modern_alarm_card, parent, context);
        return new MoAlarmClockViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MoAlarmClockViewHolder holder, int position) {
        MoAlarmClock clock = dataSet.get(position);
        clock.setListener(new MoAlarmClock.MoAlarmClockListener() {
            @Override
            public void onActiveChanged(boolean isActive) {
                onBindAgain(holder, clock);
            }

            @Override
            public void onRepeatingChanged() {
                onBindAgain(holder, clock);
            }
        });

        holder.bind(clock);
        holder.cardView.setOnClickListener((v) -> {
            if (isSelecting()) {
                onSelect(position);
            } else {
                // open alarm edit mode
                listener.onCardClicked(clock);
            }
        });
        holder.cardView.setOnLongClickListener((v) -> {
            if (isNotSelecting()) {
                startSelecting(position);
                notifyItemRangeChanged(0, getItemCount(), UPDATE_ENABLED_VIEW);
                return true;
            }
            return false;
        });
        updateEnabledSwitch(holder);
        MoSelectableUtils.applySelectedDrawable(context,R.drawable.drawable_selected_delete_outline, holder.constraintLayout, clock);
    }

    private void onBindAgain(@NonNull MoAlarmClockViewHolder holder, MoAlarmClock clock) {
        holder.bind(clock);
        listener.onActiveStatusChanged(clock);
    }

    private void updateEnabledSwitch(@NonNull MoAlarmClockViewHolder holder) {
        if (isSelecting()) {
            holder.enabled.setVisibility(View.INVISIBLE);
        } else {
            holder.enabled.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MoAlarmClockViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            if (payloads.contains(MoSelectable.PAYLOAD_SELECTED_ITEM)) {
                MoSelectableUtils.applySelectedDrawable(context,R.drawable.drawable_selected_delete_outline, holder.constraintLayout, dataSet.get(position));
            }
            if (payloads.contains(UPDATE_ENABLED_VIEW)) {
                // need to update the switch view
                updateEnabledSwitch(holder);
            }
        }
    }

    public static class MoAlarmClockViewHolder extends RecyclerView.ViewHolder {
        TextView time, date, title, repeatingDays;
        SwitchMaterial enabled;
        CardView cardView;
        MoOnActiveClockChanged listener;
        ConstraintLayout constraintLayout;

        public MoAlarmClockViewHolder(@NonNull View v, MoOnActiveClockChanged onActiveClockChanged) {
            super(v);
            time = v.findViewById(R.id.alarm_time_textView);
            date = v.findViewById(R.id.alarm_date_TextView);
            title = v.findViewById(R.id.alarm_title_textView);
            repeatingDays = v.findViewById(R.id.repeatingDaysTextView);
            enabled = v.findViewById(R.id.is_active_switch);
            cardView = v.findViewById(R.id.alarm_card_view);
            constraintLayout = v.findViewById(R.id.alarm_constraintLayout);
            this.listener = onActiveClockChanged;
        }

        public void bind(MoAlarmClock clock) {
            time.setText(clock.getDate().getReadableTime());
            date.setText(clock.getDate().getReadableDate());
            enabled.setOnClickListener((v) -> onSwitchClicked(clock));
            bindTitle(clock);
            initEnabledSwitch(clock);
            bindRepeatingDays(clock);
            updateActiveColors(clock.isActive());
        }

        private void bindRepeatingDays(MoAlarmClock clock) {
            if (!clock.getRepeating().isEmpty()) {
                repeatingDays.setVisibility(View.VISIBLE);
                repeatingDays.setText(clock.getRepeating().readableFormat());
            } else {
                repeatingDays.setVisibility(View.GONE);
            }
        }

        private void bindTitle(MoAlarmClock clock) {
            MoLog.print(clock.getTitle());
            if (clock.getTitle().isEmpty()) {
                title.setVisibility(View.GONE);
            } else {
                title.setVisibility(View.VISIBLE);
                title.setText(clock.getTitle());
            }
        }

        private void updateActiveColors(boolean isActive) {
            Context c = itemView.getContext();
            MoTextInput.setColorCondition(isActive, MoColor.color_text_on_highlight, MoColor.color_text_disabled, time, c);
            MoTextInput.setColorCondition(isActive, MoColor.color_text_on_highlight, MoColor.color_text_disabled, date, c);
            MoTextInput.setColorCondition(isActive, MoColor.color_text_on_highlight, MoColor.color_text_disabled, repeatingDays, c);
            MoTextInput.setColorCondition(isActive, MoColor.color_text_on_normal, MoColor.color_text_disabled, title, c);
        }

        private void initEnabledSwitch(MoAlarmClock clock) {
            enabled.setChecked(clock.isActive());
            enabled.setOnCheckedChangeListener(null);
        }

        private void onSwitchClicked(MoAlarmClock clock) {
            clock.setActiveWithoutInvokingListener(!clock.isActive());
            boolean isActive = clock.isActive();

            enabled.setChecked(isActive);
            updateActiveColors(isActive);
            if (isActive) {
                // activate it
                clock.activate(itemView.getContext());
            } else {
                // cancel the alarm
                try {
                    MoAlarmClockManager.getInstance().cancelAlarm(clock.getId(), itemView.getContext());
                } catch (MoEmptyAlarmException e) {
                    e.printStackTrace();
                }
            }
            date.setText(clock.getDate().getReadableDate());
            listener.onActiveStatusChanged(clock);
        }
    }


    public interface MoOnActiveClockChanged {
        void onActiveStatusChanged(MoAlarmClock clock);
        void onCardClicked(MoAlarmClock clock);
    }
}
