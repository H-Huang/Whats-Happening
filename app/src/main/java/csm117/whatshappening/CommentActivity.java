package csm117.whatshappening;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {

    private Context context;

    ListView comment_list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Intent getId = getIntent();
        Integer id = getId.getIntExtra("id", 0);

        String get_comments_url = getString(R.string.get_comments);
        comment_list_view  =(ListView)findViewById(R.id.comment_list_view);
        getComments(get_comments_url, id);
    }

    private void getComments(String url, Integer id){
        final int id_val = id;

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
                                int idOfNote = obj.getInt("belongsToID");
                                if (idOfNote == id_val) {
                                    listItems.add(obj.getString("text"));
                                }
                            }
                            //arraylist Append
                            adapter=new ArrayAdapter<String>(context,
                                    android.R.layout.simple_list_item_1,
                                    listItems);
                            comment_list_view.setAdapter(adapter);

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
