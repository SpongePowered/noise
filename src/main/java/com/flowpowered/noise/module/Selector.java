package com.flowpowered.noise.module;

public abstract class Selector extends Combiner {

    protected final Module control;

    public Selector(Module control, Module sourceA, Module sourceB) {
        super(sourceA, sourceB);
        this.control = control;
    }

    public Module getControl() {
        return control;
    }

}
