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
    private double priority;
    private int zipCode;
    private String locality;
    private String status;
    private String person_reponsible;

    public Report() {
    }

    public Report(String key, String title, String description, double latitude, double longitude,
                  String urlPhoto, double priority, int zipCode, String locality, String status, String person_reponsible) {
        this.key = key;
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.urlPhoto = urlPhoto;
        this.priority = priority;
        this.zipCode = zipCode;
        this.locality = locality;
        this.status = status;
        this.person_reponsible = person_reponsible;
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

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPerson_reponsible() {
        return person_reponsible;
    }

    public void setPerson_reponsible(String person_reponsible) {
        this.person_reponsible = person_reponsible;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Report){
            return ((Report) obj).getKey().equals(this.getKey());
        }
        return false;
    }
}
