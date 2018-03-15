package univ.pr.nj.keewitz.models;

import com.google.android.gms.maps.model.LatLng;

import univ.pr.nj.keewitz.utils.FirebaseUtils;

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

    public void sendInfosToFirebase(){
        FirebaseUtils.writeValue(String.valueOf(position.latitude), "pointsOfInterest", name, "lat");
        FirebaseUtils.writeValue(String.valueOf(position.longitude), "pointsOfInterest", name, "lng");
    }
}
