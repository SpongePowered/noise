/*
 * This file is part of Flow Noise, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2013 Flow Powered <https://flowpowered.com/>
 * Original libnoise C++ library by Jason Bevins <http://libnoise.sourceforge.net/>
 * jlibnoise Java port by Garrett Fleenor <https://github.com/RoyAwesome/jlibnoise>
 * Flow Noise is re-licensed with permission from jlibnoise author.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.flowpowered.noise.module.combiner;

import com.flowpowered.noise.module.Module;

/**
 * Represents a module that takes two source modules and combines them in some way.
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
     * Constructs a new instance out of two sources.
     *
     * @param sourceA The first source
     * @param sourceB The second source
     */
    public Combiner(Module sourceA, Module sourceB) {
        this.sourceA = sourceA;
        this.sourceB = sourceB;
    }

    /**
     * Gets the first source.
     *
     * @return source A
     */
    public Module getSourceA() {
        return sourceA;
    }

    /**
     * Gets the second source.
     *
     * @return source B
     */
    public Module getSourceB() {
        return sourceB;
    }

    /**
     * Represents a builder of {@link Combiner} instances.
     */
    public static abstract class Builder extends Module.Builder {

        /**
         * Stores the first source module.
         */
        protected Module sourceA;

        /**
         * Stores the second source module.
         */
        protected Module sourceB;

        /**
         * Gets the first source module.
         *
         * @return The first source module
         */
        public Module getSourceA() {
            return sourceA;
        }

        /**
         * Sets the second source module.
         *
         * @param sourceA The new second source module
         * @return This builder
         */
        public Builder setSourceA(Module sourceA) {
            this.sourceA = sourceA;
            return this;
        }

        /**
         * Gets the second source module.
         *
         * @return The second source module
         */
        public Module getSourceB() {
            return sourceB;
        }

        /**
         * Sets the second source module
         *
         * @param sourceB The new second source module
         * @return This builder
         */
        public Builder setSourceB(Module sourceB) {
            this.sourceB = sourceB;
            return this;
        }

        /**
         * Sets both source modules.
         *
         * @param sourceA The new first source module
         * @param sourceB The new second source module
         * @return This builder
         */
        public Builder setSources(Module sourceA, Module sourceB) {
            setSourceA(sourceA);
            setSourceB(sourceB);
            return this;
        }

        @Override
        protected void checkValues() throws IllegalStateException {
            if (sourceA == null) {
                throw new IllegalStateException("First source cannot be null");
            }
            if (sourceB == null) {
                throw new IllegalStateException("Second source cannot be null");
            }
        }

        @Override
        public abstract Combiner build() throws IllegalStateException;

    }

}
