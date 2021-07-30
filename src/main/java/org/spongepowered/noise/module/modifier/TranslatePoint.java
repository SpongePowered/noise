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

import org.spongepowered.noise.exception.NoModuleException;
import org.spongepowered.noise.module.Module;

/**
 * Noise module that moves the coordinates of the input value before returning
 * the output value from a source module.
 *
 * <p>The {@link #getValue(double, double, double)} method moves the
 * {@code (x, y, z)} coordinates of the input value by a translation amount
 * before returning the output value from the source module. To set the
 * translation amount, call the {@link #setTranslations(double, double, double)}
 * method. To set the translation amount to apply to the individual {@code x},
 * {@code y}, or {@code z} coordinates, call the
 * {@link #setXTranslation(double)}, {@link #setYTranslation(double)},
 * or {@link #setZTranslation(double)} methods, respectively.</p>
 *
 * @sourceModules 1
 */
public class TranslatePoint extends Module {

    /**
     * Default translation factor applied to the {@code x} coordinate for the
     * {@link TranslatePoint} noise module.
     */
    public static final double DEFAULT_TRANSLATE_POINT_X = 0.0;

    /**
     * Default translation factor applied to the {@code y} coordinate for the
     * {@link TranslatePoint} noise module.
     */
    public static final double DEFAULT_TRANSLATE_POINT_Y = 0.0;

    /**
     * Default translation factor applied to the {@code z} coordinate for the
     * {@link TranslatePoint} noise module.
     */
    public static final double DEFAULT_TRANSLATE_POINT_Z = 0.0;

    // Translation amount applied to the {@code x} coordinate of the input
    // value.
    private double xTranslation = TranslatePoint.DEFAULT_TRANSLATE_POINT_X;
    // Translation amount applied to the {@code y} coordinate of the input
    // value.
    private double yTranslation = TranslatePoint.DEFAULT_TRANSLATE_POINT_Y;
    // Translation amount applied to the {@code z} coordinate of the input
    // value.
    private double zTranslation = TranslatePoint.DEFAULT_TRANSLATE_POINT_Z;

    public TranslatePoint() {
        super(1);
    }

    /**
     * Get the translation amount to apply to the {@code x} coordinate of the
     * input value.
     *
     * @return the translation amount to apply to the {@code x} coordinate
     * @see #DEFAULT_TRANSLATE_POINT_X
     */
    public double getXTranslation() {
        return this.xTranslation;
    }

    /**
     * Set the translation amount to apply to the {@code x} coordinate of the
     * input value.
     *
     * <p>The {@link #getValue(double, double, double)} method moves the
     * {@code (x, y, z)} coordinates of the input value by a translation amount
     * before returning the output value from the source module.</p>
     *
     * @param xTranslation the translation amount to apply to the {@code x} coordinate
     */
    public void setXTranslation(final double xTranslation) {
        this.xTranslation = xTranslation;
    }

    /**
     * Get the translation amount to apply to the {@code y} coordinate of the
     * input value.
     *
     * @return the translation amount to apply to the {@code y} coordinate
     * @see #DEFAULT_TRANSLATE_POINT_Y
     */
    public double getYTranslation() {
        return this.yTranslation;
    }

    /**
     * Set the translation amount to apply to the {@code y} coordinate of the
     * input value.
     *
     * <p>The {@link #getValue(double, double, double)} method moves the
     * {@code (x, y, z)} coordinates of the input value by a translation amount
     * before returning the output value from the source module.</p>
     *
     * @param yTranslation the translation amount to apply to the {@code y} coordinate
     */
    public void setYTranslation(final double yTranslation) {
        this.yTranslation = yTranslation;
    }

    /**
     * Get the translation amount to apply to the {@code z} coordinate of the
     * input value.
     *
     * @return the translation amount to apply to the {@code z} coordinate
     * @see #DEFAULT_TRANSLATE_POINT_Y
     */
    public double getZTranslation() {
        return this.zTranslation;
    }

    /**
     * Set the translation amount to apply to the {@code z} coordinate of the
     * input value.
     *
     * <p>The {@link #getValue(double, double, double)} method moves the
     * {@code (x, y, z)} coordinates of the input value by a translation amount
     * before returning the output value from the source module.</p>
     *
     * @param zTranslation the translation amount to apply to the {@code z} coordinate
     */
    public void setZTranslation(final double zTranslation) {
        this.zTranslation = zTranslation;
    }

    /**
     * Set the translation amount to apply to the {@code (x, y, z)} coordinates
     * of the input value.
     *
     * <p>The {@link #getValue(double, double, double)} method moves the
     * {@code (x, y, z)} coordinates of the input value by a translation amount
     * before returning the output value from the source module.</p>
     *
     * @param x the translation amount to apply to the {@code x} coordinate
     * @param y the translation amount to apply to the {@code y} coordinate
     * @param z the translation amount to apply to the {@code z} coordinate
     */
    public void setTranslations(final double x, final double y, final double z) {
        this.setXTranslation(x);
        this.setYTranslation(y);
        this.setZTranslation(z);
    }

    @Override
    public double getValue(final double x, final double y, final double z) {
        if (this.sourceModule[0] == null) {
            throw new NoModuleException(0);
        }

        return this.sourceModule[0].getValue(x + this.xTranslation, y + this.yTranslation, z + this.zTranslation);
    }
}
