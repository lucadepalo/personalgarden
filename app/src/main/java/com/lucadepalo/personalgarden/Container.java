package com.lucadepalo.personalgarden;

import java.util.HashMap;

public class Container {

    private static int numContainer;
    private HashMap<Integer, IrrigationLine> lines;

    public Container(int numContainer) {
        this.numContainer = numContainer;
        this.lines = new HashMap<>();
    }

    public static int getNumContainer() {
        return numContainer;
    }

    public void addIrrigationLine(IrrigationLine irrigationLine) {
        this.lines.put(numContainer, irrigationLine);
    }

    public IrrigationLine getIrrigationLine(int numContainer) {
        return this.lines.get(numContainer);
    }

    public HashMap<Integer, IrrigationLine> getAllIrrigationLines() {
        return this.lines;
    }

}
