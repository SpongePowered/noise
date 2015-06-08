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
 * Represents a selector module that uses the value of its first source in a specified range and
 * uses the value of its second source outside of that range, along with interpolation at the
 * edges. The control's value is used to determine which source to use.
 *
 * <p>The range is determined based on {@link #lowerBound} and {@link #upperBound}. The edge
 * falloff (in rough terms, how smooth the range border is) is determined based on
 * {@link #edgeFalloff}.</p>
 */
public class Select extends Selector {

    /**
     * The default edge falloff.
     */
    public static final double DEFAULT_EDGE_FALLOFF = 0.0;

    /**
     * Default lower bound of the selection range.
     */
    public static final double DEFAULT_LOWER_BOUND = -1.0;

    /**
     * Default upper bound of the selection range.
     */
    public static final double DEFAULT_UPPER_BOUND = 1.0;

    /**
     * The edge falloff value.
     */
    private final double edgeFalloff;

    /**
     * The lower bound of the selection range.
     */
    private final double lowerBound;

    /**
     * The upper bound of the selection range.
     */
    private final double upperBound;

    /**
     * Constructs a new instance from a control and two source modules, an edge falloff, and the
     * bounds for the range.
     *
     * @param control The control module
     * @param sourceA The first source module
     * @param sourceB The second source module
     * @param edgeFalloff The edge falloff
     * @param lowerBound The lower bound
     * @param upperBound The upper bound
     */
    public Select(Module control, Module sourceA, Module sourceB, double edgeFalloff, double lowerBound, double upperBound) {
        super(control, sourceA, sourceB);

        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("lower must be less than upper");
        }

        // Make sure that the edge falloff curves do not overlap.
        double boundSize = upperBound - lowerBound;
        this.edgeFalloff = (edgeFalloff > boundSize / 2) ? boundSize / 2 : edgeFalloff;

        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    /**
     * Gets the edge falloff.
     *
     * @return The edge falloff
     */
    public double getEdgeFalloff() {
        return edgeFalloff;
    }

    /**
     * Gets the lower bound of the range.
     *
     * @return The lower bound
     */
    public double getLowerBound() {
        return lowerBound;
    }

    /**
     * Gets the upper bound of the range.
     *
     * @return The upper bound
     */
    public double getUpperBound() {
        return upperBound;
    }

    @Override
    public double getValue(double x, double y, double z) {

        double controlValue = control.getValue(x, y, z);
        double alpha;
        if (edgeFalloff > 0.0) {
            if (controlValue < (lowerBound - edgeFalloff)) {
                // The output value from the control module is below the selector
                // threshold; return the output value from the first source module.
                return sourceA.getValue(x, y, z);
            } else if (controlValue < (lowerBound + edgeFalloff)) {
                // The output value from the control module is near the lower end of the
                // selector threshold and within the smooth curve. Interpolate between
                // the output values from the first and second source modules.
                double lowerCurve = (lowerBound - edgeFalloff);
                double upperCurve = (lowerBound + edgeFalloff);
                alpha = Utils.sCurve3((controlValue - lowerCurve) / (upperCurve - lowerCurve));
                return Utils.linearInterp(sourceA.getValue(x, y, z), sourceB.getValue(x, y, z), alpha);
            } else if (controlValue < (upperBound - edgeFalloff)) {
                // The output value from the control module is within the selector
                // threshold; return the output value from the second source module.
                return sourceB.getValue(x, y, z);
            } else if (controlValue < (upperBound + edgeFalloff)) {
                // The output value from the control module is near the upper end of the
                // selector threshold and within the smooth curve. Interpolate between
                // the output values from the first and second source modules.
                double lowerCurve = (upperBound - edgeFalloff);
                double upperCurve = (upperBound + edgeFalloff);
                alpha = Utils.sCurve3((controlValue - lowerCurve) / (upperCurve - lowerCurve));
                return Utils.linearInterp(sourceB.getValue(x, y, z), sourceA.getValue(x, y, z), alpha);
            } else {
                // Output value from the control module is above the selector threshold;
                // return the output value from the first source module.
                return sourceA.getValue(x, y, z);
            }
        } else {
            if (controlValue < lowerBound || controlValue > upperBound) {
                return sourceA.getValue(x, y, z);
            } else {
                return sourceB.getValue(x, y, z);
            }
        }
    }

    /**
     * Returns a new builder instance.
     *
     * @return A new builder instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Represents a builder of {@link Select} instances.
     */
    public static class Builder extends Selector.Builder {

        /**
         * Stores the edge falloff, set to {@link #DEFAULT_EDGE_FALLOFF} by default.
         */
        private double edgeFalloff = DEFAULT_EDGE_FALLOFF;

        /**
         * Stores the lower bound, set to {@link #DEFAULT_LOWER_BOUND} by default.
         */
        private double lowerBound = DEFAULT_UPPER_BOUND;

        /**
         * Stores the upper bound, set to {@link #DEFAULT_LOWER_BOUND} by default.
         */
        private double upperBound = DEFAULT_LOWER_BOUND;

        /**
         * Gets the edge falloff.
         *
         * @return The edge falloff
         */
        public double getEdgeFalloff() {
            return edgeFalloff;
        }

        /**
         * Gets the lower bound.
         *
         * @return The lower bound
         */
        public double getLowerBound() {
            return lowerBound;
        }

        /**
         * Gets the upper bound.
         *
         * @return The upper bound
         */
        public double getUpperBound() {
            return upperBound;
        }

        /**
         * Sets the edge falloff.
         *
         * @param edgeFalloff The new edge falloff
         * @return This builder
         */
        public Builder setEdgeFalloff(double edgeFalloff) {
            this.edgeFalloff = edgeFalloff;
            return this;
        }

        /**
         * Sets the lower bound.
         *
         * @param lowerBound The new lower bound
         * @return This builder
         */
        public Builder setLowerBound(double lowerBound) {
            this.lowerBound = lowerBound;
            return this;
        }

        /**
         * Sets the upper bound.
         *
         * @param upperBound The new upper bound
         * @return This builder
         */
        public Builder setUpperBound(double upperBound) {
            this.upperBound = upperBound;
            return this;
        }

        /**
         * Sets the bounds of the selector range.
         *
         * @param lowerBound The new lower bound
         * @param upperBound The new upper bound
         * @return This builder
         */
        public Builder setBounds(double lowerBound, double upperBound) {
            setLowerBound(lowerBound);
            setUpperBound(upperBound);
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
        public Builder setControl(Module control) {
            this.control = control;
            return this;
        }

        @Override
        protected void checkValues() throws IllegalStateException {
            super.checkValues();
            if (lowerBound > upperBound) {
                throw new IllegalStateException("Lower bound must be less than upper bound");
            }
        }

        @Override
        public Select build() throws IllegalStateException {
            checkValues();
            return new Select(control, sourceA, sourceB, edgeFalloff, lowerBound, upperBound);
        }

    }

}
