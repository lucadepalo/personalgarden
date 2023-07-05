package com.lucadepalo.personalgarden;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> items;

    public CustomSpinnerAdapter(Context context, List<String> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }


    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_row, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView textView = convertView.findViewById(R.id.textView);

        String currentItem = getItem(position);

        if (currentItem != null) {
            textView.setText(currentItem);
            // Rendi il nome del file immagine in minuscolo e sostituisci gli spazi con _
            String imageName = currentItem.toLowerCase().replace(" ", "_");
            // Ottieni l'ID della risorsa dell'immagine dal nome del file
            int imageResId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
            // Imposta l'immagine
            imageView.setImageResource(imageResId);
        }

        return convertView;
    }
}
