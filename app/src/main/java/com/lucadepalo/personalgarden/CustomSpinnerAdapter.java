package com.lucadepalo.personalgarden;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Questa classe estende ArrayAdapter per fornire una vista personalizzata per uno Spinner.
 * Ogni elemento dello Spinner ha un'immagine e un testo associato.
 * In questo modo l'utente può selezionare le piante dall'elenco vedendone il nome e la foto corrispondente.
 */
public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    // Contesto in cui l'adapter viene utilizzato
    private Context context;

    // Lista degli elementi da visualizzare nello Spinner
    private List<String> items;

    /**
     * Costruttore della classe CustomSpinnerAdapter.
     *
     * @param context Contesto in cui l'adapter viene utilizzato.
     * @param items Lista degli elementi da visualizzare nello Spinner.
     */
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

    /**
     * Inizializza e restituisce la vista per un elemento specifico dello Spinner.
     *
     * @param position Posizione dell'elemento all'interno della lista.
     * @param convertView Vista riciclata da utilizzare, se disponibile.
     * @param parent Il genitore a cui verrà eventualmente allegata la vista.
     * @return Una vista completamente inizializzata.
     */
    private View initView(int position, View convertView, ViewGroup parent) {
        // Se non esiste una vista riciclata, ne crea una nuova
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_row, parent, false);
        }

        // Ottiene riferimenti agli elementi interni della vista
        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView textView = convertView.findViewById(R.id.textView);

        // Ottiene l'elemento corrente dalla lista
        String currentItem = getItem(position);

        // Se l'elemento corrente esiste, inizializza la vista con le sue informazioni
        if (currentItem != null) {
            textView.setText(currentItem);

            // Converte il nome dell'elemento in un nome di file immagine (es. "Mio Elemento" diventa "mio_elemento")
            String imageName = currentItem.toLowerCase().replace(" ", "_");

            // Ottiene l'ID della risorsa dell'immagine dal nome del file
            int imageResId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

            // Imposta l'immagine sulla vista
            imageView.setImageResource(imageResId);
        }

        return convertView;
    }
}
