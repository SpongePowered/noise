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
package org.spongepowered.noise.module.modifier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.spongepowered.noise.module.source.Const;

public class RangeTest {

    @Test
    public void testRangeSanity() {
        final Const constVal = new Const();
        constVal.setValue(0.75);

        final Range range = new Range(constVal);
        range.setBounds(0.5, 1f, 1f, 2f);
        // Arbitrary
        Assertions.assertEquals(1.5, range.get(35, 41, 1), 0f);
    }

    @Test
    public void testRangeIllegalLowersSetter() {
        final Range range = new Range();

        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> range.setBounds(1, 1, 0.5, 1)
        );
    }

    @Test
    public void testRangeIllegalUppersSetter() {
        final Range range = new Range();

        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> range.setBounds(0, 1, 1, 1)
        );
    }

    @Test
    public void testRangeLegalBoundsSetter() {
        final Range range = new Range();
        range.setBounds(0, 1, 0.5, 1);
    }

}
