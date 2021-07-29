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

import org.spongepowered.noise.Utils;
import org.spongepowered.noise.exception.NoModuleException;
import org.spongepowered.noise.module.Module;

public class Terrace extends Module {
    // Number of control points stored in this noise module.
    private int controlPointCount = 0;
    // Determines if the terrace-forming curve between all control points
    // is inverted.
    private boolean invertTerraces = false;
    // Array that stores the control points.
    private double[] controlPoints = new double[0];

    public Terrace() {
        super(1);
    }

    public boolean isInvertTerraces() {
        return this.invertTerraces;
    }

    public void setInvertTerraces(final boolean invertTerraces) {
        this.invertTerraces = invertTerraces;
    }

    public int getControlPointCount() {
        return this.controlPointCount;
    }

    public double[] getControlPoints() {
        return this.controlPoints;
    }

    public void addControlPoint(final double value) {
        final int insertionPos = this.findInsertionPos(value);
        this.insertAtPos(insertionPos, value);
    }

    public void clearAllControlPoints() {
        this.controlPoints = null;
        this.controlPointCount = 0;
    }

    public void makeControlPoints(final int controlPointCount) {
        if (controlPointCount < 2) {
            throw new IllegalArgumentException("Must have more than 2 control points");
        }

        this.clearAllControlPoints();

        final double terraceStep = 2.0 / (controlPointCount - 1.0);
        double curValue = -1.0;
        for (int i = 0; i < controlPointCount; i++) {
            this.addControlPoint(curValue);
            curValue += terraceStep;
        }
    }

    private int findInsertionPos(final double value) {
        int insertionPos;
        for (insertionPos = 0; insertionPos < this.controlPointCount; insertionPos++) {
            if (value < this.controlPoints[insertionPos]) {
                // We found the array index in which to insert the new control point.
                // Exit now.
                break;
            } else if (value == this.controlPoints[insertionPos]) {
                // Each control point is required to contain a unique value, so throw
                // an exception.
                throw new IllegalArgumentException("Value must be unique");
            }
        }
        return insertionPos;
    }

    private void insertAtPos(final int insertionPos, final double value) {
        // Make room for the new control point at the specified position within
        // the control point array.  The position is determined by the value of
        // the control point; the control points must be sorted by value within
        // that array.
        final double[] newControlPoints = new double[this.controlPointCount + 1];
        for (int i = 0; i < this.controlPointCount; i++) {
            if (i < insertionPos) {
                newControlPoints[i] = this.controlPoints[i];
            } else {
                newControlPoints[i + 1] = this.controlPoints[i];
            }
        }

        this.controlPoints = newControlPoints;
        ++this.controlPointCount;

        // Now that we've made room for the new control point within the array,
        // add the new control point.
        this.controlPoints[insertionPos] = value;
    }

    @Override
    public double getValue(final double x, final double y, final double z) {
        if (this.sourceModule[0] == null) {
            throw new NoModuleException(0);
        }

        // Get the output value from the source module.
        final double sourceModuleValue = this.sourceModule[0].getValue(x, y, z);

        // Find the first element in the control point array that has a value
        // larger than the output value from the source module.
        int indexPos;
        for (indexPos = 0; indexPos < this.controlPointCount; indexPos++) {
            if (sourceModuleValue < this.controlPoints[indexPos]) {
                break;
            }
        }

        // Find the two nearest control points so that we can map their values
        // onto a quadratic curve.
        final int index0 = Utils.clamp(indexPos - 1, 0, this.controlPointCount - 1);
        final int index1 = Utils.clamp(indexPos, 0, this.controlPointCount - 1);

        // If some control points are missing (which occurs if the output value from
        // the source module is greater than the largest value or less than the
        // smallest value of the control point array), get the value of the nearest
        // control point and exit now.
        if (index0 == index1) {
            return this.controlPoints[index1];
        }

        // Compute the alpha value used for linear interpolation.
        double value0 = this.controlPoints[index0];
        double value1 = this.controlPoints[index1];
        double alpha = (sourceModuleValue - value0) / (value1 - value0);
        if (this.invertTerraces) {
            alpha = 1.0 - alpha;
            final double temp = value0;
            value0 = value1;
            value1 = temp;
        }

        // Squaring the alpha produces the terrace effect.
        alpha *= alpha;

        // Now perform the linear interpolation given the alpha value.
        return Utils.linearInterp(value0, value1, alpha);
    }
}
