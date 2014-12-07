package se.fredrikcarlbom.netrunnerdbapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends ArrayAdapter<IncomingCard> {

    private List<IncomingCard> values;
    private LayoutInflater layoutInflater;
    private File applicationDir;
    private Communication communication;
    private ObjectMapper mapper;
    private List<IncomingCard> incomingCards;
    private Context context;

    public CardAdapter(Context context, List<IncomingCard> values) {
        super(context, R.layout.card, values);
        Log.d("test", "Constructor ");
        this.values = values;
        this.context = context;
        mapper = new ObjectMapper();
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        applicationDir = context.getFilesDir();
        communication = new Communication(context, "http://netrunnerdb.com/", "api/");
        DownloadImages();
    }

    private String FilenameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public void DownloadImages() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("test", "Before " +context.getFilesDir().getAbsoluteFile());
                try {
                    FileOutputStream outputStream = context.openFileOutput("Cards.json", Context.MODE_PRIVATE);
                    outputStream.write(response.getBytes());
                    Log.d("test", "Cards.json" + outputStream.getFD().toString());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    FileInputStream cardFile = context.openFileInput("Cards.json");
                    int cardFileSize = cardFile.available();
                    if (cardFileSize > 0) {
                        byte[] cardFileBuffer = new byte[cardFileSize];
                        cardFile.read(cardFileBuffer, 0, cardFileSize);
                        incomingCards = mapper.readValue(cardFileBuffer, new TypeReference<ArrayList<IncomingCard>>() {});
                        Log.d("test", "Read Cards.json "+incomingCards.size());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //for (final IncomingCard incomingCard : incomingCards) {
                final IncomingCard incomingCard = incomingCards.get(0);
                    communication.FetchImage(new Consumer<String>() {
                        public void onResponse(String response) {
                            try {
                                Log.d("test", "Response image: "+ FilenameFromUrl(incomingCard.ImageSrc));
                                FileOutputStream outputStream = context.openFileOutput(FilenameFromUrl(incomingCard.ImageSrc), Context.MODE_PRIVATE);
                                outputStream.write(response.getBytes());
                                outputStream.close();
                                Log.d("test", "Wrote "+ FilenameFromUrl(incomingCard.ImageSrc));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, incomingCard.ImageSrc);

                //}
                Log.d("test", "Done");

            }
        };

        communication.FetchCards(listener);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cardView = layoutInflater.inflate(R.layout.card, parent, false);
        TextView textView = (TextView) cardView.findViewById(R.id.textView);
        ImageView imageView = (ImageView) cardView.findViewById(R.id.imageView);
        IncomingCard item = values.get(position);
        textView.setText(item.Title);
        if (item.Bitmap != null) {
            imageView.setImageBitmap(item.Bitmap);
        }
        return cardView;
    }

}
