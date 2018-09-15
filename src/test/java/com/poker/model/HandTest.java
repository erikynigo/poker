package com.poker.model;

import com.poker.TestConstants;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests the consistent behavior of a Hand object.
 *
 * @author Erik Ynigo
 */
public class HandTest
{
    @Test
    public void testHand()
    {
        List<Card> cards = new ArrayList<Card>() {{
            add(TestConstants.TWO_OF_DIAMONDS);
            add(TestConstants.THREE_OF_CLUBS);
            add(TestConstants.FOUR_OF_SPADES);
            add(TestConstants.FIVE_OF_HEARTS);
            add(TestConstants.SIX_OF_DIAMONDS);
        }};

        Hand hand = new Hand(cards);
        Assert.assertEquals(hand.getCards(), cards);
        Assert.assertEquals(hand.getCards().size(), 5);
    }
}