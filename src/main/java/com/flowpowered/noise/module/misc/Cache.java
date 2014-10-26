/*
 * This file is part of Flow Noise, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2013 Spout LLC <http://www.spout.org/>
 * Original libnoise in C++ by Jason Bevins <http://libnoise.sourceforge.net/>
 * jlibnoise Java port by Garrett Fleenor <https://github.com/RoyAwesome/jlibnoise>
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
package com.flowpowered.noise.module.misc;

import com.flowpowered.noise.exception.NoModuleException;
import com.flowpowered.noise.module.Modifier;
import com.flowpowered.noise.module.Module;

public class Cache extends Modifier {
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

    public Cache(Module source) {
        super(source);
    }

    @Override
    public double get(double x, double y, double z) {
        if (!(isCached && x == xCache && y == yCache && z == zCache)) {
            cachedValue = source.get(x, y, z);
            xCache = x;
            yCache = y;
            zCache = z;
        }
        isCached = true;
        return cachedValue;
    }
}
