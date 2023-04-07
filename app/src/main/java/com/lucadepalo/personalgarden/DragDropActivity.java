package com.lucadepalo.personalgarden;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class DragDropActivity extends AppCompatActivity {

    private int numPotsAdded = 0;
    private float dX, dY; // variables to store the initial touch coordinates

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_drop);

        // Find the LinearLayout container for the irrigation line
        LinearLayout irrigationLine = findViewById(R.id.irrigation_line);

        // Find the ImageView for the plant pot icon
        ImageView plantPot = findViewById(R.id.plant_pot_icon);

        // Set the OnTouchListener to the ImageView so it can be dragged and dropped
        plantPot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // User started dragging the icon
                        dX = v.getX() - event.getRawX();
                        dY = v.getY() - event.getRawY();
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
                            irrigationLine.addView(newPlantPot);

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
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        // User dropped the icon outside of the intended drop zone, return it to original position
                        v.animate()
                                .x(dX)
                                .y(dY)
                                .setDuration(200)
                                .start();
                        break;
                }
                return true;
            }
        });
    }
}
