package com.poker.model;

import com.poker.metadata.Rank;
import com.poker.metadata.Suit;

/**
 * The Card class represents a standard card of a given Rank and Suit.
 *
 * @author Erik Ynigo
 */
public class Card
{
    private Rank rank;
    private Suit suit;

    public Card(Rank rank, Suit suit)
    {
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * Returns the Rank that this Card object represents.
     *
     * @return A Rank object.
     */
    public Rank getRank()
    {
        return rank;
    }

    /**
     * Returns the Suit that this Card object bears.
     *
     * @return A Suit object.
     */
    public Suit getSuit()
    {
        return suit;
    }

    /**
     * Returns a unique string for any of the possible Cards in a standard
     * 52-card deck. Useful for checking Card duplicates in a Hand.
     *
     * @return A String representing the card's specific Rank and
     *         Suit combination.
     */
    public String toString()
    {
        return rank.toString() + suit.toString();
    }
}
