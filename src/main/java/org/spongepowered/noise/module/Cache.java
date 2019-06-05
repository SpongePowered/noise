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
package org.spongepowered.noise.module;

import org.spongepowered.noise.exception.NoModuleException;

public class Cache extends Module {
    // The cached output value at the cached input value.
    private double cachedValue;
    // Determines if a cached output value is stored in this noise
    // module.
    private boolean isCached = false;
    // @a x coordinate of the cached input value.
    private double xCache;
    // @a y coordinate of the cached input value.
    private double yCache;
    // @a z coordinate of the cached input value.
    private double zCache;

    public Cache() {
        super(1);
    }

    @Override
    public int getSourceModuleCount() {
        return 1;
    }

    @Override
    public void setSourceModule(int index, Module sourceModule) {
        super.setSourceModule(index, sourceModule);
        isCached = false;
    }

    @Override
    public double getValue(double x, double y, double z) {
        if (sourceModule[0] == null) {
            throw new NoModuleException();
        }

        if (!(isCached && x == xCache && y == yCache && z == zCache)) {
            cachedValue = sourceModule[0].getValue(x, y, z);
            xCache = x;
            yCache = y;
            zCache = z;
        }
        isCached = true;
        return cachedValue;
    }
}
