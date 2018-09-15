package com.poker.metadata;

/**
 * Enum representing the possible hand-ranking categories.
 * A numerical value is assigned to each category to be able
 * to determine the strongest hand quickly and objectively.
 * Stronger categories will have a higher numerical value.
 *
 * @author Erik Ynigo
 */
public enum Category
{
    HIGH_CARD(100),
    ONE_PAIR(200),
    TWO_PAIR(300),
    THREE_OF_A_KIND(400),
    STRAIGHT(500),
    FLUSH(600),
    FULL_HOUSE(700),
    FOUR_OF_A_KIND(800),
    STRAIGHT_FLUSH(900);

    private final int value;

    Category(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
