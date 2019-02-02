package enterprise.com.finders.app.app.model;

import java.io.Serializable;

/**
 * Created by Plarent on 1/30/2018.
 */

public class Report implements Serializable{
    private String key;
    private String title;
    private String description;
    private double latitude;
    private double longitude;
    private String urlPhoto;
    private int zipcode;

    public Report() {
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public Report(String key, String title, String description, double latitude, double longitude, String urlPhoto, int zipcode) {
        this.key = key;
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.urlPhoto = urlPhoto;
        this.zipcode = zipcode;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Report){
            return ((Report) obj).getKey().equals(this.getKey());
        }
        return false;
    }
}
