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
package com.flowpowered.noise.module.modifier;

import com.flowpowered.noise.exception.NoModuleException;
import com.flowpowered.noise.module.Module;

/**
 * A modifier module to map a value from one range to another.
 * For example: 0.5 (-1 &lt; x &lt; 1) -&gt; 0.75 (0 &lt; x &lt; 1)
 */
public class Range extends Module {
    
    public static final double DEFAULT_CURRENT_LOWER_BOUND = -1f;
    public static final double DEFAULT_CURRENT_UPPER_BOUND = 1f;
    public static final double DEFAULT_NEW_LOWER_BOUND = 0f;
    public static final double DEFAULT_NEW_UPPER_BOUND = 1f;
    
    /* Current lower bound */
    private double currentLowerBound = DEFAULT_CURRENT_LOWER_BOUND;
    /* Current upper bound */
    private double currentUpperBound = DEFAULT_CURRENT_UPPER_BOUND;
    /* New lower bound */
    private double newLowerBound = DEFAULT_NEW_LOWER_BOUND;
    /* New Upper Bound */
    private double newUpperBound = DEFAULT_NEW_UPPER_BOUND;

    public Range() {
        super(1);
    }

    public double getCurrentLowerBound() {
        return currentLowerBound;
    }

    public void setCurrentLowerBound(double currentLowerBound) {
        this.currentLowerBound = currentLowerBound;
    }

    public double getCurrentUpperBound() {
        return currentUpperBound;
    }

    public void setCurrentUpperBound(double currentUpperBound) {
        this.currentUpperBound = currentUpperBound;
    }

    public double getNewLowerBound() {
        return newLowerBound;
    }

    public void setNewLowerBound(double newLowerBound) {
        this.newLowerBound = newLowerBound;
    }

    public double getNewUpperBound() {
        return newUpperBound;
    }

    public void setNewUpperBound(double newUpperBound) {
        this.newUpperBound = newUpperBound;
    }
    
    /**
     * Configure bounds for range module
     * @param currentLower current lower bound
     * @param currentUpper current upper bound
     * @param newLower new lower bound
     * @param newUpper new upper bound
     */
    public void setBounds(double currentLower, double currentUpper, double newLower,
    		double newUpper) {
    	setCurrentLowerBound(currentLower);
    	setCurrentUpperBound(currentUpper);
    	setNewLowerBound(newLower);
    	setNewUpperBound(newUpper);
    }

    @Override
    public int getSourceModuleCount() {
        return 1;
    }

    @Override
    public double getValue(double x, double y, double z) {
        if (sourceModule == null) {
            throw new NoModuleException();
        }
        if (sourceModule[0] == null) {
            throw new NoModuleException();
        }
        
        double oldVal = sourceModule[0].getValue(x, y, z);
        double scale = (newUpperBound - newLowerBound) / 
        		(currentUpperBound - currentLowerBound);
        double bias = newLowerBound - currentLowerBound * scale;
        return oldVal * scale + bias;
    }

}
