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
package net.royawesome.jlibnoise.module.selector;

import net.royawesome.jlibnoise.util.MathUtils;
import net.royawesome.jlibnoise.exception.NoModuleException;
import net.royawesome.jlibnoise.module.Module;

public class Blend extends Module {
    public Blend() {
        super(3);
    }

    public Module getControlModule() {
        if (sourceModule[2] == null) {
            throw new NoModuleException();
        }
        return sourceModule[2];
    }

    public void setControlModule(Module module) {
        if (module == null) {
            throw new IllegalArgumentException("Control Module cannot be null");
        }
        sourceModule[2] = module;
    }

    @Override
    public int getSourceModuleCount() {
        return 3;
    }

    @Override
    public double getValue(double x, double y, double z) {
        if (sourceModule[0] == null) {
            throw new NoModuleException();
        }
        if (sourceModule[1] == null) {
            throw new NoModuleException();
        }
        if (sourceModule[2] == null) {
            throw new NoModuleException();
        }

        double v0 = sourceModule[0].getValue(x, y, z);
        double v1 = sourceModule[1].getValue(x, y, z);
        double alpha = (sourceModule[2].getValue(x, y, z) + 1.0) / 2.0;
        return MathUtils.linearInterp(v0, v1, alpha);
    }
}
