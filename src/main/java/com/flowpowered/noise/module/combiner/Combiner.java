package com.flowpowered.noise.module.combiner;

import com.flowpowered.noise.module.Module;

/**
 * A Combiner is a Module that takes two modules and combines them in some way.
 * The two modules should come in no particular order.
 * It provides a constructor and protected members to its subclasses.
 */
public abstract class Combiner extends Module {

    /**
     * The first source.
     */
    protected final Module sourceA;

    /**
     * The second source.
     */
    protected final Module sourceB;

    /**
     * Construct a Combiner out of two sources.
     *
     * @param sourceA the first source
     * @param sourceB the second source
     */
    public Combiner(Module sourceA, Module sourceB) {
        this.sourceA = sourceA;
        this.sourceB = sourceB;
    }

    /**
     * Get the first source.
     *
     * @return source A
     */
    public Module getSourceA() {
        return sourceA;
    }

    /**
     * Get the second source.
     *
     * @return source B
     */
    public Module getSourceB() {
        return sourceB;
    }

}
