package se.fredrikcarlbom.netrunnerdbapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CardAdapter extends ArrayAdapter<IncomingCard> {

    private List<IncomingCard> values;
    private LayoutInflater layoutInflater;

    public CardAdapter(Context context, List<IncomingCard> values) {
        super(context, R.layout.card, values);

        this.values = values;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cardView = layoutInflater.inflate(R.layout.card, parent, false);
        TextView textView = (TextView) cardView.findViewById(R.id.textView);
        textView.setText(values.get(position).Title);
        return cardView;
    }

}
