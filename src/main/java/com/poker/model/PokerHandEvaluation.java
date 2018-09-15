package com.poker.model;

import com.poker.metadata.Category;
import com.poker.metadata.GameConstants;
import com.poker.metadata.Rank;
import com.poker.metadata.Suit;

import java.util.List;

/**
 * The PokerHandEvaluation class is meant to be a transfer object that contains
 * data related to the evaluation of a Hand object. It includes the Hand
 * object that was evaluated, the highest ranking Category achieved by the
 * hand, the ordered list of applying kickers relative to the category achieved,
 * and a unique score that is assigned to the hand to make evaluation between
 * PokerHandEvaluation objects easier and objective. This score is determined by
 * adding up the score of the Category plus the value of each kicker according
 * to its position.
 *
 * @author Erik Ynigo
 */
public class PokerHandEvaluation
{
    // The hand this score is representing
    public Hand hand;

    // The highest ranking category the hand achieved
    public Category category;

    // Kickers is the list of cards ordered from strongest to weakest order, in
    // the context of the category achieved. For example:
    // Hand: [5][2][A][J][K]    Category: HIGH_CARD     Kickers: [A][K][J][5][2]
    // Hand: [A][3][K][3][Q]    Category: ONE_PAIR      Kickers: [3][3][A][K][Q]
    // If two hands are of the same category, kickers will decide the tie breaker.
    public List<Card> kickers;

    /**
     * Returns a unique numerical score achieved by the evaluated Hand, that can be
     * used to compare one Hand against another. The higher scoring hand is
     * guaranteed to be the strongest hand.
     *
     * @return A float representing the unique score achieved by the evaluated Hand.
     */
    public float getScore()
    {
        if (category == null || kickers == null || kickers.size() != GameConstants.CARDS_IN_HAND)
        {
            return 0.0f;
        }

        // Base score is the category
        float score = category.getValue();

        // Add kicker scores
        // The position of the kicker is extremely important, as it's relative to
        // the context of the category achieved. Earlier kicker positions are
        // multiplied by greater multipliers, and the multiplier is reduced as
        // we moved along the kickers list. These kicker scores are summed between
        // them and the category score, creating a completely unique numerical score.
        // This way, the score will only be the same if the hands are the same (suits
        // do not influence the score)
        for (int i = 0; i < kickers.size(); i++)
        {
            int kickerRankValue = kickers.get(i).getRank().getValue();
            float kickerScore = kickerRankValue * GameConstants.KICKER_POSITION_MULTIPLIER[i];
            score += kickerScore;
        }

        return score;
    }

    /**
     * Returns a String that includes all results in this evaluation,
     * such as the hand evaluated, the category achieved, the order of
     * the kickers used, and the hand's unique score.
     *
     * @return A String representing the results of this evaluation.
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("Hand: ");

        for (Card card : hand.getCards())
        {
            sb.append(getSimplifiedRank(card.getRank()));
            sb.append(getSimplifiedSuit(card.getSuit()));
            sb.append(" ");
        }

        sb.append("\nCategory: ").append(category.toString());
        sb.append("\nKickers: ");

        if (kickers != null)
        {
            for (Card card : kickers)
            {
                sb.append(getSimplifiedRank(card.getRank()));
                sb.append(getSimplifiedSuit(card.getSuit()));
                sb.append(" ");
            }
        }

        sb.append("\nScore: ").append(getScore());

        return sb.toString();
    }

    /**
     * Returns a one-character representation of a Rank, to be used when
     * converting a PokerHandEvaluation object into a String via the
     * toString() function.
     *
     * @param rank The Rank object we need a one-character representation of.
     *
     * @return A String representing a Rank.
     */
    private String getSimplifiedRank(Rank rank)
    {
        return GameConstants.SIMPLIFIED_RANK[rank.getValue()];
    }

    /**
     * Returns a one-character representation of a Suit, to be used when
     * converting a PokerHandEvaluation object into a String via the
     * toString() function.
     *
     * @param suit The Suit object we need a one-character representation of.
     *
     * @return A String representing a Suit.
     */
    private String getSimplifiedSuit(Suit suit)
    {
        if (suit == Suit.CLUB)
        {
            return "♣";
        }
        else if (suit == Suit.HEART)
        {
            return "♥";
        }
        else if (suit == Suit.SPADE)
        {
            return "♠";
        }
        else if (suit == Suit.DIAMOND)
        {
            return "♦";
        }

        return "?";
    }
}
