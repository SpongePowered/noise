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

import com.flowpowered.noise.Utils;
import com.flowpowered.noise.module.Module;

/**
 * Represents a selector module that does a linear interpolation between two source modules based
 * on the value of a control module.
 *
 * <p>The value of the control module is expected to range from -1.0 to 1.0.</p>
 */
public class Blend extends Selector {

    /**
     * Constructs a new instance given a control and two sources.
     *
     * @param control The control module
     * @param sourceA The first source module
     * @param sourceB The second source module
     */
    public Blend(Module control, Module sourceA, Module sourceB) {
        super(control, sourceA, sourceB);
    }

    @Override
    public double getValue(double x, double y, double z) {
        double v0 = sourceA.getValue(x, y, z);
        double v1 = sourceB.getValue(x, y, z);
        double alpha = (control.getValue(x, y, z) + 1.0) / 2.0;
        return Utils.linearInterp(v0, v1, alpha);
    }

    /**
     * Returns a new builder instance.
     *
     * @return A new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Represents a builder of {@link Blend} instances.
     */
    public static class Builder extends Selector.Builder {

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
        public Builder setControl(Module control) {
            this.control = control;
            return this;
        }

        @Override
        public Blend build() throws IllegalStateException {
            checkValues();
            return new Blend(control, sourceA, sourceB);
        }

    }

}
