package dfds.dfds;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.contentful.java.cda.CDAArray;
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDAField;
import com.contentful.java.cda.CDAResource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Home
extends AppCompatActivity
{
    private TextView contentfulTextview ;
    private static final String TAG = "Home";
    String data;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Button button= findViewById(R.id.passenger);
        contentfulTextview = findViewById(R.id.contentfultv);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Home.this,PassengerActivity.class);
                startActivity(in);

            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);


      CDAClient client = CDAClient.builder().setSpace("s3r4cdknpx2r").setToken("b09118e753e794feaaff4ae09a3d4bc2d4f781835d7fa99f44ff486c33bb37ef").build();
        //CDAArray array = client.fetch(CDAEntry.class).all();
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();

        client.observe(CDAEntry.class)
                .where("content_type", "textAndImageContent")
                .all()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<CDAArray>() {
                    CDAArray result;

                    @Override
                    public void onComplete() {
                        for (CDAResource resource : result.items()) {
                            CDAEntry entry = (CDAEntry) resource;
                            Log.i("Contentful", entry.getField("productName").toString());
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        Log.e("Contentful", "could not request entry", error);
                    }

                    @Override
                    public void onSubscribe(Subscription s) {

                    }

                    @Override
                    public void onNext(CDAArray cdaArray) {
                        result = cdaArray;
                    }
                });
    }

}
