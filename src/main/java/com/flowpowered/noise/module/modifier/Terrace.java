/*
 * This file is part of Flow Noise, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2013 Spout LLC <http://www.spout.org/>
 * Original libnoise in C++ by Jason Bevins <http://libnoise.sourceforge.net/>
 * jlibnoise Java port by Garrett Fleenor <https://github.com/RoyAwesome/jlibnoise>
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

import com.flowpowered.math.GenericMath;

import com.flowpowered.noise.module.Modifier;
import com.flowpowered.noise.util.MathUtils;
import com.flowpowered.noise.exception.NoModuleException;
import com.flowpowered.noise.module.Module;

public class Terrace extends Modifier {
    // Determines if the terrace-forming curve between all control points
    // is inverted.
    private final boolean invertTerraces;
    // Array that stores the control points.
    private final double[] controlPoints;

    public Terrace(Module source, double[] controlPoints, boolean invertTerraces) {
        super(source);

        this.controlPoints = controlPoints;
        this.invertTerraces = invertTerraces;
    }

    public boolean isInvertTerraces() {
        return invertTerraces;
    }

    public int getControlPointCount() {
        return controlPoints.length;
    }

    public double[] getControlPoints() {
        return controlPoints;
    }

    /* TODO replicate this logic in builder
    public void addControlPoint(double value) {
        int insertionPos = findInsertionPos(value);
        insertAtPos(insertionPos, value);
    }

    public void clearAllControlPoints() {
        controlPoints = null;
    }

    public void makeControlPoints(int controlPointCount) {
        if (controlPointCount < 2) {
            throw new IllegalArgumentException("Must have more than 2 control points");
        }

        clearAllControlPoints();

        double terraceStep = 2.0 / (controlPointCount - 1.0);
        double curValue = -1.0;
        for (int i = 0; i < controlPointCount; i++) {
            addControlPoint(curValue);
            curValue += terraceStep;
        }
    }

    private int findInsertionPos(double value) {
        int insertionPos;
        for (insertionPos = 0; insertionPos < controlPointCount; insertionPos++) {
            if (value < controlPoints[insertionPos]) {
                // We found the array index in which to insert the new control point.
                // Exit now.
                break;
            } else if (value == controlPoints[insertionPos]) {
                // Each control point is required to contain a unique value, so throw
                // an exception.
                throw new IllegalArgumentException("Value must be unique");
            }
        }
        return insertionPos;
    }

    private void insertAtPos(int insertionPos, double value) {
        // Make room for the new control point at the specified position within
        // the control point array.  The position is determined by the value of
        // the control point; the control points must be sorted by value within
        // that array.
        double[] newControlPoints = new double[controlPointCount + 1];
        for (int i = 0; i < controlPointCount; i++) {
            if (i < insertionPos) {
                newControlPoints[i] = controlPoints[i];
            } else {
                newControlPoints[i + 1] = controlPoints[i];
            }
        }

        controlPoints = newControlPoints;
        ++controlPointCount;

        // Now that we've made room for the new control point within the array,
        // add the new control point.
        controlPoints[insertionPos] = value;
    }*/

    @Override
    public double get(double x, double y, double z) {
        // Get the output value from the source module.
        double sourceModuleValue = source.get(x, y, z);

        int controlPointCount = controlPoints.length;

        // Find the first element in the control point array that has a value
        // larger than the output value from the source module.
        int indexPos;
        for (indexPos = 0; indexPos < controlPointCount; indexPos++) {
            if (sourceModuleValue < controlPoints[indexPos]) {
                break;
            }
        }

        // Find the two nearest control points so that we can map their values
        // onto a quadratic curve.
        int index0 = GenericMath.clamp(indexPos - 1, 0, controlPointCount - 1);
        int index1 = GenericMath.clamp(indexPos, 0, controlPointCount - 1);

        // If some control points are missing (which occurs if the output value from
        // the source module is greater than the largest value or less than the
        // smallest value of the control point array), get the value of the nearest
        // control point and exit now.
        if (index0 == index1) {
            return controlPoints[index1];
        }

        // Compute the alpha value used for linear interpolation.
        double value0 = controlPoints[index0];
        double value1 = controlPoints[index1];
        double alpha = (sourceModuleValue - value0) / (value1 - value0);
        if (invertTerraces) {
            alpha = 1.0 - alpha;
            double temp = value0;
            value0 = value1;
            value1 = temp;
        }

        // Squaring the alpha produces the terrace effect.
        alpha *= alpha;

        // Now perform the linear interpolation given the alpha value.
        return MathUtils.linearInterp(value0, value1, alpha);
    }
}
