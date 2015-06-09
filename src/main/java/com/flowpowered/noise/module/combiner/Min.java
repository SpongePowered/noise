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
 * Represents a combiner module that takes the minimum of two source modules.
 */
public class Min extends Combiner {

    /**
     * Constructs a new instance out of two sources.
     *
     * @param sourceA The first source
     * @param sourceB The second source
     */
    public Min(Module sourceA, Module sourceB) {
        super(sourceA, sourceB);
    }

    @Override
    public double getValue(double x, double y, double z) {
        return Math.min(sourceA.getValue(x, y, z), sourceB.getValue(x, y, z));
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
     * Represents a builder of {@link Min} instances.
     */
    public static class Builder extends Combiner.Builder {

        @Override
        public Builder setSourceA(Module sourceA) {
            this.sourceA = sourceA;
            return this;
        }

        @Override
        public Builder setSourceB(Module sourceB) {
            this.sourceB = sourceB;
            return this;
        }

        @Override
        public Builder setSources(Module sourceA, Module sourceB) {
            setSourceA(sourceA);
            setSourceB(sourceB);
            return this;
        }

        @Override
        public Min build() throws IllegalStateException {
            checkValues();
            return new Min(sourceA, sourceB);
        }

    }

}
