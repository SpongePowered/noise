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
package com.flowpowered.noise.module.modifier.transformer;

import com.flowpowered.noise.NoiseQuality;
import com.flowpowered.noise.module.Module;
import com.flowpowered.noise.module.modifier.Modifier;
import com.flowpowered.noise.module.generator.Perlin;

public class Turbulence extends Modifier {
    // The power (scale) of the displacement.
    private final double power;
    // Noise module that displaces the @a x coordinate.
    private final Perlin xDistortModule;
    // Noise module that displaces the @a y coordinate.
    private final Perlin yDistortModule;
    // Noise module that displaces the @a z coordinate.
    private final Perlin zDistortModule;

    public Turbulence(Module source, double power, int roughness, double frequency, int seed) {
        super(source);
        this.power = power;
        // TODO use the perlin builder class instead of these magic values
        xDistortModule = new Perlin(roughness, frequency, 2.0, 0.5, NoiseQuality.STANDARD, seed);
        yDistortModule = new Perlin(roughness, frequency, 2.0, 0.5, NoiseQuality.STANDARD, seed + 1);
        zDistortModule = new Perlin(roughness, frequency, 2.0, 0.5, NoiseQuality.STANDARD, seed + 2);
    }

    public double getPower() {
        return power;
    }

    public int getRoughness() {
        return xDistortModule.getOctaveCount();
    }

    public double getFrequency() {
        return xDistortModule.getFrequency();
    }

    public int getSeed() {
        return xDistortModule.getSeed();
    }

    @Override
    public double get(double x, double y, double z) {
        // Get the values from the three noise::module::Perlin noise modules and
        // add each value to each coordinate of the input value.  There are also
        // some offsets added to the coordinates of the input values.  This prevents
        // the distortion modules from returning zero if the (x, y, z) coordinates,
        // when multiplied by the frequency, are near an integer boundary.  This is
        // due to a property of gradient coherent noise, which returns zero at
        // integer boundaries.
        double x0, y0, z0;
        double x1, y1, z1;
        double x2, y2, z2;
        x0 = x + (12414.0 / 65536.0);
        y0 = y + (65124.0 / 65536.0);
        z0 = z + (31337.0 / 65536.0);
        x1 = x + (26519.0 / 65536.0);
        y1 = y + (18128.0 / 65536.0);
        z1 = z + (60493.0 / 65536.0);
        x2 = x + (53820.0 / 65536.0);
        y2 = y + (11213.0 / 65536.0);
        z2 = z + (44845.0 / 65536.0);
        double xDistort = x + (xDistortModule.get(x0, y0, z0) * power);
        double yDistort = y + (yDistortModule.get(x1, y1, z1) * power);
        double zDistort = z + (zDistortModule.get(x2, y2, z2) * power);

        // Retrieve the output value at the offset input value instead of the
        // original input value.
        return source.get(xDistort, yDistort, zDistort);
    }

}
