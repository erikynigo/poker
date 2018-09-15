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
 * The PokerHandEvaluator class takes a List of two or more Hand
 * objects, evaluates them to identify the category achieved by
 * each Hand as well astheir score, and determines the strongest
 * hand based on standard poker hand ranking.
 *
 * For more information on categories and hand strength, see:
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

    /**
     * Determines the strongest Hand out of a list of Hand objects.
     *
     * @param hands The list of Hand objects that are to be evaluated
     *              to determine the winning Hand.
     *
     * @return A List of PokerHandEvaluation objects.
     *
     * @throws CannotEvaluateException  Thrown when the Hands list is null or has
     *                                  less than 2 Hands.
     * @throws IllegalHandException     Thrown when a Hand does not meet the requirements
     *                                  to be deemed a valid Hand, such as not having 5
     *                                  cards, or having duplicate cards
     */
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

        // Hands are valid poker hands. Group Hands in a map according to their score, where:
        //      - key is the score achieved
        //      - value is the list containing all PokerHandEvaluation objects with the same score
        int totalHands = hands.size();
        Map<Float, List<PokerHandEvaluation>> scoreToEvaluationsMap =
            new HashMap<Float, List<PokerHandEvaluation>>(totalHands);

        // Highest score will be the key to retrieve the list of winners from the map
        Float highestScore = 0.0f;

        for (Hand hand : hands)
        {
            PokerHandEvaluation evaluation = evaluate(hand);
            Float score = evaluation.getScore();

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
     * Given a Hand, this function will determine the highest ranking Category
     * that the hand achieved. A transfer object is returned where it contains
     * the Hand evaluated, the Category achieved, list of ordered kickers to be
     * used as tie breakers, the category achieved, and a unique score that
     * represents the strenght of the Hand, which can be used to compare Hands
     * against each other.
     *
     * @param hand The Hand object to evaluate.
     *
     * @return A PokerHandEvaluation object containing the Hand evaluated, the
     *         Category achieved, kickers to be used, and a unique score.
     */
    private PokerHandEvaluation evaluate(Hand hand)
    {
        List<Card> cards = hand.getCards();
        int totalCards = cards.size();

        Map<Rank, List<Card>> rankFrequency = new HashMap<Rank, List<Card>>(totalCards);
        Map<Suit, List<Card>> suitFrequency = new HashMap<Suit, List<Card>>(totalCards);

        // Generate frequency tables
        for (Card card : cards)
        {
            // Rank frequency can help us narrow down the possible hand categories
            // contained in a Hand. E.g., If there are 2 entries for rank, then
            // the categories FOUR_OF_A_KIND and FULL_HOUSE are now possible, while
            // TWO_PAIR, STRAIGHT, and THREE_OF_A_KIND are no longer possible.
            Rank rank = card.getRank();
            if (!rankFrequency.containsKey(rank))
            {
                rankFrequency.put(rank, new ArrayList<Card>(totalCards));
            }
            rankFrequency.get(rank).add(card);

            // Suit frequency can also help narrow down the possible hand categories.
            // E.g., if there is only one entry, then we know right away that the only
            // possible categories now would be FLUSH or STRAIGHT_FLUSH.
            Suit suit = card.getSuit();
            if (!suitFrequency.containsKey(suit))
            {
                suitFrequency.put(suit, new ArrayList<Card>(totalCards));
            }
            suitFrequency.get(suit).add(card);
        }

        // Evaluate possible hands, from highest to lowest. Stop when evaluation is not
        // null, as it will be the highest ranked hand category the Hand contains.
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
     * @param rankFrequency Map associating the Rank with the Card objects in the
     *                      hand that share the same Rank.
     * @param suitFrequency Map associating the Suit with the Card objects in the
     *                      hand that bear the same Suit.
     *
     * @return A PokerHandEvaluation object containing the results of the evaluation
     *         if the Hand represents the STRAIGHT_FLUSH category, null otherwise.
     */
    private PokerHandEvaluation evaluateStraightFlush(Hand hand, Map<Rank, List<Card>> rankFrequency,
        Map<Suit, List<Card>> suitFrequency)
    {
        // The fingerprint for a STRAIGHT_FLUSH hand is very particular:
        //      - 5 entries in the rank frequency map
        //      - 1 entry in the suit frequency map
        if (rankFrequency.size() != 5 || suitFrequency.size() != 1)
        {
            return null;
        }

        // Next, we need to sort the cards in ascending order, based on their Rank
        // value, and verify that the Card objects are sequential.
        List<Card> cards = hand.getCards();
        sortDescending(cards);

        if (!isSequential(cards, true))
        {
            return null;
        }

        PokerHandEvaluation evaluation = new PokerHandEvaluation();
        evaluation.hand = hand;
        evaluation.category = Category.STRAIGHT_FLUSH;
        evaluation.kickers = cards;

        return evaluation;
    }

    /**
     * Determines whether or not the Hand represents the category FOUR_OF_A_KIND.
     *
     * @param hand          The Hand object containing the Card list that will
     *                      be used to determine if it represents the FOUR_OF_A_KIND
     *                      category.
     * @param rankFrequency Map associating the Rank with the Card objects in the
     *                      hand that share the same Rank.
     *
     * @return A PokerHandEvaluation object containing the results of the evaluation
     *         if the Hand represents the FOUR_OF_A_KIND category, null otherwise.
     */
    private PokerHandEvaluation evaluateFourOfAKind(Hand hand, Map<Rank, List<Card>> rankFrequency)
    {
        // The fingerprint for a FOUR_OF_A_KIND hand is 2 entries in the rank frequency map.
        // This fingerprint is not unique, as the following hands have it:
        //      - FOUR_OF_A_KIND
        //      - FULL_HOUSE
        if (rankFrequency.size() != 2)
        {
            return null;
        }

        // Find four of a kind
        List<Card> quartetList = new ArrayList<Card>(4);
        Card remainingCard = null;

        for (Map.Entry<Rank, List<Card>> entry : rankFrequency.entrySet())
        {
            List<Card> sameRankCards = entry.getValue();
            int frequency = sameRankCards.size();

            // This is a full house hand, abort.
            if (frequency == 2 || frequency == 3)
            {
                return null;
            }
            else if (frequency == 4)
            {
                quartetList.addAll(sameRankCards);
            }
            else
            {
                remainingCard = sameRankCards.get(0);
            }
        }

        quartetList.add(remainingCard);

        PokerHandEvaluation evaluation = new PokerHandEvaluation();
        evaluation.hand = hand;
        evaluation.category = Category.FOUR_OF_A_KIND;
        evaluation.kickers = quartetList;

        return evaluation;
    }

    /**
     * Determines whether or not the Hand represents the category FULL_HOUSE.
     *
     * @param hand          The Hand object containing the Card list that will
     *                      be used to determine if it represents the FULL_HOUSE
     *                      category.
     * @param rankFrequency Map associating the Rank with the Card objects in the
     *                      hand that share the same Rank.
     *
     * @return A PokerHandEvaluation object containing the results of the evaluation
     *         if the Hand represents the FULL_HOUSE category, null otherwise.
     */
    private PokerHandEvaluation evaluateFullHouse(Hand hand, Map<Rank, List<Card>> rankFrequency)
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

        PokerHandEvaluation evaluation = new PokerHandEvaluation();
        evaluation.hand = hand;
        evaluation.category = Category.FULL_HOUSE;

        // Find triple and pair
        List<Card> tripleList = new ArrayList<Card>(3);
        List<Card> pairList = new ArrayList<Card>(2);

        for (Map.Entry<Rank, List<Card>> entry : rankFrequency.entrySet())
        {
            List<Card> sameRankCards = entry.getValue();
            int frequency = sameRankCards.size();

            if (frequency == 3)
            {
                tripleList.addAll(sameRankCards);
            }
            else
            {
                pairList.addAll(sameRankCards);
            }
        }

        tripleList.addAll(pairList);
        evaluation.kickers = tripleList;

        return evaluation;
    }

    /**
     * Determines whether or not the Hand represents the category FULL_HOUSE.
     *
     * @param hand          The Hand object containing the Card list that will
     *                      be used to determine if it represents the FULL_HOUSE
     *                      category.
     * @param suitFrequency Map associating the Suit with the Card objects in the
     *                      hand that bear the same Suit.
     *
     * @return A PokerHandEvaluation object containing the results of the evaluation
     *         if the Hand represents the FULL_HOUSE category, null otherwise.
     */
    private PokerHandEvaluation evaluateFlush(Hand hand, Map<Suit, List<Card>> suitFrequency)
    {
        // A FLUSH should have one entry in the suit frequency map only,
        // as all cards must be of the same suit.
        if (suitFrequency.size() > 1)
        {
            return null;
        }

        sortDescending(hand.getCards());

        PokerHandEvaluation evaluation = new PokerHandEvaluation();
        evaluation.hand = hand;
        evaluation.category = Category.FLUSH;
        evaluation.kickers = hand.getCards();

        return evaluation;
    }

    /**
     * Determines whether or not the Hand represents the category STRAIGHT.
     *
     * @param hand          The Hand object containing the Card list that will
     *                      be used to determine if it represents the STRAIGHT
     *                      category.
     * @param rankFrequency Map associating the Rank with the Card objects in the
     *                      hand that share the same Rank.
     *
     * @return A PokerHandEvaluation object containing the results of the evaluation
     *         if the Hand represents the STRAIGHT category, null otherwise.
     */
    private PokerHandEvaluation evaluateStraight(Hand hand, Map<Rank, List<Card>> rankFrequency)
    {
        // A STRAIGHT must have 5 different entries in the rank frequency table,
        // as there are no pairs involved.
        if (rankFrequency.size() != GameConstants.CARDS_IN_HAND)
        {
            return null;
        }

        // Next, we need to sort the cards in descending order (so they can also be used as
        // the kickers) based on their rank value, and verify that the ranks are sequential.
        List<Card> cards = hand.getCards();

        // Straights have a special case, where the ACE can either start a straight
        // as 1, or finish it after KING. To handle this, let's see if there is an
        // ACE in the hand, extract it, and sort the remaining 4 cards. Then we can
        // check if an ace can be inserted at the beggining or at the end of the
        // sorted list of cards
        Card ace = null;
        Iterator<Card> iterator = cards.iterator();
        while (iterator.hasNext())
        {
            Card card = iterator.next();
            if (card.getRank() == Rank.ACE)
            {
                ace = card;
                iterator.remove();
                break;
            }
        }

        sortDescending(cards);

        if (!isSequential(cards, true))
        {
            // Reinsert the ace if removed
            if (ace != null)
            {
                cards.add(ace);
            }

            return null;
        }

        if (ace != null)
        {
            // Ace can finish a straight
            if (cards.get(0).getRank() == Rank.KING)
            {
                cards.add(0, ace);
            }
            // Ace can start a straight
            else if (cards.get(cards.size() - 1).getRank() == Rank.TWO)
            {
                cards.add(ace);
            }
            // Not really a straight as the ace that we removed is not in sequence
            else
            {
                cards.add(ace);
                return null;
            }
        }

        PokerHandEvaluation evaluation = new PokerHandEvaluation();
        evaluation.hand = hand;
        evaluation.category = Category.STRAIGHT;
        evaluation.kickers = cards;

        return evaluation;
    }

    /**
     * Determines whether or not the Hand represents the category THREE_OF_A_KIND.
     *
     * @param hand          The Hand object containing the Card list that will
     *                      be used to determine if it represents the THREE_OF_A_KIND
     *                      category.
     * @param rankFrequency Map associating the Rank with the Card objects in the
     *                      hand that share the same Rank.
     *
     * @return A PokerHandEvaluation object containing the results of the evaluation
     *         if the Hand represents the THREE_OF_A_KIND category, null otherwise.
     */
    private PokerHandEvaluation evaluateThreeOfAKind(Hand hand, Map<Rank, List<Card>> rankFrequency)
    {
        // The fingerprint for a THREE_OF_A_KIND hand is 3 entries in the rank frequency map.
        // However, this fingerprint is not unique, as the following hands have it:
        //      - TWO_PAIR
        //      - THREE_OF_A_KIND
        if (rankFrequency.size() != 3)
        {
            return null;
        }

        PokerHandEvaluation evaluation = new PokerHandEvaluation();
        evaluation.hand = hand;
        evaluation.category = Category.THREE_OF_A_KIND;

        // Find triple
        List<Card> tripleList = new ArrayList<Card>(3);
        List<Card> remainingCards = new ArrayList<Card>(2);

        for (Map.Entry<Rank, List<Card>> entry : rankFrequency.entrySet())
        {
            List<Card> sameRankCards = entry.getValue();
            int frequency = sameRankCards.size();

            // This is a TWO_PAIR hand, abort
            if (frequency == 2)
            {
                return null;
            }
            else if (frequency == 3)
            {
                tripleList.addAll(sameRankCards);
            }
            else
            {
                remainingCards.addAll(sameRankCards);
            }
        }

        sortDescending(remainingCards);
        tripleList.addAll(remainingCards);
        evaluation.kickers = tripleList;

        return evaluation;
    }

    /**
     * Determines whether or not the Hand represents the category TWO_PAIR.
     *
     * @param hand          The Hand object containing the Card list that will
     *                      be used to determine if it represents the TWO_PAIR
     *                      category.
     * @param rankFrequency Map associating the Rank with the Card objects in the
     *                      hand that share the same Rank.
     *
     * @return A PokerHandEvaluation object containing the results of the evaluation
     *         if the Hand represents the TWO_PAIR category, null otherwise.
     */
    private PokerHandEvaluation evaluateTwoPair(Hand hand, Map<Rank, List<Card>> rankFrequency)
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

        PokerHandEvaluation evaluation = new PokerHandEvaluation();
        evaluation.hand = hand;
        evaluation.category = Category.TWO_PAIR;

        // Find pairs
        List<Card> pairList = new ArrayList<Card>(4);
        Card remainingCard = null;

        for (Map.Entry<Rank, List<Card>> entry : rankFrequency.entrySet())
        {
            List<Card> sameRankCards = entry.getValue();
            boolean isPair = sameRankCards.size() == 2;

            if (isPair)
            {
                pairList.addAll(sameRankCards);
            }
            else
            {
                remainingCard = sameRankCards.get(0);
            }
        }

        sortDescending(pairList);
        pairList.add(remainingCard);
        evaluation.kickers = pairList;

        return evaluation;
    }

    /**
     * Determines whether or not the Hand represents the category ONE_PAIR.
     *
     * @param hand          The Hand object containing the Card list that will
     *                      be used to determine if it represents the ONE_PAIR
     *                      category.
     * @param rankFrequency Map associating the Rank with the Card objects in the
     *                      hand that share the same Rank.
     *
     * @return A PokerHandEvaluation object containing the results of the evaluation
     *         if the Hand represents the ONE_PAIR category, null otherwise.
     */
    private PokerHandEvaluation evaluateOnePair(Hand hand, Map<Rank, List<Card>> rankFrequency)
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
        List<Card> pairList = null;
        List<Card> remainingCards = new ArrayList<Card>(3);

        for (Map.Entry<Rank, List<Card>> entry : rankFrequency.entrySet())
        {
            List<Card> sameRankCards = entry.getValue();
            int frequency = sameRankCards.size();

            if (frequency == 2)
            {
                pairList = entry.getValue();
            }
            else
            {
                // We know there is only one element in each other rank,
                // otherwise the signature would be different and would
                // not have hit this function.
                remainingCards.add(sameRankCards.get(0));
            }
        }

        sortDescending(remainingCards);
        pairList.addAll(remainingCards);
        evaluation.kickers = pairList;

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

        // If evaluation  made it all the way to this function, there is
        // no other Category than HIGH_CARD. In this case, we will sort
        // the cards in descending order and add them as kickers.
        // Kickers determine the outcome when two hands are of the same
        // category.
        sortDescending(hand.getCards());
        evaluation.kickers = hand.getCards();

        return evaluation;
    }

    /**
     * Given an list of Card objects sorted in ascending order, it
     * determines whether or not the ranks are in sequence with no
     * value gaps between them.
     *
     * @param cards      The list of Card objects sorted in ascending order.
     * @param descending Whether or not the sequence is in descending
     *                   or ascending order
     *
     * @return True if the ranks in the list are sequential with no
     *         gaps between them, false otherwise.
     */
    private boolean isSequential(List<Card> cards, boolean descending)
    {
        int delta = descending ? 1 : -1;

        for (int i = 0; i < cards.size() - 1; i++)
        {
            Card curHand = cards.get(i);
            Card nextHand = cards.get(i + 1);

            // If there is a gap, this is not a straight
            if (curHand.getRank().getValue() != nextHand.getRank().getValue() + delta)
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
