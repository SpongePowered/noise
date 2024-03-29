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
 * Noise module that maps the output value from a source module onto an
 * exponential curve.
 *
 * <p>Because most noise modules will output values that range from {@code -1.0}
 * to {@code +1.0}, this module first normalizes the output value (the range
 * becomes {@code 0.0} to {@code 1.0}), maps that value onto an exponential
 * curve, then rescales that value back to the original range.</p>
 *
 * @sourceModules 1
 */
public class Exponent extends NoiseModule {

    /**
     * Default exponent for the {@link Exponent noise module}.
     */
    public static final double DEFAULT_EXPONENT = 1.0;

    private double exponent = Exponent.DEFAULT_EXPONENT;

    public Exponent() {
        super(1);
    }

    /**
     * Create a new Exponent module with the source modules pre-configured.
     *
     * @param source the input module
     */
    public Exponent(final NoiseModule source) {
        this();
        this.setSourceModule(0, source);
    }

    /**
     * Get the exponent value to apply to the output value from the source module.
     *
     * <p>Because most noise modules will output values that range from {@code -1.0}
     * to {@code +1.0}, this module first normalizes the output value (the range
     * becomes {@code 0.0} to {@code 1.0}), maps that value onto an exponential
     * curve, then rescales that value back to the original range.</p>
     *
     * @return the exponent value
     */
    public double exponent() {
        return this.exponent;
    }

    /**
     * Set the exponent value to apply to the output value from the source module.
     *
     * <p>Because most noise modules will output values that range from {@code -1.0}
     * to {@code +1.0}, this module first normalizes the output value (the range
     * becomes {@code 0.0} to {@code 1.0}), maps that value onto an exponential
     * curve, then rescales that value back to the original range.</p>
     *
     * @param exponent  the exponent value
     */
    public void setExponent(final double exponent) {
        this.exponent = exponent;
    }

    @Override
    public double get(final double x, final double y, final double z) {
        if (this.sourceModule[0] == null) {
            throw new NoModuleException(0);
        }
        final double value = this.sourceModule[0].get(x, y, z);
        return Math.pow(value, this.exponent);
    }

}
