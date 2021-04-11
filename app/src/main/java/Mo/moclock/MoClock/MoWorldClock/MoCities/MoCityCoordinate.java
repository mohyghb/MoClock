package Mo.moclock.MoClock.MoWorldClock.MoCities;

import com.moofficial.moessentials.MoEssentials.MoUI.MoInteractable.MoSearchable.MoSearchableInterface.MoSearchableItem;
import com.moofficial.moessentials.MoEssentials.MoUI.MoInteractable.MoSearchable.MoSearchableUtils;

public class MoCityCoordinate implements MoSearchableItem {

    private String name;
    private double lat;
    private double lon;
    private boolean isSearched;

    public String getName() {
        return name;
    }

    public MoCityCoordinate setName(String name) {
        this.name = name;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public MoCityCoordinate setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLon() {
        return lon;
    }

    public MoCityCoordinate setLon(double lon) {
        this.lon = lon;
        return this;
    }

    @Override
    public boolean updateSearchable(Object... objects) {
        return MoSearchableUtils.isSearchable(true, objects, this.name);
    }

    @Override
    public boolean isSearchable() {
        return this.isSearched;
    }

    @Override
    public void setSearchable(boolean b) {
        this.isSearched = b;
    }
}
