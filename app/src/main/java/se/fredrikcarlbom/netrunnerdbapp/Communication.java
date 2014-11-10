package se.fredrikcarlbom.netrunnerdbapp;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;

public class Communication {
    private Context context;
    private String basePath;
    private JSONArray cards;

    private static final String allCardPath = "cards/";

    public Communication(Context context, String basePath) {
        this.basePath = basePath;
        this.context = context;
    }

    public void FetchCards(Response.Listener<String> listener) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this.context);
        String url = this.basePath + allCardPath;
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        };

        StringRequest request = new StringRequest(url, listener, errorListener);
        // Add the request to the RequestQueue.
        queue.add(request);
    }

}