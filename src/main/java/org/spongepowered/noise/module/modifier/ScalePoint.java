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
import org.spongepowered.noise.module.NoiseModule;

/**
 * Noise module that scales the coordinates of the input value before returning
 * the output value from a source module.
 *
 * <p>The {@link #get(double, double, double)} method multiplies the
 * {@code (x, y, z)} coordinates of the input value with a scaling factor before
 * returning the output value from the source module.</p>
 *
 * <p>To set the scaling factor, call the {@link #setScale(double)} method. To
 * set the scaling factor to apply to the individual {@code x}, {@code y},
 * or {@code z} coordinates, call the {@link #setXScale(double)},
 * {@link #setYScale(double)}, or {@link #setZScale(double)} methods,
 * respectively.</p>
 *
 * @sourceModules 1
 */
public class ScalePoint extends NoiseModule {

    /**
     * Default scaling factor applied to the {@code x} coordinate for the
     * {@link ScalePoint} noise module.
     */
    public static final double DEFAULT_SCALE_POINT_X = 1.0;

    /**
     * Default scaling factor applied to the {@code y} coordinate for the
     * {@link ScalePoint} noise module.
     */
    public static final double DEFAULT_SCALE_POINT_Y = 1.0;

    /**
     * Default scaling factor applied to the {@code z} coordinate for the
     * {@link ScalePoint} noise module.
     */
    public static final double DEFAULT_SCALE_POINT_Z = 1.0;
    // Scaling factor applied to the {@code x} coordinate of the input value.
    private double xScale = ScalePoint.DEFAULT_SCALE_POINT_X;
    // Scaling factor applied to the {@code y} coordinate of the input value.
    private double yScale = ScalePoint.DEFAULT_SCALE_POINT_Y;
    // Scaling factor applied to the {@code z} coordinate of the input value.
    private double zScale = ScalePoint.DEFAULT_SCALE_POINT_Z;

    public ScalePoint() {
        super(1);
    }

    /**
     * Create a new ScalePoint module with the source modules pre-configured.
     *
     * @param source the input module
     */
    public ScalePoint(final NoiseModule source) {
        this();
        this.setSourceModule(0, source);
    }

    /**
     * Get the scaling factor applied to the {@code x} coordinate of the
     * input value.
     *
     * @return the scaling factor applied to the {@code x} coordinate
     * @see #DEFAULT_SCALE_POINT_X
     */
    public double xScale() {
        return this.xScale;
    }

    /**
     * Set the scaling factor to apply to the {@code x} coordinate of the
     * input value.
     *
     * <p>The {@link #get(double, double, double)} method multiplies the
     * {@code (x, y, z)} coordinates of the input value with a scaling factor before
     * returning the output value from the source module.</p>
     *
     * @param xScale the scaling factor to apply to the {@code x} coordinate
     */
    public void setXScale(final double xScale) {
        this.xScale = xScale;
    }

    /**
     * Get the scaling factor applied to the {@code y} coordinate of the
     * input value.
     *
     * @return the scaling factor applied to the {@code y} coordinate
     * @see #DEFAULT_SCALE_POINT_Y
     */
    public double yScale() {
        return this.yScale;
    }

    /**
     * Set the scaling factor to apply to the {@code y} coordinate of the
     * input value.
     *
     * <p>The {@link #get(double, double, double)} method multiplies the
     * {@code (x, y, z)} coordinates of the input value with a scaling factor before
     * returning the output value from the source module.</p>
     *
     * @param yScale the scaling factor to apply to the {@code y} coordinate
     */
    public void setYScale(final double yScale) {
        this.yScale = yScale;
    }

    /**
     * Get the scaling factor applied to the {@code z} coordinate of the
     * input value.
     *
     * @return the scaling factor applied to the {@code z} coordinate
     * @see #DEFAULT_SCALE_POINT_Z
     */
    public double zScale() {
        return this.zScale;
    }

    /**
     * Set the scaling factor to apply to the {@code z} coordinate of the
     * input value.
     *
     * <p>The {@link #get(double, double, double)} method multiplies the
     * {@code (x, y, z)} coordinates of the input value with a scaling factor before
     * returning the output value from the source module.</p>
     *
     * @param zScale the scaling factor to apply to the {@code z} coordinate
     */
    public void setZScale(final double zScale) {
        this.zScale = zScale;
    }

    /**
     * Set the scaling factor to apply to the input value.
     *
     * <p>The {@link #get(double, double, double)} method multiplies the
     * {@code (x, y, z)} coordinates of the input value with a scaling factor before
     * returning the output value from the source module.</p>
     *
     * @param scale the scaling factor to apply
     */
    public void setScale(final double scale) {
        this.xScale = this.yScale = this.zScale = scale;
    }

    @Override
    public double get(final double x, final double y, final double z) {
        if (this.sourceModule[0] == null) {
            throw new NoModuleException(0);
        }

        return this.sourceModule[0].get(x * this.xScale, y * this.yScale, z * this.zScale);
    }
}
