package jejunu.hackathon.walkingdead;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by replay on 2016. 7. 12..
 */
public class Zombie {

    private final MarkerOptions zombieMarkerOptions;
    private LatLng position;

    public Zombie(MarkerOptions zombieMarkerOptions) {
        this.zombieMarkerOptions = zombieMarkerOptions;
        position = zombieMarkerOptions.getPosition();
    }

    public MarkerOptions getZombieMarkerOptions() {
        return zombieMarkerOptions;
    }

    public void setPosition(LatLng position) {
        this.position = position;
        zombieMarkerOptions.position(position);
    }

    public LatLng getPosition() {
        return position;
    }
}
