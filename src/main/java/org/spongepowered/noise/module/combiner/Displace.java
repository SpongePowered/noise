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
package org.spongepowered.noise.module.combiner;

import org.spongepowered.noise.exception.NoModuleException;
import org.spongepowered.noise.module.Module;

public class Displace extends Module {
    public Displace() {
        super(4);
    }

    @Override
    public int getSourceModuleCount() {
        return 4;
    }

    public Module getXDisplaceModule() {
        if (sourceModule == null || sourceModule[1] == null) {
            throw new NoModuleException();
        }
        return sourceModule[1];
    }

    public Module getYDisplaceModule() {
        if (sourceModule == null || sourceModule[2] == null) {
            throw new NoModuleException();
        }
        return sourceModule[2];
    }

    public Module getZDisplaceModule() {
        if (sourceModule == null || sourceModule[3] == null) {
            throw new NoModuleException();
        }
        return sourceModule[3];
    }

    public void setXDisplaceModule(Module x) {
        if (x == null) {
            throw new IllegalArgumentException("x cannot be null");
        }
        sourceModule[1] = x;
    }

    public void setYDisplaceModule(Module y) {
        if (y == null) {
            throw new IllegalArgumentException("y cannot be null");
        }
        sourceModule[2] = y;
    }

    public void setZDisplaceModule(Module z) {
        if (z == null) {
            throw new IllegalArgumentException("z cannot be null");
        }
        sourceModule[3] = z;
    }

    public void setDisplaceModules(Module x, Module y, Module z) {
        setXDisplaceModule(x);
        setYDisplaceModule(y);
        setZDisplaceModule(z);
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
        if (sourceModule[3] == null) {
            throw new NoModuleException();
        }

        // Get the output values from the three displacement modules.  Add each
        // value to the corresponding coordinate in the input value.
        double xDisplace = x + sourceModule[1].getValue(x, y, z);
        double yDisplace = y + sourceModule[2].getValue(x, y, z);
        double zDisplace = z + sourceModule[3].getValue(x, y, z);

        // Retrieve the output value using the offset input value instead of
        // the original input value.
        return sourceModule[0].getValue(xDisplace, yDisplace, zDisplace);
    }
}
