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
package com.flowpowered.noise.module.transformer;

import com.flowpowered.noise.exception.NoModuleException;
import com.flowpowered.noise.module.Modifier;
import com.flowpowered.noise.module.Module;

public class Displace extends Modifier {

    private final Module displaceX;
    private final Module displaceY;
    private final Module displaceZ;

    public Displace(Module source, Module displaceX, Module displaceY, Module displaceZ) {
        super(source);
        this.displaceX = displaceX;
        this.displaceY = displaceY;
        this.displaceZ = displaceZ;
    }

    @Override
    public double get(double x, double y, double z) {

        // Get the output values from the three displacement modules.  Add each
        // value to the corresponding coordinate in the input value.
        double xDisplace = x + displaceX.get(x, y, z);
        double yDisplace = y + displaceY.get(x, y, z);
        double zDisplace = z + displaceZ.get(x, y, z);

        // Retrieve the output value using the offset input value instead of
        // the original input value.
        return source.get(xDisplace, yDisplace, zDisplace);
    }

}
