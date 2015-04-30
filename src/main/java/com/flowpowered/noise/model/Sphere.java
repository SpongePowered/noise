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
package com.flowpowered.noise.model;

import com.flowpowered.noise.Utils;
import com.flowpowered.noise.exception.NoModuleException;
import com.flowpowered.noise.module.Module;

/**
 * Model that defines the surface of a sphere.
 */
public class Sphere {
    private Module module;

    /**
     * Constructor
     *
     * @param module The noise module that is used to generate the output values.
     */
    public Sphere(Module module) {
        if (module == null) {
            throw new IllegalArgumentException("module cannot be null");
        }
        this.module = module;
    }

    /**
     * Returns the noise module that is used to generate the output values.
     */
    public Module getModule() {
        return module;
    }

    /**
     * Sets the noise module that is used to generate the output values.
     *
     * @param module The noise module that is used to generate the output values.
     *
     * This noise module must exist for the lifetime of this object, until you pass a new noise module to this method.
     */
    public void setModule(Module module) {
        if (module == null) {
            throw new IllegalArgumentException("module cannot be null");
        }
        this.module = module;
    }

    /**
     * Returns the output value from the noise module given the (latitude, longitude) coordinates of the specified input value located on the surface of the sphere.
     *
     * @param lat The latitude of the input value, in degrees.
     * @param lon The longitude of the input value, in degrees.
     * @return The output value from the noise module.
     */
    public double getValue(double lat, double lon) {
        if (module == null) {
            throw new NoModuleException();
        }
        double[] vec = Utils.latLonToXYZ(lat, lon);
        return module.get(vec[0], vec[1], vec[2]);
    }
}
