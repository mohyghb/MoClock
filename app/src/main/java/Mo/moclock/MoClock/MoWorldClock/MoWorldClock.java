package Mo.moclock.MoClock.MoWorldClock;

import android.app.Activity;
import android.content.Context;

import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoFile;
import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoLoadable;
import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoSavable;
import com.moofficial.moessentials.MoEssentials.MoUI.MoInteractable.MoSelectable.MoSelectableInterface.MoSelectableItem;

import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;
import Mo.moclock.MoClock.MoWorldClock.MoCities.MoCityCoordinate;
import Mo.moclock.MoDate.MoDate;
import Mo.moclock.MoHotWordDetection.MoHotWordDetector;


public class MoWorldClock implements MoSavable, MoLoadable, MoSelectableItem {


    private String name;
    private String zoneId;
    private TimeZone timeZone;
    private MoDate moDate = new MoDate();
    private boolean isSelected;

    public String getName() {
        return name;
    }

    public MoWorldClock setName(String name) {
        this.name = name;
        return this;
    }

    public String getZoneId() {
        return zoneId;
    }

    public MoWorldClock setZoneId(String zoneId) {
        this.zoneId = zoneId;
        return this;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public MoWorldClock setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public void apply(Calendar c) {
        c.setTimeZone(this.timeZone);
    }

    public MoDate getMoDate() {
        return moDate;
    }

    // updates the date time
    public MoWorldClock update() {
        this.moDate = new MoDate();
        return this;
    }

    public MoWorldClock updateTimeZone() {
        // todo this needs to be changed in another ticket
        timeZone = TimeZone.getTimeZone("America/Vancouver");
        moDate.getCalendar().setTimeZone(timeZone);
        return this;
    }

    @Override
    public void load(String data, Context context) {
        String[] com = MoFile.loadable(data);
        this.name = com[0];
        this.zoneId = com[1];

        updateTimeZone();
    }

    @Override
    public String getData() {
        return MoFile.getData(this.name, this.zoneId);
    }

    @Override
    public void setSelected(boolean b) {
        this.isSelected = b;
    }

    @Override
    public boolean isSelected() {
        return this.isSelected;
    }


    public static MoWorldClock from(MoCityCoordinate cityCoordinate) {
        // todo this needs to be addressed in another ticket, we are just hard coding timezones right now
        return new MoWorldClock()
                .setName(cityCoordinate.getName())
                .setZoneId("")
                .updateTimeZone();
    }
}
