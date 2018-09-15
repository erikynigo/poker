package com.poker.model;

import com.poker.metadata.Rank;
import com.poker.metadata.Suit;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests the consistent behavior of a Card object.
 *
 * @author Erik Ynigo
 */
public class CardTest
{
    @Test
    public void testCard()
    {
        Card card = new Card(Rank.ACE, Suit.SPADE);
        Assert.assertEquals(card.getRank(), Rank.ACE);
        Assert.assertEquals(card.getSuit(), Suit.SPADE);
        Assert.assertEquals(card.toString(), Rank.ACE.toString() + Suit.SPADE.toString());
    }
}