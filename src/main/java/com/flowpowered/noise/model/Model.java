package com.flowpowered.noise.model;

import com.flowpowered.noise.module.Module;

public class Model {

    protected final Module module;

    public Model(Module module) {
        if (module == null) {
            throw new IllegalArgumentException("Module for a model cannot be null");
        }
        this.module = module;
    }

    public Module getModule() {
        return module;
    }

}
