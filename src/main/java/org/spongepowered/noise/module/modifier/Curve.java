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

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.noise.Utils;
import org.spongepowered.noise.exception.NoModuleException;
import org.spongepowered.noise.exception.NoiseException;
import org.spongepowered.noise.module.Module;

/**
 * Noise module that maps the output value from a source module onto an
 * arbitrary function curve.
 *
 * <p>This noise module maps the output value from the source module onto an
 * application-defined curve. This curve is defined by a number of
 * <em>control points</em>; each control point has an <em>input value</em> that
 * maps to an <em>output value.</em></p>
 *
 * <p>To add control points to this curve, call the
 * {@link #addControlPoint(double, double)} method.</p>
 *
 * <p>Since this curve is a cubic spline, an application must add a minimum of
 * four control points to the curve. If this is not done, the
 * {@link #getValue(double, double, double)} method fails. Each control point
 * can have any input and output value, although no two control points can have
 * the same input value. There is no limit to the number of control points that
 * can be added to the curve.</p>
 *
 * @sourceModules 1
 */
public class Curve extends Module {
    private final List<ControlPoint> controlPoints = new ArrayList<>();

    public Curve() {
        super(1);
    }

    /**
     * Create a new Curve module with the source modules pre-configured.
     *
     * @param source the input module
     */
    public Curve(final Module source) {
        this();
        this.setSourceModule(0, source);
    }

    /**
     * Adds a control point to the curve.
     *
     * <p>No two control points amy have the same input value.</p>
     *
     * <p>It does not matter which order these points are added in.</p>
     *
     * @param inputValue the input value stored in the control point
     * @param outputValue the output value stored in the control point
     * @throws IllegalArgumentException if more than one control point has the
     *     same input value
     */
    public void addControlPoint(final double inputValue, final double outputValue) {
        final int index = this.findInsertionPos(inputValue);
        this.insertAtPos(index, inputValue, outputValue);
    }

    /**
     * Get a copy of the array holding all control points.
     *
     * @return a copy of the array of control points
     */
    public ControlPoint[] getControlPoints() {
        return this.controlPoints.toArray(new ControlPoint[0]);
    }

    /**
     * Delete all control points on the curve.
     */
    public void clearAllControlPoints() {
        this.controlPoints.clear();
    }

    /**
     * Determines the array index in which to insert the control point into the
     * internal control point array.
     *
     * <p>By inserting the control point at the returned array index, this class
     * ensures that the control point array is sorted by input value. The code
     * that maps a value onto the curve requires a sorted control
     * point array.</p>
     *
     * @param inputValue the input value of the control point
     * @return the array index in which to insert the control point
     * @throws IllegalArgumentException if the input value is non-unique
     */
    private int findInsertionPos(final double inputValue) {
        int insertionPos;
        for (insertionPos = 0; insertionPos < this.controlPoints.size(); insertionPos++) {
            if (inputValue < this.controlPoints.get(insertionPos).inputValue) {
                // We found the array index in which to insert the new control point.
                // Exit now.
                break;
            } else if (inputValue == this.controlPoints.get(insertionPos).inputValue) {
                // Each control point is required to contain a unique input value, so
                // throw an exception.
                throw new IllegalArgumentException("inputValue must be unique");
            }
        }
        return insertionPos;
    }

    /**
     * Inserts the control point at the specified position in the internal
     * control point array.
     *
     * <p>Because the curve mapping algorithm used by this noise module requires
     * that all control points in the array mut be sorted by input value, the
     * new control point should be inserted at the position in which the order
     * is still preserved.</p>
     *
     * @param insertionPos the zero-based array position in which to insert the
     * control point
     * @param inputValue the input value stored in the control point
     * @param outputValue the output value stored in the control point
     */
    private void insertAtPos(final int insertionPos, final double inputValue, final double outputValue) {
        final ControlPoint newPoint = new ControlPoint(inputValue, outputValue);
        this.controlPoints.add(insertionPos, newPoint);
    }

    @Override
    public double getValue(final double x, final double y, final double z) {
        if (this.sourceModule[0] == null) {
            throw new NoModuleException(0);
        }
        final int size = this.controlPoints.size();
        if (size < 4) {
            throw new NoiseException("Curve module must have at least 4 control points");
        }

        // Get the output value from the source module.
        final double sourceModuleValue = this.sourceModule[0].getValue(x, y, z);

        // Find the first element in the control point array that has an input value
        // larger than the output value from the source module.
        int indexPos;
        for (indexPos = 0; indexPos < size; indexPos++) {
            if (sourceModuleValue < this.controlPoints.get(indexPos).inputValue) {
                break;
            }
        }

        // Find the four nearest control points so that we can perform cubic
        // interpolation.
        final int lastIndex = size - 1;
        final int index0 = Utils.clamp(indexPos - 2, 0, lastIndex);
        final int index1 = Utils.clamp(indexPos - 1, 0, lastIndex);
        final int index2 = Utils.clamp(indexPos, 0, lastIndex);
        final int index3 = Utils.clamp(indexPos + 1, 0, lastIndex);

        // If some control points are missing (which occurs if the value from the
        // source module is greater than the largest input value or less than the
        // smallest input value of the control point array), get the corresponding
        // output value of the nearest control point and exit now.
        if (index1 == index2) {
            return this.controlPoints.get(index1).outputValue;
        }

        // Compute the alpha value used for cubic interpolation.
        final double input0 = this.controlPoints.get(index1).inputValue;
        final double input1 = this.controlPoints.get(index2).inputValue;
        final double alpha = (sourceModuleValue - input0) / (input1 - input0);

        // Now perform the cubic interpolation given the alpha value.
        return Utils.cubicInterp(
            this.controlPoints.get(index0).outputValue, this.controlPoints.get(index1).outputValue, this.controlPoints.get(index2).outputValue,
            this.controlPoints.get(index3).outputValue, alpha);
    }

    /**
     * A control point for defining splines.
     */
    public static final class ControlPoint {
        final double inputValue;
        final double outputValue;

        public ControlPoint(final double inputValue, final double outputValue) {
            this.inputValue = inputValue;
            this.outputValue = outputValue;
        }

        /**
         * Get the input value.
         *
         * @return the input value
         */
        public double getInputValue() {
            return this.inputValue;
        }

        /**
         * Get the output value mapped from the input value.
         *
         * @return the output value mapped from the input value.
         */
        public double getOutputValue() {
            return this.outputValue;
        }
    }
}
