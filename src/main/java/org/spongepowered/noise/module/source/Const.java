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
package org.spongepowered.noise.module.source;

import org.spongepowered.noise.module.NoiseModule;

/**
 * Noise module that outputs a constant value.
 *
 * <p>To specify the constant value, call the {@link #setValue(double)} method.</p>
 *
 * <p>This noise module is not useful by itself, but is often used as a source
 * module for other noise modules.</p>
 *
 * @sourceModules 0
 */
public class Const extends NoiseModule {
    public static final double DEFAULT_VALUE = 0;
    private double value = Const.DEFAULT_VALUE;

    public Const() {
        super(0);
    }

    /**
     * Get the constant value for this noise module.
     *
     * @return the constant output value for this noise module.
     */
    public double value() {
        return this.value;
    }

    /**
     * Set the constant value for this noise module.
     *
     * @param value  the constant output value for this noise module.
     */
    public void setValue(final double value) {
        this.value = value;
    }

    @Override
    public double get(final double x, final double y, final double z) {
        return this.value;
    }
}
