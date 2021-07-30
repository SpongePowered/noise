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
package org.spongepowered.noise;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;


public class NoiseTest {
    @Test
    public void test() throws IOException {

        // Euclidean norm check
        for (int i = 0; i < Utils.RANDOM_VECTORS.length >> 2; i++) {
            final double gradientMagnitudeSquared =
                    (Utils.RANDOM_VECTORS[i << 2] * Utils.RANDOM_VECTORS[i << 2])
                    + (Utils.RANDOM_VECTORS[(i << 2) + 1] * Utils.RANDOM_VECTORS[(i << 2) + 1])
                    + (Utils.RANDOM_VECTORS[(i << 2) + 2] * Utils.RANDOM_VECTORS[(i << 2) + 2]);
            final int finalI = i;
            Assertions.assertEquals(gradientMagnitudeSquared, 1.0, 0.00001,
                () -> "Gradient[i=" + finalI + "] Euclidean Norm, " + gradientMagnitudeSquared + ", { " + Utils.RANDOM_VECTORS[finalI << 2]
                    + ", " + Utils.RANDOM_VECTORS[(finalI << 2) + 1] + ", " + Utils.RANDOM_VECTORS[(finalI << 2) + 2] + " }");
        }

        // Rudimentary Perlin range test.
        // Fails when normalized for the actual maximum, rather than for the theoretical sqrt(3) for if arbitrary gradient directions were possible.
        // It would be more difficult to test the actual maximum in here. In favor of better-normalized noise, I loosened up the condition, instead
        // of normalizing the gradient set by the theoretical sqrt(3). ~K.jpg
        for (int i = 0; i < Utils.RANDOM_VECTORS.length >> 2; i++) {
            final double gradientMaximumRampedValue =
                    Math.abs(Utils.RANDOM_VECTORS_PERLIN[i << 2])
                    +  Math.abs(Utils.RANDOM_VECTORS_PERLIN[(i << 2) + 1])
                    +  Math.abs(Utils.RANDOM_VECTORS_PERLIN[(i << 2) + 2]);
            // Would be 1.0 if normalized under the assumption that any gradient direction were possible.
            Assertions.assertTrue(gradientMaximumRampedValue <= 1.01, "Gradient[i=" + i + "] Perlin Range, " + gradientMaximumRampedValue
                + ", { " + Utils.RANDOM_VECTORS[i << 2] + ", " + Utils.RANDOM_VECTORS[(i << 2) + 1] + ", " + Utils.RANDOM_VECTORS[(i << 2) + 2] + " }");
        }

        /*
        final int width = 2048, height = 2048;
        final double xPeriod = 128, yPeriod = 128;
        final Module module = new Voronoi();
        ((Voronoi) module).setEnableDistance(true);
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_GRAY);
        final short[] data = ((DataBufferUShort) image.getRaster().getDataBuffer()).getData();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                final double noise = module.getValue(x / xPeriod, y / yPeriod, 0) / 2;
                data[y * width + x] = (short) (noise * 65_535);
            }
        }
        ImageIO.write(image, "PNG", new File("noise.png"));
        */
    }
}
