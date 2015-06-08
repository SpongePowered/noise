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
package com.flowpowered.noise.module.modifier.transformer;

import com.flowpowered.noise.module.Module;
import com.flowpowered.noise.module.modifier.Modifier;

public class RotatePoint extends Modifier {

    // Rotation angles
    private final double xAngle;
    private final double yAngle;
    private final double zAngle;

    // An entry within the 3x3 rotation matrix used for rotating the
    // input value.
    private final double x1Matrix;
    // An entry within the 3x3 rotation matrix used for rotating the
    // input value.
    private final double x2Matrix;
    // An entry within the 3x3 rotation matrix used for rotating the
    // input value.
    private final double x3Matrix;
    // An entry within the 3x3 rotation matrix used for rotating the
    // input value.
    private final double y1Matrix;
    // An entry within the 3x3 rotation matrix used for rotating the
    // input value.
    private final double y2Matrix;
    // An entry within the 3x3 rotation matrix used for rotating the
    // input value.
    private final double y3Matrix;
    // An entry within the 3x3 rotation matrix used for rotating the
    // input value.
    private final double z1Matrix;
    // An entry within the 3x3 rotation matrix used for rotating the
    // input value.
    private final double z2Matrix;
    // An entry within the 3x3 rotation matrix used for rotating the
    // input value.
    private final double z3Matrix;

    public RotatePoint(Module source, double xAngle, double yAngle, double zAngle) {
        super(source);
        this.xAngle = xAngle;
        this.yAngle = yAngle;
        this.zAngle = zAngle;

        final double xCos, yCos, zCos, xSin, ySin, zSin;
        xCos = Math.cos(Math.toRadians(xAngle));
        yCos = Math.cos(Math.toRadians(yAngle));
        zCos = Math.cos(Math.toRadians(zAngle));
        xSin = Math.sin(Math.toRadians(xAngle));
        ySin = Math.sin(Math.toRadians(yAngle));
        zSin = Math.sin(Math.toRadians(zAngle));

        x1Matrix = ySin * xSin * zSin + yCos * zCos;
        y1Matrix = xCos * zSin;
        z1Matrix = ySin * zCos - yCos * xSin * zSin;
        x2Matrix = ySin * xSin * zCos - yCos * zSin;
        y2Matrix = xCos * zCos;
        z2Matrix = -yCos * xSin * zCos - ySin * zSin;
        x3Matrix = -ySin * xCos;
        y3Matrix = xSin;
        z3Matrix = yCos * xCos;
    }

    public double getXAngle() {
        return xAngle;
    }

    public double getYAngle() {
        return yAngle;
    }

    public double getZAngle() {
        return zAngle;
    }

    @Override
    public double getValue(double x, double y, double z) {
        double nx = (x1Matrix * x) + (y1Matrix * y) + (z1Matrix * z);
        double ny = (x2Matrix * x) + (y2Matrix * y) + (z2Matrix * z);
        double nz = (x3Matrix * x) + (y3Matrix * y) + (z3Matrix * z);
        return source.getValue(nx, ny, nz);
    }

}
