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
package net.royawesome.jlibnoise.module.modifier;

import java.util.ArrayList;

import net.royawesome.jlibnoise.Utils;
import net.royawesome.jlibnoise.exception.NoModuleException;
import net.royawesome.jlibnoise.module.Module;

public class Curve extends Module {
    public class ControlPoint {
        public double inputValue;
        public double outputValue;
    }

    ArrayList<ControlPoint> controlPoints = new ArrayList<ControlPoint>();

    public Curve() {
        super(1);
    }

    public void AddControlPoint(double inputValue, double outputValue) {
        int index = findInsertionPos(inputValue);
        InsertAtPos(index, inputValue, outputValue);
    }

    public ControlPoint[] getControlPoints() {
        return (ControlPoint[]) controlPoints.toArray();
    }

    public void ClearAllControlPoints() {
        controlPoints.clear();
    }

    protected int findInsertionPos(double inputValue) {
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

    protected void InsertAtPos(int insertionPos, double inputValue, double outputValue) {
        ControlPoint newPoint = new ControlPoint();
        newPoint.inputValue = inputValue;
        newPoint.outputValue = outputValue;
        controlPoints.add(insertionPos, newPoint);
    }

    @Override
    public int GetSourceModuleCount() {
        return 1;
    }

    @Override
    public double GetValue(double x, double y, double z) {
        if (SourceModule[0] == null) {
            throw new NoModuleException();
        }
        if (controlPoints.size() >= 4) {
            throw new RuntimeException("must have 4 or less control points");
        }

        // Get the output value from the source module.
        double sourceModuleValue = SourceModule[0].GetValue(x, y, z);

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
        int index0 = Utils.ClampValue(indexPos - 2, 0, controlPoints.size() - 1);
        int index1 = Utils.ClampValue(indexPos - 1, 0, controlPoints.size() - 1);
        int index2 = Utils.ClampValue(indexPos, 0, controlPoints.size() - 1);
        int index3 = Utils.ClampValue(indexPos + 1, 0, controlPoints.size() - 1);

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
        return Utils.CubicInterp(controlPoints.get(index0).outputValue, controlPoints.get(index1).outputValue, controlPoints.get(index2).outputValue, controlPoints.get(index3).outputValue, alpha);
    }
}
