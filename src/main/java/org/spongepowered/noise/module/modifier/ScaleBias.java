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
 * Noise module that applies a scaling factor and a bias to the output value
 * from a source module.
 *
 * <p>The {@link #getValue(double, double, double)} method retrieves the output
 * value from the source module, multiplies it with a scaling factor, adds a
 * bias to it, then outputs the value.</p>
 *
 * @sourceModules 1
 */
public class ScaleBias extends NoiseModule {

    /**
     * Default bias for the {@link ScaleBias} noise module.
     */
    public static final double DEFAULT_BIAS = 0.0;

    /**
     * Default scale for the {@link ScaleBias} noise module.
     */
    public static final double DEFAULT_SCALE = 1.0;

    // Bias to apply to the scaled output value from the source module.
    private double bias = ScaleBias.DEFAULT_BIAS;
    // Scaling factor to apply to the output value from the source
    // module.
    private double scale = ScaleBias.DEFAULT_SCALE;

    public ScaleBias() {
        super(1);
    }

    /**
     * Create a new ScaleBias module with the source modules pre-configured.
     *
     * @param source the input module
     */
    public ScaleBias(final NoiseModule source) {
        this();
        this.setSourceModule(0, source);
    }

    /**
     * Get the bias to apply to the scaled output value from the source module.
     *
     * <p>The {@link #getValue(double, double, double)} method retrieves the output
     * value from the source module, multiplies it with a scaling factor, adds a
     * bias to it, then outputs the value.</p>
     *
     * @return the bias to apply
     */
    public double getBias() {
        return this.bias;
    }

    /**
     * Set the bias to apply to the scaled output value from the source module.
     *
     * <p>The {@link #getValue(double, double, double)} method retrieves the output
     * value from the source module, multiplies it with a scaling factor, adds a
     * bias to it, then outputs the value.</p>
     *
     * @param bias the bias to apply
     */
    public void setBias(final double bias) {
        this.bias = bias;
    }

    /**
     * Get the scaling factor to apply to the output value from the
     * source module.
     *
     * <p>The {@link #getValue(double, double, double)} method retrieves the output
     * value from the source module, multiplies it with a scaling factor, adds a
     * bias to it, then outputs the value.</p>
     *
     * @return the scaling factor to apply
     */
    public double getScale() {
        return this.scale;
    }

    /**
     * Set the scaling factor to apply to the output value from the
     * source module.
     *
     * <p>The {@link #getValue(double, double, double)} method retrieves the output
     * value from the source module, multiplies it with a scaling factor, adds a
     * bias to it, then outputs the value.</p>
     *
     * @param scale the scaling factor to apply
     */
    public void setScale(final double scale) {
        this.scale = scale;
    }

    @Override
    public double getValue(final double x, final double y, final double z) {
        if (this.sourceModule[0] == null) {
            throw new NoModuleException(0);
        }

        return this.sourceModule[0].getValue(x, y, z) * this.scale + this.bias;
    }
}
