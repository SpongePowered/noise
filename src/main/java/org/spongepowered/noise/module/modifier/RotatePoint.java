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
    // Default {@code x} rotation angle for the noise::module::RotatePoint noise
    // module.
    public static final double DEFAULT_ROTATE_X = 0.0;
    // Default {@code y} rotation angle for the noise::module::RotatePoint noise
    // module.
    public static final double DEFAULT_ROTATE_Y = 0.0;
    // Default {@code z} rotation angle for the noise::module::RotatePoint noise
    // module.
    public static final double DEFAULT_ROTATE_Z = 0.0;
    private double xAngle = RotatePoint.DEFAULT_ROTATE_X;
    private double yAngle = RotatePoint.DEFAULT_ROTATE_Y;
    private double zAngle = RotatePoint.DEFAULT_ROTATE_Z;
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
        this.setAngles(RotatePoint.DEFAULT_ROTATE_X, RotatePoint.DEFAULT_ROTATE_Y, RotatePoint.DEFAULT_ROTATE_Z);
    }

    public void setAngles(final double x, final double y, final double z) {
        final double xCos = Math.cos(Math.toRadians(x));
        final double yCos = Math.cos(Math.toRadians(y));
        final double zCos = Math.cos(Math.toRadians(z));
        final double xSin = Math.sin(Math.toRadians(x));
        final double ySin = Math.sin(Math.toRadians(y));
        final double zSin = Math.sin(Math.toRadians(z));

        this.x1Matrix = ySin * xSin * zSin + yCos * zCos;
        this.y1Matrix = xCos * zSin;
        this.z1Matrix = ySin * zCos - yCos * xSin * zSin;
        this.x2Matrix = ySin * xSin * zCos - yCos * zSin;
        this.y2Matrix = xCos * zCos;
        this.z2Matrix = -yCos * xSin * zCos - ySin * zSin;
        this.x3Matrix = -ySin * xCos;
        this.y3Matrix = xSin;
        this.z3Matrix = yCos * xCos;

        this.xAngle = x;
        this.yAngle = y;
        this.zAngle = z;
    }

    public double getXAngle() {
        return this.xAngle;
    }

    public void setXAngle(final double xAngle) {
        this.setAngles(xAngle, this.yAngle, this.zAngle);
    }

    public double getYAngle() {
        return this.yAngle;
    }

    public void setYAngle(final double yAngle) {
        this.setAngles(this.xAngle, yAngle, this.zAngle);
    }

    public double getZAngle() {
        return this.zAngle;
    }

    public void setZAngle(final double zAngle) {
        this.setAngles(this.xAngle, this.yAngle, zAngle);
    }

    @Override
    public double getValue(final double x, final double y, final double z) {
        if (this.sourceModule[0] == null) {
            throw new NoModuleException(0);
        }

        final double nx = (this.x1Matrix * x) + (this.y1Matrix * y) + (this.z1Matrix * z);
        final double ny = (this.x2Matrix * x) + (this.y2Matrix * y) + (this.z2Matrix * z);
        final double nz = (this.x3Matrix * x) + (this.y3Matrix * y) + (this.z3Matrix * z);
        return this.sourceModule[0].getValue(nx, ny, nz);
    }
}
