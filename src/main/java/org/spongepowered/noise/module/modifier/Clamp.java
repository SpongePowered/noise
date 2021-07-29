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

public class Clamp extends Module {
    public static final double DEFAULT_LOWER_BOUND = 0.0;
    public static final double DEFAULT_UPPER_BOUND = 1.0;
    private double lowerBound = Clamp.DEFAULT_LOWER_BOUND;
    private double upperBound = Clamp.DEFAULT_UPPER_BOUND;

    public Clamp() {
        super(1);
    }

    public double getLowerBound() {
        return this.lowerBound;
    }

    public void setLowerBound(final double lowerBound) {
        this.lowerBound = lowerBound;
    }

    public double getUpperBound() {
        return this.upperBound;
    }

    public void setUpperBound(final double upperBound) {
        this.upperBound = upperBound;
    }

    @Override
    public int getSourceModuleCount() {
        return 1;
    }

    @Override
    public double getValue(final double x, final double y, final double z) {
        if (this.sourceModule[0] == null) {
            throw new NoModuleException();
        }

        final double value = this.sourceModule[0].getValue(x, y, z);
        if (value < this.lowerBound) {
            return this.lowerBound;
        } else if (value > this.upperBound) {
            return this.upperBound;
        } else {
            return value;
        }
    }
}
