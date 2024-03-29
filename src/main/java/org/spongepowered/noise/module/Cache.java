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

/**
 * Noise module that caches the last output value generated by a source module.
 *
 * <p>If an application passes an input value to the
 * {@link #get(double, double, double)} method that differs from the
 * previously passed-in input value, this noise module instructs the source
 * module to calculate the output value. This value, as well as the
 * {@code (x, y, z)} coordinates of the input value, are stored (cached) in this
 * noise module.</p>
 *
 * <p>If the application passes an input value to the
 * {@link #get(double, double, double)} method that is equal to the
 * previously passed-in input values, this noise module returns the cached
 * output value without having the source module recalculate the output value.</p>
 *
 * <p>If an application passes a new source module to the
 * {@link #setSourceModule(int, NoiseModule)} method, the cache is invalidated.</p>
 *
 * <p>Caching a noise module is useful if it is used as a source module for
 * multiple noise modules. If a source module is not cached, the source module
 * will redundantly calculate the same output value once for each noise module
 * in which it is included.</p>
 *
 * <p>This noise module requires one source module.</p>
 */
public class Cache extends NoiseModule {
    // The cached output value at the cached input value.
    private double cachedValue;
    // Determines if a cached output value is stored in this noise
    // module.
    private boolean isCached = false;
    // {@code x} coordinate of the cached input value.
    private double xCache;
    // {@code y} coordinate of the cached input value.
    private double yCache;
    // {@code z} coordinate of the cached input value.
    private double zCache;

    public Cache() {
        super(1);
    }

    /**
     * Create a new Cache module with the source module pre-configured.
     *
     * @param source the module to cache
     */
    public Cache(final NoiseModule source) {
        this();
        this.setSourceModule(0, source);
    }


    @Override
    public void setSourceModule(final int index, final NoiseModule sourceModule) {
        super.setSourceModule(index, sourceModule);
        this.isCached = false;
    }

    @Override
    public double get(final double x, final double y, final double z) {
        if (this.sourceModule[0] == null) {
            throw new NoModuleException(0);
        }

        if (!(this.isCached && x == this.xCache && y == this.yCache && z == this.zCache)) {
            this.cachedValue = this.sourceModule[0].get(x, y, z);
            this.xCache = x;
            this.yCache = y;
            this.zCache = z;
        }
        this.isCached = true;
        return this.cachedValue;
    }
}
