package enterprise.com.finders.app.app.retro;

public class CloudVisionRequest {
    public Image image;
public Feature[] features;

    public CloudVisionRequest(Image image, Feature[] features) {
        this.image = image;
        this.features = features;
    }
}
