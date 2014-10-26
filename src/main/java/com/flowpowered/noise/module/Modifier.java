package com.flowpowered.noise.module;

public abstract class Modifier extends Module {

    protected final Module source;

    public Modifier(Module source) {
        this.source = source;
    }

    public Module getSource() {
        return source;
    }

}
