package Mo.moclock.MoClock.MoTimer.MoTimerPresetPackage;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import Mo.moclock.MoDate.MoTimeUtils;
import Mo.moclock.MoIO.MoFile;
import Mo.moclock.MoIO.MoLoadable;
import Mo.moclock.MoIO.MoSavable;
import Mo.moclock.MoInflatorView.MoInflaterView;
import Mo.moclock.MoInflatorView.MoViewDisplayable;
import Mo.moclock.R;

public class MoTimerPreset implements MoSavable, MoLoadable, MoViewDisplayable {


    public static boolean isInDeleteMode = false;

    private static final String SEP_KEY = "motimerpresetsepkey";

    private String name;
    private long milliseconds;
    private boolean isSelected;

    public MoTimerPreset(String n, long ms){
        this.name = n;
        this.milliseconds = ms;
    }

    MoTimerPreset(){

    }

    public String getReadableTime(){
        return MoTimeUtils.convertToReadableFormat(this.milliseconds);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void click(){
        this.isSelected = !this.isSelected;
    }

    /**
     * loads a savable object into its class
     *
     * @param data
     * @param context
     */
    @Override
    public void load(String data, Context context) {
        String[] parts = MoFile.loadable(data);
        this.name = parts[0];
        this.milliseconds = Long.parseLong(parts[1]);
    }

    /**
     * @return the data that is going to be saved by the save method
     * inside the class which implements MoSavable
     */
    @Override
    public String getData() {
        return MoFile.getData(this.name,this.milliseconds);
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
        Context context = (Context)args[0];
        View v = MoInflaterView.inflate(R.layout.timer_preset_layout,context);

        TextView title = v.findViewById(R.id.timer_preset_text);
        TextView time = v.findViewById(R.id.timer_preset_time);

        title.setText(this.name);
        time.setText(MoTimeUtils.convertToReadableFormat(this.milliseconds));

        return v;
    }
}
