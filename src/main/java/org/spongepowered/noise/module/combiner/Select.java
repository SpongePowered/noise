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
package org.spongepowered.noise.module.combiner;

import org.spongepowered.noise.Utils;
import org.spongepowered.noise.exception.NoModuleException;
import org.spongepowered.noise.module.Module;

public class Select extends Module {
    // Default edge-falloff value for the noise::module::Select noise module.
    public static final double DEFAULT_SELECT_EDGE_FALLOFF = 0.0;
    // Default lower bound of the selection range for the
    // noise::module::Select noise module.
    public static final double DEFAULT_SELECT_LOWER_BOUND = -1.0;
    // Default upper bound of the selection range for the
    // noise::module::Select noise module.
    public static final double DEFAULT_SELECT_UPPER_BOUND = 1.0;
    // Edge-falloff value.
    private double edgeFalloff = DEFAULT_SELECT_EDGE_FALLOFF;
    // Lower bound of the selection range.
    private double lowerBound = DEFAULT_SELECT_LOWER_BOUND;
    // Upper bound of the selection range.
    private double upperBound = DEFAULT_SELECT_UPPER_BOUND;

    public Select() {
        super(3);
    }

    public Module getControlModule() {
        if (sourceModule == null || sourceModule[2] == null) {
            throw new NoModuleException();
        }
        return sourceModule[2];
    }

    public void setControlModule(Module m) {
        if (m == null) {
            throw new IllegalArgumentException("the module cannot be null");
        }
        sourceModule[2] = m;
    }

    public double getEdgeFalloff() {
        return edgeFalloff;
    }

    public void setEdgeFalloff(double edgeFalloff) {
        // Make sure that the edge falloff curves do not overlap.
        double boundSize = upperBound - lowerBound;
        this.edgeFalloff = (edgeFalloff > boundSize / 2) ? boundSize / 2 : edgeFalloff;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public void setBounds(double upper, double lower) {
        if (lower > upper) {
            throw new IllegalArgumentException("lower must be less than upper");
        }
        this.lowerBound = lower;
        this.upperBound = upper;

        setEdgeFalloff(edgeFalloff);
    }

    @Override
    public int getSourceModuleCount() {
        return 3;
    }

    @Override
    public double getValue(double x, double y, double z) {
        if (sourceModule[0] == null) {
            throw new NoModuleException();
        }
        if (sourceModule[1] == null) {
            throw new NoModuleException();
        }
        if (sourceModule[2] == null) {
            throw new NoModuleException();
        }

        double controlValue = sourceModule[2].getValue(x, y, z);
        double alpha;
        if (edgeFalloff > 0.0) {
            if (controlValue < (lowerBound - edgeFalloff)) {
                // The output value from the control module is below the selector
                // threshold; return the output value from the first source module.
                return sourceModule[0].getValue(x, y, z);
            } else if (controlValue < (lowerBound + edgeFalloff)) {
                // The output value from the control module is near the lower end of the
                // selector threshold and within the smooth curve. Interpolate between
                // the output values from the first and second source modules.
                double lowerCurve = (lowerBound - edgeFalloff);
                double upperCurve = (lowerBound + edgeFalloff);
                alpha = Utils.sCurve3((controlValue - lowerCurve) / (upperCurve - lowerCurve));
                return Utils.linearInterp(sourceModule[0].getValue(x, y, z), sourceModule[1].getValue(x, y, z), alpha);
            } else if (controlValue < (upperBound - edgeFalloff)) {
                // The output value from the control module is within the selector
                // threshold; return the output value from the second source module.
                return sourceModule[1].getValue(x, y, z);
            } else if (controlValue < (upperBound + edgeFalloff)) {
                // The output value from the control module is near the upper end of the
                // selector threshold and within the smooth curve. Interpolate between
                // the output values from the first and second source modules.
                double lowerCurve = (upperBound - edgeFalloff);
                double upperCurve = (upperBound + edgeFalloff);
                alpha = Utils.sCurve3((controlValue - lowerCurve) / (upperCurve - lowerCurve));
                return Utils.linearInterp(sourceModule[1].getValue(x, y, z), sourceModule[0].getValue(x, y, z), alpha);
            } else {
                // Output value from the control module is above the selector threshold;
                // return the output value from the first source module.
                return sourceModule[0].getValue(x, y, z);
            }
        } else {
            if (controlValue < lowerBound || controlValue > upperBound) {
                return sourceModule[0].getValue(x, y, z);
            } else {
                return sourceModule[1].getValue(x, y, z);
            }
        }
    }
}
