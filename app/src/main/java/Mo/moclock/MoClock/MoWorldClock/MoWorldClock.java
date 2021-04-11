package Mo.moclock.MoClock.MoWorldClock;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoLoadable;
import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoSavable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import Mo.moclock.MoAnimation.MoAnimation;
import Mo.moclock.MoClock.MoAlarmListView;
import Mo.moclock.MoDate.MoDate;;
import Mo.moclock.MoId.MoId;
import Mo.moclock.MoInflatorView.MoInflaterView;
import Mo.moclock.MoInflatorView.MoViewDisplayable;
import Mo.moclock.R;

public class MoWorldClock implements MoSavable, MoLoadable, MoViewDisplayable {


    private static final String TIME_ZONE_IN = "time zone in";

    public static boolean isInDeleteMode = false;

    /**
     * A world clock has a name of city
     * and the difference time with the their location time
     */

    //private String id;
    //private TimeZone timeZone;
    private boolean searchMode;
    private MoTimeZoneOffset timeZone;
    private boolean isSelected;
    private MoId id;

    public MoWorldClock(MoTimeZoneOffset tz){
        this.timeZone = tz;
        this.searchMode = true;
        this.id = new MoId();
    }

    // for loading
    MoWorldClock(){
        this.id = new MoId();
    }

    public void parseSearchResult(String nameAndTimeZone){
        String lowerCase = nameAndTimeZone.toLowerCase();
    }

//    public MoWorldClock(String id,boolean searchMode){
//        this.id = id;
//        this.searchMode = searchMode;
//    }
//
//    public boolean hasPortionsOf(String s){
//        return this.id.toLowerCase().contains(s.toLowerCase());
//    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void onSelect(){
        this.isSelected = !this.isSelected;
    }

    public void setSearchMode(boolean searchMode) {
        this.searchMode = searchMode;
    }

    /**
     * loads a savable object into its class
     *
     * @param data
     * @param context
     */
    @Override
    public void load(String data, Context context) {
        this.timeZone = new MoTimeZoneOffset();
        this.timeZone.load(data,context);
        this.searchMode = false;
    }

    /**
     * @return the data that is going to be saved by the save method
     * inside the class which implements MoSavable
     */
    @Override
    public String getData() {
        return timeZone.getData();
    }

    /**
     * takes args and makes the class which implements it
     * displayable for the user
     * for example this can be used in the U.I
     *
     * @param args arguments that are passed to this method via other classes
     */
    @Override
    public View display(Object... args) {
        Context context = (Context) args[0];
        View v = MoInflaterView.inflate(R.layout.view_city_world_clock,context);
        MoWorldClockArrayAdapter arrayAdapter = (MoWorldClockArrayAdapter) args[1];
        Boolean shouldAnimate = (Boolean) args[2];


        View divider = v.findViewById(R.id.clock_divider);
        divider.setVisibility(View.INVISIBLE);
        TextView name = v.findViewById(R.id.name_city_world_clock);
        TextView currentTime = v.findViewById(R.id.time_world_clock);
        TextView currentDate = v.findViewById(R.id.date_world_clock);
        CheckBox checkBox = v.findViewById(R.id.world_clock_check_box);

        name.setText(this.timeZone.getNameCity());

        MoDate date = new MoDate();
        this.timeZone.applyTimeZone(date);
        currentTime.setText(date.getReadableTime());
        currentDate.setText(date.getReadableDate());








        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!compoundButton.isPressed()){
                    return;
                }
                selectWorldClock(checkBox,arrayAdapter);
            }
        });

        CardView cardView = v.findViewById(R.id.world_clock_card_view);


        cardView.setOnClickListener((view)->{
            // if they click they want this to be added to their collection
            // pop a dialog to make sure they want to do that
            if(this.searchMode){
                new MaterialAlertDialogBuilder(context)
                        .setTitle("Confirm")
                        .setMessage(String.format("Would you like to add %s to your world clocks?",this.timeZone.getNameCity()))
                        .setPositiveButton("Add", (dialogInterface, i) -> {
                            // add it here
                            MoWorldClockManager.manager.add(context,this);
                            ((Activity) context).finish();
                        })
                        .setNegativeButton("cancel",null)
                        .show();
            }else if(isInDeleteMode){
                // select it
                selectWorldClock(checkBox,arrayAdapter);
            }

        });

        if(!searchMode){
            cardView.setOnLongClickListener(view -> {
                // this is for deleting these items
                activateDeleteMode(checkBox,arrayAdapter);
                return true;
            });

            if(isInDeleteMode){
                checkBox.setChecked(this.isSelected);
                MoAnimation.animate(checkBox,View.VISIBLE,MoAnimation.APPEAR,this.id,shouldAnimate);
            }else{
                MoAnimation.animate(checkBox,View.GONE,MoAnimation.DISAPPEAR,this.id,shouldAnimate);
            }
        }



        return v;
    }

    private void selectWorldClock(CheckBox checkBox,MoWorldClockArrayAdapter arrayAdapter){
        this.isSelected = !this.isSelected;
        checkBox.setChecked(this.isSelected);
        arrayAdapter.onClickToSelect(this);
    }

    private void activateDeleteMode(CheckBox checkBox,MoWorldClockArrayAdapter arrayAdapter){
        if(!isInDeleteMode){
            isInDeleteMode = true;
            this.isSelected = true;
            arrayAdapter.activateDeleteMode(true);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoWorldClock that = (MoWorldClock) o;
        return Objects.equals(this.timeZone.getNameCity(), that.timeZone.getNameCity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.timeZone.getNameCity());
    }



}
