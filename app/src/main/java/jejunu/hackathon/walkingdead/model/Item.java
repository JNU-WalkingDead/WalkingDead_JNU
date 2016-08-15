package jejunu.hackathon.walkingdead.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Kim on 2016-08-15.
 */
public class Item {

    private final MarkerOptions markerOptions;
    private LatLng position;

    public Item(MarkerOptions markerOptions) {
        this.markerOptions = markerOptions;
        position = markerOptions.getPosition();
    }

    public MarkerOptions getMarkerOptions() {
        return markerOptions;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
        markerOptions.position(position);
    }

}
