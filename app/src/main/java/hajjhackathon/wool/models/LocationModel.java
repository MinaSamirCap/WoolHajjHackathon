package hajjhackathon.wool.models;

public class LocationModel {

    public static final int TYPE_BUSY = 20;
    public static final int TYPE_FREE = 21;

    public double lat;
    public double lng;
    public int type;

    public LocationModel() {
    }

    public LocationModel(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static LocationModel getFreeType(double lat, double lng) {
        LocationModel model = new LocationModel(lat, lng);
        model.type = TYPE_FREE;
        return model;
    }

    public static LocationModel getBusyType(double lat, double lng) {
        LocationModel model = new LocationModel(lat, lng);
        model.type = TYPE_BUSY;
        return model;
    }
}
