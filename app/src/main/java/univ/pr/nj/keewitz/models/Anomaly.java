package univ.pr.nj.keewitz.models;

import com.google.android.gms.maps.model.LatLng;

import univ.pr.nj.keewitz.utils.FirebaseUtils;

public class Anomaly {

    private LatLng position;
    private String criticity;
    private String urlImage;


    public Anomaly() {}

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public String getCriticity() {
        return criticity;
    }

    public void setCriticity(String criticity) {
        this.criticity = criticity;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public void sendInfosToFirebase(String fileName){
        if (urlImage != null){
            FirebaseUtils.writeValue(urlImage, "anomalies", fileName, "urlImage");
        }
        if (position != null){
            FirebaseUtils.writeValue(position.toString(), "anomalies", fileName, "position");
        }
        if (criticity != null){
            FirebaseUtils.writeValue(criticity, "anomalies", fileName, "criticity");
        }
    }
}
