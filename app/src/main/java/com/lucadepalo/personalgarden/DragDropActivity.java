package com.lucadepalo.personalgarden;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class DragDropActivity extends AppCompatActivity {

    private FrameLayout objectsContainer;

    private int numPotsAdded = 0;
    private float dX, dY; // variables to store the initial touch coordinates

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_drop);

        GridView irrigationLine = findViewById(R.id.irrigation_line);
        // Find the ImageView for the plant pot icon
        ImageView plantPot = findViewById(R.id.plant_pot_icon);

        objectsContainer = findViewById(R.id.objects_container);


        // Set the OnTouchListener to the ImageView so it can be dragged and dropped
        plantPot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // User started dragging the icon
                        dX = v.getX() - event.getRawX();
                        dY = v.getY() - event.getRawY();
                        ClipData dragData = ClipData.newPlainText("", "");
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                        v.startDrag(dragData, shadowBuilder, v, 0);
                        // Nasconde l'icona originale
                        v.setVisibility(View.INVISIBLE);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // User is moving the icon
                        v.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        break;
                    case MotionEvent.ACTION_UP:
                        // User released the icon
                        if (numPotsAdded < 8) {
                            // Create a new instance of the plant pot icon
                            ImageView newPlantPot = new ImageView(DragDropActivity.this);
                            newPlantPot.setImageResource(R.drawable.ic_pot);
                            newPlantPot.setLayoutParams(new LinearLayout.LayoutParams(
                                    64,64
                            ));

                            // Add the new plant pot to the irrigation line
                            if (isViewOverlapping(newPlantPot, irrigationLine)) {
                                objectsContainer.removeView(v);
                                irrigationLine.addView(newPlantPot);
                            } else {
                                objectsContainer.addView(v);
                            }

                            // Increment the number of pots added
                            numPotsAdded++;
                            newPlantPot.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    // User wants to delete the pot
                                    irrigationLine.removeView(newPlantPot);
                                    numPotsAdded--;
                                    return true;
                                }
                            });
                        } else {
                            objectsContainer.addView(v);
                        }
                        v.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        // User dropped the icon outside of the intended drop zone, return it to original position
                        v.animate()
                                .x(dX)
                                .y(dY)
                                .setDuration(200)
                                .start();
                        v.setVisibility(View.VISIBLE);
                        break;
                }
                return true;
            }

            private boolean isViewOverlapping(View firstView, View secondView) {
                RectF firstRect = new RectF(firstView.getLeft(), firstView.getTop(), firstView.getRight(), firstView.getBottom());
                RectF secondRect = new RectF(secondView.getLeft(), secondView.getTop(), secondView.getRight(), secondView.getBottom());
                return RectF.intersects(firstRect, secondRect);
            }
        });

        irrigationLine.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DROP:
                        break;
                    case DragEvent.ACTION_DRAG_STARTED:
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        break;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        break;
                }
                return true;
            }
        });
    }
}
