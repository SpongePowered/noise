/*
 * This file is part of Flow Noise, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2013 Flow Powered <https://flowpowered.com/>
 * Original libnoise C++ library by Jason Bevins <http://libnoise.sourceforge.net/>
 * jlibnoise Java port by Garrett Fleenor <https://github.com/RoyAwesome/jlibnoise>
 * Flow Noise is re-licensed with permission from jlibnoise author.
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
package com.flowpowered.noise.module;

import com.flowpowered.noise.exception.NoModuleException;
import com.flowpowered.noise.module.source.Const;

public abstract class Module {
    protected Module[] sourceModule;

    public Module(int sourceModuleCount) {

        // Create an array of pointers to all source modules required by this
        // noise module.  Set these pointers to Const modules.
    	sourceModule = new Module[sourceModuleCount];     
        for (int i = 0; i < sourceModuleCount; i++) {
            sourceModule[i] = new Const();
        }
    }

    public Module getSourceModule(int index) {
        if (index >= getSourceModuleCount() || index < 0 || sourceModule[index] == null) {
            throw new NoModuleException();
        }
        return (sourceModule[index]);
    }

    public void setSourceModule(int index, Module sourceModule) {
        if (this.sourceModule == null) {
            return;
        }
        if (index >= getSourceModuleCount() || index < 0) {
            throw new IllegalArgumentException("Index must be between 0 and GetSourceModuleCount()");
        }
        this.sourceModule[index] = sourceModule;
    }

    public abstract int getSourceModuleCount();

    public abstract double getValue(double x, double y, double z);
}
