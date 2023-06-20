package com.lucadepalo.personalgarden;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;

public class GridActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private ImageView plantIcon;
    private ImageView trashIcon;
    private GridLayout gridLayout;
    private FrameLayout[] cells = new FrameLayout[8];
    private HashMap<Integer, Integer> line = new HashMap<>();

    private int cropID = 0;


    private IrrigationLine irrigationLine = new IrrigationLine(0);
    private int plantNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        plantIcon = findViewById(R.id.plant_pot_icon);
        plantIcon.setTag(0);
        trashIcon = findViewById(R.id.trash_icon);
        gridLayout = findViewById(R.id.grid_layout);


        // Initialize the cells array with FrameLayout views
        cells[0] = findViewById(R.id.cell1);
        cells[0].setTag(1);
        cells[1] = findViewById(R.id.cell2);
        cells[1].setTag(2);
        cells[2] = findViewById(R.id.cell3);
        cells[2].setTag(3);
        cells[3] = findViewById(R.id.cell4);
        cells[3].setTag(4);
        cells[4] = findViewById(R.id.cell5);
        cells[4].setTag(5);
        cells[5] = findViewById(R.id.cell6);
        cells[5].setTag(6);
        cells[6] = findViewById(R.id.cell7);
        cells[6].setTag(7);
        cells[7] = findViewById(R.id.cell8);
        cells[7].setTag(8);


        // Add drag and drop logic to the ImageView
        plantIcon.setOnTouchListener(new MyTouchListener());
        gridLayout.setOnDragListener(new MyDragListener());

        trashIcon.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // Do nothing
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        v.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light)); // Change color to indicate active drop area
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        v.setBackgroundColor(android.graphics.Color.TRANSPARENT); // Change color back to normal when item is not over the trash icon
                        break;

                    case DragEvent.ACTION_DROP:
                        View view = (View) event.getLocalState();
                        // Only remove the view if it is not the original plant icon
                        if (view != plantIcon && view.getParent() instanceof ViewGroup) {
                            // Remove the dragged item from its parent layout
                            ViewGroup parent = (ViewGroup) view.getParent();
                            parent.removeView(view);

                            // Remove the association from the HashMap
                            line.remove(parent.getTag().toString());
                            Toast.makeText(getBaseContext(), "Eliminato", Toast.LENGTH_SHORT).show();

                        }
                        break;

                    case DragEvent.ACTION_DRAG_ENDED:
                        v.setBackgroundColor(android.graphics.Color.TRANSPARENT); // Change color back to normal when drag is completed
                        break;

                    default:
                        break;
                }
                return true;
            }
        });

        // Add drag and drop logic to each cell
        for (FrameLayout cell : cells) {
            cell.setOnDragListener(new MyDragListener());
        }

        // Aggiunge il listener per il click su ogni cella
        for (FrameLayout cell : cells) {
            cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Se la cella è vuota, aggiungi una pianta
                    FrameLayout clickedCell = (FrameLayout) v;
                    if (clickedCell.getChildCount() == 0) {
                        ImageView pot = new ImageView(getApplicationContext());
                        pot.setImageDrawable(getResources().getDrawable(R.drawable.ic_pot));

                        // Set the unique tag for the new pot
                        int newPotTag = ++plantNumber;
                        pot.setTag(newPotTag);

                        clickedCell.addView(pot);

                        // Add the new association to the HashMap
                        line.put((Integer) clickedCell.getTag(), newPotTag);
                    }
                }
            });
        }
    }
    // Listener for touch events on the ImageView
    private static final class MyTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                // Set up the shadow to be dragged
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                // Start the drag and drop operation
                view.startDragAndDrop(data, shadowBuilder, view, 0);
                // Make the ImageView invisible during the drag
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    // Listener for drag and drop events
    private class MyDragListener implements View.OnDragListener {
        boolean droppedOnValidCell = false;

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    droppedOnValidCell = false;
                    break;

                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackground(null);
                    break;

                case DragEvent.ACTION_DROP:
                    droppedOnValidCell = true;
                    View draggedView = (View) event.getLocalState();
                    FrameLayout targetView = (FrameLayout) v;

                    Drawable drawable = ((ImageView) draggedView).getDrawable();
                    if (drawable != null) {
                        ImageView droppedImage = new ImageView(getApplicationContext());
                        droppedImage.setImageDrawable(drawable);
                        droppedImage.setTag(draggedView.getTag());  // Make sure to set the tag for the dropped image
                        targetView.addView(droppedImage);
                        droppedImage.setOnTouchListener(new MyTouchListener());
                        if ((int) droppedImage.getTag() == (0)) {
                            Toast.makeText(getBaseContext(), "FIRST POT LOADED", Toast.LENGTH_SHORT).show();
                            PlantPot pot = new PlantPot();
                            pot.setNumPlace((int) droppedImage.getTag());

                            //Intent intent = new Intent(getApplicationContext(), SetCropActivity.class);
                            //intent.putExtra("com.lucadepalo.personalgarden.numPlace", (int) targetView.getTag());
                            //startActivity(intent);

                            Intent intent = new Intent(GridActivity.this, SetCropActivity.class);
                            startActivityForResult(intent, REQUEST_CODE);

                            pot.setCropID(cropID);

                        } else {
                            Toast.makeText(getBaseContext(), "SETCROPACTIVITY ELSE", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(getApplicationContext(), SetCropActivity.class).putExtra("numPlace",(int)droppedImage.getTag()).putExtra("fk_specie1",fk_specie1));
                        }
                        //PlantPot pot = new PlantPot((int)targetView.getTag(), 0);


                        // Add the new association to the HashMap
                        line.put((Integer) targetView.getTag(), (Integer) droppedImage.getTag());
                    }

                    // Ensure that the draggedView is not the original plantIcon
                    if (draggedView != plantIcon) {
                        ViewGroup oldParent = (ViewGroup) draggedView.getParent();
                        oldParent.removeView(draggedView);

                        // Remove the old association from the HashMap
                        line.remove((Integer) oldParent.getTag());
                    }
                    break;



                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundColor(android.graphics.Color.TRANSPARENT);
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

    private void irriga(int fk_contenitore, int fk_linea){
        final String FK_CONTENITORE = ((Integer)fk_contenitore).toString();
        final String FK_LINEA = ((Integer)fk_linea).toString();

        class RelazioneIrriga extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("fk_contenitore", FK_CONTENITORE);
                params.put("fk_linea", FK_LINEA);

                return requestHandler.sendPostRequest(URLs.URL_QRCODE, params);
            }
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Container container = new Container(0);
                container.addIrrigationLine(irrigationLine);

                SharedPrefManager.getInstance(getApplicationContext()).setLineInContainer(container,irrigationLine);
            }
        }
        RelazioneIrriga ri = new RelazioneIrriga();
        ri.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                // Estrai il valore dalla Intent data
                cropID = data.getIntExtra("cropInt", 0);

            }
        }
    }




}

