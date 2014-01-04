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
package net.royawesome.jlibnoise.module.combiner;

import net.royawesome.jlibnoise.exception.NoModuleException;
import net.royawesome.jlibnoise.module.Module;

public class Displace extends Module {
    public Displace() {
        super(4);
    }

    @Override
    public int GetSourceModuleCount() {
        return 4;
    }

    public Module GetXDisplaceModule() {
        if (SourceModule == null || SourceModule[1] == null) {
            throw new NoModuleException();
        }
        return SourceModule[1];
    }

    public Module GetYDisplaceModule() {
        if (SourceModule == null || SourceModule[2] == null) {
            throw new NoModuleException();
        }
        return SourceModule[2];
    }

    public Module GetZDisplaceModule() {
        if (SourceModule == null || SourceModule[3] == null) {
            throw new NoModuleException();
        }
        return SourceModule[3];
    }

    public void SetXDisplaceModule(Module x) {
        if (x == null) {
            throw new IllegalArgumentException("x cannot be null");
        }
        SourceModule[1] = x;
    }

    public void SetYDisplaceModule(Module y) {
        if (y == null) {
            throw new IllegalArgumentException("y cannot be null");
        }
        SourceModule[2] = y;
    }

    public void SetZDisplaceModule(Module z) {
        if (z == null) {
            throw new IllegalArgumentException("z cannot be null");
        }
        SourceModule[3] = z;
    }

    public void SetDisplaceModules(Module x, Module y, Module z) {
        SetXDisplaceModule(x);
        SetYDisplaceModule(y);
        SetZDisplaceModule(z);
    }

    @Override
    public double GetValue(double x, double y, double z) {
        if (SourceModule[0] == null) {
            throw new NoModuleException();
        }
        if (SourceModule[1] == null) {
            throw new NoModuleException();
        }
        if (SourceModule[2] == null) {
            throw new NoModuleException();
        }
        if (SourceModule[3] == null) {
            throw new NoModuleException();
        }

        // Get the output values from the three displacement modules.  Add each
        // value to the corresponding coordinate in the input value.
        double xDisplace = x + (SourceModule[1].GetValue(x, y, z));
        double yDisplace = y + (SourceModule[2].GetValue(x, y, z));
        double zDisplace = z + (SourceModule[3].GetValue(x, y, z));

        // Retrieve the output value using the offsetted input value instead of
        // the original input value.
        return SourceModule[0].GetValue(xDisplace, yDisplace, zDisplace);
    }
}
