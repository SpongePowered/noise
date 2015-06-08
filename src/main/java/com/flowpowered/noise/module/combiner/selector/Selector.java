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
package com.flowpowered.noise.module.combiner.selector;

import com.flowpowered.noise.module.Module;
import com.flowpowered.noise.module.combiner.Combiner;

/**
 * Represents a combiner module that uses third module, a control module ({@link #control}) t
 * determine what to do with the output of two source modules.
 */
public abstract class Selector extends Combiner {

    /**
     * The control module.
     */
    protected final Module control;

    /**
     * Constructs a new instance from a control and two sources.
     *
     * @param control The control module
     * @param sourceA The first source module
     * @param sourceB The second source module
     */
    public Selector(Module control, Module sourceA, Module sourceB) {
        super(sourceA, sourceB);
        this.control = control;
    }

    /**
     * Gets the control module.
     *
     * @return The control module
     */
    public Module getControl() {
        return control;
    }

    /**
     * Represents a builder of {@link Selector} instances.
     */
    public static abstract class Builder extends Combiner.Builder {

        /**
         * Stores the control module.
         */
        protected Module control;

        /**
         * Gets the control module.
         *
         * @return The control module
         */
        public Module getControl() {
            return control;
        }

        /**
         * Sets the control module.
         *
         * @param control The new control module
         * @return This builder
         */
        public Builder setControl(Module control) {
            this.control = control;
            return this;
        }

        @Override
        public Builder setSourceA(Module sourceA) {
            super.setSourceA(sourceA);
            return this;
        }

        @Override
        public Builder setSourceB(Module sourceA) {
            super.setSourceA(sourceA);
            return this;
        }

        @Override
        public Builder setSources(Module sourceA, Module sourceB) {
            super.setSources(sourceA, sourceB);
            return this;
        }

        @Override
        protected void checkValues() throws IllegalStateException {
            super.checkValues();
            if (control == null) {
                throw new IllegalStateException("control cannot be null");
            }
        }

        @Override
        public abstract Selector build() throws IllegalStateException;

    }

}
