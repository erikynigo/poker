package com.poker.service;

import com.poker.TestConstants;
import com.poker.exceptions.CannotEvaluateException;
import com.poker.exceptions.IllegalHandException;
import com.poker.metadata.Category;
import com.poker.metadata.Rank;
import com.poker.model.Card;
import com.poker.model.Hand;
import com.poker.model.PokerHandEvaluation;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
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
    private PokerHandEvaluator evaluator;

    @BeforeClass
    private void initialize()
    {
        evaluator = new PokerHandEvaluator();
    }

    // TESTS REGARDING INPUTS

    @Test(expectedExceptions = CannotEvaluateException.class)
    public void testNullInput() throws CannotEvaluateException, IllegalHandException
    {
        evaluator.evaluate(null);
    }

    @Test(expectedExceptions = CannotEvaluateException.class)
    public void testOneHandInput() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.ACE_HIGH_HAND);
        }};

        evaluator.evaluate(hands);
    }

    @Test(expectedExceptions = IllegalHandException.class)
    public void testHandWithNullCardsInput() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.ACE_HIGH_HAND);
            add(TestConstants.INVALID_HAND_NULL_CARDS);
        }};

        evaluator.evaluate(hands);
    }

    @Test(expectedExceptions = IllegalHandException.class)
    public void testHandWithNoCardsInput() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.ACE_HIGH_HAND);
            add(TestConstants.INVALID_HAND_NO_CARDS);
        }};

        evaluator.evaluate(hands);
    }

    @Test(expectedExceptions = IllegalHandException.class)
    public void testHandWithLessCardsInput() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.ACE_HIGH_HAND);
            add(TestConstants.INVALID_HAND_LESS_THAN_5_CARDS);
        }};

        evaluator.evaluate(hands);
    }

    @Test(expectedExceptions = IllegalHandException.class)
    public void testHandWithMoreCardsInput() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.ACE_HIGH_HAND);
            add(TestConstants.INVALID_HAND_MORE_THAN_5_CARDS);
        }};

        evaluator.evaluate(hands);
    }

    @Test(expectedExceptions = IllegalHandException.class)
    public void testHandWithDuplicateCardsInput() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.ACE_HIGH_HAND);
            add(TestConstants.INVALID_HAND_DUPLICATE_CARDS);
        }};

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

        List<PokerHandEvaluation> winners = evaluator.evaluate(hands);
        PokerHandEvaluation winner = winners.get(0);

        Assert.assertEquals(winners.size(), 1, "Only one winner.");
        Assert.assertEquals(winner.category, Category.STRAIGHT_FLUSH, "Straight flush beats four of a kind.");
    }

    // TEST REGARDING HANDS OF THE SAME CATEGORY
    @Test
    public void testEqualHands() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.KING_HIGH_HAND);
            add(TestConstants.KING_HIGH_HAND);
        }};

        List<PokerHandEvaluation> winners = evaluator.evaluate(hands);
        PokerHandEvaluation winner = winners.get(0);

        Assert.assertEquals(winners.size(), 2, "Hands are equal, both are winners.");
        Assert.assertEquals(winner.category, Category.HIGH_CARD, "Hands are equal and should tie.");
    }

    @Test
    public void testSameHighCardDifferentKickers() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.ACE_HIGH_1_HAND); // [A][J][8][5][2]
            add(TestConstants.ACE_HIGH_2_HAND); // [A][J][8][3][3]
        }};

        List<PokerHandEvaluation> winners = evaluator.evaluate(hands);
        PokerHandEvaluation winner = winners.get(0);

        Assert.assertEquals(winners.size(), 1, "Only one winner.");
        Assert.assertEquals(winner.category, Category.HIGH_CARD, "Stringest category is HIGH_CARD");

        Card lastKicker = winner.kickers.get(winner.kickers.size()-1);
        Assert.assertEquals(lastKicker.getRank(), Rank.THREE, "[3] kicker beats [2] kicker.");
    }

    @Test
    public void testSameOnePairDifferentKickers() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.ONE_PAIR_1_HAND); // [10][10][A][K][Q]
            add(TestConstants.ONE_PAIR_2_HAND); // [10][10][A][K][J]
        }};

        List<PokerHandEvaluation> winners = evaluator.evaluate(hands);
        PokerHandEvaluation winner = winners.get(0);

        Assert.assertEquals(winners.size(), 1, "Only one winner.");
        Assert.assertEquals(winner.category, Category.ONE_PAIR, "Strongest category is ONE_PAIR.");

        Card lastKicker = winner.kickers.get(winner.kickers.size()-1);
        Assert.assertEquals(lastKicker.getRank(), Rank.QUEEN, "[Q] kicker beats [J] kicker.");
    }

    @Test
    public void testSameTwoPairDifferentKickers() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.TWO_PAIR_1_HAND); // [10][10][9][9][5]
            add(TestConstants.TWO_PAIR_2_HAND); // [10][10][9][9][K]
        }};

        List<PokerHandEvaluation> winners = evaluator.evaluate(hands);
        PokerHandEvaluation winner = winners.get(0);

        Assert.assertEquals(winners.size(), 1, "Only one winner.");
        Assert.assertEquals(winner.category, Category.TWO_PAIR, "Strongest category is TWO_PAIR.");

        Card lastKicker = winner.kickers.get(winner.kickers.size()-1);
        Assert.assertEquals(lastKicker.getRank(), Rank.KING, "[K] kicker beats [5] kicker.");
    }

    @Test
    public void testSameThreeOfAKindDifferentKickers() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.THREE_OF_A_KIND_1_HAND); // [2][2][2][Q][10]
            add(TestConstants.THREE_OF_A_KIND_2_HAND); // [2][2][2][J][10]
        }};

        List<PokerHandEvaluation> winners = evaluator.evaluate(hands);
        PokerHandEvaluation winner = winners.get(0);

        Assert.assertEquals(winners.size(), 1, "Only one winner.");
        Assert.assertEquals(winner.category, Category.THREE_OF_A_KIND, "Strongest category is THREE_OF_A_KIND.");

        Card secondToLastKicker = winner.kickers.get(winner.kickers.size()-2);
        Assert.assertEquals(secondToLastKicker.getRank(), Rank.QUEEN, "[Q] kicker beats [J] kicker.");
    }

    @Test
    public void testTwoDifferentStraights() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.STRAIGHT_1_HAND); // [J][10][9][8][7]
            add(TestConstants.STRAIGHT_2_HAND); // [5][4][3][2][A]
        }};

        List<PokerHandEvaluation> winners = evaluator.evaluate(hands);
        PokerHandEvaluation winner = winners.get(0);

        Assert.assertEquals(winners.size(), 1, "Only one winner.");
        Assert.assertEquals(winner.category, Category.STRAIGHT, "Strongest category is STRAIGHT.");

        Card firstKicker = winner.kickers.get(0);
        Assert.assertEquals(firstKicker.getRank(), Rank.JACK, "[J] high straight beats [5] high straight.");
    }

    @Test
    public void testTwoDifferentFlush() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.FLUSH_1_HAND); // [A][6][5][3][2]
            add(TestConstants.FLUSH_2_HAND); // [K][Q][J][10][3]
        }};

        List<PokerHandEvaluation> winners = evaluator.evaluate(hands);
        PokerHandEvaluation winner = winners.get(0);

        Assert.assertEquals(winners.size(), 1, "Only one winner.");
        Assert.assertEquals(winner.category, Category.FLUSH, "Strongest category is FLUSH.");

        Card lastKicker = winner.kickers.get(0);
        Assert.assertEquals(lastKicker.getRank(), Rank.ACE, "[A] high flush beats [K] high flush.");
    }

    @Test
    public void testTwoDifferentFullHouse() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.FULL_HOUSE_1_HAND); // [2][2][2][A][A]
            add(TestConstants.FULL_HOUSE_2_HAND); // [2][2][2][4][4]
        }};

        List<PokerHandEvaluation> winners = evaluator.evaluate(hands);
        PokerHandEvaluation winner = winners.get(0);

        Assert.assertEquals(winners.size(), 1, "Only one winner.");
        Assert.assertEquals(winner.category, Category.FULL_HOUSE, "Strongest category is FULL_HOUSE.");

        Card lastKicker = winner.kickers.get(winner.kickers.size()-1);
        Assert.assertEquals(lastKicker.getRank(), Rank.ACE, "[A] high kicker beats [4] high kicker.");
    }

    @Test
    public void testSameFourOfAKindDifferentKicker() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.FOUR_OF_A_KIND_1_HAND); // [J][J][J][J][5]
            add(TestConstants.FOUR_OF_A_KIND_2_HAND); // [J][J][J][J][Q]
        }};

        List<PokerHandEvaluation> winners = evaluator.evaluate(hands);
        PokerHandEvaluation winner = winners.get(0);

        Assert.assertEquals(winners.size(), 1, "Only one winner.");
        Assert.assertEquals(winner.category, Category.FOUR_OF_A_KIND, "Strongest category is FOUR_OF_A_KIND.");

        Card lastKicker = winner.kickers.get(winner.kickers.size()-1);
        Assert.assertEquals(lastKicker.getRank(), Rank.QUEEN, "[Q] high kicker beats [5] high kicker.");
    }

    @Test
    public void testDifferentStraightFlush() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.STRAIGHT_FLUSH_1_HAND); // [A][K][Q][J][10]
            add(TestConstants.STRAIGHT_FLUSH_2_HAND); // [5][4][3][2][A]
        }};

        List<PokerHandEvaluation> winners = evaluator.evaluate(hands);
        PokerHandEvaluation winner = winners.get(0);

        Assert.assertEquals(winners.size(), 1, "Only one winner.");
        Assert.assertEquals(winner.category, Category.STRAIGHT_FLUSH, "Strongest category is STRAIGHT_FLUSH.");

        Card firstKicker = winner.kickers.get(0);
        Assert.assertEquals(firstKicker.getRank(), Rank.ACE, "[A] high straight flush beats [5] high straight flush.");
    }

    @Test
    public void testEveryTypeOfHand() throws CannotEvaluateException, IllegalHandException
    {
        List<Hand> hands = new ArrayList<Hand>() {{
            add(TestConstants.ACE_HIGH_1_HAND);
            add(TestConstants.ONE_PAIR_1_HAND);
            add(TestConstants.TWO_PAIR_1_HAND);
            add(TestConstants.THREE_OF_A_KIND_1_HAND);
            add(TestConstants.STRAIGHT_1_HAND);
            add(TestConstants.FLUSH_1_HAND);
            add(TestConstants.FULL_HOUSE_1_HAND);
            add(TestConstants.FOUR_OF_A_KIND_1_HAND);
            add(TestConstants.STRAIGHT_FLUSH_1_HAND);
        }};

        List<PokerHandEvaluation> winners = evaluator.evaluate(hands);
        PokerHandEvaluation winner = winners.get(0);

        Assert.assertEquals(winners.size(), 1, "Only one winner.");
        Assert.assertEquals(winner.category, Category.STRAIGHT_FLUSH, "Strongest category is STRAIGHT_FLUSH.");
    }
}