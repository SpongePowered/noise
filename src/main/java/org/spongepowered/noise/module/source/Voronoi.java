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
package org.spongepowered.noise.module.source;

import org.spongepowered.noise.Noise;
import org.spongepowered.noise.Utils;
import org.spongepowered.noise.module.Module;

public class Voronoi extends Module {
    private static final double SQRT_3 = 1.7320508075688772935;
    // Default displacement to apply to each cell for the
    // noise::module::Voronoi noise module.
    public static final double DEFAULT_VORONOI_DISPLACEMENT = 1.0;
    // Default frequency of the seed points for the noise::module::Voronoi
    // noise module.
    public static final double DEFAULT_VORONOI_FREQUENCY = 1.0;
    // Default seed of the noise function for the noise::module::Voronoi
    // noise module.
    public static final int DEFAULT_VORONOI_SEED = 0;
    // Scale of the random displacement to apply to each Voronoi cell.
    private double displacement = DEFAULT_VORONOI_DISPLACEMENT;
    // Determines if the distance from the nearest seed point is applied to
    // the output value.
    private boolean enableDistance = false;
    // Frequency of the seed points.
    private double frequency = DEFAULT_VORONOI_FREQUENCY;
    // Seed value used by the coherent-noise function to determine the
    // positions of the seed points.
    private int seed = DEFAULT_VORONOI_SEED;

    public Voronoi() {
        super(0);
    }

    public double getDisplacement() {
        return displacement;
    }

    public void setDisplacement(double displacement) {
        this.displacement = displacement;
    }

    public boolean isEnableDistance() {
        return enableDistance;
    }

    public void setEnableDistance(boolean enableDistance) {
        this.enableDistance = enableDistance;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    @Override
    public int getSourceModuleCount() {
        return 0;
    }

    @Override
    public double getValue(double x, double y, double z) {
        double x1 = x;
        double y1 = y;
        double z1 = z;
        // This method could be more efficient by caching the seed values.  Fix
        // later.

        x1 *= frequency;
        y1 *= frequency;
        z1 *= frequency;

        int xInt = (x1 > 0.0 ? (int) x1 : (int) x1 - 1);
        int yInt = (y1 > 0.0 ? (int) y1 : (int) y1 - 1);
        int zInt = (z1 > 0.0 ? (int) z1 : (int) z1 - 1);

        double minDist = 2147483647.0;
        double xCandidate = 0;
        double yCandidate = 0;
        double zCandidate = 0;

        // Inside each unit cube, there is a seed point at a random position.  Go
        // through each of the nearby cubes until we find a cube with a seed point
        // that is closest to the specified position.
        for (int zCur = zInt - 2; zCur <= zInt + 2; zCur++) {
            for (int yCur = yInt - 2; yCur <= yInt + 2; yCur++) {
                for (int xCur = xInt - 2; xCur <= xInt + 2; xCur++) {

                    // Calculate the position and distance to the seed point inside of
                    // this unit cube.
                    double xPos = xCur + Noise.valueNoise3D(xCur, yCur, zCur, seed);
                    double yPos = yCur + Noise.valueNoise3D(xCur, yCur, zCur, seed + 1);
                    double zPos = zCur + Noise.valueNoise3D(xCur, yCur, zCur, seed + 2);
                    double xDist = xPos - x1;
                    double yDist = yPos - y1;
                    double zDist = zPos - z1;
                    double dist = xDist * xDist + yDist * yDist + zDist * zDist;

                    if (dist < minDist) {
                        // This seed point is closer to any others found so far, so record
                        // this seed point.
                        minDist = dist;
                        xCandidate = xPos;
                        yCandidate = yPos;
                        zCandidate = zPos;
                    }
                }
            }
        }

        double value;
        if (enableDistance) {
            // Determine the distance to the nearest seed point.
            double xDist = xCandidate - x1;
            double yDist = yCandidate - y1;
            double zDist = zCandidate - z1;
            value = Math.sqrt(xDist * xDist + yDist * yDist + zDist * zDist) / SQRT_3;
        } else {
            value = 0.0;
        }

        // Return the calculated distance with the displacement value applied.
        return value + (displacement * Noise.valueNoise3D(Utils.floor(xCandidate), Utils.floor(yCandidate), Utils.floor(zCandidate), seed));
    }
}
