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
package org.spongepowered.noise.model;

import org.spongepowered.noise.exception.NoModuleException;
import org.spongepowered.noise.module.NoiseModule;

import java.util.Objects;

/**
 * Model that defines the displacement of a line segment.
 *
 * <p>This model returns an output value from a noise module given the
 * one-dimensional coordinate of an input value located on a line segment, which
 * can be used as displacements.</p>
 *
 * <p>This class is useful for creating:</p>
 * <ul>
 * <li>roads and rivers</li>
 * <li>disaffected college students</li>
 * </ul>
 *
 * <p>To generate an output value, pass an input value between 0.0 and 1.0 to
 * the {@link #get(double)} method. {@code 0.0} represents the start
 * position of the line segment and {@code 1.0} represents the end position
 * of the line segment.</p>
 */
public class Line {

    // A flag that specifies whether the value is to be attenuated
    // (moved toward 0.0) as the ends of the line segment are approached.
    private boolean attenuate = false;
    // A pointer to the noise module used to generate the output values.
    private NoiseModule module;
    // {@code x} coordinate of the start of the line segment.
    private double x0 = 0;
    // {@code x} coordinate of the end of the line segment.
    private double x1 = 1;
    // {@code y} coordinate of the start of the line segment.
    private double y0 = 0;
    // {@code y} coordinate of the end of the line segment.
    private double y1 = 1;
    // {@code z} coordinate of the start of the line segment.
    private double z0 = 0;
    // {@code z} coordinate of the end of the line segment.
    private double z1 = 1;

    /**
     * @param module The noise module that is used to generate the output values.
     */
    public Line(final NoiseModule module) {
        if (module == null) {
            throw new IllegalArgumentException("module cannot be null");
        }
        this.module = module;
    }

    /**
     * Returns a flag indicating whether the output value is to be attenuated
     * (moved toward 0.0) as the ends of the line segment are approached by
     * the input value.
     *
     * @return true if the value is to be attenuated false if not.
     */
    public boolean attenuate() {
        return this.attenuate;
    }

    /**
     * Sets a flag indicating that the output value is to be attenuated
     * (moved toward 0.0) as the ends of the line segment are approached.
     *
     * @param att A flag that specifies whether the output value is to be attenuated.
     */
    public void setAttenuate(final boolean att) {
        this.attenuate = att;
    }

    /**
     * Sets the position ( {@code x}, {@code y}, {@code z} ) of the start of the
     * line segment to choose values along.
     *
     * @param x x coordinate of the start position.
     * @param y y coordinate of the start position.
     * @param z z coordinate of the start position.
     */
    public void setStartPoint(final double x, final double y, final double z) {
        this.x0 = x;
        this.y0 = y;
        this.z0 = z;
    }

    /**
     * Sets the position ( {@code x}, {@code y}, {@code z} ) of the end of the
     * line segment to choose values along.
     *
     * @param x x coordinate of the end position.
     * @param y y coordinate of the end position.
     * @param z z coordinate of the end position.
     */
    public void setEndPoint(final double x, final double y, final double z) {
        this.x1 = x;
        this.y1 = y;
        this.z1 = z;
    }

    /**
     * Returns the noise module that is used to generate the output values.
     *
     * @return the module used to generate output values
     */
    public NoiseModule module() {
        return this.module;
    }

    /**
     * Sets the noise module that is used to generate the output values.
     *
     * <p>This noise module must exist for the lifetime of this object, until
     * you pass a new noise module to this method.</p>
     *
     * @param module The noise module that is used to generate the output values.
     */
    public void setModule(final NoiseModule module) {
        this.module = Objects.requireNonNull(module, "Module cannot be null");
    }

    /**
     * Returns the output value from the noise module given the one-dimensional
     * coordinate of the specified input value located on the line segment.
     *
     * @param p The distance along the line segment (ranges from 0.0 to 1.0)
     * @return The output value from the noise module.
     */
    public double get(final double p) {
        if (this.module == null) {
            throw new NoModuleException(0);
        }

        final double x = (this.x1 - this.x0) * p + this.x0;
        final double y = (this.y1 - this.y0) * p + this.y0;
        final double z = (this.z1 - this.z0) * p + this.z0;
        final double value = this.module.get(x, y, z);

        if (this.attenuate) {
            return p * (1.0 - p) * 4 * value;
        } else {
            return value;
        }
    }

}
