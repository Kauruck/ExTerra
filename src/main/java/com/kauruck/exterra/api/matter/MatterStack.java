package com.kauruck.exterra.api.matter;

import java.util.Objects;

import static com.kauruck.exterra.fx.MathHelper.clamp;

/**
 * A representation of an amount of matter in the world.
 * @since 1.0
 */
public class MatterStack {

    /**
     * The max Stack size of a matter stack.
     * Immutable.
     * @since 1.0
     */
    public static final int MAX_STACK_SIZE = 64;
    public static final MatterStack EMPTY = new MatterStack(null, 0);
    private Matter matter;
    private int amount;

    public MatterStack(Matter matter, int amount) {
        this.matter = matter;
        this.amount = amount;
    }

    public MatterStack(Matter matter) {
        this.matter = matter;
        this.amount = 0;
    }

    /**
     * Returns the matter of the stack
     * @return The matter
     * @since 1.0
     */
    public Matter getMatter() {
        return matter;
    }

    /**
     * Sets the matter for the stack
     * @param matter The new matter
     * @since 1.0
     */
    public void setMatter(Matter matter) {
        this.matter = matter;
    }

    /**
     * Gets the amount of matter in this stack
     * @return The amount
     * @since 1.0
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the stack.
     * The amount is clamped between 0 and MAX_STACK_SIZE
     * @param amount The new amount
     * @since 1.0
     */
    public void setAmount(int amount) {
        this.amount = clamp(amount, 0, MAX_STACK_SIZE);
    }

    /**
     * Adds an amount to the stack.
     * If the new amount, would go over the stack limit, the function will return the
     * difference between the max stack size and the new amount.
     * The set amount cannot go over the max stack size
     * @param amount The amount to add
     * @return The difference or 0
     * @since 1.0
     */
    public int addMatter(int amount){
        int delta = MAX_STACK_SIZE - (this.amount + amount);
        this.amount += amount;
        this.amount = clamp(this.amount, 0, MAX_STACK_SIZE);
        return Math.max(0, delta);
    }

    /**
     * Removes matter from the stack. If amount of would go under 0, the amount will be set to 0
     * and the difference will be returned.
     * @param amount The amount to remove
     * @return The difference or 0
     * @since 2
     */
    public int removeMatter(int amount){
        int delta = this.amount - amount;
        this.amount -= amount;
        this.amount = clamp(this.amount, 0, MAX_STACK_SIZE);
        return Math.max(0, Math.abs(delta));
    }

    @Override
    public String toString() {
        return this.matter + "(" + this.amount + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MatterStack that = (MatterStack) o;

        return Objects.equals(matter, that.matter) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        int result = matter != null ? matter.hashCode() : 0;
        result = 31 * result + amount;
        return result;
    }
}
