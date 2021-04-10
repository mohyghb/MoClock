package Mo.moclock.MoClock.MoWorldClock;

public class MoCountryCityCoordinate {

    private String name;
    private double lat;
    private double lon;

    public String getName() {
        return name;
    }

    public MoCountryCityCoordinate setName(String name) {
        this.name = name;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public MoCountryCityCoordinate setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLon() {
        return lon;
    }

    public MoCountryCityCoordinate setLon(double lon) {
        this.lon = lon;
        return this;
    }
}
