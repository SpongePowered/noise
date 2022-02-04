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
import org.spongepowered.noise.module.NoiseModule;

/**
 * A modifier module to map a value from one range to another.
 *
 * <p>For example: 0.5 (-1 &lt; x &lt; 1) -&gt; 0.75 (0 &lt; x &lt; 1)</p>
 *
 * @sourceModules 1
 */
public class Range extends NoiseModule {
    
    public static final double DEFAULT_CURRENT_LOWER_BOUND = -1f;
    public static final double DEFAULT_CURRENT_UPPER_BOUND = 1f;
    public static final double DEFAULT_NEW_LOWER_BOUND = 0f;
    public static final double DEFAULT_NEW_UPPER_BOUND = 1f;
    
    /* Current lower bound */
    private double currentLowerBound = Range.DEFAULT_CURRENT_LOWER_BOUND;
    /* Current upper bound */
    private double currentUpperBound = Range.DEFAULT_CURRENT_UPPER_BOUND;
    /* New lower bound */
    private double newLowerBound = Range.DEFAULT_NEW_LOWER_BOUND;
    /* New Upper Bound */
    private double newUpperBound = Range.DEFAULT_NEW_UPPER_BOUND;
    /* Cache variables */
    private double scale;
    private double bias;

    public Range() {
        super(1);
    }

    /**
     * Create a new Range module with the source modules pre-configured.
     *
     * @param source the input module
     */
    public Range(final NoiseModule source) {
        this();
        this.setSourceModule(0, source);
    }

    public double getCurrentLowerBound() {
        return this.currentLowerBound;
    }

    public double getCurrentUpperBound() {
        return this.currentUpperBound;
    }

    public double getNewLowerBound() {
        return this.newLowerBound;
    }
    
    public double getNewUpperBound() {
        return this.newUpperBound;
    }
    
    /*
     * Calculate the scale and biased to be a applied during Range#getValue(int x, int y, int z)
     * Should be called when the bounds are modified
     */
    private void recalculateScaleBias() {
        this.scale = (this.getNewUpperBound() - this.getNewLowerBound()) /
            (this.getCurrentUpperBound() - this.getCurrentLowerBound());
        this.bias = this.getNewLowerBound() - this.getCurrentLowerBound() * this.scale;
    }
    
    /**
     * Configure bounds for range module
     * @param currentLower current lower bound
     * @param currentUpper current upper bound
     * @param newLower new lower bound
     * @param newUpper new upper bound
     */
    public void setBounds(
        final double currentLower, final double currentUpper, final double newLower,
            final double newUpper) {
        if (currentLower == currentUpper) {
            throw new IllegalArgumentException("currentLower must not equal currentUpper. Both are " + currentUpper);
        }
        if (newLower == newUpper) {
            throw new IllegalArgumentException("newLowerBound must not equal newUpperBound. Both are " + newUpper);
        }
        this.currentLowerBound = currentLower;
        this.currentUpperBound = currentUpper;
        this.newLowerBound = newLower;
        this.newUpperBound = newUpper;
        this.recalculateScaleBias();
    }

    @Override
    public double getValue(final double x, final double y, final double z) {
        if (this.sourceModule[0] == null) {
            throw new NoModuleException(0);
        }
        
        final double oldVal = this.sourceModule[0].getValue(x, y, z);
        return oldVal * this.scale + this.bias;
    }

}
