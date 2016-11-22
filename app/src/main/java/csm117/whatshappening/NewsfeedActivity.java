package csm117.whatshappening;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class NewsfeedActivity extends AppCompatActivity {

    public Activity activity = null;
    ListView location_note_list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);

        activity = this;
        location_note_list_view =(ListView)findViewById(R.id.newsfeed_listview);
        //get location notes url
        String get_location_notes_url = getString(R.string.get_location_notes);
        getLocationNotes(get_location_notes_url);

        //back button
        final Button map_redirect = (Button) findViewById(R.id.back_button);
        assert map_redirect != null;
        map_redirect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


    private void getLocationNotes(String url){
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, (String)null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response

                        //to populate comment list view
                        ArrayAdapter<String> adapter;
                        ArrayList<String> listItems=new ArrayList<String>();

                        try {
                            for (int i = 0; i < response.length(); i++){
                                JSONObject obj = response.getJSONObject(i);
                                listItems.add(obj.getString("title") + ": " + obj.getString("description"));
                            }

                            //want the recently created ones to appear at the top
                            Collections.reverse(listItems);

                            //arraylist Append
                            adapter=new ArrayAdapter<String>(activity,
                                    android.R.layout.simple_list_item_1,
                                    listItems);
                            location_note_list_view.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        int socketTimeout = 6000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        getRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(getRequest);
    }
}
