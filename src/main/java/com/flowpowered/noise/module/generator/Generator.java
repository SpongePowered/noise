package com.flowpowered.noise.module.generator;

import com.flowpowered.noise.module.Module;

/**
 * Represents a module that does not take other modules as input; a module that only "generates"
 * values.
 */
public abstract class Generator extends Module {

    /**
     * Represents a builder of {@link Generator} instances.
     */
    public static abstract class Builder extends Module.Builder {

        @Override
        public abstract Generator build() throws IllegalStateException;

    }

}
