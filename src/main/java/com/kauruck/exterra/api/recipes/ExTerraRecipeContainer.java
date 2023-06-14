package com.kauruck.exterra.api.recipes;

import java.util.Collection;

public interface ExTerraRecipeContainer<T> extends Iterable<T>{

    /**
     * @return Returns the number of Objects in the Container
     */
    int getSize();

    /**
     * Returns an empty version of the Object of this container
     * @return The empty
     */
    T getEmpty();

    /**
     * Returns weather this container is ordered.
     * - If it is ordered, the container must implement getAt(index)
     *  and ensure the order is always the same, for the same inputs.
     *  Further more getAll() must return a collection that is ordered, e.g. a List
     * - If it is not ordered, the container may not implement the  getAt(index) methode
     *  or if it does so, may not guarantee the order of the Objects in the Container, but it must not return an Object twice.
     *  Further more getAll() should return an unordered Collection, e.g. a Set
     * @return
     */
    boolean isOrdered();

    /**
     * Gets the object at the index.
     * If the Container is unordered, the order is not guaranteed.
     * If the Container is unordered, use this methode with caution, it may not be implemented.
     * @throws UnsupportedOperationException may be thrown when the container is unordered
     * @throws IndexOutOfBoundsException When the index is bigger then the size of the container
     * @param index The index of the Object
     * @return The Object at that index
     */
    T getAt(int index);

    /**
     * Gets all Objects in the Container.
     * When the container is ordered, this is an ordered Collection, if not mentioned otherwise a List.
     * Whe the container is unordered, this is an unordered Collection, if not mentioned otherwise a Set.
     * @return Collection of all Objects.
     */
    Collection<T> getAll();
}
