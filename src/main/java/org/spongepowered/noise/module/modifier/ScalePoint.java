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

public class ScalePoint extends Module {
    // Default scaling factor applied to the @a x coordinate for the
    // noise::module::ScalePoint noise module.
    public static final double DEFAULT_SCALE_POINT_X = 1.0;
    // Default scaling factor applied to the @a y coordinate for the
    // noise::module::ScalePoint noise module.
    public static final double DEFAULT_SCALE_POINT_Y = 1.0;
    // Default scaling factor applied to the @a z coordinate for the
    // noise::module::ScalePoint noise module.
    public static final double DEFAULT_SCALE_POINT_Z = 1.0;
    // Scaling factor applied to the @a x coordinate of the input value.
    private double xScale = DEFAULT_SCALE_POINT_X;
    // Scaling factor applied to the @a y coordinate of the input value.
    private double yScale = DEFAULT_SCALE_POINT_Y;
    // Scaling factor applied to the @a z coordinate of the input value.
    private double zScale = DEFAULT_SCALE_POINT_Z;

    public ScalePoint() {
        super(1);
    }

    public double getXScale() {
        return xScale;
    }

    public void setXScale(double xScale) {
        this.xScale = xScale;
    }

    public double getYScale() {
        return yScale;
    }

    public void setYScale(double yScale) {
        this.yScale = yScale;
    }

    public double getZScale() {
        return zScale;
    }

    public void setZScale(double zScale) {
        this.zScale = zScale;
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

        return sourceModule[0].getValue(x * xScale, y * yScale, z * zScale);
    }
}
