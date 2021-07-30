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

/**
 * Noise module that outputs Voronoi cells.
 *
 * <p>In mathematics, a <em>Voronoi cell</em> is a region containing all the
 * points that are closer to a specific <em>seed point</em> than to any other
 * seed point. These cells mesh with one another, producing polygon-like
 * formations.</p>
 *
 * <p>By default, this noise module randomly places a seed point within each
 * unit cube. By modifying the <em>frequency</em> of the seed points, an
 * application can change the distance between seed points. The higher the
 * frequency, the closer together this noise module places the seed points,
 * which reduces the size of the cells. To specify the frequency of the cells,
 * call the {@link #setFrequency(double)} method.</p>
 *
 * <p>This noise module assigns each Voronoi cell with a random constant value
 * from a coherent-noise function. The <em>displacement value</em> controls the
 * range of random values to assign to each cell. The range of random values is
 * +/- the displacement value. Call the {@link #setDisplacement(double)} method
 * to specify the displacement value.</p>
 *
 * <p>To modify the random positions of the seed points, call the
 * {@link #setSeed(int)} method.</p>
 *
 * <p>This noise module can optionally add the distance from the nearest seed
 * to the output value. To enable this feature, call the
 * {@link #setEnableDistance(boolean)} method. This causes the points in the
 * Voronoi cells to increase in value the further away that point is from the
 * nearest seed point.</p>
 *
 * <p>Voronoi cells are often used to generate cracked-mud terrain
 * formations or crystal-like textures.</p>
 *
 * @sourceModules 0
 */
public class Voronoi extends Module {
    private static final double SQRT_3 = 1.7320508075688772935;
    /**
     * Default displacement to apply to each cell for the {@link Voronoi}
     * noise module.
     */
    public static final double DEFAULT_VORONOI_DISPLACEMENT = 1.0;

    /**
     * Default frequency of the seed points for the {@link Voronoi}
     * noise module.
     */
    public static final double DEFAULT_VORONOI_FREQUENCY = 1.0;

    /**
     * Default seed of the noise function for the {@link Voronoi}
     * noise module.
     */
    public static final int DEFAULT_VORONOI_SEED = 0;
    // Scale of the random displacement to apply to each Voronoi cell.
    private double displacement = Voronoi.DEFAULT_VORONOI_DISPLACEMENT;
    // Determines if the distance from the nearest seed point is applied to
    // the output value.
    private boolean enableDistance = false;
    // Frequency of the seed points.
    private double frequency = Voronoi.DEFAULT_VORONOI_FREQUENCY;
    // Seed value used by the coherent-noise function to determine the
    // positions of the seed points.
    private int seed = Voronoi.DEFAULT_VORONOI_SEED;

    public Voronoi() {
        super(0);
    }

    /**
     * Get the displacement value of the Voronoi cells.
     *
     * <p>This noise module assigns each Voronoi cell with a random constant
     * value from a coherent-noise function. The <em>displacement value</em>
     * controls the range of random values to assign to each cell. The range of
     * random values is +/- the displacement value.</p>
     *
     * @return the displacement value of the Voronoi cells
     * @see #DEFAULT_VORONOI_DISPLACEMENT
     */
    public double getDisplacement() {
        return this.displacement;
    }

    /**
     * Set the displacement value of the Voronoi cells.
     *
     * <p>This noise module assigns each Voronoi cell with a random constant
     * value from a coherent-noise function. The <em>displacement value</em>
     * controls the range of random values to assign to each cell. The range of
     * random values is +/- the displacement value.</p>
     *
     * @param displacement  the displacement value of the Voronoi cells
     */
    public void setDisplacement(final double displacement) {
        this.displacement = displacement;
    }

    /**
     * Get if the distance from the nearest seed point is applied to
     * the output value.
     *
     * <p>Applying the distance from the nearest seed point to the output value
     * causes the points in the Voronoi cells to increase in value the further
     * away that point is from the nearest seed point.</p>
     *
     * @return  whether to apply the distance to the output value or not
     */
    public boolean isEnableDistance() {
        return this.enableDistance;
    }

    /**
     * Enables or disables applying the distance from the nearest seed point to
     * the output value.
     *
     * <p>Applying the distance from the nearest seed point to the output value
     * causes the points in the Voronoi cells to increase in value the further
     * away that point is from the nearest seed point. Setting this value to
     * {@code true} (and setting the displacement to a near-zero value) causes
     * this noise module to generate cracked mud formations.</p>
     *
     * @param enableDistance whether to apply the distance to the output
     *     value or not
     */
    public void setEnableDistance(final boolean enableDistance) {
        this.enableDistance = enableDistance;
    }

    /**
     * Get the frequency of the seed points.
     *
     * <p>The frequency determines the size of the Voronoi cells and the
     * distance between these cells.</p>
     *
     * @return the frequency of the seed points
     */
    public double getFrequency() {
        return this.frequency;
    }

    /**
     * Set the frequency of the seed points.
     *
     * <p>The frequency determines the size of the Voronoi cells and the
     * distance between these cells.</p>
     *
     * @param frequency the frequency of the seed points
     */
    public void setFrequency(final double frequency) {
        this.frequency = frequency;
    }

    /**
     * Get the seed value used by the Voronoi cells.
     *
     * <p>The positions of the seed values are calculated by a coherent-noise
     * function. By modifying the seed value, the output of that function
     * changes.</p>
     *
     * @return the seed value
     */
    public int getSeed() {
        return this.seed;
    }

    /**
     * Set the seed value used by the Voronoi cells.
     *
     * <p>The positions of the seed values are calculated by a coherent-noise
     * function. By modifying the seed value, the output of that function
     * changes.</p>
     *
     * @param seed the seed value
     */
    public void setSeed(final int seed) {
        this.seed = seed;
    }

    @Override
    public double getValue(final double x, final double y, final double z) {
        double x1 = x;
        double y1 = y;
        double z1 = z;
        // This method could be more efficient by caching the seed values.  Fix
        // later.

        x1 *= this.frequency;
        y1 *= this.frequency;
        z1 *= this.frequency;

        final int xInt = (x1 > 0.0 ? (int) x1 : (int) x1 - 1);
        final int yInt = (y1 > 0.0 ? (int) y1 : (int) y1 - 1);
        final int zInt = (z1 > 0.0 ? (int) z1 : (int) z1 - 1);

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
                    final double xPos = xCur + Noise.valueNoise3D(xCur, yCur, zCur, this.seed);
                    final double yPos = yCur + Noise.valueNoise3D(xCur, yCur, zCur, this.seed + 1);
                    final double zPos = zCur + Noise.valueNoise3D(xCur, yCur, zCur, this.seed + 2);
                    final double xDist = xPos - x1;
                    final double yDist = yPos - y1;
                    final double zDist = zPos - z1;
                    final double dist = xDist * xDist + yDist * yDist + zDist * zDist;

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

        final double value;
        if (this.enableDistance) {
            // Determine the distance to the nearest seed point.
            final double xDist = xCandidate - x1;
            final double yDist = yCandidate - y1;
            final double zDist = zCandidate - z1;
            value = Math.sqrt(xDist * xDist + yDist * yDist + zDist * zDist) / Voronoi.SQRT_3;
        } else {
            value = 0.0;
        }

        // Return the calculated distance with the displacement value applied.
        return value + (this.displacement * Noise.valueNoise3D(Utils.floor(xCandidate), Utils.floor(yCandidate), Utils.floor(zCandidate), this.seed));
    }
}
