/*
 * This file is part of Noise, licensed under the MIT License (MIT).
 *
 * Copyright (c) Flow Powered <https://github.com/flow>
 * Copyright (c) SpongePowered <https://github.com/SpongePowered>
 * Copyright (c) contributors
 *
 * Original libnoise C++ library by Jason Bevins <http://libnoise.sourceforge.net>
 * jlibnoise Java port by Garrett Fleenor <https://github.com/RoyAwesome/jlibnoise>
 * Noise is re-licensed with permission from jlibnoise author.
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
package org.spongepowered.noise.module.modifier;

import org.spongepowered.noise.exception.NoModuleException;
import org.spongepowered.noise.module.Module;

/**
 * Noise module that clamps the output value from a source module to a range
 * of values.
 *
 * <p>The range of values in which to clamp the output value is called the
 * <em>clamping range</em>.</p>
 *
 * <p>If the output value from the source module is less than the lower bound of
 * the clamping range, this noise module clamps that value to the lower bound.
 * If the output value from the source module is greater than the upper bound
 * of the clamping range, this noise module clamps that value to the
 * upper bound.</p>
 *
 * <p>To specify the upper and lower bounds of the clamping range, call the
 * {@link #setLowerBound(double)} and {@link #setUpperBound(double)} methods.</p>
 *
 * @sourceModules 1
 */
public class Clamp extends Module {

    /**
     * Default lower bound of the clamping range for the {@link Clamp}
     * noise module.
     */
    public static final double DEFAULT_LOWER_BOUND = 0.0;

    /**
     * Default upper bound of the clamping range of the {@link Clamp}
     * noise module.
     */
    public static final double DEFAULT_UPPER_BOUND = 1.0;

    private double lowerBound = Clamp.DEFAULT_LOWER_BOUND;
    private double upperBound = Clamp.DEFAULT_UPPER_BOUND;

    public Clamp() {
        super(1);
    }

    /**
     * Get the lower bound of the clamping range.
     *
     * <p>If the output value from the source module is less than the lower
     * bound of the clamping range, this noise module clamps that value to the
     * lower bound.</p>
     *
     * @return the lower bound
     */
    public double getLowerBound() {
        return this.lowerBound;
    }

    /**
     * Set the lower bound of the clamping range.
     *
     * <p>The lower bound must be less than or equal to the upper bound.</p>
     *
     * @param lowerBound the lower bound
     */
    public void setLowerBound(final double lowerBound) {
        this.lowerBound = lowerBound;
    }

    /**
     * Get the upper bound of the clamping range.
     *
     * <p>If the output value from the source module is greater than the upper
     * bound of the clamping range, this noise module clamps that value to the
     * upper bound.</p>
     *
     * @return the upper bound
     */
    public double getUpperBound() {
        return this.upperBound;
    }

    /**
     * Set the upper bound of the clamping range.
     *
     * <p>The upper bound must be greater than or equal to the lower bound.</p>
     *
     * @param upperBound the upper bound
     */
    public void setUpperBound(final double upperBound) {
        this.upperBound = upperBound;
    }

    @Override
    public double getValue(final double x, final double y, final double z) {
        if (this.sourceModule[0] == null) {
            throw new NoModuleException(0);
        }
        assert this.lowerBound <= this.upperBound;

        final double value = this.sourceModule[0].getValue(x, y, z);
        if (value < this.lowerBound) {
            return this.lowerBound;
        } else if (value > this.upperBound) {
            return this.upperBound;
        } else {
            return value;
        }
    }
}
