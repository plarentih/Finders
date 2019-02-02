package enterprise.com.finders.app.app.retro;


import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VisionTest extends IntentService {
    public VisionTest() {
        this("Vision");
    }

    public VisionTest(String name) {
        super(name);
    }

    public void test(byte[] image){

        String imageString=Base64.encodeToString(image,Base64.DEFAULT);
        CloudVision c = new CloudVision(imageString);
        final Gson gson = new GsonBuilder()
                //.registerTypeAdapter(Boolean.class, new BooleanTypeAdapter())
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vision.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        final RESTService connection = retrofit.create(RESTService.class);
        Call<SResponse> call = connection.scanImage(c);
        call.enqueue(new Callback<SResponse>() {
            @Override
            public void onResponse(Call<SResponse> call, Response<SResponse> response) {
               String s= response.body().responses[0].safeSearchAnnotation.adult;
                Intent i = new Intent("RESULT");

                i.putExtra("adult",s);
                LocalBroadcastManager.getInstance(VisionTest.this).sendBroadcast(i);

            }

            @Override
            public void onFailure(Call<SResponse> call, Throwable t) {

            }
        });
    }
    public void test(String path){
        //String path = getPath(uri);
        File file = new File(path);
        byte[] image = new byte[(int) file.length()];
        try {
        FileInputStream fis = new FileInputStream(file);
        fis.read(image); //read file into bytes[]

            fis.close();
            test(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String path =intent.getStringExtra("path");
        test(path);
    }
}
