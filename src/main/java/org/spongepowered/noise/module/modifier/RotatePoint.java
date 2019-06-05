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
import org.spongepowered.noise.module.Module;

public class RotatePoint extends Module {
    // Default @a x rotation angle for the noise::module::RotatePoint noise
    // module.
    public static final double DEFAULT_ROTATE_X = 0.0;
    // Default @a y rotation angle for the noise::module::RotatePoint noise
    // module.
    public static final double DEFAULT_ROTATE_Y = 0.0;
    // Default @a z rotation angle for the noise::module::RotatePoint noise
    // module.
    public static final double DEFAULT_ROTATE_Z = 0.0;
    private double xAngle = DEFAULT_ROTATE_X;
    private double yAngle = DEFAULT_ROTATE_Y;
    private double zAngle = DEFAULT_ROTATE_Z;
    // An entry within the 3x3 rotation matrix used for rotating the
    // input value.
    private double x1Matrix;
    // An entry within the 3x3 rotation matrix used for rotating the
    // input value.
    private double x2Matrix;
    // An entry within the 3x3 rotation matrix used for rotating the
    // input value.
    private double x3Matrix;
    // An entry within the 3x3 rotation matrix used for rotating the
    // input value.
    private double y1Matrix;
    // An entry within the 3x3 rotation matrix used for rotating the
    // input value.
    private double y2Matrix;
    // An entry within the 3x3 rotation matrix used for rotating the
    // input value.
    private double y3Matrix;
    // An entry within the 3x3 rotation matrix used for rotating the
    // input value.
    private double z1Matrix;
    // An entry within the 3x3 rotation matrix used for rotating the
    // input value.
    private double z2Matrix;
    // An entry within the 3x3 rotation matrix used for rotating the
    // input value.
    private double z3Matrix;

    public RotatePoint() {
        super(1);
        setAngles(DEFAULT_ROTATE_X, DEFAULT_ROTATE_Y, DEFAULT_ROTATE_Z);
    }

    public void setAngles(double x, double y, double z) {
        double xCos, yCos, zCos, xSin, ySin, zSin;
        xCos = Math.cos(Math.toRadians(x));
        yCos = Math.cos(Math.toRadians(y));
        zCos = Math.cos(Math.toRadians(z));
        xSin = Math.sin(Math.toRadians(x));
        ySin = Math.sin(Math.toRadians(y));
        zSin = Math.sin(Math.toRadians(z));

        x1Matrix = ySin * xSin * zSin + yCos * zCos;
        y1Matrix = xCos * zSin;
        z1Matrix = ySin * zCos - yCos * xSin * zSin;
        x2Matrix = ySin * xSin * zCos - yCos * zSin;
        y2Matrix = xCos * zCos;
        z2Matrix = -yCos * xSin * zCos - ySin * zSin;
        x3Matrix = -ySin * xCos;
        y3Matrix = xSin;
        z3Matrix = yCos * xCos;

        this.xAngle = x;
        this.yAngle = y;
        this.zAngle = z;
    }

    public double getXAngle() {
        return xAngle;
    }

    public void setXAngle(double xAngle) {
        setAngles(xAngle, yAngle, zAngle);
    }

    public double getYAngle() {
        return yAngle;
    }

    public void setYAngle(double yAngle) {
        setAngles(xAngle, yAngle, zAngle);
    }

    public double getZAngle() {
        return zAngle;
    }

    public void setZAngle(double zAngle) {
        setAngles(xAngle, yAngle, zAngle);
    }

    @Override
    public int getSourceModuleCount() {
        return 1;
    }

    @Override
    public double getValue(double x, double y, double z) {
        if (sourceModule[0] == null) {
            throw new NoModuleException();
        }

        double nx = (x1Matrix * x) + (y1Matrix * y) + (z1Matrix * z);
        double ny = (x2Matrix * x) + (y2Matrix * y) + (z2Matrix * z);
        double nz = (x3Matrix * x) + (y3Matrix * y) + (z3Matrix * z);
        return sourceModule[0].getValue(nx, ny, nz);
    }
}
