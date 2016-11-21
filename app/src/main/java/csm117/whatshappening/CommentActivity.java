package csm117.whatshappening;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {
    public Activity activity = null;
    ListView comment_list_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        activity = this;
        comment_list_view  =(ListView)findViewById(R.id.comment_list_view);

        Intent getId = getIntent();
        int id = getId.getIntExtra("location_id", 0);

        final String id_string = Integer.toString(id);

        String get_comments_url = getString(R.string.get_comments);
        final String create_comments_url = get_comments_url;
        getComments(get_comments_url, id);

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

        //create comment button
        final Button create_comment = (Button) findViewById(R.id.create_comment);
        assert create_comment != null;
        create_comment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText comment_edit_text = (EditText) findViewById(R.id.create);
                final String comment_text = comment_edit_text.getText().toString();
                makeComment(create_comments_url, comment_text, id_string);
                Intent i = new Intent(getApplicationContext(),CommentActivity.class);
                Integer id_final = Integer.parseInt(id_string);
                i.putExtra("location_id", id_final);
                startActivity(i);
                finish();
            }
        });

    }

    private void getComments(String url, int id){
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
                                String string_idOfNote = obj.getString("belongsToID");
                                int idOfNote = Integer.parseInt(string_idOfNote);
                                if (idOfNote == id_val) {
                                    listItems.add(obj.getString("text"));
                                }
                            }
                            //arraylist Append
                            adapter=new ArrayAdapter<String>(activity,
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

    private void makeComment(String CREATE_COMMENT, String comment, String id){
        final String final_comment = comment;
        final String final_id = id;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, CREATE_COMMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(InputActivity.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(InputActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("text", final_comment);
                params.put("belongsToID", final_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
