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
package net.royawesome.jlibnoise.module;

import net.royawesome.jlibnoise.exception.NoModuleException;

public abstract class Module {
    protected Module[] SourceModule;

    public Module(int sourceModuleCount) {
        SourceModule = null;

        // Create an array of pointers to all source modules required by this
        // noise module.  Set these pointers to NULL.
        if (sourceModuleCount > 0) {
            SourceModule = new Module[sourceModuleCount];
            for (int i = 0; i < sourceModuleCount; i++) {
                SourceModule[i] = null;
            }
        } else {
            SourceModule = null;
        }
    }

    public Module getSourceModule(int index) {
        if (index >= GetSourceModuleCount() || index < 0 || SourceModule[index] == null) {
            throw new NoModuleException();
        }
        return (SourceModule[index]);
    }

    public void SetSourceModule(int index, Module sourceModule) {
        if (SourceModule == null) {
            return;
        }
        if (index >= GetSourceModuleCount() || index < 0) {
            throw new IllegalArgumentException("Index must be between 0 and GetSourceMoudleCount()");
        }
        SourceModule[index] = sourceModule;
    }

    public abstract int GetSourceModuleCount();

    public abstract double GetValue(double x, double y, double z);
}
