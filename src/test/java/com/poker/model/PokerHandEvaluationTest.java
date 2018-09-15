package com.poker.model;

import com.poker.TestConstants;
import com.poker.metadata.Category;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests the consistent behavior of a PokerHandEvaluation object.
 *
 * @author Erik Ynigo
 */
public class PokerHandEvaluationTest
{
    @Test
    public void testPokerHandEvaluation()
    {
        List<Card> cards = new ArrayList<Card>() {{
            add(TestConstants.ACE_OF_HEARTS);
            add(TestConstants.KING_OF_HEARTS);
            add(TestConstants.QUEEN_OF_HEARTS);
            add(TestConstants.JACK_OF_HEARTS);
            add(TestConstants.TEN_OF_HEARTS);
        }};

        Hand hand = new Hand(cards);

        PokerHandEvaluation evaluation = new PokerHandEvaluation();
        evaluation.hand = hand;
        evaluation.category = Category.STRAIGHT_FLUSH;
        evaluation.kickers = cards;
        Assert.assertEquals(evaluation.getScore(), TestConstants.HIGHEST_SCORE);
    }
}