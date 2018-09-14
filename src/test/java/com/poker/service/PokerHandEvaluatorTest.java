package com.poker.service;

import com.poker.TestConstants;
import com.poker.exceptions.CannotEvaluateException;
import com.poker.exceptions.IllegalHandException;
import com.poker.metadata.Category;
import com.poker.model.Hand;
import com.poker.model.PokerHandEvaluation;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests the proper behavior of the PokerHandEvaluator by throwing a
 * variety of cases and verifying the expected outcome. Cases include:
 * Null hands, hands with no cards, hands with less or more cards than
 * allowed, hands with duplicated cards, proper hand strength ranking,
 * and proper edge cases using kicker cards as tie breakers.
 */
public class PokerHandEvaluatorTest
{
    // TESTS REGARDING INPUTS

    @Test(expectedExceptions = CannotEvaluateException.class)
    public void testNullInput() throws CannotEvaluateException, IllegalHandException
    {
        PokerHandEvaluator evaluator = new PokerHandEvaluator();
        evaluator.evaluate(null);
    }

    @Test(expectedExceptions = CannotEvaluateException.class)
    public void testOneHandInput() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.ACE_HIGH_HAND);
        }};

        PokerHandEvaluator evaluator = new PokerHandEvaluator();
        evaluator.evaluate(hands);
    }

    @Test(expectedExceptions = IllegalHandException.class)
    public void testHandWithNullCardsInput() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.ACE_HIGH_HAND);
            add(TestConstants.INVALID_HAND_NULL_CARDS);
        }};

        PokerHandEvaluator evaluator = new PokerHandEvaluator();
        evaluator.evaluate(hands);
    }

    @Test(expectedExceptions = IllegalHandException.class)
    public void testHandWithNoCardsInput() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.ACE_HIGH_HAND);
            add(TestConstants.INVALID_HAND_NO_CARDS);
        }};

        PokerHandEvaluator evaluator = new PokerHandEvaluator();
        evaluator.evaluate(hands);
    }

    @Test(expectedExceptions = IllegalHandException.class)
    public void testHandWithLessCardsInput() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.ACE_HIGH_HAND);
            add(TestConstants.INVALID_HAND_LESS_THAN_5_CARDS);
        }};

        PokerHandEvaluator evaluator = new PokerHandEvaluator();
        evaluator.evaluate(hands);
    }

    @Test(expectedExceptions = IllegalHandException.class)
    public void testHandWithMoreCardsInput() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.ACE_HIGH_HAND);
            add(TestConstants.INVALID_HAND_MORE_THAN_5_CARDS);
        }};

        PokerHandEvaluator evaluator = new PokerHandEvaluator();
        evaluator.evaluate(hands);
    }

    @Test(expectedExceptions = IllegalHandException.class)
    public void testHandWithDuplicateCardsInput() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.ACE_HIGH_HAND);
            add(TestConstants.INVALID_HAND_DUPLICATE_CARDS);
        }};

        PokerHandEvaluator evaluator = new PokerHandEvaluator();
        evaluator.evaluate(hands);
    }

    // TESTS REGARDING HAND STRENGTH RANKING

    @Test
    public void testPairOverHighCard() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.ACE_HIGH_HAND);
            add(TestConstants.PAIR_OF_FIVES_HAND);
        }};

        PokerHandEvaluator evaluator = new PokerHandEvaluator();
        List<PokerHandEvaluation> winners = evaluator.evaluate(hands);
        PokerHandEvaluation winner = winners.get(0);

        Assert.assertEquals(winners.size(), 1, "Only one winner.");
        Assert.assertEquals(winner.category, Category.ONE_PAIR, "One pair beats Ace high.");
    }

    @Test
    public void testTwoPairOverPair() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.PAIR_OF_FIVES_HAND);
            add(TestConstants.PAIRS_OF_JACKS_AND_EIGHTS_HAND);
        }};

        PokerHandEvaluator evaluator = new PokerHandEvaluator();
        List<PokerHandEvaluation> winners = evaluator.evaluate(hands);
        PokerHandEvaluation winner = winners.get(0);

        Assert.assertEquals(winners.size(), 1, "Only one winner.");
        Assert.assertEquals(winner.category, Category.TWO_PAIR, "Two pair beats one pair.");
    }

    @Test
    public void testThreeOfAKindOverTwoPair() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.THREE_SIXES_HAND);
            add(TestConstants.PAIRS_OF_JACKS_AND_EIGHTS_HAND);
        }};

        PokerHandEvaluator evaluator = new PokerHandEvaluator();
        List<PokerHandEvaluation> winners = evaluator.evaluate(hands);
        PokerHandEvaluation winner = winners.get(0);

        Assert.assertEquals(winners.size(), 1, "Only one winner.");
        Assert.assertEquals(winner.category, Category.THREE_OF_A_KIND, "Three of a kind beats two pair.");
    }

    @Test
    public void testStraightOverThreeOfAKind() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.THREE_SIXES_HAND);
            add(TestConstants.SIX_HIGH_STRAIGHT_HAND);
        }};

        PokerHandEvaluator evaluator = new PokerHandEvaluator();
        List<PokerHandEvaluation> winners = evaluator.evaluate(hands);
        PokerHandEvaluation winner = winners.get(0);

        Assert.assertEquals(winners.size(), 1, "Only one winner.");
        Assert.assertEquals(winner.category, Category.STRAIGHT, "Straight beats three of a kind.");
    }

    @Test
    public void testFlushOverStraight() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.JACK_HIGH_FLUSH_HAND);
            add(TestConstants.SIX_HIGH_STRAIGHT_HAND);
        }};

        PokerHandEvaluator evaluator = new PokerHandEvaluator();
        List<PokerHandEvaluation> winners = evaluator.evaluate(hands);
        PokerHandEvaluation winner = winners.get(0);

        Assert.assertEquals(winners.size(), 1, "Only one winner.");
        Assert.assertEquals(winner.category, Category.FLUSH, "Flush beats straight.");
    }

    @Test
    public void testFullHouseOverFlush() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.JACK_HIGH_FLUSH_HAND);
            add(TestConstants.KING_HIGH_FULL_HOUSE_HAND);
        }};

        PokerHandEvaluator evaluator = new PokerHandEvaluator();
        List<PokerHandEvaluation> winners = evaluator.evaluate(hands);
        PokerHandEvaluation winner = winners.get(0);

        Assert.assertEquals(winners.size(), 1, "Only one winner.");
        Assert.assertEquals(winner.category, Category.FULL_HOUSE, "Full house beats flush.");
    }

    @Test
    public void testFourOfAKindOverFullHouse() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.FOUR_FOURS_HAND);
            add(TestConstants.KING_HIGH_FULL_HOUSE_HAND);
        }};

        PokerHandEvaluator evaluator = new PokerHandEvaluator();
        List<PokerHandEvaluation> winners = evaluator.evaluate(hands);
        PokerHandEvaluation winner = winners.get(0);

        Assert.assertEquals(winners.size(), 1, "Only one winner.");
        Assert.assertEquals(winner.category, Category.FOUR_OF_A_KIND, "Four of a kind beats full house.");
    }

    @Test
    public void testStraightFlushOverFourOfAKind() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.FOUR_FOURS_HAND);
            add(TestConstants.NINE_HIGH_STRAIGHT_FLUSH_HAND);
        }};

        PokerHandEvaluator evaluator = new PokerHandEvaluator();
        List<PokerHandEvaluation> winners = evaluator.evaluate(hands);
        PokerHandEvaluation winner = winners.get(0);

        Assert.assertEquals(winners.size(), 1, "Only one winner.");
        Assert.assertEquals(winner.category, Category.STRAIGHT_FLUSH, "Straight flush beats four of a kind.");
    }

    // TEST REGARDING SAME HAND CATEGORY WITH KICKERS INVOLVED

    
}