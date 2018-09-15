package com.poker.metadata;

/**
 * Enum representing the possible ranks that a standard
 * card, in a standard 52-card deck, can represent.
 * A numerical value is assigned to each Rank to be able
 * to determine the strongest Rank quickly and objectively.
 * Stronger Ranks will have a higher numerical value.
 *
 * @author Erik Ynigo
 */
public enum Rank
{
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(11),
    QUEEN(12),
    KING(13),
    ACE(14);

    private final int value;

    Rank(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
