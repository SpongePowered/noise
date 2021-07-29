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

public abstract class Module {
    private static final Module[] EMPTY_MODULE_ARRAY = new Module[0];

    protected Module[] sourceModule;

    public Module(final int sourceModuleCount) {
        if (sourceModuleCount > 0) {
            this.sourceModule = new Module[sourceModuleCount];
        } else {
            this.sourceModule = Module.EMPTY_MODULE_ARRAY;
        }
    }

    public Module getSourceModule(final int index) {
        if (index >= this.sourceModule.length || index < 0 || this.sourceModule[index] == null) {
            throw new NoModuleException(index);
        }
        return (this.sourceModule[index]);
    }

    public void setSourceModule(final int index, final Module sourceModule) {
        if (this.sourceModule.length == 0) {
            return;
        }
        if (index >= this.sourceModule.length || index < 0) {
            throw new IllegalArgumentException("Index must be between 0 and " + this.sourceModule.length);
        }
        this.sourceModule[index] = sourceModule;
    }

    public final int getSourceModuleCount() {
        return this.sourceModule.length;
    }

    public abstract double getValue(double x, double y, double z);
}
