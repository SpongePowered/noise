package net.royawesome.jlibnoise.util;

/**
 * A utility class for builders.
 *
 * @param <B> the type to be built
 */
public interface Builder<B> {

    /**
     * Build the object
     *
     * @return the built object of type B
     */
    public B build();

}
