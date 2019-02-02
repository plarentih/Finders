package enterprise.com.finders.app.app.model;

import java.io.Serializable;

/**
 * Created by Plarent on 1/30/2018.
 */

public class Contact implements Serializable {
    private String key;
    private String contactName;
    private String contactNumber;

    public Contact(){

    }

    public Contact(String key, String contactName, String contactNumber) {
        this.key = key;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
