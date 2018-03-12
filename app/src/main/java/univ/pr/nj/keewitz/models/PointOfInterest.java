package univ.pr.nj.keewitz.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * A point of interest saved on the map.
 */
public class PointOfInterest {

    private LatLng position;
    private String name;


    public PointOfInterest(LatLng position, String name) {
        this.position = position;
        this.name = name;
    }

    public LatLng getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }
}
