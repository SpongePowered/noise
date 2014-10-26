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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.flowpowered.math.GenericMath;

import com.flowpowered.noise.module.Modifier;
import com.flowpowered.noise.util.MathUtils;
import com.flowpowered.noise.exception.NoModuleException;
import com.flowpowered.noise.module.Module;

public class Curve extends Modifier {
    private final List<ControlPoint> controlPoints;

    public Curve(Module source, ControlPoint... controlPoints) {
        super(source);
        if (controlPoints.length < 4) {
            throw new RuntimeException("Curve module must have at least 4 control points");
        }

        this.controlPoints = Arrays.asList(controlPoints);
    }

    public Curve(Module source, List<ControlPoint> controlPoints) {
        super(source);
        if (controlPoints.size() < 4) {
            throw new RuntimeException("Curve module must have at least 4 control points");
        }

        this.controlPoints = controlPoints;
    }

    /* TODO replicate this logic in builder
    public void addControlPoint(double inputValue, double outputValue) {
        int index = findInsertionPos(inputValue);
        insertAtPos(index, inputValue, outputValue);
    }


    private int findInsertionPos(double inputValue) {
        int insertionPos;
        for (insertionPos = 0; insertionPos < controlPoints.size(); insertionPos++) {
            if (inputValue < controlPoints.get(insertionPos).inputValue) {
                // We found the array index in which to insert the new control point.
                // Exit now.
                break;
            } else if (inputValue == controlPoints.get(insertionPos).inputValue) {
                // Each control point is required to contain a unique input value, so
                // throw an exception.
                throw new IllegalArgumentException("inputValue must be unique");
            }
        }
        return insertionPos;
    }

    private void insertAtPos(int insertionPos, double inputValue, double outputValue) {
        ControlPoint newPoint = new ControlPoint();
        newPoint.inputValue = inputValue;
        newPoint.outputValue = outputValue;
        controlPoints.add(insertionPos, newPoint);
    }*/

    @Override
    public double get(double x, double y, double z) {
        // Get the output value from the source module.
        double sourceModuleValue = source.get(x, y, z);

        // Find the first element in the control point array that has an input value
        // larger than the output value from the source module.
        int indexPos;
        for (indexPos = 0; indexPos < controlPoints.size(); indexPos++) {
            if (sourceModuleValue < controlPoints.get(indexPos).inputValue) {
                break;
            }
        }

        // Find the four nearest control points so that we can perform cubic
        // interpolation.
        int index0 = GenericMath.clamp(indexPos - 2, 0, controlPoints.size() - 1);
        int index1 = GenericMath.clamp(indexPos - 1, 0, controlPoints.size() - 1);
        int index2 = GenericMath.clamp(indexPos, 0, controlPoints.size() - 1);
        int index3 = GenericMath.clamp(indexPos + 1, 0, controlPoints.size() - 1);

        // If some control points are missing (which occurs if the value from the
        // source module is greater than the largest input value or less than the
        // smallest input value of the control point array), get the corresponding
        // output value of the nearest control point and exit now.
        if (index1 == index2) {
            return controlPoints.get(indexPos).outputValue;
        }

        // Compute the alpha value used for cubic interpolation.
        double input0 = controlPoints.get(indexPos).inputValue;
        double input1 = controlPoints.get(indexPos).inputValue;
        double alpha = (sourceModuleValue - input0) / (input1 - input0);

        // Now perform the cubic interpolation given the alpha value.
        return MathUtils.cubicInterp(controlPoints.get(index0).outputValue, controlPoints.get(index1).outputValue, controlPoints.get(index2).outputValue, controlPoints.get(index3).outputValue, alpha);
    }

    public static class ControlPoint {
        private double inputValue;
        private double outputValue;
    }
}
