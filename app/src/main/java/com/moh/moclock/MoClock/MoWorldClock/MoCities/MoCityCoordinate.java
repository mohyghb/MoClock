package com.moh.moclock.MoClock.MoWorldClock.MoCities;

import com.moofficial.moessentials.MoEssentials.MoUI.MoInteractable.MoSearchable.MoSearchableInterface.MoSearchableItem;
import com.moofficial.moessentials.MoEssentials.MoUI.MoInteractable.MoSearchable.MoSearchableUtils;

public class MoCityCoordinate implements MoSearchableItem {

    private String name;
    private String zoneId;
    private boolean isSearched;

    public String getName() {
        return name;
    }

    public MoCityCoordinate setName(String name) {
        this.name = name;
        return this;
    }

    public String getZoneId() {
        return zoneId;
    }

    public MoCityCoordinate setZoneId(String zoneId) {
        this.zoneId = zoneId;
        return this;
    }

    @Override
    public boolean updateSearchable(Object... objects) {
        return MoSearchableUtils.isSearchable(false, objects, this.name);
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
