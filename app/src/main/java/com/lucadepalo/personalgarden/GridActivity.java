package com.lucadepalo.personalgarden;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;

public class GridActivity extends AppCompatActivity {

    private ImageView plantIcon;
    private GridLayout gridLayout;
    private FrameLayout[] cells = new FrameLayout[8];

    private int plantNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        plantIcon = findViewById(R.id.plant_pot_icon);
        gridLayout = findViewById(R.id.grid_layout);

        // Initialize the cells array with FrameLayout views
        cells[0] = findViewById(R.id.cell1);
        cells[1] = findViewById(R.id.cell2);
        cells[2] = findViewById(R.id.cell3);
        cells[3] = findViewById(R.id.cell4);
        cells[4] = findViewById(R.id.cell5);
        cells[5] = findViewById(R.id.cell6);
        cells[6] = findViewById(R.id.cell7);
        cells[7] = findViewById(R.id.cell8);

        // Add drag and drop logic to the ImageView
        plantIcon.setOnTouchListener(new MyTouchListener());
        gridLayout.setOnDragListener(new MyDragListener());

        // Add drag and drop logic to each cell
        for (FrameLayout cell : cells) {
            cell.setOnDragListener(new MyDragListener());
        }

/*        // Aggiunge il listener per il drag and drop alla cella di origine
        cells[0].setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DROP:
                        // La ImageView è stata rilasciata nella cella di origine, reimposta la visibilità
                        View view = (View) event.getLocalState();
                        view.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
 */

        // Aggiunge il listener per il click su ogni cella
        for (FrameLayout cell : cells) {
            cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Se la cella è vuota, aggiungi una pianta
                    FrameLayout clickedCell = (FrameLayout) v;
                    if (clickedCell.getChildCount() == 0) {
                        ImageView plant = new ImageView(getApplicationContext());
                        plant.setImageDrawable(getResources().getDrawable(R.drawable.cloud_toggle));
                        clickedCell.addView(plant);
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
                        targetView.addView(droppedImage);
                        droppedImage.setOnTouchListener(new MyTouchListener());
                    }

                    if (draggedView != plantIcon) {
                        ViewGroup oldParent = (ViewGroup) draggedView.getParent();
                        oldParent.removeView(draggedView);
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
}

