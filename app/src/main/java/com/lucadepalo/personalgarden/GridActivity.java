package com.lucadepalo.personalgarden;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class GridActivity extends AppCompatActivity {
    private ImageView plantIcon, trashIcon;
    private GridLayout gridLayout;
    private FrameLayout[] cells = new FrameLayout[8];
    private int plantNumber = 0;
    private PlantPot[] pots = new PlantPot[8];

    private ToggleButton configToggle, cloudToggle, waterToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        plantIcon = findViewById(R.id.plant_pot_icon);
        plantIcon.setTag(0);
        trashIcon = findViewById(R.id.trash_icon);
        gridLayout = findViewById(R.id.grid_layout);
        //button = findViewById((R.id.button_set));
        //button.setOnClickListener(this);
        configToggle = findViewById(R.id.configToggle);
        configToggle.setOnCheckedChangeListener(new ToggleButtonListener());
        cloudToggle = findViewById(R.id.cloudToggle);
        cloudToggle.setOnCheckedChangeListener(new ToggleButtonListener());
        waterToggle = findViewById(R.id.waterToggle);
        waterToggle.setOnCheckedChangeListener(new ToggleButtonListener());
        for (int i = 0; i < cells.length; i++) {
            int resourceId = getResources().getIdentifier("cell" + (i + 1), "id", getPackageName());
            cells[i] = findViewById(resourceId);
            cells[i].setTag(i + 1);
        }
        for (int i = 0; i < pots.length; i++) {
            pots[i] = new PlantPot(GridActivity.this);
            pots[i].setPotID(i + 1);
        }
        plantIcon.setOnTouchListener(new MyTouchListener());
        //gridLayout.setOnDragListener(new MyDragListener());
        trashIcon.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        v.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                    case DragEvent.ACTION_DRAG_ENDED:
                        v.setBackground(getDrawable(R.drawable.border));
                        break;
                    case DragEvent.ACTION_DROP:
                        View view = (View) event.getLocalState();
                        if (view != plantIcon && view.getParent() instanceof ViewGroup) {
                            ViewGroup parent = (ViewGroup) view.getParent();
                            parent.removeView(view);
                            SharedPrefManager.irrigationLine.deletePotInPlace((Integer) parent.getTag());
                            Toast.makeText(getBaseContext(), "Eliminato", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        for (FrameLayout cell : cells) {
            cell.setOnDragListener(new MyDragListener());
        }


        if (!SharedPrefManager.getInstance(this).haveTipsBeenShown()) {
            showFirstTip();
        }
    }


    private static final class MyTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDragAndDrop(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }
    private class MyDragListener implements View.OnDragListener {
        boolean droppedOnValidCell = false;
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    droppedOnValidCell = false;
                    v.setBackgroundResource(R.drawable.border);
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundResource(R.drawable.border_active);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundResource(R.drawable.border);
                    break;
                case DragEvent.ACTION_DROP:
                    droppedOnValidCell = true;
                    v.setBackgroundResource(R.drawable.border);
                    View draggedView = (View) event.getLocalState();
                    FrameLayout targetView = (FrameLayout) v;
                    Drawable drawable = ((ImageView) draggedView).getDrawable();
                    if (drawable != null ) {
                        pots[plantNumber].setPotID(plantNumber);
                        pots[plantNumber].setNumPlace((Integer) targetView.getTag());
                        targetView.addView(pots[plantNumber]);
                        pots[plantNumber].setOnTouchListener(new MyTouchListener());
                        SharedPrefManager.irrigationLine.addPot((Integer) targetView.getTag(), pots[plantNumber]);
                        if (pots[plantNumber].getPotID() == 0) {
                            startActivity( new Intent(GridActivity.this, SetCropActivity.class));
                        } else {
                            startActivity(new Intent(GridActivity.this, SynergyActivity.class).putExtra("potID", plantNumber));
                        }
                        plantNumber++;
                    }
                    if (draggedView != plantIcon) {
                        ViewGroup oldParent = (ViewGroup) draggedView.getParent();
                        oldParent.removeView(draggedView);
                        SharedPrefManager.irrigationLine.deletePotInPlace((Integer) oldParent.getTag());
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundResource(R.drawable.border);
                    View view = (View) event.getLocalState();
                    if (!droppedOnValidCell && view != plantIcon) {
                        ViewGroup parent = (ViewGroup) view.getParent();
                        if (parent != null) {
                            parent.removeView(view);
                        }
                    } else {
                        view.setVisibility(View.VISIBLE);
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    }
    private void irriga(){
        final String FK_CONTENITORE = ((Integer)SharedPrefManager.container.getNumContainer()).toString();
        final String FK_LINEA = ((Integer) SharedPrefManager.irrigationLine.getNumLine()).toString();
        class RelazioneIrriga extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("fk_contenitore", FK_CONTENITORE);
                params.put("fk_linea", FK_LINEA);
                return requestHandler.sendPostRequest(URLs.URL_IRRIGA, params);
            }
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                SharedPrefManager.container.addIrrigationLine(SharedPrefManager.irrigationLine);
                SharedPrefManager.getInstance(getApplicationContext()).setLineInContainer(SharedPrefManager.container, SharedPrefManager.irrigationLine);
            }
        }
        RelazioneIrriga ri = new RelazioneIrriga();
        ri.execute();
    }

    private void dispone(PlantPot plantPot){
        final String FK_LINEA = ((Integer) SharedPrefManager.irrigationLine.getNumLine()).toString();
        final String FK_POSTO = ((Integer)plantPot.getNumPlace()).toString();
        class RelazioneDispone extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("fk_linea", FK_LINEA);
                params.put("fk_posto", FK_POSTO);
                return requestHandler.sendPostRequest(URLs.URL_DISPONE, params);
            }
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                SharedPrefManager.getInstance(getApplicationContext()).setPotInLine(SharedPrefManager.irrigationLine, plantPot);
            }
        }
        RelazioneDispone rd = new RelazioneDispone();
        rd.execute();
    }

    private class ToggleButtonListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                switch (buttonView.getId()) {
                    case R.id.configToggle:
                        for(int i = 0; i<pots.length; i++) {
                            dispone(pots[i]);
                            plantIcon.setVisibility(View.INVISIBLE);
                            trashIcon.setVisibility(View.INVISIBLE);
                        }
                        irriga();
                        break;
                    case R.id.cloudToggle:

                        break;
                    case R.id.waterToggle:

                        break;
                }
            } else {
                switch (buttonView.getId()) {
                    case R.id.configToggle:
                        plantIcon.setVisibility(View.VISIBLE);
                        trashIcon.setVisibility(View.VISIBLE);
                        break;
                    case R.id.cloudToggle:

                        break;
                    case R.id.waterToggle:

                        break;
                }
            }
        }
    }

    private void showFirstTip() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // mezzo secondo tra ogni showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this);

        sequence.setConfig(config);

        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(findViewById(R.id.configToggle))
                .setDismissText("AVANTI")
                .setContentText("CONFIGURAZIONE DELL'ORTO: Per aggiungere un vaso all'orto, trascina l'icona del vaso nella posizione che preferisci. Per eliminarlo, trascinalo nel cestino. Per scambiare due vasi, trascinali uno sull'altro. Quando sei soddisfatto dell'orto premi la spunta di conferma. Per tornare alla configurazione premi il tasto impostazioni.")
                .build()
        );

        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(findViewById(R.id.cloudToggle))
                .setDismissText("AVANTI")
                .setContentText("IRRIGAZIONE SMART: Questo tasto mostra lo stato del servizio di irrigazione smart. Tocca play per lasciare che il tuo orto venga automaticamente irrigato solo quando c'è bisogno. Tocca pausa per fermare il servizio smart e passare alla modalità manuale.")
                .build()
        );

        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(findViewById(R.id.waterToggle))
                .setDismissText("CAPITO!")
                .setContentText("IRRIGAZIONE MANUALE: Questo tasto mostra lo stato della valvola di irrigazione. Tocca la goccia per aprire manualmente la valvola, tocca la goccia barrata per chiuderla. Ricorda che quando usi questo interruttore passi alla modalità di irrigazione manuale.")
                .build()
        );

        sequence.start();

        // Segna come mostrati
        SharedPrefManager.getInstance(GridActivity.this).markTipsAsShown();
    }


}

