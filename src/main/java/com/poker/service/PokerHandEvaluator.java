package com.poker.service;

import com.poker.exceptions.CannotEvaluateException;
import com.poker.exceptions.IllegalHandException;
import com.poker.metadata.Category;
import com.poker.metadata.GameConstants;
import com.poker.metadata.Rank;
import com.poker.metadata.Suit;
import com.poker.model.Card;
import com.poker.model.Hand;
import com.poker.model.PokerHandEvaluation;

import java.util.*;

/**
 * The PokerHandEvaluator class will take two or more Hand objects,
 * and will determine the strongest hand based on standard
 * poker hand ranking. For more information on categories and
 * hand strength, see:
 * <a href="https://en.wikipedia.org/wiki/List_of_poker_hands">
 * List of poker hands</a>
 *
 * @author Erik Ynigo
 */
public class PokerHandEvaluator
{
    public PokerHandEvaluator()
    {

    }

    public List<PokerHandEvaluation> evaluate(List<Hand> hands) throws CannotEvaluateException,
        IllegalHandException
    {
        // Make sure at least two hands are available to compare against each other
        if (hands == null || hands.size() < 2)
        {
            throw new CannotEvaluateException("At least two hands are required " +
                "to determine a winning hand.");
        }

        // Make sure all hands are valid poker hands
        for (Hand hand : hands)
        {
            List<Card> cards = hand.getCards();

            if (cards == null || cards.size() != GameConstants.CARDS_IN_HAND)
            {
                throw new IllegalHandException("Hand must contain exactly "
                    + GameConstants.CARDS_IN_HAND + " cards.");
            }

            if (hasDuplicates(cards))
            {
                throw new IllegalHandException("Hand must not contain duplicate cards.");
            }
        }

        // Hands are valid poker hands. Store all hands of the same score together in a map, where:
        // key is the score achieved, and
        // value is the list containing all PokerHandEvaluation objects with the same score
        int totalHands = hands.size();
        Map<Integer, List<PokerHandEvaluation>> scoreToEvaluationsMap = new HashMap<Integer, List<PokerHandEvaluation>>(totalHands);

        // Highest score will be the key to retrieve the list of winners from the map
        Integer highestScore = 0;

        for (Hand hand : hands)
        {
            PokerHandEvaluation evaluation = evaluate(hand);
            Integer score = evaluation.getScore();

            // New score, create list for it
            if (!scoreToEvaluationsMap.containsKey(score)) {
                scoreToEvaluationsMap.put(score, new ArrayList<PokerHandEvaluation>(totalHands));
            }

            // Fetch list, add new evaluation to it
            List<PokerHandEvaluation> evaluationList = scoreToEvaluationsMap.get(score);
            evaluationList.add(evaluation);

            // Keep track of the highest score to retrieve the proper list at the end
            if (score > highestScore)
            {
                highestScore = score;
            }
        }

        // Return winner(s)
        return scoreToEvaluationsMap.get(highestScore);
    }

    /**
     * Given a Hand, it will determine the highest ranking Category that the
     * hand achieved. A transfer object is returned where it contains the
     * Hand evaluated, the Category achieved, the highest Rank relative to
     * the category achieved, and a unique score representing the hand that
     * can be used to quickly compare Hands against each other.
     *
     * @param hand The Hand object to evaluate.
     *
     * @return A PokerHandEvaluation object containing the Hand evaluated, the
     *         Category achieved, the highest Rank, and a unique score.
     */
    private PokerHandEvaluation evaluate(Hand hand)
    {
        List<Card> cards = hand.getCards();
        int totalCards = cards.size();

        Map<Rank, Integer> rankFrequency = new HashMap<Rank, Integer>(totalCards);
        Map<Suit, Integer> suitFrequency = new HashMap<Suit, Integer>(totalCards);

        // Generate frequency tables
        for (Card card : cards)
        {
            // Rank frequency can help us narrow down the possible hand categories
            // contained in a Hand. E.g., If there are 2 entries for rank, then
            // the categories FOUR_OF_A_KIND and FULL_HOUSE are now possible, while
            // TWO_PAIR, STRAIGHT, and THREE_OF_A_KIND are no longer possible.
            Rank rank = card.getRank();
            int newRankFrequency = rankFrequency.containsKey(rank) ? rankFrequency.get(rank) + 1 : 1;
            rankFrequency.put(rank, newRankFrequency);

            // Suit frequency can also help narrow down the possible hand categories.
            // E.g., if there is only one entry, then we know right away that the only
            // possible categories now would be FLUSH or STRAIGHT_FLUSH.
            Suit suit = card.getSuit();
            int newSuitFrequency = suitFrequency.containsKey(suit) ? suitFrequency.get(suit) + 1 : 1;
            suitFrequency.put(suit, newSuitFrequency);
        }

        // Evaluate possible hands, from highest to lowest. Stop when evaluation is not
        // null, as it will contain the highest ranked hand category the Hand contains
        // Note that the order of Category evaluation is of extreme importance, as later
        // evaluating functions (for weaker hands) will make assumptions that if the hand
        // was a stronger hand, it would not have reached those weaker functions.
        PokerHandEvaluation evaluation = null;

        // Straight flush
        evaluation = evaluateStraightFlush(hand, rankFrequency, suitFrequency);

        if (evaluation != null)
        {
            return evaluation;
        }

        // Four of a kind
        evaluation = evaluateFourOfAKind(hand, rankFrequency);

        if (evaluation != null)
        {
            return evaluation;
        }

        // Full house
        evaluation = evaluateFullHouse(hand, rankFrequency);

        if (evaluation != null)
        {
            return evaluation;
        }

        // Flush
        evaluation = evaluateFlush(hand, suitFrequency);

        if (evaluation != null)
        {
            return evaluation;
        }

        // Straight
        evaluation = evaluateStraight(hand, rankFrequency);

        if (evaluation != null)
        {
            return evaluation;
        }

        // Three of a kind
        evaluation = evaluateThreeOfAKind(hand, rankFrequency);

        if (evaluation != null)
        {
            return evaluation;
        }

        // Two pairs
        evaluation = evaluateTwoPair(hand, rankFrequency);

        if (evaluation != null)
        {
            return evaluation;
        }

        // One pair
        evaluation = evaluateOnePair(hand, rankFrequency);

        if (evaluation != null)
        {
            return evaluation;
        }

        // High card (always returns an evaluation, as it's the base case)
        return evaluateHighCard(hand);
    }

    /**
     * Determines whether or not the Hand represents the category STRAIGHT_FLUSH.
     *
     * @param hand          The Hand object containing the Card list that will
     *                      be used to determine if it represents the STRAIGHT_FLUSH
     *                      category.
     * @param rankFrequency Map associating the Rank with the number of times
     *                      it appears in the hand.
     * @param suitFrequency Map associating the Suit with the number of times
     *                      it appears in the hand.
     *
     * @return A PokerHandEvaluation object containing the results of the evaluation
     *         if the Hand represents the STRAIGHT_FLUSH category, null otherwise.
     */
    private PokerHandEvaluation evaluateStraightFlush(Hand hand, Map<Rank, Integer> rankFrequency,
                                                      Map<Suit, Integer> suitFrequency)
    {
        // The fingerprint for a STRAIGHT_FLUSH hand is very particular:
        //      - 5 entries in the rank frequency map
        //      - 1 entry in the suit frequency map
        if (rankFrequency.size() != 5 || suitFrequency.size() != 1)
        {
            return null;
        }

        // Next, we need to sort the cards in ascending order, based on their rank value,
        // and verify that the ranks are sequential.
        List<Card> cards = hand.getCards();
        sortAscending(cards);

        if (!isSequential(cards))
        {
            return null;
        }

        PokerHandEvaluation evaluation = new PokerHandEvaluation();
        evaluation.hand = hand;
        evaluation.category = Category.STRAIGHT_FLUSH;
        evaluation.highestRank = cards.get(cards.size() - 1).getRank();

        return evaluation;
    }

    /**
     * Determines whether or not the Hand represents the category FOUR_OF_A_KIND.
     *
     * @param hand          The Hand object containing the Card list that will
     *                      be used to determine if it represents the FOUR_OF_A_KIND
     *                      category.
     * @param rankFrequency Map associating the Rank with the number of times
     *                      it appears in the hand.
     *
     * @return A PokerHandEvaluation object containing the results of the evaluation
     *         if the Hand represents the FOUR_OF_A_KIND category, null otherwise.
     */
    private PokerHandEvaluation evaluateFourOfAKind(Hand hand, Map<Rank, Integer> rankFrequency)
    {
        // The fingerprint for a FOUR_OF_A_KIND hand is 2 entries in the rank frequency map.
        // This fingerprint is not unique, as the following hands have it:
        //      - FOUR_OF_A_KIND
        //      - FULL_HOUSE
        if (rankFrequency.size() != 2)
        {
            return null;
        }

        // Search for the rank with a frequency of 4. If it's not found, this
        // is a FULL_HOUSE hand.
        Rank highestRank = null;

        for (Map.Entry<Rank, Integer> entry : rankFrequency.entrySet())
        {
            Rank rank = entry.getKey();
            int frequency = entry.getValue();

            if (frequency == 4)
            {
                highestRank = rank;
                break;
            }
        }

        if (highestRank == null)
        {
            return null;
        }

        PokerHandEvaluation evaluation = new PokerHandEvaluation();
        evaluation.hand = hand;
        evaluation.category = Category.FOUR_OF_A_KIND;
        evaluation.highestRank = highestRank;

        return evaluation;
    }

    /**
     * Determines whether or not the Hand represents the category FULL_HOUSE.
     *
     * @param hand          The Hand object containing the Card list that will
     *                      be used to determine if it represents the FULL_HOUSE
     *                      category.
     * @param rankFrequency Map associating the Rank with the number of times
     *                      it appears in the hand.
     *
     * @return A PokerHandEvaluation object containing the results of the evaluation
     *         if the Hand represents the FULL_HOUSE category, null otherwise.
     */
    private PokerHandEvaluation evaluateFullHouse(Hand hand, Map<Rank, Integer> rankFrequency)
    {
        // The fingerprint for a FULL_HOUSE hand is 2 entries in the rank frequency map.
        // This fingerprint is not unique, as the following hands have it:
        //      - FOUR_OF_A_KIND
        //      - FULL_HOUSE
        // Note however that if this were a FOUR_OF_A_KIND hand, it would not have
        // reached this weaker hand evaluating function.
        if (rankFrequency.size() != 2)
        {
            return null;
        }

        // Verify that the rank frequency of a card is not 4, otherwise this hand
        // would be a FOUR_OF_A_KIND.
        Rank highestRank = null;

        for (Map.Entry<Rank, Integer> entry : rankFrequency.entrySet())
        {
            Rank rank = entry.getKey();
            int frequency = entry.getValue();

            if (frequency == 3)
            {
                highestRank = rank;
                break;
            }
        }

        PokerHandEvaluation evaluation = new PokerHandEvaluation();
        evaluation.hand = hand;
        evaluation.category = Category.FULL_HOUSE;
        evaluation.highestRank = highestRank;

        return evaluation;
    }

    /**
     * Determines whether or not the Hand represents the category FLUSH.
     *
     * @param hand          The Hand object containing the Card list that will
     *                      be used to determine if it represents the FLUSH
     *                      category.
     * @param suitFrequency Map associating the Suit with the number of times
     *                      it appears in the hand.
     *
     * @return A PokerHandEvaluation object containing the results of the evaluation
     *         if the Hand represents the FLUSH category, null otherwise.
     */
    private PokerHandEvaluation evaluateFlush(Hand hand, Map<Suit, Integer> suitFrequency)
    {
        // A FLUSH should have one entry in the suit frequency map only,
        // as all cards must be of the same suit.
        if (suitFrequency.size() > 1)
        {
            return null;
        }

        PokerHandEvaluation evaluation = new PokerHandEvaluation();
        evaluation.hand = hand;
        evaluation.category = Category.FLUSH;
        evaluation.highestRank = findHighestRank(hand.getCards());;

        return evaluation;
    }

    /**
     * Determines whether or not the Hand represents the category STRAIGHT.
     *
     * @param hand          The Hand object containing the Card list that will
     *                      be used to determine if it represents the STRAIGHT
     *                      category.
     * @param rankFrequency Map associating the Rank with the number of times
     *                      it appears in the hand.
     *
     * @return A PokerHandEvaluation object containing the results of the evaluation
     *         if the Hand represents the STRAIGHT category, null otherwise.
     */
    private PokerHandEvaluation evaluateStraight(Hand hand, Map<Rank, Integer> rankFrequency)
    {
        // A STRAIGHT must have 5 different entries in the rank frequency table,
        // as there are no pairs involved.
        if (rankFrequency.size() != GameConstants.CARDS_IN_HAND)
        {
            return null;
        }

        // Next, we need to sort the cards in ascending order, based on their rank value,
        // and verify that the ranks are sequential.
        List<Card> cards = hand.getCards();
        sortAscending(cards);

        if (!isSequential(cards))
        {
            return null;
        }

        PokerHandEvaluation evaluation = new PokerHandEvaluation();
        evaluation.hand = hand;
        evaluation.category = Category.STRAIGHT;
        evaluation.highestRank = cards.get(cards.size() - 1).getRank();

        return evaluation;
    }

    /**
     * Determines whether or not the Hand represents the category THREE_OF_A_KIND.
     *
     * @param hand          The Hand object containing the Card list that will
     *                      be used to determine if it represents the THREE_OF_A_KIND
     *                      category.
     * @param rankFrequency Map associating the Rank with the number of times
     *                      it appears in the hand.
     *
     * @return A PokerHandEvaluation object containing the results of the evaluation
     *         if the Hand represents the THREE_OF_A_KIND category, null otherwise.
     */
    private PokerHandEvaluation evaluateThreeOfAKind(Hand hand, Map<Rank, Integer> rankFrequency)
    {
        // The fingerprint for a THREE_OF_A_KIND hand is 3 entries in the rank frequency map.
        // However, this fingerprint is not unique, as the following hands have it:
        //      - TWO_PAIR
        //      - THREE_OF_A_KIND
        if (rankFrequency.size() != 3)
        {
            return null;
        }

        // Verify that the rank frequency of a card is 3, otherwise this hand
        // would be a TWO_PAIR.
        Rank highestRank = null;

        for (Map.Entry<Rank, Integer> entry : rankFrequency.entrySet())
        {
            Rank rank = entry.getKey();
            int frequency = entry.getValue();

            // This is a TWO_PAIR hand, abort
            if (frequency == 2)
            {
                return null;
            }

            if (frequency == 3)
            {
                highestRank = rank;
                break;
            }
        }

        PokerHandEvaluation evaluation = new PokerHandEvaluation();
        evaluation.hand = hand;
        evaluation.category = Category.THREE_OF_A_KIND;
        evaluation.highestRank = highestRank;

        return evaluation;
    }

    /**
     * Determines whether or not the Hand represents the category TWO_PAIR.
     *
     * @param hand          The Hand object containing the Card list that will
     *                      be used to determine if it represents the TWO_PAIR
     *                      category.
     * @param rankFrequency Map associating the Rank with the number of times
     *                      it appears in the hand.
     *
     * @return A PokerHandEvaluation object containing the results of the evaluation
     *         if the Hand represents the TWO_PAIR category, null otherwise.
     */
    private PokerHandEvaluation evaluateTwoPair(Hand hand, Map<Rank, Integer> rankFrequency)
    {
        // The fingerprint for a TWO_PAIR hand is 3 entries in the rank frequency map.
        // This fingerprint is not unique, as the following hands have it:
        //      - TWO_PAIR
        //      - THREE_OF_A_KIND
        // Note however that if this were a THREE_OF_A_KIND hand, it would not have
        // reached this weaker hand evaluating function.
        if (rankFrequency.size() != 3)
        {
            return null;
        }

        Rank highestRank = null;

        for (Map.Entry<Rank, Integer> entry : rankFrequency.entrySet())
        {
            Rank rank = entry.getKey();
            int frequency = entry.getValue();
            boolean isPair = frequency > 1;

            if (isPair && (highestRank == null || rank.getValue() > highestRank.getValue()))
            {
                highestRank = rank;
            }
        }

        PokerHandEvaluation evaluation = new PokerHandEvaluation();
        evaluation.hand = hand;
        evaluation.category = Category.TWO_PAIR;
        evaluation.highestRank = highestRank;

        return evaluation;
    }

    /**
     * Determines whether or not the Hand represents the category ONE_PAIR.
     *
     * @param hand          The Hand object containing the Card list that will
     *                      be used to determine if it represents the ONE_PAIR
     *                      category.
     * @param rankFrequency Map associating the Rank with the number of times
     *                      it appears in the hand.
     *
     * @return A PokerHandEvaluation object containing the results of the evaluation
     *         if the Hand represents the ONE_PAIR category, null otherwise.
     */
    private PokerHandEvaluation evaluateOnePair(Hand hand, Map<Rank, Integer> rankFrequency)
    {
        // The fingerprint of a ONE_PAIR hand is 4 entries in the rankFrequency map.
        // E.g., the hand [3][3][7][K][A] will have three entries with a frequency of 1,
        // and one entry with a frequency of 2, totaling 4 entries. No other hand
        // combination will have this very distinct fingerprint.
        if (rankFrequency.size() != 4)
        {
            return null;
        }

        PokerHandEvaluation evaluation = new PokerHandEvaluation();
        evaluation.hand = hand;
        evaluation.category = Category.ONE_PAIR;

        // Find pair
        for (Map.Entry<Rank, Integer> entry : rankFrequency.entrySet())
        {
            Rank rank = entry.getKey();
            int frequency = entry.getValue();

            if (frequency == 2)
            {
                evaluation.highestRank = rank;
                break;
            }
        }

        return evaluation;
    }

    /**
     * Iterates through the Card objects in a given Hand and determines the
     * highest ranking one. Note that this function will always return a
     * PokerHandEvaluation object, as all Hand objects will have their own
     * highest ranking card.
     *
     * @param hand The Hand object containing the Card list from which the
     *             highest ranking Card needs to be determined.
     *
     * @return A PokerHandEvaluation object containing the results of the evaluation.
     */
    private PokerHandEvaluation evaluateHighCard(Hand hand)
    {
        PokerHandEvaluation evaluation = new PokerHandEvaluation();
        evaluation.hand = hand;
        evaluation.category = Category.HIGH_CARD;
        evaluation.highestRank = findHighestRank(hand.getCards());


        // If evaluation  made it all the way to this function, there is
        // no other Category than HIGH_CARD. In this case, we will sort
        // the cards in descending order, add


        return evaluation;
    }

    /**
     * Given an list of Card objects sorted in ascending order, it
     * determines whether or not the ranks are in sequence with no
     * value gaps between them.
     *
     * @param cards The list of Card objects sorted in ascending order.
     *
     * @return True if the ranks in the list are sequential with no
     *         gaps between them, false otherwise.
     */
    private boolean isSequential(List<Card> cards)
    {
        for (int i = 0; i < cards.size() - 2; i++)
        {
            Card curHand = cards.get(i);
            Card nextHand = cards.get(i + 1);

            // If there is a gap, this is not a straight
            if (curHand.getRank().getValue() != nextHand.getRank().getValue() - 1)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns true if a Card object contained in the Card list has the
     * same rank and suit combination as another card in the same array.
     *
     * @param cards The list of Card objects that make up this hand.
     *
     * @return true if a duplicate card exists, false otherwise.
     */
    private boolean hasDuplicates(List<Card> cards)
    {
        int totalCards = cards.size();

        Map<String, Card> frequency = new HashMap<String, Card>(totalCards);

        for (Card card : cards)
        {
            String key = card.toString();

            if (frequency.containsKey(key))
            {
                return true;
            }
            else
            {
                frequency.put(key, card);
            }
        }

        return false;
    }

    /**
     * Given a list of Card objects, it iterates through them
     * to find the Card with the highest rank among them.
     *
     * @param cards The list of Card objects from where the highest
     *              rank needs to be identified from.
     *
     * @return The highest Rank found in the Card list.
     *
     */
    private Rank findHighestRank(List<Card> cards)
    {
        Rank highestRank = null;

        for (Card card : cards)
        {
            Rank rank = card.getRank();

            if (highestRank == null || rank.getValue() > highestRank.getValue())
            {
                highestRank = rank;
            }
        }

        return highestRank;
    }

    /**
     * Given a list of Card objects, it sorts the Cards in ascending order
     * based on the Card's rank value.
     *
     * @param cards The list of Card objects to sort.
     */
    private void sortAscending(List<Card> cards)
    {
        Collections.sort(cards, new CardRankComparator(false));
    }

    /**
     * Given a list of Card objects, it sorts the Cards in descending order
     * based on the Card's rank value.
     *
     * @param cards The list of Card objects to sort.
     */
    private void sortDescending(List<Card> cards)
    {
        Collections.sort(cards, new CardRankComparator(true));
    }

    /**
     * The CardRankComparator helper class compares two cards based on
     * their rank value.
     */
    private class CardRankComparator implements Comparator<Card>
    {
        private boolean descending;

        public CardRankComparator(boolean descending)
        {
            this.descending = descending;
        }

        public int compare(Card card1, Card card2)
        {
            Integer rank1 = card1.getRank().getValue();
            Integer rank2 = card2.getRank().getValue();
            return descending ? rank2.compareTo(rank1) : rank1.compareTo(rank2);
        }
    }
}
