package com.poker.model;

import java.util.List;

/**
 * The Hand class is a simple class containing a series of Card objects
 * that represent a hand in a card game. This class places no restrictions
 * regarding the number of cards the hand must have, or whether or not
 * duplicate cards are allowed. That task is left to whatever evaluating
 * service receives the Hand as input, so it can enforce the rules for the
 * game that it is evaluating for.
 *
 * @author Erik Ynigo
 */
public class Hand
{
    private List<Card> cards;

    public Hand(List<Card> cards)
    {
        this.cards = cards;
    }

    /**
     * Returns the list of Card objects that make up this Hand.
     *
     * @return A list of Card objects.
     */
    public List<Card> getCards()
    {
        return cards;
    }
}
