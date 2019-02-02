package enterprise.com.finders.app.app.retro;

public class CloudVision {
    public CloudVisionRequest[] requests;
public CloudVision(String image, String type){
    requests = new CloudVisionRequest[1];
    Image im = new Image(image);
    Feature[] features = new Feature[1];
    Feature f = new Feature(type);
    features[0]=f;
    requests[0] = new CloudVisionRequest(im,features);

}
    public CloudVision(String image){
     this(image,"SAFE_SEARCH_DETECTION");

    }
}
