package com.poker.model;

import com.poker.metadata.Rank;
import com.poker.metadata.Suit;

/**
 * The Card class represents a standard card of a given rank and suit.
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

    public Rank getRank()
    {
        return rank;
    }

    public Suit getSuit()
    {
        return suit;
    }

    /**
     * Returns a unique string for any of the possible Cards in a standard
     * 52-card deck. Useful for checking Card duplicates in a Hand.
     *
     * @return A String representing the card's specific rank and
     *         suit combination.
     */
    public String toString()
    {
        return rank.toString() + suit.toString();
    }
}
