package se.fredrikcarlbom.netrunnerdbapp;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;

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

    public void FetchImage(Consumer<Bitmap> consumer, String relativeUrl) {
        DownloadImageTask downloadImageTask = new DownloadImageTask(consumer);
        downloadImageTask.execute(hostname + relativeUrl);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        Consumer<Bitmap> consumer;

        public DownloadImageTask(Consumer<Bitmap> consumer) {
            this.consumer = consumer;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            consumer.onResponse(bitmap);
        }
    }

}