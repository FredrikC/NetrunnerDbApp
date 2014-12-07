package se.fredrikcarlbom.netrunnerdbapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.InputStream;

public class Communication {
    private Context context;
    private String hostname;
    private String baseFolder;

    private static final String allCardPath = "cards/";

    public Communication(Context context, String hostname, String baseFolder) {
        this.hostname = hostname;
        this.baseFolder = baseFolder;
        this.context = context;
    }

    public void FetchCards(Response.Listener<String> listener) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this.context);
        String url = this.hostname + baseFolder + allCardPath;
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        };

        StringRequest request = new StringRequest(url, listener, errorListener);
        // Add the request to the RequestQueue.
        queue.add(request);
    }

    public void FetchImage(Consumer<String> consumer, String relativeUrl) {
        Log.d("test", "Fetching image: "+relativeUrl);
        DownloadImageTask downloadImageTask = new DownloadImageTask(consumer);
        downloadImageTask.execute(hostname + relativeUrl);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, String> {
        Consumer<String> consumer;

        public DownloadImageTask(Consumer<String> consumer) {
            this.consumer = consumer;
        }

        @Override
        protected String doInBackground(String... urls) {
            String urldisplay = urls[0];
            StringBuilder buffer = new StringBuilder();
            byte[] data = new byte[16384];
            try {
                InputStream incomingStream = new java.net.URL(urldisplay).openStream();
                while ((incomingStream.read(data, 0, data.length)) != -1) {
                    buffer.append(data);
                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return buffer.toString();
        }
    }

}