package enterprise.com.finders.app.app.retro;
//AIzaSyA094bp0pO5o4qPMYn8ON8JhZmzVmW6vC4
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RESTService {
    @POST("/v1/images:annotate?key=AIzaSyA094bp0pO5o4qPMYn8ON8JhZmzVmW6vC4")
    Call<SResponse> scanImage(@Body CloudVision vision);

}
