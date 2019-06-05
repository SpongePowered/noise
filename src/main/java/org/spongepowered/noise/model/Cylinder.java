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
package org.spongepowered.noise.model;

import org.spongepowered.noise.exception.NoModuleException;
import org.spongepowered.noise.module.Module;

/**
 * Model that defines the surface of a cylinder.
 */
public class Cylinder {
    private Module module;

    /**
     * @param mod The noise module that is used to generate the output values.
     */
    public Cylinder(Module mod) {
        this.module = mod;
    }

    /**
     * Returns the noise module that is used to generate the output values.
     *
     * @return A reference to the noise module.
     */
    public Module getModule() {
        return this.module;
    }

    /**
     * Sets the noise module that is used to generate the output values.
     *
     * @param mod The noise module that is used to generate the output values.
     * <p/>
     * This noise module must exist for the lifetime of this object, until you pass a new noise module to this method.
     */
    public void setModule(Module mod) {
        if (mod == null) {
            throw new IllegalArgumentException("Mod cannot be null");
        }
        this.module = mod;
    }

    /**
     * Returns the output value from the noise module given the (angle, height) coordinates of the specified input value located on the surface of the cylinder.
     *
     * @param angle The angle around the cylinder's center, in degrees.
     * @param height The height along the @a y axis.
     * @return The output value from the noise module.
     */
    public double getValue(double angle, double height) {
        if (module == null) {
            throw new NoModuleException();
        }

        double x, y, z;
        x = Math.cos(Math.toRadians(angle));
        y = height;
        z = Math.sin(Math.toRadians(angle));
        return module.getValue(x, y, z);
    }
}
