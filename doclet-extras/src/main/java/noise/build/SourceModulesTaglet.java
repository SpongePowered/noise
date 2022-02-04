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
package noise.build;

import com.sun.source.doctree.DocTree;
import com.sun.source.doctree.TextTree;
import com.sun.source.doctree.UnknownBlockTagTree;
import jdk.javadoc.doclet.Taglet;

import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;

public final class SourceModulesTaglet implements Taglet {

    private static final String NAME = "sourceModules";

    public SourceModulesTaglet() {
    }

    @Override
    public Set<Location> getAllowedLocations() {
        return Set.of(Location.TYPE);
    }

    @Override
    public boolean isInlineTag() {
        return false;
    }

    @Override
    public String getName() {
        return SourceModulesTaglet.NAME;
    }

    @Override
    public String toString(final List<? extends DocTree> tags, final Element element) {
        if (tags.size() != 1) {
            throw new IllegalArgumentException("Expected a single-element list");
        }
        final DocTree first = tags.get(0);
        if (first.getKind() != DocTree.Kind.UNKNOWN_BLOCK_TAG) {
            throw new IllegalStateException("Expcted to receive an UnknownBlockTag DocTree, but got " + first.getKind());
        }

        final DocTree unwrapped = ((UnknownBlockTagTree) first).getContent().get(0);
        if (unwrapped.getKind() != DocTree.Kind.TEXT) {
            throw new IllegalArgumentException("The value of a @" + SourceModulesTaglet.NAME + " taglet must be a plain integer");
        }
        final String valueText = ((TextTree) unwrapped).getBody();
        final int value;
        try {
            value = Integer.parseInt(valueText);
        } catch (final NumberFormatException ex) {
            throw new IllegalArgumentException("Provided number " + valueText + " was not an integer", ex);
        }

        final String description = switch (value) {
            case 0 -> "This module does not require any source modules.";
            case 1 -> "This module requires one source module.";
            default -> "This module requires " + value + " source modules.";
        };

        return "<dt>Source Modules</dt><dd>" + description + "</dd>";
    }
}
