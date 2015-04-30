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
package com.flowpowered.noise.module.selector;

import com.flowpowered.noise.Utils;
import com.flowpowered.noise.module.Module;

public class Select extends Selector {

    // Edge-falloff value.
    private final double edgeFalloff;
    // Lower bound of the selection range.
    private final double lowerBound;
    // Upper bound of the selection range.
    private final double upperBound;

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

    public double getEdgeFalloff() {
        return edgeFalloff;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    @Override
    public double get(double x, double y, double z) {

        double controlValue = control.get(x, y, z);
        double alpha;
        if (edgeFalloff > 0.0) {
            if (controlValue < (lowerBound - edgeFalloff)) {
                // The output value from the control module is below the selector
                // threshold; return the output value from the first source module.
                return sourceA.get(x, y, z);
            } else if (controlValue < (lowerBound + edgeFalloff)) {
                // The output value from the control module is near the lower end of the
                // selector threshold and within the smooth curve. Interpolate between
                // the output values from the first and second source modules.
                double lowerCurve = (lowerBound - edgeFalloff);
                double upperCurve = (lowerBound + edgeFalloff);
                alpha = Utils.sCurve3((controlValue - lowerCurve) / (upperCurve - lowerCurve));
                return Utils.linearInterp(sourceA.get(x, y, z), sourceB.get(x, y, z), alpha);
            } else if (controlValue < (upperBound - edgeFalloff)) {
                // The output value from the control module is within the selector
                // threshold; return the output value from the second source module.
                return sourceB.get(x, y, z);
            } else if (controlValue < (upperBound + edgeFalloff)) {
                // The output value from the control module is near the upper end of the
                // selector threshold and within the smooth curve. Interpolate between
                // the output values from the first and second source modules.
                double lowerCurve = (upperBound - edgeFalloff);
                double upperCurve = (upperBound + edgeFalloff);
                alpha = Utils.sCurve3((controlValue - lowerCurve) / (upperCurve - lowerCurve));
                return Utils.linearInterp(sourceB.get(x, y, z), sourceA.get(x, y, z), alpha);
            } else {
                // Output value from the control module is above the selector threshold;
                // return the output value from the first source module.
                return sourceA.get(x, y, z);
            }
        } else {
            if (controlValue < lowerBound || controlValue > upperBound) {
                return sourceA.get(x, y, z);
            } else {
                return sourceB.get(x, y, z);
            }
        }
    }
}
