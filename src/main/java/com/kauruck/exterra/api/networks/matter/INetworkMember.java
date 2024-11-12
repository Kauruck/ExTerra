package com.kauruck.exterra.api.networks.matter;

import com.kauruck.exterra.api.matter.Matter;
import com.kauruck.exterra.api.matter.MatterStack;
import net.minecraft.core.Direction;

/**
 * A member of a network of all "machines" in a ritual
 * @since 1.0
 */
public interface INetworkMember{
    /**
     * The name of the member.
     * For debugging only
     * @since 1.o
     * @return The name
     */
    String getName();

    /**
     * Weather this member accepts this type of matter.
     * Returns always false, when this member does not accept matter.
     * @param matter The matter
     * @return Weather it is accepted
     * @since 1.0
     */
    boolean acceptsMatter(Matter matter, Direction direction);

    /**
     * Returns an array of all accepted matters.
     * If a matter is in this array, the methode acceptsMatter(matter) must return true for this matter.
     * @return The array.
     * @since 1.0
     */
    Matter[] acceptedMatter();

    /**
     * Pushes matter from the network to the member.
     * The member already accepts this type of matter.
     * @param matterStack The stack to push
     * @return The remainder of the stack, that could not be pushed in the member
     * @since 1.0
     */
    MatterStack pushMatter(MatterStack matterStack, Direction direction);

    /**
     * Returns an array of all matters that could be pulled into the network.
     * If a matter is in this array, the methode pullsMatter(matter) must return true for this matter.
     * @return The array.
     * @since 1.0
     */
    Matter[] pulledMatter(Direction direction);

    /**
     * Weather this matter could be pulled from this member.
     * Returns always false, when no matter can be pulled.
     * @param matter The matter
     * @return Weather the matter could be pulled
     * @since 1.0
     */
    boolean pullsMatter(Matter matter);

    /**
     * Returns all matters that should be pulled into the Network.
     * @param matter The matter to pull
     * @param direction The side to pull from
     * @return All matters
     * @since 1.0
     */
    MatterStack pullMatter(Matter matter, Direction direction);

    /**
     * This functions handles all matters that could not handled by the network.
     * The network expects all supplied matters to be dealt with.
     * @param matters The unhandled matters
     */
    void applyBackpressure(MatterStack[] matters);
}
