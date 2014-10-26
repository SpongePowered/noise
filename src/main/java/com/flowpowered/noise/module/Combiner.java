package com.flowpowered.noise.module;

import com.flowpowered.noise.module.Module;

public abstract class Combiner extends Module {

    protected final Module sourceA;
    protected final Module sourceB;

    public Combiner(Module sourceA, Module sourceB) {
        this.sourceA = sourceA;
        this.sourceB = sourceB;
    }

    public Module getSourceA() {
        return sourceA;
    }

    public Module getSourceB() {
        return sourceB;
    }

}
